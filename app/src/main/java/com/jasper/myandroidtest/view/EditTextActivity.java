package com.jasper.myandroidtest.view;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class EditTextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        final EditText editText = ((EditText) findViewById(R.id.et_action_send));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(EditTextActivity.this, "发送的内容：" + editText.getText().toString(), Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                return false;
            }
        });

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actv);
        String[] strings = {getResources().getString(R.string.app_name)};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        autoCompleteTextView.setAdapter(adapter);
    }

}
