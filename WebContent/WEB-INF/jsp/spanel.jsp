<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html ng-app="SE2-Software">
<head>
  <title>SE2-Praktikumssoftware</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">-->
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap.min.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap-select.min.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap-select.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/ui-bootstrap-csp.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/awesome-bootstrap-checkbox.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.css" />" >

</head>
<body ng-controller="TabController">
  <div class="container" ng-controller="ProfileController">
    <div class="row" ng-controller="MeldungenController">

      <!--Buttonpanel start-->
      <div class="col-sm-12" ng-controller="AnmeldeController">
        <div class="btn-group btn-group-justified">
          <div class="btn-group">
            <button type="button" class="btn btn-my-default" ng-click="switchToNews()">News</button>
          </div>
          <div class="btn-group">
            <button type="button" class="btn btn-my-default" ng-click="switchToProfile()">Profil</button>
          </div>
          <div class="btn-group">
            <button type="button" class="btn btn-my-default" ng-click="switchToTeams()">Meine Teams</button>
          </div>
          <div class="btn-group">
            <button type="button" class="btn btn-my-default dropdown-toggle" data-toggle="dropdown">
              Anmelden <span class="caret"></span></button>
              <ul class="dropdown-menu" role="menu">
                <li><a href="" ng-click="switchToPraktika()"><span>Pflichtpraktika</span></a></li>
                <li><a href="" ng-click="switchToWahlpflicht()"><span>Wahlpflicht</span></a></li>
                <li><a href="" ng-click="switchToProjekte()"><span>Projekt</span></a></li>
              </ul>
          </div>
          <div class="btn-group">
            <button type="button" class="btn btn-my-default" ng-click="switchToLeistungen()">Leistungen</button>
          </div>
          <div class="btn-group">
            <button  type="button" class="btn btn-my-default" ng-click="switchToMeldungen()">Meldungen <span class="badge">{{getAnzMeldungen()}}</span></button>
          </div>
          <div class="btn-group">
            <button type="button" ng-click="logout()" class="btn btn-my-default">Logout</button>
          </div>
        </div>
      </div>
    </div>


    <!-- Contentrow start-->
    <div class="row">
      <!--Buttonpanel end-->
      <div class="col-sm-12">
        <div ng-switch on="getCurrTab()">
          <div ng-switch-when="meldungen">
            <div ng-include="getMeldungenTab()">
            </div>
          </div>
          <div ng-switch-when="tests">
            <div ng-include="getTestSuite()">
            </div>
          </div>
          <div ng-switch-when="teamKonfig">
            <div ng-include src="getTeamKonfig()"></div>
          </div>
          <div ng-switch-when="teams">
            <div ng-include src="getTeamUebersicht()"></div>
          </div>
          <div ng-switch-when="news">
            <div ng-include src="getNews()"></div>
          </div>
          <div ng-switch-when="profile">
            <div ng-include src="getProfile()"></div>
          </div>
          <div ng-switch-when="praktika">
            <div ng-include src="getPraktika()"></div>
          </div>
          <div ng-switch-when="projekte">
            <div ng-include src="getProjekte()"></div>
          </div>
          <div ng-switch-when="wahlpflicht">
            <div ng-include src="getWahlpflicht()"></div>
          </div>
          <div ng-switch-when="gruppen">
            <div ng-include src="getGruppen()"></div>
          </div>
          <div ng-switch-when="leistungen">
            <div ng-include src="getLeistungen()"></div>
          </div>
          <div ng-switch-when="datenex">
            <div ng-include src="getDatenEx()"></div>
          </div>
          <div ng-switch-when="datenim">
            <div ng-include src="getDatenIm()"></div>
          </div>
          <div ng-switch-when="tmUebersicht">
            <div ng-include src="getTmUebersicht()"></div>
          </div>
          <div ng-switch-default>
            <div ng-include src="getProfile()"></div>
          </div>
        </div>
      </div>
    </div>
    <!--Contentrow end-->
  </div>
</body>
<!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>-->
<script src="<c:url value="/resources/js/jquery.min.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap.min.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-select.min.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-select.js" />" ></script>
<!--<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js" ></script>-->
<script src="<c:url value="/resources/js/angular.min.js" />" ></script>
<script src="<c:url value="/resources/global/app/app.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/angular-strap.min.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/angular-strap.tpl.min.js" />" ></script>
<script src="<c:url value="/resources/global/services/AutoScrollService.js" />" ></script>
<script src="<c:url value="/resources/global/controllers/NewsController.js" />" ></script>
<script src="<c:url value="/resources/global/services/database/DBNewsService.js" />" ></script>
<script src="<c:url value="/resources/global/controllers/AnmeldeController.js" />" ></script>
<script src="<c:url value="/resources/global/controllers/ErrorController.js" />" ></script>
<script src="<c:url value="/resources/global/services/database/DBAnmeldeService.js" />" ></script>
<script src="<c:url value="/resources/global/services/database/DBErrorService.js" />" ></script>
<script src="<c:url value="/resources/global/services/database/URLService.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/controllers/TabController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/controllers/ModalController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/controllers/TeamController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/controllers/PraktikaController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/controllers/MeldungenController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/controllers/LeistungenController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/Tests/TestController.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/Tests/UeberschneiderTest.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/database/DBTeamService.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/database/DBMeldungenService.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/database/DBPraktikaService.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/database/DBLeistService.js" />" ></script>
<script src="<c:url value="/resources/studpanel/js/angular/services/UeberschneidungsPruefer.js" />" ></script>
<script src="<c:url value="/resources/global/controllers/ProfileController.js" />" ></script>
<script src="<c:url value="/resources/global/services/database/DBProfileService.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/ui-bootstrap.min.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/ui-bootstrap-tpls.js" />" ></script>
<script src="<c:url value="/resources/js/bootstrap/ui-bootstrap-tpls.min.js" />" ></script>
<script src="<c:url value="/resources/js/angular-scroll.min.js" />" ></script>
<!--<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-scroll/0.7.2/angular-scroll.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-scroll/0.7.2/angular-scroll.min.js"></script>-->
<script>
</script>
</html>
