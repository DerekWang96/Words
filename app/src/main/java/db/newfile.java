package db;

import android.content.Context;
import java.io.*;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class newfile {
    private final int BUFFER_SIZE = 10000;
    public static final String FILE_NAME = "com.example.words";
    public static final String FILE_PATH = "data/data/" + FILE_NAME;

    private Context context;
    public newfile(Context context) {
        this.context = context;
    }
    public void writecurrentuser(String username) throws IOException {
        File jhPath = new File(FILE_PATH +"/"+ "currentusername.txt");
        jhPath.createNewFile();
        FileWriter fw = new FileWriter(jhPath,false);
        fw.write(username);
        fw.close();
    }
    public void writelastuser(String username,String password) throws IOException, NoSuchAlgorithmException {
        File jhPath = new File(FILE_PATH +"/"+ "lastusername.txt");
        FileWriter fw = new FileWriter(jhPath,false);
        String md5 = "";
        MessageDigest md=MessageDigest.getInstance("MD5");
        byte[] messageByte = password.getBytes("UTF-8");
        byte[] md5Byte = md.digest(messageByte);
        md5 = bytesToHex(md5Byte);
        fw.write(username+"-"+md5);
        fw.close();
    }
    public void writecurrentwordbook(String wordbookname) throws IOException {
        File jhPath = new File(FILE_PATH +"/"+ "wordbookname.txt");
        jhPath.createNewFile();
        FileWriter fw = new FileWriter(jhPath,false);
        fw.write(wordbookname);
        fw.close();
    }
    public String readcurrentwordbook() throws IOException {
        File filename = new File(FILE_PATH +"/"+ "wordbookname.txt"); // 要读取以上路径的input。txt文件
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
    public String readcurrentuser() throws IOException {
        File filename = new File(FILE_PATH +"/"+ "currentusername.txt"); // 要读取以上路径的input。txt文件
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
