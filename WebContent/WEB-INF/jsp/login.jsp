<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" ng-app="SE2-Login">
<head>
  <title>Login</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap.min.css" />" >
  <!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">-->
  <link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />" >
</head>
<body ng-controller="LoginController">
  <div class="container">
    <br>
    <div class="row">
      <div class="col-sm-10 col-lg-offset-1 text-center">
        <div class="panel panel-cust-default">
          <div class="panel-body">
            <h1 class="text-muted">Univent v1.3</h1>
            <br>
            <form class="form-signin col-sm-8 col-lg-offset-2 text-center" ng-model="panel" ng-submit="login(username, passwort)" method="post">
              <br />
              Univent-User <br/>
              <label for="inputEmail" class="sr-only">Benutzername</label>
              <input type="username" id="inputUser" class="form-control" placeholder="Benutzername" ng-model="username" required autofocus>
              <label for="inputPassword" class="sr-only">Password</label>
              <input type="password" id="inputPassword" class="form-control" placeholder="Passwort" ng-model="passwort" required>
              <br />
              <div class="checkbox">
                <label>
                  <input type="checkbox" value="remember-me">Daten merken
                </label>
              </div>
              <p><button class="btn btn-my-block btn-lg" type="submit">Anmelden</button></p>
              <br>
            </form>
            <div class="panel panel-cust-primary col-sm-10 col-lg-offset-1">
                <div class="panel-body text-left">
                   Login Student1 : aaa016 -> Helge Schneider <br />
                   Login Student2 : aaa017 -> Max Mustermann  <br />
                   Login Student3 : aaa001 -> Martin Schmidt <br />
                   Login Student4 : aaa014 -> Bob McCabe <br />
                   <br/>
                   Login Angestellter: naa001 -> Maria Albricht <br />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
  <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>-->
  <script src="<c:url value="/resources/js/jquery.min.js" />" ></script>
  <!--<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>-->
    <script src="<c:url value="/resources/js/angular.min.js" />" ></script>
  <!--<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>-->
    <script src="<c:url value="/resources/js/bootstrap/bootstrap.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/ui-bootstrap.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/ui-bootstrap-tpls.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/ui-bootstrap-tpls.min.js" />" ></script>
  <script src="<c:url value="/resources/global/app/login.js" />" ></script>
  <script src="<c:url value="/resources/global/controllers/LoginController.js" />" ></script>
  <script src="<c:url value="/resources/global/services/database/DBLoginService.js" />" ></script>
</html>
