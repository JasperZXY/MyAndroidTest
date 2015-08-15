package com.jasper.myandroidtest.effect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.fragment.reader.DetailsActivity;
import com.jasper.myandroidtest.fragment.reader.ReaderManager;

public class TouchClickActivity extends Activity {
    private EditText etLog;
    private ToggleButton tgTouchDown;
    private ToggleButton tgTouchUp;
    private ToggleButton tgLayoutClickable;
    private ToggleButton tgButtonClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_click);
        etLog = (EditText) findViewById(R.id.et_log);

        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLog.setText("");
            }
        });
        findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(ReaderManager.TITLE, "Touch跟Click");
                intent.putExtra(ReaderManager.PATH, "note/Touch and Click.txt");
                startActivity(intent);
            }
        });

        //同一个控件的Touch与Click
        tgTouchDown = (ToggleButton) findViewById(R.id.tg_touch_down);
        tgTouchUp = (ToggleButton) findViewById(R.id.tg_touch_up);
        final Button buttonTouch = (Button) findViewById(R.id.btn_touch);
        buttonTouch.setOnTouchListener(new TouchListener());
        buttonTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendLog("button", "Click");
            }
        });
        buttonTouch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                appendLog("button", "Long click");
                return false;
            }
        });
        findViewById(R.id.btn_no_clickable).setOnTouchListener(new TouchListener());

        //控件与父控件的Click事件
        tgLayoutClickable= (ToggleButton) findViewById(R.id.tg_layout_clickable);
        tgButtonClickable= (ToggleButton) findViewById(R.id.tg_button_clickable);
        final Button btnLayout = (Button) findViewById(R.id.btn_layout);
        final ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        layout.setOnClickListener(new ClickListener("Layout"));
        btnLayout.setOnClickListener(new ClickListener("Button"));
        layout.setClickable(tgLayoutClickable.isChecked());
        btnLayout.setClickable(tgButtonClickable.isChecked());
        tgLayoutClickable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layout.setClickable(isChecked);
            }
        });
        tgButtonClickable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnLayout.setClickable(isChecked);
            }
        });
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            String name = String.format("down[%s],up[%s]", tgTouchDown.isChecked(), tgTouchUp.isChecked());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                appendLog(name, "down");
                return tgTouchDown.isChecked();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                appendLog(name, "up");
                return tgTouchUp.isChecked();
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                appendLog(name, "move");
            }
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                appendLog(name, "cancel");
            }
            return false;
        }
    }

    private void appendLog(String name, String msg) {
        etLog.append(String.format("%s:%s\n", name, msg));
        Selection.setSelection(etLog.getText(), etLog.getText().length());
    }

    private class ClickListener implements View.OnClickListener {
        private String name;
        public ClickListener(String name) {
            this.name = name;
        }

        @Override
        public void onClick(View v) {
            appendLog(name, "click");
        }
    }

}
