var page = 0
$(document).ready(function () {
    forSelect2("#coursesForFilter", contextPath + "course/get-for-select")
    forSelect2("#selectForStatus", contextPath + "lesson/get-status-for-select")
    getPageWithFilter(page)
    document.querySelector(".flatpickr-for-filter").flatpickr({
        weekNumbers: true,
        enableTime: true,
        dateFormat: "d.m.Y H:i"
    })
})
function getPageWithFilter(page) {
    showLoader('lessonTable')
    this.page = page
    var filterElements = $('.for-filter')
    $.ajax({
        type: "Get",
        url: contextPath + 'lesson/get-all',
        data: {
            page: page,
            pageSize: pageSize,
            date: filterElements[0].value,
            courseId: filterElements[1].value,
            statusLesson: filterElements[2].value
        },
        success: function (objects) {
            var table = document.getElementById("lessonTable");
            var tbody = table.querySelector("tbody");
            $('#lessonTable tbody').empty();
            if($("#message-about-empty"))$("#message-about-empty").remove()
            if(objects.content.length == 0){
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow();
                var cell0 = newRow.insertCell(0);
                cell0.innerHTML = `${changeFormatDate(object.date)}`;

                var cell1 = newRow.insertCell(1);
                var courses = ``
                if (object.course) {
                    const entries = Object.entries(object.course);
                    const courseLinks = entries.map(([key, value]) => {
                        return `<a href="${contextPath}course/edit/${key}">${value}</a>`
                    })
                    courses = courseLinks.join(', ')
                }
                cell1.innerHTML = courses

                var cell2 = newRow.insertCell(2);
                if(object.presentStudent != 0)cell2.innerHTML = `${object.presentStudent}`

                var cell3 = newRow.insertCell(3)
                if (object.status == 'IN_PROGRESS') {
                    cell3.innerHTML = `<span class="badge bg-label-primary">В процесі</span>`
                } else {
                    cell3.innerHTML = `<span class="badge bg-label-danger">Закінчено</span>`
                }
                var cell4 = newRow.insertCell(4)
                if (object.status == 'IN_PROGRESS') {
                    cell4.innerHTML = `<center><a href="${contextPath}lesson/edit/${object.id}" class="btn btn-outline-secondary"><i class="fa-solid fa-pencil"></i></a></center>`
                } else {
                    cell4.innerHTML = `<center><button onclick="modalForView(${object.id})" class="btn btn-outline-secondary"><i class="fa-regular fa-eye"></i></button></center>`
                }
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader("lessonTable")
        }
    })
}
function showModalForLessonStart(){
    if ($('#modalForLessonStart').html()) $('#modalForLessonStart').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modalForLessonStart" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Почати заняття</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Посилання на заняття
                        <input id="link" class="form-control ">
                        <div class="mt-3">Курс</div>
                        <select id="courseId" class="form-control "></select>
                        <div class="mt-3">Групи</div>
                        <select id="groupForLessonStart" disabled class="form-control" ></select>
                        <button id="buttonForChooseGroup" class="float-end btn btn-primary mt-3" onclick="lessonStart()">Розпочати</button>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    forSelect2("#courseId", contextPath + "course/get-for-select")
    $('#courseId').change(function() {
        $("#groupForLessonStart").prop("disabled", false)
        $("#groupForLessonStart").prop("multiple", true)
        forSelect2("#groupForLessonStart", contextPath + "student/get-groups-by-course-for-select/"+$(this).val())
    })
    $('#modalForLessonStart').modal('show')
}
function modalForView(id){
    window.location.href = fullContextPath + 'lesson/view/'+id;
}
function lessonStart(){
    showLoader("modalForLessonStart")
    $.ajax({
        url: contextPath + 'lesson/start',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            link: $("#link").val(),
            courseId: $("#courseId").val(),
            groups: $("#groupForLessonStart").val()
        },
        success: function () {
            getPageWithFilter(page)
            showSuccessToast()
            $('#modalForLessonStart').modal('hide')
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("modalForLessonStart")
        }
    })
}