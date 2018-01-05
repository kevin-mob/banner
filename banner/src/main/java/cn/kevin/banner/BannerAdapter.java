package cn.kevin.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * 创建日期：2017/7/18.
 *
 * @author yangjinhai
 */
public class BannerAdapter extends BaseViewAdapter<IBannerItem> {
    private ImageLoader imageLoader;

    public BannerAdapter(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    protected View createView(Context context, IBannerItem item) {
        ImageView itemView = new ImageView(context);
        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(imageLoader != null) {
            imageLoader.onDisplayImage(context, itemView, item.ImageUrl());
        }
        return itemView;
    }
}
