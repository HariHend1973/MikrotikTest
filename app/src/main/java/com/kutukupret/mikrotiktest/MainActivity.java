package com.kutukupret.mikrotiktest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import me.legrange.mikrotik.ApiConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "mLog";

    Button btnConnect;
    Button btnClear;
    TextView tvResult;
    TextView tvIP;
    MyTask mt;
    String IPADDR;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnClear = (Button) findViewById(R.id.btnClear);

        tvIP = (TextView)findViewById(R.id.tvIP);
        TextView tvAddress = (TextView)findViewById(R.id.tvAddress);

        tvResult = (TextView)findViewById(R.id.tvResult);
        tvResult.setMovementMethod(new ScrollingMovementMethod());
        tvAddress.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IPADDR = tvIP.getText().toString();
                mt = new MyTask();
                mt.execute();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResult.setText("");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
    }


    class MyTask extends AsyncTask<Void, String, List<Map<String, String>>> {
        //ProgressDialog progressDialog;
        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
            List<Map<String, String>> res = null;
            //Map<String, String> res = null;
            publishProgress("Loading...");

            try {
                //List<Map<String, String>> result = null;
                try
                {
                    Log.d(LOG_TAG, "start");

                    ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), IPADDR, ApiConnection.DEFAULT_PORT, 2000);
                    Log.d(LOG_TAG, "start2");
                    con.login("harihend1220", "J1ngkr4k201273");

                    if(con.isConnected())
                    {
                        //tvResult.setText("OK!");
                        Log.d(LOG_TAG, "Connected");
                    }
                    res = con.execute("/ip/dhcp-server/lease/print");
                    //for(Map<String, String>res : result)
                    //{
                    //    Log.d(LOG_TAG, res.toString());
                    //    //tvResult.setText(res.toString());
                    //}

                    Thread.sleep(1000);

                    con.close();
                }
                catch (Exception e)
                {
                    Log.d(LOG_TAG, "error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            tvResult.setText("");
            tvResult.append("============================\n");
            tvResult.append("[ip dhcp-server lease print]\n");
            tvResult.append("============================\n\n");
            try {
                for (Map<String, String> res : result) {
                    //    Log.d(LOG_TAG, res.toString());
                    //tvResult.append("----------------------------\n");
                    tvResult.setTextSize(14f);
                    tvResult.append("mac-address: " + res.get("mac-address") + "\n");
                    tvResult.append("server: " + res.get("server") + "\n");
                    tvResult.append("address: " + res.get("address") + "\n");
                    tvResult.append("host-name: " + res.get("host-name") + "\n");
                    tvResult.append("last-seen: " + res.get("last-seen") + "\n");
                    tvResult.append("blocked: " + res.get("blocked") + "\n");
                    tvResult.append("comment: " + res.get("comment") + "\n");
                    tvResult.append("client-id: " + res.get("client-id") + "\n");
                    tvResult.append("status: " + res.get("status") + "\n");
                    tvResult.append("----------------------------\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //tvResult.setText("End");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(MainActivity.this,
            //        "Mikrotik",
            //        "Loading..");
            //tvResult.setText("Begin");
            dialog.show();
            Log.d(LOG_TAG, "Begin");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            tvResult.setText(text[0]);
        }
    }
}

