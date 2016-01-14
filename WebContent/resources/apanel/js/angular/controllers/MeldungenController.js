/**
 * Managed die Meldungen
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var MeldungenController = function($scope) {

    $scope.popover = {};
    $scope.popover.template = "resources/apanel/popovers/meldungen.html"
  };
  // Controller bei der App "anmelden"
  app.controller("MeldungenController", MeldungenController);
  // Code sofort ausführen
}());
