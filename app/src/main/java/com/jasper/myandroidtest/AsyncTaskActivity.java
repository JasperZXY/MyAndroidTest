package com.jasper.myandroidtest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;

/**
 * AsyncTask使用例子
 */
public class AsyncTaskActivity extends ActionBarActivity {
    private Button button;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        button = (Button) findViewById(R.id.button03);
        progressBar = (ProgressBar) findViewById(R.id.progressBar02);
        textView = (TextView) findViewById(R.id.textView01);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(textView, progressBar);
                asyncTask.execute(1000);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_async_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, String> {

    private TextView textView;
    private ProgressBar progressBar;

    public ProgressBarAsyncTask(TextView textView, ProgressBar progressBar) {
        super();
        this.textView = textView;
        this.progressBar = progressBar;
    }


    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    @Override
    protected String doInBackground(Integer... params) {
        long curTime = System.currentTimeMillis();
        int i = 5;
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i > 100) {
                publishProgress(100);
                break;
            }
            publishProgress(i);
            i += Math.random() * 15;
        }
        return "共花费：" + (System.currentTimeMillis() - curTime) + "MS";
    }


    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPostExecute(String result) {
        textView.setText("异步操作执行结束。" + result);
    }


    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPreExecute() {
        textView.setText("正在执行异步线程...");
    }


    /**
     * 这里的Intege参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int vlaue = values[0];
        progressBar.setProgress(vlaue);
    }

}
