/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBDatenImExService = function($http, DBErrorService, URLService) {

    var url = URLService.getUrl();
    //var url = "http://localhost:8080/SE2-Praktikumssoftware/"; // URL um Backend anzusprechen
    var dateien = [];
    var dateienImp = [];
    var dateienEx = [];

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

    var initBackupDateien = function(){

      dateien = [];
      $http.get(url+"getfiles").
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;
        for (i = 0; i < data.length; i++) {

          var datei = data[i].split("_");
          var date = parseInt(datei[1]);
          dateien.push(new Date(date));
        }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }


    var initDateienImp = function(){

      dateienImp = [];
      $http.get(url+"getfilesImp").
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;
        for (i = 0; i < data.length; i++) {
          dateienImp.push(data[i]);
        }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }

    var initDateienEx = function(){

      dateienEx = [];
      $http.get(url+"getfilesEx").
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;
        for (i = 0; i < data.length; i++) {

          var datei = data[i].split("_");
          var date = parseInt(datei[1]);
          dateienEx.push(new Date(date).getString() + " - " + datei[2]);
        }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }

    var stelleBackupWiederHer = function(index){

      var bkp = dateien[index];
      var folder = "bkp_" + dateien[index].getTime();

      $http.post(url+"wiederherstellen", folder).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;

        DBErrorService.setError(response.data);
        console.log(response.data);
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });



    }

    var exportiereDaten = function(daten){

      $http.get(url+daten).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;

        DBErrorService.setError(response.data);
        console.log(response.data);
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }
    var getDateienImp = function(){

      return dateienImp;
    }


    var exportiere = function(index){


      if(index === 0){
        exportiereDaten("exp_professoren");
      }else if(index === 1){
        exportiereDaten("exp_studenten");
      }else if(index === 2){
        exportiereDaten("exp_leistungen");
      }else if(index === 3){
        exportiereDaten("vabasisdatenexport");
      }else if(index === 4){
        exportiereDaten("vadatengruppexport");
      }else if(index === 5){
        exportiereDaten("vadatengruppteamexport");
      }
    }

    var importiere = function(index){

      var datei = dateienImp[index];
      var subUrl = "";
      if(datei.indexOf("assistenten") > -1){

        subUrl = "imp_assistenten";

      }else if(datei.indexOf("professoren") > -1){
        subUrl = "imp_professoren";
      }else if(datei.indexOf("veranstaltungen") > -1){
        subUrl = "imp_vabasisdaten";
      }else if(datei.indexOf("gruppen") > -1){
        subUrl = "imp_vagruppdaten";
      }else if(datei.indexOf("studenten") > -1){
        subUrl = "imp_studenten";
      }

      $http.post(url+subUrl, datei).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;


        DBErrorService.setError(response.data);
        console.log(response.data);
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }

    var bkp = function(){

    }

    var sichereDaten = function(){

      $http.get(url+"backup").
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;

        DBErrorService.setError(response.data);
        console.log(response.data);

        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });
    }

    var getDateienEx = function(){

      return dateienEx;
    }

    var getDateien = function(){

      return dateien;
    }
    return {

      initBackupDateien: initBackupDateien,
      getDateien: getDateien,
      stelleBackupWiederHer: stelleBackupWiederHer,
      sichereDaten: sichereDaten,
      getDateienImp, getDateienImp,
      initDateienImp: initDateienImp,
      exportiere: exportiere,
      getDateienEx: getDateienEx,
      initDateienEx: initDateienEx,
      importiere: importiere

    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBDatenImExService", DBDatenImExService);

  // Code sofort ausführen
}());
