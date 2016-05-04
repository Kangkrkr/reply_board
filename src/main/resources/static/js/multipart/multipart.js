
var setMultiformConfig = function() {
	
	$('.photo_uploader').MultiFile({
		max : 1, //업로드 최대 파일 갯수 (지정하지 않으면 무한대)
		accept : 'jpg|png|gif', //허용할 확장자(지정하지 않으면 모든 확장자 허용)
		maxfile : 1024, //각 파일 최대 업로드 크기
		maxsize : 3024, //전체 파일 최대 업로드 크기
		STRING : { //Multi-lingual support : 메시지 수정 가능
			remove : "제거", //추가한 파일 제거 문구, 이미태그를 사용하면 이미지사용가능
			duplicate : "$file 은 이미 선택된 파일입니다.",
			denied : "$ext 는(은) 업로드 할수 없는 파일 확장자입니다.",
			selected : '$file 을 선택했습니다.',
			toomuch : "업로드할 수 있는 최대 크기를 초과하였습니다.($size)",
			toomany : "업로드할 수 있는 최대 갯수는 $max개 입니다.",
			toobig : "$file 은 크기가 매우 큽니다. (max $size)"
		}
	});
};

var setCallbackToUploadForm = function(){
	$('#upload').ajaxForm({
        beforeSubmit: function (data,form,option) {
        	
            //validation체크 
            var photo = $('input[type=file]').val();
        	var content = $('#content').val();
        	var password = $('#password').val();
        	
        	if('' === content || undefined === content){
        		alert("글을 작성해 주세요.");
        		return false;
        	}
        	
            return true;
        },
        success: function(response,status){
            //성공 후 서버에서 받은 데이터 처리
            alert(response);
            $('#myModal').modal('hide');
            window.location.reload();
        },
        error: function(e){
            alert('글 게시에 실패하였습니다.');
            console.log(e);
        }                               
    });
};

