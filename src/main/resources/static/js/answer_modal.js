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

function updateUserModal(id) {
    window.location.href = "/user/toUpdate?userId=" + id;
}

function updateExerciseModal(id) {
    window.location.href = "/exercise/toUpdate?exerciseId=" + id;
}

function updateExerciseSetModal(id) {
    window.location.href = "/exerciseSet/toUpdate?exerciseSetId=" + id;
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

function resultOK(){
    var href = $('#resultUrl').val();
    window.location.href = href;
}
