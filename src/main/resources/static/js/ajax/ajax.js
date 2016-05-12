var setFooter = function() {
	$.ajax({
		type : "GET",
		url : "/list_size",
		success : function(result) {
			
			// 게시글 수에 따른 최대 block index.
			var maxEnd = Math.ceil((result.totalSize) / (result.maxPageSize));
			
			// URL 주소상의 모든 파라미터를 가져온다.
			var params = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
			
			// split된 데이터들을 '='을 기준으로 한번더 잘라 key/value로 구분짓는다.
			var paramsList = [];
			for(var i=0; i < params.length; i++){
				var param = params[i].split('=');
				var key = param[0];
				var value = param[1];
				
				paramsList.push(key);
				paramsList[key] = value;
			}
			
			// 파라미터 리스트에서 page 파라미터에 대한 값 가져오기.
			var curPage = paramsList['page'];
			
			// 현재 페이지에 대한 start block index 설정.
			var start = (Math.floor((curPage - 1) / 5) * 5) + 1;
			// 현재 페이지에 대한 end block index 설정.
			var end = Math.ceil(curPage / 5) * 5;

			// 만약 위에서 설정한 end가 설정할 수 있는 최대 block index를 넘는다면 maxEnd로 설정한다.
			end = (end > maxEnd) ? maxEnd : end;
			
			// 현재 페이지가 제일 처음(1)의 block index와 같다면 '이전 버튼'은 클릭할 수 없게 만든다. 
			if(curPage == 1){
				$('#previous').addClass('disabled');
			}
			
			// 현재 페이지가 최대 block index(maxEnd)와 같다면 '다음 버튼'은 클릭할 수 없게 만든다.
			if(curPage == maxEnd){
				$('#next').addClass('disabled');
			}
			
			// '이전'이나 '다음'버튼이 'disabled'가 된 상태가 아니어야 이전이나 다음페이지로 넘어가게 설정.
			$('#previous').on('click', function(){
				if($(this).attr('class') !== 'disabled'){
					curPage = (parseInt(curPage) > 1) ? parseInt(curPage) - 1 : parseInt('1');
					location.href = '/list?page=' + curPage;
				}
			});
			
			$('#next').on('click', function(){
				if($(this).attr('class') !== 'disabled'){
					curPage = (parseInt(curPage) < maxEnd) ? parseInt(curPage) + 1 : parseInt(maxEnd);
					location.href = '/list?page=' + curPage;
				}
			});
			
			// 페이지 이동 block들을 추가하기.
			for (var i = start; i <= end; i++) {
				$('#next').before('<li><a href="/list?page=' + i + '">' + i + ' </a></li>');
			}
			
			/*
			$('#hide_footer').on('click', function(){
				$('#footer').fadeOut('fast');
			});
			*/
		},
		error : function() {
			alert('조회 중 오류가 발생했습니다.');
		}
	});
};

var doDelete = function(id) {
	$.ajax({
		type : "GET",
		url : "/delete",
		data : {
			id : id
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