package db;
import android.content.Context;
import android.content.res.AssetManager;
import org.w3c.dom.Document;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
public class ImportxmlDoc {
    private final int BUFFER_SIZE = 10000;
    private String XML_NAME = null;
    public static final String PACKAGE_NAME = "com.example.words";
    public static final String DB_PATH = "data/data/" + PACKAGE_NAME;
    private String XML_FILEPATH = null;
    private Context context;
    public ImportxmlDoc(Context context,String xmlDocname){
        this.context=context;
        setxmlDoc(xmlDocname);
        setxmlDocFilePath();
    }
    void setxmlDoc(String Dbname){
        XML_NAME = Dbname;
    }
    void setxmlDocFilePath(){
        XML_FILEPATH = DB_PATH + "/" + XML_NAME + ".db";
    }
    public Document openxml(boolean newXML){
        File file = new File(XML_FILEPATH);
        Document Doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (file.exists()) {
                Doc = builder.parse(file);
            }else if(newXML == true){
                try{
                    AssetManager am = context.getAssets();
                    if(am == null){
                        file.createNewFile();
                    }else {
                        InputStream is = am.open(XML_NAME + ".xml");
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int count = 0;
                        while ((count = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Doc = openxml(true);
            }else Doc = null;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Doc;
    }
}
