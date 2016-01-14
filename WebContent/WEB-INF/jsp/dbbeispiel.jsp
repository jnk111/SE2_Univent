<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html ng-app="SE2-Software" ng-controller="DBController">

<head>
  <title>SE2-Praktikumssoftware</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap-select.min.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap-select.css" />" >
  <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/ui-bootstrap-csp.css" />" >
</head>

<body ng-controller="DBController">
<div class="container">
  <div class="row">
    <div class="col-sm-12">
      <h3>DB-Beispiel</h3>
      <br/>
      <button type="button" class="btn btn-my-default" method="post" ng-click="getAllStudents()">Studenten abrufen</button>
    </div>
  </div>
  <div class="row">
    <br/>
    <div class="col-sm-12">
      <div ng-show="isClicked()">
        <table class="table table-bordered table-striped">
          <thead>
            <tr>
              <th>Matrikel-Nr.</th>
              <th>Vorname</th>
              <th>Nachname</th>
            </tr>
          </thead>
          <tbody>
            <tr ng-repeat="student in students">
              <td>{{student.matrNr}}</td>
              <td>{{student.vorName}}</td>
              <td>{{student.nachName}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div ng-show="isError()">
    	<span class="text text-danger">Verbindung konnte nicht hergestellt werden, wahrscheinlich fehlt gültige A-Kennung in der Klasse DB-Connectors im Package Controllers.</span>
    </div>
  </div>
</div>


</body>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="<c:url value="/resources/js/bootstrap/bootstrap.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/bootstrap-select.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/bootstrap-select.js" />" ></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js" ></script>
  <script src="<c:url value="/resources/js/angular/app/app.js" />" ></script>
  <script src="<c:url value="/resources/js/angular/controllers/DBController.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/ui-bootstrap.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/ui-bootstrap-tpls.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/ui-bootstrap-tpls.min.js" />" ></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-scroll/0.7.2/angular-scroll.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-scroll/0.7.2/angular-scroll.min.js"></script>
</html>