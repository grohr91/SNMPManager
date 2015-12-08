<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <!DOCTYPE html>
    <!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
    <!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
    <!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
    <!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
        <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
            <title></title>
            <meta name="description" content="">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <meta http-equiv="refresh" content="<s:property value="refreshTime"/>">


            <link rel="stylesheet" href="assets/bootstrap-3.3.5/css/bootstrap.min.css">
            <link rel="stylesheet" href="assets/bootstrap-3.3.5/css/bootstrap-theme.min.css">
            <link rel="stylesheet" href="assets/highcharts/highslide.min.css">

            <style>
                body {
                    padding-top: 50px;
                    padding-bottom: 20px;
                }
            </style>

        </head>
        <body>
            <!--[if lt IE 7]>
                <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
            <![endif]-->
            <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
                <div class="container">
                    <div class="navbar-header" >
                        <span class="navbar-brand">Gerência e Administração de Redes - Trabalho 2 - Gerente SNMP - Prof. Lucas Muller</span>
                    </div>
                    <div class="nav navbar-nav navbar-right" style="margin-top: 1%;s">
                    </div>
                </div>
            </div>

            <!-- Main jumbotron for a primary marketing message or call to action -->
            <input type="hidden" id="url" value="<%=request.getContextPath()%>"/>
            <div class="jumbotron">
                <div class="container">
                    <div class="row">
                        <!-- ---------------- COFIGURAÇÕES ---------------- -->
                        <div class="col-md-12" >
                            <s:form namespace="/" action="index" cssClass="form-horizontal" method="post" theme="simple">
                                <input type="hidden" id="operation" name="operation"/>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <label style='margin-bottom: 0px;'>
                                                <span class="glyphicon glyphicon-cog"></span> Configurations
                                            </label>
                                        </h4>
                                    </div>
                                    <div class="panel-body">
                                        <div class="col-md-6">
                                            <div class="input-group">
                                                <span class="input-group-addon">Host IP</span>
                                                <s:textfield type="text" id="hostIp" name="snmpDTO.hostIp" cssClass="form-control" placeholder="Ex: 127.0.0.1"/>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="input-group">
                                                <span class="input-group-addon">Refresh Time</span>
                                                <s:textfield id="refreshTime" name="refreshTime" type="number" cssClass="form-control" placeholder="Ex: 1000 ms"/>
                                                <span class="input-group-addon">sec</span>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <ul class="list-group">
                                                <li class="list-group-item"><i class="glyphicon glyphicon-calendar"></i> 
                                                    <input type="hidden" id="currentHostIp" name="currentIp" value="<s:property value="snmpDTO.hostIp"/>"/>
                                                    <input type="hidden" id="newIp" name="newIp" value=""/>
                                                    <strong>Managing since</strong> <s:date name="dtStart" format="dd/MM/yyyy HH:MM:ss"/>
                                                </li>
                                                <li class="list-group-item"><i class="glyphicon glyphicon-play"></i> 
                                                    <strong>Uptime: </strong> <s:property value="snmpDTO.sysUpTime"/>
                                                </li>
                                            </ul>
                                        </div>

                                        <div class="col-md-6">
                                            <ul class="list-group">
                                                <li class="list-group-item"><i class="glyphicon glyphicon-list"></i> 
                                                    <strong>Host Description: </strong> <s:property value="snmpDTO.sysDescr"/>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="panel-footer text-right">
                                        <a class="btn btn-primary" onclick="refreshDatas();"><i class="glyphicon glyphicon-refresh"></i> Refresh Manually</a>
                                        <a class="btn btn-primary" onclick="saveModifications();"><i class="glyphicon glyphicon-save"></i> Save Modifications</a>
                                    </div>
                                </div>
                            </s:form>
                        </div>
                        <!-- ---------------- FIM COFIGURAÇÕES ---------------- -->


                        <!-- ---------------- DASHBOARD ---------------- -->
                        <div class="col-md-12" >
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <label style='margin-bottom: 0px;'>
                                            <span class="glyphicon glyphicon-dashboard"></span> Dashboard 
                                        </label>
                                        <%-- <label class="pull-right">Last Update: <small> <s:date name="dtLastUpdate" format="dd/MM/yyyy HH:MM:ss"/></small></label> --%>
                                    </h4>
                                </div>
                                <div class="panel-body">
                                    <div class="col-md-6">
                                        <div id="memoryGraph" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>
                                    </div>
                                    <div class="col-md-6">
                                        <div id="diskGraph" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <!-- ---------------- FIM DASHBOARD ---------------- -->


                        <!-- ---------------- SOFWTARES ---------------- -->
                        <div class="col-md-12" >
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                            <label style='margin-bottom: 0px; width: 100%; cursor: pointer;'>
                                                <span class="glyphicon glyphicon-th-list"></span> Softwares and Aplications Installed
                                                <span class="pull-right">Total: <small> <s:property value="snmpDTO.softwares.size()"/></small></span>
                                            </label>
                                        </a>
                                    </h4>
                                </div>
                                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                                    <div class="panel-body">
                                        <ul class="list-group">
                                            <s:iterator value="snmpDTO.softwares">
                                                <li class="list-group-item"><s:property /> </li>
                                                </s:iterator>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- ---------------- FIM SOFWTARES ---------------- -->

                    </div>
                </div>
            </div>

            <div class="container">
                <!-- ---------------- MEMÓRIA ---------------- -->
                <div class="row">
                    <div class="col-md-12">
                        <div id="log" class="panel-group">
                        </div>
                    </div>
                </div>

                <footer class="row">
                    <div class="col-md-12 text-center">© Copyright 2015.IESAM - UNISC - Universidade de Santa Cruz do Sul - Todos os direitos reservados.</br>
                        <a href="mailto:grohr@ig.com.br">Guilherme Rohr</a> e <a href="mailto:dreinicke@mx2.unisc.br">Douglas Reinicke</a>
                    </div>
                </footer>
            </div>
            <!-- /container -->

            <script>window.jQuery || document.write('<script src="assets/jquery/jquery-1.11.3.min.js"><\/script>')</script>
            <script src="assets/bootstrap-3.3.5/js/bootstrap.min.js"></script>
            <script src="assets/highcharts/highcharts.js"></script>
            <script src="assets/highcharts/exporting.js"></script>
            <script src="assets/main.js"></script>
        </body>
    </html>
