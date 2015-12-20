package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.adapter.SimpleListViewAdapter;
import com.jasper.myandroidtest.listView.entity.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MenuActivity extends Activity {
    private ListView listViewActionModeMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.btn_popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MenuActivity.this, view); //关联的view
                popupMenu.inflate(R.menu.menu_menu);
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

        //上下文菜单
        TextView tv = (TextView) findViewById(R.id.tv_context);
        registerForContextMenu(tv);

        ListView listViewContextMenu = (ListView) findViewById(R.id.listView_context);
        listViewContextMenu.setAdapter(new SimpleListViewAdapter(this));
        registerForContextMenu(listViewContextMenu);  //可以使用，但长按不知按了哪个item

        //Action Mode
        findViewById(R.id.tv_action_mode).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActionMode(actionModeCallback);
                return false;
            }
        });

        //这个可优化，长按时不知选了哪几个item，可加多选框
        final List<UserInfo> users = new ArrayList<>();
        for (int i=0; i<20; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName("名字" + i);
            userInfo.setPhone("135" + (int) (Math.random() * 1e8));
            users.add(userInfo);
        }
        listViewActionModeMenu = (ListView) findViewById(R.id.listView_action_mode);
        final SimpleListViewAdapter userAdapter = new SimpleListViewAdapter(this, users);
        listViewActionModeMenu.setAdapter(userAdapter);
        final Set<Integer> removeSet = new HashSet<>();
        listViewActionModeMenu.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewActionModeMenu.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("自定义");
                menu.add("删除");
                menu.add("修改");
                menu.add("分享");
                return true;   //这里需要返回true
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Toast.makeText(MenuActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                if (item.getTitle() == "删除" && removeSet.size() > 0) {
                    List<Integer> removeList = new ArrayList<>(removeSet);
                    Collections.sort(removeList, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer lhs, Integer rhs) {
                            return rhs - lhs;
                        }
                    });
                    //先后面的先删除，防止前面的删除，后面的下标就改变了
                    for (int index : removeList) {
                        users.remove(index);
                    }
                    removeSet.clear();
                    userAdapter.notifyDataSetChanged();
                }
                mode.finish();   //菜单消失
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    removeSet.add(position);
                } else {
                    removeSet.remove(position);
                }
            }
        });
    }

    //上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //这里可以配置菜单内容
        menu.setHeaderTitle("Context Menu");
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
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuItemSelected(item);
        //由于添加了group，并且有多选框或单选框，这里的写法需注意
        switch (item.getItemId()) {
            case R.id.menu_item1:
            case R.id.menu_item2:
            case R.id.menu_item3:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void menuItemSelected(MenuItem item) {
        Toast.makeText(this, String.format("groupId[%s], ID[%s], title:%s",
                item.getGroupId(), item.getItemId(), item.getTitle()), Toast.LENGTH_SHORT).show();
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_menu, menu);
            return true;   //这里需要返回true
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Toast.makeText(MenuActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
            mode.finish();   //菜单消失
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

}
