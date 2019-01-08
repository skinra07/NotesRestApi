package com.lb.notes.model;

public class NotesData {

	int id;
	String body;
	
	public NotesData()
	{
		super();
	}
	
	public NotesData(int Id, String Body)
	{
		super();
		id = Id;
		body = Body;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getBody()
	{
		return body;
	}
}
