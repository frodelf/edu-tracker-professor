var currentPage;

function updatePagination(currentPage, totalButtons, container) {
    var containerBlock = document.getElementById(container);
    containerBlock.innerHTML = '';

    var pagination = document.createElement("div")

    var li, link;
    if (currentPage >= 0) {
        li = document.createElement('span');
        li.className = "page-link";
        li.setAttribute("href", "#");
        li.setAttribute("aria-label", "Previous");
        link = document.createElement('span');
        link.innerHTML = "&#10094;";
        if(currentPage == 0)li.classList.add("disabled")
        else li.setAttribute("onclick", `getPageWithFilter(${currentPage - 1})`);
        li.appendChild(link);
        pagination.appendChild(li);
    }
    if (totalButtons <= 7) {
        for (var i = 0; i <= (totalButtons - 1); i++) {
            li = createPaginationItem(i);
            if (i == currentPage) {
                li.classList.add("active");
            }
            pagination.appendChild(li);
        }
    } else {
        li = createPaginationItem(0);
        if (0 == currentPage) {
            li.classList.add("active");
        }
        pagination.appendChild(li);
        if (currentPage < 3) {
            for (var i = 1; i < 4; i++) {
                li = createPaginationItem(i);
                if (i == currentPage) {
                    li.classList.add("active");
                }
                pagination.appendChild(li);
            }
            li = createEllipsisItem();
            pagination.appendChild(li);
        } else if (parseInt(currentPage) >= parseInt(totalButtons) - 3) {

            li = createEllipsisItem();
            if (i == currentPage) {
                li.classList.add("active");
            }
            pagination.appendChild(li);
            for (var i = totalButtons - 4; i <= totalButtons - 2; i++) {
                li = createPaginationItem(i);
                if (i == currentPage) {
                    li.classList.add("active");
                }
                pagination.appendChild(li);
            }
        } else {
            li = createEllipsisItem();
            if (i == currentPage) {
                li.classList.add("active");
            }
            pagination.appendChild(li);
            for (var i = currentPage - 1; i <= parseInt(currentPage) + 1; i++) {

                li = createPaginationItem(i);
                if (i == currentPage) {
                    li.classList.add("active");
                }
                pagination.appendChild(li);
            }
            li = createEllipsisItem();
            if (i == currentPage) {
                li.classList.add("active");
            }
            pagination.appendChild(li);
        }
        if ((currentPage + 1) <= totalButtons) {
            li = createPaginationItem(totalButtons - 1);
            if (i == currentPage) {
                li.classList.add("active");
            }
            pagination.appendChild(li);
        }
    }

    if (totalButtons >= (currentPage + 1) && totalButtons > 1) {
        li = document.createElement('span');
        li.className = "page-link";
        li.setAttribute("href", "#");
        li.setAttribute("aria-label", "Previous");
        link = document.createElement('span');
        link.innerHTML = "&#10095;";
        li.appendChild(link);
        if(totalButtons == (currentPage + 1))li.classList.add("disabled")
        else li.setAttribute("onclick", `getPageWithFilter(${currentPage + 1})`);
        pagination.appendChild(li);
    }

    var block = document.createElement('div')
    block.style.width='100%'
    block.innerHTML = `
<div class="row">
    <div class="pagination pagination-sm m-0 mt-1" style="display: flex; justify-content: flex-end">
        ${pagination.innerHTML}
    </div>
</div>`

    containerBlock.appendChild(block)
}

function createPaginationItem(pageNumber) {
    var li = document.createElement('li');
    li.classList.add('page-item');
    if (parseInt(pageNumber) === parseInt(currentPage)) {
        li.classList.add('active');
    }
    var button = document.createElement('button');
    button.classList.add('page-link');
    button.innerText = pageNumber + 1;
    li.appendChild(button);

    button.setAttribute("onclick", `getPageWithFilter(${pageNumber})`)
    return li;
}

function createEllipsisItem() {
    var li = document.createElement('li');
    li.classList.add('page-item');
    var link = document.createElement('a');
    link.classList.add('page-link');
    link.innerText = '...';
    li.appendChild(link);
    return li;
}

function updateLabelPagination(currentPage, container, totalElements, lastPageElements, sizePage) {
    if (totalElements > 0) {
        let label = '<span>Показано</span> ' + (currentPage * sizePage + 1) + '-' + Math.min((currentPage + 1) * sizePage, totalElements) + ` <span> из </span> ` + totalElements;

        if (currentPage * sizePage == 0 && totalElements > 0) {
            label = `<span>Показано</span> ` + 1 + `-` + Math.min((currentPage + 1) * sizePage, totalElements) + ` <span>из</span> ` + totalElements;
        }

        if ((currentPage + 1) * sizePage > totalElements) {
            label = `<span>Показано</span> ` + (currentPage * sizePage) + `-` + (currentPage * sizePage + lastPageElements) + ` <span> из </span> ` + totalElements;
        }

        if (currentPage * sizePage == 0 && totalElements > 0 && (currentPage + 1) * sizePage > totalElements) {
            label = `<span>Показано</span> ` + 1 + `-` + (currentPage * sizePage + lastPageElements) + ` <span>из</span> ` + totalElements;
        }
        var p = document.createElement('p');
        p.innerHTML = label;
        var changeLabel = document.getElementById(container);
        changeLabel.innerHTML = '';
        changeLabel.appendChild(p);
    }
}