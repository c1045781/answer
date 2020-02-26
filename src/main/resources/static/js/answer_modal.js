function deleteUserModal(id) {
    $('#deleteId').val(id);
    $('#deleteUserModal').modal();
}

function deleteExerciseModal(id) {
    $('#deleteId').val(id);
    $('#deleteExerciseModal').modal();
}

function deleteExerciseSetModal(id) {
    $('#deleteId').val(id);
    $('#deleteExerciseSetModal').modal();
}

function deleteSubjectModal(id) {
    $('#deleteId').val(id);
    $('#deleteSubjectModal').modal();
}

function deleteCommentModal(id) {
    $('#deleteId').val(id);
    $('#deleteCommentModal').modal();
}

function updateUserModal(id) {
    window.location.href = "/user/toUpdate?userId=" + id;
}

function updateExerciseModal(id) {
    window.location.href = "/exercise/toUpdate?exerciseId=" + id;
}

function updateExerciseSetModal(id) {
    window.location.href = "/exerciseSet/toUpdate?exerciseSetId=" + id;
}

function updateSubjectModal(id) {
    window.location.href = "/subject/toUpdate?subjectId=" + id;
}

function deleteUser() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/user/delete',
        type: 'post',
        data: {userId: id},
        dataType: "text",
        success: function (data) {
            $('#deleteUserModal').modal('hide');
            if(data == 'success'){
                $('#deleteResultSuccessModal').modal();
            }else{
                $('#deleteResultErrorModal').modal();
            }
            $('#resultUrl').val("/user/check");
        }
    });
}

function deleteExercise() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/exercise/delete',
        type: 'post',
        data: {exerciseId: id},
        dataType: "text",
        success: function (data) {
            $('#deleteExerciseModal').modal('hide');
            if(data == 'success'){
                $('#deleteResultSuccessModal').modal();
            }else{
                $('#deleteResultErrorModal').modal();
            }
            $('#resultUrl').val("/exercise/check");
        }
    });
}

function deleteExerciseSet() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/exerciseSet/delete',
        type: 'post',
        data: {exerciseSetId: id},
        dataType: "text",
        success: function (data) {
            $('#deleteExerciseSetModal').modal('hide');
            if(data == 'success'){
                $('#deleteResultSuccessModal').modal();
            }else{
                $('#deleteResultErrorModal').modal();
            }
            $('#resultUrl').val("/exerciseSet/check");
        },error:function(){
            alert("error");
        }
    });
}

function deleteSubject() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/subject/delete',
        type: 'post',
        data: {subjectId: id},
        dataType: "text",
        success: function (data) {
            $('#deleteSubjectModal').modal('hide');
            if(data == 'success'){
                $('#deleteResultSuccessModal').modal();
            }else{
                $('#deleteResultErrorModal').modal();
            }
            $('#resultUrl').val("/subject/check");
        }
    });
}

function deleteComment() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/comment/delete',
        type: 'post',
        data: {id: id},
        dataType: "text",
        success: function (data) {
            $('#deleteCommentModal').modal('hide');
            if(data == 'success'){
                $('#deleteResultSuccessModal').modal();
            }else{
                $('#deleteResultErrorModal').modal();
            }
            $('#resultUrl').val("/comment/check");
        }
    });
}

function resultOK(){
    var href = $('#resultUrl').val();
    window.location.href = href;
}

function viewExerciseReviewModal(id){
    var content = '';
    $('#option').html('');
    $.ajax({
        url: '/exercise/viewExercise',
        type: 'post',
        data: {id: id},
        dataType: "json",
        success: function (data) {
            $('#title').html(data.exercise.exerciseTitle);
            $('#correct').html("正确答案：" + data.exercise.correct);
            $.each(data.options,function(index,item){
                content += '<span class="list-group-item-text option-span">'+item.option + '.' + item.content + '</span>';

            });
            $('#option').append(content);
            $('#viewExerciseReview').modal();
        }
    });
}

function successExerciseReviewModal(id) {
    $.ajax({
        url: '/exercise/status',
        type: 'post',
        data: {id: id,status:1},
        dataType: "text",
        success: function (data) {
            $('#successModal').modal();
            $('#resultUrl').val("/exercise/review");
        }
    });
}

function failExerciseReviewModal(id) {
    $.ajax({
        url: '/exercise/status',
        type: 'post',
        data: {id: id,status:3},
        dataType: "text",
        success: function (data) {
            $('#successModal').modal();
            $('#resultUrl').val("/exercise/review");
        }
    });

}function successPermissionReviewModal(id,userId) {
    $.ajax({
        url: '/permission/role',
        type: 'post',
        data: {id: id,role:3,userId:userId},
        dataType: "text",
        success: function (data) {
            $('#successModal').modal();
            $('#resultUrl').val("/permission/check");
        }
    });
}

function failPermissionReviewModal(id,userId) {
    $.ajax({
        url: '/permission/role',
        type: 'post',
        data: {id: id,role:2,userId:userId},
        dataType: "text",
        success: function (data) {
            $('#successModal').modal();
            $('#resultUrl').val("/permission/check");
        }
    });

}

function showPermission(id){
    $.ajax({
        url: '/permission/checkOfOne',
        type: 'post',
        data: {id: id},
        dataType: "json",
        success: function (data) {
            $("#permission_content").html(data.content);
            if (data.status == 0){
                $("#permission_status").html("未处理");
            } else if (data.status == 1){
                $("#permission_status").html("同意");
            } else {
                $("#permission_status").html("拒绝");
            }

            $("#permission_date").html(Format(new Date(data.createTime),"yyyy-MM-dd hh:mm:ss"));
            if (data.reason != null && data.reason != "") {
                $("#permission_reason").html(data.reason);
            }else {
                $("#permission_is").css("display","none");
            }
            $('#showPermission').modal();
        }
    });
}

function addPermission(){
    $('#addPermission').modal();
}