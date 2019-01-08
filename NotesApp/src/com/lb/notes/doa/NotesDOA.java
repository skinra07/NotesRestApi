package com.lb.notes.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.util.logging.*;

import com.lb.notes.model.*;


public class NotesDOA {

	private final static Logger log = Logger.getLogger("com.lb.notes.doa"); 
	
	public static Connection Connect()
	{
		try 
		{
		    Context ctx = new InitialContext();
		    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/sqlite");
		    Connection conn = ds.getConnection();
		    return conn;
		}
		catch(SQLException  ex)
		{
			log.severe("JDBC Connection Error: " + ex.getMessage());
		} 
		catch (NamingException e) 
		{
			log.severe("JDBC Connection Context Error: " + e.getMessage());
		}
		return null;
	}
	
	public static NotesData addNote(String note)
	{
		NotesData nData = null;
		String sqlInsert = "INSERT INTO NOTES(BODY) VALUES(?)";
		try (Connection conn = Connect();
				PreparedStatement  pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS))
		{
			pstmt.setString(1, note );
			int status = pstmt.executeUpdate();
			if ( status == 1)
			{
				try ( ResultSet generatedKeys = pstmt.getGeneratedKeys() )
				{
					if (generatedKeys.next() )
					{
						int rowId = generatedKeys.getInt(1);
						System.out.println("rowId=" + rowId);
						if ( rowId > 0 )
						{
							nData = new NotesData(rowId,note);
						}
					}
				}
				catch(Exception ex)
				{
					log.severe("Error getting last rowId for Insert: " + ex.getMessage());
					System.out.println(ex.getMessage());
				}
			}
		}
		catch(SQLException ex)
		{
			log.severe("Insert Body in Notes Table Error: " + ex.getMessage());
			System.out.println(ex.getMessage());
		}
		
		return nData;
	}
	
	public static List<NotesData> getNotes(String query)
	{
		List<NotesData> notes = new ArrayList<NotesData>();
		String sqlQuery = "";
		if ( query != null && !query.isEmpty() )
		{
			sqlQuery = "SELECT * FROM NOTES WHERE BODY LIKE '%" + query + "%'";
		}
		else
		{
			sqlQuery = "SELECT * FROM NOTES";
		}
		
		try (Connection conn = Connect();
				Statement  stmt = conn.createStatement())
		{
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next() )
			{
				int id = rs.getInt(1);
				String body = rs.getString(2);
				
				NotesData note = new NotesData(id,body);
				notes.add(note);
			}
			
		} catch (SQLException ex) {
			log.severe("Error selecting Notes w/o query" + ex.getMessage());
		}
		
		return notes;
	}
	
	public static NotesData getNote(int Id)
	{
		NotesData note = null;
		String sqlQuery = "SELECT * FROM NOTES WHERE ID = ?";
		
		try (Connection conn = Connect();
				PreparedStatement  pstmt = conn.prepareStatement(sqlQuery))
		{
			pstmt.setInt(1, Id);
			ResultSet rs = pstmt.executeQuery();
			
			if( rs.next() )
			{
				int id = rs.getInt(1);
				String body = rs.getString(2);
				
				note = new NotesData(id,body);
			}
			
		} catch (SQLException ex) {
			log.severe("Error selecting Notes for id=" + Id + " :" + ex.getMessage());
		}
		
		return note;
	}
	
	public static NotesData updateNote(int Id, String note)
	{
		NotesData updNote = null;
		
		String sqlUpd = "UPDATE NOTES SET BODY = ? WHERE ID = ?";
		
		try (Connection conn = Connect();
				PreparedStatement  pstmt = conn.prepareStatement(sqlUpd))
		{
			pstmt.setString(1, note);
			pstmt.setInt(2, Id);
			
			int status = pstmt.executeUpdate();
			
			if ( status == 1 )
			{
				updNote = new NotesData(Id, note);
			}
		}
		catch(SQLException ex)
		{
			log.severe("Error updating Note with [id/body] = [" + Id + "/" + note + "] :"  + ex.getMessage());
		}
		
		return updNote;
	}
	
	public static int deleteNote(int Id)
	{
		int status = 0;
		
		String sqlDel = "DELETE FROM NOTES WHERE ID = ?";
		
		try (Connection conn = Connect();
				PreparedStatement  pstmt = conn.prepareStatement(sqlDel))
		{
			pstmt.setInt(1, Id);
			
			status = pstmt.executeUpdate();
		}
		catch(SQLException ex)
		{
			log.severe("Error deleting Note with id=" + Id + " :" + ex.getMessage());
		}
		
		return status;
	}
}
