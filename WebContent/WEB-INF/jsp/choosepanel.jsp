<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html ng-app="SE2-Software">
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
<body ng-controller="TabController">
  <div class="container">
    <div class="row">
      <div class="col-sm-12">
        <br />
      </div>
    </div>
    <div class="row row-centered">
      <div class="col-sm-12 col-centered">
        <h1 class="text-muted">SE2-Praktikumssoftware</h1>
      </div>
    </div>
    <div class="row row-centered">
      <div class="col-sm-12 col-centered">
        <h4 class="text-muted">Prototyp - Demo</h4>
      </div>
    </div>
    <div class="row row-centered">
      <div class="col-sm-12 col-centered">
        <br />
      </div>
    </div>
    <div class="row row-centered">
      <div class="col-sm-12 col-centered">
        <a href="angestellter" class="btn btn-my-default">Mitarbeiteroberflaeche</a>
        <a href="student" class="btn btn-my-default">Studentenoberfaeche</a>
      </div>
    </div>
  </div>
    <!--Contentrow end-->
  </div>
</body>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="<c:url value="/resources/js/bootstrap/bootstrap.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/bootstrap-select.min.js" />" ></script>
  <script src="<c:url value="/resources/js/bootstrap/bootstrap-select.js" />" ></script>
</html>
