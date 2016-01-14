/**
 * Managed die Veranstaltungen
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object, $http: holt JSON Daten via SpringMVC Backend -> folgt später
  var VeranstaltungsController = function($scope, autoscroller, DBVeranstService, DBGruppService /*, $http*/ ) {



    $scope.va = {};
    $scope.va.edit = {};
    $scope.va.erst = {};
    $scope.fachbereiche = ["WI", "AI", "TI"];
    $scope.semeter = [1, 2, 3, 4, 5, 6];

    $scope.va.m.fachbereich = $scope.fachbereiche[0];
    $scope.va.m.semester = $scope.semester[0];

    $scope.getVeranstaltungen = function(){

      return DBVeranstService.getVeranstaltungen();
    }
  };

  // Controller bei der App "anmelden"
  app.controller("VeranstaltungsController", VeranstaltungsController);

  // Code sofort ausführen
}());
