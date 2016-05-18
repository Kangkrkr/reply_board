module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    // task를 정의한다. 여기서는 concat과 uglify가 정의 되어있으며, cmd창에서 grunt <task>의 명령어로 실행이 가능하다.
    concat: {	// src에 명시된 파일의 소스들을 dest에 명시한 파일로 이어붙여줌(concat)
      options: {
    	// concat된 파일의 제목에 날짜를 적어준다.
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      basic: {
        src: 'src/main/resources/static/css/*',
        dest: 'src/main/resources/static/css/tmup_board.css'
      },
      extras: {
    	  src: ['src/main/resources/static/js/ajax/ajax.js',
    	        'src/main/resources/static/js/common/common.js',
    	        'src/main/resources/static/js/multipart/multipart.js',
    	        'src/main/resources/static/js/multipart/jquery.form.js',
    	        'src/main/resources/static/js/multipart/jquery.MultiFile.js'
    	        ],
    	  dest: 'src/main/resources/static/js/tmup_board.js'
      }
    },
    uglify: {	// src에 명시한 파일의 소스를 uglify 시킨다. 소스는 공백이 제거되어 압축됨.
    	options:{
    		banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
    	},
    	build: {
    		src: 'src/main/resources/static/js/tmup_board.js',
    		dest: 'src/main/resources/static/js/tmup_board.min.js'
    	}
    }
  });

  // npm task를 로드.
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  
  // default task를 명시한다.
  grunt.registerTask('concate', ['concat']);
  grunt.registerTask('default', ['uglify']);

};