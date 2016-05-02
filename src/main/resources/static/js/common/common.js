var initContent = function() {
	$('#content').val('');
	$('#password').val('');
}

var openModal = function() {

	var args = arguments;

	if (args[0] === 'post') {
		//writePost(args[1]);
		$('input[type=hidden]').val('post');
	} else if (args[0] === 'reply') {
		//reply(args[1], args[2], $('#content').val(), $('#password').val());
		$('input[type=hidden]').val('reply#' + args[1]);
	} else if (args[0] === 'reply_on_reply') {
		$('input[type=hidden]').val('reply_on_reply#' + args[1]);
	}else{
		if(args[0] === 'edit_post'){
			$('input[type=hidden]').val('edit_post#' + args[1]);
		}else{
			$('input[type=hidden]').val('edit_reply#' + args[1]);
		}
	}

	// 전송 역할을 하는 모달내의 버튼 클릭시 발생할 콜백 함수. 
	var callback = function() {
		$('#postModal').modal('hide');
	};

	// 입력 폼 초기화 후 보여주기.
	initContent();
	$('#postModal').modal('show');
	$('#upload').find('#content').focus();

	// 클릭시 수행될 콜백함수 지정.
	$('#postModal #sendButton').click(callback);

	// 모달이 화면에서 사라졌을 때 이벤트 지정. 버튼에 지정해둔 모든 콜백함수를 제거한다.
	$('#postModal').on('hidden.bs.modal', function(e) {
		$('#postModal #sendButton').unbind();
	});
}

var editArticle = function(type, postId, currentUser, uploader) {

	if (currentUser !== uploader) {
		alert('자신의 게시물만 수정할 수 있습니다.');
		return;
	}
	
	openModal(type, postId);

	console.log("뭐지");
	
	//location.href="/list/edit?type=" + type + "&" + "postId=" + postId;
	//doEdit(type, postId);
}

var deleteArticle = function(type, currentUser, uploader, postId) {

	if (currentUser !== uploader) {
		alert('자신의 게시물만 삭제할 수 있습니다.');
		return;
	}

	doDelete(type, postId);
}

var setScrollEvent = function() {
	$(window).scroll(function() {
		// 스크롤바 현재 위치.
		var curScrollbarPos = Math.ceil($(window).scrollTop());

		// footer가 사라지게 할 타겟 지점.
		var targetPos = $(document).height() - $(window).height();

		if (curScrollbarPos >= targetPos - 10) {
			$('#footer').fadeOut('fast');
		} else {
			$('#footer').fadeIn('fast');
		}
	});
}

$(function(){
	footerSetter();
	setScrollEvent();
	setMultiformConfig();
	setCallbackToUploadForm();
});