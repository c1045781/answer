function deleteUserModal(id) {
    $('#deleteId').val(id);
    $('#deleteModal').modal();
}

function deleteExercisesModal(id) {
    $('#deleteId').val(id);
    $('#deleteExercisesModal').modal();
}

function updateUserModal(id) {
    window.location.href = "/user/toUpdate?id=" + id;
}

function updateExercisesModal(id) {
    window.location.href = "/exercises/toUpdate?id=" + id;
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

function deleteExercises() {
    var id = $('#deleteId').val();
    console.log(id)
    $.ajax({
        url: '/exercises/delete',
        type: 'post',
        data: JSON.stringify({id: id}),
        contentType: "application/json",
        success: function () {
            $('#deleteModal').modal('hide');
            window.location.href = "/exercises/check";
        }
    });
}


/*$('#myModal').on('shown.bs.modal', function () {
    $('#myInput').focus()
})*/

var input = $("#correct").children(":last-child").find(":input").val().charCodeAt();

window.onload = function () {
    var type = $("#type").find("option:selected").text();
    if (type == "单选题" || type == "判断题") {
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type','radio');
            $(this).find(":input").attr('required','');
        })
    } else {
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type','checkbox');
            $(this).find(":input").removeAttr('required');
        })
    }

    var modelInput = $("#modelInput").val();
    if (modelInput != null && modelInput != "") {
        return
    }
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

function changeCorrect(e) {
    if (e == "单选题" || e == "判断题") {
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type','radio');
        })
    } else {
        $("#correct > label").each(function () {
            $(this).find(":input").attr('type','checkbox');
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
                console.log(data);
                $('#subject').html("");
                for (var i = 0; i < data.length; i++) {
                    $('#subject').append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
                }
            }
        }
    });
}

function addOption() {
    console.log(input)
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
    console.log(A);
    A.children(':last-child').removeClass("disabled");
    $("#opts").children(":last-child").remove();
    $("#correct").children(":last-child").remove();
    input = input - 1;
}
