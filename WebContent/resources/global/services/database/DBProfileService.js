/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBProfileService = function($http, $window, DBErrorService, URLService, DBMeldungenService) {

    // Locals
    // #########################################################################
    var user = {};
    var url = URLService.getUrl();
    var init = false;
    // #########################################################################

    Date.prototype.getWeek = function() {
      var date = new Date(this.getTime());
      date.setHours(0, 0, 0, 0);
      // Thursday in current week decides the year.
      date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
      // January 4 is always in week 1.
      var week1 = new Date(date.getFullYear(), 0, 4);
      // Adjust to Thursday in week 1 and count number of weeks from date to week1.
      return 1 + Math.round(((date.getTime() - week1.getTime()) / 86400000 - 3 + (week1.getDay() + 6) % 7) / 7);
    }

    Date.prototype.getString = function() {
        var day = this.getDate();
        var month = this.getMonth() + 1;
        var year = this.getFullYear().toString();
        var dateString = "";

        if (day < 10) {
          dateString += "0";
        }
        day = day.toString();
        dateString += day;
        dateString += ".";

        if (month < 10) {
          dateString += "0"
        }
        month = month.toString();
        dateString += month;
        dateString += ".";
        dateString += year;
        return dateString;
      }

    // SCHNITTSTELLE
   // #########################################################################

   var initUser = function(){

     if(!init){
       init = true;
       $http.get(url+"profil").
      // Funktion, falls gültige Daten zurückkommen
        then(function(response){;

          user = response.data;
          user.gebDatum = new Date(user.gebDatum);

          if(user.benutzername != null && user.benutzername.indexOf("aaa") > -1){
            DBMeldungenService.initMeldungen(user.matrNr.matrNr);
          }

        },
        // Funktion bei Fehler
        function(response){
          DBErrorService.setError(false);
        });
     }


   }
   var getCurrUser = function(){


     return user;


   }


   var logout = function(){

     var id = null;
     if(user.benutzername == null){
       id = user.maID.id;
     }else{
       id = user.matrNr.matrNr;
     }
     console.log(url+"logout/" + id);
     $http.get(url+"logout/" + id).
     // Funktion, falls gültige Daten zurückkommen
       then(function(response){;
         $window.location = '/SE2-Praktikumssoftware/';
       },
       // Funktion bei Fehler
       function(response){
         DBErrorService.setError(false);
       });

   }

 // #########################################################################

    // Gebe dieses Objekt und mache die Methoden sichtbar
    return {

      getCurrUser: getCurrUser,
      initUser: initUser,
      logout: logout
    }

  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBProfileService", DBProfileService);

  // Code sofort ausführen
}());
