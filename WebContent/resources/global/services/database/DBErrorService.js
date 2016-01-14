/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBErrorService = function($http) {


    var error = false;

    // #######################################################################

    var isError = function(){

      return error;
    }

    var setError = function(bool){

      if(bool == false){

        error = true;
      }else{
        error = false;
      }
    }

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {

      isError: isError,
      setError: setError,


    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBErrorService", DBErrorService);

  // Code sofort ausführen
}());
