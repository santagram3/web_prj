<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>

    <!-- jquery -->
    <script src="/js/jquery-3.3.1.min.js"></script>

    <style>
        .fileDrop {
            width: 800px;
            height: 400px;
            border: 1px dashed gray;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 1.5em;
        }

        .uploaded-list {
            display: flex;
        }

        .img-sizing {
            display: block;
            width: 100px;
            height: 100px;
        }
    </style>

</head>

<body>

    <!-- 파일 업로드를 위한 form - 동기 처리 -->
    <form action="/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" multiple>
        <button type="submit">업로드</button>
    </form>

    <!-- 비동기 통신을 통한 실시간 파일 업로드 처리 -->
    <div class="fileDrop">
        <span>DROP HERE!!</span>
    </div>

    <!-- 
        - 파일 정보를 서버로 보내기 위해서는 <input type="file"> 이 필요
        - 해당 input태그는 사용자에게 보여주어 파일을 직접 선택하게 할 것이냐
          혹은 드래그앤 드롭으로만 처리를 할 것이냐에 따라 display를 상태를 결정
     -->
    <div class="uploadDiv">
        <input type="file" name="files" id="ajax-file" style="display:none;">
    </div>

    <!-- 업로드된 이미지의 썸네일을 보여주는 영역 -->
    <div class="uploaded-list">

    </div>


    <script>
        // start JQuery 
        $(document).ready(function () {

            function isImageFile(originFileName) {
                //정규표현식
                const pattern = /jpg$|gif$|png$/i;
                // $ .. 로 끝나는것 뜻 i 는 대소문자 구분 안하겠다. 
                return originFileName.match(pattern);
            }

            // 파일의 확장자에 따른 렌더링 처리
            function checkExtType(fileName) {

                //원본 파일 명 추출
                let originFileName = fileName.substring(fileName.indexOf("_") + 1);
                // fileName이라는 문자열에서 indexOf 처음부터 _를 기준으로 그 뒤를 짜른다 ! 
                // 2022/08/01/qfwqfj[oqwjf_상어.jpg에서 
                // 상어.jpg 이걸 추출한다 

                //확장자 추출후 이미지인지까지 확인
                if (isImageFile(originFileName)) { // 파일이 이미지라면
                    // isImageFile 이것도 메서드 

                    const $img = document.createElement('img');
                    // 이미지 태그 생성 
                    $img.classList.add('img-sizing'); // css 를 쓰기 위해서 ! 만듬
                    // class="img-sizing"
                    // $img.setAttribute('src', fileName); 이렇게 하니까 보안상 이슈가 생김 그래서 

                    $img.setAttribute('src', '/loadFile?fileName=' + fileName);
                    // 경로 

                    $img.setAttribute('alt', originFileName); // 시각 장애인에 대한 배려 
                    // 이미지가 안나올시 , 또는 이미지를 볼수없는 사람이 읽을수 있도록 사진에 코멘트를 달아준다 ! 

                    // <img src = "이미지 경로" alt="이미지에 대한 설명 (코멘트)">

                    $('.uploaded-list').append($img);
                }
                // 이미지가 아니라면 다운로드 링크를 생성
                else {

                    const $a = document.createElement('a');
                    $a.setAttribute('href', '/loadFile?fileName=' + fileName);

                    const $img = document.createElement('img');
                    $img.classList.add('img-sizing');
                    $img.setAttribute('src', '/img/file_icon.jpg');
                    $img.setAttribute('alt', originFileName);

                    $a.append($img);
                    $a.innerHTML += '<span>' + originFileName + '</span';

                    $('.uploaded-list').append($a);

                }

            }


            // 드롭한 파일을 화면에 보여주는 함수
            function showFileData(fileNames) {
                // 2022/08/01/qfwqfj[oqwjf_상어.jpg 같은애들 묶음 

                // 이미지인지? 이미지가 아닌지에 따라 구분하여 처리
                // 이미지면 썸네일을 렌더링하고 아니면 다운로드 링크를 렌더링한다.
                for (let fileName of fileNames) {
                    checkExtType(fileName);
                }
            }



            // drag & drop 이벤트
            const $dropBox = $('.fileDrop');

            // drag 진입 이벤트
            $dropBox.on('dragover dragenter', e => {
                e.preventDefault();
                $dropBox
                    .css('border-color', 'red')
                    .css('background', 'lightgray');
            });

            // drag 탈출 이벤트
            $dropBox.on('dragleave', e => {
                e.preventDefault();
                $dropBox
                    .css('border-color', 'gray')
                    .css('background', 'transparent');
                    //transparent 투명색 
            });

            // drop 이벤트
            $dropBox.on('drop', e => {
                e.preventDefault();
                console.log('드롭 이벤트 작동!');
                console.log("===========================");

                // 드롭된 파일 정보를 서버로 전송

                // 1. 드롭된 파일 데이터 읽기
                console.log(e);
                console.log("===========================");

                const files = e.originalEvent.dataTransfer.files;

                console.log('drop file data: ', files);
                console.log("===========================");
                // 2. 읽은 파일 데이터를 input[type=file]태그에 저장
                const $fileInput = $('#ajax-file');
                // 밖에서는 안보이는 인풋창을 잡아와서 
                $fileInput.prop('files', files);
                // 인풋창에  files 놈을 'files' 라는 이름으로 저장한다 

                console.log("this is fileInput");
                console.log($fileInput);
                console.log("===========================");

                // 3. 파일 데이터를 비동기 전송하기 위해서는 FormData객체가 필요
                const formData = new FormData(); 
                // 파일 데이터를 담기위한 배열이나 리스트로 생각하면됨! 

                // 4. 전송할 파일들을 전부 FormData안에 포장
                for (let file of $fileInput[0].files) {
                    // 인풋창 안에는 여러가지 프로퍼티가 있는데 그중에 0번째공간에 파일들이 담겨져 있다 . 
                    // 그래서 그안에서 파일이 담겨져 있는 0번째 있는것들을 formData 리스트,객체.에 넣어준다 
                    formData.append('files', file);
                    // formData라는곳에 
                }
                console.log('this is formData');
                console.log(formData);

                // 5. 비동기 요청 전송
                const reqInfo = {
                    method: 'POST',
                    body: formData
                };
                fetch('/ajax-upload', reqInfo)
                    .then(res => {
                        //console.log(res.status);
                        return res.json();
                        // 여기까지가 파일 업로드이다 ! 저장까지의 과정 ! 
                    })
                    .then(fileNames => {
                        // 2022/08/01/qfwqfj[oqwjf_상어.jpg 이런거 묶음 

                        // 위에서 저장을 했고 , 저장되어있는 자리(경로)를 반환받았다 
                        // 여기서부터는 썸네일 보여주는 작업 
                        console.log(fileNames);

                        showFileData(fileNames);
                    });

            });

        });
        // end jQuery
    </script>


</body>

</html>