# BannerViewPager
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/keviny-mob/maven/bannerview/images/download.svg) ](https://bintray.com/keviny-mob/maven/bannerview/_latestVersion)

轮播图控件

## 预览
![](demo.gif)


## 1.用法
Android Studio使用依赖:
```java
compile 'cn.kevin:bannerview:1.0.0'
```
或者下载工程后使用
```java
compile project(':banner')
```

## 2.功能
 * 实现广告轮播，通过设置参数，可以实现当前item、前一个、后一个同时显示的效果。

## 3.属性

<table>
  <tdead>
    <tr>
      <th align="center">自定义属性</th>
      <th align="center">含义</th>
    </tr>
  </tdead>
  <tbody>
    <tr>
      <td align="center">marginLeft</td>
      <td align="center">中心item距离整体控件左侧距离</td>
    </tr>
    <tr>
      <td align="center">marginRight</td>
      <td align="center">中心item距离整体控件右侧距离</td>
    </tr>
    <tr>
      <td align="center">item_margin</td>
      <td align="center">item与item的间距</td>
    </tr>
    <tr>
      <td align="center">around_visible</td>
      <td align="center">是否露出上一个和下一个item, 默认为true</td>
    </tr>
    <tr>
      <td align="center">point_drawable</td>
      <td align="center">页索引的drawle,默认为圆点， 为selector, 通过state_selected区分选中状态</td>
    </tr>
    <tr>
      <td align="center">point_gravity</td>
      <td align="center">页索引的显示位置，默认为bottom_center</td>
    </tr>
     </tbody>
</table>

## 4.代码演示 也可参考demo中的代码

### 1.在布局文件中加入BannerViewPager
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/viewPagerContainer"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <kevinmob.banner.BannerViewPager
        android:id="@+id/bvp"
        android:layout_width="200dp"
        android:layout_height="100dp"
        android:layout_marginTop="30dp"
        android:layerType="software"
        app:item_margin="20dp"
        app:marginLeft="40dp"
        app:marginRight="40dp"
        app:point_gravity="bottom_center" />
    </FrameLayout>
```

### 2.在java中设置数据
```java
    bannerViewPager = (BannerViewPager) findViewById(R.id.bvp);
	
    BannerAdapter adapter = new BannerAdapter();
    //item需要实现IBannerItem接口
    List<IBannerItem> list = new ArrayList<>();
    adapter.setData(context, list);

    adapter.setImageLoader(new GlideImageLoader());
    bannerViewPager.setBannerAdapter(adapter);
    bannerViewPager.setBannerItemClick(new BannerViewPager.IBannerItemClick() {
        @Override
        public void onClick(IBannerItem data) {
            Toast.makeText(MainActivity.this, " data.ImageUrl() " + data.ImageUrl(), Toast.LENGTH_SHORT).show();
        }
    });

    //这里举例为Glide,实际使用时需配合自己项目中的图片加载框架完成图片加载
    class GlideImageLoader implements ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).into(imageView);
        }
    }
```
