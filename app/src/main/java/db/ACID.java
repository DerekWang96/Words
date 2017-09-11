package db;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ACID {
   public ACID (Context context){
	   setContext(context);
   }
   public boolean addUserIDandpassword(String UserID,String password,String Databasename){
	   Cursor cursor = null;
	   boolean bool = false;
	   ImportDB importdb = new ImportDB(context, Databasename);
	   SQLiteDatabase db = importdb.openDatabase(true);
	   String sql = "select count(*) as c from sqlite_master where type ='table' and name ='usertable' "; 
       cursor = db.rawQuery(sql, null);  
       if (cursor.moveToNext()) {  
           int count = cursor.getInt(0);  
           if (count > 0) {  
              bool = true;   
           }  
       }
       if(bool == true){
    	   db.execSQL("insert into usertable(userID,password)values(?,?)",
    			   new String[]{UserID,password});
       }else{
    	   db.execSQL("create table usertable(userID varchar primary key,password varchar)");
    	   db.execSQL("insert into usertable(userID,password)values(?,?)",
    			   new String[]{UserID,password});
    	   bool = true;
       }
       return bool;
   }
    public Word searchword(String word){
        Cursor cursor = null;
        boolean bool = false;
        ImportDB importdb = new ImportDB(context, "englishword");
        SQLiteDatabase db = importdb.openDatabase(true);
        Word w = new Word();
        cursor=db.rawQuery("select * from words where Word = ?",new String[]{word});
        if(cursor.moveToFirst()){
            do {
                w.setSpelling(cursor.getString(cursor.getColumnIndex("Word")));
                w.setExample(cursor.getString(cursor.getColumnIndex("lx")));
                w.setParaphrase(cursor.getString(cursor.getColumnIndex("meaning")));
                w.setSoundmark(cursor.getString(cursor.getColumnIndex("Soundmark")));
                w.setPronounce(cursor.getBlob(cursor.getColumnIndex("Pronounce")));
            }while (cursor.moveToNext());
        }
        return w;
    }
    public void updateOrder(List<Wordbook>list){
        System.out.println(list.get(0).getName());
        Cursor cursor;
        ImportDB importdb = new ImportDB(context,"wordbooklist");
        SQLiteDatabase db = importdb.openDatabase(true);
        for(int i = 0;i < list.size();i++ ) {
            if(list.get(i).getOrder()!= i+1 )
            db.execSQL("Update Wordbooklist set ord = ? where name = ?",new String[]{String.valueOf(i+1),list.get(i).getName()});
        }
    }
    public void addwordbooklist(Wordbook wb , String usestate){
        Cursor cursor;
        ImportDB importdb = new ImportDB(context,"wordbooklist");
        SQLiteDatabase db = importdb.openDatabase(true);
        cursor = db.rawQuery("select count(*) from Wordbooklist",null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.execSQL("insert into Wordbooklist(name,author,picture," +
                "Downnumber,Collectnumber,type,wordnumber,state,usestate,ord)values(?,?,?,?,?,?,?,?,?,?)",
        new String[]{wb.getName(),wb.getAuthor(),String.valueOf(wb.getPicture()),String.valueOf(wb.getDownnumber()),String.valueOf(wb.getCollectnumber())
        ,wb.getType(),String.valueOf(wb.getWordnumber()),String.valueOf(wb.getstate()),usestate,String.valueOf(count+1)});
    }
    public Wordbook openwordbook(String name){
        Cursor cursor;
        ImportDB importdb = new ImportDB(context,name);
        SQLiteDatabase db = importdb.openDatabase(true);
        Wordbook wd = new Wordbook();
        cursor=db.rawQuery("selet * from Wordbooklist where name = ?",new String[]{name});
        if(cursor.moveToFirst()){
            do {
                wd.setCollectnumber(cursor.getInt(cursor.getColumnIndex("Collectnumber")));
                wd.setDownnumber(cursor.getInt(cursor.getColumnIndex("Downnumber")));
                wd.setPicture(cursor.getBlob(cursor.getColumnIndex("picture")));
                wd.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                wd.setName(cursor.getString(cursor.getColumnIndex("name")));
                wd.setType(cursor.getString(cursor.getColumnIndex("type")));
                wd.setWordnumber(cursor.getInt(cursor.getColumnIndex("wordnumber")));
                wd.setState(cursor.getString(cursor.getColumnIndex("state")));
            }while (cursor.moveToNext());
        }
        return wd;
    }
    public List<Wordbook> getwordbooklist(String userstate){
        Cursor cursor;
        List<Wordbook> list =new ArrayList<Wordbook>();
        ImportDB importdb = new ImportDB(context,"wordbooklist");
        SQLiteDatabase db = importdb.openDatabase(true);
        if(userstate==null){
            cursor=db.rawQuery("select * from wordbooklist",new String[]{});
        }else{
        cursor=db.rawQuery("select * from wordbooklist where usestate = ? order by ord",new String[]{userstate});}
        if(cursor.moveToFirst()) {
            do {
                Wordbook wd = new Wordbook();
                wd.setCollectnumber(cursor.getInt(cursor.getColumnIndex("Collectnumber")));
                wd.setDownnumber(cursor.getInt(cursor.getColumnIndex("Downnumber")));
                wd.setPicture(cursor.getBlob(cursor.getColumnIndex("picture")));
                wd.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                wd.setName(cursor.getString(cursor.getColumnIndex("name")));
                wd.setType(cursor.getString(cursor.getColumnIndex("type")));
                wd.setWordnumber(cursor.getInt(cursor.getColumnIndex("wordnumber")));
                wd.setState(cursor.getString(cursor.getColumnIndex("state")));
                list.add(wd);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<Word> getwordfromwb(String name,String laststring){
        Cursor cursor=null;
        List<Word> list =new ArrayList<Word>();
        ImportDB importdb = new ImportDB(context,name);//
        ImportDB importdb1 = new ImportDB(context,"englishword");
        SQLiteDatabase db = importdb.openDatabase(true);
        SQLiteDatabase db1 = importdb1.openDatabase(true);
        int number = 0;
        if(db==null)return null;
        if(laststring==null){
            number=0;
        }else{
        cursor=db.rawQuery("select * from ? where name = ?",new String[]{name,laststring});
        if(cursor.moveToFirst()){
            do{
                number=cursor.getInt(cursor.getColumnIndex("rowid"));
            }while (cursor.moveToNext());
        }}
        cursor=db.rawQuery("select * from "+name+" order by rowid desc limit ?,?"
                ,new String[]{String.valueOf(number),String.valueOf(number+10)});
        if(cursor.moveToFirst()){
            do{
                Cursor cursor1=null;
                cursor1=db1.rawQuery("select * from words where Word = ?",
                        new String[]{cursor.getString(cursor.getColumnIndex("name"))});
                if(cursor1.moveToFirst()){
                do {
                    Word w = new Word();
                    w.setSpelling(cursor1.getString(cursor1.getColumnIndex("Word")));
                    w.setExample(cursor1.getString(cursor1.getColumnIndex("lx")));
                    w.setParaphrase(cursor1.getString(cursor1.getColumnIndex("meaning")));
                    w.setSoundmark(cursor1.getString(cursor1.getColumnIndex("Soundmark")));
                    w.setPronounce(cursor1.getBlob(cursor1.getColumnIndex("Pronounce")));
                    list.add(w);
                   }while (cursor1.moveToNext());
                }

            }while (cursor.moveToNext());
        }
        return list;
    }
    public void addword(String name,String word){
        ImportDB importdb = new ImportDB(context,name);
        SQLiteDatabase db = importdb.openDatabase(true);
        db.execSQL("insert into "+name+"(name)values(?);",new String[]{word});
    }
    public void addword(String name,String word,String familarity){
        ImportDB importdb = new ImportDB(context,name);
        SQLiteDatabase db = importdb.openDatabase(true);
        db.execSQL("insert into "+name+"(name,familarity)values(?,?)",new String[]{word});
    }
    public void deleteword(String name,String word){
        ImportDB importdb = new ImportDB(context,name);
        SQLiteDatabase db = importdb.openDatabase(true);
        db.execSQL("delete from "+name+" where name = ?",new String[]{word});
    }
    public boolean createwordbook(String name,String Discri,String Prim,byte[] picture){
        Cursor cursor=null;
        ImportDB importdb = new ImportDB(context,name);
        SQLiteDatabase db = importdb.openDatabase(true);
        db.execSQL("create table "+name+"([name] VARCHAR(100) PRIMARY KEY ON CONFLICT REPLACE)");
        importdb = new ImportDB(context,"wordbooklist");
        db = importdb.openDatabase(true);
        db.execSQL("insert into wordbooklist(name,author,picture,Downnumber," +
                "Collectnumber,type,wordnumber,state,usestate,ord,discri,prim)",new String[]{name,"asdsf",String.valueOf(picture),
        String.valueOf(0),String.valueOf(0),"备考",String.valueOf(0),String.valueOf(0),"自定义",String.valueOf(1),"fsgdh",String.valueOf(false)});
        return true;
    }
//    public List<Word> getword(String name){
//        Cursor cursor = null;
//        Cursor cursor1 = null;
//        ImportDB importdb = new ImportDB(context,name);
//        ImportDB importdb1 = new ImportDB(context,"mywords");
//        SQLiteDatabase db = importdb.openDatabase(true);
//        SQLiteDatabase db1 = importdb1.openDatabase(true);
//        List<Word> list =new ArrayList<Word>();
//        Random r = new Random();
//        String[] word = new String[10];
//        cursor = db.rawQuery("select count(*) from "+name,null);
//        cursor.moveToFirst();
//        long count = cursor.getLong(0);
//        int c=0;
//        while (c<10) {
//            int num = r.nextInt((int) count) + 1 % (int) count;
//            cursor = db.rawQuery("select name from " + name + " where rowid = ?", new String[]{String.valueOf(num)});
//            cursor.moveToFirst();
//            boolean bool=true;
//            for(int i=0;i<c;i++){
//                if(word[i].equals(cursor.getString(cursor.getColumnIndex("name"))))bool=false;
//            }
//            if(bool==false);else{
//            //System.out.println(cursor.getString(cursor.getColumnIndex("name")));
//            cursor1 = db1.rawQuery("select familarity from mywords where name = ?", new String[]{
//                    cursor.getString(cursor.getColumnIndex("name"))});
//            cursor1.moveToFirst();
//            if (cursor1.getCount() == 0) {
//                word[c] = cursor.getString(cursor.getColumnIndex("name"));
//                c++;
//            } else {
//                if (Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("familarity"))) == 3) {
//                    //r.nextInt((int)count+1);
//                } else {
//                    word[c] = cursor.getString(cursor.getColumnIndex("name"));
//                    c++;
//                }
//            }
//            }
//        }
//        System.out.println("word"+word[9]);
//        importdb = new ImportDB(context,"englishword");
//        db=importdb.openDatabase(true);
//        int d=0;
//        while (d<10){
//            cursor= db.rawQuery("select * from words where Word = ?",new String[]{word[d]});
//            cursor.moveToFirst();
//            Word w = new Word();
//            w.setSpelling(cursor.getString(cursor.getColumnIndex("Word")));
//            w.setExample(cursor.getString(cursor.getColumnIndex("lx")));
//            w.setParaphrase(cursor.getString(cursor.getColumnIndex("meaning")));
//            w.setSoundmark(cursor.getString(cursor.getColumnIndex("Soundmark")));
//            w.setPronounce(cursor.getBlob(cursor.getColumnIndex("Pronounce")));
//            list.add(w);
//            d++;
//        }
//        return list;
//    }
    public List<Word> getword(String name){
        Cursor cursor = null;
        Cursor cursor1 = null;
        ImportDB importdb = new ImportDB(context,name);
        ImportDB importdb1 = new ImportDB(context,"mywords");
        SQLiteDatabase db = importdb.openDatabase(true);
        SQLiteDatabase db1 = importdb1.openDatabase(true);
        List<Word> list =new ArrayList<Word>();
        List<String> word =new ArrayList<String>();
        Random r = new Random();
        cursor=db.rawQuery("select name from "+name,new String[]{});
        if(cursor.moveToFirst()){
            do {
                cursor1 = db1.rawQuery("select familarity from mywords where name = ?", new String[]{
                        cursor.getString(cursor.getColumnIndex("name"))});
                cursor1.moveToFirst();
                if(cursor1.getString(cursor1.getColumnIndex("familarity")).equals("3"));
                else word.add(cursor.getString(cursor.getColumnIndex("name")));
            }while (cursor.moveToNext());
        }
        int count = 0;
        if(word.size()>50)count=50;
        else count=word.size();
        String[] Word = new String[count];
        for(int i=0;i<count;i++) {
            int num=r.nextInt(word.size())+1%word.size();
            Word[i]=word.get(num);
            word.remove(num);
        }
        importdb = new ImportDB(context,"englishword");
        db=importdb.openDatabase(true);
        int c=0;
        while (c<count){
            cursor= db.rawQuery("select * from words where Word = ?",new String[]{Word[c]});
            cursor.moveToFirst();
            Word w = new Word();
            w.setSpelling(cursor.getString(cursor.getColumnIndex("Word")));
            w.setExample(cursor.getString(cursor.getColumnIndex("lx")));
            w.setParaphrase(cursor.getString(cursor.getColumnIndex("meaning")));
            w.setSoundmark(cursor.getString(cursor.getColumnIndex("Soundmark")));
            w.setPronounce(cursor.getBlob(cursor.getColumnIndex("Pronounce")));
            list.add(w);
            c++;
        }
        return list;
    }
    public void addintomywords(List<Word> L){
        ImportDB importdb = new ImportDB(context,"mywords");
        SQLiteDatabase db=importdb.openDatabase(true);
        for(int i=0;i<L.size();i++){
           db.execSQL("insert into mywords(name,familarity)values(?,?)",new String[]{L.get(i).getSpelling(),
                   String.valueOf(L.get(i).getFamilarity())});
        }
//        Cursor cursor =null;
//        cursor=db.rawQuery("select * from mywords where name=?",new String[]{L.get(1).getSpelling()});
//        cursor.moveToFirst();
//        System.out.println(cursor.getString(cursor.getColumnIndex("name")));
//        System.out.println(cursor.getString(cursor.getColumnIndex("familarity")));
    }
    public void addxmlnode(String word,String xmlDocname){

    }
    public void getxmlnode(String xmlDocname){
        ImportxmlDoc importxmlDoc = new ImportxmlDoc(context,xmlDocname);

    }
   void setContext(Context pcontext){
	  context = pcontext; 
   }
   Context getContext(){
	   return context;
   }
   private Context context;
}
