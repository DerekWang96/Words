package com.example.words;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ExMsgTask extends AsyncTask<MessagePack, Void, MessagePack> {

    public AsyncMsgRes delegate = null;
    public MessagePack messagePack;

    @Override
    protected MessagePack doInBackground(MessagePack... params) {
        try {

//            Socket socket = new Socket("10.0.3.2", 10000);
            Socket socket = new Socket("223.3.122.242",10000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(messagePack);
            oos.flush();
//          --------------------------
            Object obj = ois.readObject();
            System.out.println("obj:"+obj);
            MessagePack mp;
            mp = (MessagePack) obj;
            //得到MassagePack
            return mp;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(MessagePack messagePack) {//线程结束的时候执行
        super.onPostExecute(messagePack);
        System.out.println("messagePack:"+messagePack);
        System.out.println("delegate:"+delegate);
        delegate.processFinish(messagePack);
    }

    public MessagePack getMessagePack() {
        return messagePack;
    }

    public void setMessagePack(MessagePack messagePack) {
        this.messagePack = messagePack;
    }
    public AsyncMsgRes getDelegate() {
        return delegate;
    }

    public void setDelegate(AsyncMsgRes delegate) {
        this.delegate = delegate;
    }
}
