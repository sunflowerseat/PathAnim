# PathAnim
这是一个路径动画的库.使用该库,只需要传入一个路径 和对路径轮廓颜色和跑动光线颜色,填充颜色的设置即可完成一个路径动画,效果图如下:
为了展现动画效果,把时间调节的有些慢,可以自行设置快一点.
<br/>
![alt text](https://raw.githubusercontent.com/sunflowerseat/PathAnim/master/preview/startpage-anim.gif "Title")

使用方法:
该路径动画库使用了nineoldandroid兼容就api版本,可以支持到2.3以上
在module中添加依赖:
```
compile 'com.fancy.library:pathanim:1.0.1'
```
如果有其他需求,可以把path-anim-lib拷贝到本地修改.

在需要使用路径动画的布局文件中
```
<com.fancy.path_anim_lib.AnimatedSvgView
            android:id="@+id/animated_svg_view"
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_centerInParent="true"
            oak:oakSvgFillStart="500"
            oak:oakSvgFillTime="100"
            oak:oakSvgImageSizeX="200"
            oak:oakSvgImageSizeY="200"
            oak:oakSvgTraceTime="2000"
            oak:oakSvgTraceTimePerGlyph="1000" />
```
简单介绍部分重要属性:

oakSvgFillTime路径动画填充时间

oakSvgImageSizeX 原图x所占像素

oakSvgImageSizeY 原图y所占像素

oakSvgTraceTimePerGlyph 路径绘制时间

在Activity代码中
```
        mAnimatedSvgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        //设置path String
        mAnimatedSvgView.setGlyphStrings(AnimPath.ANIM_PATH);
        //Path填充颜色
        mAnimatedSvgView.setFillPaints(255,255,255,255);
        //设置跑动光线的颜色
        mAnimatedSvgView.setTraceColors(255,255,255,255);
        //设置轮廓颜色
        mAnimatedSvgView.setTraceResidueColors(255,255,255,255);

        mAnimatedSvgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == AnimatedSvgView.STATE_FILL_STARTED) {

                    AnimatorSet set = new AnimatorSet();
                    Interpolator interpolator = new DecelerateInterpolator();
                    ObjectAnimator a1 = ObjectAnimator.ofFloat(mAnimatedSvgView, "translationY", 0);
                    a1.setInterpolator(interpolator);
                    set.playTogether(a1);
                    set.start();
                }
            }
        });
```
注意事项:
1/库导入了nineoldandroid兼容动画,如果你的项目中也导入了这个库,会报一个冲突,删掉你项目中nineoldandroid的依赖即可.
2/最好在oncreate方法中,调用mAnimatedSvgView.setGlyphStrings(AnimPath.ANIM_PATH); 这算是这个库的小bug,后期打算修改成使用资源文件配置这一项.

关于demo可以参考app中的代码和[我的博客](http://blog.csdn.net/Fancy_xty/article/details/51699679 "Title")
http://blog.csdn.net/Fancy_xty/article/details/51699679
快动手写一个属于你的独特的启动页动画吧.
有任何问题欢迎加群讨论:283272067
希望大家多聊技术,多分享代码.

