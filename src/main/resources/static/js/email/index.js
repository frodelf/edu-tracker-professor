$(document).ready(function () {
    forSelect2("#course", contextPath + "course/get-for-select")
    forSelect2WithSearchAndPageable("#group", contextPath + "student/get-group-for-select")
})
function sendMessage(){
    showLoader("content-form")
    let formData = new FormData()
    if($("#group").val())formData.append('group', $("#group").val())
    if($("#course").val())formData.append('course', $("#course").val())
    formData.append('theme', $("#theme").val())
    formData.append('message', $("#message").val())
    $.ajax({
        url: contextPath + 'email/sendMessage',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            cleanInputs()
            showSuccessToast("Розсилку здійснено успішно")
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("content-form")
        }
    })
}