# 自定义懒加载Fragment

[ ![Download](https://api.bintray.com/packages/maoqiqi/LazyLoad/lazyload/images/download.svg) ](https://bintray.com/maoqiqi/LazyLoad/lazyload/_latestVersion)
[ ![API](https://img.shields.io/badge/API-14%2B-blue.svg) ](https://developer.android.com/about/versions/android-4.0.html)
[ ![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg) ](http://www.apache.org/licenses/LICENSE-2.0)
[ ![Author](https://img.shields.io/badge/Author-March-orange.svg) ](fengqi.mao.march@gmail.com)

支持数据懒加载并且只加载一次，复用View和提供Fragment可见与不可见回调。


## 目录

* [Download](#Download)
* [Usage](#Usage)
* [Screenshot](#Screenshot)
* [Description](#Description)
* [About](#About)
* [License](#License)


## Download

Download [the JARs](https://jcenter.bintray.com/com/codearms/maoqiqi/lazyload) or configure this dependency:

```
implementation 'com.codearms.maoqiqi:lazyload:1.1.0'
```


## Usage

继承LazyLoadFragment抽象类，实现createView()方法，该方法返回一个视图。

方法说明

* initViews()：初始化控件。
* onVisibleChange()：提供Fragment可见与不可见回调。
* loadData()：支持数据懒加载并且只加载一次。
* setReuse()：设置是否需要复用View。
* setForcedToRefresh()：设置是否切换Fragment是否需要强制刷新数据。

例如：

```
public class TestFragment extends LazyLoadFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setReuse(false);
        setForcedToRefresh(true);
    }

    @Override
    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return null;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
    }

    @Override
    protected void onVisibleChange(boolean isVisible) {
        super.onVisibleChange(isVisible);
    }

    @Override
    protected void loadData() {
        super.loadData();
    }
}
```


## Screenshot

<img src="/screenshot/Screenshot_1.png" width="280px" />
<img src="/screenshot/Screenshot_2.png" width="280px" />
<img src="/screenshot/Screenshot_3.png" width="280px" />

<img src="/screenshot/Screenshot_4.png" width="280px" />
<img src="/screenshot/Screenshot_5.png" width="280px" />

<img src="/screenshot/Screenshot_6.png" width="520px" />
<img src="/screenshot/Screenshot_7.png" width="520px" />


## Description

封装支持以下的功能：

1. 支持数据懒加载并且只加载一次。

   有些时候，我们打开一个Fragment页面时，希望它是在可见时才去加载数据，也就是不要在后台就开始加载数据。
   而且，我们也希望加载数据的操作只是第一次打开该Fragment时才进行的操作，如果再重新打开该Fragment，就不要再重复加载数据。

   所以我们通常需要在setUserVisibleHint()里去判断当前Fragment是否可见，可见时再去跟服务器交互。

   ViewPager首次显示的页面经过方法调用setUserVisibleHint(false)->setUserVisibleHint(true)->onCreateView()...，所以该页面的数据加载放在onCreateView中。
   其它预加载页面预加载时setUserVisibleHint(false)->onCreateView()...，当选中该页面显示时调用setUserVisibleHint(true)，所以预加载页面数据加载放在setUserVisibleHint中。

   但是这样还是会出现一个问题，就是每次可见时都会重复去下载数据，我们希望的是只有第一次可见时才需要去下载，那么就还需要再做一些判断。

2. 提供Fragment可见与不可见时回调，并可以在该函数内进行一些ui操作，如显示/隐藏加载框，不会报null异常。

   除此之外可能我们还需要每次Fragment的打开或关闭时显示数据加载进度。

   我们打开一个Fragment时，如果当前Fragment正处于加载状态，那么应该给个下载进度或者加载框提示，在离开该Fragment时需要隐藏加载动画。
   当返回Fragment时，如果还是处于加载状态。则要实现自动显示加载动画，如果数据已经加载完毕则不需要再显示出来。
   这就需要有个Fragment可见与不可见时触发的回调方法，并且该方法还得保证是在view创建完后才触发的，这样才能支持对ui进行操作。

3. 支持view的复用，防止与ViewPager使用时出现重复创建view的问题。

   因为ViewPager缓存机制，所以进行了view的复用，防止onCreateView()重复的创建view，

   view的复用是指ViewPager在销毁和重建Fragment时会不断调用onCreateView()->onDestroyView()之间的生命函数，这样可能会出现重复创建view的情况，
   view的复用其实就是指保存第一次创建的view，后面再onCreateView()时直接返回第一次创建的view。其实也就是将view设置为成员变量，创建view时先判断是否为null。

4. 强制刷新数据。

   可能存在某个Fragment，对数据及时性要求比较高，需要每次进入该Fragment时，都需要刷新数据。


## About

* **作者**：March
* **邮箱**：fengqi.mao.march@gmail.com
* **头条**：https://toutiao.io/u/425956/subjects
* **简书**：https://www.jianshu.com/u/02f2491c607d
* **掘金**：https://juejin.im/user/5b484473e51d45199940e2ae
* **CSDN**：http://blog.csdn.net/u011810138
* **SegmentFault**：https://segmentfault.com/u/maoqiqi
* **StackOverFlow**：https://stackoverflow.com/users/8223522

## License

```
   Copyright 2019 maoqiqi

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```