$(function ($) {
    $("#accordion").find("a").click();
    carregaGraficoMemoria();
    carregaGraficoDisco();
});


function carregaGraficoMemoria() {
    $.ajax({
        url: $('#url').val() + '/loadMemGraph',
        data: {
            'ip': $("#currentHostIp").val()
        },
        success: function (data) {
            declaraGraficoMemoria(data);
        },
        error: function () {
            exibeMensagemErro("Ocorreu um erro durante este processo, por favor contate o administrador.");
            desbloqueiaTela();
        }
    });
}

function declaraGraficoMemoria(dataSource) {
    $('#memoryGraph').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Memory (Gb)'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y:.1f} <span style="color: gray;">({point.percentage:.1f}%)</span></b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                showInLegend: true,
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}: {point.y:.1f} <span style="color: gray;">({point.percentage:.1f}%)</span></b>',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
                name: 'Percent',
                colorByPoint: true,
                data: dataSource.graphSourceList
            }]
    });
}

function carregaGraficoDisco() {
    $.ajax({
        url: $('#url').val() + '/loadDiskGraph',
        data: {
            'ip': $("#currentHostIp").val()
        },
        success: function (data) {
            declaraGraficoDisco(data);
        },
        error: function () {
            exibeMensagemErro("Ocorreu um erro durante este processo, por favor contate o administrador.");
            desbloqueiaTela();
        }
    });
}

function declaraGraficoDisco(dataSource) {
    $('#diskGraph').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Disk (Gb)'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y:.1f}  <span style="color: gray;">({point.percentage:.1f}%)</span></b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                showInLegend: true,
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.y:.1f} <span style="color: gray;">({point.percentage:.1f}%)</span>',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
                name: 'Percent',
                colorByPoint: true,
                data: dataSource.graphSourceList
            }]
    });
}



/**
 * Salva modificações realizadas no painel Configuration
 * @returns void
 */
function saveModifications() {
    $("#operation").val("1");
    $("#newIp").val(
            $("#hostIp").val());
    $("form").submit();
}


/**
 * Atuliza informações do painel da Dashboard e Softwares and Aplications Available
 * @returns void
 */
function refreshDatas() {
    $("#operation").val("0");
    $("#newIp").val(
            $("#hostIp").val());
    $("form").submit();
}