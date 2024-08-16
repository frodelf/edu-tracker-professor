var firstForSelect = true
$(document).ready(function () {
    forSelect2("#coursesEl", contextPath+"course/get-for-select-by-student/"+student.id)
    $.ajax({
        url: contextPath + 'student/statistic',
        type: 'Get',
        data: {
            studentId: student.id
        },
        success: function (statistics) {
            addBlock(statistics)
        },
    })

    $('#coursesEl').on('change', function() {
        $.ajax({
            url: contextPath + 'student/statistic',
            type: 'Get',
            data: {
                studentId: student.id,
                courseId: $(this).val()
            },
            success: function (statistics) {
                addBlock(statistics)
            },
        })
    })
})
function addBlock(statistics){
    var statistic = $('#statistic');
    if (statistic.html()) statistic.html('')
    if(statistic.status){
        statistic.html(`
    <div class="row">
        <center><h2>Недостатньо даних для відображення</h2></center>
    </div>`)
        return
    }
    if(firstForSelect){
        forSelect2("#coursesEl", contextPath+"course/get-for-select-by-student/"+student.id, statistics.courseId, statistics.course)
        firstForSelect = false
    }
    statistic.html(`
    <div class="row">
        <div class="col-lg-3 col-md-6 col-sm-12">
            <div class="card drag-item  mb-lg-0 mb-6">
                <div class="card-body text-center">
                <h2>
                    <i class="fa-solid fa-users" style="color: #B197FC;"></i>                                    
                </h2>
                <h4>${statistics.course || ''}</h4>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-md-6 col-sm-12">
            <div class="card drag-item  mb-lg-0 mb-6">
                <div class="card-body text-center">
                <h2>
                    <i class="fa-solid fa-users" style="color: #B197FC;"></i>                                    
                </h2>
                <h4>Завдання</h4>
                <h5>
                    <div style="display: flex; justify-content: center; align-items: center">
                        <div>${statistics.allTasks || ''}  (</div>
                        <div style="color: green">${statistics.doneTasks || ''}</div>
                        <div>-</div>
                        <div style="color: red">${statistics.notDoneTasks || ''}</div>
                        <div>)</div>
                    </div>
                </h5>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-md-6 col-sm-12">
            <div class="card drag-item  mb-lg-0 mb-6">
                <div class="card-body text-center">
                <h2>
                    <i class="fa-solid fa-users" style="color: #B197FC;"></i>                                    
                </h2>
                <h4>Відвідані заняття</h4>
                <h5>${statistics.lessons || ''}</h5>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-md-6 col-sm-12">
            <div class="card drag-item  mb-lg-0 mb-6">
                <div class="card-body text-center">
                <h2>
                    <i class="fa-solid fa-users" style="color: #B197FC;"></i>                                    
                </h2>
                <h4>Оцінка</h4>
                <h5>${statistics.mark || ''}</h5>
                </div>
            </div>
        </div>
    </div>
    `)
}