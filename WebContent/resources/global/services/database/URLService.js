/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var URLService = function() {


   //var url = "http://46.101.189.85:8080/SE2-Praktikumssoftware/"; // URL um Backend anzusprechen
    var url = "http://localhost:8080/SE2-Praktikumssoftware/"; // URL um Backend anzusprechen

    var getUrl = function(){

      return url;
    }

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {

      getUrl: getUrl

    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("URLService", URLService);

  // Code sofort ausführen
}());
