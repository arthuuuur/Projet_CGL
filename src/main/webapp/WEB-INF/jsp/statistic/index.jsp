<%@ page contentType="text/html;charset=UTF-8"%>

<%@ include file="../common/header.jsp"%>

<div class="row justify-content-center">
    <div class="card w-75">
        <div class="container-fluid">
            <div class="row align-items-center justify-content-center">
                <div class="mt-1">
                    <div class="list-group shadow" id="list-tab" role="tablist">
                        <a class="list-group-item list-group-item-action active list-group-item-dark" id="list-home-list"
                           data-bs-toggle="list" href="#chartNbBusinessByBusinessProvider" role="tab">Nombre d'affaires par apporteur d'affaire</a>
                        <a class="list-group-item list-group-item-action list-group-item-dark" id="list-profile-list"
                           data-bs-toggle="list" href="#chartAmountEarnedByBusinessProvider" role="tab">Somme gagnée pour chaque apporteur d'affaire</a>
                    </div>
                </div>
                <div class="row tab-content justify-content-center" id="nav-tabContent">
                    <div class="tab-pane fade show active w-50" id="chartNbBusinessByBusinessProvider" role="tabpanel">
                        <canvas id="canvasNbBusinessByBusinessProvider"></canvas>
                    </div>
                    <div class="tab-pane fade w-50" id="chartAmountEarnedByBusinessProvider" role="tabpanel">
                        <canvas id="canvasAmountEarnedByBusinessProvider"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const data = {
        labels: ${nbBusinessByBusinessProvider.get("labels")},
        datasets: [{
            label: 'Nombre d\'affaire',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: ${nbBusinessByBusinessProvider.get("values")},
        }]
    };
    const config = {
        type: 'line',
        data: data,
        options: {
            plugins: {
                title: {
                    display: true,
                    text: 'Nombre d\'affaire par apporteur d\'affaire'
                }
            }
        }
    };

    const data2 = {
        labels: ${amountEarnedByBusinessProvider.get("labels")},
        datasets: [{
            label: 'Somme gagnée (en €)',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: ${amountEarnedByBusinessProvider.get("values")},
        }]
    };

    const config2 = {
        type: 'line',
        data: data2,
        options: {
            plugins: {
                title: {
                    display: true,
                    text: 'Somme gagnée par apporteur d\'affaire (en €)'
                }
            }
        }
    };

    const myChart = new Chart($('#canvasNbBusinessByBusinessProvider'), config);
    const myChart2 = new Chart($('#canvasAmountEarnedByBusinessProvider'), config2);
</script>

<%@ include file="../common/footer.jsp"%>