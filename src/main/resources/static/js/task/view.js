var page = 0
$(document).ready(function () {
    const courseId = Object.keys(task.course)[0];
    const courseValue = task.course[courseId];
    $("#course").html(`Курс: <a href="${contextPath}course/edit/${courseId}">${courseValue}</a>`)
    getPageWithFilter(page)
    toSelect2("#statusEl")
})
function getPageWithFilter(page) {
    showLoader('userTable')
    this.page = page
    var filterElements = $('.for-filter');
    $.ajax({
        type: "Get",
        url: contextPath + 'student-task/get-all-by-task',
        data: {
            page: page,
            pageSize: pageSize,
            taskId: task.id,
            groupName: filterElements[0].value,
            fullName: filterElements[1].value,
            telegram: filterElements[2].value,
            status: filterElements[3].value
        },
        success: function (objects) {
            var table = document.getElementById("userTable")
            var tbody = table.querySelector("tbody")
            $('#userTable tbody').empty()
            if($("#message-about-empty"))$("#message-about-empty").remove()
            if(objects.content.length == 0){
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow()
                var cell0 = newRow.insertCell(0)
                cell0.innerHTML = `${object.groupName}`
                var cell1 = newRow.insertCell(1)
                cell1.innerHTML = `<a href="${contextPath}/sudent/${object.id}">${object.fullName}</a>`
                var cell2 = newRow.insertCell(2)
                cell2.innerHTML = `<a href="https://t.me/${object.telegram.replace("@", "")}">${object.telegram}</a>`
                var cell3 = newRow.insertCell(3)
                if (object.status == 'IN_PROCESS') {
                    cell3.innerHTML = `<span class="badge bg-label-danger">В процесі</span>`
                } else if(object.status == 'GRANTED') {
                    cell3.innerHTML = `<span class="badge bg-label-info">Здано</span>`
                } else {
                    cell3.innerHTML = `<span class="badge bg-label-success">Оцінено</span>`
                }
                var cell4 = newRow.insertCell(4);
                if (object.status == 'IN_PROCESS') {
                    cell4.innerHTML = ``
                } else if(object.status == 'GRANTED') {
                    cell4.style.padding = '0px';
                    cell4.innerHTML = `<button class="btn mt-2" onclick="downloadFileFrom('${object.myWork}')" style="padding: 0px">
                        <img src="${contextPath}img/default-file-download.png" alt="" width="40%">
                    </button>`
                } else {
                    cell4.innerHTML = `<center>${object.mark}</center>`
                }
            }
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader("userTable")
        }
    })
}
function downloadFileFrom(fileName) {
    var file = task.file
    if(fileName) file = fileName
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