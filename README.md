## 2015-05-31
1. 初始化项目
2. 底部导航栏之FragmentTabHost
3. 底部导航栏之RadioGroup and Fragment并添加手势
4. 底部导航栏之ViewPager Fragment and RadioGroup

### 问题
1. FragmentTabHost是不保存状态的，填入的信息，在tab切换后会消失，所以用RadioGroup and Fragment实现
2. ViewPager跟Fragment实现底部导航栏，只能保存三个页面的状态，当前和当前左右两个Fragment


## 2015-06-01
1. ActionBar
2. 存储使用


## 2015-06-02
1. 摄像头使用：拍照

### 问题
自定义的样式的拍照界面，确定后重新点击开始，界面不会跟着动


## 2015-06-04
1. ActionBar：实现搜索功能、下拉菜单、Tab按钮

## 2015-06-06
1. 首选项：ListPreference、CheckboxPreference、EditTextboxPreference、
	动态配置的ListPreference、手动读写Preference
2. 整理FragmentPage，分扩展包跟非扩展包


## 2015-06-08
1. ListView使用


## 2015-06-09
1. 自定义ListView，实现下拉刷新、上拉更多
2. 解释ImageView的scaleType问题的例子
3. Menu

## 2015-06-10
1. 简单获取应用的权限
2. 对话框应用

## 2015-06-11
1. FragmentTransaction中的addToBackStack操作
2. 简单WebView使用


## 2015-06-14
1. Bitmap二级缓存，内存+文件存储


## 2015-06-17
1. WebView加载项目gif图片
2. FlowLayout 来自博客


## 2015-06-18
摄像头使用代码整理，有点bug


## 2015-06-20
自定义界面拍照

### 问题
屏幕旋转后程序无法知道屏幕进行了旋转


## 2015-06-21
1. 传感器：水平仪应用
2. 修改应用图标（注：小米桌面有bug，桌面图标会被缓存，得重启）
3. ListView分类显示（思路：布局文件同时定义了Group跟Item，
若要Group，则隐藏Item；若显示Item，则隐藏Group。）
4. 手机相关信息获取


## 2015-06-22
1. 传感器
2. 自定义界面拍照，解决屏幕旋转问题
3. 图像处理（Camera+Matrix）


## 2015-06-24
1. 视频播放


## 2015-07-05
1. 主页面改变，用ExpandableListView做显示
2. FlowLayout简单使用例子
3. Drawable资源


## 2015-07-08
1. 底部导航栏，用TabActivity+Activity实现<br/>
   注：这种方式是为了支持低版本的API，可支持2.x，另外用TabActivity是弃用的API


## 2015-07-10
1. TableLayout-Android版本与API版本对应表
2. ListView分类显示，第二种实现方式


## 2015-08-02
1. GridView，一行显示


## 2015-08-03
1. 软键盘windowSoftInputMode属性及显示隐藏


## 2015-08-06
1. Touch跟Click事件处理


## 2015-08-07
1. 动画实现
2. 仿IOS选择对话框实现
3. 控件拖动实现


## 2015-08-13
1. 手动控制软键盘的显示，软键盘显示时，让输入框能够推上去


## 2015-08-15
1. Activity学习例子
2. Fragment学习例子
3. 简单Assets文件阅读器例子，横屏做两屏显示，竖屏做详细内容跳转显示


## 2015-08-16
1. CursorLoader例子


## 2015-08-20
1. 状态栏一体化，沉浸式状态栏


## 2015-08-21
1. 不同drawable文件夹下图片的ImageView显示


## 2015-08-22
1. Bound Service学习例子（Binder、Messenger、AIDL）


## 2015-08-23
1. BroadcastReceiver学习例子
2. EditText


## 2015-09-02
1. 所有的界面进行美化，统一风格


## 2015-09-03
1. 根据不同的主题来设置样式


## 2015-09-12
1. 进一步优化手动控制软键盘的显示，软键盘显示时，让输入框能够能够缩小，输入框以下部分能够推上去。


## 2015-12-27
1. 解决DialogFragment宽度无法自定义的问题，需要在onResume中进行设置才可以，但还是有边框距离
2. notification例子：添加显示、删除监听、自定义布局、Progress显示删除与线程停止
3. 应用内红点

## 2015-12-29
1. Toast例子


## 2016-1-1
1. 搜索例子
2. Drag/Drop 框架例子 


## 2016-1-2
1. 绘画：Path、Paint等的使用，各种图形空心、实心、渐变的实现，画各种不同的线
2. 官方例子整合：Xfermodes


## 2016-1-3
1. 官方例子整合：LunarLander


## 2016-1-9
1. 官网例子：使用MediaRecorder录音
2. 使用Intent跳转系统摄像头拍照与拍摄


## 2016-1-10
1. 自定义UI拍摄视频，还有些问题未解决
2. 设置页面添加清除缓存按钮


## 2016-1-12
1. 地理位置获取例子


## 2016-1-16
1. Environment Sensor例子


## 2016-2-28
1. 升级为2.0版本，添加第三方库
2. androidannotations
3. Butter Knife
4. Retrofit
5. EventBus


## 2016-4-24
1. Universal Image Loader
2. 初步抽象BaseAdapter
3. 减少ListView在滑动过程中图片闪动的概率


## 2016-5-1
1. 添加一个模块，模块名为“补助程序”
2. 获取CPU信息