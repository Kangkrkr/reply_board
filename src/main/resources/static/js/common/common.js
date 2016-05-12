
var initContent = function() {
	$('#content').val('');
}

//전송 역할을 하는 모달내의 버튼 클릭시 발생할 콜백 함수. 
var callback = function() {
	$('#postModal').modal('hide');
};

var openModal = function(type, targetId) {

	if (type === 'post') {
		$('input[type=hidden]').val('post');
	} else if (type === 'reply') {
		$('input[type=hidden]').val('reply#' + targetId);
	} else if (type === 'edit'){
		$('input[type=hidden]').val('edit#' + targetId);
	}

	// 입력 폼 초기화 후 보여주기.
	initContent();
	$('#postModal').modal('show');

	// 클릭시 수행될 콜백함수 지정.
	$('#sendButton').click(callback);

	// 모달이 화면에서 사라졌을 때 이벤트 지정. 버튼에 지정해둔 모든 콜백함수를 제거한다.
	$('#postModal').on('hidden.bs.modal', function(e) {
		$('#sendButton').unbind();
	});
};

var setScrollEvent = function() {
	$(window).scroll(function() {
		// 스크롤바 현재 위치.
		var curScrollbarPos = Math.ceil($(window).scrollTop());

		// footer가 사라지게 할 타겟 지점.
		var targetPos = ($(document).height() - $(window).height());

		if (curScrollbarPos >= targetPos) {
			$('#footer').fadeOut('fast');
		} else {
			$('#footer').fadeIn('fast');
		}
	});
};

$(function(){
	setFooter();
	setScrollEvent();
	setMultiformConfig();
	setCallbackToUploadForm();
});