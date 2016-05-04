var footerSetter = function() {
	$.ajax({
		type : "GET",
		url : "/list_size",
		success : function(result) {
			var totalSize = result.totalSize;
			var maxPageSize = result.maxPageSize;

			/* <![CDATA[ */
			for (var i = 1; i <= Math.ceil(totalSize / maxPageSize); i++) {
				$('#next').before(
						'<li><a href="/list?page=' + (i - 1) + '">' + i + ' </a></li>');
			}
			/* ]]> */
		},
		error : function() {	// xhr을 인자로 받아 에러코드 별로 오류내역을 출력해주도록 한다.
			alert('조회 중 오류가 발생했습니다.');
		}
	});
};

var doEdit = function(type, postId) {
	$.ajax({
		type : "GET",
		url : "/list/edit",
		data : {
			type : type,
			postId : postId
		},
		success : function(result) {
			alert(result);
			window.location.reload();
		},
		error : function() {
			alert('수정중 중 오류가 발생했습니다.');
		}
	});
};

var doDelete = function(type, postId) {
	$.ajax({
		type : "GET",
		url : "/delete",
		data : {
			type : type,
			postId : postId
		},
		success : function(result) {
			alert(result);
			window.location.reload();
		},
		error : function() {
			alert('삭제중 중 오류가 발생했습니다.');
		}
	});
};

var logout = function() {
	$.ajax({
		type : "POST",
		url : "/logout",
		success : function(result) {
			alert(result);
			location.href = '/form/login';
		},
		error : function() {
			alert('로그아웃 중 오류가 발생했습니다.');
		}
	});
};