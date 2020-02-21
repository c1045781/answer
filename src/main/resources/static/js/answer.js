function changeCorrect(e) {
    if (e == "单选题") {
        $('#correct_button').attr("disabled",false);
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'radio');
        })
    } else if (e == "判断题") {
        for (var i = 1; i < 100; i++) {
            deleteOption()
        }
        $('#correct_button').attr("disabled",true);
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'radio');
        })

    }else{
        $('#correct_button').attr("disabled",false);
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
            if (data != null) {
                $('#subjectId').html(content);
                for (var i = 0; i < data.length; i++) {
                    $('#subjectId').append("<option value='" + data[i].subjectId + "'>" + data[i].name + "</option>");
                }
            }
        }
    });
}

function addOption() {
    var A = $("#" + String.fromCharCode(input));
    A.children(':last-child').addClass("disabled");
    $("#opts").append("<div class=\"option-div\" id='" + String.fromCharCode(input + 1) + "'>\n" +
        "<label class=\"control-label\" style=\"width: 5%;\"> " + String.fromCharCode(input + 1) + "：</label>\n" +
        "<input type=\"text\" class=\"form-control\" name=\"answers\" required='' >\n" +
        "<button onclick=\"deleteOption()\" type=\"button\" class=\"btn btn-danger option-button\" >删除</button>" +
        "</div>\n");
    $("#correct").append("<label class=\"correct-label\"><input type=\"radio\" name=\"correct\" required=''  value=\"" + String.fromCharCode(input + 1) + "\">" + String.fromCharCode(input + 1) + "</label>");
    input = input + 1;
}

function deleteOption() {
    if (input < 67) return;
    var A = $("#" + String.fromCharCode(input - 1));
    A.children(':last-child').removeClass("disabled");
    $("#opts").children(":last-child").remove();
    $("#correct").children(":last-child").remove();
    input = input - 1;
}

function addExercise(e) {
    if (e == 'button') {
        $("#page").val('');
        $("#totalPage").val('');
    }
    $.ajax({
        url: '/exercise/getExerciseBySubjectId',
        type: 'post',
        data: {
            subjectId: $("#subjectId").val(), type: $("#type option:selected").val()
            , search: $("#search").val(), currentPage: $("#page").val()
        },
        dataType: 'json',
        success: function (data) {
            if (data != null) {
                var list = data.dataList;
                $("#table-tbody").html("");
                for (var i = 0; i < list.length; i++) {
                    $("#table-tbody").append("<tr id='" + list[i].exercise.exerciseId + "'><td><input type='checkbox' value='" + list[i].exercise.exerciseId + "'></td>\n" +
                        "<td>" + list[i].exercise.exerciseId + "</td>\n" +
                        "<td>" + list[i].exercise.exerciseType + "</td>\n" +
                        "<td>" + list[i].exercise.exerciseTitle + "</td></tr>");
                }
                if (data.totalPage == 0) {
                    $("#page").val(data.totalPage);
                } else {
                    $("#page").val(data.currentPage);
                }
                $("#totalPage").val(data.totalPage);

                $("#pageA").html($("#page").val() + "/" + $("#totalPage").val());
                $("#search").val(data.search);
                $.each($("#type").children(), function () {
                    if ($(this).val() == data.type) {
                        $(this).prop('selected', 'true');
                    }
                })
            }
        }
    });
    $("#addExercise").modal();
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
    $('#addExercise').modal('hide');
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
    if (isNaN(skip) || skip == null || skip < 1){
        skip = 1;
    }
    if (skip > total) {
        skip = total;
    }
    window.location.href = "/user/check?currentPage=" + skip+"&role="+ role +"&type="+ type+"&search="+search;
}

function submitExerciseFrom(e){
    var skip = parseInt(e);
    var subjectId = parseInt($("#subjectId").val());
    var type = $("#type").val();
    var exerciseType = $("#exerciseType").val();
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
    window.location.href = "/exercise/check?currentPage=" + skip+"&subjectId="+ subjectId +"&type="+ type+"&search="+search+"&exerciseType="+exerciseType;
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
    window.location.href = "/exerciseSet/check?currentPage=" + skip+"&subjectId="+ subjectId +"&type="+ type+"&search="+search;
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
    window.location.href = "/subject/check?currentPage=" + skip +"&type="+ type+"&search="+search;
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
    window.location.href = "/exercise/review?currentPage=" + skip+"&subjectId="+ subjectId;
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
    window.location.href = "/comment/check?currentPage=" + skip +"&type="+ type+"&search="+search;
}


function exerciseTypeChange(e){
    var subjectId = $("#hidden_subjectId").val();
    var search = $("#hidden_search").val();
    window.location.href = "/exercise/check?subjectId="+ subjectId+"&search="+search+"&exerciseType="+e;
}

function orderbyChange(e){
    var subjectId = $("#hidden_subjectId").val();
    var search = $("#hidden_search").val();
    debugger
    window.location.href = "/exerciseSet/check?subjectId="+ subjectId+"&search="+search+"&orderby="+e;
}

function answer_Exercise(e){
    window.open( "/exercise/exerciseToAnswer?exerciseId=" + e);
}

function answer_Exercise_set(e){
    window.open("/exercise/exerciseSetToAnswer?exerciseSetId=" + e);
}

function confirm_answer(){

    $("div[name=corrent_div]").css('display','block');
    $("input[type=radio]:checked").each(function() {
        checked_name = $(this).attr('name');
        corrent = $("#"+checked_name).html();
        if(corrent.indexOf($(this).val()) >= 0){
            $("#corrent_div"+checked_name).removeClass();
            $("#corrent_div"+checked_name).addClass("alert alert-success");
            $("#isCorrect"+checked_name).html("正确");
        }else{
            $("#corrent_div"+checked_name).removeClass();
            $("#corrent_div"+checked_name).addClass("alert alert-danger");
            $("#isCorrect"+checked_name).html("错误");
        }

        $.ajax({
            url: '/answer/add',
            type: 'post',
            data: JSON.stringify({exerciseId: checked_name,answer: $(this).val()}),
            contentType: "application/json"
        });
    });

    var arr = new Array();
    $("input[type=checkbox]").each(function() {
        if($(this).attr('name') != '' && $.inArray($(this).attr('name'),arr) == '-1'){
            arr.push($(this).attr('name'));
        }
    });
    for (i in arr){
        var arr_id = arr[i];
        var corrent = $("#"+arr[i]).html();
        var corrent_count =  corrent.split('：')[1].split(',').length; // 答案个数
        var i = 0;
        var answer = '';
        $("input[name="+ arr[i] +"]:checked").each(function() {
            answer += $(this).val() + ",";
            checked_name = $(this).attr('name');
            i++;
            console.log(checked_name);
            if(corrent.indexOf($(this).val()) >= 0){
                $("#corrent_div"+checked_name).removeClass();
                $("#corrent_div"+checked_name).addClass("alert alert-success");
                $("#isCorrect"+checked_name).html("正确");
            }else{
                $("#corrent_div"+checked_name).removeClass();
                $("#corrent_div"+checked_name).addClass("alert alert-danger");
                $("#isCorrect"+checked_name).html("错误");
                return false;
            }
        });
        if ( corrent_count != i){
            $("#corrent_div"+arr_id).removeClass();
            $("#corrent_div"+arr_id).addClass("alert alert-danger");
            $("#isCorrect"+arr_id).html("错误");
        }

        $.ajax({
            url: '/answer/add',
            type: 'post',
            data: JSON.stringify({exerciseId: arr_id,answer: answer}),
            contentType: "application/json"
        });

        answer = '';
    }

    $("#a_confirm").css('display','none');

}

function open_exercise_comment(e){
    $.ajax({
        url: '/comment/getCommentDTOList',
        type: 'post',
        data: {"id":e},
        success:function(data){
            var content = "";
            for (var i=0;i<data.length;i++){
                content += '<li class="comment">' +
                    '<article>' +
                    '<div class="comment-avatar ">' +
                    '<img alt="" src="'+ data[i].user.avatarImgUrl +'">' +
                    '</div>' +
                    '<div class="comment-content">' +
                    '<header>' +
                    '<div class="entry-meta ">' +
                    '<p class="entry-author">' +
                    '<a href="#">'+ data[i].user.username +'</a>' +
                    '</p>' +
                    '<p class="entry-date">' +
                    '</p><div>'+ Format(new Date(data[i].createDate),"yyyy-MM-dd hh:mm:ss") +'</div>' +
                    '</div>' +
                    '</header>' +
                    '<p>'+ data[i].content +'</p>' +
                    '</div>' +
                    '</article>' +
                    '</li>';
            }
            $("#comment_ol"+e).html(content);
        }
    });
    $("#comment_section"+e).css('display','block');
    $("#a_close_comment"+e).css('display','block');
    $("#a_open_comment"+e).css('display','none');
}

function close_exercise_comment(e) {
    $("#a_close_comment"+e).css('display','none');
    $("#a_open_comment"+e).css('display','block');
    $("#comment_section"+e).css('display','none');
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

function submit_comment(e){
    $.ajax({
        type: "POST",
        dataType: "text",
        url: "/comment/addComment",
        data: $('#comment_form'+e).serialize(),
        success:function (data) {
            open_exercise_comment(e);
        }
    })
    $("#comment_form"+ e +" textarea").val('');
}

function changeCollectIcon(e) {
    console.log(e);
    $.ajax({
        url:"/collect/addCollect",
        contentType:"text",
        dataType:"text",
        data:{"exerciseId":e},
        success:function(data){
            $("#collectActiveIcon" +e).css("display","block");
            $("#collectIcon" + e).css("display","none");
        }
    })
}

function changeCollectActiveIcon(e) {
    console.log(e);
    $.ajax({
        url:"/collect/deleteCollect",
        contentType:"text",
        dataType:"text",
        data:{"exerciseId":e},
        success:function(data){
            $("#collectActiveIcon" +e).css("display","none");
            $("#collectIcon" + e).css("display","block");
        }
    });
}

function personInformation(){
    $.ajax({
        url:"/user/information",
        contentType:"application/json",
        success:function (data){
            $("#userId").val(data.userId);
            $("#password").val(data.password);
            $("#passwordConfirm").val(data.password);
            $("#account").val(data.account);
            $("#avatarImgUrl").attr("src",data.avatarImgUrl);
            $("#phone").val(data.phone);
            $("#description").val(data.description);
            $("#username").val(data.username);
            $("#sex").val(data.sex);
        }
    });
}