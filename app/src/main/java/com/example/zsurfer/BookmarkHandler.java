package com.example.zsurfer;

import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.*;
 
public class BookmarkHandler extends SQLiteOpenHelper {
	
	private final static int DATABASE_VERSION=1;
	private final static String DATABASE_NAME="bkmrk.db";
	
	public final static String TABLE="bookmarkTable";
	public final static String COLUMN_ID="_id";
	public final static String COLUMN_BK="bookmarks";
	
	public BookmarkHandler(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query="CREATE TABLE "+TABLE+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_BK+" TEXT );";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE);
		onCreate(db);
	}

	public void addDB(BookMarks bk) {
		ContentValues values=new ContentValues();
		values.put(COLUMN_BK,bk.getBookMark());
		SQLiteDatabase db=getWritableDatabase();
		db.insert(TABLE, null, values);
		db.close();
	}

	public void deleteDB(String bdDEL) {
		SQLiteDatabase db=getWritableDatabase();
		db.execSQL("DELETE FROM "+TABLE+" WHERE "+COLUMN_BK+"=\""+bdDEL+"\";");
	}

	public String databaseToString() {
		SQLiteDatabase db=getWritableDatabase();
		String dbString="";
		String query="SELECT * FROM "+TABLE+" WHERE 1";
		Cursor c=db.rawQuery(query, null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			if(c.getString(c.getColumnIndex("bookmarks"))!=null) {
				dbString+=c.getString(c.getColumnIndex("bookmarks"));
				dbString+="\n";
			}
			c.moveToNext();
		}
		db.close();
		return dbString;
	}
}