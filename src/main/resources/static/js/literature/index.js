var page = 0
$(document).ready(function () {
    forSelect2("#coursesForFilter", contextPath + "course/get-for-select")
    getPageWithFilter(page)
})

function getPageWithFilter(page) {
    var tableId = 'literatureTable'
    showLoader(tableId)
    this.page = page
    var filterElements = $('.for-filter');
    $.ajax({
        type: "Get",
        url: contextPath + 'literature/get-all',
        data: {
            page: page,
            pageSize: pageSize,
            name: filterElements[0].value,
            course: filterElements[1].value,
        },
        success: function (objects) {
            var table = document.getElementById(tableId);
            var tbody = table.querySelector("tbody");
            $('#' + tableId + ' tbody').empty();
            if ($("#message-about-empty")) $("#message-about-empty").remove()
            if (objects.content.length == 0) {
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow();
                var cell0 = newRow.insertCell(0);
                cell0.innerHTML = `<a href="${object.link}">${object.name}</a>`;

                var cell1 = newRow.insertCell(1);
                cell1.innerHTML = Object.entries(object.course).map(([key, value]) => {
                    return `<a href="${contextPath}course/edit/${key}">${value}</a>`
                })
                var cell2 = newRow.insertCell(2);
                cell2.innerHTML = `<button onclick="modalForRemoveObject(${object.id}, 'literature/delete')" class="btn btn-outline-danger float-end" style="margin-left: 10px"><i class="fa-solid fa-trash"></i></button>
                                <button onclick="showModalForAddLiterature(${object.id})" class="btn btn-outline-primary float-end" style="margin-left: 10px"><i class="fa-solid fa-pencil"></i></button>
                                <a href="${object.link}" class="btn btn-outline-secondary float-end"><i class="fa-regular fa-eye"></i></a>
                                `;
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader(tableId)
        }
    })
}
function showModalForAddLiterature(literatureId){
    if ($('#modal-for-add-literature').html()) $('#modal-for-add-literature').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modal-for-add-literature" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div id="content-form" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Додати літературу</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Назва
                        <input id="name" class="form-control">
                        <div class="mt-3">
                            Посилання
                        </div>
                        <input id="link" class="form-control">
                        <div class="mt-3">
                            Курс
                        </div>
                        <select id="course"></select>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addLiterature(${literatureId})">Додати</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#modal-for-add-literature').modal('show');
    if(literatureId){
        $.ajax({
            type: "Get",
            url: contextPath + 'literature/get-by-id',
            data: {
                id: literatureId
            },
            success: function (object) {
                $("#name").val(object.name)
                $("#link").val(object.link)
                forSelect2("#course", contextPath + "course/get-for-select", object.courseId, object.courseName)
            }
        })
    }
    else forSelect2("#course", contextPath + "course/get-for-select")
}
function addLiterature(literatureId){
    showLoader("content-form")
    let formData = new FormData()
    if(literatureId)formData.append('id', literatureId)
    formData.append('name', $("#name").val())
    formData.append('link', $("#link").val())
    formData.append('course', $("#course").val())
    $.ajax({
        url: contextPath + 'literature/add',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            cleanInputs()
            showToastForSave()
            getPageWithFilter(page)
            $('#modal-for-add-literature').modal('hide')
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