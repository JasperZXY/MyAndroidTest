package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasper.myandroidtest.actionbar.MyActionBarActivity;
import com.jasper.myandroidtest.activity.SimpleActivity;
import com.jasper.myandroidtest.camera.CameraActivity;
import com.jasper.myandroidtest.dialog.DialogActivity;
import com.jasper.myandroidtest.fragment.ListFragmentActivity;
import com.jasper.myandroidtest.fragment.reader.ReaderActivity;
import com.jasper.myandroidtest.fragmentManager.FragmentManagerActivity;
import com.jasper.myandroidtest.layout.ApiOverviewActivity;
import com.jasper.myandroidtest.layout.FlowLayoutActivity;
import com.jasper.myandroidtest.layout.LayoutParamActivity;
import com.jasper.myandroidtest.layout.MarginPaddingActivity;
import com.jasper.myandroidtest.listView.Classify1Activity;
import com.jasper.myandroidtest.listView.Classify2Activity;
import com.jasper.myandroidtest.listView.MyListViewActivity;
import com.jasper.myandroidtest.listView.SimpleListViewActivity;
import com.jasper.myandroidtest.effect.animator.AnimatorActivity;
import com.jasper.myandroidtest.other.AsyncTaskActivity;
import com.jasper.myandroidtest.other.CursorLoaderActivity;
import com.jasper.myandroidtest.other.MenuActivity;
import com.jasper.myandroidtest.other.PermissionActivity;
import com.jasper.myandroidtest.effect.SoftInputModeChooseActivity;
import com.jasper.myandroidtest.effect.TouchClickActivity;
import com.jasper.myandroidtest.other.ScreenActivity;
import com.jasper.myandroidtest.preference.MyPreferenceActivity;
import com.jasper.myandroidtest.sensor.SimpleSensorActivity;
import com.jasper.myandroidtest.service.BinderServiceActivity;
import com.jasper.myandroidtest.service.MessengerServiceActivity;
import com.jasper.myandroidtest.service.ServiceActivity;
import com.jasper.myandroidtest.store.FileActivity;
import com.jasper.myandroidtest.tabhost.Tabs1Activity;
import com.jasper.myandroidtest.tabhost.Tabs2Activity;
import com.jasper.myandroidtest.tabhost.Tabs3Activity;
import com.jasper.myandroidtest.tabhost.Tabs4Activity;
import com.jasper.myandroidtest.utils.BitmapCache;
import com.jasper.myandroidtest.video.VideoActivity;
import com.jasper.myandroidtest.effect.DragViewActivity;
import com.jasper.myandroidtest.view.WebViewActivity;
import com.jasper.myandroidtest.view.GridViewActivity;
import com.jasper.myandroidtest.image.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ExpandableListView elv;
    private float density;

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
        elv.setGroupIndicator(null);
        elv.setAdapter(new MainAdapter(this, getData(), density));
        elv.setOverScrollMode(ExpandableListView.OVER_SCROLL_NEVER);

    }

    @Override
    protected void onDestroy() {
        BitmapCache.getInstance().clearCache();
        super.onDestroy();
        getApplication().onTerminate();
    }

    private List<Group> getData() {
        List<Group> groups = new ArrayList<>();

        Group groupAndroid  = new Group("四大组件与Fragment", new ArrayList<Child>());
        groupAndroid.getChildren().add(new Child("Activity", SimpleActivity.class));
        groupAndroid.getChildren().add(new Child("Service与Activity通信", ServiceActivity.class));
        groupAndroid.getChildren().add(new Child(getString(R.string.title_activity_binder_service), BinderServiceActivity.class));
        groupAndroid.getChildren().add(new Child(getString(R.string.title_activity_messenger_service), MessengerServiceActivity.class));
        groupAndroid.getChildren().add(new Child("FragmentManager", FragmentManagerActivity.class));
        groupAndroid.getChildren().add(new Child("ListFragment", ListFragmentActivity.class));
        groupAndroid.getChildren().add(new Child("Fragment-Assets文件阅读器", ReaderActivity.class));
        groups.add(groupAndroid);

        Group groupList = new Group("ListView", new ArrayList<Child>());
        groupList.getChildren().add(new Child("简单ListView", SimpleListViewActivity.class));
        groupList.getChildren().add(new Child("可上拉下拉ListView", MyListViewActivity.class));
        groupList.getChildren().add(new Child("分类ListView1-手机相关信息", Classify1Activity.class));
        groupList.getChildren().add(new Child("分类ListView2-手机相关信息", Classify2Activity.class));
        groups.add(groupList);

        Group groupTab  = new Group("TabHost", new ArrayList<Child>());
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
        groups.add(groupLayout);

        Group groupView = new Group("View", new ArrayList<Child>());
        groupView.getChildren().add(new Child("GridView", GridViewActivity.class));
        groupView.getChildren().add(new Child("WebView", WebViewActivity.class));
        groups.add(groupView);

        Group groupImage = new Group("Image", new ArrayList<Child>());
        groupImage.getChildren().add(new Child("ImageView的scaleType问题", ImageViewScaleTypeActivity.class));
        groupImage.getChildren().add(new Child(getString(R.string.title_activity_image_view_different_dpi), ImageViewDifferentDPIActivity.class));
        groupImage.getChildren().add(new Child("图片-矩阵变化", MatrixActivity.class));
        groupImage.getChildren().add(new Child("BitmapCache使用", BitmapCacheActivity.class));
        groupImage.getChildren().add(new Child("Drawable资源", DrawableActivity.class));
        groupImage.getChildren().add(new Child("GifView（第三方库）", GifViewActivity.class));
        groups.add(groupImage);

        Group groupEffect = new Group("效果", new ArrayList<Child>());
        groupEffect.getChildren().add(new Child("Touch跟Click", TouchClickActivity.class));
        groupEffect.getChildren().add(new Child("控件拖动", DragViewActivity.class));
        groupEffect.getChildren().add(new Child("软键盘", SoftInputModeChooseActivity.class));
        groupEffect.getChildren().add(new Child("动画", AnimatorActivity.class));
        groups.add(groupEffect);

        Group groupAMD = new Group("AMD", new ArrayList<Child>());
        groupAMD.getChildren().add(new Child("ActionBar", MyActionBarActivity.class));
        groupAMD.getChildren().add(new Child("Menu", MenuActivity.class));
        groupAMD.getChildren().add(new Child("Dialog", DialogActivity.class));
        groups.add(groupAMD);

        Group groupStore = new Group("store", new ArrayList<Child>());
        groupStore.getChildren().add(new Child("文件读写", FileActivity.class));
        groupStore.getChildren().add(new Child("首选项", MyPreferenceActivity.class));
        groups.add(groupStore);

        Group groupCV = new Group("摄像头、视频", new ArrayList<Child>());
        groupCV.getChildren().add(new Child("摄像头", CameraActivity.class));
        groupCV.getChildren().add(new Child("视频", VideoActivity.class));
        groups.add(groupCV);

        Group groupOther = new Group("其他", new ArrayList<Child>());
        groupOther.getChildren().add(new Child("异步任务", AsyncTaskActivity.class));
        groupOther.getChildren().add(new Child("权限相关", PermissionActivity.class));
        groupOther.getChildren().add(new Child("重力感应", SimpleSensorActivity.class));
        groupOther.getChildren().add(new Child("CursorLoader", CursorLoaderActivity.class));
        groupOther.getChildren().add(new Child("状态栏一体化", ScreenActivity.class));
        groups.add(groupOther);

        return groups;
    }

    private class Child {
        private Class activityClass;
        private String name;

        public Child(String name, Class activityClass) {
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
            ll.setBackgroundColor(Color.rgb(215, 202, 153));
            TextView textView = getTextView();
            textView.setTextColor(Color.DKGRAY);
            Group group = (Group) getGroup(groupPosition);
            textView.setText(group.getName());
            ll.addView(textView);
            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
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
            return ll;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
