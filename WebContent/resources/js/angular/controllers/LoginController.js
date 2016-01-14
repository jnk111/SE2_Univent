/**
 * Managed den Loginvorgang
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Login");

  var LoginController = function($scope, DBLoginService) {

	  // Felder
    // #########################################################################
    $scope.username;	// Username
    $scope.passwort; // Pass
    $scope.fail = false;
    // #########################################################################

    // Button-klick-Funktionen
    // #########################################################################

    // User einloggen
    $scope.login = function(){

      if(!DBLoginService.einloggen($scope.username, $scope.passwort))
      {
    	  $scope.fail = true;
      }else{
    	  $scope.fail = false;
      }
    }

    // #########################################################################

  };

  // Controller bei der App "anmelden"
  app.controller("LoginController", LoginController);

  // Code sofort ausführen
}());
