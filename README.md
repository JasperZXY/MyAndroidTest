## 2015-05-31
1. 初始化项目
2. 底部导航栏之FragmentTabHost
3. 底部导航栏之RadioGroup and Fragment并添加手势
4. 底部导航栏之ViewPager Fragment and RadioGroup  
### 问题
1. FragmentTabHost是不保存状态的，填入的信息，在tab切换后会消失，所以用RadioGroup and Fragment实现
2. ViewPager跟Fragment实现底部导航栏，只能保存三个页面的状态，当前和当前左右两个Fragment