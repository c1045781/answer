function deleteUserModal(id) {
    $('#deleteId').val(id);
    $('#deleteModal').modal();
}

function deleteExerciseModal(id) {
    $('#deleteId').val(id);
    $('#deleteExerciseModal').modal();
}

function updateUserModal(id) {
    window.location.href = "/user/toUpdate?id=" + id;
}

function updateExerciseModal(id) {
    window.location.href = "/exercise/toUpdate?id=" + id;
}

function deleteUser() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/user/delete',
        type: 'post',
        data: JSON.stringify({id: id}),
        contentType: "application/json",
        success: function () {
            $('#deleteModal').modal('hide');
            window.location.href = "/user/check";
        }
    });
}

function deleteExercise() {
    var id = $('#deleteId').val();
    $.ajax({
        url: '/exercise/delete',
        type: 'post',
        data: JSON.stringify({id: id}),
        contentType: "application/json",
        success: function () {
            $('#deleteModal').modal('hide');
            window.location.href = "/exercise/check";
        }
    });
}


/*$('#myModal').on('shown.bs.modal', function () {
    $('#myInput').focus()
})*/

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
                $('#subject').html("");
                for (var i = 0; i < data.length; i++) {
                    $('#subject').append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
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
    if(e == 'button'){
        $("#page").val('');
        $("#totalPage").val('');
    }
    $.ajax({
        url: '/exercise/getExerciseBySubjectId',
        type: 'post',
        data: {subjectId: $("#subject").val(), type: $("#type option:selected").val()
            , search: $("#search").val(),currentPage:$("#page").val()},
        dataType: 'json',
        success: function (data) {
            if (data != null) {
                var list = data.dataList;
                $("#table-tbody").html("");
                for (var i = 0; i < list.length; i++) {
                    $("#table-tbody").append("<tr id='" + list[i].exercise.id + "'><td><input name='exerciseIds' type='checkbox' value='" + list[i].exercise.id + "'></td>\n" +
                        "<td>" + list[i].exercise.id + "</td>\n" +
                        "<td>" + list[i].exercise.exerciseType + "</td>\n" +
                        "<td>" + list[i].exerciseContentVM.title + "</td><td></td></tr>");
                    var vm = list[i].exerciseContentVM;
                    $.each(vm, function (n, index) {
                        if (n == 'options') {
                            $.each(index, function () {
                                $('#table-tbody').children(':last-child').children(':last-child').append($(this)[0].option + ' : ' + $(this)[0].content + '\t');
                            })
                        }
                    })
                }
                $("#page").val(data.currentPage);
                $("#totalPage").val(data.totalPage);
                $("#pageA").html($("#page").val() +"/"+ $("#totalPage").val());
                $("#search").val(data.search);
                $.each($("#type").children(),function(){
                    if($(this).val() == data.type){
                        $(this).prop('selected','true');
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

function delPage(e){
    var page = $("#page").val();
    if (parseInt(page) <= 1) {
        $("#page").val(page);
    }else{
        $("#page").val(parseInt(page)-1);
    }
    addExercise(e)
}
function addPage(e){
    var page = $("#page").val();
    var total = $("#totalPage").val();
    if (parseInt(page) < parseInt(total)) {
        $("#page").val(parseInt(page)+1);
    }else{
        $("#page").val(total);
    }
    addExercise(e)
}