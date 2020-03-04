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

function deleteExerciseSetForUser(e) {
    $.ajax({
        url: '/exerciseSet/deleteExerciseSetForUser',
        type: 'post',
        data: {exerciseSetId: e},
        dataType: "text",
        success: function (data) {
            alert("删除成功！");
            window.location.href="/user/personal";
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
            $("#exampleModalLabel2").html("习题详情");
            var content = "<div class='list-group'>" +
                "<h3 class='list-group-item-heading exercise-list-title'>"+data.exercise.exerciseTitle+"</h3>"+
                "<div class='exercise-list-info'>";
            $.each(data.options,function(index,item){
                content += '<p class="list-group-item-text option-span">'+item.option + '.' + item.content + '</p>';
            });
            content += "</div>" +
                "<div class='exercise-list-info'>"+"题目分类："+data.subject.baseSubject +'»'+data.subject.name+"</div>" +
                "<div class='exercise-list-info'>"+"正确答案："+data.exercise.correct+"</div>" +
                "<div class='exercise-list-info'>"+"答案解析："+data.exercise.analysis+"</div>" +
                "</div>";

            /*$('#addExerciseTitle').html(data.exercise.exerciseTitle);
            $('#addExerciseCorrect').html("正确答案：" + data.exercise.correct);
            $('#addExerciseAnalysis').html("答案解析：" + data.exercise.analysis);
            $('#addExerciseSubject').html("题目分类：" + data.subject.baseSubject +'»'+data.subject.name);
            $.each(data.options,function(index,item){
                content += '<span class="list-group-item-text option-span">'+item.option + '.' + item.content + '</span>';

            });
            $('#addExerciseOption').html(content);*/
            content += "<section id='permission' class='kopa-area' style='padding: 20px 0'><div class='container'><div class='shop-content'>" +
                "<div class='product has-post-thumbnail'>" +
                "<div class='summary entry-summary' style='float: none;'>" +
                "<div class='product_meta'>";
            if (data.message.status == 0){
                content += "<span class='sku_wrapper'>申请状态：未处理<span class='sku' id='addExercise_status'></span></span>";
            } else if (data.message.status == 1){
                content += "<span class='sku_wrapper'>申请状态：审核通过<span class='sku' id='addExercise_status'></span></span>";
            } else {
                content += "<span class='sku_wrapper'>申请状态：审核未通过<span class='sku' id='addExercise_status'></span></span>";
            }
            if (data.message.reason != null && data.message.reason != "") {
                content += "<span class='posted_in' id='addExercise_is'>拒绝理由："+data.message.reason+"<span class='sku' id='addExercise_reason'></span></span>";
                $("#modify").attr("onclick","modifyExercise("+ data.exercise.exerciseId +")");
                $("#modify").css("display","block");
            }else {
                $("#modify").css("display","none");
            }
                content +="<span class='tagged_as'>申请日期:"+Format(new Date(data.message.createTime),"yyyy-MM-dd hh:mm:ss")+"<span class='sku' id='addExercise_date'></span>" +
                            "</span></div></div></div></div></div></section>";



            // $("#addExercise_date").html(Format(new Date(data.message.createTime),"yyyy-MM-dd hh:mm:ss"));
            $('#exerciseBody').html(content);
            $("#permission").css("display","block");
            $("#showAddExercise").modal();
        }
    })

}

function showAddExerciseSet(e,page){
    $.ajax({
        url: '/exerciseSet/checkOfExerciseSet',
        type: 'post',
        data: {exerciseSetId: e,currentPage:page},
        dataType: "json",
        success: function (data) {
            $("#exampleModalLabel2").html("套题详情");
            var content = '';
            var list = data.dataList[0].list;
            for (var i = 0; i < list.length; i++){
                content += "<div class='list-group'>" +
                        "<h3 class='list-group-item-heading exercise-list-title'>"+list[i].exercise.exerciseTitle+"</h3>"+
                        "<div class='exercise-list-info'>";
                $.each(list[i].options,function(index,item){
                    content += '<p class="list-group-item-text option-span">'+item.option + '.' + item.content + '</p>';
                });
                content += "</div>" +
                        "<div class='exercise-list-info'>"+"题目分类："+list[i].subject.baseSubject +'»'+list[i].subject.name+"</div>" +
                        "<div class='exercise-list-info'>"+"正确答案："+list[i].exercise.correct+"</div>" +
                        "<div class='exercise-list-info'>"+"答案解析："+list[i].exercise.analysis+"</div>" +
                        "</div>";
            }

            if(data.totalPage !=0) {
                content += "<nav id='navPage' style='text-align: center;'>" +
                    "<ul class='pagination'>";
                if (data.currentPage > 1) {
                    content += "<li><a onclick='showAddExerciseSet(" + data.dataList[0].exerciseSet.exerciseSetId + "," + (data.currentPage - 1) + ")'  href='javascript:void(0)' aria-label='Previous'>";
                } else {
                    content += "<li><a onclick='showAddExerciseSet(" + data.dataList[0].exerciseSet.exerciseSetId + ",1)'  href='javascript:void(0)' aria-label='Previous'>";
                }
                content += "<span aria-hidden='true'>&lt;</span></a></li>";
                if (data.totalPage >= 1) {
                    content += " <li><a id=href='javascript:void(0)' >" + data.currentPage + "/" + data.totalPage + "</a></li>";
                } else {
                    content += " <li><a id=href='javascript:void(0)' >" + 0 / 0 + "</a></li>";
                }

                if (data.currentPage < data.totalPage) {
                    content += "<li><a onclick='showAddExerciseSet(" + data.dataList[0].exerciseSet.exerciseSetId + "," + (data.currentPage + 1) + ")'  href='javascript:void(0)' aria-label='Next'>";
                } else {
                    content += "<li><a onclick='showAddExerciseSet(" + data.dataList[0].exerciseSet.exerciseSetId + "," + data.totalPage + ")'  href='javascript:void(0)' aria-label='Next'>";
                }
                content += "<input type='hidden' id='page'>" +
                    "<input type='hidden' id='totalPage'>" +
                    "<span aria-hidden='true'>&gt;</span></a></li></ul></nav>";
            }
            $('#exerciseBody').html(content);
            $("#modify").attr("onclick","modifyExerciseSet("+ data.dataList[0].exerciseSet.exerciseSetId +")");
            $("#modify").css("display","block");
            $("#permission").css("display","none");
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