/* scripts/edit_init.js */

var SUCCESS = 0;
var deleteNoteId = 0;
var deleteNoteBookId = 0;
var deleteCollectId = 0;
var deleteNoteCompleteId = 0;
var renameNoteBookId = 0;
var noteShareTemplate='<li class="online">'+
	'<a>'+
	'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>'+
	'[title]<button type="button" class="btn btn-default btn-xs btn_position btn_collect" title="放入收藏夹"><i class="fa fa-star"></i></button>'+
	'</a>'+
	'</li>';

$(function(){
	document.onkeydown=function(event){
		event = window.event;
		if(event && event.keyCode==13){
			if ($('#search_note').val() != '') {

				var url='note/sharelist.do';
				var data = {noteTitle: $('#search_note').val()};
				$.post(url, data, function(result){
					if(result.state==SUCCESS){
						var notes = result.data;
						var ul = $('#pc_part_6 ul');
						ul.empty();
						for(var i=0; i<notes.length; i++){
					 		var note=notes[i];
					 		li = noteShareTemplate.replace(
					 			'[title]', note.noteTitle);
					 		li = $(li).data('shareId', note.shareId);
					 		
					 		ul.append(li);
					 	}
					 	$('#pc_part_6').show();
						$('.opacity_bg').show();
						$('#search_note').val("");				
					}else{
						alert(result.message);
					}
				});

			}
		}
	}
	//初始化 edit.html 页面
	//var userId=getCookie('userId');
	//console.log(userId);
	
	//在网页加载以后立即加载笔记本列表信息
	loadNotebooksAction();
	//点击#add_notebook时候打开添加笔记本对话框
	$('#add_notebook').click(openAddNotebookDialog);

	$('#logout').click(function() {
		$.post("user/logout.do", function(result){
			window.location.href = "login_in.html"
		})
	});

	$('#searchClose').click(closeSearchList);
	//绑定笔记本对话框中的添加笔记本按钮事件
	$('#can').on('click', 
		'.add-notebook', addNotebookAction);
	//利用事件冒泡,在can上绑定关闭按钮
	$('#can').on('click',
		'.close,.cancel',closeDialog);
	
	//绑定笔记本列表点击事件
	//$('#notebooks').on('click','li',showNotesAction);
	
	//绑定“翻页”笔记本列表点击事件
	$('#notebooks').on('click','li',
			showPagedNotesAction);


	//绑定笔记列表点击事件
	$('#pc_part_2 ul').on('click',
			'li.note', loadNoteAction);
	//绑定more事件
	$('#pc_part_2 ul').on('click',
			'li.more', nextPageNotesAction);

	
	//绑定保存笔记事件
	$('#save_note').click(updateNoteAction);
	
	//绑定添加笔记按钮事件
	$('#add_note').click(openAddNoteDialog);
	//绑定添加笔记窗口中的 确定按钮事件
	$('#can').on('click', '.add-note', addNoteAction);
	
	//显示回收站
	$('#rollback_button').click(switchRollback);
	$('#like_button').click(switchCollection);
	
	//绑定显示笔记子菜单的弹出事件
	$('#pc_part_2')
		.on('click','li .btn_slide_down', 
		showNoteSubMenu);
	//关闭子菜单
	$('body').click(hideNoteSubMenu);
	
	//绑定删除笔记按钮事件
	$('#pc_part_2 ul')
		.on('click', 'li .btn_delete',
		deleteNoteAction);

	$('#pc_part_7 ul')
		.on('click', 'li .btn_collect_delete',
		deleteCollectNoteAction);

	$('#pc_part_2 ul')
		.on('click', 'li .btn_share',
		shareNoteAction);

	$('#can').on('click', '.delete-note', deleteNoteConfirm);
	$('#can').on('click', '.delete-note-complete', deleteNoteCompleteConfirm);
	$('#can').on('click', '.delete-collect', deleteCollectNoteConfirm);
	$('#can').on('click', '.delete-note-book', deleteNoteBookConfirm);
	$('#can').on('click', '.btn-rename', renameNoteBookActionConfirm);

	$('#pc_part_4 ul')
		.on('click', 'li .btn_replay',
		recoverNoteAction);

	$('#pc_part_4 ul')
		.on('click', 'li .btn_delete',
		deleteNoteComplete);

	$('#pc_part_1 ul')
		.on('click', 'li .btn_delete',
		deleteNoteBookAction);

	$('#pc_part_1 ul')
		.on('click', 'li .btn_rename',
		renameNoteBookAction);

	$('#pc_part_6 ul')
		.on('click', 'li .btn_collect',
		addCollection);

	$('#pc_part_7 ul')
		.on('click', 'li',
		showCollectionNote);
	
});

function showCollectionNote() {
	var btn = $(this);
	var body=btn.data('body');

	if (body == "") {
		body = "笔记内容为空";
	}
	
	$('#can').load('./alert/note_preview.html',function(){
		$('#error_info').html(body);
		$('.opacity_bg').show();
	});
}

function deleteNoteComplete() {
	var btn = $(this);
	deleteNoteCompleteId=btn.parents('li').data('noteId');

	var url="alert/alert_delete_note_complete.html";
	$('#can').load(url);
	$('.opacity_bg').show();
}

function deleteNoteCompleteConfirm() {
	var url='note/deleteComplete.do';
	var data = {
		noteId : deleteNoteCompleteId
	}
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
			$('#can').load('./alert/alert_confirm.html',function(){
				$('#error_info').text(' 删除成功');
				$('.opacity_bg').show();
			});
			getTrashBin();
		}else{
			alert(result.message);
		}
	});
}

function deleteCollectNoteAction() {
	var btn = $(this);
	deleteCollectId=btn.parents('li').data('collectionId');

	var url="alert/alert_delete_like.html";
	$('#can').load(url);
	$('.opacity_bg').show();
}

function addCollection() {
	var btn = $(this);
	var id=btn.parents('li').data('shareId');

	var data = {
		shareId : id,
		userId : getCookie('userId')
	}
	var url='note/collect.do';
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
		$('#can').load('./alert/alert_confirm.html',function(){
			$('#error_info').text(' 收藏成功');
			$('.opacity_bg').show();
		});
		}else{
			alert(result.message);
		}
	});
} 

function closeSearchList() {
	$('#pc_part_6').hide();
	$('.opacity_bg').hide();	
}

function shareNoteAction() {
	var btn = $(this);
	var id=btn.parents('li').data('noteId');
	var data = {
		noteId : id
	}
	var url='note/share.do';
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
		$('#can').load('./alert/alert_confirm.html',function(){
			$('#error_info').text(' 分享成功');
			$('.opacity_bg').show();
		});
		}else{
			alert(result.message);
		}
	});
}

function deleteNoteBookAction(event) {
	event.stopPropagation(); 
	var btn = $(this);
	deleteNoteBookId = btn.parents('li').data('notebookId');
	
	var url="alert/alert_delete_notebook.html";
	$('#can').load(url);
	$('.opacity_bg').show();

}

function renameNoteBookAction(event) {
	event.stopPropagation(); 
	var btn = $(this);
	renameNoteBookId = btn.parents('li').data('notebookId');
	
	var url="alert/alert_rename.html";
	$('#can').load(url);
	$('.opacity_bg').show();

}

function deleteNoteAction(){
	var btn = $(this);
	var id=btn.parents('li').data('noteId');
	deleteNoteId = id;

	var url="alert/alert_delete_note.html";
	$('#can').load(url);
	$('.opacity_bg').show();
}

function deleteNoteConfirm() {
	// console.log('删除'+ deleteNoteId);
	var data = {
		noteId : deleteNoteId
	}
	closeDialog();
	var url='note/delete.do';
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
		$('#can').load('./alert/alert_confirm.html',function(){
			$('#error_info').text(' 删除成功');
			$('.opacity_bg').show();
		});
		nextPageNotesAction(true);
		}else{
			alert(result.message);
		}
	});
}

function deleteCollectNoteConfirm() {
	var data = {
		collectionId : deleteCollectId
	}
	closeDialog();
	var url='note/deleteCollect.do';
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
		$('#can').load('./alert/alert_confirm.html',function(){
			$('#error_info').text(' 删除收藏笔记成功');
			$('.opacity_bg').show();
		});
		getCollection();
		}else{
			alert(result.message);
		}
	});
}

function deleteNoteBookConfirm() {
	var data = {
		noteBookId : deleteNoteBookId
	}
	closeDialog();
	var url='notebook/delete.do';
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
		$('#can').load('./alert/alert_confirm.html',function(){
			$('#error_info').text(' 删除成功');
			$('.opacity_bg').show();
		});
		loadNotebooksAction();
		$('#pc_part_2 ul').empty();
		$('#pc_part_2 ul').data('notebookId', false);
		}else{
			alert(result.message);
		}
	});
}

function renameNoteBookActionConfirm() {
	var name = $('#input_notebook_rename').val();
	if (name == "") {
		alert("笔记本名不能为空");
		return;
	}

	var data = {
		noteBookId : renameNoteBookId,
		name : name
	}
	closeDialog();
	var url='notebook/rename.do';
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
		$('#can').load('./alert/alert_confirm.html',function(){
			$('#error_info').text('重命名成功');
			$('.opacity_bg').show();
		});
		loadNotebooksAction();
		$('#pc_part_2 ul').empty();
		$('#pc_part_2 ul').data('notebookId', false);
		}else{
			alert(result.message);
		}
	});
}

function recoverNoteAction() {
	var btn = $(this);
	var id=btn.parents('li').data('noteId');
	
	var url='note/restore.do';
	var data = {
		noteId : id
	}
	$.post(url, data, function(result){
		if(result.state==SUCCESS){
			$('#can').load('./alert/alert_confirm.html',function(){
				$('#error_info').text(' 还原成功');
				$('.opacity_bg').show();
			});
			getTrashBin();
		}else{
			alert(result.message);
		}
	});
}

function hideNoteSubMenu(){
	$('#pc_part_2 .note_menu').hide(200);
}

function showNoteSubMenu(){
	var btn=$(this);
	btn.parent().next().toggle(200);
	return false;
}

function switchRollback(){
	//检查回收站是否打开，如果打开就关闭，并且
	//开启笔记列表，如果是关闭状态就关闭其他列表
	//回收站
	
	//pc_part_8 //参加活动的笔记列表
	//pc_part_7 //收藏笔记列表 
	//pc_part_6 //搜索笔记列表
	//pc_part_4 //回收站
	//pc_part_2 //笔记列表
	$('#pc_part_8').hide();
	$('#pc_part_7').hide();
	$('#pc_part_6').hide();
	if($('#pc_part_4').css('display')=='block'){
		$('#pc_part_4').hide();
		$('#pc_part_2').show();
	}else{
		$('#pc_part_2').hide();
		$('#pc_part_4').show();
		getTrashBin();
	}
}

function switchCollection() {
	$('#pc_part_8').hide();
	$('#pc_part_4').hide();
	$('#pc_part_6').hide();
	if($('#pc_part_7').css('display')=='block'){
		$('#pc_part_7').hide();
		$('#pc_part_2').show();
	}else{
		$('#pc_part_2').hide();
		$('#pc_part_7').show();
		getCollection();
	}
}

function getCollection() {
	var url='note/collectionList.do';
	var data={userId:getCookie('userId')};
	$.getJSON(url, data, function(result){
		if(result.state==SUCCESS){
			var list=result.data;
			showCollectionNotes(list);
		}else{
			alert(result.message);
		}
	});
}

function getTrashBin() {
	var url='note/trashList.do';
	var data={userId:getCookie('userId')};
	$.getJSON(url, data, function(result){
		if(result.state==SUCCESS){
			var list=result.data;
			showTrashNotes(list);
		}else{
			alert(result.message);
		}
	});

}

var noteTrashTemplate='<li class="disable">'+
	'<a>'+
	'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>'+
	'[title]<button type="button" class="btn btn-default btn-xs btn_position btn_delete" title="彻底删除"><i class="fa fa-times"></i></button>'+
	'<button type="button" class="btn btn-default btn-xs btn_position_2 btn_replay" title="还原"><i class="fa fa-reply"></i></button>' + 
	'</a>'+
	'</li>';

var noteCollectionTemplate='<li class="disable">'+
	'<a>'+
	'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>'+
	'[title]<button type="button" class="btn btn-default btn-xs btn_position btn_collect_delete" title="删除"><i class="fa fa-times"></i></button>'+
	'</a>'+
	'</li>';

function showTrashNotes(notes){
	//找到UL元素
 	var ul = $('#pc_part_4 ul');
 	ul.empty();
 	if(notes.length == 0){
 		return;
 	}
 	//console.log(ul);
 	for(var i=0; i<notes.length; i++){
 		var note=notes[i];
 		li = noteTrashTemplate.replace(
 			'[title]', note.title);
 		li = $(li).data('noteId', note.id);
 		
 		ul.append(li);
 	}
}

function showCollectionNotes(notes){
	//找到UL元素
 	var ul = $('#pc_part_7 ul');
 	ul.empty();
 	if(notes.length == 0){
 		return;
 	}
 	//console.log(ul);
 	for(var i=0; i<notes.length; i++){
 		var note=notes[i];
 		li = noteCollectionTemplate.replace(
 			'[title]', note.title);
 		li = $(li).data('collectionId', note.collectionId).data('body', note.body);
 		ul.append(li);
 	}
}

function closeDialog(){
	if ($('#pc_part_6').css('display') == "none") {
		$('.opacity_bg').hide();
	}
	$('#can').empty();
}








