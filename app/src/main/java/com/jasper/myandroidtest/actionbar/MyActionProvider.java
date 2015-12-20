package com.jasper.myandroidtest.actionbar;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

/**
 * 感觉跟定义子菜单一样
 */
public class MyActionProvider extends ActionProvider {
    private Context context;

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public MyActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        //需要返回null, 才能弹出子菜单.
        return null;
    }

    /**
     * 创建子菜单
     *
     * @param subMenu
     */
    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add(0, 0, 0, "Java").setIcon(R.drawable.java).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, text + " java", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        subMenu.add(0, 1, 1, "Android").setIcon(R.drawable.android).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, text + " android", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * @return 返回true代表有子菜单
     */
    @Override
    public boolean hasSubMenu() {
        return true;
    }


}
