/**
 * Managed die Extensions der einzelnen Tabs
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var ErrorController = function($scope, DBErrorService) {


    $scope.isError = function(){

        return DBErrorService.isError();
    }

    $scope.setError = function(bool){

      DBErrorService.setError(bool);
      $('#modalError').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }



  };

  // Controller bei der App "anmelden"
  app.controller("ErrorController", ErrorController);

  // Code sofort ausführen
}());
