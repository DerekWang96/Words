package com.example.words;


import android.content.Context;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ExDataTask extends AsyncTask<MessagePack, Void, MessagePack> {

    private Context context;
    public MessagePack messagePack;
    public String orderType;
    public String filename;


    @Override
    protected MessagePack doInBackground(MessagePack... params) {

        Socket socket = null;
        try {
            String s;
            int i;
            int length = 0;
            byte[] inputByte = new byte[1024];
            socket = new Socket("223.3.155.129",10001);
            System.out.println("socket:"+socket);
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);

            dos.writeUTF(orderType);
            dos.writeUTF(filename);
            dos.flush();

//            oos.writeObject(messagePack);
//            oos.flush();
//          --------------------------
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
    public MessagePack getMessagePack() {
        return messagePack;
    }

    public void setMessagePack(MessagePack messagePack) {
        this.messagePack = messagePack;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
