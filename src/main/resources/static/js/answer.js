function changeCorrect(e) {
    if (e == "单选题") {
        $('#correct_button').prop("disabled",false);
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'radio');
            $(this).find(":input").attr('required', 'true');
        })
    } else if (e == "判断题") {
        for (var i = 1; i < 100; i++) {
            deleteOption()
        }
        $('#correct_button').prop("disabled",true);
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'radio');
            $(this).find(":input").attr('required', 'true');
        })

    }else{
        $('#correct_button').prop("disabled",false);
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'checkbox');
            $(this).find(":input").removeAttr('required');
        })
    }

}

function showSubject(e) {
    var content = $('#select_option').val();
    if (content == null){
        content = "";
    }

    $.ajax({
        url: '/subject/subjectByBase',
        type: 'post',
        data: JSON.stringify({baseSubject: e}),
        contentType: "application/json",
        success: function (data) {
            if (data.length > 0) {
                $('#subjectId').html(content);
                for (var i = 0; i < data.length; i++) {
                    $('#subjectId').append("<option value='" + data[i].subjectId + "'>" + data[i].name + "</option>");
                }
            }else{
                $('#subjectId').append("<option>全部</option>");
            }
        }
    });
}

function showSubjectForUser(e) {
    $.ajax({
        url: '/subject/subjectByBase',
        type: 'post',
        data: JSON.stringify({baseSubject: e}),
        contentType: "application/json",
        success: function (data) {
            if (data != null) {
                $('#exerciseSetSubjectId').html("");
                for (var i = 0; i < data.length; i++) {
                    $('#exerciseSetSubjectId').append("<option value='" + data[i].subjectId + "'>" + data[i].name + "</option>");
                }
            }
        }
    });
}

function addOption() {
    var input = $("#correct").children(":last-child").find(":input").val().charCodeAt();
    var A = $("#" + String.fromCharCode(input));
    A.children(':last-child').addClass("disabled");
    $("#opts").append("<div class='option-div' id='" + String.fromCharCode(input + 1) + "'>" +
        "<label class='control-label' style='width: 5%;'> " + String.fromCharCode(input + 1) + "：</label>" +
        "<input type='text' class='form-control' name='answers' required='' autocomplete='off'>" +
        "<button onclick='deleteOption()' type='button' class='btn btn-danger option-button' >删除</button>" +
        "</div>");
    if ($("#type").val() == '多选题'){
        $("#correct").append("<label class='correct-label'><input type='checkbox' name='correct' value='" + String.fromCharCode(input + 1) + "'>" + String.fromCharCode(input + 1) + "</label>");
    }else {
        $("#correct").append("<label class='correct-label'><input type='radio' name='correct' required='true'  value='" + String.fromCharCode(input + 1) + "'>" + String.fromCharCode(input + 1) + "</label>");
    }
}

function deleteOption() {
    var input = $("#correct").children(":last-child").find(":input").val().charCodeAt();
    if (input < 67) return;
    var A = $("#" + String.fromCharCode(input - 1));
    A.children(':last-child').removeClass("disabled");
    $("#opts").children(":last-child").remove();
    $("#correct").children(":last-child").remove();
}

function addExercise(e) {
    if (e == 'button') {
        $("#page").val('');
        $("#totalPage").val('');
    }
    $.ajax({
        url: '/manager/exercise/getExerciseBySubjectId',
        type: 'post',
        data: {
            subjectId: $("#subjectId").val(), type: $("#exerciseTypeHidden").val()
            , search: $("#searchHidden").val(), currentPage: $("#page").val()
        },
        dataType: 'json',
        success: function (data) {
            if (data != null) {
                var list = data.dataList;
                $("#table-tbody").html("");
                for (var i = 0; i < list.length; i++) {
                    $("#table-tbody").append("<tr id='" + list[i].exercise.exerciseId + "'><td><input type='checkbox' value='" + list[i].exercise.exerciseId + "'></td>" +
                        "<td>" + list[i].exercise.exerciseId + "</td>" +
                        "<td>" + list[i].exercise.exerciseType + "</td>" +
                        "<td>" + list[i].exercise.exerciseTitle + "</td>" +
                        "<td><button type='button' class='btn btn-primary' onclick='showExerciseForManager("+ list[i].exercise.exerciseId +")'>确认</button></td>" +
                        "</tr>");
                }
                if (data.totalPage == 0) {
                    $("#page").val(data.totalPage);
                } else {
                    $("#page").val(data.currentPage);
                }
                $("#totalPage").val(data.totalPage);

                $("#pageA").html($("#page").val() + "/" + $("#totalPage").val());
                $("#search").val(data.search);
                $.each($("#exerciseType").children(), function () {
                    if ($(this).val() == data.type) {
                        $(this).prop('selected', 'true');
                    }
                })
            }
        }
    });
    $("#addExerciseModal").modal();
}

function addExerciseForUser(e) {
    if (e == 'button') {
        $("#page").val('');
        $("#totalPage").val('');
    }
    $.ajax({
        url: '/user/exercise/getExerciseBySubjectId',
        type: 'post',
        data: {
            subjectId: $("#exerciseSetSubjectId").val(), type: $("#exerciseTypeHidden").val()
            , search: $("#searchHidden").val(), currentPage: $("#page").val()
        },
        dataType: 'json',
        success: function (data) {
            if (data != null) {
                var list = data.dataList;
                $("#table-tbody").html("");
                for (var i = 0; i < list.length; i++) {
                    $("#table-tbody").append("<tr id='" + list[i].exercise.exerciseId + "'><td><input type='checkbox' value='" + list[i].exercise.exerciseId + "'></td>" +
                        "<td>" + list[i].exercise.exerciseId + "</td>" +
                        "<td>" + list[i].exercise.exerciseType + "</td>" +
                        "<td style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'>" + list[i].exercise.exerciseTitle + "</td>" +
                        "<td><button type='button' class='btn btn-primary' onclick='showExerciseForUser("+ list[i].exercise.exerciseId +")'>查看</button></td>" +
                        "</tr>");
                }
                if (data.totalPage == 0) {
                    $("#page").val(data.totalPage);
                } else {
                    $("#page").val(data.currentPage);
                }
                $("#totalPage").val(data.totalPage);

                $("#pageA").html($("#page").val() + "/" + $("#totalPage").val());
                $("#search").val(data.search);
                $.each($("#exerciseType").children(), function () {
                    if ($(this).val() == data.type) {
                        $(this).prop('selected', 'true');
                    }
                })
            }
        }
    });
    $("#addExerciseModal").modal();
}


function delPageForUser(e) {
    var page = $("#page").val();
    if (parseInt(page) <= 1) {
        $("#page").val(page);
    } else {
        $("#page").val(parseInt(page) - 1);
    }
    addExerciseForUser(e)
}

function addPageForUser(e) {
    var page = $("#page").val();
    var total = $("#totalPage").val();
    if (parseInt(page) < parseInt(total)) {
        $("#page").val(parseInt(page) + 1);
    } else {
        $("#page").val(total);
    }
    addExerciseForUser(e)
}

function submitSearchExerciseForUser(){
    var type = $("#exerciseType option:selected").val();
    var search = $("#search").val();
    $("#exerciseTypeHidden").val(type);
    $("#searchHidden").val(search);
    addExerciseForUser('button');
}

function submitSearchExercise(){
    var type = $("#exerciseType option:selected").val();
    var search = $("#search").val();
    $("#exerciseTypeHidden").val(type);
    $("#searchHidden").val(search);
    addExercise('button');
}

function addExerciseToset() {
    var id = new Array();
    $.each($('#exercise-tbody').children(), function (index) {
        id[index] = $(this).children(':first-child').find(':input').val();
    });
    $.each($('#table-tbody').children().find(':checkbox:checked'), function () {
        if (id.length == 0) {
            $("#exercise-div").css('display', 'block');
        }
        var flag = true;
        for (var i = 0; i < id.length; i++) {
            if ($(this).parent('td').parent('tr').children(':first-child').find(':input').val() == id[i]) {
                flag = false;
            }

        }
        if (flag) {
            var contents = "<tr>" + $(this).parent('td').parent('tr').html() +
                "<td>" +
                "<button type='button'onclick='deleteExerciseTbody(" + $(this).parent('td').parent('tr').children(':first-child').find(':input').val() + ")' class='btn btn-primary'> 删除 </button></td></tr>";
            $("#exercise-tbody").append(contents);
            $("#exercise-tbody tr").find("td:eq(0)").find(':input').prop('checked', 'true')
            $.each($("#exercise-tbody tr").find("td:eq(0)").find(':input'), function (index, e) {
                $(this).attr("name", "exerciseList[" + index + "].exerciseId");
            })
        }
    });
    $('#addExerciseModal').modal('hide');
}

function showExerciseForManager(id){
    $.ajax({
        url: '/manager/exercise/viewExercise',
        type: 'post',
        data: {exerciseId: id},
        dataType: "json",
        success: function (data) {

            var content = '';
                content += "<div class='list-group'>" +
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
            $('#exerciseBody').html(content);
            $("#modify").css("display","none");
            $('#permission').css("display","none");
            $('#showAddExercise').modal();
        }
    });
}

function showExerciseForUser(id){
    $.ajax({
        url: '/user/exercise/viewExercise',
        type: 'post',
        data: {exerciseId: id},
        dataType: "json",
        success: function (data) {

            var content = '';
                content += "<div class='list-group'>" +
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
            $('#exerciseBody').html(content);
            $("#modify").css("display","none");
            $('#permission').css("display","none");
            $('#showAddExercise').modal();
        }
    });
}



function deleteExerciseTbody(e) {
    $.each($('#exercise-tbody').children(), function () {
        if (e == $(this).children(':first-child').find(':input').val()) {
            $(this).remove()
        }
    });
}

function delPage(e) {
    var page = $("#page").val();
    if (parseInt(page) <= 1) {
        $("#page").val(page);
    } else {
        $("#page").val(parseInt(page) - 1);
    }
    addExercise(e)
}

function addPage(e) {
    var page = $("#page").val();
    var total = $("#totalPage").val();
    if (parseInt(page) < parseInt(total)) {
        $("#page").val(parseInt(page) + 1);
    } else {
        $("#page").val(total);
    }
    addExercise(e)
}

function submitUserFrom(e){
    var skip = parseInt(e);
    var type = $("#type").val();
    var role = $("#role").val();
    var search = $("#search").val();
    var total = parseInt($("#totalPage").val());
    if (type == 'id' && search != ""){
        var reg = /^[0-9]+.?[0-9]*$/;
        if (!reg.test(search)) {
            alert("ID格式错误");
            return;
        }

    }
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/user/check?currentPage=" + skip+"&role="+ role +"&type="+ type+"&search="+search;
}

function submitExerciseFrom(e){
    var skip = parseInt(e);
    var subjectId = parseInt($("#subjectId").val());
    var type = $("#type").val();
    var exerciseType = $("#exerciseType").val();
    var search = $("#search").val();
    var total = parseInt($("#totalPage").val());
    var score = $("#score").val();
    if (subjectId == null || isNaN(subjectId)){
        subjectId = "";
    }
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/exercise/check?currentPage=" + skip+"&subjectId="+ subjectId +"&type="+ type+"&search="+search+"&exerciseType="+exerciseType+"&score="+score;
}

function submitExerciseSetFrom(e){
    var skip = parseInt(e);
    var subjectId = parseInt($("#subjectId").val());
    var type = $("#type").val();
    var search = $("#search").val();
    var total = parseInt($("#totalPage").val());
    if (subjectId == null || isNaN(subjectId)){
        subjectId = "";
    }
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/exerciseSet/check?currentPage=" + skip+"&subjectId="+ subjectId +"&type="+ type+"&search="+search;
}

function submitSubjectFrom(e) {
    var skip = parseInt(e);
    var type = $("#type").val();
    var search = $("#search").val();
    var total = parseInt($("#totalPage").val());

    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/subject/check?currentPage=" + skip +"&type="+ type+"&search="+search;
}

function submitReviewExerciseFrom(e) {
    var skip = parseInt(e);
    var subjectId = parseInt($("#subjectId").val());
    var total = parseInt($("#totalPage").val());
    if (subjectId == null || isNaN(subjectId)){
        subjectId = "";
    }
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/exercise/review?currentPage=" + skip+"&subjectId="+ subjectId;
}

function submitReviewPermissionFrom(e) {
    var skip = parseInt(e);
    var total = parseInt($("#totalPage").val());
    if (subjectId == null || isNaN(subjectId)){
        subjectId = "";
    }
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/permission/review?currentPage=" + skip;
}

function submitCommentFrom(e){
    var skip = parseInt(e);
    var type = $("#type").val();
    var search = $("#search").val();
    var total = parseInt($("#totalPage").val());
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/manager/comment/check?currentPage=" + skip +"&type="+ type+"&search="+search;
}


function exerciseTypeChange(e){
    var subjectId = $("#hidden_subjectId").val();
    var search = $("#hidden_search").val();
    window.location.href = "/user/exercise/check?subjectId="+ subjectId+"&search="+search+"&exerciseType="+e;
}

function orderbyChange(e){
    var subjectId = $("#hidden_subjectId").val();
    var search = $("#hidden_search").val();
    window.location.href = "/user/exerciseSet/check?subjectId="+ subjectId+"&search="+search+"&orderby="+e;
}

/*function answer_Exercise_set(e){
    window.open("/exercise/exerciseSetToAnswer?exerciseSetId=" + e);
}*/

function confirmAnswer(){

    $("div[name='correct_div']").css('display','block');
    $("input[type=radio]:checked").each(function() {
        checked_name = $(this).attr('name');
        correct = $("#"+checked_name).html();
        if(correct.indexOf($(this).val()) >= 0){
            $("#correct_div"+checked_name).removeClass();
            $("#correct_div"+checked_name).addClass("alert alert-success");
            $("#isCorrect"+checked_name).html("正确");
        }else{
            $("#correct_div"+checked_name).removeClass();
            $("#correct_div"+checked_name).addClass("alert alert-danger");
            $("#isCorrect"+checked_name).html("错误");
        }

        $.ajax({
            url: '/user/answer/addOrUpdate',
            type: 'post',
            data: JSON.stringify({exerciseId: checked_name,answer: $(this).val()}),
            contentType: "application/json"
        });
    });

    var arr = new Array();
    $("input[type=checkbox]").each(function() {
        if($(this).attr('name') != '' && $.inArray($(this).attr('name'),arr) == '-1'){ // 获取所有题目id  (name = id)
            arr.push($(this).attr('name'));
        }
    });
    for (var i = 0; i < arr.length;i++){
        var arr_id = arr[i];
        var correct = $("#"+arr[i]).html();
        var correct_count =  correct.split('：')[1].split(',').length; // 答案个数
        var count = 0;
        var answer = '';
        $("input[name="+ arr[i] +"]:checked").each(function() {
            answer += $(this).val() + ",";
            console.log(answer);
            checked_name = $(this).attr('name');
            count++;
        });
        answer = answer.substr(0,answer.length-1);
        if(correct.indexOf(answer) >= 0){
            $("#correct_div"+checked_name).removeClass();
            $("#correct_div"+checked_name).addClass("alert alert-success");
            $("#isCorrect"+checked_name).html("正确");
        }else{
            $("#correct_div"+checked_name).removeClass();
            $("#correct_div"+checked_name).addClass("alert alert-danger");
            $("#isCorrect"+checked_name).html("错误");
        }
        if ( correct_count != count){
            $("#correct_div"+arr_id).removeClass();
            $("#correct_div"+arr_id).addClass("alert alert-danger");
            $("#isCorrect"+arr_id).html("错误");
        }

        $.ajax({
            url: '/user/answer/addOrUpdate',
            type: 'post',
            data: JSON.stringify({exerciseId: arr_id,answer: answer}),
            contentType: "application/json"
        });

        answer = '';
    }

    $("#a_confirm").css('display','none');
    // $("#correct_div").css('display','block');

}

function openExerciseComment(e,page){
    $.ajax({
        url: '/user/comment/getCommentDTOList',
        type: 'post',
        data: {parentId:e,currentPage:page},
        success:function(data){
            var content = "";
            var list = data.dataList;
            for (var i=0;i<list.length;i++){
                content += '<li class="comment">' +
                    '<article>' +
                    '<div class="comment-avatar ">' +
                    '<img alt="" src="'+ list[i].myUser.avatarImgUrl +'">' +
                    '</div>' +
                    '<div class="comment-content">' +
                    '<header>' +
                    '<div class="entry-meta ">' +
                    '<p class="entry-author"> <a href="#">'+ list[i].myUser.nickname +'</a> </p>' +
                    '<div>'+ Format(new Date(list[i].createTime),"yyyy-MM-dd hh:mm:ss") +'</div>' +
                    '</div>' +
                    '</header>' +
                    '<p>'+ list[i].content +'</p>' +
                    '<div class="comment-actions" style="margin-top: 0;">' +
                    '<span id="likeSpan'+list[i].commentId+'">' +
                    '<a class="fa fa-thumbs-o-up" href="javascript:void(0)" onclick="addExerciseCommentLike('+list[i].commentId +');" style="border: 0;">' +
                    '<span>（'+list[i].likeCount+'）</span></a></span>'+
                    '<a class="comment-reply-link" href="javascript:void(0)" onclick="ExerciseCommentSecond('+list[i].commentId +',this,'+list[i].exercise.exerciseId+');" style="border: 0;">回复</a>' +
                    '</div>'+
                    '</div>' +
                    '</article>';
                    content += '<ol class="children" id="children'+list[i].commentId+'">';
                if (list[i].paginationDTO != null && list[i].paginationDTO.length != 0){
                    var list1 = list[i].paginationDTO.dataList;
                    for (var j=0;j<list1.length;j++){
                        content += '<li class="comment">' +
                            '<article style="margin-top: 20px">' +
                            '<div class="comment-avatar ">' +
                            '<img alt="" src="'+ list1[j].myUser.avatarImgUrl +'">' +
                            '</div>' +
                            '<div class="comment-content">' +
                            '<header>' +
                            '<div class="entry-meta ">' +
                            '<p class="entry-author"> <a href="#">'+ list1[j].myUser.nickname +'</a> </p>' +
                            '<div>'+ Format(new Date(list1[j].createTime),"yyyy-MM-dd hh:mm:ss") +'</div>' +
                            '</div>' +
                            '</header>' +
                            '<p>回复 <a href="#" style="color: #3498DB;">'+list1[j].receiver.nickname+'</a> :'+list1[j].content+'</p>'+
                            '<div class="comment-actions" style="margin-top: 0px">' +
                            '<span id="likeSpan'+list1[j].commentId+'">' +
                            '<a class="fa fa-thumbs-o-up" href="javascript:void(0)" onclick="addExerciseCommentLike('+list1[j].commentId +');" style="border: 0;">' +
                            '<span>（'+list1[j].likeCount+'）</span></a></span>'+
                            '<a class="comment-reply-link" href="javascript:void(0)" onclick="ExerciseCommentSecond('+list1[j].commentId +',this,'+list1[j].exercise.exerciseId+');" style="border: 0;">回复</a>' +
                            '</div>'+
                            '</div>' +
                            '</article></li>';
                    }
                    if(list[i].paginationDTO.totalPage !=0 && list[i].paginationDTO.totalPage !=1) {
                        if (list[i].paginationDTO.currentPage == 1 && list[i].paginationDTO.currentPage != list[i].paginationDTO.totalPage) {
                            content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                            "<li>共"+list[i].paginationDTO.totalPage+"页</li><li class='disabled'><a href='javascript:void(0)'>&lt;</a></li>"+
                            "<li><a>第"+list[i].paginationDTO.currentPage+"页</a></li>"+
                            "<li><a href='javascript:void(0)' onclick='secondCommentPage("+list[i].commentId+","+(list[i].paginationDTO.currentPage+1)+")'>&gt;</a></li>"+
                            "</ul></nav></div>";
                        } else if (list[i].paginationDTO.currentPage == list[i].paginationDTO.totalPage && list[i].paginationDTO.currentPage != 1) {
                            content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                                "<li>共"+list[i].paginationDTO.totalPage+"页</li><li><a href='javascript:void(0)' onclick='secondCommentPage("+list[i].commentId+","+(list[i].paginationDTO.currentPage-1)+")'>&lt;</a></li>"+
                                "<li><a>第"+list[i].paginationDTO.currentPage+"页</a></li>"+
                                "<li class='disabled'><a href='javascript:void(0)'>&gt;</a></li>"+
                                "</ul></nav></div>";
                        } else /*if (list[i].paginationDTO.currentPage != list[i].paginationDTO.totalPage && list[i].paginationDTO.currentPage != 1)*/ {
                            content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                                "<li>共"+list[i].paginationDTO.totalPage+"页</li><li><a href='javascript:void(0)' onclick='secondCommentPage("+list[i].commentId+","+(list[i].paginationDTO.currentPage-1)+")'>&lt;</a></li>"+
                                "<li><a>第"+list[i].paginationDTO.currentPage+"页</a></li>"+
                                "<li><a href='javascript:void(0)' onclick='secondCommentPage("+list[i].commentId+","+(list[i].paginationDTO.currentPage+1)+")'>&gt;</a></li>"+
                                "</ul></nav></div>";
                        }
                    }else{
                        content += "";
                    }
                }
                content += '</ol></li>';
            }
            if(data.totalPage != 0 && data.totalPage != 1) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    content +="<div id='nav' style='margin-left: 25px;margin-top: 20px;'><nav aria-label='Page navigation'>" +
                        "<ul class='pagination pagination'>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='openExerciseComment("+data.dataList[0].exercise.exerciseId+"," + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav></div>";
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    content +="<div id='nav' style='margin-left: 25px;margin-top: 20px;'><nav aria-label='Page navigation'>" +
                        "<ul class='pagination pagination'>" +
                        "<li>" +
                        "<a onclick='openExerciseComment("+data.dataList[0].exercise.exerciseId+"," + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li   class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav></div>";
                } else /*if (data.currentPage != data.totalPage && data.currentPage != 1)*/ {
                    content +="<div id='nav' style='margin-left: 25px;margin-top: 20px;'><nav aria-label='Page navigation'>" +
                        "<ul class='pagination pagination'>" +
                        "<li>" +
                        "<a onclick='openExerciseComment("+data.dataList[0].exercise.exerciseId+"," + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='openExerciseComment("+data.dataList[0].exercise.exerciseId+"," + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav></div>";
                }
            }else {
                content +="";
            }
            $("#comment_ol"+e).html(content);
        }
    });
    $("#comment_section"+e).css('display','block');
    $("#a_close_comment"+e).css('display','block');
    $("#a_open_comment"+e).css('display','none');
}

function secondCommentPage(id,page){
    $.ajax({
        url:"/user/comment/secondComment",
        data:{commentId:id,currentPage:page},
        type:"post",
        dataType:"json",
        success:function(data){
            var content = "";
            var list = data.dataList;
            for (var j=0;j<list.length;j++){
                content += '<li class="comment">' +
                    '<article style="margin-top: 20px">' +
                    '<div class="comment-avatar ">' +
                    '<img alt="" src="'+ list[j].myUser.avatarImgUrl +'">' +
                    '</div>' +
                    '<div class="comment-content">' +
                    '<header>' +
                    '<div class="entry-meta ">' +
                    '<p class="entry-author"> <a href="#">'+ list[j].myUser.nickname +'</a> </p>' +
                    '<div>'+ Format(new Date(list[j].createTime),"yyyy-MM-dd hh:mm:ss") +'</div>' +
                    '</div>' +
                    '</header>' +
                    '<p>回复 <a href="#" style="color: #3498DB;">'+list[j].receiver.nickname+'</a> :'+list[j].content+'</p>'+
                    '<div class="comment-actions" style="margin-top: 0px">' +
                    '<span id="likeSpan'+list[j].commentId+'">' +
                    '<a class="fa fa-thumbs-o-up" href="javascript:void(0)" onclick="addExerciseCommentLike('+list[j].commentId +');" style="border: 0;">' +
                    '<span>（'+list[j].likeCount+'）</span></a></span>'+
                    '<a class="comment-reply-link" href="javascript:void(0)" onclick="ExerciseCommentSecond('+list[j].commentId +',this,'+list[j].exercise.exerciseId+');" style="border: 0;">回复</a>' +
                    '</div>'+
                    '</div>' +
                    '</article></li>';
            }
            if(data.totalPage !=0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                        "<li>共"+data.totalPage+"页</li><li class='disabled'><a href='javascript:void(0)'>&lt;</a></li>"+
                        "<li><a>第"+data.currentPage+"页</a></li>"+
                        "<li><a href='javascript:void(0)' onclick='secondCommentPage("+id+","+(data.currentPage+1)+")'>&gt;</a></li>"+
                        "</ul></nav></div>";
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                        "<li>共"+data.totalPage+"页</li><li><a href='javascript:void(0)' onclick='secondCommentPage("+id+","+(data.currentPage-1)+")'>&lt;</a></li>"+
                        "<li><a>第"+data.currentPage+"页</a></li>"+
                        "<li class='disabled'><a href='javascript:void(0)'>&gt;</a></li>"+
                        "</ul></nav></div>";
                } else {
                    content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                        "<li>共"+data.totalPage+"页</li><li><a href='javascript:void(0)' onclick='secondCommentPage("+id+","+(data.currentPage-1)+")'>&lt;</a></li>"+
                        "<li><a>第"+data.currentPage+"页</a></li>"+
                        "<li><a href='javascript:void(0)' onclick='secondCommentPage("+id+","+(data.currentPage+1)+")'>&gt;</a></li>"+
                        "</ul></nav></div>";
                }
            }
            $("#children"+id).html(content);
        }
    });
}

function addExerciseCommentLike(id){
     $.ajax({
         url:"/user/comment/addLike",
         data:{commentId:id},
         success:function(data){
            $("#likeSpan"+id).html("<a style='border: 0;color:#3498db;' class='fa fa-thumbs-o-up' href='javascript:void(0)' onclick='delExerciseCommentLike("+ id +");'><span>（"+data.data.likeCount+"） </span></a>");
         }
     });
}

function delExerciseCommentLike(id){
    $.ajax({
        url:"/user/comment/delLike",
        data:{commentId:id},
        success:function(data){
            $("#likeSpan"+id).html('<a class="fa fa-thumbs-o-up" href="javascript:void(0)" onclick="addExerciseCommentLike('+id +');" style="border: 0;"><span>（'+data.likeCount+'） </span></a>');
        }
    });
}
function ExerciseCommentSecond(id,e,exerciseId){
    if($("#commentSecondForm").html() != null){
        $("#commentSecondForm").remove();
    }
    $(e).parent().append("<form onsubmit='return false' id='commentSecondForm' method='post' class='comment-form  novalidate' novalidate=''>" +
        "<p class='textarea-block' style='width: 100%;'>" +
        "<textarea aria-required='true' name='content' rows='3' placeholder='' style='width: 80%;margin: 10px;'></textarea></p>" +
        "<p class='post-comment input-block'>" +
        "<button id='submit-comment' onclick='submitCommentSecond("+id+");' name='submit' type='submit' class='button-01' style='padding: 10px 10px;padding: 10px 40px;margin: 0 10px;'>发布</button>" +
        "</p></form>");

}

function submitCommentSecond(id){
    var content = $("#commentSecondForm textarea").val();
    if (content == ""){
        alert("评论内容不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        url: "/user/comment/addComment",
        data: JSON.stringify({parentId:id,type:2,content:content}),
        success:function (data) {
            if (data.code == 200){
                // 跳转到二级评论最后一页，最新评论
                newSecondComment(id);
                // openExerciseComment(exerciseId);
            } else {
                alert(data.message);
            }

        }
    });
    $("#commentSecondForm").remove()
}

function newSecondComment(id){
    $.ajax({
       url:"/user/comment/newSecondComment",
       data:{commentId:id},
       type:"post",
        async:false,
       dataType:"json",
       success:function(data){
           var commentId;
           $.ajax({
               url:"/user/comment/getFirstCommentId",
               data:{commentId:id},
               type:"post",
               async:false,
               success:function(data){
                   commentId = data
               }
           });
           debugger;
           var content = "";
           var list = data.dataList;
           for (var j=0;j<list.length;j++){
               content += '<li class="comment">' +
                   '<article style="margin-top: 20px">' +
                   '<div class="comment-avatar ">' +
                   '<img alt="" src="'+ list[j].myUser.avatarImgUrl +'">' +
                   '</div>' +
                   '<div class="comment-content">' +
                   '<header>' +
                   '<div class="entry-meta ">' +
                   '<p class="entry-author"> <a href="#">'+ list[j].myUser.nickname +'</a> </p>' +
                   '<div>'+ Format(new Date(list[j].createTime),"yyyy-MM-dd hh:mm:ss") +'</div>' +
                   '</div>' +
                   '</header>' +
                   '<p>回复 <a href="#" style="color: #3498DB;">'+list[j].receiver.nickname+'</a> :'+list[j].content+'</p>'+
                   '<div class="comment-actions" style="margin-top: 0px">' +
                   '<span id="likeSpan'+list[j].commentId+'">' +
                   '<a class="fa fa-thumbs-o-up" href="javascript:void(0)" onclick="addExerciseCommentLike('+list[j].commentId +');" style="border: 0;">' +
                   '<span>（'+list[j].likeCount+'）</span></a></span>'+
                   '<a class="comment-reply-link" href="javascript:void(0)" onclick="ExerciseCommentSecond('+list[j].commentId +',this,'+list[j].exercise.exerciseId+');" style="border: 0;">回复</a>' +
                   '</div>'+
                   '</div>' +
                   '</article></li>';
           }
           if(data.totalPage !=0 && data.totalPage !=1) {
               if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                   content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                       "<li>共"+data.totalPage+"页</li><li class='disabled'><a href='javascript:void(0)'>&lt;</a></li>"+
                       "<li><a>第"+data.currentPage+"页</a></li>"+
                       "<li><a href='javascript:void(0)' onclick='secondCommentPage("+commentId+","+(data.currentPage+1)+")'>&gt;</a></li>"+
                       "</ul></nav></div>";
               } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                   content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                       "<li>共"+data.totalPage+"页</li><li><a href='javascript:void(0)' onclick='secondCommentPage("+commentId+","+(data.currentPage-1)+")'>&lt;</a></li>"+
                       "<li><a>第"+data.currentPage+"页</a></li>"+
                       "<li class='disabled'><a href='javascript:void(0)'>&gt;</a></li>"+
                       "</ul></nav></div>";
               } else {
                   content += "<div id='nav' style='margin-top: 10px;'><nav aria-label='Page navigation'><ul class='pager' style='text-align: left;'>"+
                       "<li>共"+data.totalPage+"页</li><li><a href='javascript:void(0)' onclick='secondCommentPage("+commentId+","+(data.currentPage-1)+")'>&lt;</a></li>"+
                       "<li><a>第"+data.currentPage+"页</a></li>"+
                       "<li><a href='javascript:void(0)' onclick='secondCommentPage("+commentId+","+(data.currentPage+1)+")'>&gt;</a></li>"+
                       "</ul></nav></div>";
               }
           }

           $("#children"+commentId).html(content);
       }
    });
}

function closeExerciseComment(e) {
    $("#a_close_comment"+e).css('display','none');
    $("#a_open_comment"+e).css('display','block');
    $("#comment_section"+e).css('display','none');
}

function openExerciseNote(e){
    $.ajax({
        url: '/user/note/getNote',
        type: 'post',
        data: {"exerciseId":e},
        dataType:"json",
        success:function(data){
            console.log(data);
            $("#note_form"+e).css('display','none');
            if (data == null){
                $("#note_a"+e).html("添加笔记");
            } else{
                $("#note_a"+e).html("编辑");
                $("#note_p"+e).html(data.content);
                $("#noteId").val(data.noteId);
            }
        },
        error:function () {
            $("#note_form"+e).css('display','none');
            $("#note_a"+e).html("添加笔记");
        }
    });
    $("#note_section"+e).css('display','block');
    $("#a_close_note"+e).css('display','block');
    $("#a_open_note"+e).css('display','none');
}

function closeExerciseNote(e){
    $("#note_form"+e).css('display','none');
    $("#a_close_note"+e).css('display','none');
    $("#a_open_note"+e).css('display','block');
    $("#note_section"+e).css('display','none');
}

function showNoteForm(e){
    $("#note_textarea"+e).val($("#note_p"+e).html());
    $("#note_a"+e).html("");
    $("#note_p"+e).html("");
    $("#note_form"+e).css('display','block');
}

function Format(datetime,fmt) {
    if (parseInt(datetime)==datetime) {
        if (datetime.length==10) {
            datetime=parseInt(datetime)*1000;
        } else if(datetime.length==13) {
            datetime=parseInt(datetime);
        }
    }
    datetime=new Date(datetime);
    var o = {
        "M+" : datetime.getMonth()+1,                 //月份
        "d+" : datetime.getDate(),                    //日
        "h+" : datetime.getHours(),                   //小时
        "m+" : datetime.getMinutes(),                 //分
        "s+" : datetime.getSeconds(),                 //秒
        "q+" : Math.floor((datetime.getMonth()+3)/3), //季度
        "S"  : datetime.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (datetime.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}


function submitInfo(){
    if ($("#avatarImg")[0].files[0] != null){
        var size = $("#avatarImg")[0].files[0].size;
        var substring = $("#avatarImg").val().substring($("#avatarImg").val().lastIndexOf("."));
        var arr = [".jpg",".png",".jpeg"];
        var flag = false;
        for(var i=0;i<arr.length;i++)
        {
            if(substring == arr[i])
            {
                flag = true;
                break;
            }
        }
        if (!flag){
            alert("图片格式错误");
            return;
        }
        if (size > 1024*1024){
            alert("请选择1M以内图片");
            return;
        }
    }
    var phone = $("#phone").val();
    var reg = /(1[3-9]\d{9}$)/;
    if (!reg.test(phone)){
        alert("手机号格式错误");
        return;
    }
    var formData = new FormData();
    formData.append("avatarImg",$("#avatarImg")[0].files[0]);
    formData.append("userId",$("#userId").val());
    formData.append("avatarImgUrl",$("#avatarImgUrl").attr("src"));
    formData.append("username",$("#username").val());
    formData.append("phone",$("#phone").val());
    formData.append("description",$("#description").val());
    formData.append("nickname",$("#nickname").val());
    formData.append("sex",$("#sex").val());

    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/user/users/personal/update",
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success:function (data) {
            if (data.code == 200){
                // $("#fileinputA").click();
                personInformation();
            } else {
                alert(data.message);
            }

        }
    });
}

function submitExerciseUploadFile(){
    var file = $("#exerciseFile")[0].files[0];
    if (file == null){
        alert("请选择上传文件");
        return;
    }else {
        var substring = $("#exerciseFile").val().substring($("#exerciseFile").val().lastIndexOf("."));
        var arr = [".xls",".xlsx"];
        var flag = false;
        for(var i=0;i<arr.length;i++)
        {
            if(substring == arr[i])
            {
                flag = true;
                break;
            }
        }
        if (!flag){
            alert("文件类型错误");
            return;
        }
    }

    var formData = new FormData();
    formData.append("exerciseFile",$("#exerciseFile")[0].files[0]);
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/user/exercise/uploadFile",
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success:function (data) {
            if (data.code == 200){
                AddExerciseList(1);
                alert(data.message);
            } else {
                alert(data.message);
            }

        }
    });
}

function submitComment(e){
    var content = $('#comment_form' + e + ' textarea').val();
    if (content == ""){
        alert("评论内容不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/user/comment/addComment",
        contentType:"application/json",
        data: JSON.stringify({parentId:e,content:content}),
        success:function (data) {
            if (data.code == 200){
                openExerciseComment(e,data.data);
            } else {
                alert(data.message);
            }

        }
    });
    $("#comment_form"+ e +" textarea").val('');
}

function submitNote(e){
    $.ajax({
        type: "POST",
        url: "/user/note/addOrUpdate",
        data: JSON.stringify({"content":$("#note_textarea"+e).val(),"exerciseId":e,"noteId":$("#noteId").val()}),
        contentType:"application/json;charset=utf-8",
        dataType: "json",
        success:function (data) {
            if (data.code == 200){
                openExerciseNote(e);
            } else {
                alert(data.message);
            }
        }
    });
}

function changeCollectIcon(e) {
    $.ajax({
        url:"/user/collect/addCollect",
        contentType:"text",
        dataType:"json",
        data:{"exerciseId":e},
        success:function(data){
            if (data.code == 200){
                $("#collectActiveIcon" +e).css("display","block");
                $("#collectIcon" + e).css("display","none");
            }else {
                alert(data.message);
            }
        }
    })
}

function changeCollectActiveIcon(e) {
    $.ajax({
        url:"/user/collect/deleteCollect",
        type:"post",
        dataType:"json",
        data:{"exerciseId":e},
        success:function(data){
            if (data.code == 200){
                $("#collectActiveIcon" +e).css("display","none");
                $("#collectIcon" + e).css("display","block");
            }else {
                alert(data.message);
            }

        }
    });
}

function personInformation(msg){
    $.ajax({
        url:"/user/users/information",
        contentType:"application/json",
        success:function (data){
            /*$("#userId").val(data.userId);
            $("#username").val(data.username);
            $("#avatarImgUrl").attr("src",data.avatarImgUrl);
            $("#phone").val(data.phone);
            $("#description").val(data.description);
            $("#nickname").val(data.nickname);
            $("#sex").val(data.sex);*/
            $("#showUserId").val(data.userId);
            $("#showUsername").html("账号："+data.username);
            $("#showNickname").html("昵称："+data.nickname);
            $("#showAvatarImgUrl").attr("src",data.avatarImgUrl);
            $("#showPhone").html("手机号："+data.phone);
            $("#showDescription").html("自我介绍："+data.description);
            $("#showSex").html("性别："+data.sex);
            showInfo();
        }
    });
    if (msg != null && msg != ''){
        alert(msg);
    }
}


function modifyInfo(){
    $("#userId").val( $("#showUserId").val());
    $("#username").val($("#showUsername").html().substring(3));
    $("#avatarImgUrl").attr("src",$("#showAvatarImgUrl").attr("src"));
    $("#phone").val($("#showPhone").html().substring(4));
    $("#description").val($("#showDescription").html().substring(5));
    $("#nickname").val($("#showNickname").html().substring(3));
    $("#sex").val($("#showSex").html().substring(3));

    $("#showInfo").css("display","none");
    $("#modifyInfo").css("display","block");

}

function showInfo(){
    $("#fileinputA").click();
    $("#showInfo").css("display","block");
    $("#modifyInfo").css("display","none");
}

function historyAnswer(e){
    $.ajax({
        url:"/user/answer/findAnswer",
        dataType:"json",
        data:{"currentPage":e},
        success:function(data){
            var content = "";
            var list = data.dataList;
            for (var i=0;i<list.length;i++){
                content += "<li class='product has-post-thumbnail' style='width: 100%'>" +
                    "<div class='content'>" +
                    "<div class='history-div-55'>" +
                    "<h3 class='history-left'>"+ list[i].exercise.exerciseTitle +"</h3>" +
                    "<h3 class='history-right'>"+ list[i].exercise.exerciseType +"</h3>" +
                    "<h3 class='history-right' style='padding-right:20px;'>"+ list[i].subject.baseSubject+ "»" + list[i].subject.name +"</h3>" +
                    "</div>" +
                    "<div class='history-div-50'>" +
                    "<p class='history-right'>答题时间：<span>"+ Format(new Date(list[i].createTime),"yyyy-MM-dd hh:mm:ss") +"</span></p>" +
                    "</div>" +
                    "<a target='_blank' href='/user/answer/viewHistoryExercise?answerId="+ list[i].answerId +"' class='button-01 history-right'>查看</a>" +
                    "</div>" +
                    "</li>";
            }
            $("#history_ul").html(content);
            if(data.totalPage !=0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    $("#nav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='historyAnswer(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    $("#nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='historyAnswer(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li   class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                    $("#nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='historyAnswer(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='historyAnswer(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else {
                    $("#nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                }
            }else {
                $("#nav").html("");
            }
        }
    });
}

function permissionList(currentPage){
    $.ajax({
        url: '/user/permission/permissionList',
        type: 'post',
        data:{currentPage:currentPage},
        dataType: "json",
        success: function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0;i<list.length;i++){
                content += "<li>" +
                "<p class='permission-content'><span class='fa fa-caret-right'></span>"+ list[i].content +"</p>" +
                "<a href='javascript:void(0)' onclick='showPermission("+ list[i].messageId +")'>查看</a></li>"
            }
            $("#permission_ul").html(content);

            if(data.totalPage !=0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    $("#permission_nav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='permissionList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    $("#permission_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='permissionList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                    $("#permission_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='permissionList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='permissionList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else {
                    $("#permission_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                }
            }else {
                $("#permission_nav").html("");
            }
        }
    });
}

function submitAddPermissionFrom() {
    var content = $("#addPermissionFrom textarea").val();
    if (content == ""){
        alert("内容不能为空");
        return;
    }
    $.ajax({
        url:"/user/permission/add",
        type:"post",
        data:{"content":content},
        dataType:"json",
        success:function(data){
            if (data.code == 200){
                $('#addPermission').modal('hide');
                alert(data.message);
                permissionList();
            }else {
                alert(data.message);
            }

        }
    });
}

function collectList(e){
    $.ajax({
        url:"/user/collect/findCollectList",
        dataType:"json",
        data:{"currentPage":e},
        success:function(data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0; i < list.length; i++) {
                content += "<li class='product has-post-thumbnail' style='width: 100%'>" +
                    "<div class='content'>" +
                    "<div class='history-div-55'>" +
                    "<h3 class='history-left'>" + list[i].exercise.exerciseTitle + "</h3>" +
                    "<h3 class='history-right'>" + list[i].exercise.exerciseType + "</h3>" +
                    "<h3 class='history-right' style='padding-right:20px;'>" + list[i].subject.baseSubject + "»" + list[i].subject.name + "</h3>" +
                    "</div>" +
                    "<div class='history-div-50'>" +
                    "<p class='history-right'>收藏时间：<span>" + Format(new Date(list[i].collect.createTime), "yyyy-MM-dd hh:mm:ss") + "</span></p>" +
                    "</div>" +
                    "<a target='_blank' href='/user/exercise/exerciseToAnswer?exerciseId="+list[i].exercise.exerciseId+"' class='button-01 history-right'>查看</a>" +
                    "</div>" +
                    "</li>";
            }
            $("#collect_ul").html(content);

            if (data.totalPage != 0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    $("#collect_nav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='collectList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    $("#collect_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='collectList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li   class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                    $("#collect_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='collectList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='collectList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else {
                    $("#collect_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                }
            }
        }
    });
}


function noteList(e){
    $.ajax({
        url:"/user/note/findNoteList",
        type:"post",
        data:{"currentPage":e},
        dataType:"json",
        success:function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0;i<list.length;i++){
                content += "<article class='entry-item'>" +
                    "<div class='entry-content'>" +
                    "<h4 class='entry-title'>" +
                    "<a target='_blank' href='/user/answer/viewNoteExercise?noteId="+ list[i].noteId +"'>"+ list[i].content +"</a>" +
                    "</h4>" +
                    "<div class='entry-meta'>" +
                    "<span>"+ Format(list[i].modifyTime,'yyyy-MM-dd hh:mm:ss') +"</span>" +
                    "</div>" +
                    "</div>" +
                    "</article>"
            }
            $("#note").html(content);

            if(data.totalPage !=0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    $("#note_nav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='noteList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    $("#note_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='noteList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li   class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                    $("#note_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='noteList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='noteList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else {
                    $("#note_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                }
            }else {
                $("#note_nav").html("");
            }
        }

    });
}


function changePasswdType() {
    if($("input[name='password']").attr("type")=="text"){
        $("input[name='password']").attr("type","password");
    }else {
        $("input[name='password']").attr("type","text");
    }
}

function addExerciseForForm(){
    $("#addExerciseList").css("display","none");
    $("#addExercise").css("display","block");
    $("#AddExerciseXLS").css("display","none");
    $("#AddExerciseForm").css("display","block");

    $.ajax({
        url: '/subject/base',
        type: 'post',
        contentType: "application/json",
        success: function (data) {
            if (data != null) {
                $('#base').html("");
                for (var i = 0; i < data.length; i++) {
                    $('#base').append("<option>" + data[i] + "</option>");
                }
                showSubject(data[0]);
            }
        }
    });
}

function addExerciseSetForForm(){
    $("#addExerciseSetList").css("display","none");
    $("#addExerciseSet").css("display","block");

    $.ajax({
        url: '/subject/base',
        type: 'post',
        contentType: "application/json",
        success: function (data) {
            if (data != null) {
                $('#exerciseBase').html("");
                for (var i = 0; i < data.length; i++) {
                    $('#exerciseBase').append("<option>" + data[i] + "</option>");
                }
                showSubjectForUser(data[0]);
            }
        }
    });
}

function addExerciseForXLS(){
    $("#addExerciseList").css("display","none");
    $("#addExercise").css("display","block");
    $("#AddExerciseXLS").css("display","block");
    $("#AddExerciseForm").css("display","none");
}

function toAddExerciseList(){
    $("#addExerciseList").css("display","block");
    $("#addExercise").css("display","none");
    /*$("#exerciseXLSForm")[0].reset();
    $("#exerciseForm")[0].reset();*/
    /*$("#exerciseForm input").each(function(){console.log($(this).val(''))});
    $("#exerciseXLSForm input").each(function(){console.log($(this).val(''))});*/
    $("#exerciseForm option:selected").each(function(){$(this).prop("selected",false)});
    $("#exerciseForm select").each(function(){$(this).removeAttr("disabled")});
    $("#exerciseForm input[name!='correct']").each(function(){
        $(this).val("");
    });
    $("#exerciseForm input[name='correct']").each(function(){
        $(this).prop("checked",false);
    });
    $("#exerciseForm textarea").each(function(){
        $(this).val("");
    });
    $("#exerciseForm input[type=checkbox]:checked").each(function(){
        $(this).attr("type","radio");
        $(this).prop("checked",false);
    });
}

function toExerciseSetList(){
    $("#addExerciseSetList").css("display","block");
    $("#addExerciseSet").css("display","none");

    $("#exerciseSetForm option:selected").each(function(){$(this).prop("selected",false)});
    $("#exerciseSetForm input").each(function(){
        $(this).val("");
    });
    $("#exercise-tbody").html("");
    $("#exercise-div").css("display","none");
}

function AddExerciseList(page){
    var status = $("#status").val();
    $.ajax({
        url: '/user/exercise/getReviewExerciseByUserId',
        type: 'post',
        data:{currentPage:page,status:status},
        dataType: "json",
        success: function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0;i<list.length;i++){
                content += "<li>" +
                    "<p class='permission-content'><span class='fa fa-caret-right'></span>"+ list[i].exerciseTitle +"</p>" +
                    "<a href='javascript:void(0)' onclick='showAddExercise("+ list[i].exerciseId +")'>查看</a>" +
                    "<a href='javascript:void(0)' style='position: absolute;right: 125px;' onclick='deleteAddExercise("+ list[i].exerciseId +")'>删除</a></li>"
            }
            $("#add_exercise_ul").html(content);
            $("#status").val(data.type == 'null'?'':data.type);
            if(data.totalPage !=0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    $("#add_exercise_nav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='AddExerciseList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    $("#add_exercise_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='AddExerciseList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li   class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                    $("#add_exercise_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='AddExerciseList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='AddExerciseList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else {
                    $("#add_exercise_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                }
            }else {
                $("#add_exercise_nav").html("");
            }
            toAddExerciseList();
        }
    })
}

function modifyExercise(id){
    $("#addExerciseList").css("display","none");
    $("#addExercise").css("display","block");
    $("#AddExerciseXLS").css("display","none");
    $("#AddExerciseForm").css("display","block");
    $.ajax({
       url:"/user/exercise/toUpdate",
       data:{exerciseId:id},
       dataType:"json",
       success:function (data) {
           var content = '';
           var list = data.baseList;
           for (var i = 0; i < list.length; i++){
               if (data.subjectList[0].baseSubject== list[i]) {
                   content += "<option selected='true'>" + list[i] + "</option>";
               }else {
                   content += "<option>" + list[i] + "</option>";
               }
           }
           $("#base").html(content);
           $("#base").prop("disabled",true);
           content = '';
           list = data.subjectList;
           for (var i = 0; i < list.length; i++){
               if (list[i].subjectId == data.subjectId) {
                   content += "<option value='"+ list[i].subjectId +"' selected='true'>" + list[i].name + "</option>";
               }else{
                   content += "<option value='"+ list[i].subjectId+ "'>" + list[i].name + "</option>";
               }
           }
           $("#subjectId").html(content);
           $("#subjectId").prop("disabled",true);

           $("#type option").each(function(){
               if($(this).html()==data.exerciseType){
                   $(this).prop("selected",true);
               }
           });
           changeCorrect(data.exerciseType);

            $("#title").val(data.title);

           content = '';
           list = data.options;
           for (var i = 0; i < list.length; i++){
               content += "<div class='option-div' id='"+ list[i].option +"'>" +
                   "<label class='control-label' style='width: 5%;'>"+ list[i].option +"：</label>" +
                   "<input type='text' class='form-control' name='answers' value='"+ list[i].content +"'"+
                   "required=''>" +
                   "<button onclick='deleteOption()' type='button' class='btn btn-danger option-button";
                    if ((i+1) != list.length){
                        content += ' disabled';
                    }
                    content += "'>" +
                   "删除" +
                   "</button>" +
                   "</div>";
           }
           $("#opts").html(content);

           content = '';
           list = data.options;
           if (data.exerciseType == '多选题'){
               for (var i = 0; i < list.length; i++){
                   content += "<label class='correct-label'>" +
                       "<input type='checkbox' name='correct' " +
                       " value='"+ list[i].option +"' ";
                   if (data.correct.toUpperCase().indexOf(list[i].option.toUpperCase()) >= 0){
                       content += " checked = 'true'";
                   }
                   content += ">"+ list[i].option + "</label>";
               }
           } else {
               for (var i = 0; i < list.length; i++){
                   content += "<label class='correct-label'>" +
                       "<input type='radio' name='correct' required='required'" +
                       " value='"+ list[i].option +"' ";
                    if (data.correct.toUpperCase().indexOf(list[i].option.toUpperCase()) >= 0){
                        content += " checked = 'true'";
                    }
                    content += ">"+ list[i].option + "</label>";
               }
           }
           $("#correct").html(content);

           $("#analysis").val(data.analysis);
           $("#id").val(data.exerciseEditId);
       }
    });
}

function modifyExerciseSet(e){
    $.ajax({
        url:"/user/exerciseSet/toUpdate",
        data:{exerciseSetId:e},
        dataType:"json",
        type:"post",
        success:function(data){
            addExerciseSetForForm();
            $("#exerciseSetForm option").each(function(){
                if($(this).val() == data.subject.baseSubject){
                    $(this).prop("selected",true);
                    showSubject(data.subject.baseSubject);
                    $("#exerciseSetForm option").each(function(){
                        if($(this).val() == data.subject.subjectId){
                            $(this).prop("selected",true);
                        }
                    });
                }
            });
            $("#exerciseSetTitle").val(data.title);
            var content = "" ;
            var list = data.exerciseList;
            for (var i=0; i<list.length; i++){
                content += "<tr><td><input value='"+list[i].exerciseId+"' checked type='checkbox' name='exerciseList["+i+"].exerciseId'></td>" +
                            "<td>"+list[i].exerciseId+"</td>" +
                            "<td>"+list[i].exerciseType+"</td>" +
                            "<td>"+list[i].exerciseTitle+"</td>" +
                            "<td><button type='button' onclick='showExerciseForExerciseSet("+list[i].exerciseId+")'  class='btn btn-primary'> 查看 </button></td>"+
                            "<td><button type='button' onclick='deleteExerciseTbody("+list[i].exerciseId+")'  class='btn btn-primary'> 删除 </button></td></tr>";
            }
            $("#exercise-tbody").html(content);
            $("#exerciseSetId").val(data.exerciseSetId);
            $("#exercise-div").css("display","block");
        }
    })
}

function deleteAddExercise(id){
    $.ajax({
        url:"/user/exercise/delete",
        data:{exerciseId:id},
        dataType:"json",
        success:function(data){
            if (data.code == 200){
                AddExerciseList(1);
                alert(data.message);
            }else {
                alert(data.message);
            }
        }
    });
}

function submitAddExercise(){

    if ($("#title").val() == ""){
        alert("题目不能为空");
        return;
    }

    var flag = false;
    $("#exerciseForm input[name='answers']").each(function(){
        if ($(this).val() == ""){
            flag = true;
        }
    });
    if (flag){
        alert("选项不能为空");
        return;
    }

    flag = false;
    $("#exerciseForm input[name='correct']:checked").each(function(){
        flag = true;
    });
    if (!flag){
        alert("请选择正确答案");
        return;
    }

    $.ajax({
        url:"/user/exercise/addOrUpdate",
        type:"post",
        dataType:"json",
        data:$("#exerciseForm").serialize(),
        success:function(data){
            if(data.code == 200){
                alert(data.message);
                AddExerciseList(1);
            }else {
                alert(data.message);
            }
        }
    });

}

function AddExerciseSetList(page){
    $.ajax({
        url: '/user/exerciseSet/getExerciseSetByUserId',
        type: 'post',
        data:{currentPage:page},
        dataType: "json",
        success: function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0;i<list.length;i++){
                content += "<li>" +
                    "<div style='width: 85%;float: left;'><p class='permission-content' style='float: left;width: 80%;'>"+
                    "<span class='fa fa-caret-right'></span>"+ list[i].title +"</p><p class='permission-content' style='width: 20%;margin-top: 6px;'>"+Format(list[i].createTime,"yyyy-MM-dd hh:mm:ss")+"</p></div>"+
                    "<a href='javascript:void(0)' onclick='deleteExerciseSetForUser("+ list[i].exerciseSetId +")'>删除</a>"+
                    "<a href='javascript:void(0)' onclick='showAddExerciseSet("+ list[i].exerciseSetId +",1)'>查看</a>";
            }
            $("#add_exercise_set_ul").html(content);


            if(data.totalPage !=0) {
                if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                    $("#add_exercise_set_nav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li  class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='AddExerciseSetList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                    $("#add_exercise_set_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='AddExerciseSetList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li   class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                    $("#add_exercise_set_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li>" +
                        "<a onclick='AddExerciseSetList(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a onclick='AddExerciseSetList(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                } else {
                    $("#add_exercise_set_nav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                        "<ul class='pagination pagination-lg'>" +
                        "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Previous'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a>" +
                        "</li>" +
                        "<li class='disabled'>" +
                        "<a href='javascript:void(0)' aria-label='Next'>" +
                        " <span aria-hidden='true'>下一页</span>" +
                        "</a>" +
                        "</li>" +
                        "</ul>" +
                        "</nav>")
                }
            }else {
                $("#add_exercise_set_nav").html("");
            }
            toExerciseSetList();
        }
    })
}

function like(page){
    $.ajax({
        url: '/user/notification/getLikeList',
        type: 'post',
        data: {currentPage: page},
        dataType: "json",
        success: function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0; i < list.length; i++) {
                if (list[i].type == 1 && list[i].notifier.userId != list[i].receiver.userId) {
                    content += "<article class='entry-item'>" +
                        "<div class='entry-content'>" +
                        "<h4 class='entry-title'>" +
                        "<a style='display: block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'  href='/user/notification/view/"+list[i].notificationId+"'>" +
                        list[i].notifier.nickname+ "<span style='font-size: 0.9em;color: #a3a3a3;'> 赞了我的评论 </span>" + list[i].title +"</a>" +
                        "</h4>" +
                        "<div class='entry-meta'>" +
                        "<span>" + Format(list[i].createTime, 'yyyy-MM-dd hh:mm:ss') + "</span>" +
                        "</div>" +
                        "</div>" +
                        "</article>";
                }
                $("#like").html(content);


                if (data.totalPage != 0) {
                    if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                        $("#likeNav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li  class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                        $("#likeNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li   class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                        $("#likeNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else {
                        $("#likeNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    }
                } else {
                    $("#likeNav").html("");
                }
            }
            updateNotificationNum();
            allNotification()
        }
    });
}

function reply(page){
    $.ajax({
        url: '/user/notification/getReplyList',
        type: 'post',
        data: {currentPage: page},
        dataType: "json",
        success: function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0; i < list.length; i++) {
                if (list[i].type == 2 && list[i].notifier.userId != list[i].receiver.userId) {
                    content += "<article class='entry-item'>" +
                        "<div class='entry-content'>" +
                        "<h4 class='entry-title'>" +
                        "<a target='_blank' style='display: block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;' href='/user/notification/view/"+list[i].notificationId+"'>" +
                        list[i].notifier.nickname + "<span style='font-size: 0.9em;color: #a3a3a3;'> 回复了我的评论 </span>" + list[i].title + "</a>" +
                        "</h4>" +
                        "<div class='entry-meta'>" +
                        "<span>" + Format(list[i].createTime, 'yyyy-MM-dd hh:mm:ss') + "</span>" +
                        "</div>" +
                        "</div>" +
                        "</article>";
                }
                $("#reply").html(content);


                if (data.totalPage != 0) {
                    if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                        $("#replyNav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li  class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                        $("#replyNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li   class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                        $("#replyNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else {
                        $("#replyNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    }
                } else {
                    $("#replyNav").html("");
                }
            }
            updateNotificationNum();
            allNotification()
        }
    });
}

function updateNotificationNum(){
    $.ajax({
        url:"/user/notification/updateNotification",
        dataType:"json",
        success:function(data){
            $("#replyNum").html(data.replyNum==0?'':data.replyNum);
            $("#likeNum").html(data.likeNum==0?'':data.likeNum);
            $("#systemNum").html(data.systemNum==0?'':data.systemNum);
            $("#chatNum").html(data.chatNum==0?'':data.chatNum);
        }
    });
}

function system(page){
    $.ajax({
        url: '/user/notification/getSystemList',
        type: 'post',
        data: {currentPage: page},
        dataType: "json",
        success: function (data) {
            var content = "";
            var list = data.dataList;
            for (var i = 0; i < list.length; i++) {
                if (list[i].type == 3) {
                    content += "<article class='entry-item'>" +
                        "<div class='entry-content'>" +
                        "<h4 class='entry-title'>" +
                        "<span>" +
                        "尊敬的用户：你提交的试题申请 " + list[i].title;
                    if (list[i].message != null){
                       if (list[i].message.status == 1){
                           content += " 审核通过";
                       } else {
                           content += " 审核未通过，原因："+list[i].message.reason;
                       }
                    }
                    content += "</span>" +
                        "</h4>" +
                        "<div class='entry-meta'>" +
                        "<span>" + Format(list[i].createTime, 'yyyy-MM-dd hh:mm:ss') + "</span>" +
                        "</div>" +
                        "</div>" +
                        "</article>";
                }else if(list[i].type == 4){
                    content += "<article class='entry-item'>" +
                        "<div class='entry-content'>" +
                        "<h4 class='entry-title'>" +
                        "<span href='javascript:void(0)'>" +
                        "尊敬的用户：你提交的权限申请 " + list[i].title;
                        if (list[i].message != null){
                            if (list[i].message.status == 1){
                                content += " 审核通过";
                            } else {
                                content += " 审核未通过，原因："+list[i].message.reason;
                            }
                        }
                        content += "</span>" +
                        "</h4>" +
                        "<div class='entry-meta'>" +
                        "<span>" + Format(list[i].createTime, 'yyyy-MM-dd hh:mm:ss') + "</span>" +
                        "</div>" +
                        "</div>" +
                        "</article>";
                }else {
                    content += "<article class='entry-item'>" +
                        "<div class='entry-content'>" +
                        "<h4 class='entry-title'>" +
                        "<span href='javascript:void(0)'>" +
                        "公告： "+list[i].title+
                        "</span>" +
                        "</h4>" +
                        "<div class='entry-meta'>" +
                        "<span>" + Format(list[i].createTime, 'yyyy-MM-dd hh:mm:ss') + "</span>" +
                        "</div>" +
                        "</div>" +
                        "</article>";
                }
                $("#system").html(content);


                if (data.totalPage != 0) {
                    if (data.currentPage == 1 && data.currentPage != data.totalPage) {
                        $("#systemNav").html("<nav aria-label='Page navigation' style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li  class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else if (data.currentPage == data.totalPage && data.currentPage != 1) {
                        $("#systemNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li   class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else if (data.currentPage != data.totalPage && data.currentPage != 1) {
                        $("#systemNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage - 1) + ")' href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li>" +
                            "<a onclick='like(" + (data.currentPage + 1) + ")' href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    } else {
                        $("#systemNav").html("<nav aria-label='Page navigation'  style='text-align: center;'>" +
                            "<ul class='pagination pagination-lg'>" +
                            "<li><span>" + data.currentPage + "/" + data.totalPage + "</span></li>" +
                            "<li class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Previous'>" +
                            "<span aria-hidden='true'>上一页</span>" +
                            "</a>" +
                            "</li>" +
                            "<li class='disabled'>" +
                            "<a href='javascript:void(0)' aria-label='Next'>" +
                            " <span aria-hidden='true'>下一页</span>" +
                            "</a>" +
                            "</li>" +
                            "</ul>" +
                            "</nav>")
                    }
                } else {
                    $("#systemNav").html("");
                }
            }
            updateNotificationNum();
            allNotification()
        }
    });
}

function chatList(){
    $.ajax({
        cache:false,
        url:"/user/notification/chat",
        async:false,
        dataType:"json",
        success:function(data){
            var content = "";
            var user;
            $.ajax({
                url:"/user/users/information",
                dataType:"json",
                async:false,
                success:function(result){
                    if (result != null){
                        user = result;
                    }
                }
            });
            data.forEach(function (element, sameElement, set) {
                if (user != null) {
                    if (element.receiver.userId == user.userId){
                        content += "<li style='cursor: pointer;background-color: #ffffff;' name='"+element.notifier.userId+"' onclick='showChat(" + element.notifier.userId + ",this)'>" +
                            "<article style='margin-top: 20px'>" +
                            "<div class='comment-avatar '>" +
                            "<img src='" + element.notifier.avatarImgUrl + "' style='width: 40px;height: 40px;'>" +
                            "</div>" +
                            "<div class='comment-content' style='width: 80%;float: right; margin-top: -15%;'>" +
                            "<header>" +
                            "<div class='entry-meta '>" +
                            "<div> " + element.notifier.nickname + " </div>" +
                            "<div style='font-size: 0.9em;margin-top: 10px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>" + element.title + "</div>" +
                            "</div>" +
                            "</header>" +
                            "</div>" +
                            "</article>" +
                            "</li>";
                    }else {
                        content += "<li style='cursor: pointer;background-color: #ffffff;' name='"+element.receiver.userId+"' onclick='showChat(" + element.receiver.userId + ",this)'>" +
                            "<article style='margin-top: 20px'>" +
                            "<div class='comment-avatar '>" +
                            "<img src='" + element.receiver.avatarImgUrl + "' style='width: 40px;height: 40px;'>" +
                            "</div>" +
                            "<div class='comment-content' style='width: 80%;float: right; margin-top: -15%;'>" +
                            "<header>" +
                            "<div class='entry-meta '>" +
                            "<div> " + element.receiver.nickname + " </div>" +
                            "<div style='font-size: 0.9em;margin-top: 10px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>" + element.title + "</div>" +
                            "</div>" +
                            "</header>" +
                            "</div>" +
                            "</article>" +
                            "</li>";
                    }
                }
            });
            $("#chatUl").html(content);
            $("#chatDiv").html("");
            $("#chatTitle").html("");
            updateNotificationNum();
            allNotification()
        }
    });
}

function showChat(id,e){
    $("#chatUl li").each(function(){
        $(this).css("background-color","#ffffff");
    });
    $(e).css("background-color","#fafafa");

    $.ajax({
        url:"/user/notification/showChat",
        data:{secondId:id},
        type:"post",
        cache:false,
        async:false,
        dataType:"json",
        success:function(data){
            var content = "";
            var user;
            $.ajax({
                url:"/user/users/information",
                dataType:"json",
                async:false,
                cache:false,
                success:function(result){
                    if (result != null){
                        user = result;
                    }
                }
            });
            for (var i = 0;i<data.length;i++){
                if (i != 0 && new Date(data[i].createTime).getTime() - new Date(data[i-1].createTime).getTime()>1000*60*5){
                        content += "<div style='text-align: center;margin: 15px;'>"+Format(data[i].createTime, 'yyyy-MM-dd hh:mm:ss')+"</div>";
                }
                if(i == 0) {
                    content += "<div style='text-align: center;margin: 15px;'>"+Format(data[i].createTime, 'yyyy-MM-dd hh:mm:ss')+"</div>";
                }
                if (data[i].notifier.userId == user.userId){
                    content += "<div class='user-group'>" +
                        "<div class='user-msg'>" +
                        "<pre class='user-reply'>"+data[i].title+"</pre>" +
                        "<i class='triangle-user'></i>" +
                        "</div>" +
                        "<img class='user-img' src='"+data[i].notifier.avatarImgUrl+"'>" +
                        "</div>";
                    $("#chatTitle").html(data[i].receiver.nickname);
                    $("#to").val(data[i].receiver.username);
                }else {
                    content += "<div class='admin-group'>" +
                        "<img class='admin-img' src='"+data[i].notifier.avatarImgUrl+"'>" +
                        "<div class='admin-msg'>" +
                        "<i class='triangle-admin'></i>" +
                        "<pre class='admin-reply'>"+data[i].title+"</pre>" +
                        "</div>" +
                        "</div>";
                    $("#chatTitle").html(data[i].notifier.nickname);
                    $("#to").val(data[i].notifier.username);
                }
            }
            $("#chatDiv").html(content);
            $('#chatDiv').scrollTop( $('#chatDiv')[0].scrollHeight);
        }
    });
}

function viewNotification(id){
    $.ajax({
        url:"/user/notification/viewNotification",
        data:{notificationId:id},
        dataType:"json",
        success:function(data){
            if (data.code == ''){

            } else if(data.code){

            }else {

            }
        }
    });
}

function submitScore(id){
    var score = $("#score"+id).rating().val();
    $.ajax({
        url:"/user/evaluation/addOrUpdate",
        type:'post',
        data:{score:score,exerciseId:id},
        dataType:"json",
        success:function(data){
            if (data.code == 200){
                alert(data.message);
            }else {
                alert(data.message);
            }
        }
    });
}

function submitAddExerciseSet(){
    if($("#exerciseSetTitle").val() == ""){
        alert("标题不能为空");
        return;
    }
    if ($('#exercise-tbody').children().length == 0){
        alert("最少选择一道题目");
        return;
    }

    $.ajax({
        url:"/user/exerciseSet/addOrUpdate",
        type:"post",
        dataType:"json",
        data:$("#exerciseSetForm").serialize(),
        success:function (data){
            if(data.code == 200){
                AddExerciseSetList(1);
                alert(data.message);
            }else {
                alert(data.message);
            }
        }
    });
}

function addExerciseSetLike(e){
    $.ajax({
        url:"/user/exerciseSet/addLike",
        data:{exerciseSetId:e},
        success:function(data){
            $("#likeSpan").html("<a class='fa fa-thumbs-o-up' href='javascript:void(0)' style='color:#3498db;' onclick='delExerciseSetLike("+ e +");'><span>（"+data.likeCount+"） </span></a>");
        }
    });
}

function delExerciseSetLike(e){
    $.ajax({
        url:"/user/exerciseSet/delLike",
        data:{exerciseSetId:e},
        success:function(data){
            $("#likeSpan").html("<a class='fa fa-thumbs-o-up' href='javascript:void(0)' onclick='addExerciseSetLike("+ e +");'><span>（"+data.likeCount+"）</span></a>");
        }
    });
}

function verificationSubject(){
    var name = $("#name").val();
    var base = $("#base option:checked").val();
    var reg = /^[0-9]+.?[0-9]*$/;
    if (base == ""){
        alert("学科不能为空");
        return;
    }
    if (base.length > 50){
        alert("学科长度不能大于50字符");
        return;
    }
    if (!reg.test(base)) {
        alert("学科不能为纯数字");
        return;
    }
    if (name == ""){
        alert("试题分类不能为空");
        return;
    }
    if (name.length > 50){
        alert("试题分类长度不能大于50字符");
        return;
    }
    if (!reg.test(name)) {
        alert("试题分类不能为纯数字");
        return;
    }
}

function allNotification(){
    $.ajax({
        url: "/user/notification/allNotification",
        type: "post",
        success(data) {
            if (data == 0){
                $("span[name='num']").html("");
            }else if (data > 99){
                $("span[name='num']").html("99+");
            } else {
                $("span[name='num']").html(data);
            }
        }
    });
}

window.setInterval(allNotification, 1000*60);