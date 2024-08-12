$(document).ready(function () {
    showLoader("content-form")
    $('#avatar-img').html(nameForAvatar)
    $.ajax({
        url: fullContextPath + 'professor/get-personal-data',
        method: 'GET',
        success: function (professor) {
            $('#phone').val(professor.phone)
            $('#lastName').val(professor.lastName)
            $('#degree').val(professor.degree)
            $('#name').val(professor.name)
            $('#telegram').val(professor.telegram)
            $('#middleName').val(professor.middleName)
            $('#email').val(professor.email)
            if(professor.image){
                $('.avatar-preview').html(`
                    <img src="${professor.image}" width=250px height=250px style="border-radius: 50%">
                `)
            }
        },
        complete: function (xhr, status) {
            hideLoader("content-form")
        }
    })
    document.getElementById('image').addEventListener('change', function(event) {
        const file = event.target.files[0]
        const validExtensions = ['image/jpeg', 'image/png'];

        if(!validExtensions.includes(file.type)){
            showErrorToast("Файл повинен мати формат jpg, jpeg або png")
            document.getElementById('image').value = "";
            return
        }

        if (file) {
            $('.avatar-preview').html(`<img id="avatar-img" src=""  style="border-radius: 50%">`)
            const reader = new FileReader()
            reader.onload = function(e) {
                document.getElementById('avatar-img').src = e.target.result
            }
            reader.readAsDataURL(file)
        }
    })
})

function updatePersonalData(){
    showLoader("content-form")
    let formData = new FormData()
    formData.append('id', professorAuth.id)
    formData.append('middleName', $("#middleName").val())
    formData.append('name', $("#name").val())
    formData.append('lastName', $("#lastName").val())
    formData.append('degree', $("#degree").val())
    if($("#image").val())formData.append('image', $("#image")[0].files[0])
    formData.append('phone', $("#phone").val())
    formData.append('email', $("#email").val())
    formData.append('telegram', $("#telegram").val())

    $.ajax({
        url: contextPath + 'professor/update-personal-data',
        type: 'PUT',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function () {
            cleanInputs()
            showToastForSave()
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                cleanInputs()
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