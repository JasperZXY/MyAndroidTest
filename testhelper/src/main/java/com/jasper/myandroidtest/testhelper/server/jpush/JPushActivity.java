package com.jasper.myandroidtest.testhelper.server.jpush;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.testhelper.BaseActivity;
import com.jasper.myandroidtest.testhelper.R;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class JPushActivity extends BaseActivity {
    private static final String TAG = "JPushActivity";
    private EditText etLog;
    private EditText etRegId;
    private EditText etTitle;
    private EditText etMessage;
    private EditText etExtraKey0;
    private EditText etExtraValue0;
    private EditText etExtraKey1;
    private EditText etExtraValue1;
    private JPushClient jPushClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush);
        initView();
        jPushClient = new JPushClient("f58eb06627e2cb868f046a74", "091732b7a319112deddd064f");
    }

    private void send() {
        String regId = etRegId.getText().toString();
        Map<String, String> extra = new HashMap<>();
        extra.put(etExtraKey0.getText().toString(), etExtraValue0.getText().toString());
        extra.put(etExtraKey1.getText().toString(), etExtraValue1.getText().toString());
        String title = etTitle.getText().toString();
        String message = etMessage.getText().toString();
        PushResult pushResult = null;
        // 本来是要调用sendMessageAll跟sendAndroidMessageWithRegistrationID，但有些字段没有，所有改用sendPush
        // jPushClient.sendMessageAll(message);
        // jPushClient.sendAndroidMessageWithRegistrationID(title, message, regId);
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        if (TextUtils.isEmpty(regId)) {
            pushPayloadBuilder = pushPayloadBuilder.setPlatform(Platform.all())
                    .setAudience(Audience.all());
        } else {
            pushPayloadBuilder = pushPayloadBuilder.setPlatform(Platform.android())
                    .setAudience(Audience.registrationId(regId));
        }
        pushPayloadBuilder = pushPayloadBuilder
                .setMessage(
                        Message.newBuilder().setTitle(title).setMsgContent(message)
                                .addExtras(extra).build())
                ;
        PushPayload pushPayload = pushPayloadBuilder.build();
        try {
            pushResult = jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            Log.e(TAG, "APIConnectionException:", e);
            etLog.append("\nAPIConnectionException:" + Log.getStackTraceString(e));
        } catch (APIRequestException e) {
            Log.e(TAG, "APIRequestException:", e);
            etLog.append("\nAPIRequestException:" + Log.getStackTraceString(e));
        }

        if (pushResult == null) {
            etLog.append("\n发送结果为空");
        } else {
            etLog.append(String.format("\nmsg_id:%s,sendno:%s", pushResult.msg_id, pushResult.sendno));
        }
    }

    private void initView() {
        etLog = (EditText) findViewById(R.id.et_log);
        etRegId = (EditText) findViewById(R.id.et_regId);
        etTitle = (EditText) findViewById(R.id.et_title);
        etMessage = (EditText) findViewById(R.id.et_message);
        etExtraKey0 = (EditText) findViewById(R.id.et_extra_key0);
        etExtraValue0 = (EditText) findViewById(R.id.et_extra_value0);
        etExtraKey1 = (EditText) findViewById(R.id.et_extra_key1);
        etExtraValue1 = (EditText) findViewById(R.id.et_extra_value1);

        findViewById(R.id.bn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

}
