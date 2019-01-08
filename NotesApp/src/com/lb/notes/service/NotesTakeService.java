package com.lb.notes.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lb.notes.model.*;
import com.lb.notes.doa.*;

@Path("/notes")
public class NotesTakeService  {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotes(@QueryParam("query") String search) 
	{
		List<NotesData> notes = NotesDOA.getNotes(search);
		
		if ( notes.isEmpty() )
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok().entity(notes).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNote(@PathParam("id") int id)
	{
		NotesData note = NotesDOA.getNote(id);
		
		if ( note == null )
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok().entity(note).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNote(NotesData data)
	{
		NotesData newNote = NotesDOA.addNote(data.getBody());
		 
		if ( newNote == null )
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok().entity(newNote).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteNote(@PathParam("id") int id)
	{
		int deleteOk = NotesDOA.deleteNote(id);
		
		if ( deleteOk == 1)
		{
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		else
		{
			String msg = "Note is NOT deleted for id=" + id;
			return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNode(NotesData data)
	{
		NotesData updNote = NotesDOA.updateNote(data.getId(), data.getBody());
		 
		if ( updNote == null )
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok().entity(updNote).build();
	}
}
