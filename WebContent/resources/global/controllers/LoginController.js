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

    /**
     * Loggt einen User ein. ruft dazu die entsprechende Schnittstelle
     * am Backend auf. Laedt anschließend das entsprechende Panel.
     */
    $scope.login = function(){

      if(!DBLoginService.einloggen($scope.username, $scope.passwort))
      {
    	  $scope.fail = true;
      }else{
    	  $scope.fail = false;
      }
    }

    /**
     * Loggt einen User aus. ruft dazu die entsprechende Schnittstelle
     * am Backend auf.
     * Laedt anschließend den Loginscreen.
     */
    $scope.logout = function(){

      DBLoginService.logout();
    }

    // #########################################################################

  };

  // Controller bei der App "anmelden"
  app.controller("LoginController", LoginController);

  // Code sofort ausführen
}());
