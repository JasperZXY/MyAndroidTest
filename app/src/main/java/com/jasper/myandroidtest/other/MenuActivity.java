package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;


public class MenuActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        context = this;

        TextView tv = (TextView) findViewById(R.id.tv_context);
        registerForContextMenu(tv);

        findViewById(R.id.btn_popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.menu_menu);
//                popupMenu.getMenuInflater().inflate(R.menu.menu_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        menuItemSelected(item);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    //上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //这里可以配置菜单内容
        menu.setHeaderTitle("ContextMenu");
        menu.add(1, 1, 1, "My2Activity");
        menu.add(1, 2, 2, "g1_item2");
        menu.add(2, 3, 3, "g2_item3");
        //加上下面这句代码，菜单显示的选项就是上面3个加上R.menu.menu_menu这个文件里面的菜单一起显示
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        menuItemSelected(item);
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    public void menuItemSelected(MenuItem item) {
        Toast.makeText(this, String.format("groupId[%s], ID[%s], title:%s",
                item.getGroupId(), item.getItemId(), item.getTitle()), Toast.LENGTH_SHORT).show();
    }
}
