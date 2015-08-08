package com.jasper.myandroidtest.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

/**
 * 仿IOS选择对话框
 * 来自http://www.javaapk.com/source/6994.html
 */
public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView tvTitle;
    private TextView tvCancel;
    private LinearLayout layout;
    private ScrollView scrollView;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private int screenWidth;
    private int screenHeight;
    private OnCancelListener cancelListener;

    public ActionSheetDialog setSheetItemList(List<SheetItem> sheetItemList) {
        this.sheetItemList = sheetItemList;
        return this;
    }

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
        windowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;
    }

    public ActionSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_actionsheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(screenWidth);

        // 获取自定义Dialog布局中的控件
        scrollView = (ScrollView) view.findViewById(R.id.sv);
        layout = (LinearLayout) view.findViewById(R.id.layout_item);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (cancelListener != null) {
                    cancelListener.onCancel();
                }
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancelListener != null) {
                    cancelListener.onCancel();
                }
            }
        });

        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public ActionSheetDialog addOnCancelLisener(OnCancelListener lisener) {
        cancelListener = lisener;
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelListener.onCancel();
            }
        });
        return this;
    }

    /**
     * @param item  条目名称
     * @param color 条目字体颜色
     * @param listener 点击事件监听器
     * @return
     */
    public ActionSheetDialog addSheetItem(String item, int color, OnItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new SheetItem(item, color, listener));
        return this;
    }

    /**
     * @param item  条目名称
     * @param listener 点击事件监听器
     * @return
     */
    public ActionSheetDialog addSheetItem(String item, OnItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new SheetItem(item, SheetItemColor.Blue.getColor(), listener));
        return this;
    }

    /**
     * @param item  条目名称
     * @return
     */
    public ActionSheetDialog addSheetItem(String item) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new SheetItem(item, SheetItemColor.Blue.getColor(), null));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LayoutParams) scrollView
                    .getLayoutParams();
            params.height = screenHeight / 2;
            scrollView.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 0; i < size; i++) {
            final SheetItem sheetItem = sheetItemList.get(i);

            TextView textView = new TextView(context);
            textView.setText(sheetItem.name);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single);
                }
            } else {
                if (showTitle) {
                    if (i < size - 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom);
                    }
                } else {
                    if (i == 0) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top);
                    } else if (i < size - 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom);
                    }
                }
            }

            textView.setTextColor(sheetItem.color);

            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, height));

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (sheetItem.listener != null) {
                        sheetItem.listener.onClick();
                    }
                }
            });

            layout.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnItemClickListener {
        void onClick();
    }
    public interface OnCancelListener {
        void onCancel();
    }

    public class SheetItem {
        String name;
        int color;
        OnItemClickListener listener;

        public SheetItem(String name, int color, OnItemClickListener listener) {
            this.name = name;
            this.color = color;
            this.listener = listener;
        }
    }

    public enum SheetItemColor {
        Blue(0xFF037BFF),
        Red(0xFFFD4A2E),;

        private int color;

        SheetItemColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
}
