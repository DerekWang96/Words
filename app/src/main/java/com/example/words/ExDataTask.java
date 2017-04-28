package com.example.words;


import android.content.Context;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ExDataTask extends AsyncTask<MessagePack, Void, MessagePack> {

    private Context context;


    @Override
    protected MessagePack doInBackground(MessagePack... params) {
        Socket socket = null;
        try {
            String s;
            int i;
            int length = 0;
            byte[] inputByte = new byte[1024];
//            socket = new Socket("10.0.3.2", 10001);
            socket = new Socket("223.3.107.94",10001);
            System.out.println("socket:"+socket);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            FileOutputStream fos = context.openFileOutput("test.txt", Context.MODE_PRIVATE);
            s = dis.readUTF();
            i = dis.readInt();
            System.out.println("读到的数据:"+s);
            System.out.println("读到的整形:"+i);

            System.out.println("开始接收数据...");
            while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                fos.write(inputByte, 0, length);
                fos.flush();
                System.out.println("剩余长度"+length);
            }
            System.out.println("完成接收");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ExDataTask (Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(MessagePack messagePack) {
        super.onPostExecute(messagePack);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(MessagePack messagePack) {
        super.onCancelled(messagePack);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
