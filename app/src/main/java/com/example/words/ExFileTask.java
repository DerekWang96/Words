package com.example.words;


import android.content.Context;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ExFileTask extends AsyncTask<MessagePack, Void, MessagePack>{

    private Context context;
    public String fileName;



    public String taskType;


    @Override
    protected MessagePack doInBackground(MessagePack... params) {
        try {
//            Socket socket = new Socket("10.0.3.2", 10001);
            Socket socket = new Socket("223.3.114.245",10002);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = context.openFileInput(fileName);

            byte[] sendBytes = new byte[1024];
            dos.writeUTF(fileName);
            dos.flush();
            dos.writeUTF(taskType);
            dos.flush();
            int length;

            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                dos.write(sendBytes, 0, length);
                dos.flush();
                System.out.println("长度:"+length);
            }
            System.out.println("发送文件完毕");
            fis.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
