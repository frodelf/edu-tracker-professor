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
    forSelect2("#course", contextPath + "course/get-for-select")

    $('#course').on('change', handleCourseChange)
})
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
                            return '<div class="px-3 py-2">' + '<span>' + series[seriesIndex][dataPointIndex] + '%</span>' + '</div>';
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