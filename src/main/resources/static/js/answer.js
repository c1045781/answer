function changeCorrect(e) {
    console.log(e)
    if (e == "单选题" || e == "判断题") {
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'radio');
        })
    } else {
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type', 'checkbox');
            $(this).find(":input").removeAttr('required');
        })
    }

}

function showSubject(e) {
    $.ajax({
        url: '/subject/subjectByBase',
        type: 'post',
        data: JSON.stringify({baseSubject: e}),
        contentType: "application/json",
        success: function (data) {
            if (data != null) {
                $('#subjectId').html("");
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
            subjectId: $("#subject").val(), type: $("#type option:selected").val()
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