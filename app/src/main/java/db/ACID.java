package db;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.List;

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
    public List<Word> getwordfromwb(String name,String laststring){
        Cursor cursor;
        List<Word> list =new ArrayList<Word>();
        ImportDB importdb = new ImportDB(context,name);
        SQLiteDatabase db = importdb.openDatabase(false);
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
        cursor=db.rawQuery("selet * from ? order by rowid desc limit ?,?",
                new String[]{name,String.valueOf(number+1),String.valueOf(number+20)});
        if(cursor.moveToFirst()){
            do{
                Word w = new Word();
                w.setSpelling(cursor.getString(cursor.getColumnIndex("Word")));
                w.setExample(cursor.getString(cursor.getColumnIndex("lx")));
                w.setParaphrase(cursor.getString(cursor.getColumnIndex("meaning")));
                w.setSoundmark(cursor.getString(cursor.getColumnIndex("Soundmark")));
                w.setPronounce(cursor.getBlob(cursor.getColumnIndex("Pronounce")));
                list.add(w);
            }while (cursor.moveToNext());
        }
        return list;
    }
   void setContext(Context pcontext){
	  context = pcontext; 
   }
   Context getContext(){
	   return context;
   }
   private Context context;
}
