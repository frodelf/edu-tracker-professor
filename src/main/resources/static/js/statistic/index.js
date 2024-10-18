$(document).ready(function () {
    $.ajax({
        type: "Get",
        url: contextPath + 'student/get-count-active-student',
        success: function (countActiveStudent) {
            $('#active-student').html(`
            <div class="card-body">
                <h2>Активні студенти</h2>
                <h2>${countActiveStudent}</h2>
            </div>
            <hr style="margin-top: 0; margin-bottom: 0">
            <center style="margin-bottom: 10px; margin-top: 10px"><a href='${contextPath}student' style="margin: 5px">Детальніше</a></center>
            `)
        }
    })
    $.ajax({
        type: "Get",
        url: contextPath + 'course/get-count-course',
        success: function (count) {
            $('#active-course').html(`
            <div class="card-body">
                <h2>Активні курси</h2>
                <h2>${count}</h2>
            </div>
            <hr style="margin-top: 0; margin-bottom: 0">
            <center style="margin-bottom: 10px; margin-top: 10px"><a href='${contextPath}course' style="margin: 5px">Детальніше</a></center>
            `)
        }
    })
    $.ajax({
        type: "Get",
        url: contextPath + 'lesson/get-count-lesson-held',
        success: function (count) {
            $('#lesson-held').html(`
            <div class="card-body">
                <h2>Проведено занять</h2>
                <h2>${count}</h2>
            </div>
            <hr style="margin-top: 0; margin-bottom: 0">
            <center style="margin-bottom: 10px; margin-top: 10px"><a href='${contextPath}lesson' style="margin: 5px">Детальніше</a></center>
            `)
        }
    })
    $.ajax({
        type: "Get",
        url: contextPath + 'task/get-count-task',
        success: function (count) {
            $('#number-tasks').html(`
            <div class="card-body">
                <h2>Кількість задач</h2>
                <h2>${count}</h2>
            </div>
            <hr style="margin-top: 0; margin-bottom: 0">
            <center style="margin-bottom: 10px; margin-top: 10px"><a href='${contextPath}task' style="margin: 5px">Детальніше</a></center>
            `)
        }
    })
    statisticByLesson()
    $('#course').on('change', handleCourseChange)

    var page = 0

    $.ajax({
        type: "Get",
        url: contextPath + 'professor/get-first-course',
        success: function (course) {
            Object.entries(course).map(([key, value]) => {
                forSelect2("#courseForStudent", contextPath + "course/get-for-select", key, value)
                getPageWithFilter(page, key)
                studentMarkStats(key, value)

                forSelect2("#course", contextPath + "course/get-for-select", key, value)
                statisticByLesson(key)
            })
        },
        error: function (xhr, status, error) {
            if (xhr.status === 404) {
                var table = document.getElementById("studentTable");
                if ($(".message-about-empty")) $(".message-about-empty").remove()
                table.insertAdjacentHTML('afterend', '<center><h2 class="message-about-empty">Немає даних для відображення</h2></center>')
                $('#pagination_container').empty()
                $('.empty-block').html(`<center><h2>Немає даних для відображення</h2></center>`)
                studentMarkStats()
            }
        }
    })
    $('#courseForStudent').on('change', function () {
        getPageWithFilter(page)
    })
})
function studentMarkStats(key, value){
    if(key && value)forSelect2("#courseForStudentsMark", contextPath + "course/get-for-select", key, value)
// Color Variableslet cardColor, headingColor, labelColor, borderColor, legendColor;
    if($("#block-student-mark-stats").html())$("#block-student-mark-stats").remove()
    $("#block-student-mark-stats").html(`<canvas id="mark-stats" class="chartjs" data-height="400"></canvas>`)
    showLoader("block-student-mark-stats")
    $.ajax({
        type: "Get",
        url: contextPath + 'student/get-all-for-statistic',
        data: {
            page: 0,
            pageSize: 10,
            search: '',
            courseId: key,
        },
        success: function (objects) {
            var st = []
            var mk = []
            for (const el of objects.content) {
                st.push(el.groupName +" "+ el.lastName)
                mk.push(el.mark)
            }
            if (isDarkStyle) {
                cardColor = config.colors_dark.cardColor;
                headingColor = config.colors_dark.headingColor;
                labelColor = config.colors_dark.textMuted;
                legendColor = config.colors_dark.bodyColor;
                borderColor = config.colors_dark.borderColor;
            } else {
                cardColor = config.colors.cardColor;
                headingColor = config.colors.headingColor;
                labelColor = config.colors.textMuted;
                legendColor = config.colors.bodyColor;
                borderColor = config.colors.borderColor;
            }
            const barChart = document.getElementById('mark-stats');
            if (barChart) {
                const barChartVar = new Chart(barChart, {
                    type: 'bar',
                    data: {
                        labels: st,
                        datasets: [
                            {
                                data: mk,
                                backgroundColor: `#7367f0`,
                                borderColor: 'transparent',
                                maxBarThickness: 15,
                                borderRadius: {
                                    topRight: 15,
                                    topLeft: 15
                                }
                            }
                        ]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        animation: {
                            duration: 500
                        },
                        plugins: {
                            tooltip: {
                                rtl: isRtl,
                                backgroundColor: cardColor,
                                titleColor: headingColor,
                                bodyColor: legendColor,
                                borderWidth: 1,
                                borderColor: borderColor
                            },
                            legend: {
                                display: false
                            }
                        },
                        scales: {
                            x: {
                                grid: {
                                    color: borderColor,
                                    drawBorder: false,
                                    borderColor: borderColor
                                },
                                ticks: {
                                    color: labelColor
                                }
                            },
                            y: {
                                min: 0,
                                max: 100,
                                grid: {
                                    color: borderColor,
                                    drawBorder: false,
                                    borderColor: borderColor
                                },
                                ticks: {
                                    stepSize: 20,
                                    color: labelColor
                                }
                            }
                        }
                    }
                });
            }
            hideLoader("block-student-mark-stats")
        }
    })
}
function handleCourseChange() {
    var selectedCourseId = $('#course').val()
    statisticByLesson(selectedCourseId)
}
function statisticByLesson(courseId){
    if($('#lineChart').html())$('#lineChart').html('')
    $.ajax({
        type: "Get",
        url: contextPath + 'lesson/get-date-count-map',
        data:{
            courseId: courseId
        },
        success: function (statistic) {
            const categories = Object.keys(statistic)
            const data = Object.values(statistic).map(Number)
            if(categories.length<1){
                $('#lineChart').html(`<center><h2>Недостатньо даних для статистики</h2></center>`)
                return
            }
            const background = config.colors.background
            const linkColor = '#8176f2'
            const lineChartEl = document.querySelector('#lineChart'),
                lineChartConfig = {
                    chart: {
                        height: 400,
                        type: 'line',
                        parentHeightOffset: 0,
                        zoom: {
                            enabled: false
                        },
                        toolbar: {
                            show: false
                        }
                    },
                    series: [
                        {
                            data: data
                        }
                    ],
                    markers: {
                        strokeWidth: 7,
                        strokeOpacity: 1,
                        strokeColors: [config.colors.white],
                        colors: [config.colors.warning]
                    },
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        curve: 'straight'
                    },
                    colors: [config.colors.warning],
                    grid: {
                        borderColor: background,
                        xaxis: {
                            lines: {
                                show: true
                            }
                        },
                        padding: {
                            top: -20
                        }
                    },
                    tooltip: {
                        custom: function ({ series, seriesIndex, dataPointIndex, w }) {
                            return '<div class="px-3 py-2">' + '<span>' + series[seriesIndex][dataPointIndex] + ' студ.</span>' + '</div>';
                        }
                    },
                    xaxis: {
                        categories: categories,
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        labels: {
                            style: {
                                colors: linkColor,
                                fontSize: '13px'
                            }
                        }
                    },
                    yaxis: {
                        labels: {
                            style: {
                                colors: linkColor,
                                fontSize: '13px'
                            }
                        }
                    }
                };
            if (typeof lineChartEl !== undefined && lineChartEl !== null) {
                const lineChart = new ApexCharts(lineChartEl, lineChartConfig);
                lineChart.render();
            }
        }
    })
}

function getPageWithFilter(page, courseId) {
    var tableId = 'studentTable'
    showLoader(tableId)
    this.page = page
    var filterElements = $('.for-filter-student');
    if($("#courseForStudent").val()){
       courseId = $("#courseForStudent").val()
    }
    console.log('sd')
    $.ajax({
        type: "Get",
        url: contextPath + 'student/get-all-for-statistic',
        data: {
            page: page,
            pageSize: 10,
            search: filterElements[0].value,
            courseId: courseId,
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
                cell0.innerHTML = `${object.groupName}`;

                var cell1 = newRow.insertCell(1);
                cell1.innerHTML = `<a href="${contextPath}student/${object.id}">${object.fullName}</a>`

                var cell2 = newRow.insertCell(2);
                cell2.innerHTML = `<a href="https://t.me/${object.telegram.replace("@", "")}">${object.telegram}</a>`

                var cell3 = newRow.insertCell(3);
                cell3.innerHTML = `<h5>
                    <div style="display: flex; justify-content: center; align-items: center">
                        <div>${object.numberOfTasks || ''}  (</div>
                        <div style="color: green">${object.numberOfTasksDone || ''}</div>
                        <div>-</div>
                        <div style="color: red">${object.numberOfTasksNotDone || ''}</div>
                        <div>)</div>
                    </div>
                </h5>`

                var cell4 = newRow.insertCell(4);
                cell4.innerHTML = `${object.mark}`

            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader(tableId)
        }
    })
}