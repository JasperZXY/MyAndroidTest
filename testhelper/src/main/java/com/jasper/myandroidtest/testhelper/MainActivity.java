package com.jasper.myandroidtest.testhelper;

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

import com.jasper.myandroidtest.testhelper.server.jpush.JPushActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ExpandableListView elv;
    private float density;
    private MainAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(mDisplayMetrics);
        density = mDisplayMetrics.density;
        elv = (ExpandableListView) findViewById(R.id.elv);
        //让group不显示那个箭头
        //elv.setGroupIndicator(null);
        //分隔条
        elv.setDivider(null);

        simpleAdapter = new MainAdapter(this, getMainData(), density);

        elv.setAdapter(simpleAdapter);
        elv.setOverScrollMode(ExpandableListView.OVER_SCROLL_NEVER);

        for(int i=0; i<elv.getChildCount(); i++){
            elv.expandGroup(i);
        }
        elv.smoothScrollToPosition(0);
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
        super.onDestroy();
        getApplication().onTerminate();
    }

    private List<Group> getMainData() {
        List<Group> groups = new ArrayList<>();

        Group groupServer = new Group("服务端", new ArrayList<Child>());
        groupServer.getChildren().add(new Child(getString(R.string.title_activity_jpush), JPushActivity.class));
        groups.add(groupServer);

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

