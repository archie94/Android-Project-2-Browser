package com.example.zsurfer;

import android.content.Context;
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
		super(context, name, factory, version);
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
		
	}
	public void deleteHistory(String s)
	{
		
	}
	public void databaseToString()
	{
		
	}

}
