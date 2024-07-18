var messageForDelete = "Об'єкт успішно видалено"
var messageForSave = "Об'єкт успішно збережено"
var pageSize = 10.
$(document).ready(function () {
    const inputs = document.querySelectorAll('.for-filter');
    let timeout = null;
    inputs.forEach(input => {
        input.addEventListener('input', function () {
            clearTimeout(timeout)
            timeout = setTimeout(() => {
                getPageWithFilter(0)
            }, 1000)
        })
    })
    $('.for-filter').on('change', function () {
        getPageWithFilter(0)
    })
})
function showLoader(blockId) {
    $("#" + blockId).block({
        message: `
            <div class="d-flex justify-content-center">
                <p class="me-2 mb-0">Please wait...</p>
                <div class="sk-wave sk-primary m-0">
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                    <div class="sk-rect sk-wave-rect"></div>
                </div>
            </div>`,
        css: {
            backgroundColor: "transparent",
            border: "0"
        },
        overlayCSS: {
            backgroundColor: "#fff",
            opacity: 0.8
        }
    });
}

function hideLoader(blockId) {
    $("#" + blockId).unblock();
}

function showSuccessToast(message) {
    toastr.options.closeButton = true
    toastr.options.progressBar = true;
    toastr.success(message)
}

function showErrorToast(message) {
    toastr.options.closeButton = true
    toastr.options.progressBar = true;
    toastr.error(message)
}

function showToastForDelete() {
    toastr.options.closeButton = true
    toastr.options.progressBar = true;
    toastr.success(messageForDelete)
}

function showToastForSave() {
    toastr.options.closeButton = true
    toastr.options.progressBar = true;
    toastr.success(messageForSave)
}

function previewImage(imgId, inputId) {
    const img = document.getElementById(imgId);
    const input = document.getElementById(inputId);
    const file = input.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
}

function setImage(imageName, imageBlock) {
    $.ajax({
        type: "Get",
        url: contextPath + 'minio/get-image',
        data: {
            imageName: imageName
        },
        success: function (url) {
            $("#" + imageBlock).attr("src", url)
        }
    })
}

var countError = 0;

function validDataFromResponse(errors) {
    cleanInputs()
    for (var fieldName in errors) {
        if (errors.hasOwnProperty(fieldName)) {
            var errorMessage = errors[fieldName];
            scrollToElement($('#' + fieldName.toString()));
            addText($('#' + fieldName.toString()), errorMessage)
            $('#' + fieldName.toString()).css("border", "1px solid #ff0000")
        }
    }
    countError = 0
}

function scrollToElement($element) {
    if (countError !== 0) return
    countError++
    if ($element.length > 0) {
        var windowHeight = $(window).height();
        var targetOffset = $element.offset().top - windowHeight / 4;

        $('html, body').animate({
            scrollTop: targetOffset
        }, 100);
    }
}

function addText(inputId, message) {
    message = translateError(message)
    var icon = $('<p class="text-for-validating" style="color: #ff0000;">' + message + '</p>')
    icon.tooltip({
        content: message,
        position: {my: "left+15 center", at: "right center"}
    })
    inputId.after(icon);
    inputId.css("border-color", "#ff0000")
}

function modalForLogoutModal() {
    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForLogout" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-md" role="document">
                <div class="modal-content">
                    <div class="modal-header text-center" style="font-size: 20px">
                        Ви впевнені що хочете вийти?
                    </div>
                    <div class="modal-footer">
                        <a href="${contextPath}logout" class="btn btn-danger float-end">Вийти</a>
                        <button class="btn btn-secondary float-end" data-bs-dismiss="modal">Скасувати</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#ModalForLogout').modal('show');
}

function translateError(key) {
    return key
        .replace('Please fill in the field', 'Заповніть поле')
        .replace('The number should be greater than', 'Число повинно бути більше, ніж')
        .replace('The number should be less than', 'Число повинно бути менше, ніж')
        .replace('The field should have fewer than', 'Поле повинно містити менше ніж')
        .replace('characters and more than', 'і більше ніж')
        .replace('ones', 'символів')
        .replace('characters', 'символів')
        .replace('Invalid email format', 'Невірний формат електронної пошти')
        .replace('Invalid phone format', 'Невірний формат телефону')
        .replace('File extension not valid', 'Тип файлу повиннен бути .jpeg, .png, .jpg')
}

function cleanInputs() {
    $('.text-for-validating').remove()
    var elements = document.querySelectorAll('input, select, textarea, button, .ql-editor,form');
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        element.style.borderColor = '';
    }
    var select2Selects = document.querySelectorAll('.select2-selection');
    for (var i = 0; i < select2Selects.length; i++) {
        var select2Select = select2Selects[i];
        select2Select.style.borderColor = '';
    }
    $("#goal").css("border", "")
}

$(document).ready(function () {
    $(".phone").each(function (index, element) {
        new Cleave("#" + element.id, {
            blocks: [13],
            numericOnly: true,
            prefix: "+380"
        })
    });
    $('.onlyNumber').on('input', function () {
        $(this).val(function (_, value) {
            return value.replace(/[^\d.]+/g, '').replace(/^(\d*\.\d*)\..*$/, '$1');
        });
    });
})
function toSelect2(selectId){
    $(selectId).select2({
        placeholder: "Виберіть об'єкт",
        minimumResultsForSearch: Infinity
    })
}
function forSelect2(selectId, url, id, text) {
    $(selectId).select2({
        placeholder: "виберіть об'єкт",
        ajax: {
            type: "Get",
            url: url,
            processResults: function (data) {
                var results = Object.keys(data).map(function (key) {
                    return {id: key, text: translateTest(data[key])};
                });
                return {
                    results: results
                }
            },
        },
        minimumResultsForSearch: Infinity
    })
    if (text && id) {
        $(selectId).append(new Option(text.toString(), id.toString(), true, true));
        $(selectId).trigger('change');
    }
}
function forSelect2WithSearchAndPageable(selectId, url, selectedItemId) {
    $(selectId).select2({
        //TODO доробити пошук
        minimumResultsForSearch: Infinity,
        placeholder: "виберіть об'єкт",
        ajax: {
            type: "Get",
            url: url,
            dataType: 'json',
            delay: 1500,
            data: function (params) {
                var number = params.page > 0 ? params.page - 1 : 0;
                return {
                    query: params.term || '',
                    page: number,
                    size: 10
                };
            },
            processResults: function (data) {
                var results = data.content.map(obj => {
                    const key = Object.keys(obj)[0];
                    const value = obj[key]
                    return {id: key, text: value};
                })
                var hasMore = data.totalPages > data.number
                return {
                    results: results,
                    pagination: {
                        more: hasMore
                    }
                }
            },
            cache: true
        }
    });
    if (selectedItemId) $(selectId).val(selectedItemId).trigger('change')
}

function validSelect2(select) {
    if (!select.val() || (Array.isArray(select.val()) && select.val().length === 0)) {
        scrollToElement(select)
        select.next().find(".select2-selection").css("border", "1px solid #ff0000");
        addText(select.next().find(".select2-selection"), "Елемент має бути вибрано");
        return false;
    }
    return true
}
function translateTest(text){
    if(text=="CLOSE")text="Закрите"
    if(text=="OPEN")text="Відкрите"
    return text
}