package com.example.zsurfer;

public class Bookmark 
{

	
	private int _id;
	private String _bkmk;
	
	public Bookmark()
	{
		
	}
	public Bookmark(String b)
	{
		_bkmk=b;
	}
	public void setId(int id)
	{
		this._id=id;
	}
	public void setHistory(String a)
	{
		this._bkmk=a;
	}
	public int getId()
	{
		return this._id;
	}
	public String getHistory()
	{
		return this._bkmk;
	}
}
