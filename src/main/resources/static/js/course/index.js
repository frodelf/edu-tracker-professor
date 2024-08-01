var page = 0
getPageWithFilter(page)

function getPageWithFilter(page) {
    showLoader('courses')
    this.page = page
    $.ajax({
        type: "Get",
        url: contextPath + 'course/get-all',
        data: {
            page: page,
            pageSize: pageSize
        },
        success: function (courses) {
            var content = ``
            for (var i = 0; i < courses.content.length; i++) {
                if (i === 0) content += `<div class="row mt-0 gy-4">`
                else if (i % 2 === 0) content += `</div><div class="row mt-0 gy-4">`
                content += addBlock(courses.content[i])
                if (i === courses.content.length - 1) {
                    content += `</div>`
                    $("#courses").html(content)

                    $('#pagination_container').empty();
                    if (courses.totalPages > 1) updatePagination(page, courses.totalPages, 'pagination_container')
                    return
                }
            }
        },
        complete: function (xhr, status) {
            hideLoader("courses")
        }
    })
}

function addBlock(course) {
    return `
        <div class="col-lg-6 col-md-12">
            <div class="card">
                <div class="card-content">
                    <div class="row" style="width: 96%; margin-left: 2%">
                        <div class="col-9">
                            <p class="font-weight-bond fs-3">${course.name}</p>
                            <br/>
                            Ціль курсу: ${course.goal}
                        </div>
                        <div class="col-3" style="padding-right: 0">
                            <img src="${course.image}" width="90%" height="90px" style="margin-top: 10px" class="float-end">
                        </div>
                    </div>
                </div>
                <div class="card-footer mt-4 mb-2" style="padding: 0">
                    <button class="btn btn-outline-primary float-end" style="margin-right: 10px" onclick="showModalForTasks(${course.id})"><i class="fa-solid fa-list-check"></i></button>
                    <button class="btn btn-outline-secondary float-end" style="margin-right: 10px" onclick="view(${course.id})"><i class="fa-regular fa-eye"></i></button>
                </div>
            </div>
        </div>
    `
}
function view(id) {
    if($('#ModalForView').html())$('#ModalForView').remove()

    $.ajax({
        type: "Get",
        url: contextPath + 'course/statistic',
        data: {
            id: id
        },
        success: function (statistics) {
            var modalBlock = document.createElement('div');
            modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForView" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Статистика</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-lg-3 col-md-6 col-sm-12">
                                <div class="card drag-item  mb-lg-0 mb-6">
                                    <div class="card-body text-center">
                                    <h2>
                                        <i class="fa-solid fa-users" style="color: #B197FC;"></i>                                    
                                    </h2>
                                    <h4>Активні студенти</h4>
                                    <h5>${statistics.students}</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 col-sm-12">
                                <div class="card drag-item  mb-lg-0 mb-6">
                                    <div class="card-body text-center">
                                    <h2>
                                        <i class="fa-solid fa-book-open" style="color: #B197FC;"></i>
                                    </h2>
                                    <h4>Кількість літератури</h4>
                                    <h5>${statistics.literatures}</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 col-sm-12">
                                <div class="card drag-item  mb-lg-0 mb-6">
                                    <div class="card-body text-center">
                                    <h2>
                                        <i class="fa-solid fa-person-chalkboard" style="color: #B197FC;"></i>
                                    </h2>
                                    <h4>Проведено занять</h4>
                                    <h5>${statistics.lessons}</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 col-sm-12">
                                <div class="card drag-item mb-lg-0 mb-6">
                                    <div class="card-body text-center">
                                    <h2>
                                        <i class="fa-solid fa-list-check" style="color: #B197FC;"></i>
                                    </h2>
                                    <h4>Кількість задач</h4>
                                    <h5>
                                        <div style="display: flex; justify-content: center; align-items: center">
                                            <div>${statistics.allTasks}  (</div>
                                            <div style="color: green">${statistics.openTasks}</div>
                                            <div>-</div>
                                            <div style="color: red">${statistics.closeTasks}</div>
                                            <div>)</div>
                                        </div>
                                    </h5>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
            document.body.appendChild(modalBlock);
            $('#ModalForView').modal('show');
        },
    })
}

function showModalForTasks(courseId){
    if($('#ModalForTasks').html())$('#ModalForTasks').remove()

    $.ajax({
        type: "Get",
        url: contextPath + 'course/statistic',
        data: {
            id: courseId
        },
        success: function (statistics) {
            var modalBlock = document.createElement('div');
            modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForTasks" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Завдання</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="card drag-item  mb-lg-0 mb-6">
                                <div class="card-body text-center">
                                    <h2>
                                        <i class="fa-solid fa-lock-open"></i>                                    
                                    </h2>
                                    <h4>Відкриті задачі</h4>
                                    <h5 style="color: green">${statistics.openTasks}</h5>
                                </div>
                                <div class="card-footer">
                                    <button class="btn btn-outline-primary float-end" onclick="showModalForOpenTask(${courseId})"><i class="fa-solid fa-plus"></i></button>
                                </div>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="card drag-item  mb-lg-0 mb-6">
                                <div class="card-body text-center">
                                    <h2>
                                        <i class="fa-solid fa-lock-open"></i>                                    
                                    </h2>
                                    <h4>Закрити задачі</h4>
                                    <h5 style="color: red">${statistics.closeTasks}</h5>
                                </div>
                                <div class="card-footer">
                                    <button class="btn btn-outline-danger float-end" onclick="showModalForCloseTask(${courseId})"><i class="fa-solid fa-minus"></i></button>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <center>
                                <button class="btn btn-outline-danger  mt-3">Закрити всі задачі</button>
                            </center>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
            document.body.appendChild(modalBlock)
            $('#ModalForTasks').modal('show')
        }
    })
}

function showModalForCloseTask(courseId){
    if($('#ModalForCloseTasks').html())$('#ModalForCloseTasks').remove()
    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForCloseTasks" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Завдання</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <select id="taskForClose" multiple></select>
                        <button class="btn btn-outline-danger float-end mt-3" onclick="closeTask(${courseId})">Закрити</button>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    forSelect2("#taskForClose", contextPath + "task/get-all-open-for-select-by-course-id/"+courseId)
    $('#ModalForCloseTasks').modal('show')
}

function closeTask(courseId){
    showLoader("ModalForCloseTasks")

    let formData = new FormData()
    if($('#taskForClose').val())formData.append('taskForClose', $('#taskForClose').val())

    $.ajax({
        url: contextPath + 'task/close',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            showToastForSave()
            $('#ModalForCloseTasks').modal('hide')
            $('#ModalForTasks').modal('hide')
            showModalForTasks(courseId)
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("ModalForOpenTasks")
        }
    })
}

function showModalForOpenTask(courseId){
    if($('#ModalForOpenTasks').html())$('#ModalForOpenTasks').remove()
    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForOpenTasks" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Завдання</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <select id="taskId"></select>
                        <div class="mt-3">Дедлайн</div>
                        <input id="deadline" class="form-control">
                        <button class="btn btn-outline-success float-end mt-3" onclick="openTask(${courseId})">Відкрити</button>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    forSelect2("#taskId", contextPath + "task/get-all-close-for-select-by-course-id/"+courseId)
    document.querySelector("#deadline").flatpickr({
        weekNumbers: true,
        enableTime: true,
        dateFormat: "d.m.Y H:i"
    })
    $('#ModalForOpenTasks').modal('show')
}
function openTask(courseId){
    showLoader("ModalForOpenTasks")

    let formData = new FormData()
    if($('#taskId').val())formData.append('taskId', $('#taskId').val())
    if($('#deadline').val())formData.append('deadline', $('#deadline').val())

    $.ajax({
        url: contextPath + 'task/open',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            showToastForSave()
            $('#ModalForOpenTasks').modal('hide')
            $('#ModalForTasks').modal('hide')
            showModalForTasks(courseId)
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("ModalForOpenTasks")
        }
    })
}