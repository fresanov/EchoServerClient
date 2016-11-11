package com.resanovic.filip.echoclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {
    private Button btnSend;
    private TextView tvResponse;
    private TextView tvMessage;
    private EditText etIp;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvResponse = (TextView) findViewById(R.id.tvResponse);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        etIp = (EditText) findViewById(R.id.etIp);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = etIp.getText().toString();
                EchoTask task = new EchoTask();
                task.execute();
            }
        });
    }

    String response = "";
    String sendMessage = tvMessage.getText().toString();

    private class EchoTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Socket socket = null;
            try {
                socket = new Socket(ip, 27015);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Send the message to the server
            OutputStream os = null;
            try {
                os = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            try {
                bw.write(sendMessage);
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Get the return message from the server
            InputStream is = null;
            try {
                is = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = null;
            try {
                message = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = message;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvResponse.setText(response);
            super.onPostExecute(aVoid);
        }
    }
}
