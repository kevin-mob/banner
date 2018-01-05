package cn.kevin.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author like
 * @date 2018/1/5
 */
public abstract class BaseViewAdapter<T> extends PagerAdapter {

    private List<T> itemList = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private int indexCount;
    private static final int MIN_ITEM_COUNT = 6;

    public void setData(Context context, List<T> items){
        indexCount = items.size();
        itemList.clear();
        while(itemList.size() < MIN_ITEM_COUNT){
            itemList.addAll(items);
        }
        createView(context, itemList);
    }

    private void createView(Context context, List<T> items) {
        views.clear();
        for (int i = 0; i < items.size(); i ++){
            views.add(createView(context, items.get(i)));
        }
    }

    protected abstract View createView(Context context, T item);

    /**
     * 根据在viewpager中的位置获取在真实内容item
     * @param position viewpager中的位置
     * @return
     */
    T getItem(int position){
        return itemList.get(getItemListPosition(position));
    }


    /**
     * 获取实际内容的数量
     * @return 内容数量
     */
    public int getIndexCount(){
        return indexCount;
    }

    /**
     * 根据在viewpager中的位置获取在真实内容中的position
     * @param position viewpager中的位置
     * @return 真实内容中的position
     */
    int getIndexPosition(int position){
        return position % getIndexCount();
    }

    private int getItemListPosition(int position){
        return position % itemList.size();
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {}

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = getItemListPosition(position);
        View v = views.get(realPosition);
        if (null != v.getParent())
            ((ViewGroup)v.getParent()).removeView(v);

        try {
            container.addView(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
}
