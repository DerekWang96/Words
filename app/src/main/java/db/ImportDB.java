package db;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class ImportDB {
	private final int BUFFER_SIZE = 10000;
	private String DB_NAME = null;
	public static final String PACKAGE_NAME = "com.example.words";
	public static final String DB_PATH = "data/data/" + PACKAGE_NAME;
	private String DB_FILEPATH = null;
	private Context context;

	public ImportDB(Context context,String Databasename) {
		this.context = context;
		setDatabasename(Databasename);
		setDatabaseFilePath();
	}
    void setDatabasename(String Dbname){
    	DB_NAME = Dbname;
    }
    void setDatabaseFilePath(){
    	DB_FILEPATH = DB_PATH + "/" + DB_NAME + ".db";
    }
	public SQLiteDatabase openDatabase(boolean newdb) {
		System.out.println("filePath:" + DB_FILEPATH);
		File jhPath = new File(DB_FILEPATH);
		if (jhPath.exists()) {
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else if (newdb=true){
			File path = new File(DB_PATH);
			Log.i(DB_NAME, "pathStr=" + path);
			if (path.mkdir()) {
			} else {
			}
			try {
				AssetManager am = context.getAssets();
				if(am == null){
				  SQLiteDatabase.openOrCreateDatabase(jhPath, null);				
				}else{
				InputStream is = am.open(DB_NAME + ".db");
				FileOutputStream fos = new FileOutputStream(jhPath);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
				fos.close();
				is.close();
				}} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return openDatabase(true);
		}else return  null;
	}
}
