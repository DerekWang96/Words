package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.*;
import java.io.Serializable;

/**
 * 测试类——Word
 * Created by 6gold on 2017/3/14.
 */

public class Word implements Serializable {
    private static final long serialVersionUID = 1L;

    public String spelling;//拼写
    public String soundmark;//音标
    public String meaning;//释义
    public String pronounce;//发音
    public String example;//例子
    public Word(){

    }
    public void setSpelling(String spelling) {
       this.spelling = spelling;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setParaphrase(String meaning) {
        this.meaning = meaning;
    }

    public String getParaphrase() {
        return meaning;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExample() {
        return example;
    }
    public String getSoundmark() {
        return soundmark;
    }

    public void setSoundmark(String soundmark) {
        this.soundmark = soundmark;
    }
}