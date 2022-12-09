var main = { //main이란 객체 생성해서 function 유효 범위 선언
        init : function () {
            var _this = this;
            /*var flag = false;*/
            $('#btn-save').on('click', function () {
                _this.save();
            });

            $('#btn-update').on('click', function () {
                _this.update();
            });
            $('#btn-delete').on('click', function () {
                _this.delete();
            });

    },
    save: function () {
        let data = {
            title : $('#title').val(),
            content: $('#content').val()
        }
        let fileImg = $('#filename')[0].files[0];

        let formdata = new FormData();
        formdata.append("p", new Blob([JSON.stringify(data)],
            {type: "application/json"}))
        formdata.append("f", fileImg);

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts/save',
            contentType: false,
            data: formdata,
            processData:false,
        }).done(function () {
            alert('글이 등록되었습니다.');
            window.location.href = '/'; //글 등록이 성공하면 메인페이지("/")로 이동
        }).fail(function (error) {
            alert('error');
        });
    },
    update : function () {

        var data = {
            title: $('#title').val(),
            content: $('#content').val(),
            deleteFlag: $('#flag').val() //삭제 버튼 눌렀을 경우 true가 감
        };

        var id = $('#id').val();

        let fileImg = $('#filename')[0].files[0];

        let formdata = new FormData();
        formdata.append("p", new Blob([JSON.stringify(data)],
            {type: "application/json"}))
        formdata.append("f", fileImg);

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            contentType: false,
            data: formdata,
            processData:false,
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    <!--delete 기능 추가-->
    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE', <!--삭제 관련 http 메소드인 DELETE-->
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/'; <!--삭제 후 메인화면으로 복귀-->
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};
main.init();