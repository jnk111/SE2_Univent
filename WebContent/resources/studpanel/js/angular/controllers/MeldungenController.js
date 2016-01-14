/**
 * Managed die Popups
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope: Model Object
  var MeldungenController = function($scope, DBMeldungenService, DBProfileService) {


    $scope.init = false;

    $scope.getMeldungen = function(){

      return DBMeldungenService.getMeldungen();
    }

    /**
     * Gibt die Anzahl der aktuell vorhanden Meldungen zurueck
     * und zeigt diese auf der View an.
     */
    $scope.getAnzMeldungen = function(){


      return DBMeldungenService.getMeldungen().length;
    }

    /**
     * Ruft die Backendschnittstelle zum Meldungen initialsieren
     * auf und aktualisiert die View, sofern neue Meldungen
     * zwischenzeitlich eingetroffen sind.
     */
    $scope.aktualisiere = function(){

      var user = DBProfileService.getCurrUser();
      DBMeldungenService.initMeldungen(user.matrNr.matrNr);
    }

    $scope.einladungAblehnen = function(index){


    }

  };

  // Controller bei der App "anmelden"
  app.controller("MeldungenController", MeldungenController);

  // Code sofort ausführen
}());
