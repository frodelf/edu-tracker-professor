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
                // cell1.innerHTML = Object.entries(object.course).map(([key, value]) => {
                //     return `<a href="${contextPath}course/edit/${key}">${value}</a>`
                // })
                var cell2 = newRow.insertCell(2);
                cell2.innerHTML = '';
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader(tableId)
        }
    })
}