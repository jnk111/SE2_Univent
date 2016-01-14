/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBNewsService = function($http, DBErrorService, URLService) {

    // Locals
    // #########################################################################
    //var url = "http://46.101.189.85:8080/SE2-Praktikumssoftware/"; // URL um Backend anzusprechen
    var url = URLService.getUrl();
    var header = "";
    var body = "";
    // #########################################################################

    // SCHNITTSTELLE
   // #########################################################################

   var getHeader = function(){

     return header;

   }

   var getBody = function(){
     return body;

   }

   var initNews = function(){
     $http.get(url+"holeNews").
      then(function(response){;

        var data = response.data;

        header = data[0];
        body = data[1];

      },
      // Funktion bei Fehler
      function(response){
        DBErrorService.setError(false);
      });

   }

   var speichereHeader = function(head){

     $http.post(url+"newsHeader", head).
    // Funktion, falls gültige Daten zurückkommen
      then(function(response){;

        DBErrorService.setError(response.data);
        console.log(response.data);
      },
      // Funktion bei Fehler
      function(response){
        DBErrorService.setError(false);
      });
   }


   var speichereBody = function(body){

     $http.post(url+"newsBody", body).
    // Funktion, falls gültige Daten zurückkommen
      then(function(response){;
        DBErrorService.setError(response.data);
        console.log(response.data);
      },
      // Funktion bei Fehler
      function(response){
        DBErrorService.setError(false);
      });
   }

   var speichereNews = function(head, bod){


      speichereHeader(head);
      speichereBody(bod);


   }



 // #########################################################################

    // Gebe dieses Objekt und mache die Methoden sichtbar
    return {

      getHeader: getHeader,
      getBody: getBody,
      speichereNews: speichereNews,
      initNews: initNews
    }

  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBNewsService", DBNewsService);

  // Code sofort ausführen
}());
