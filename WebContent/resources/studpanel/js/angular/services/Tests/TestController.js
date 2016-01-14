// Test-Pattern
/**
 * Managed die einzelnen Tabs
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var TestController = function($scope, UeberschneiderTest) {


    $scope.ueberschneiderTestErgebnisse = [];

    $scope.testeUeberSchneider = function(){

      UeberschneiderTest.starteTestRoutine();
      $scope.ueberschneiderTestErgebnisse = UeberschneiderTest.getTestErgebnisse();




    }



  };

  // Controller bei der app "anmelden"
  app.controller("TestController", TestController);

  // Code sofort ausführen
}());
