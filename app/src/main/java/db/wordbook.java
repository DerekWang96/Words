package db;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
public class Wordbook implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String author;
	private byte[] picture;
	private int downnumber;
	private int collectnumber;
	private int wordnumber;
	private String state;
	private String type;
	private  int Order;
	private String discri;
	public String getState() {
		return state;
	}

	public void setOrder(int order) {
		Order = order;
	}

	public String getDiscri() {
		return discri;
	}

	public void setDiscri(String discri) {
		this.discri = discri;
	}

	public boolean getPrim() {
		return prim;
	}

	public void setPrim(boolean prim) {
		this.prim = prim;
	}

	private boolean prim;
	public int getCollectnumber() {
		return collectnumber;
	}
	public void setCollectnumber(int collectnumber) {
		this.collectnumber = collectnumber;
	}
	public int getDownnumber() {
		return downnumber;
	}
	public void setDownnumber(int downnumber) {
		this.downnumber = downnumber;
	}
	public void  setPicture(byte[] picture) {
		this.picture=picture;
	}
	public byte[] getPicture() {
		return picture;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getWordnumber() {
		return wordnumber;
	}
	public void setWordnumber(int wordnumber) {
		this.wordnumber = wordnumber;
	}
	public String getstate(){return state;}
	public void setState(String state){this.state = state;}
	public int getOrder(){return Order;}
	public void setOrder(){this.Order=Order;}
}

