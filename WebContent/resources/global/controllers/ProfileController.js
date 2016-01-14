/**
 * Managed den Loginvorgang
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var ProfileController = function($scope, DBProfileService) {

    $scope.user = DBProfileService.getCurrUser();
    $scope.getCurrUser = function(){
      $scope.user = DBProfileService.getCurrUser();
      return $scope.user;
    }

    /**
     * Holt mithilfe des Backends die Profilinformationen des
     * aktuellen Users und zeigt diese auf der View an.
     * 
     */
    $scope.initProfile = function(){
      DBProfileService.initUser();
    }

    /**
     * Loggt einen User aus. ruft dazu die entsprechende Schnittstelle
     * am Backend auf.
     * Laedt anschließend den Loginscreen.
     */
    $scope.logout = function(){

      DBProfileService.logout();
    }


  };

  // Controller bei der App "anmelden"
  app.controller("ProfileController", ProfileController);

  // Code sofort ausführen
}());
