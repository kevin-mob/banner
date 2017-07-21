package kevinmob.banner;

import android.content.Context;
import android.widget.ImageView;

/**
 * 创建日期：2017/7/18.
 *
 * @author yangjinhai
 */

public interface ImageLoader {
    /**
     * 需要子类实现该方法，以确定如何加载和显示图片
     *
     * @param imageView 需要展示图片的ImageView
     * @param url       图片地址
     */
    void onDisplayImage(Context context, ImageView imageView, String url);
}
