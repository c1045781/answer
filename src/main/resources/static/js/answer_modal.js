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
    $.ajax({
        url: '/exercise/viewExercise',
        type: 'post',
        data: {id: id},
        dataType: "json",
        success: function (data) {
            var content = '';
            $('#title').html(data.exercise.exerciseTitle);
            $('#correct').html("正确答案：" + data.exercise.correct);
            $('#analysis').html("答案解析：" + data.exercise.analysis);
            $.each(data.options,function(index,item){
                content += '<span class="list-group-item-text option-span">'+item.option + '.' + item.content + '</span>';

            });
            $('#option').html(content);
            $('#viewExerciseReview').modal();
        }
    });
}

function successExerciseReviewModal(exerciseId,messageId) {
    $.ajax({
        url: '/exercise/status',
        type: 'post',
        data: {exerciseId: exerciseId,status:1,messageId:messageId},
        dataType: "text",
        success: function (data) {
            $('#successModal').modal();
            $('#resultUrl').val("/exercise/review");
        }
    });
}

function failExerciseReviewModal() {
    var id = $("#reasonUserId").val();
    var reasonId = $("#reasonId").val();
    var reason = $("#reason").val();
    $.ajax({
        url: '/exercise/status',
        type: 'post',
        data: {exerciseId: id,status:2,reason:reason,messageId:reasonId},
        dataType: "text",
        success: function () {
            $('#reasonModal').modal("hide");
            $('#successModal').modal();
            $('#resultUrl').val("/exercise/review");
        }
    });

}function successPermissionReviewModal(id,userId) {
    $.ajax({
        url: '/permission/role',
        type: 'post',
        data: {id: id,role:3,userId:userId,status:1},
        dataType: "text",
        success: function (data) {
            $('#successModal').modal();
            $('#resultUrl').val("/permission/check");
        }
    });
}

function reasonModal(reasonId,id){
    $("#reasonUserId").val(id);
    $("#reasonId").val(reasonId);
    $('#reasonModal').modal();
}

function failPermissionReviewModal() {
    var userId = $("#reasonUserId").val();
    var id = $("#reasonId").val();
    var reason = $("#reason").val();
    $.ajax({
        url: '/permission/role',
        type: 'post',
        data: {id: id,role:2,userId:userId,reason:reason,status:2},
        dataType: "text",
        success: function (data) {
            $('#reasonModal').modal('hide');
            $('#successModal').modal();
            $('#resultUrl').val("/permission/check");
        }
    });

}

function showAddExercise(e){
    $.ajax({
        url: '/exercise/checkOfAddExercise',
        type: 'post',
        data: {exerciseId: e},
        dataType: "json",
        success: function (data) {

            var content = '';
            $('#addExerciseTitle').html(data.exercise.exerciseTitle);
            $('#addExerciseCorrect').html("正确答案：" + data.exercise.correct);
            $('#addExerciseAnalysis').html("答案解析：" + data.exercise.analysis);
            $('#addExerciseSubject').html("题目分类：" + data.subject.baseSubject +'»'+data.subject.name);
            $.each(data.options,function(index,item){
                content += '<span class="list-group-item-text option-span">'+item.option + '.' + item.content + '</span>';

            });
            $('#addExerciseOption').html(content);

            if (data.message.status == 0){
                $("#addExercise_status").html("未处理");
            } else if (data.message.status == 1){
                $("#addExercise_status").html("审核通过");
            } else {
                $("#addExercise_status").html("审核未通过");
            }

            $("#addExercise_date").html(Format(new Date(data.message.createTime),"yyyy-MM-dd hh:mm:ss"));
            if (data.message.reason != null && data.message.reason != "") {
                $("#modify").attr("onclick","modifyExercise("+ data.exercise.exerciseId +")");
                $("#modify").css("display","block");
                $("#addExercise_reason").html(data.message.reason);
            }else {
                $("#modify").css("display","none");
                $("#addExercise_is").css("display","none");
            }
            $("#showAddExercise").modal();
        }
    })

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
                $("#permission_status").html("未审核");
            } else if (data.status == 1){
                $("#permission_status").html("审核通过");
            } else {
                $("#permission_status").html("审核未通过");
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