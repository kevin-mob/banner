package kevinmob.bannerdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.util.ArrayList;
import java.util.List;

import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.ImageLoader;
import cn.kevin.banner.BannerAdapter;
import cn.kevin.banner.transformer.YZoomTransFormer;

public class MainActivity extends AppCompatActivity {
    BannerViewPager bannerViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerViewPager = (BannerViewPager) findViewById(R.id.bvp);
        BannerAdapter adapter = new BannerAdapter(new GlideImageLoader());
        List<IBannerItem> list = new ArrayList<>();
        list.add(new BannerItem("https://cdn.pixabay.com/photo/2017/12/10/20/56/feather-3010848_1280.jpg"));
        list.add(new BannerItem("https://cdn.pixabay.com/photo/2016/06/15/01/21/strauss-spring-1458012_1280.jpg"));
        list.add(new BannerItem("https://cdn.pixabay.com/photo/2016/08/16/16/15/feather-1598302_1280.jpg"));
        list.add(new BannerItem("https://cdn.pixabay.com/photo/2016/08/16/16/19/feather-1598311_1280.jpg"));
        adapter.setData(this, list);
        bannerViewPager.setBannerAdapter(adapter);
        bannerViewPager.setBannerItemClick(new BannerViewPager.OnBannerItemClick<IBannerItem>() {
            @Override
            public void onClick(IBannerItem data) {
                Toast.makeText(MainActivity.this, " data.ImageUrl() " + data.ImageUrl(), Toast.LENGTH_SHORT).show();
            }
        });
        bannerViewPager.setPageTransformer(true, new YZoomTransFormer(.8f));
    }

    class GlideImageLoader implements ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .bitmapTransform(new CropRoundTransFormation(MainActivity.this, dp2px(4)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .into(imageView);
        }
    }


    public int dp2px(int dpValue){
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics()) + .5f);
    }

    public class CropRoundTransFormation implements Transformation<Bitmap> {
        private BitmapPool mBitmapPool;
        private int mRadius;

        CropRoundTransFormation(Context context, int radius){
            this(Glide.get(context).getBitmapPool());
            mRadius = radius;
        }

        public CropRoundTransFormation(BitmapPool pool) {
            this.mBitmapPool = pool;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();

            Bitmap bitmap = mBitmapPool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader =
                    new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

            if (source.getWidth() != outWidth || source.getHeight() != outHeight) {
                //缩放局中
                final float scale;
                float dx = 0, dy = 0;
                Matrix m = new Matrix();
                if (source.getWidth() * outHeight > outWidth * source.getHeight()) {
                    scale = (float) outHeight / (float) source.getHeight();
                    dx = (outWidth - source.getWidth() * scale) * 0.5f;
                } else {
                    scale = (float) outWidth / (float) source.getWidth();
                    dy = (outHeight - source.getHeight() * scale) * 0.5f;
                }
                m.setScale(scale, scale);
                m.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
                shader.setLocalMatrix(m);
            }

            paint.setShader(shader);
            paint.setAntiAlias(true);

            canvas.drawRoundRect(new RectF(0, 0, outWidth, outHeight), mRadius, mRadius, paint);

            return BitmapResource.obtain(bitmap, mBitmapPool);
        }

        @Override
        public String getId() {
            return "CropRoundTransFormation()" + mRadius;
        }
    }
}
