var page = 0
$(document).ready(function () {
    getPageWithFilter(page)
    if(view)$('#finish-lesson-button').remove()
})
function getPageWithFilter(page) {
    showLoader('reviewTable')
    this.page = page
    var filterElements = $('.for-filter')
    $.ajax({
        type: "Get",
        url: contextPath + 'review/get-all-for-lesson-edit',
        data: {
            page: page,
            pageSize: pageSize,
            lessonId: lessonId,
            fullName: filterElements[0].value
        },
        success: function (objects) {
            var table = document.getElementById("reviewTable");
            var tbody = table.querySelector("tbody");
            $('#reviewTable tbody').empty();
            if($("#message-about-empty"))$("#message-about-empty").remove()
            if(objects.content.length == 0){
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow();
                var cell0 = newRow.insertCell(0);
                cell0.innerHTML = `<a href="${contextPath}student/${object.studentId}">${object.fullName}</a>`

                var cell1 = newRow.insertCell(1);
                cell1.innerHTML = `<center><input type="checkbox" class="form-check-input" id="review${object.id}" onclick="saveReview(${object.id})" ${object.present?'checked':''}></center>`

                var cell2 = newRow.insertCell(2);
                cell2.innerHTML = `<button class="btn float-end" type="button" onclick="showModalForTasks(${lessonId}, ${object.studentId})"><i class="fa-solid fa-list-check"></i></button>`
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader("reviewTable")
        }
    })
}

function showModalForTasks(lessonId, studentId){
    $.ajax({
        url: contextPath + 'student-task/get-all-for-lesson-edit',
        type: 'Get',
        data: {
            lessonId: lessonId,
            studentId: studentId
        },
        success: function (reviews) {
            if ($('#modalForTasks').html()){
                $('#modalForTasks').modal('hide')
                $('#modalForTasks').remove()
            }

            var modalBlock = document.createElement('div');
            modalBlock.innerHTML = `
        <div class="modal fade" id="modalForTasks" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Завдання</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped linkedRow" id="tasksTable"
                                   style="table-layout: fixed;">
                                    <thead>
                                        <th>Назва</th>       
                                        <th>Статус</th>       
                                        <th></th>       
                                    </thead>
                                    <tbody>
                                        
                                    </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
            document.body.appendChild(modalBlock)

            var tbody = $('#tasksTable tbody')
            reviews.forEach(function(review) {
                var statusHTML
                if (review.status === 'IN_PROCESS') {
                    statusHTML = '<center><span class="badge bg-label-danger">В процесі</span></center>'
                } else if (review.status === 'GRANTED') {
                    statusHTML = '<center><span class="badge bg-label-info">Здано</span></center>'
                } else {
                    statusHTML = '<center><span class="badge bg-label-success">Оцінено</span></center>'
                }

                var markHTML
                if (review.status == 'IN_PROCESS') {
                    markHTML = ``
                } else if(review.status == 'GRANTED') {
                    markHTML = `
                        <button class="btn mt-2 d-flex align-items-center justify-content-center" style="padding: 0; width: 100%; height: 100%;">
                            <i class="fa-solid fa-arrow-down" onclick="downloadFileFrom('${review.myWork}')" style="margin-right: 10px"></i>
                            <i class="fa-regular fa-star" onclick="showModalForEvaluate('${review.id}', '${studentId}')"></i>
                        </button>`;
                } else {
                    markHTML =      `<center>
                                        <a href="javascript:void(0)" onclick="downloadFileFrom('${review.myWork}')">${review.mark}</a>
                                        <button class="btn" style="padding: 7px"><i class="fa-solid fa-ban" onclick="cancelMark('${review.id}', '${studentId}')"></i></button>
                                    </center>`
                }

                var row = `
                    <tr>
                        <td><a href="${contextPath}task/${review.taskId}">${review.taskName}</a></td>
                        <td>${statusHTML}</td>
                        <td>${markHTML}</td>
                    </tr>
                `;
                tbody.append(row)
            })

            $('#modalForTasks').modal('show')
        },
    })
}
function downloadFileFrom(fileName) {
    var file = fileName
    $.ajax({
        type: "GET",
        url: contextPath+"minio/download",
        xhrFields: {
            responseType: 'blob'
        },
        data: {
            fileName: file
        },
        success: function (data) {
            var blob = new Blob([data], {type: 'application/octet-stream'});
            var filename = "file.pdf";

            if (window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                var link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = filename;
                link.click();
            }
        },
        error: function () {
            console.log("Помилка завантаження файлу");
        }
    });
}
function cancelMark(studentTaskId, studentId){
    if(view)return
    $.ajax({
        url: contextPath + 'student-task/cancel-mark',
        type: 'PUT',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            studentTaskId: studentTaskId
        },
        success: function (request) {
            showModalForTasks(lessonId, studentId)
        },
    })
}
function showModalForEvaluate(studentTaskId, studentId){
    if(view)return
    if ($('#modalForEvaluate').html()) $('#modalForEvaluate').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modalForEvaluate" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Оцінка за роботу</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Оцінка
                        <input class="form-control onlyNumberTo100" id="mark">
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="toEvaluate(${studentTaskId}, ${studentId})">Оцінити</button>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    $('#modalForEvaluate').modal('show')
    $('.onlyNumberTo100').on('input', function () {
        $(this).val(function (_, value) {
            value = value.replace(/[^\d.]+/g, '').replace(/^(\d*\.\d*)\..*$/, '$1');
            if (parseFloat(value) > 100) {
                return '100';
            }
            return value;
        });
    });

}
function toEvaluate(studentTaskId, studentId){
    if(view)return
    showLoader("modalForEvaluate")
    $.ajax({
        url: contextPath + 'student-task/evaluate',
        type: 'PUT',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            studentTaskId: studentTaskId,
            mark: $("#mark").val()
        },
        success: function (request) {
            $('#modalForEvaluate').modal('hide')
            showModalForTasks(lessonId, studentId)
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("modalForEvaluate")
        }
    })
}
function lessonFinish(){
    if(view)return
    showLoader("all-content")
    $.ajax({
        url: contextPath + 'lesson/finish',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            lessonId: lessonId
        },
        success: function (request) {
            window.location.href = fullContextPath + 'lesson';
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("all-content")
        }
    })
}

function saveReview(reviewId){
    if(view)return
    showLoader("all-content")
    $.ajax({
        url: contextPath + 'review/update-present',
        type: 'PUT',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            reviewId: reviewId,
            checked: document.getElementById('review'+reviewId).checked
        },
        success: function (request) {
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("all-content")
        }
    })
}