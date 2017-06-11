package cn.tedu.note.service;

import java.util.List;
import java.util.Map;

import cn.tedu.note.entity.Note;

public interface NoteService {
	
	List<Map<String, Object>> listNotes(
			String notebookId)
			throws NotebookNotFoundException;

	public List<Map<String, Object>> listTrashNotes(
			String userId);

	public boolean restoreNote(String noteId);

	Note loadNote(String noteId)
		throws NoteNotFoundException;
	public boolean addShareNote(String noteId);
	
	boolean updateNote(String noteId, 
			String title, String body)
		throws NoteNotFoundException;

	boolean deleteNote(String noteId)
throws NoteNotFoundException;

public boolean deleteComplete(String noteId);

	Note addNote(String userId, 
		String notebookId, String title);

	int deleteNotes(String... ids);
	
	
	List<Map<String, Object>> listNotes(
			String notebookId,
			int page)
			throws NotebookNotFoundException;

	public List<Map<String, Object>> sharelist(
			String noteTitle);
	public boolean addCollectNote(String shareId, String userId);

	public List<Map<String, Object>> collectionlist(
			String userId);
	public boolean deleteCollect(String collectionId);
}





