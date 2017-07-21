package kevinmob.bannerdemo;

import kevinmob.banner.IBannerItem;

/**
 * 创建日期：2017/7/18.
 *
 * @author yangjinhai
 */

public class BannerItem implements IBannerItem {
    String url;
    public BannerItem(String url){
        this.url = url;
    }
    @Override
    public String ImageUrl() {
        return url;
    }
}
