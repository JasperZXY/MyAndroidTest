package com.jasper.myandroidtest.actionbar;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.fragmentPage.FragmentPage01;
import com.jasper.myandroidtest.fragmentPage.FragmentPage02;
import com.jasper.myandroidtest.ui.search.SearchResultActivity;

import java.lang.reflect.Method;

/**
 * 1. 让ActionBar显示到底部，在AndroidManifest.xml进行配置，14以上的直接配置android:uiOptions="splitActionBarWhenNarrow"，
 * 兼容包的用<meta-data android:name="android.support.UI_OPTIONS" android:value="splitActionBarWhenNarrow" />
 * 2. 搜索功能，照着官方文档做，有一步需要注意，searchable.xml文件中的属性要引用资源文件里面的，不能写死在里面，不然或失败
 * 3. 实现Tab菜单
 * 4. 点击显示下拉菜单
 */
public class MyActionBarActivity extends Activity {
    private static final String TAG = "MyActionBarActivity";
    private FragmentPage01 fragment01 = new FragmentPage01();
    private FragmentPage02 fragment02 = new FragmentPage02();
    private boolean isTab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action_bar);

        ActionBar actionBar = getActionBar();
        //ActionBar图标设置
        actionBar.setDisplayShowHomeEnabled(true);
        //ActionBar标题设置
        actionBar.setDisplayShowTitleEnabled(false);
        //添加返回键，需要重写getParentActivityIntent方法
        actionBar.setDisplayHomeAsUpEnabled(true);

        //实现Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab = actionBar.newTab()
                .setText("Page01")
                .setTabListener(new MyTabListener(fragment01));
        actionBar.addTab(tab);
        tab = actionBar.newTab()
                .setText("Page02")
                .setTabListener(new MyTabListener(fragment02));
        actionBar.addTab(tab);

        //实现下拉菜单
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);
        arrayAdapter.add("Page01");
        arrayAdapter.add("Page02");
        actionBar.setListNavigationCallbacks(arrayAdapter, mOnNavigationListener);
    }

    //与ActionBar的返回键结合使用
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        this.finish();
        return super.getParentActivityIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_action_bar, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        if (searchView == null) {
            return true;
        }

        //AndroidManifest.xml需要做相应配置meta-data
        // 获取搜索服务管理器
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // searchable activity的component name，由此系统可通过intent进行唤起
//        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
        // 通过搜索管理器，从searchable activity中获取相关搜索信息，就是searchable的xml设置。如果返回null，表示该activity不存在，或者不是searchable
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        if (info == null) {
            Log.e(TAG, "Fail to get search info.");
        }
        // 将searchable activity的搜索信息与search view关联
        searchView.setSearchableInfo(info);
        return true;
    }

    /**
     * 设置Action Bar隐藏菜单、子菜单能够显示图标
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true); // 设置Action Bar隐藏菜单、子菜单能够显示图标
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_switch:
                isTab = !isTab;
                if (isTab) {
                    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                } else {
                    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                }
                break;
            case R.id.item_search:
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    static class MyTabListener implements ActionBar.TabListener {
        private static Fragment curFragment = null;
        private Fragment fragment;

        public MyTabListener(Fragment fragment) {
            this.fragment = fragment;
        }

        //注意没有commit操作
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.add(R.id.layout, fragment);
            }
            curFragment = fragment;
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            if (curFragment != null) {
                fragmentTransaction.hide(curFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }

    ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
        Fragment curFagment = null;
        @Override
        public boolean onNavigationItemSelected(int position, long itemId) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (curFagment != null) {
                ft.hide(curFagment);
            }
            switch (position) {
                case 0:
                    if (fragment01.isAdded()) {
                        ft.show(fragment01);
                    } else {
                        ft.add(R.id.layout, fragment01);
                    }
                    curFagment = fragment01;
                    break;
                case 1:
                    if (fragment02.isAdded()) {
                        ft.show(fragment02);
                    } else {
                        ft.add(R.id.layout, fragment02);
                    }
                    curFagment = fragment02;
                    break;
                default:
                    Log.e(TAG, "error,position:" + position);
            }
            ft.commit();
            return true;
        }
    };

}
