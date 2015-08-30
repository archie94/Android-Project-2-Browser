package com.example.zsurfer;

public class History 
{

	private int _id;
	private String _page;
	
	public History()
	{
		
	}
	public History(String page)
	{
		_page=page;
	}
	public void setId(int id)
	{
		this._id=id;
	}
	public void setHistory(String a)
	{
		this._page=a;
	}
	public int getId()
	{
		return this._id;
	}
	public String getHistory()
	{
		return this._page;
	}
}
