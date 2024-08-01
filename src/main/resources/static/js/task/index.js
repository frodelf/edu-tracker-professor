var page = 0
$(document).ready(function () {
    forSelect2(".coursesForFilter", contextPath + "course/get-for-select")
    forSelect2(".statusForFilter", contextPath + "enum/get-task-status")
    getPageWithFilter(page)
    document.querySelector(".flatpickr-for-filter").flatpickr({
        weekNumbers: true,
        enableTime: true,
        dateFormat: "d.m.Y H:i"
    })
})

function getPageWithFilter(page) {
    showLoader('taskTable')
    this.page = page
    var filterElements = $('.for-filter');
    $.ajax({
        type: "Get",
        url: contextPath + 'task/get-all',
        data: {
            page: page,
            pageSize: pageSize,
            name: filterElements[0].value,
            courseId: filterElements[1].value,
            deadline: filterElements[2].value,
            status: filterElements[3].value
        },
        success: function (objects) {
            var table = document.getElementById("taskTable");
            var tbody = table.querySelector("tbody");
            $('#taskTable tbody').empty();
            if($("#message-about-empty"))$("#message-about-empty").remove()
            if(objects.content.length == 0){
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow();
                var cell0 = newRow.insertCell(0);
                cell0.innerHTML = `${object.name}`;

                var cell1 = newRow.insertCell(1);
                var courses = ``
                if (object.course) {
                    const entries = Object.entries(object.course);
                    const courseLinks = entries.map(([key, value]) => {
                        return `<a href="${contextPath}course/edit/${key}">${value}</a>`
                    })
                    courses = courseLinks.join(', ')
                }
                cell1.innerHTML = courses;
                var cell2 = newRow.insertCell(2);
                if(object.status != 'OPEN')cell2.innerHTML = `—————`
                else if(object.deadline)cell2.innerHTML = `${changeFormatDate(object.deadline)}`
                else cell2.innerHTML = `Немає обмежень по часу`
                var cell3 = newRow.insertCell(3)
                if (object.status == 'OPEN') {
                    cell3.innerHTML = `<span class="badge bg-label-success">Відкрите</span>`
                } else {
                    cell3.innerHTML = `<span class="badge bg-label-danger">Закрите</span>`
                }
                var cell4 = newRow.insertCell(4)
                cell4.innerHTML = `
<button onclick="modalForRemove(${object.id})" class="btn btn-outline-danger float-end" style="margin-left: 10px"><i class="fa-solid fa-trash"></i></button>
<button onclick="modalForAdd(${object.id})" class="btn btn-outline-primary float-end" style="margin-left: 10px"><i class="fa-solid fa-pencil"></i></button>
<a href="${contextPath}task/${object.id}" class="btn btn-outline-secondary float-end"><i class="fa-regular fa-eye"></i></a>
`;
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader("taskTable")
        }
    })
}

function modalForRemove(taskId) {
    if ($('#modalForRemove').html()) $('#modalForRemove').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modalForRemove" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Ви впевені що хочете видалити об'єкт</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-danger" onclick="remove(${taskId})">Видалити</button>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    $('#modalForRemove').modal('show')
}

function remove(taskId) {
    $.ajax({
        url: contextPath + 'task/delete',
        type: 'DELETE',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            taskId: taskId
        },
        success: function (request) {
            getPageWithFilter(page)
            showToastForDelete()
            $('#modalForRemove').modal('hide')
        },
    })
}

function modalForAdd(taskId) {
    if (taskId) {
        $.ajax({
            url: contextPath + 'task/get-by-id',
            data: {
                taskId: taskId
            },
            success: function (task) {
                if ($('#modalForAdd').html()) $('#modalForAdd').remove()
                var modalBlock = document.createElement('div');
                modalBlock.innerHTML = `
        <div class="modal fade" id="modalForAdd" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div id="content-form" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Новий об'єкт</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Файл з завданням
                        <input id="file" type="file" class="form-control">
                        <div class="mt-3">Назва завдання</div>
                        <input id="name" class="form-control">
                        <div class="mt-3">Курс</div>
                        <select id="courseId" class="coursesForFilter"></select>
                        <div class="mt-3">
                            Дедлайн
                        </div>
                        <input id="deadline" class="form-control">
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addTask(${task.id})">Зберегти</button>
                    </div>
                </div>
            </div>
        </div>
    `
                document.body.appendChild(modalBlock)
                document.querySelector("#deadline").flatpickr({
                    weekNumbers: true,
                    enableTime: true,
                    dateFormat: "d.m.Y H:i",
                    defaultDate: changeFormatDate(task.deadline)
                })
                $("#name").val(task.name)
                
                forSelect2("#courseId", contextPath + "course/get-for-select", task.courseId, task.courseName)
                $('#modalForAdd').modal('show')
            },
        })
    } else {
        if ($('#modalForAdd').html()) $('#modalForAdd').remove()
        var modalBlock = document.createElement('div');
        modalBlock.innerHTML = `
        <div class="modal fade" id="modalForAdd" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div id="content-form" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Новий об'єкт</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Файл з завданням
                        <input id="file" type="file" class="form-control">
                        <div class="mt-3">
                        Назва завдання
                        </div>
                        <input id="name" class="form-control">
                        <div class="mt-3">
                        Курс
                        </div>
                        <select id="courseId" class="coursesForFilter"></select>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addTask()">Зберегти</button>
                    </div>
                </div>
            </div>
        </div>
    `
        document.body.appendChild(modalBlock)

        $('#modalForAdd').modal('show')
        forSelect2("#courseId", contextPath + "course/get-for-select")
    }
}
function addTask(taskId){
    showLoader("content-form")
    let formData = new FormData()
    if(taskId)formData.append('id', taskId)
    formData.append('name', $("#name").val())
    if($("#courseId").val())formData.append('courseId', $("#courseId").val())
    $.ajax({
        url: contextPath + 'task/add',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            cleanInputs()
            showToastForSave()
            getPageWithFilter(page)
            $('#modalForAdd').modal('hide')
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