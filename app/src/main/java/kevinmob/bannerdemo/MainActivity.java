package kevinmob.bannerdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.util.ArrayList;
import java.util.List;

import kevinmob.banner.BannerAdapter;
import kevinmob.banner.BannerViewPager;
import kevinmob.banner.IBannerItem;
import kevinmob.banner.ImageLoader;

public class MainActivity extends AppCompatActivity {
    BannerViewPager bannerViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerViewPager = (BannerViewPager) findViewById(R.id.bvp);
        BannerAdapter adapter = new BannerAdapter();
        List<BannerItem> list = new ArrayList<>();
        list.add(new BannerItem("http://img.mukewang.com/597058780001221707500250.jpg"));
        list.add(new BannerItem("http://img.mukewang.com/5970589100012c0107500250.jpg"));
        list.add(new BannerItem("http://img.mukewang.com/596da0790001dee207500250.jpg"));
        list.add(new BannerItem("http://img.mukewang.com/5965c6350001556207500250.jpg"));
        list.add(new BannerItem("http://img.mukewang.com/59658a5400011c3007500250.jpg"));
        list.add(new BannerItem("http://img.mukewang.com/5962dfbc0001c8bb07500250.jpg"));
        adapter.setData(this, list);
        adapter.setImageLoader(new GlideImageLoader());
        bannerViewPager.setBannerAdapter(adapter);
        bannerViewPager.setBannerItemClick(new BannerViewPager.IBannerItemClick() {
            @Override
            public void onClick(IBannerItem data) {
                Toast.makeText(MainActivity.this, " data.ImageUrl() " + data.ImageUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class GlideImageLoader implements ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .bitmapTransform(new CropRoundTransFormation(MainActivity.this, dp2px(4)))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//
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
            return "CropRoundTransFormation()";
        }
    }
}
