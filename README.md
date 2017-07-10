最近在和项目经理都斗智斗勇的时候，突然被甩过来一个类似支付宝首页的功能需求，虽然网上有一些类似的功能，但是都是以前比较老一些的版本，于是决定自己来定制一个，老规矩，先上图

![GIF.gif](http://upload-images.jianshu.io/upload_images/2599466-3a7bd996bd887dac.gif?imageMogr2/auto-orient/strip)

要实现这样一个效果，首先想到的自然就是 CoordinatorLayout；

什么是CoordinatorLayout？
CoordinatorLayout是用来协调其子view们之间动作的一个父view，而Behavior就是用来给CoordinatorLayout的子view们实现交互的。关于这个控件，大神们已经介绍的很详细了，这里我就不过多啰嗦，直接开撸了

关于Behavior 
要自定义Behavior，首先要搞清楚两个核心View，child 和 dependency；他们分别代表的是要应用behavior的View 和触发behavior并与child进行互动的View。

要实现上面的的效果，需要实现以下几个关键方法
 - layoutDependsOn(CoordinatorLayout parent, View child, View dependency)
用来判断child是否有一个对应的dependency，如果有就返回true，默认情况下返回的是false

 - onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection)
此方法可用于动态更改childView的布局，如果自定义Behaior,这个方法一定返回true，否则将使用默认Behavior的布局

 - onDependentViewChanged(CoordinatorLayout parent, View child, View dependency)
此方法在dependencyView发生变化的时候调用，在这里，我们可以对child根据dependency的变化进行一定的操作

 - onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes)
此方法表示开始滑动，最后一个参数表示的是滑动方向，并且只有在返回值为true的时候才能出发接下来的操作，在这里可以进行一些过滤操作，比如值接受垂直方向的滑动等

 -  onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes)
在onStartNestedScroll返回为true的时候调用，此时表示CoordinatorLayout已经拦截了滑动，在这里可以做一些滑动初始化的操作

 - onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed)
在开始嵌套滑动之前，会执行此操作，dx、dy分别表示用户手指滑动的距离，consumed则表示在操作过程中，消耗掉的滑动距离，例如：
```
consumed[1] = dy;
```
此时表示垂直方向的滑动被全部消耗，将不会被传递到下一步操作，相对应的child，如RecycleView将不能接受的滑动操作，不会进行滑动

 - onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) 
此方法在嵌套滑动时候调用，可以多滑动过程进行操作

具体解析，请看我的个人文章：[来来来，随老夫撸一个支付宝玩玩——自定义Behavior的正确打开方式](http://www.jianshu.com/p/de8081d41b9c)
