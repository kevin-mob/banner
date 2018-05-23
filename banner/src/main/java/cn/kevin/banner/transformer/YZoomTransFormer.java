package cn.kevin.banner.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 *  垂直方向的page缩放转换器
 * @author yangjinhai
 * Date 2018/5/23
 */
public class YZoomTransFormer implements ViewPager.PageTransformer {
    float minScale;
    public YZoomTransFormer(float minScale) {
        this.minScale = minScale;
    }
    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {//看不到的一页 *
            //page.setScaleX(MIN_SCALE);
            page.setScaleY(minScale);
        } else if (position < 0) {//滑出的页 0.0 ~ -1 *
            float scaleFactor = (1 - minScale) * (0 - position);
            //page.setScaleX(1 - scaleFactor);
            page.setScaleY(1 - scaleFactor);
        } else if(position <= 1 ){//滑进的页 1 ~ 0.0 *
            float scaleFactor = (1 - minScale) * (1 - position);
            //page.setScaleX(MIN_SCALE + scaleFactor);
            page.setScaleY(minScale + scaleFactor);
        } else {//看不到的另一页 *
            //page.setScaleX(MIN_SCALE);
            page.setScaleY(minScale);
        }
    }
}
