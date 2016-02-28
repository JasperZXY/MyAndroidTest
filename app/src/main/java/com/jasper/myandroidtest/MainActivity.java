package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasper.myandroidtest.actionbar.*;
import com.jasper.myandroidtest.activity.*;
import com.jasper.myandroidtest.camera.CameraActivity;
import com.jasper.myandroidtest.dialog.DialogActivity;
import com.jasper.myandroidtest.event.TouchClickActivity;
import com.jasper.myandroidtest.fragment.*;
import com.jasper.myandroidtest.fragment.reader.ReaderActivity;
import com.jasper.myandroidtest.fragmentManager.FragmentManagerActivity;
import com.jasper.myandroidtest.layout.*;
import com.jasper.myandroidtest.library.AndroidAnnotationsActivity_;
import com.jasper.myandroidtest.library.ButterKnifeActivity;
import com.jasper.myandroidtest.library.RetrofitActivity;
import com.jasper.myandroidtest.listView.*;
import com.jasper.myandroidtest.effect.animator.AnimatorActivity;
import com.jasper.myandroidtest.other.*;
import com.jasper.myandroidtest.resource.DrawableActivity;
import com.jasper.myandroidtest.resource.StringActivity;
import com.jasper.myandroidtest.resource.StyleAttributesActivity;
import com.jasper.myandroidtest.sensor.EnvironmentSensorActivity;
import com.jasper.myandroidtest.sensor.GradienterActivity;
import com.jasper.myandroidtest.service.aidl.MyAidlActivity;
import com.jasper.myandroidtest.ui.*;
import com.jasper.myandroidtest.effect.*;
import com.jasper.myandroidtest.preference.MyPreferenceActivity;
import com.jasper.myandroidtest.sensor.SimpleSensorActivity;
import com.jasper.myandroidtest.service.*;
import com.jasper.myandroidtest.store.FileActivity;
import com.jasper.myandroidtest.tabhost.*;
import com.jasper.myandroidtest.ui.draw.DrawActivity;
import com.jasper.myandroidtest.ui.draw.LunarLanderActivity;
import com.jasper.myandroidtest.ui.notification.NotificationActivity;
import com.jasper.myandroidtest.ui.search.SearchActivity;
import com.jasper.myandroidtest.utils.BitmapCache;
import com.jasper.myandroidtest.video.AudioActivity;
import com.jasper.myandroidtest.video.VideoActivity;
import com.jasper.myandroidtest.effect.DragViewActivity;
import com.jasper.myandroidtest.view.*;
import com.jasper.myandroidtest.image.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ExpandableListView elv;
    private float density;
    private MainAdapter simpleAdapter;  //普通例子用
    private MainAdapter libraryAdapter; //第三方库例子用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication application = (MyApplication) getApplication();
        application.sayHello(this);

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(mDisplayMetrics);
        density = mDisplayMetrics.density;
        elv = (ExpandableListView) findViewById(R.id.elv);
        //让group不显示那个箭头
//        elv.setGroupIndicator(null);
        //分隔条
        elv.setDivider(null);

        simpleAdapter = new MainAdapter(this, getMainData(), density);
        libraryAdapter = new MainAdapter(this, getLibraryData(), density);

        elv.setAdapter(simpleAdapter);
        elv.setOverScrollMode(ExpandableListView.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collapse_all:
                for(int i=0; i<elv.getChildCount(); i++){
                    if(elv.isGroupExpanded(i)){
                        elv.collapseGroup(i);
                    }
                }
                elv.smoothScrollToPosition(0);
                break;

            case R.id.action_simple:
                elv.setAdapter(simpleAdapter);
                break;

            case R.id.action_library:
                elv.setAdapter(libraryAdapter);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        BitmapCache.getInstance().clearCache();
        super.onDestroy();
        getApplication().onTerminate();
    }

    private List<Group> getMainData() {
        List<Group> groups = new ArrayList<>();

        Group groupBasic = new Group("四大组件与Fragment", new ArrayList<Child>());
        groupBasic.getChildren().add(new Child("Activity", SimpleActivity.class));
        groupBasic.getChildren().add(new Child("Service与Activity通信", ServiceActivity.class));
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_binder_service), BinderServiceActivity.class));
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_messenger_service), MessengerServiceActivity.class));
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_my_aidl), MyAidlActivity.class));
        groupBasic.getChildren().add(new Child("FragmentManager", FragmentManagerActivity.class));
        groupBasic.getChildren().add(new Child("ListFragment", ListFragmentActivity.class));
        groupBasic.getChildren().add(new Child("Fragment-Assets文件阅读器", ReaderActivity.class));
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_broadcast_receiver), BroadcastReceiverActivity.class));
        groups.add(groupBasic);

        Group groupTab = new Group("TabHost", new ArrayList<Child>());
        groupTab.getChildren().add(new Child("FragmentTabHost", Tabs1Activity.class));
        groupTab.getChildren().add(new Child("Fragment+RadioGroup", Tabs2Activity.class));
        groupTab.getChildren().add(new Child("Fragment+ViewPager", Tabs3Activity.class));
        groupTab.getChildren().add(new Child("TabActivity+Activity", Tabs4Activity.class));
        groups.add(groupTab);

        Group groupLayout = new Group("Layout", new ArrayList<Child>());
        groupLayout.getChildren().add(new Child("Margin与Padding", MarginPaddingActivity.class));
        groupLayout.getChildren().add(new Child("LayoutParam", LayoutParamActivity.class));
        groupLayout.getChildren().add(new Child("TableLayout-Android版本与API版本对应表", ApiOverviewActivity.class));
        groupLayout.getChildren().add(new Child("自定义FlowLayout", FlowLayoutActivity.class));
        groupLayout.getChildren().add(new Child("动态确定坐标", CoordinateActivity.class));
        groups.add(groupLayout);

        Group groupList = new Group("ListView", new ArrayList<Child>());
        groupList.getChildren().add(new Child("简单的ListView", SimpleListViewActivity.class));
        groupList.getChildren().add(new Child(getString(R.string.title_activity_scroll_list_view), ScrollListViewActivity.class));
        groupList.getChildren().add(new Child("可上拉下拉ListView", MyListViewActivity.class));
        groupList.getChildren().add(new Child("分类ListView1-手机相关信息", Classify1Activity.class));
        groupList.getChildren().add(new Child("分类ListView2-手机相关信息", Classify2Activity.class));
        groups.add(groupList);

        Group groupImage = new Group("Image", new ArrayList<Child>());
        groupImage.getChildren().add(new Child("ImageView的scaleType问题", ImageViewScaleTypeActivity.class));
        groupImage.getChildren().add(new Child(getString(R.string.title_activity_image_view_different_dpi), ImageViewDifferentDPIActivity.class));
        groupImage.getChildren().add(new Child("图片-矩阵变化", MatrixActivity.class));
        groupImage.getChildren().add(new Child("BitmapCache使用", BitmapCacheActivity.class));
        groupImage.getChildren().add(new Child("GifView（第三方库）", GifViewActivity.class));
        groups.add(groupImage);

        Group groupView = new Group("View", new ArrayList<Child>());
        groupView.getChildren().add(new Child("View", ViewActivity.class));
        groupView.getChildren().add(new Child("GridView", GridViewActivity.class));
        groupView.getChildren().add(new Child("WebView", WebViewActivity.class));
        groupView.getChildren().add(new Child("EditText", EditTextActivity.class));
        groupView.getChildren().add(new Child("TextView", TextViewActivity.class));
        groups.add(groupView);

        Group groupUI = new Group("UI", new ArrayList<Child>());
        groupUI.getChildren().add(new Child("ActionBar", MyActionBarActivity.class));
        groupUI.getChildren().add(new Child("ActionBar Action Provider", ActionProviderActivity.class));
        groupUI.getChildren().add(new Child("Menu", MenuActivity.class));
        groupUI.getChildren().add(new Child("Dialog", DialogActivity.class));
        groupUI.getChildren().add(new Child("Notification", NotificationActivity.class));
        groupUI.getChildren().add(new Child(getResources().getString(R.string.title_activity_toast), ToastActivity.class));
        groupUI.getChildren().add(new Child(getResources().getString(R.string.title_activity_search), SearchActivity.class));
        groupUI.getChildren().add(new Child(getResources().getString(R.string.title_activity_drag_drop), DragDropActivity.class));
        groupUI.getChildren().add(new Child(getResources().getString(R.string.title_activity_draw), DrawActivity.class));
        groupUI.getChildren().add(new Child(getResources().getString(R.string.title_activity_Xfermodes), XfermodesActivity.class));
        groupUI.getChildren().add(new Child(getResources().getString(R.string.title_activity_lunar_lander), LunarLanderActivity.class));
        groups.add(groupUI);

        Group groupResource = new Group("Resource", new ArrayList<Child>());
        groupResource.getChildren().add(new Child("Drawable资源", DrawableActivity.class));
        groupResource.getChildren().add(new Child(getString(R.string.title_activity_style_attributes), StyleAttributesActivity.class));
        groupResource.getChildren().add(new Child(getString(R.string.title_activity_string), StringActivity.class));
        groups.add(groupResource);

        Group groupEvent = new Group("Event", new ArrayList<Child>());
        groupEvent.getChildren().add(new Child("Touch跟Click", TouchClickActivity.class));
        groups.add(groupEvent);

        Group groupSensor = new Group("Sensor", new ArrayList<Child>());
        groupSensor.getChildren().add(new Child("重力感应", SimpleSensorActivity.class));
        groupSensor.getChildren().add(new Child("重力感应——水平仪", GradienterActivity.class));
        groupSensor.getChildren().add(new Child(getString(R.string.title_activity_environment_sensor), EnvironmentSensorActivity.class));
        groups.add(groupSensor);

        Group groupEffect = new Group("效果", new ArrayList<Child>());
        groupEffect.getChildren().add(new Child("控件拖动", DragViewActivity.class));
        groupEffect.getChildren().add(new Child("软键盘", SoftInputModeChooseActivity.class));
        groupEffect.getChildren().add(new Child("动画", AnimatorActivity.class));
        groups.add(groupEffect);

        Group groupStore = new Group("store", new ArrayList<Child>());
        groupStore.getChildren().add(new Child("文件读写", FileActivity.class));
        groupStore.getChildren().add(new Child("首选项", MyPreferenceActivity.class));
        groups.add(groupStore);

        Group groupCV = new Group("摄像头、视频、音频", new ArrayList<Child>());
        groupCV.getChildren().add(new Child("摄像头", CameraActivity.class));
        groupCV.getChildren().add(new Child("视频", VideoActivity.class));
        groupCV.getChildren().add(new Child(getResources().getString(R.string.title_activity_audio), AudioActivity.class));
        groups.add(groupCV);

        Group groupOther = new Group("其他", new ArrayList<Child>());
        groupOther.getChildren().add(new Child("异步任务", AsyncTaskActivity.class));
        groupOther.getChildren().add(new Child("权限相关", PermissionActivity.class));
        groupOther.getChildren().add(new Child("CursorLoader", CursorLoaderActivity.class));
        groupOther.getChildren().add(new Child(getString(R.string.title_activity_screen), ScreenActivity.class));
        groupOther.getChildren().add(new Child(getString(R.string.title_activity_settings), SettingsActivity.class));
        groupOther.getChildren().add(new Child(getString(R.string.title_activity_tip), TipActivity.class));
        groupOther.getChildren().add(new Child(getString(R.string.title_activity_location), LocationActivity.class));
        groups.add(groupOther);

        return groups;
    }

    private List<Group> getLibraryData() {
        List<Group> groups = new ArrayList<>();

        Group groupBasic = new Group("普通", new ArrayList<Child>());
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_android_annotations), AndroidAnnotationsActivity_.class));
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_butter_knife), ButterKnifeActivity.class));
        groupBasic.getChildren().add(new Child(getString(R.string.title_activity_retrofit), RetrofitActivity.class));
        groups.add(groupBasic);

        return groups;
    }

    private class Child {
        private Class activityClass;
        private String name;

        public Child(String name, Class<? extends Activity> activityClass) {
            this.name = name;
            this.activityClass = activityClass;
        }

        public Class getActivityClass() {
            return activityClass;
        }

        public String getName() {
            return name;
        }
    }

    private class Group {
        private String name;
        private List<Child> children;

        public Group(String name, List<Child> children) {
            this.name = name;
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public List<Child> getChildren() {
            return children;
        }
    }

    private class MainAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<Group> groups;
        private float density;

        public MainAdapter(Context context, List<Group> groups, float density) {
            this.context = context;
            this.groups = groups;
            this.density = density;
        }

        //自己定义一个获得文字信息的方法
        TextView getTextView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(context);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            int padding = (int) (8 * density);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextSize(20);
            textView.setTextColor(Color.DKGRAY);
            return textView;
        }

        //重写ExpandableListAdapter中的各个方法
        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).getChildren().size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups.get(groupPosition).getChildren().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
//            ll.setBackgroundColor(Color.rgb(215, 202, 153));
            ll.setBackgroundResource(R.drawable.item_home_menu);
            TextView textView = getTextView();
            textView.setTextColor(Color.DKGRAY);
            Group group = (Group) getGroup(groupPosition);
            textView.setText(group.getName());
            ll.addView(textView);
            ((LinearLayout.MarginLayoutParams)textView.getLayoutParams()).leftMargin = 80;
            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            FrameLayout layout = new FrameLayout(context);
            LinearLayout ll = new LinearLayout(context);
            layout.addView(ll);
            ((LinearLayout.MarginLayoutParams)ll.getLayoutParams()).leftMargin = 20;
            ((LinearLayout.MarginLayoutParams)ll.getLayoutParams()).rightMargin = 20;
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setBackgroundResource(R.drawable.item_home_menu);
            TextView textView = getTextView();
            final Child child = (Child) getChild(groupPosition, childPosition);
            textView.setText(child.getName());
            ll.addView(textView);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, child.getActivityClass()));
                }
            });
            return layout;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
