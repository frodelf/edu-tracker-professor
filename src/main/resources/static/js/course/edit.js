//TODO: доробити функції для додавання і закривання різних тасок у курсах
var courseId
const snowEditor = new Quill('#goal', {
    bounds: '#goal',
    modules: {
        formula: true,
        toolbar: '#snow-toolbar'
    },
    theme: 'snow'
})

if (course) {
    courseId = course.id
    $("#name").val(course.name)
    $("#maximumMark").val(course.maximumMark)
    setImage(course.image, 'image-preview')
    var delta = snowEditor.clipboard.convert(course.goal)
    snowEditor.setContents(delta)
    $.ajax({
        type: "Get",
        url: contextPath + 'course/statistic',
        data: {
            id: course.id
        },
        success: function (statistic) {
            $("#statistic").html(`
                <div class="row">
                    <div class="col-lg-6 col-sm-12"></div>
                    <div class="col-lg-6 col-sm-12">
                        <div class="card drag-item  mb-lg-0 mb-6">
                            <div class="card-body text-center">
                                <h2>
                                    <i class="fa-solid fa-lock" style="color: #f90101;"></i>
                                </h2>
                                <h4>Закриті завдання</h4>
                                <h5>${statistic.closeTasks}</h5>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row gy-2 mt-1 mb-3">
                    <div class="col-lg-6 col-sm-12"></div>
                    <div class="col-lg-6 col-sm-12">
                        <div class="card drag-item  mb-lg-0 mb-6">
                            <div class="card-body text-center">
                                <h2>
                                    <i class="fa-solid fa-unlock" style="color: #63E6BE;"></i>
                                </h2>
                                <h4>Відкриті завдання</h4>
                                <h5>${statistic.openTasks}</h5>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row gy-2 mt-1 mb-3">
                    <div class="col-lg-6 col-sm-12"></div>
                    <div class="col-lg-6 col-sm-12">
                        <button class="btn btn-danger" style="width: 100%; font-size: 20px">Закрити всі завдання</button>
                    </div>
                </div>
    `)
        }
    })
}
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('cancel').onclick = function () {
        window.location.href = contextPath + 'course'
    }
    $(".check-image-extension").change(function () {
        var file = this.files[0]
        if (file) {
            var fileName = file.name
            var fileExtension = fileName.split('.').pop().toLowerCase()

            var allowedExtensions = ['png', 'jpeg', 'jpg']

            if (allowedExtensions.indexOf(fileExtension) === -1) {
                toastr.options.progressBar = true;
                toastr.error('Дозволені тільки файли з розширеннями .png, .jpeg або .jpg')
                $(this).val('')
                return
            }
            previewImage("image-preview", "image")
        }
    })
})

function save() {
    showLoader("content-form")
    let formData = new FormData()
    if(courseId)formData.append("id", courseId)
    formData.append("name", $("#name").val())
    if(snowEditor.root.innerHTML !== '<p><br></p>') formData.append("goal", snowEditor.root.innerHTML)
    formData.append("maximumMark", $("#maximumMark").val())
    if($("#image")[0].files[0]){
        formData.append("image", $("#image")[0].files[0])
    }

    $.ajax({
        url: contextPath + 'course/add',
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
            hideLoader("content-form")
        }
    })
}