# SlidingLayout

SlidingLayout是一种Android平台的View控件，可以帮助你实现类似微信网页浏览的下拉功能，也可以帮助你实现类似iOS中UITableView的下拉上拉弹跳的果冻效果。

SlidingLayout完美兼容Android自带库和兼容库的所有View组件，包括RecyclerView、ListView、ScrollView以及WebView等等。

SlidingLayout简单易用，最低支持Android api v9。

Project site： <https://github.com/HomHomLin/SlidingLayout>.

Demo: <https://github.com/HomHomLin/SlidingLayout/tree/master/demo>.

最新版本:v0.9.0

## 效果图：

![p1](https://raw.githubusercontent.com/HomHomLin/SlidingLayout/master/pic/demo.gif)

![p2](https://raw.githubusercontent.com/HomHomLin/SlidingLayout/master/pic/list.gif)

![p3](https://raw.githubusercontent.com/HomHomLin/SlidingLayout/master/pic/webview.gif)

## 导入项目

**Gradle dependency:**
``` groovy
compile 'homhomlin.lib:sldinglayout:0.9.0'
```

or

**Maven dependency:**
``` xml
<dependency>
  <groupId>homhomlin.lib</groupId>
  <artifactId>sldinglayout</artifactId>
  <version>0.9.0</version>
</dependency>
```

**依赖:**

如果你的项目需要支持API V9，你需要添加以下依赖：

``` groovy
compile 'com.nineoldandroids:library:2.4.0'
```

## 用法

SlidingLayout的使用非常简单，你只需要将你想实现的控件在XML布局中嵌套进SlidingLayout即可，如你需要让ListView实现果冻效果：

### 1.创建背景View的xml

``` xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#8c8c8e"
    android:gravity="top"
    android:textSize="12sp"
    android:textColor="#f5f3f3"
    android:padding="16dp"
    android:text="developed by HomhomLin"/>
```

### 2.将你的控件放进SlidingLayout中

注意布局需要res-auto命名空间，注意将自己的控件设置一个背景，否则会将背景View透视出来。

```xml
<?xml version="1.0" encoding="utf-8"?>
<lib.homhomlib.design.SlidingLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/slidingLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:sliding_mode="both"
    app:background_view="@layout/view_bg">
    <!--background_view为你的背景布局-->
    <ListView
        android:id="@+id/listview"
        android:background="#ffffff"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </ListView>
</lib.homhomlib.design.SlidingLayout>
```

### 3.没有第三步啦！

运行即可看到效果！具体其他的内容可以看Demo。

## XML样式参数

 * `background_view` 背景view
 * `sliding_mode` 滑动模式，both为上下可弹跳，top为顶部弹跳，bottom为底部弹跳，默认为both
 * `sliding_pointer_mode` 手指模式，one为只识别一个手指，more为支持多指滑动，默认为more
 * `top_max` 当滑动模式为top时才有效，用于可滑动的最大距离，如"top_max:200dp"，默认为-1（不限制）

## 常用API

 * `public void setSlidingOffset(float slidingOffset)` 设置控件的滑动阻力，有效值为0.1F~1.0F，值越小阻力越大，默认为0.5F
 * `public void setTargetView(View view)` 设置控件的前景View
 * `public void setBackgroundView(View view)` 设置控件的背景View
 * `public void setSlidingListener(SlidingListener slidingListener)` 给控件设置监听，可以监听滑动情况
 * `public void setSlidingMode(int mode)` 设置滑动模式
 * `public void setSlidingDistance(int max)` 设置最大滑动距离，仅在top模式下有效

## Developed By

 * Linhonghong - <linhh90@163.com>

## License
Copyright 2016 LinHongHong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.