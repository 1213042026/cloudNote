package cn.tedu.note.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.note.dao.NoteDao;
import cn.tedu.note.dao.NotebookDao;
import cn.tedu.note.dao.UserDao;
import cn.tedu.note.entity.Note;
import cn.tedu.note.entity.Notebook;
import cn.tedu.note.entity.User;
import cn.tedu.note.service.NoteNotFoundException;
import cn.tedu.note.service.NoteService;
import cn.tedu.note.service.NotebookNotFoundException;
import cn.tedu.note.service.UserNotFoundException;

@Service("noteService")
public class NoteServiceImpl implements NoteService {

	@Resource
	private NoteDao noteDao;
	
	@Resource 
	private NotebookDao notebookDao;
	
	@Resource
	private UserDao userDao;
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> listNotes(
			String notebookId) throws NotebookNotFoundException {
		if(notebookId==null||notebookId.trim().isEmpty()){
			throw new NotebookNotFoundException("notebookId不能空");
		}
		Notebook book = notebookDao.findNotebookById(notebookId);
		if(book == null){
			throw new NotebookNotFoundException("notebookId不存在");
		}
		
		return noteDao.findNotesByNotebookId(notebookId);
	}

	@Transactional(readOnly=true)
	public List<Map<String, Object>> sharelist(
			String noteTitle) throws NotebookNotFoundException {
		if(noteTitle==null||noteTitle.trim().isEmpty()){
			throw new NotebookNotFoundException("noteTitle不能空");
		}
		
		return noteDao.sharelist(noteTitle);
	}

	@Transactional(readOnly=true)
	public List<Map<String, Object>> collectionlist(
			String userId) throws NotebookNotFoundException {
		if(userId==null||userId.trim().isEmpty()){
			throw new NotebookNotFoundException("userId不能空");
		}
		
		return noteDao.collectionlist(userId);
	}

	@Transactional(readOnly=true)
	public List<Map<String, Object>> listTrashNotes(
			String userId) throws NotebookNotFoundException {
		if(userId==null||userId.trim().isEmpty()){
			throw new UserNotFoundException("userId不能空");
		}
		User user = userDao.findUserById(userId);
		if(user == null){
			throw new UserNotFoundException("userId不存在");
		}
		
		return noteDao.findTrashNotesByUserid(userId);
	}
	
	@Transactional
	public Note loadNote(String noteId) 
			throws NoteNotFoundException {
		if(noteId==null || noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("ID错误了");
		}
		return note;
	}
	
	@Transactional
	public boolean updateNote(String noteId, 
			String title, String body) 
			throws NoteNotFoundException {
		if(noteId==null||noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("ID错");
		}
		//创建Map, 封装更新参数
		//如 title 是 null 则不更新title
		Map<String, Object> params = 
			new HashMap<String, Object>();
		if(title!=null &&
				! title.trim().isEmpty()){
			params.put("title", title.trim());	
		}
		//笔记本内容为null, 不更新笔记内容
		if(body!=null){
			if(!body.equals(note.getBody())){
				params.put("body", body.trim());
			}
		}
		//如果title 和 body 都没有,则无需更新?
		if(params.isEmpty()){
			return false;//更新失败
		}
		//添加必须参数
		params.put("id", noteId);
		params.put("lastModifyTime", 
			System.currentTimeMillis());
		//更新数据
		int n =noteDao.updateNote(params);
		return n==1;
//		if(n==1){
//			return true;//更新成功
//		}
//		return false;//更新失败
	}

	@Transactional
	public boolean deleteNote(String noteId) 
			throws NoteNotFoundException {
		if(noteId==null||noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("ID错");
		}
		//创建Map, 封装更新参数
		//如 title 是 null 则不更新title
		Map<String, Object> params = 
			new HashMap<String, Object>();
		params.put("id", noteId);
		//更新数据
		int n =noteDao.deleteNote(params);
		return n==1;
//		if(n==1){
//			return true;//更新成功
//		}
//		return false;//更新失败
	}

	@Transactional
	public boolean deleteComplete(String noteId) 
			throws NoteNotFoundException {
		if(noteId==null||noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("ID错");
		}
		//创建Map, 封装更新参数
		//如 title 是 null 则不更新title
		Map<String, Object> params = 
			new HashMap<String, Object>();
		params.put("id", noteId);
		//更新数据
		int n =noteDao.deleteComplete(params);
		return n==1;
//		if(n==1){
//			return true;//更新成功
//		}
//		return false;//更新失败
	}

	@Transactional
	public boolean deleteCollect(String collectionId) 
			throws NoteNotFoundException {
		if(collectionId==null||collectionId.trim().isEmpty()){
			throw new NoteNotFoundException("collectId不能空");
		}
		//创建Map, 封装更新参数
		//如 title 是 null 则不更新title
		Map<String, Object> params = 
			new HashMap<String, Object>();
		params.put("id", collectionId);
		//更新数据
		int n =noteDao.deleteCollect(params);
		return n==1;
//		if(n==1){
//			return true;//更新成功
//		}
//		return false;//更新失败
	}

	@Transactional
	public boolean restoreNote(String noteId) 
			throws NoteNotFoundException {
		if(noteId==null||noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("ID错");
		}
		//创建Map, 封装更新参数
		//如 title 是 null 则不更新title
		Map<String, Object> params = 
			new HashMap<String, Object>();
		params.put("id", noteId);
		//更新数据
		int n =noteDao.restoreNote(params);
		return n==1;
//		if(n==1){
//			return true;//更新成功
//		}
//		return false;//更新失败
	}
	
	@Transactional
	public Note addNote(String userId,
		String notebookId, String title) {
		if(notebookId==null||notebookId.trim().isEmpty()){
			throw new NotebookNotFoundException("notebookId不能空");
		}
		Notebook book = notebookDao.findNotebookById(notebookId);
		if(book == null){
			throw new NotebookNotFoundException("notebookId不存在");
		}
		if(userId==null||userId.trim().isEmpty()){
			throw new UserNotFoundException("userId不能空");
		}
		User user = userDao.findUserById(userId);
		if(user == null){
			throw new UserNotFoundException("userId不存在");
		}
		if(title==null || title.trim().isEmpty()){
			throw new RuntimeException("title不存在");
		}
		String id = UUID.randomUUID().toString();
		title = title.trim();
		String body = "";
		String typeId="0";
		String statusId="1";
		long now = System.currentTimeMillis();
		Note note = new Note(id, notebookId, userId, statusId, typeId, title, body, now, now);
		int n = noteDao.addNote(note);
		if( n == 1){
			
			return note;
		}
		throw new RuntimeException("保存失败!");

	}

	@Transactional
	public boolean addShareNote(String noteId) {
		if(noteId==null || noteId.trim().isEmpty()){
			throw new NoteNotFoundException("分享笔记ID不能空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("分享笔记ID错误了");
		}

		String id = UUID.randomUUID().toString();
		Map<String, Object> map=
			new HashMap<String, Object>();
		map.put("noteId", noteId);
		map.put("id", id);
		int n = noteDao.addShareNote(map);
		if( n == 1){
			
			return true;
		}
		throw new RuntimeException("分享失败!");

	}

	@Transactional
	public boolean addCollectNote(String shareId, String userId) {
		if(shareId==null || shareId.trim().isEmpty()){
			throw new NoteNotFoundException("分享ID不能空");
		}
		
		if(userId==null || userId.trim().isEmpty()){
			throw new NoteNotFoundException("用户ID不能空");
		}

		String id = UUID.randomUUID().toString();
		Map<String, Object> map=
			new HashMap<String, Object>();
		map.put("shareId", shareId);
		map.put("userId", userId);
		map.put("id", id);
		int n = noteDao.addCollectNote(map);
		if( n == 1){
			
			return true;
		}
		throw new RuntimeException("收藏失败!");

	}
//	@Transactional
//	public void test(){
//		deleteNotes();
//		addNote(userId, notebookId, title);
//	}
	
	@Transactional
	public int deleteNotes(String... ids) {
		//String... 就是 String[] 
//		for(String id: ids){
//			int n = noteDao.deleteNote(id);
//			if(n!=1){
//				throw new NoteNotFoundException(id);
//			}
//		}
//		return ids.length;
		int n = noteDao.deleteNotes(
				Arrays.asList(ids));
		if(n!=ids.length){
			throw new NoteNotFoundException("id错");
		}
		return n;
	}
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> 
		listNotes(String notebookId, int page)
		throws NotebookNotFoundException {
		
		if(notebookId==null || notebookId.trim().isEmpty()){
			throw new NotebookNotFoundException("ID NULL");
		}
		Notebook book=notebookDao.findNotebookById(notebookId);
		if(book==null){
			throw new NotebookNotFoundException("ID错误");
		}
		//计算分页参数
		int size = 6;
		int start = page * size;
		//检查start是否有效
		if(start < 0){
			start = 0;
		}
		int max = 
			noteDao.countNotes(notebookId);
		if(start>=max){
			return new 
			ArrayList<Map<String,Object>>();
		}
		//拼凑参数 
		Map<String, Object> map=
			new HashMap<String, Object>();
		map.put("notebookId", notebookId);
		map.put("start", start);
		map.put("size", size);
		//分页查询
		return noteDao.findNotesByNotebookIdPaged(map);
	}
}











