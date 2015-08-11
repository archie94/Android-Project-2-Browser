package com.example.zsurfer;

public class BookMarks 
{
	private int _id;
	private String _address;
	 
	public BookMarks()
	{
		
	}
	public BookMarks(String _a)
	{
		this._address=_a;
	}
	public void setId(int id)
	{
		this._id=id;
	}
	public void setBookMark(String a)
	{
		this._address=a;
	}
	public int getId()
	{
		return this._id;
	}
	public String getBookMark()
	{
		return this._address;
	}
	
}
