package cn.tedu.note.dao;

import java.util.List;
import java.util.Map;

import cn.tedu.note.entity.Note;

public interface NoteDao {
	List<Map<String, Object>> 
		findNotesByNotebookId(String notebookId);

	List<Map<String, Object>> 
		findTrashNotesByUserid(String userId);

	List<Map<String, Object>> 
		sharelist(String noteTitle);
	
	List<Map<String, Object>> 
		collectionlist(String userId);

	Note findNoteById(String noteId);

	int addShareNote(Map<String, Object> params);
	int addCollectNote(Map<String, Object> params);
	
	int updateNote(Map<String, Object> params);

	int deleteNote(Map<String, Object> params);
	int deleteComplete(Map<String, Object> params);
	int deleteCollect(Map<String, Object> params);
	int restoreNote(Map<String, Object> params);


	int addNote(Note note);
	
	int deleteNote(String id);
	
	int deleteNotes(List<String> list);
	
	int deleteNotesByParams(
		Map<String, Object> param);
	
	List<Map<String, Object>> 
		findNoteByParams(Map<String, Object> param);

	//分页查询参数：{notebookId:id,start:n,size:m}
	List<Map<String, Object>>  
		findNotesByNotebookIdPaged(
		Map<String, Object> param);

	//NoteDao 接口
	int countNotes(String notebookId);
	
}







