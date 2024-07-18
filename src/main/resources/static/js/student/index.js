var page = 0
$(document).ready(function () {
    forSelect2("#coursesEl", contextPath + "course/get-for-select")
    getPageWithFilter(page)
})

function getPageWithFilter(page) {
    showLoader('userTable')
    this.page = page
    var filterElements = $('.for-filter');
    $.ajax({
        type: "Get",
        url: contextPath + 'student/get-all',
        data: {
            page: page,
            pageSize: pageSize,
            group: filterElements[0].value,
            fullName: filterElements[1].value,
            course: filterElements[2].value,
            telegram: filterElements[3].value,
            phone: filterElements[4].value,
        },
        success: function (objects) {
            var table = document.getElementById("userTable");
            var tbody = table.querySelector("tbody");
            $('#userTable tbody').empty();
            if($("#message-about-empty"))$("#message-about-empty").remove()
            if(objects.content.length == 0){
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow();
                var cell0 = newRow.insertCell(0);
                cell0.innerHTML = `${object.group}`;

                var cell1 = newRow.insertCell(1);
                cell1.innerHTML = `<a href="${contextPath}student/${object.id}">${object.lastName} ${object.name} ${object.middleName}</a>`

                var cell2 = newRow.insertCell(2);
                var courses = ``
                if (object.courses) {
                    const entries = Object.entries(object.courses);
                    const courseLinks = entries.map(([key, value]) => {
                        return `<a href="${contextPath}course/edit/${key}">${value}</a>`
                    })
                    courses = courseLinks.join(', ')
                }
                cell2.innerHTML = courses;

                var cell3 = newRow.insertCell(3);
                cell3.innerHTML = `<a href="https://t.me/${object.telegram.replace("@", "")}">${object.telegram}</a>`;

                var cell4 = newRow.insertCell(4);
                cell4.innerHTML = `${object.phone ? object.phone : '———'}`;

                var cell5 = newRow.insertCell(5);
                cell5.innerHTML = `
<a href="${contextPath}student/${object.id}" class="btn btn-outline-secondary float-end"><i class="fa-regular fa-eye"></i></a>
<button onclick="showModalForRemove(${object.id})" class="btn btn-outline-primary"><i class="fa-solid fa-trash"></i></button>`;
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader("userTable")
        }
    })
}
function showModalForRemove(studentId){
    if ($('#modalForRemove').html()) $('#modalForRemove').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modalForRemove" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Курс з якого виключити студента</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Курс
                        <select id="coursesForRemove"></select>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-danger" onclick="removeFromCourse(${studentId})">Вилучити</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#modalForRemove').modal('show');
    forSelect2("#coursesForRemove", contextPath+"course/get-for-select-by-student/"+studentId)
}
function removeFromCourse(studentId){
    cleanInputs()
    if (!($("#coursesForRemove").val())) {
        validSelect2($("#coursesForRemove"))
        return
    }
    remove(studentId, $("#coursesForRemove").val())
    $('#ModalForRemove').modal('hide')
}
function showModalForAddStudentStepFirst() {
    if ($('#ModalForAddStudentStepFirst').html()) $('#ModalForAddStudentStepFirst').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForAddStudentStepFirst" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Додати студента на курс</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Група
                        <select id="groupForStudentAdding"></select>
                        Курс
                        <select id="coursesForStudentAdding"></select>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="searchStudent()">Шукати</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#ModalForAddStudentStepFirst').modal('show');
    forSelect2("#coursesForStudentAdding", contextPath + "course/get-for-select")
    forSelect2WithSearchAndPageable("#groupForStudentAdding", contextPath + "student/get-group-for-select")
}

function searchStudent() {
    cleanInputs()
    var valid = true
    if (!($("#groupForStudentAdding").val())) {
        validSelect2($("#groupForStudentAdding"))
        valid = false
    }
    if (!($("#coursesForStudentAdding").val())) {
        validSelect2($("#coursesForStudentAdding"))
        valid = false
    }
    if(!valid)return

    $('#ModalForAddStudentStepFirst').modal('hide');
    showModalForAddStudentStepSecond($("#groupForStudentAdding").val(), $("#coursesForStudentAdding").val())
}
function remove(studentId, courseId) {
    $.ajax({
        url: contextPath + 'student/delete-from-course',
        type: 'DELETE',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: {
            studentId: studentId,
            courseId: courseId
        },
        success: function (request) {
            getPageWithFilter(page)
            showToastForDelete()
        },
    })
}
function activateAllCheckbox(checkbox) {
        if (checkbox.checked) {
            console.log("asd")
            $(".for-student-adding").prop('checked', true);
        } else {
            console.log("asdddddd")
            $(".for-student-adding").prop('checked', false);
        }
}
function showModalForAddStudentStepSecond(group, courseId) {
    $.ajax({
        url: contextPath + 'student/get-all-by-group-and-course',
        data: {
            group: group,
            courseId: courseId
        },
        success: function (students) {
            console.log(students)
            if ($('#ModalForAddStudentStepSecond').html()) $('#ModalForAddStudentStepSecond').remove()

            var modalBlock = document.createElement('div');
            modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForAddStudentStepSecond" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Студенти</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="card">
                            <div class="card-header">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped linkedRow"
                                           id="tableForStudentAdd" style="table-layout: fixed;">
                                        <thead>
                                        <tr>
                                        <th>Група</th>
                                        <th>Назва</th>
                                        <th><center><input type="checkbox" onclick="activateAllCheckbox(this)" class="form-check-input"></center></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addStudentToCourse(${courseId})">Додати</button>
                    </div>
                </div>
            </div>
        </div>
    `
            document.body.appendChild(modalBlock)
            $('#ModalForAddStudentStepSecond').modal('show')

            var table = document.getElementById("tableForStudentAdd")
            var tbody = table.querySelector("tbody")
            $('#tableForStudentAdd tbody').empty()
            for (const student of students) {
                var newRow = tbody.insertRow()
                var cell0 = newRow.insertCell(0)
                cell0.innerHTML = `${student.groupName}`

                var cell1 = newRow.insertCell(1)
                cell1.innerHTML = `<a href="${contextPath}student/${student.id}">${student.fullName}</a>`

                var cell2 = newRow.insertCell(2)
                cell2.innerHTML = `<center><input type="checkbox" id="student${student.id}" class="form-check-input for-student-adding" ${student.present == true ? 'checked'  : ''}></center>`
            }
        },
    })
}
function addStudentToCourse(courseId) {
    var formData = new FormData();
    document.querySelectorAll('.for-student-adding').forEach(function(input) {
        var studentId = input.id.replace("student", "");
        formData.append(studentId, input.checked);
    });

    formData.append("courseId", courseId);

    $.ajax({
        url: contextPath + 'student/add-to-course',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            showToastForSave()
            $('#ModalForAddStudentStepSecond').modal('hide')
            getPageWithFilter(page)
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}
