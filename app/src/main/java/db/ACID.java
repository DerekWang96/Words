package db;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
public class ACID {
   public ACID (Context context){
	   setContext(context);
   }
   public boolean addUserIDandpassword(String UserID,String password,String Databasename){
	   Cursor cursor = null;
	   boolean bool = false;
	   ImportDB importdb = new ImportDB(context, Databasename);
	   SQLiteDatabase db = importdb.openDatabase();
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
        SQLiteDatabase db = importdb.openDatabase();
        Word w = new Word();
        cursor=db.rawQuery("select * from words where Word = ?",new String[]{word});
        if(cursor.moveToFirst()){
            do {
                w.setSpelling(cursor.getString(cursor.getColumnIndex("Word")));
                w.setExample(cursor.getString(cursor.getColumnIndex("lx")));
                w.setParaphrase(cursor.getString(cursor.getColumnIndex("meaning")));
                w.setSoundmark(cursor.getString(cursor.getColumnIndex("Soundmark")));
                w.setPronounce(cursor.getString(cursor.getColumnIndex("Pronounce")));
            }while (cursor.moveToNext());
        }
        return w;
    }
   void setContext(Context pcontext){
	  context = pcontext; 
   }
   Context getContext(){
	   return context;
   }
   private Context context;
}
