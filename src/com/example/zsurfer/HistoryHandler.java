package com.example.zsurfer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryHandler extends SQLiteOpenHelper 
{
	/*This class will handle the history of 
	 * browsing 
	 */

	
	private final static int DATABASE_VERSION=1;
	private final static String DATABASE_NAME="history.db";
	
	public final static String TABLE="historyTable";
	public final static String COLUMN_ID="_id";
	public final static String COLUMN_PG="pages";
	
	
	public HistoryHandler(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		String query="CREATE TABLE "+TABLE+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_PG+" TEXT );";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+TABLE);
		onCreate(db);
	}
	
	public void addHistory(History h)
	{
		ContentValues values=new ContentValues();
		values.put(COLUMN_PG,h.getHistory());
		SQLiteDatabase db=getWritableDatabase();
		db.insert(TABLE, null, values);
		db.close();
		
	}
	public void deleteHistory(String s)
	{
		SQLiteDatabase db=getWritableDatabase();
		db.execSQL("DELETE FROM "+TABLE+" WHERE "+COLUMN_PG+"=\""+s+"\";");
		
	}
	public String databaseToString()
	{
		SQLiteDatabase db=getWritableDatabase();
		String dbString="";
		String query="SELECT * FROM "+TABLE+" WHERE 1";
		Cursor c=db.rawQuery(query, null);
		c.moveToFirst();
		while(!c.isAfterLast())
		{
			if(c.getString(c.getColumnIndex("pages"))!=null)
			{
				dbString+=c.getString(c.getColumnIndex("pages"));
				dbString+="\n";
			}
			c.moveToNext();
		}
		db.close();
		return dbString;
		
	}

}
