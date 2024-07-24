//TODO Видалити це
$(document).ready(function () {
    //TODO Видалити це///////////////////////////////
    $("#username").val("professor@gmail.com")
    $("#password").val("professor")
    /////////////////////////////////////////////////


    if(window.location.href.includes("error")){
        $("#password-eye").css("border-color", "#ff0000")
        $("#password").css("border-color", "#ff0000")
        $("#username").css("border-color", "#ff0000")
        addText($("#password-block"), "Електрона адреса або пароль не коректні")
    }
})


function modalForRegistration(taskId) {
    if ($('#modalForRegistration').html()) $('#modalForRegistration').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modalForRegistration" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered  modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Реєстрація</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="card">
                            <div class="card-content">
                                <div class="row">
                                    <div class="col-lg-6 col-sm-12">
                                        Прізвище
                                        <input id="lastName" class="form-control">
                                    </div>
                                    <div class="col-lg-6 col-sm-12">
                                        Ступінь
                                        <input id="degree" class="form-control">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6 col-sm-12">
                                        Ім'я
                                        <input id="name" class="form-control">
                                    </div>
                                    <div class="col-lg-6 col-sm-12">
                                        telegram
                                        <input id="telegram" class="form-control">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6 col-sm-12">
                                        По батькові
                                        <input id="middleName" class="form-control">
                                    </div>
                                    <div class="col-lg-6 col-sm-12">
                                        E-mail
                                        <input id="email" class="form-control">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6 col-sm-12">
                                        Пароль
                                        <input id="password" class="form-control">
                                    </div>
                                    <div class="col-lg-6 col-sm-12">
                                        Повтор пароля
                                        <input id="passwordRepeat" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="registration()">Зареєструватись</button>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    $('#modalForRegistration').modal('show')
}

function registration(){
    showLoader("modalForRegistration")
    let formData = new FormData()
    formData.append("lastName", $("#lastName").val())
    formData.append("degree", $("#degree").val())
    formData.append("name", $("#name").val())
    formData.append("telegram", $("#telegram").val())
    formData.append("middleName", $("#middleName").val())
    formData.append("email", $("#email").val())
    formData.append("password", $("#password").val())
    formData.append("passwordRepeat", $("#passwordRepeat").val())

    $.ajax({
        url: contextPath + 'registration',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            cleanInputs()
            showToastForSave()
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("modalForRegistration")
        }
    })
}

function addText(input, message) {
    message = translateError(message)
    var icon = $('<p class="text-for-validating" style="color: #ff0000;">' + message + '</p>')
    icon.tooltip({
        content: message,
        position: {my: "left+15 center", at: "right center"}
    })
    input.after(icon);
    input.css("border-color", "#ff0000")
}
function translateError(key) {
    return key
        .replace('Please fill in the field', 'Заповніть поле')
        .replace('The number should be greater than', 'Число повинно бути більше, ніж')
        .replace('The number should be less than', 'Число повинно бути менше, ніж')
        .replace('The field should have fewer than', 'Поле повинно містити менше ніж')
        .replace('characters and more than', 'і більше ніж')
        .replace('ones', 'символів')
        .replace('characters', 'символів')
        .replace('Invalid email format', 'Невірний формат електронної пошти')
        .replace('Invalid phone format', 'Невірний формат телефону')
        .replace('File extension not valid', 'Тип файлу повиннен бути .jpeg, .png, .jpg')
        .replace('The telegram already exists!', 'Телеграм уже існує')
        .replace('The email already exists!', 'E-mail уже існує')
        .replace('The phone already exists!', 'Телефон уже існує')
}
function showLoader(blockId) {
    $("#" + blockId).block({
        message: `
            <div class="d-flex justify-content-center">
                <p class="me-2 mb-0">Please wait...</p>
                <div class="sk-wave sk-primary m-0">
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                </div>
            </div>`,
        css: {
            backgroundColor: "transparent",
            border: "0"
        },
        overlayCSS: {
            backgroundColor: "#fff",
            opacity: 0.8
        }
    });
}

function hideLoader(blockId) {
    $("#" + blockId).unblock();
}

function cleanInputs() {
    $('.text-for-validating').remove()
    var elements = document.querySelectorAll('input, select, textarea, button, .ql-editor,form');
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        element.style.borderColor = '';
    }
    var select2Selects = document.querySelectorAll('.select2-selection');
    for (var i = 0; i < select2Selects.length; i++) {
        var select2Select = select2Selects[i];
        select2Select.style.borderColor = '';
    }
    $("#goal").css("border", "")
}
var countError = 0;
function validDataFromResponse(errors) {
    cleanInputs()
    for (var fieldName in errors) {
        if (errors.hasOwnProperty(fieldName)) {
            var errorMessage = errors[fieldName];
            scrollToElement($('#' + fieldName.toString()));
            addText($('#' + fieldName.toString()), errorMessage)
            $('#' + fieldName.toString()).css("border", "1px solid #ff0000")
        }
    }
    countError = 0
}

function scrollToElement($element) {
    if (countError !== 0) return
    countError++
    if ($element.length > 0) {
        var windowHeight = $(window).height();
        var targetOffset = $element.offset().top - windowHeight / 4;

        $('html, body').animate({
            scrollTop: targetOffset
        }, 100);
    }
}