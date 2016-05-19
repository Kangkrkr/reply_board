/*!  2016-05-18 */
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
			var curPage = parseInt(paramsList['page']);
			
			if(curPage <= 1) curPage = 1;
			if(maxEnd !== 0 && curPage >= maxEnd) curPage = maxEnd;
			
			// 현재 페이지에 대한 start block index 설정.
			var start = (Math.floor((curPage - 1) / 5) * 5) + 1;
			// 현재 페이지에 대한 end block index 설정.
			var end = Math.ceil(curPage / 5) * 5;

			// 만약 위에서 설정한 end가 설정할 수 있는 최대 block index를 넘는다면 maxEnd로 설정한다.
			end = (end >= maxEnd) ? maxEnd : end;
			
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
					curPage = (parseInt(curPage) > 1) ? curPage - 1 : 1;
					location.href = '/list?page=' + curPage;
				}
			});
			
			$('#next').on('click', function(){
				if($(this).attr('class') !== 'disabled'){
					curPage = (parseInt(curPage) < maxEnd) ? curPage + 1 : maxEnd;
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

var editArticle = function(type, targetId, currentUser, uploader) {

	if (currentUser !== uploader) {
		alert('자신의 게시물만 수정할 수 있습니다.');
		return;
	}
	
	openModal(type, targetId);
};

var deleteArticle = function(currentUser, uploader, targetId) {

	if (currentUser !== uploader) {
		alert('자신의 게시물만 삭제할 수 있습니다.');
		return;
	}
	
	$.ajax({
		type : "DELETE",
		url : "/delete/" + targetId,
		success : function(result) {
			alert(result);
			window.location.reload();
		},
		error : function() {
			alert('삭제중 중 오류가 발생했습니다.');
		}
	});
};



var initContent = function() {
	$('#content').val('');
}

//전송 역할을 하는 모달내의 버튼 클릭시 발생할 콜백 함수. 
var post_callback = function() {
	$('#postModal').modal('hide');
};

var nick_callback = function() {
	$('#nicknameModal').modal('hide');
};

var openModal = function(type, targetId) {

	var inputElem = $('input[type=hidden]');
	
	if (type === 'post') {
		$(inputElem).val(type);
	} else {
		// type이 reply나 edit 시.
		$(inputElem).val(type + '#' + targetId);
	}

	// 입력 폼 초기화 후 보여주기.
	initContent();
	$('#postModal').modal('show');

	// 클릭시 수행될 콜백함수 지정.
	$('#sendButton').click(post_callback);

	// 모달이 화면에서 사라졌을 때 이벤트 지정. 버튼에 지정해둔 모든 콜백함수를 제거한다.
	$('#postModal').on('hidden.bs.modal', function(e) {
		$('#sendButton').unbind();
	});
};

var openNickModal = function(userEmail) {

	// 입력 폼 초기화 후 보여주기.
	$('#nickname').val('');
	$('#nicknameModal').modal('show');

	// 클릭시 수행될 콜백함수 지정.
	$('#nickname_button').click(function(){
		$.ajax({
			type : "POST",
			url : "/nickname",
			data : {
				email : userEmail,
				nickname : $('#nickname').val()
			},
			success : function(result) {
				alert(result);
				nick_callback();
				window.location.reload();
			},
			error : function() {
				alert('닉네임 등록 중 오류가 발생했습니다.');
			}
		});
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

var setMultiformConfig = function() {
	
	$('.photo_uploader').MultiFile({
		max : 1, //업로드 최대 파일 갯수 (지정하지 않으면 무한대)
		accept : 'jpg|png|gif', //허용할 확장자 (지정하지 않으면 모든 확장자 허용)
		maxfile : 1024, //각 파일 최대 업로드 크기
		maxsize : 3024, //전체 파일 최대 업로드 크기
		STRING : { // 액션별 메시지 커스터마이징
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


/*!
 * jQuery Form Plugin
 * version: 3.27.0-2013.02.06
 * @requires jQuery v1.5 or later
 *
 * Examples and documentation at: http://malsup.com/jquery/form/
 * Project repository: https://github.com/malsup/form
 * Dual licensed under the MIT and GPL licenses:
 *    http://malsup.github.com/mit-license.txt
 *    http://malsup.github.com/gpl-license-v2.txt
 */
/*global ActiveXObject alert */
;(function($) {
"use strict";

/*
    Usage Note:
    -----------
    Do not use both ajaxSubmit and ajaxForm on the same form.  These
    functions are mutually exclusive.  Use ajaxSubmit if you want
    to bind your own submit handler to the form.  For example,

    $(document).ready(function() {
        $('#myForm').on('submit', function(e) {
            e.preventDefault(); // <-- important
            $(this).ajaxSubmit({
                target: '#output'
            });
        });
    });

    Use ajaxForm when you want the plugin to manage all the event binding
    for you.  For example,

    $(document).ready(function() {
        $('#myForm').ajaxForm({
            target: '#output'
        });
    });

    You can also use ajaxForm with delegation (requires jQuery v1.7+), so the
    form does not have to exist when you invoke ajaxForm:

    $('#myForm').ajaxForm({
        delegation: true,
        target: '#output'
    });

    When using ajaxForm, the ajaxSubmit function will be invoked for you
    at the appropriate time.
*/

/**
 * Feature detection
 */
var feature = {};
feature.fileapi = $("<input type='file'/>").get(0).files !== undefined;
feature.formdata = window.FormData !== undefined;

/**
 * ajaxSubmit() provides a mechanism for immediately submitting
 * an HTML form using AJAX.
 */
$.fn.ajaxSubmit = function(options) {
    /*jshint scripturl:true */

    // fast fail if nothing selected (http://dev.jquery.com/ticket/2752)
    if (!this.length) {
        log('ajaxSubmit: skipping submit process - no element selected');
        return this;
    }

    var method, action, url, $form = this;

    if (typeof options == 'function') {
        options = { success: options };
    }

    method = this.attr('method');
    action = this.attr('action');
    url = (typeof action === 'string') ? $.trim(action) : '';
    url = url || window.location.href || '';
    if (url) {
        // clean url (don't include hash vaue)
        url = (url.match(/^([^#]+)/)||[])[1];
    }

    options = $.extend(true, {
        url:  url,
        success: $.ajaxSettings.success,
        type: method || 'GET',
        iframeSrc: /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank'
    }, options);

    // hook for manipulating the form data before it is extracted;
    // convenient for use with rich editors like tinyMCE or FCKEditor
    var veto = {};
    this.trigger('form-pre-serialize', [this, options, veto]);
    if (veto.veto) {
        log('ajaxSubmit: submit vetoed via form-pre-serialize trigger');
        return this;
    }

    // provide opportunity to alter form data before it is serialized
    if (options.beforeSerialize && options.beforeSerialize(this, options) === false) {
        log('ajaxSubmit: submit aborted via beforeSerialize callback');
        return this;
    }

    var traditional = options.traditional;
    if ( traditional === undefined ) {
        traditional = $.ajaxSettings.traditional;
    }

    var elements = [];
    var qx, a = this.formToArray(options.semantic, elements);
    if (options.data) {
        options.extraData = options.data;
        qx = $.param(options.data, traditional);
    }

    // give pre-submit callback an opportunity to abort the submit
    if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false) {
        log('ajaxSubmit: submit aborted via beforeSubmit callback');
        return this;
    }

    // fire vetoable 'validate' event
    this.trigger('form-submit-validate', [a, this, options, veto]);
    if (veto.veto) {
        log('ajaxSubmit: submit vetoed via form-submit-validate trigger');
        return this;
    }

    var q = $.param(a, traditional);
    if (qx) {
        q = ( q ? (q + '&' + qx) : qx );
    }
    if (options.type.toUpperCase() == 'GET') {
        options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
        options.data = null;  // data is null for 'get'
    }
    else {
        options.data = q; // data is the query string for 'post'
    }

    var callbacks = [];
    if (options.resetForm) {
        callbacks.push(function() { $form.resetForm(); });
    }
    if (options.clearForm) {
        callbacks.push(function() { $form.clearForm(options.includeHidden); });
    }

    // perform a load on the target only if dataType is not provided
    if (!options.dataType && options.target) {
        var oldSuccess = options.success || function(){};
        callbacks.push(function(data) {
            var fn = options.replaceTarget ? 'replaceWith' : 'html';
            $(options.target)[fn](data).each(oldSuccess, arguments);
        });
    }
    else if (options.success) {
        callbacks.push(options.success);
    }

    options.success = function(data, status, xhr) { // jQuery 1.4+ passes xhr as 3rd arg
        var context = options.context || this ;    // jQuery 1.4+ supports scope context
        for (var i=0, max=callbacks.length; i < max; i++) {
            callbacks[i].apply(context, [data, status, xhr || $form, $form]);
        }
    };

    // are there files to upload?

    // [value] (issue #113), also see comment:
    // https://github.com/malsup/form/commit/588306aedba1de01388032d5f42a60159eea9228#commitcomment-2180219
    var fileInputs = $('input[type=file]:enabled[value!=""]', this);

    var hasFileInputs = fileInputs.length > 0;
    var mp = 'multipart/form-data';
    var multipart = ($form.attr('enctype') == mp || $form.attr('encoding') == mp);

    var fileAPI = feature.fileapi && feature.formdata;
    log("fileAPI :" + fileAPI);
    var shouldUseFrame = (hasFileInputs || multipart) && !fileAPI;

    var jqxhr;

    // options.iframe allows user to force iframe mode
    // 06-NOV-09: now defaulting to iframe mode if file input is detected
    if (options.iframe !== false && (options.iframe || shouldUseFrame)) {
        // hack to fix Safari hang (thanks to Tim Molendijk for this)
        // see:  http://groups.google.com/group/jquery-dev/browse_thread/thread/36395b7ab510dd5d
        if (options.closeKeepAlive) {
            $.get(options.closeKeepAlive, function() {
                jqxhr = fileUploadIframe(a);
            });
        }
        else {
            jqxhr = fileUploadIframe(a);
        }
    }
    else if ((hasFileInputs || multipart) && fileAPI) {
        jqxhr = fileUploadXhr(a);
    }
    else {
        jqxhr = $.ajax(options);
    }

    $form.removeData('jqxhr').data('jqxhr', jqxhr);

    // clear element array
    for (var k=0; k < elements.length; k++)
        elements[k] = null;

    // fire 'notify' event
    this.trigger('form-submit-notify', [this, options]);
    return this;

    // utility fn for deep serialization
    function deepSerialize(extraData){
        var serialized = $.param(extraData).split('&');
        var len = serialized.length;
        var result = [];
        var i, part;
        for (i=0; i < len; i++) {
            // #252; undo param space replacement
            serialized[i] = serialized[i].replace(/\+/g,' ');
            part = serialized[i].split('=');
            // #278; use array instead of object storage, favoring array serializations
            result.push([decodeURIComponent(part[0]), decodeURIComponent(part[1])]);
        }
        return result;
    }

     // XMLHttpRequest Level 2 file uploads (big hat tip to francois2metz)
    function fileUploadXhr(a) {
        var formdata = new FormData();

        for (var i=0; i < a.length; i++) {
            formdata.append(a[i].name, a[i].value);
        }

        if (options.extraData) {
            var serializedData = deepSerialize(options.extraData);
            for (i=0; i < serializedData.length; i++)
                if (serializedData[i])
                    formdata.append(serializedData[i][0], serializedData[i][1]);
        }

        options.data = null;

        var s = $.extend(true, {}, $.ajaxSettings, options, {
            contentType: false,
            processData: false,
            cache: false,
            type: method || 'POST'
        });

        if (options.uploadProgress) {
            // workaround because jqXHR does not expose upload property
            s.xhr = function() {
                var xhr = jQuery.ajaxSettings.xhr();
                if (xhr.upload) {
                    xhr.upload.addEventListener('progress', function(event) {
                        var percent = 0;
                        var position = event.loaded || event.position; /*event.position is deprecated*/
                        var total = event.total;
                        if (event.lengthComputable) {
                            percent = Math.ceil(position / total * 100);
                        }
                        options.uploadProgress(event, position, total, percent);
                    }, false);
                }
                return xhr;
            };
        }

        s.data = null;
            var beforeSend = s.beforeSend;
            s.beforeSend = function(xhr, o) {
                o.data = formdata;
                if(beforeSend)
                    beforeSend.call(this, xhr, o);
        };
        return $.ajax(s);
    }

    // private function for handling file uploads (hat tip to YAHOO!)
    function fileUploadIframe(a) {
        var form = $form[0], el, i, s, g, id, $io, io, xhr, sub, n, timedOut, timeoutHandle;
        var useProp = !!$.fn.prop;
        var deferred = $.Deferred();

        if (a) {
            // ensure that every serialized input is still enabled
            for (i=0; i < elements.length; i++) {
                el = $(elements[i]);
                if ( useProp )
                    el.prop('disabled', false);
                else
                    el.removeAttr('disabled');
            }
        }

        s = $.extend(true, {}, $.ajaxSettings, options);
        s.context = s.context || s;
        id = 'jqFormIO' + (new Date().getTime());
        if (s.iframeTarget) {
            $io = $(s.iframeTarget);
            n = $io.attr('name');
            if (!n)
                 $io.attr('name', id);
            else
                id = n;
        }
        else {
            $io = $('<iframe name="' + id + '" src="'+ s.iframeSrc +'" />');
            $io.css({ position: 'absolute', top: '-1000px', left: '-1000px' });
        }
        io = $io[0];


        xhr = { // mock object
            aborted: 0,
            responseText: null,
            responseXML: null,
            status: 0,
            statusText: 'n/a',
            getAllResponseHeaders: function() {},
            getResponseHeader: function() {},
            setRequestHeader: function() {},
            abort: function(status) {
                var e = (status === 'timeout' ? 'timeout' : 'aborted');
                log('aborting upload... ' + e);
                this.aborted = 1;

                try { // #214, #257
                    if (io.contentWindow.document.execCommand) {
                        io.contentWindow.document.execCommand('Stop');
                    }
                }
                catch(ignore) {}

                $io.attr('src', s.iframeSrc); // abort op in progress
                xhr.error = e;
                if (s.error)
                    s.error.call(s.context, xhr, e, status);
                if (g)
                    $.event.trigger("ajaxError", [xhr, s, e]);
                if (s.complete)
                    s.complete.call(s.context, xhr, e);
            }
        };

        g = s.global;
        // trigger ajax global events so that activity/block indicators work like normal
        if (g && 0 === $.active++) {
            $.event.trigger("ajaxStart");
        }
        if (g) {
            $.event.trigger("ajaxSend", [xhr, s]);
        }

        if (s.beforeSend && s.beforeSend.call(s.context, xhr, s) === false) {
            if (s.global) {
                $.active--;
            }
            deferred.reject();
            return deferred;
        }
        if (xhr.aborted) {
            deferred.reject();
            return deferred;
        }

        // add submitting element to data if we know it
        sub = form.clk;
        if (sub) {
            n = sub.name;
            if (n && !sub.disabled) {
                s.extraData = s.extraData || {};
                s.extraData[n] = sub.value;
                if (sub.type == "image") {
                    s.extraData[n+'.x'] = form.clk_x;
                    s.extraData[n+'.y'] = form.clk_y;
                }
            }
        }

        var CLIENT_TIMEOUT_ABORT = 1;
        var SERVER_ABORT = 2;

        function getDoc(frame) {
            var doc = frame.contentWindow ? frame.contentWindow.document : frame.contentDocument ? frame.contentDocument : frame.document;
            return doc;
        }

        // Rails CSRF hack (thanks to Yvan Barthelemy)
        var csrf_token = $('meta[name=csrf-token]').attr('content');
        var csrf_param = $('meta[name=csrf-param]').attr('content');
        if (csrf_param && csrf_token) {
            s.extraData = s.extraData || {};
            s.extraData[csrf_param] = csrf_token;
        }

        // take a breath so that pending repaints get some cpu time before the upload starts
        function doSubmit() {
            // make sure form attrs are set
            var t = $form.attr('target'), a = $form.attr('action');

            // update form attrs in IE friendly way
            form.setAttribute('target',id);
            if (!method) {
                form.setAttribute('method', 'POST');
            }
            if (a != s.url) {
                form.setAttribute('action', s.url);
            }

            // ie borks in some cases when setting encoding
            if (! s.skipEncodingOverride && (!method || /post/i.test(method))) {
                $form.attr({
                    encoding: 'multipart/form-data',
                    enctype:  'multipart/form-data'
                });
            }

            // support timout
            if (s.timeout) {
                timeoutHandle = setTimeout(function() { timedOut = true; cb(CLIENT_TIMEOUT_ABORT); }, s.timeout);
            }

            // look for server aborts
            function checkState() {
                try {
                    var state = getDoc(io).readyState;
                    log('state = ' + state);
                    if (state && state.toLowerCase() == 'uninitialized')
                        setTimeout(checkState,50);
                }
                catch(e) {
                    log('Server abort: ' , e, ' (', e.name, ')');
                    cb(SERVER_ABORT);
                    if (timeoutHandle)
                        clearTimeout(timeoutHandle);
                    timeoutHandle = undefined;
                }
            }

            // add "extra" data to form if provided in options
            var extraInputs = [];
            try {
                if (s.extraData) {
                    for (var n in s.extraData) {
                        if (s.extraData.hasOwnProperty(n)) {
                           // if using the $.param format that allows for multiple values with the same name
                           if($.isPlainObject(s.extraData[n]) && s.extraData[n].hasOwnProperty('name') && s.extraData[n].hasOwnProperty('value')) {
                               extraInputs.push(
                               $('<input type="hidden" name="'+s.extraData[n].name+'">').val(s.extraData[n].value)
                                   .appendTo(form)[0]);
                           } else {
                               extraInputs.push(
                               $('<input type="hidden" name="'+n+'">').val(s.extraData[n])
                                   .appendTo(form)[0]);
                           }
                        }
                    }
                }

                if (!s.iframeTarget) {
                    // add iframe to doc and submit the form
                    $io.appendTo('body');
                    if (io.attachEvent)
                        io.attachEvent('onload', cb);
                    else
                        io.addEventListener('load', cb, false);
                }
                setTimeout(checkState,15);
                // just in case form has element with name/id of 'submit'
                var submitFn = document.createElement('form').submit;
                submitFn.apply(form);
            }
            finally {
                // reset attrs and remove "extra" input elements
                form.setAttribute('action',a);
                if(t) {
                    form.setAttribute('target', t);
                } else {
                    $form.removeAttr('target');
                }
                $(extraInputs).remove();
            }
        }

        if (s.forceSync) {
            doSubmit();
        }
        else {
            setTimeout(doSubmit, 10); // this lets dom updates render
        }

        var data, doc, domCheckCount = 50, callbackProcessed;

        function cb(e) {
            if (xhr.aborted || callbackProcessed) {
                return;
            }
            try {
                doc = getDoc(io);
            }
            catch(ex) {
                log('cannot access response document: ', ex);
                e = SERVER_ABORT;
            }
            if (e === CLIENT_TIMEOUT_ABORT && xhr) {
                xhr.abort('timeout');
                deferred.reject(xhr, 'timeout');
                return;
            }
            else if (e == SERVER_ABORT && xhr) {
                xhr.abort('server abort');
                deferred.reject(xhr, 'error', 'server abort');
                return;
            }

            if (!doc || doc.location.href == s.iframeSrc) {
                // response not received yet
                if (!timedOut)
                    return;
            }
            if (io.detachEvent)
                io.detachEvent('onload', cb);
            else
                io.removeEventListener('load', cb, false);

            var status = 'success', errMsg;
            try {
                if (timedOut) {
                    throw 'timeout';
                }

                var isXml = s.dataType == 'xml' || doc.XMLDocument || $.isXMLDoc(doc);
                log('isXml='+isXml);
                if (!isXml && window.opera && (doc.body === null || !doc.body.innerHTML)) {
                    if (--domCheckCount) {
                        // in some browsers (Opera) the iframe DOM is not always traversable when
                        // the onload callback fires, so we loop a bit to accommodate
                        log('requeing onLoad callback, DOM not available');
                        setTimeout(cb, 250);
                        return;
                    }
                    // let this fall through because server response could be an empty document
                    //log('Could not access iframe DOM after mutiple tries.');
                    //throw 'DOMException: not available';
                }

                //log('response detected');
                var docRoot = doc.body ? doc.body : doc.documentElement;
                xhr.responseText = docRoot ? docRoot.innerHTML : null;
                xhr.responseXML = doc.XMLDocument ? doc.XMLDocument : doc;
                if (isXml)
                    s.dataType = 'xml';
                xhr.getResponseHeader = function(header){
                    var headers = {'content-type': s.dataType};
                    return headers[header];
                };
                // support for XHR 'status' & 'statusText' emulation :
                if (docRoot) {
                    xhr.status = Number( docRoot.getAttribute('status') ) || xhr.status;
                    xhr.statusText = docRoot.getAttribute('statusText') || xhr.statusText;
                }

                var dt = (s.dataType || '').toLowerCase();
                var scr = /(json|script|text)/.test(dt);
                if (scr || s.textarea) {
                    // see if user embedded response in textarea
                    var ta = doc.getElementsByTagName('textarea')[0];
                    if (ta) {
                        xhr.responseText = ta.value;
                        // support for XHR 'status' & 'statusText' emulation :
                        xhr.status = Number( ta.getAttribute('status') ) || xhr.status;
                        xhr.statusText = ta.getAttribute('statusText') || xhr.statusText;
                    }
                    else if (scr) {
                        // account for browsers injecting pre around json response
                        var pre = doc.getElementsByTagName('pre')[0];
                        var b = doc.getElementsByTagName('body')[0];
                        if (pre) {
                            xhr.responseText = pre.textContent ? pre.textContent : pre.innerText;
                        }
                        else if (b) {
                            xhr.responseText = b.textContent ? b.textContent : b.innerText;
                        }
                    }
                }
                else if (dt == 'xml' && !xhr.responseXML && xhr.responseText) {
                    xhr.responseXML = toXml(xhr.responseText);
                }

                try {
                    data = httpData(xhr, dt, s);
                }
                catch (e) {
                    status = 'parsererror';
                    xhr.error = errMsg = (e || status);
                }
            }
            catch (e) {
                log('error caught: ',e);
                status = 'error';
                xhr.error = errMsg = (e || status);
            }

            if (xhr.aborted) {
                log('upload aborted');
                status = null;
            }

            if (xhr.status) { // we've set xhr.status
                status = (xhr.status >= 200 && xhr.status < 300 || xhr.status === 304) ? 'success' : 'error';
            }

            // ordering of these callbacks/triggers is odd, but that's how $.ajax does it
            if (status === 'success') {
                if (s.success)
                    s.success.call(s.context, data, 'success', xhr);
                deferred.resolve(xhr.responseText, 'success', xhr);
                if (g)
                    $.event.trigger("ajaxSuccess", [xhr, s]);
            }
            else if (status) {
                if (errMsg === undefined)
                    errMsg = xhr.statusText;
                if (s.error)
                    s.error.call(s.context, xhr, status, errMsg);
                deferred.reject(xhr, 'error', errMsg);
                if (g)
                    $.event.trigger("ajaxError", [xhr, s, errMsg]);
            }

            if (g)
                $.event.trigger("ajaxComplete", [xhr, s]);

            if (g && ! --$.active) {
                $.event.trigger("ajaxStop");
            }

            if (s.complete)
                s.complete.call(s.context, xhr, status);

            callbackProcessed = true;
            if (s.timeout)
                clearTimeout(timeoutHandle);

            // clean up
            setTimeout(function() {
                if (!s.iframeTarget)
                    $io.remove();
                xhr.responseXML = null;
            }, 100);
        }

        var toXml = $.parseXML || function(s, doc) { // use parseXML if available (jQuery 1.5+)
            if (window.ActiveXObject) {
                doc = new ActiveXObject('Microsoft.XMLDOM');
                doc.async = 'false';
                doc.loadXML(s);
            }
            else {
                doc = (new DOMParser()).parseFromString(s, 'text/xml');
            }
            return (doc && doc.documentElement && doc.documentElement.nodeName != 'parsererror') ? doc : null;
        };
        var parseJSON = $.parseJSON || function(s) {
            /*jslint evil:true */
            return window['eval']('(' + s + ')');
        };

        var httpData = function( xhr, type, s ) { // mostly lifted from jq1.4.4

            var ct = xhr.getResponseHeader('content-type') || '',
                xml = type === 'xml' || !type && ct.indexOf('xml') >= 0,
                data = xml ? xhr.responseXML : xhr.responseText;

            if (xml && data.documentElement.nodeName === 'parsererror') {
                if ($.error)
                    $.error('parsererror');
            }
            if (s && s.dataFilter) {
                data = s.dataFilter(data, type);
            }
            if (typeof data === 'string') {
                if (type === 'json' || !type && ct.indexOf('json') >= 0) {
                    data = parseJSON(data);
                } else if (type === "script" || !type && ct.indexOf("javascript") >= 0) {
                    $.globalEval(data);
                }
            }
            return data;
        };

        return deferred;
    }
};

/**
 * ajaxForm() provides a mechanism for fully automating form submission.
 *
 * The advantages of using this method instead of ajaxSubmit() are:
 *
 * 1: This method will include coordinates for <input type="image" /> elements (if the element
 *    is used to submit the form).
 * 2. This method will include the submit element's name/value data (for the element that was
 *    used to submit the form).
 * 3. This method binds the submit() method to the form for you.
 *
 * The options argument for ajaxForm works exactly as it does for ajaxSubmit.  ajaxForm merely
 * passes the options argument along after properly binding events for submit elements and
 * the form itself.
 */
$.fn.ajaxForm = function(options) {
    options = options || {};
    options.delegation = options.delegation && $.isFunction($.fn.on);

    // in jQuery 1.3+ we can fix mistakes with the ready state
    if (!options.delegation && this.length === 0) {
        var o = { s: this.selector, c: this.context };
        if (!$.isReady && o.s) {
            log('DOM not ready, queuing ajaxForm');
            $(function() {
                $(o.s,o.c).ajaxForm(options);
            });
            return this;
        }
        // is your DOM ready?  http://docs.jquery.com/Tutorials:Introducing_$(document).ready()
        log('terminating; zero elements found by selector' + ($.isReady ? '' : ' (DOM not ready)'));
        return this;
    }

    if ( options.delegation ) {
        $(document)
            .off('submit.form-plugin', this.selector, doAjaxSubmit)
            .off('click.form-plugin', this.selector, captureSubmittingElement)
            .on('submit.form-plugin', this.selector, options, doAjaxSubmit)
            .on('click.form-plugin', this.selector, options, captureSubmittingElement);
        return this;
    }

    return this.ajaxFormUnbind()
        .bind('submit.form-plugin', options, doAjaxSubmit)
        .bind('click.form-plugin', options, captureSubmittingElement);
};

// private event handlers
function doAjaxSubmit(e) {
    /*jshint validthis:true */
    var options = e.data;
    if (!e.isDefaultPrevented()) { // if event has been canceled, don't proceed
        e.preventDefault();
        $(this).ajaxSubmit(options);
    }
}

function captureSubmittingElement(e) {
    /*jshint validthis:true */
    var target = e.target;
    var $el = $(target);
    if (!($el.is("[type=submit],[type=image]"))) {
        // is this a child element of the submit el?  (ex: a span within a button)
        var t = $el.closest('[type=submit]');
        if (t.length === 0) {
            return;
        }
        target = t[0];
    }
    var form = this;
    form.clk = target;
    if (target.type == 'image') {
        if (e.offsetX !== undefined) {
            form.clk_x = e.offsetX;
            form.clk_y = e.offsetY;
        } else if (typeof $.fn.offset == 'function') {
            var offset = $el.offset();
            form.clk_x = e.pageX - offset.left;
            form.clk_y = e.pageY - offset.top;
        } else {
            form.clk_x = e.pageX - target.offsetLeft;
            form.clk_y = e.pageY - target.offsetTop;
        }
    }
    // clear form vars
    setTimeout(function() { form.clk = form.clk_x = form.clk_y = null; }, 100);
}


// ajaxFormUnbind unbinds the event handlers that were bound by ajaxForm
$.fn.ajaxFormUnbind = function() {
    return this.unbind('submit.form-plugin click.form-plugin');
};

/**
 * formToArray() gathers form element data into an array of objects that can
 * be passed to any of the following ajax functions: $.get, $.post, or load.
 * Each object in the array has both a 'name' and 'value' property.  An example of
 * an array for a simple login form might be:
 *
 * [ { name: 'username', value: 'jresig' }, { name: 'password', value: 'secret' } ]
 *
 * It is this array that is passed to pre-submit callback functions provided to the
 * ajaxSubmit() and ajaxForm() methods.
 */
$.fn.formToArray = function(semantic, elements) {
    var a = [];
    if (this.length === 0) {
        return a;
    }

    var form = this[0];
    var els = semantic ? form.getElementsByTagName('*') : form.elements;
    if (!els) {
        return a;
    }

    var i,j,n,v,el,max,jmax;
    for(i=0, max=els.length; i < max; i++) {
        el = els[i];
        n = el.name;
        if (!n) {
            continue;
        }

        if (semantic && form.clk && el.type == "image") {
            // handle image inputs on the fly when semantic == true
            if(!el.disabled && form.clk == el) {
                a.push({name: n, value: $(el).val(), type: el.type });
                a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
            }
            continue;
        }

        v = $.fieldValue(el, true);
        if (v && v.constructor == Array) {
            if (elements)
                elements.push(el);
            for(j=0, jmax=v.length; j < jmax; j++) {
                a.push({name: n, value: v[j]});
            }
        }
        else if (feature.fileapi && el.type == 'file' && !el.disabled) {
            if (elements)
                elements.push(el);
            var files = el.files;
            if (files.length) {
                for (j=0; j < files.length; j++) {
                    a.push({name: n, value: files[j], type: el.type});
                }
            }
            else {
                // #180
                a.push({ name: n, value: '', type: el.type });
            }
        }
        else if (v !== null && typeof v != 'undefined') {
            if (elements)
                elements.push(el);
            a.push({name: n, value: v, type: el.type, required: el.required});
        }
    }

    if (!semantic && form.clk) {
        // input type=='image' are not found in elements array! handle it here
        var $input = $(form.clk), input = $input[0];
        n = input.name;
        if (n && !input.disabled && input.type == 'image') {
            a.push({name: n, value: $input.val()});
            a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
        }
    }
    return a;
};

/**
 * Serializes form data into a 'submittable' string. This method will return a string
 * in the format: name1=value1&amp;name2=value2
 */
$.fn.formSerialize = function(semantic) {
    //hand off to jQuery.param for proper encoding
    return $.param(this.formToArray(semantic));
};

/**
 * Serializes all field elements in the jQuery object into a query string.
 * This method will return a string in the format: name1=value1&amp;name2=value2
 */
$.fn.fieldSerialize = function(successful) {
    var a = [];
    this.each(function() {
        var n = this.name;
        if (!n) {
            return;
        }
        var v = $.fieldValue(this, successful);
        if (v && v.constructor == Array) {
            for (var i=0,max=v.length; i < max; i++) {
                a.push({name: n, value: v[i]});
            }
        }
        else if (v !== null && typeof v != 'undefined') {
            a.push({name: this.name, value: v});
        }
    });
    //hand off to jQuery.param for proper encoding
    return $.param(a);
};

/**
 * Returns the value(s) of the element in the matched set.  For example, consider the following form:
 *
 *  <form><fieldset>
 *      <input name="A" type="text" />
 *      <input name="A" type="text" />
 *      <input name="B" type="checkbox" value="B1" />
 *      <input name="B" type="checkbox" value="B2"/>
 *      <input name="C" type="radio" value="C1" />
 *      <input name="C" type="radio" value="C2" />
 *  </fieldset></form>
 *
 *  var v = $('input[type=text]').fieldValue();
 *  // if no values are entered into the text inputs
 *  v == ['','']
 *  // if values entered into the text inputs are 'foo' and 'bar'
 *  v == ['foo','bar']
 *
 *  var v = $('input[type=checkbox]').fieldValue();
 *  // if neither checkbox is checked
 *  v === undefined
 *  // if both checkboxes are checked
 *  v == ['B1', 'B2']
 *
 *  var v = $('input[type=radio]').fieldValue();
 *  // if neither radio is checked
 *  v === undefined
 *  // if first radio is checked
 *  v == ['C1']
 *
 * The successful argument controls whether or not the field element must be 'successful'
 * (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
 * The default value of the successful argument is true.  If this value is false the value(s)
 * for each element is returned.
 *
 * Note: This method *always* returns an array.  If no valid value can be determined the
 *    array will be empty, otherwise it will contain one or more values.
 */
$.fn.fieldValue = function(successful) {
    for (var val=[], i=0, max=this.length; i < max; i++) {
        var el = this[i];
        var v = $.fieldValue(el, successful);
        if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length)) {
            continue;
        }
        if (v.constructor == Array)
            $.merge(val, v);
        else
            val.push(v);
    }
    return val;
};

/**
 * Returns the value of the field element.
 */
$.fieldValue = function(el, successful) {
    var n = el.name, t = el.type, tag = el.tagName.toLowerCase();
    if (successful === undefined) {
        successful = true;
    }

    if (successful && (!n || el.disabled || t == 'reset' || t == 'button' ||
        (t == 'checkbox' || t == 'radio') && !el.checked ||
        (t == 'submit' || t == 'image') && el.form && el.form.clk != el ||
        tag == 'select' && el.selectedIndex == -1)) {
            return null;
    }

    if (tag == 'select') {
        var index = el.selectedIndex;
        if (index < 0) {
            return null;
        }
        var a = [], ops = el.options;
        var one = (t == 'select-one');
        var max = (one ? index+1 : ops.length);
        for(var i=(one ? index : 0); i < max; i++) {
            var op = ops[i];
            if (op.selected) {
                var v = op.value;
                if (!v) { // extra pain for IE...
                    v = (op.attributes && op.attributes['value'] && !(op.attributes['value'].specified)) ? op.text : op.value;
                }
                if (one) {
                    return v;
                }
                a.push(v);
            }
        }
        return a;
    }
    return $(el).val();
};

/**
 * Clears the form data.  Takes the following actions on the form's input fields:
 *  - input text fields will have their 'value' property set to the empty string
 *  - select elements will have their 'selectedIndex' property set to -1
 *  - checkbox and radio inputs will have their 'checked' property set to false
 *  - inputs of type submit, button, reset, and hidden will *not* be effected
 *  - button elements will *not* be effected
 */
$.fn.clearForm = function(includeHidden) {
    return this.each(function() {
        $('input,select,textarea', this).clearFields(includeHidden);
    });
};

/**
 * Clears the selected form elements.
 */
$.fn.clearFields = $.fn.clearInputs = function(includeHidden) {
    var re = /^(?:color|date|datetime|email|month|number|password|range|search|tel|text|time|url|week)$/i; // 'hidden' is not in this list
    return this.each(function() {
        var t = this.type, tag = this.tagName.toLowerCase();
        if (re.test(t) || tag == 'textarea') {
            this.value = '';
        }
        else if (t == 'checkbox' || t == 'radio') {
            this.checked = false;
        }
        else if (tag == 'select') {
            this.selectedIndex = -1;
        }
		else if (t == "file") {
			if (/MSIE/.test(navigator.userAgent)) {
				$(this).replaceWith($(this).clone());
			} else {
				$(this).val('');
			}
		}
        else if (includeHidden) {
            // includeHidden can be the value true, or it can be a selector string
            // indicating a special test; for example:
            //  $('#myForm').clearForm('.special:hidden')
            // the above would clean hidden inputs that have the class of 'special'
            if ( (includeHidden === true && /hidden/.test(t)) ||
                 (typeof includeHidden == 'string' && $(this).is(includeHidden)) )
                this.value = '';
        }
    });
};

/**
 * Resets the form data.  Causes all form elements to be reset to their original value.
 */
$.fn.resetForm = function() {
    return this.each(function() {
        // guard against an input with the name of 'reset'
        // note that IE reports the reset function as an 'object'
        if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType)) {
            this.reset();
        }
    });
};

/**
 * Enables or disables any matching elements.
 */
$.fn.enable = function(b) {
    if (b === undefined) {
        b = true;
    }
    return this.each(function() {
        this.disabled = !b;
    });
};

/**
 * Checks/unchecks any matching checkboxes or radio buttons and
 * selects/deselects and matching option elements.
 */
$.fn.selected = function(select) {
    if (select === undefined) {
        select = true;
    }
    return this.each(function() {
        var t = this.type;
        if (t == 'checkbox' || t == 'radio') {
            this.checked = select;
        }
        else if (this.tagName.toLowerCase() == 'option') {
            var $sel = $(this).parent('select');
            if (select && $sel[0] && $sel[0].type == 'select-one') {
                // deselect all other options
                $sel.find('option').selected(false);
            }
            this.selected = select;
        }
    });
};

// expose debug var
$.fn.ajaxSubmit.debug = false;

// helper fn for console logging
function log() {
    if (!$.fn.ajaxSubmit.debug)
        return;
    var msg = '[jquery.form] ' + Array.prototype.join.call(arguments,'');
    if (window.console && window.console.log) {
        window.console.log(msg);
    }
    else if (window.opera && window.opera.postError) {
        window.opera.postError(msg);
    }
}

})(jQuery);
/*
 ### jQuery Multiple File Selection Plugin v2.2.1 - 2015-03-23 ###
 * Home: http://www.fyneworks.com/jquery/multifile/
 * Code: http://code.google.com/p/jquery-multifile-plugin/
 *
	* Licensed under http://en.wikipedia.org/wiki/MIT_License
 ###
*/
/*# AVOID COLLISIONS #*/
;
if (window.jQuery)(function ($) {
	"use strict";
	/*# AVOID COLLISIONS #*/

	// size label function (shows kb and mb where accordingly)
	function sl(x) {
		return x > 1048576 ? (x / 1048576).toFixed(1) + 'Mb' : (x==1024?'1Mb': (x / 1024).toFixed(1) + 'Kb' )
	};
	// utility function to return an array of
	function FILE_LIST(x){
		return ((x.files&&x.files.length) ? x.files : null) || [{
			name: x.value,
			size: 0,
			type: ((x.value || '').match(/[^\.]+$/i) || [''])[0]
		}];
	};

	// plugin initialization
	$.fn.MultiFile = function (options) {
		if (this.length == 0) return this; // quick fail

		// Handle API methods
		if (typeof arguments[0] == 'string') {
			// Perform API methods on individual elements
			if (this.length > 1) {
				var args = arguments;
				return this.each(function () {
					$.fn.MultiFile.apply($(this), args);
				});
			};
			// Invoke API method handler (and return whatever it wants to return)
			return $.fn.MultiFile[arguments[0]].apply(this, $.makeArray(arguments).slice(1) || []);
		};

		// Accept number
		if (typeof options == 'number') {
			options = {max: options};
		};

		// Initialize options for this call
		var options = $.extend({} /* new object */ ,
			$.fn.MultiFile.options /* default options */ ,
			options || {} /* just-in-time options */
		);

		// Empty Element Fix!!!
		// this code will automatically intercept native form submissions
		// and disable empty file elements
		$('form')
			.not('MultiFile-intercepted')
			.addClass('MultiFile-intercepted')
			.submit($.fn.MultiFile.disableEmpty);

		//### http://plugins.jquery.com/node/1363
		// utility method to integrate this plugin with others...
		if ($.fn.MultiFile.options.autoIntercept) {
			$.fn.MultiFile.intercept($.fn.MultiFile.options.autoIntercept /* array of methods to intercept */ );
			$.fn.MultiFile.options.autoIntercept = null; /* only run this once */
		};

		// loop through each matched element
		this
			.not('.MultiFile-applied')
			.addClass('MultiFile-applied')
			.each(function () {
				//#####################################################################
				// MAIN PLUGIN FUNCTIONALITY - START
				//#####################################################################

				// BUG 1251 FIX: http://plugins.jquery.com/project/comments/add/1251
				// variable group_count would repeat itself on multiple calls to the plugin.
				// this would cause a conflict with multiple elements
				// changes scope of variable to global so id will be unique over n calls
				window.MultiFile = (window.MultiFile || 0) + 1;
				var group_count = window.MultiFile;

				// Copy parent attributes - Thanks to Jonas Wagner
				// we will use this one to create new input elements
				var MultiFile = {
					e: this,
					E: $(this),
					clone: $(this).clone()
				};

				//===

				//# USE CONFIGURATION
				var o = $.extend({},
					$.fn.MultiFile.options,
					options || {}, ($.metadata ? MultiFile.E.metadata() : ($.meta ? MultiFile.E.data() : null)) || {}, /* metadata options */ {} /* internals */
				);
				// limit number of files that can be selected?
				if (!(o.max > 0) /*IsNull(MultiFile.max)*/ ) {
					o.max = MultiFile.E.attr('maxlength');
				};
				if (!(o.max > 0) /*IsNull(MultiFile.max)*/ ) {
					o.max = (String(MultiFile.e.className.match(/\b(max|limit)\-([0-9]+)\b/gi) || ['']).match(/[0-9]+/gi) || [''])[0];
					if (!(o.max > 0)) o.max = -1;
					else o.max = String(o.max).match(/[0-9]+/gi)[0];
				};
				o.max = new Number(o.max);
				// limit extensions?
				o.accept = o.accept || MultiFile.E.attr('accept') || '';
				if (!o.accept) {
					o.accept = (MultiFile.e.className.match(/\b(accept\-[\w\|]+)\b/gi)) || '';
					o.accept = new String(o.accept).replace(/^(accept|ext)\-/i, '');
				};
				// limit total pay load size
				o.maxsize = o.maxsize>0?o.maxsize:null || MultiFile.E.data('maxsize') || 0;
				if (!(o.maxsize > 0) /*IsNull(MultiFile.maxsize)*/ ) {
					o.maxsize = (String(MultiFile.e.className.match(/\b(maxsize|maxload|size)\-([0-9]+)\b/gi) || ['']).match(/[0-9]+/gi) || [''])[0];
					if (!(o.maxsize > 0)) o.maxsize = -1;
					else o.maxsize = String(o.maxsize).match(/[0-9]+/gi)[0];
				};
				// limit individual file size
				o.maxfile = o.maxfile>0?o.maxfile:null || MultiFile.E.data('maxfile') || 0;
				if (!(o.maxfile > 0) /*IsNull(MultiFile.maxfile)*/ ) {
					o.maxfile = (String(MultiFile.e.className.match(/\b(maxfile|filemax)\-([0-9]+)\b/gi) || ['']).match(/[0-9]+/gi) || [''])[0];
					if (!(o.maxfile > 0)) o.maxfile = -1;
					else o.maxfile = String(o.maxfile).match(/[0-9]+/gi)[0];
				};

				//===

				// size options are accepted in kylobytes, so multiple them by 1024
				if(o.maxfile>1) o.maxfile = o.maxfile * 1024;
				if(o.maxsize>1) o.maxsize = o.maxsize * 1024;

				//===

				// HTML5: enforce multiple selection to be enabled, except when explicitly disabled
				if (o.multiple !== false) {
		                    if (o.max > 1) MultiFile.E.attr('multiple', 'multiple').prop('multiple', true);
		                }

				//===

				// APPLY CONFIGURATION
				$.extend(MultiFile, o || {});
				MultiFile.STRING = $.extend({}, $.fn.MultiFile.options.STRING, MultiFile.STRING);

				//===

				//#########################################
				// PRIVATE PROPERTIES/METHODS
				$.extend(MultiFile, {
					n: 0, // How many elements are currently selected?
					slaves: [],
					files: [],
					instanceKey: MultiFile.e.id || 'MultiFile' + String(group_count), // Instance Key?
					generateID: function (z) {
						return MultiFile.instanceKey + (z > 0 ? '_F' + String(z) : '');
					},
					trigger: function (event, element, MultiFile, files) {
						var rv, handler = MultiFile[event] || MultiFile['on'+event] ;
						if (handler){
							files = files || MultiFile.files || FILE_LIST(this);
							;
							$.each(files,function(i, file){
								// execute function in element's context, so 'this' variable is current element
								rv = handler.apply(MultiFile.wrapper, [element, file.name, MultiFile, file]);
							});
							return rv;
						};
					}
				});

				//===

				// Setup dynamic regular expression for extension validation
				// - thanks to John-Paul Bader: http://smyck.de/2006/08/11/javascript-dynamic-regular-expresions/
				if (String(MultiFile.accept).length > 1) {
					MultiFile.accept = MultiFile.accept.replace(/\W+/g, '|').replace(/^\W|\W$/g, '');
					MultiFile.rxAccept = new RegExp('\\.(' + (MultiFile.accept ? MultiFile.accept : '') + ')$', 'gi');
				};

				//===

				// Create wrapper to hold our file list
				MultiFile.wrapID = MultiFile.instanceKey;// + '_wrap'; // Wrapper ID?
				MultiFile.E.wrap('<div class="MultiFile-wrap" id="' + MultiFile.wrapID + '"></div>');
				MultiFile.wrapper = $('#' + MultiFile.wrapID + '');

				//===

				// MultiFile MUST have a name - default: file1[], file2[], file3[]
				MultiFile.e.name = MultiFile.e.name || 'file' + group_count + '[]';

				//===

				if (!MultiFile.list) {
					// Create a wrapper for the list
					// * OPERA BUG: NO_MODIFICATION_ALLOWED_ERR ('list' is a read-only property)
					// this change allows us to keep the files in the order they were selected
					MultiFile.wrapper.append('<div class="MultiFile-list" id="' + MultiFile.wrapID + '_list"></div>');
					MultiFile.list = $('#' + MultiFile.wrapID + '_list');
				};
				MultiFile.list = $(MultiFile.list);

				//===

				// Bind a new element
				MultiFile.addSlave = function (slave, slave_count) {
					//if(window.console) console.log('MultiFile.addSlave',slave_count);
					
					// Keep track of how many elements have been displayed
					MultiFile.n++;
					// Add reference to master element
					slave.MultiFile = MultiFile;

					// BUG FIX: http://plugins.jquery.com/node/1495
					// Clear identifying properties from clones
					slave.id = slave.name = '';

					// Define element's ID and name (upload components need this!)
					//slave.id = slave.id || MultiFile.generateID(slave_count);
					slave.id = MultiFile.generateID(slave_count);
					//FIX for: http://code.google.com/p/jquery-multifile-plugin/issues/detail?id=23
					//CHANGE v2.2.1 - change ID of all file elements, keep original ID in wrapper

					// 2008-Apr-29: New customizable naming convention (see url below)
					// http://groups.google.com/group/jquery-dev/browse_frm/thread/765c73e41b34f924#
					slave.name = String(MultiFile.namePattern
						/*master name*/
						.replace(/\$name/gi, $(MultiFile.clone).attr('name'))
						/*master id */
						.replace(/\$id/gi, $(MultiFile.clone).attr('id'))
						/*group count*/
						.replace(/\$g/gi, group_count) //(group_count>0?group_count:''))
						/*slave count*/
						.replace(/\$i/gi, slave_count) //(slave_count>0?slave_count:''))
					);

					// If we've reached maximum number, disable input slave
					var disable_slave;
					if ((MultiFile.max > 0) && ((MultiFile.files.length) > (MultiFile.max))) {
						slave.disabled = true;
						disable_slave = true;
					};

					// Remember most recent slave
					MultiFile.current = slave;
					
					// We'll use jQuery from now on
					slave = $(slave);

					// Clear value
					slave.val('').attr('value', '')[0].value = '';

					// Stop plugin initializing on slaves
					slave.addClass('MultiFile-applied');

					// Triggered when a file is selected
					slave.change(function (a, b, c) {
						//if(window.console) console.log('MultiFile.slave.change',slave_count);
						//if(window.console) console.log('MultiFile.slave.change',this.files);

						// Lose focus to stop IE7 firing onchange again
						$(this).blur();

						//# NEW 2014-04-14 - accept multiple file selection, HTML5
						var e = this,
								prevs = MultiFile.files || [],
								files = this.files || [{
									name: this.value,
									size: 0,
									type: ((this.value || '').match(/[^\.]+$/i) || [''])[0]
								}],
								newfs = [],
								newfs_size = 0,
								total_size = MultiFile.total_size || 0/*,
								html5_multi_mode = this.files && $(this).attr('multiple')*/
							;

						// recap
						//console.log('START '+ prevs.length + ' files @ '+ sl(total_size) +'.', prevs);

						//# Retrive value of selected file from element
						var ERROR = []; //, v = String(this.value || '');

						// make a normal array
						$.each(files, function (i, file) {
							newfs[newfs.length] = file;
						});

						//# Trigger Event! onFileSelect
						MultiFile.trigger('FileSelect', this, MultiFile, newfs);
						//# End Event!

						// validate individual files
						$.each(files, function (i, file) {

							// pop local variables out of array/file object
							var v = file.name,
									s = file.size,
									p = function(z){
										return z
											.replace('$ext', String(v.match(/[^\.]+$/i) || ''))
											.replace('$file', v.match(/[^\/\\]+$/gi))
											.replace('$size', sl(s) + ' > ' + sl(MultiFile.maxfile))
									}
							;

							// check extension
							if (MultiFile.accept && v && !v.match(MultiFile.rxAccept)) {
								ERROR[ERROR.length] = p(MultiFile.STRING.denied);
								MultiFile.trigger('FileInvalid', this, MultiFile, [file]);
							};

							// Disallow duplicates
							$(MultiFile.wrapper).find('input[type=file]').not(e).each(function(){
								// go through each file in each slave
								$.each(FILE_LIST(this), function (i, file) {
									if(file.name){
										//console.log('MultiFile.debug> Duplicate?', file.name, v);
										var x = (file.name || '').replace(/^C:\\fakepath\\/gi,'');
										if ( v == x || v == x.substr(x.length - v.length)) {
											ERROR[ERROR.length] = p(MultiFile.STRING.duplicate);
											MultiFile.trigger('FileDuplicate', e, MultiFile, [file]);
										};
									};
								});
							});

							// limit the max size of individual files selected
							if (MultiFile.maxfile>0 && s>0 && s>MultiFile.maxfile) {
								ERROR[ERROR.length] = p(MultiFile.STRING.toobig);
								MultiFile.trigger('FileTooBig', this, MultiFile, [file]);
							};

							// check extension
							var customError = MultiFile.trigger('FileValidate', this, MultiFile, [file]);
							if(customError && customError!=''){
								ERROR[ERROR.length] = p(customError);
							};
								
							// add up size of files selected
							newfs_size += file.size;

						});
						
						// add up total for all files selected (existing and new)
						total_size += newfs_size;

						// put some useful information in the file array
						newfs.size = newfs_size;
						newfs.total = total_size;
						newfs.total_length = newfs.length + prevs.length;

						// limit the number of files selected
						if (MultiFile.max>0 && prevs.length + files.length > MultiFile.max) {
							ERROR[ERROR.length] = MultiFile.STRING.toomany.replace('$max', MultiFile.max);
							MultiFile.trigger('FileTooMany', this, MultiFile, newfs);
						};

						// limit the max size of files selected
						if (MultiFile.maxsize > 0 && total_size > MultiFile.maxsize) {
							ERROR[ERROR.length] = MultiFile.STRING.toomuch.replace('$size', sl(total_size) + ' > ' + sl(MultiFile.maxsize));
							MultiFile.trigger('FileTooMuch', this, MultiFile, newfs);
						};

						// Create a new file input element
						var newEle = $(MultiFile.clone).clone(); // Copy parent attributes - Thanks to Jonas Wagner
						//# Let's remember which input we've generated so
						// we can disable the empty ones before submission
						// See: http://plugins.jquery.com/node/1495
						newEle.addClass('MultiFile');

						// Handle error
						if (ERROR.length > 0) {

							// Handle error
							MultiFile.error(ERROR.join('\n\n'));

							// 2007-06-24: BUG FIX - Thanks to Adrian Wróbel <adrian [dot] wrobel [at] gmail.com>
							// Ditch the trouble maker and add a fresh new element
							MultiFile.n--;
							MultiFile.addSlave(newEle[0], slave_count);
							slave.parent().prepend(newEle);
							slave.remove();
							return false;

						}
						else { // if no errors have been found

							// remember total size
							MultiFile.total_size = total_size;

							// merge arrays
							files = prevs.concat(newfs);

							// put some useful information in the file array
							files.size = total_size;
							files.size_label = sl(total_size);

							// recap
							//console.log('NOW '+ files.length + ' files @ '+ sl(total_size) +'.', files);

							// remember files
							MultiFile.files = files;

							// Hide this element (NB: display:none is evil!)
							$(this).css({
								position: 'absolute',
								top: '-3000px'
							});

							// Add new element to the form
							slave.after(newEle);

							// Bind functionality
							MultiFile.addSlave(newEle[0], slave_count + 1);

							// Update list
							MultiFile.addToList(this, slave_count, newfs);

							//# Trigger Event! afterFileSelect
							MultiFile.trigger('afterFileSelect', this, MultiFile, newfs);
							//# End Event!

						}; // no errors detected

					}); // slave.change()

					// point to wrapper
					$(slave).data('MultiFile-wrap', MultiFile.wrapper);

					// store contorl's settings and file info in wrapper
					$(MultiFile.wrapper).data('MultiFile',MultiFile);

					// disable?
					if(disable_slave) $(slave).attr('disabled','disabled').prop('disabled',true);

				}; // MultiFile.addSlave
				// Bind a new element



				// Add a new file to the list
				MultiFile.addToList = function (slave, slave_count, files) {
					//if(window.console) console.log('MultiFile.addToList',slave_count);

					//# Trigger Event! onFileAppend
					MultiFile.trigger('FileAppend', slave, MultiFile, files);
					//# End Event!
					
					var names = $('<span/>');
					$.each(files, function (i, file) {
						var v = String(file.name || '' ),
								S = MultiFile.STRING,
								n = S.label || S.file || S.name,
								t = S.title || S.tooltip || S.selected,
								p = '<img class="MultiFile-preview" style="'+ MultiFile.previewCss+'"/>',
								label =	$(
										(
											'<span class="MultiFile-label" title="' + t + '">'+
												'<span class="MultiFile-title">'+ n +'</span>'+
												(file.type.substr(0,6) == 'image/' && (MultiFile.preview || $(slave).is('.with-preview')) ? p : '' )+
											'</span>'
										)
										.replace(/\$(file|name)/gi, (v.match(/[^\/\\]+$/gi)||[v])[0])
										.replace(/\$(ext|extension|type)/gi, (v.match(/[^\.]+$/gi)||[''])[0])
										.replace(/\$(size)/gi, sl(file.size || 0))
										.replace(/\$(preview)/gi, p)
										.replace(/\$(i)/gi, i)
								);
						
						// now supports preview via locale string.
						// just add an <img class='MultiFile-preview'/> anywhere within the "file" string
						label.find('img.MultiFile-preview').each(function(){
							var t = this;
							var oFReader = new FileReader();
							oFReader.readAsDataURL(file);
							oFReader.onload = function (oFREvent) {
								t.src = oFREvent.target.result;
							};
						});

						// append file label to list
						if(i>0) names.append(', ');
						names.append(label);

						var v = String(file.name || '' );
						names[names.length] =
							(
								'<span class="MultiFile-title" title="' + MultiFile.STRING.selected + '">'
									+ MultiFile.STRING.file +
								'</span>'
							)
							.replace(/\$(file|name)/gi, (v.match(/[^\/\\]+$/gi)||[v])[0])
							.replace(/\$(ext|extension|type)/gi, (v.match(/[^\.]+$/gi)||[''])[0])
							.replace(/\$(size)/gi, sl(file.size || 0))
							.replace(/\$(i)/gi, i)
						;
					});

					//$.each(files, function (i, file) {
						// Create label elements
						var
							r = $('<div class="MultiFile-label"></div>'),
							b = $('<a class="MultiFile-remove" href="#' + MultiFile.wrapID + '">' + MultiFile.STRING.remove + '</a>')
								
								// ********
								// TODO:
								// refactor this as a single event listener on the control's
								// wrapper for better performance and cleaner code
								// ********
								.click(function () {

									// get list of files being removed
									var files_being_removed = FILE_LIST(slave);
									
									//# Trigger Event! onFileRemove
									MultiFile.trigger('FileRemove', slave, MultiFile, files_being_removed);
									//# End Event!

									MultiFile.n--;
									MultiFile.current.disabled = false;

									// remove the relevant <input type="file"/> element
									$(slave).remove();

									// remove the relevant label
									$(this).parent().remove();

									// Show most current element again (move into view) and clear selection
									$(MultiFile.current).css({
										position: '',
										top: ''
									});
									$(MultiFile.current).reset().val('').attr('value', '')[0].value = '';

									// point to currently visible element (always true, not necessary)
									//MultiFile.current = MultiFile.wrapper.find('[type=file]:visible');

									// rebuild array with the files that are left.
									var files_remaining = [], remain_size = 0;
									// go through each slave
									$(MultiFile.wrapper).find('input[type=file]').each(function(){
										// go through each file in each slave
										$.each(FILE_LIST(this), function (i, file) {
											if(file.name){
												//console.log('MultiFile.debug> FileRemove> remaining file', file.size, file);
												// fresh file array
												files_remaining[files_remaining.length] = file;
												// fresh size count
												remain_size += file.size;
											};
										});
									});

									// update MultiFile object
									MultiFile.files = files_remaining;
									MultiFile.total_size = remain_size;
									MultiFile.size_label = sl(remain_size);

									// update current control's reference to MultiFile object
									$(MultiFile.wrapper).data('MultiFile', MultiFile);

									//# Trigger Event! afterFileRemove
									MultiFile.trigger('afterFileRemove', slave, MultiFile, files_being_removed);
									//# End Event!

									//# Trigger Event! onFileChange
									MultiFile.trigger('FileChange', MultiFile.current, MultiFile, files_remaining);
									//# End Event!

									return false;
								});

						// Insert label
						MultiFile.list.append(
							r.append(b, ' ', names)
						);

					//}); // each file?

					//# Trigger Event! afterFileAppend
					MultiFile.trigger('afterFileAppend', slave, MultiFile, files);
					//# End Event!

					//# Trigger Event! onFileChange
					MultiFile.trigger('FileChange', slave, MultiFile, MultiFile.files);
					//# End Event!

				}; // MultiFile.addToList
				// Add element to selected files list



				// Bind functionality to the first element
				if (!MultiFile.MultiFile) MultiFile.addSlave(MultiFile.e, 0);

				// Increment control count
				//MultiFile.I++; // using window.MultiFile
				MultiFile.n++;

				// deprecated: contorl's data now stored in wrapper because it is never removed.
				// improved performance and lower memory comsumption
				// Save control to element
				//MultiFile.E.data('MultiFile', MultiFile);


				//#####################################################################
				// MAIN PLUGIN FUNCTIONALITY - END
				//#####################################################################
			}); // each element
	};

	/*--------------------------------------------------------*/

	/*
	### Core functionality and API ###
	*/
	$.extend($.fn.MultiFile, {


		/**
		 * This method exposes the all the control's data
		 *
		 * Returns an object with various settings and properties of the selected files
		 * for this particular instance of the control. stored in the control's wrapper
		 *
		 * @name data
		 * @type Object
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $('#selector').MultiFile('data');
		 */
		data: function () {
			
			// analyse this element
			var e = $(this), b = e.is('.MultiFile-wrap');
			
			// get control wrapper
			var wp = b ? e : e.data('MultiFile-wrap');
			if(!wp || !wp.length)
				return !console.error('Could not find MultiFile control wrapper');
			
			// get control data from wrapper
			var mf = wp.data('MultiFile');
			if(!mf)
				return !console.error('Could not find MultiFile data in wrapper');
			
			// return data
			return mf || {};
		},


		/**
		 * This method removes all selected files
		 *
		 * Returns a jQuery collection of all affected elements.
		 *
		 * @name reset
		 * @type jQuery
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $.fn.MultiFile.reset();
		 */
		reset: function () {
			var mf = this.MultiFile('data');
			if (mf) $(mf.list).find('a.MultiFile-remove').click();
			return $(this);
		},


		/**
		 * This method exposes the array of selected files
		 *
		 * Returns an array of file objects
		 *
		 * @name files
		 * @type Array
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $('#selector').MultiFile('files');
		 */
		files: function () {
			var mf = this.MultiFile('data');
			if(!mf) return !console.log('MultiFile plugin not initialized');
			return mf.files || [];
		},


		/**
		 * This method exposes the plugin's sum of the sizes of all files selected
		 *
		 * Returns size (in bytes) of files selected
		 *
		 * @name size
		 * @type Number
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $('#selector').MultiFile('size');
		 */
		size: function () {
			var mf = this.MultiFile('data');
			if(!mf) return !console.log('MultiFile plugin not initialized');
			return mf.total_size || 0;
		},


		/**
		 * This method exposes the plugin's tally of how many files have been selected
		 *
		 * Returns number (a count) of files selected
		 *
		 * @name count
		 * @type Number
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $('#selector').MultiFile('size');
		 */
		count: function () {
			var mf = this.MultiFile('data');
			if(!mf) return !console.log('MultiFile plugin not initialized');
			return mf.files ? mf.files.length || 0 : 0;
		},


		/**
		 * This utility makes it easy to disable all 'empty' file elements in the document before submitting a form.
		 * It marks the affected elements so they can be easily re-enabled after the form submission or validation.
		 *
		 * Returns a jQuery collection of all affected elements.
		 *
		 * @name disableEmpty
		 * @type jQuery
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $.fn.MultiFile.disableEmpty();
		 * @param String class (optional) A string specifying a class to be applied to all affected elements - Default: 'mfD'.
		 */
		disableEmpty: function (klass) {
			klass = (typeof (klass) == 'string' ? klass : '') || 'mfD';
			var o = [];
			$('input:file.MultiFile').each(function () {
				if ($(this).val() == '') o[o.length] = this;
			});

			// automatically re-enable for novice users
			window.clearTimeout($.fn.MultiFile.reEnableTimeout);
			$.fn.MultiFile.reEnableTimeout = window.setTimeout($.fn.MultiFile.reEnableEmpty, 500);
			
			return $(o).each(function () {
				this.disabled = true
			}).addClass(klass);
		},


		/**
		 * This method re-enables 'empty' file elements that were disabled (and marked) with the $.fn.MultiFile.disableEmpty method.
		 *
		 * Returns a jQuery collection of all affected elements.
		 *
		 * @name reEnableEmpty
		 * @type jQuery
		 * @cat Plugins/MultiFile
		 * @author Diego A. (http://www.fyneworks.com/)
		 *
		 * @example $.fn.MultiFile.reEnableEmpty();
		 * @param String klass (optional) A string specifying the class that was used to mark affected elements - Default: 'mfD'.
		 */
		reEnableEmpty: function (klass) {
			klass = (typeof (klass) == 'string' ? klass : '') || 'mfD';
			return $('input:file.' + klass).removeClass(klass).each(function () {
				this.disabled = false
			});
		},


		/**
		* This method will intercept other jQuery plugins and disable empty file input elements prior to form submission
		*

		* @name intercept
		* @cat Plugins/MultiFile
		* @author Diego A. (http://www.fyneworks.com/)
		*
		* @example $.fn.MultiFile.intercept();
		* @param Array methods (optional) Array of method names to be intercepted
		*/
		intercepted: {},
		intercept: function (methods, context, args) {
			var method, value;
			args = args || [];
			if (args.constructor.toString().indexOf("Array") < 0) args = [args];
			if (typeof (methods) == 'function') {
				$.fn.MultiFile.disableEmpty();
				value = methods.apply(context || window, args);
				//SEE-http://code.google.com/p/jquery-multifile-plugin/issues/detail?id=27
				setTimeout(function () {
					$.fn.MultiFile.reEnableEmpty()
				}, 1000);
				return value;
			};
			if (methods.constructor.toString().indexOf("Array") < 0) methods = [methods];
			for (var i = 0; i < methods.length; i++) {
				method = methods[i] + ''; // make sure that we have a STRING
				if (method)(function (method) { // make sure that method is ISOLATED for the interception
					$.fn.MultiFile.intercepted[method] = $.fn[method] || function () {};
					$.fn[method] = function () {
						$.fn.MultiFile.disableEmpty();
						value = $.fn.MultiFile.intercepted[method].apply(this, arguments);
						//SEE http://code.google.com/p/jquery-multifile-plugin/issues/detail?id=27
						setTimeout(function () {
							$.fn.MultiFile.reEnableEmpty()
						}, 1000);
						return value;
					}; // interception
				})(method); // MAKE SURE THAT method IS ISOLATED for the interception
			}; // for each method
		} // $.fn.MultiFile.intercept

	});

	/*--------------------------------------------------------*/

	/*
	### Default Settings ###
	eg.: You can override default control like this:
	$.fn.MultiFile.options.accept = 'gif|jpg';
	*/
	$.fn.MultiFile.options = { //$.extend($.fn.MultiFile, { options: {
		accept: '', // accepted file extensions
		max: -1, // maximum number of selectable files
		maxfile: -1, // maximum size of a single file
		maxsize: -1, // maximum size of entire payload

		// name to use for newly created elements
		namePattern: '$name', // same name by default (which creates an array)
		/*master name*/ // use $name
		/*master id */ // use $id
		/*group count*/ // use $g
		/*slave count*/ // use $i
		/*other	 */ // use any combination of he above, eg.: $name_file$i

		// previews
		preview: false,
		previewCss: 'max-height:100px; max-width:100px;',

		// STRING: collection lets you show messages in different languages
		STRING: {
			remove: 'x',
			denied: 'You cannot select a $ext file.\nTry again...',
			file: '$file',
			selected: 'File selected: $file',
			duplicate: 'This file has already been selected:\n$file',
			toomuch: 'The files selected exceed the maximum size permited ($size)',
			toomany: 'Too many files selected (max: $max)',
			toobig: '$file is too big (max $size)'
		},

		// name of methods that should be automcatically intercepted so the plugin can disable
		// extra file elements that are empty before execution and automatically re-enable them afterwards
		autoIntercept: ['submit', 'ajaxSubmit', 'ajaxForm', 'validate', 'valid' /* array of methods to intercept */ ],

		// error handling function
		error: function (s) {

			if(typeof console != 'undefined') console.log(s);

			// TODO: add various dialog handlers here?
			alert(s);
		}
	}; //} });

	/*--------------------------------------------------------*/

	/*
	### Additional Methods ###
	Required functionality outside the plugin's scope
	*/

	// Native input reset method - because this alone doesn't always work: $(element).val('').attr('value', '')[0].value = '';
	$.fn.reset = $.fn.reset || function () {
		return this.each(function () {
			try {
				this.reset();
			} catch (e) {}
		});
	};

	/*--------------------------------------------------------*/

	/*
	### Default implementation ###
	The plugin will attach itself to file inputs
	with the class 'multi' when the page loads
	*/
	$(function () {
		//$("input:file.multi").MultiFile();
		$("input[type=file].multi").MultiFile();
	});



	/*# AVOID COLLISIONS #*/
})(jQuery);
/*# AVOID COLLISIONS #*/
