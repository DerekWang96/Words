package com.example.words;


import android.os.AsyncTask;



public class TestAsync extends AsyncTask<MessagePack, Void, MessagePack>{
    MessagePack messagePack ;
    public AsyncMsgRes delegate = null;

    @Override
    protected MessagePack doInBackground(MessagePack... params) {
        MessagePack mp  = new MessagePack();
        mp.booleanResult.add(true);
        mp.booleanResult.add(true);
        return mp;
    }

    @Override
    protected void onPostExecute(MessagePack messagePack) {
        super.onPostExecute(messagePack);
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
