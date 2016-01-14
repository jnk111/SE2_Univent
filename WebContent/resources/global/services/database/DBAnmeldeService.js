/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBAnmeldeService = function($http, DBErrorService, URLService) {


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


      function Uhrzeit(stunden, minuten){
        this.stunden = stunden,
        this.minuten = minuten
        this.string = function(){

          var stunde = "";
          var minute = "";
          if(this.stunden < 10){
            stunde += "0" + this.stunden;
          }else{
            stunde += this.stunden;
          }
          if(this.minuten < 10){
            minute += "0" + this.minuten;
          }else{
            minute += this.minuten;
          }

          return stunde + ":" + minute;
        }
      }

      Date.prototype.addDays = function(days){
        var dat = new Date(this.valueOf());
        dat.setDate(dat.getDate() + days);
        return dat;
      }


    // Locals
    //#######################################################################
    var url = URLService.getUrl();

    var uzRegex = /^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
    var datumRegex = /^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[012])\.(19|20)\d\d$/;


    var prakt = {};
    var wp = {};
    var po = {};
    // #######################################################################

    var init = false;

    var initTermine = function(){

      if(!init){
        init = true;
        $http.get(url+"holeAnmeldefristen").
        // Funktion, falls gültige Daten zurückkommen

          then(function(response) {

            var data = response.data;

            for(var i = 0; i < data.length; i++){

              var termin = data[i];

              if(termin.vTyp == "Praktikum"){
                if(termin.aTyp == "Team"){
                  prakt.datumStart = new Date(termin.dStart);
                  prakt.datumEnde = new Date(termin.dEnde);
                  prakt.uzStart = new Uhrzeit(termin.uzStart.stunden, termin.uzStart.minuten).string();
                  prakt.uzEnde = new Uhrzeit(termin.uzEnde.stunden, termin.uzEnde.minuten).string();
                }else if(termin.aTyp = "Einzel"){
                  prakt.datumEinzelStart = new Date(termin.dStart);
                  prakt.datumEinzelEnde = new Date(termin.dEnde);
                  prakt.uzEinzelStart = new Uhrzeit(termin.uzStart.stunden, termin.uzStart.minuten).string();
                  prakt.uzEinzelEnde = new Uhrzeit(termin.uzEnde.stunden, termin.uzEnde.minuten).string();
                }
              }else if(termin.vTyp == "WPP"){
                if(termin.aTyp == "Team"){
                  wp.datumStart = new Date(termin.dStart);
                  wp.datumEnde = new Date(termin.dEnde);
                  wp.uzStart = new Uhrzeit(termin.uzStart.stunden, termin.uzStart.minuten).string();
                  wp.uzEnde = new Uhrzeit(termin.uzEnde.stunden, termin.uzEnde.minuten).string();
                }else if(termin.aTyp = "Einzel"){
                  wp.datumEinzelStart = new Date(termin.dStart);
                  wp.datumEinzelEnde = new Date(termin.dEnde);
                  wp.uzEinzelStart = new Uhrzeit(termin.uzStart.stunden, termin.uzEnde.minuten).string();
                  wp.uzEinzelEnde = new Uhrzeit(termin.uzEnde.stunden, termin.uzEnde.minuten).string();
                }
              }else if(termin.vTyp == "PO"){

                if(termin.aTyp == "Team"){
                  po.datumStart = new Date(termin.dStart);
                  po.datumEnde = new Date(termin.dEnde);
                  po.uzStart = new Uhrzeit(termin.uzStart.stunden, termin.uzStart.minuten).string();
                  po.uzEnde = new Uhrzeit(termin.uzEnde.stunden, termin.uzEnde.minuten).string();
                }else if(termin.aTyp = "Einzel"){
                  po.datumEinzelStart = new Date(termin.dStart);
                  po.datumEinzelEnde = new Date(termin.dEnde);
                  po.uzEinzelStart = new Uhrzeit(termin.uzStart.stunden, termin.uzStart.minuten).string();
                  po.uzEinzelEnde = new Uhrzeit(termin.uzEnde.stunden, termin.uzEnde.minuten).string();
                }
              }
            }

          },

          // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);
          });
              // url = gruppErstellen

      }





    }

    // Preconditions
    // #######################################################################
    var istGueltigeUhrzeitEingabe = function(uhrzeit){
      return uzRegex.test(uhrzeit);
    }

    var istGueltigeDatumEingabe = function(datum){

      return datum instanceof Date
    }


    var istGueltigerZeitRaumDatum = function(start, ende){

      if(istGueltigeDatumEingabe(start)
          && istGueltigeDatumEingabe(ende)) {

            if (start.getMonth() == ende.getMonth()) {
                return start.getDate() < ende.getDate()
                        && start.getTime() < ende.getTime();
            }
            return start.getTime() < ende.getTime();
        }

        return false;
    }

    var istGueltigerZeitRaumEinzelDatum = function(datumEinzelStart, datumEnde){

      if(istGueltigeDatumEingabe(datumEinzelStart)
          && istGueltigeDatumEingabe(datumEnde)) {

            if (datumEinzelStart.getMonth() == datumEnde.getMonth()) {
                return datumEinzelStart.getDate() >= datumEnde.getDate()
                        && datumEinzelStart.getTime() >= datumEnde.getTime();
            }
            return datumEinzelStart.getTime() >= datumEnde.getTime();
        }

        return false;
    }

    var istGueltigeStartUhrzeitEinzel = function(datumEinzelStart, datumEnde, uzStart, uzEnde){

      if(uzRegex.test(uzStart)
          && uzRegex.test(uzEnde)
            && istGueltigeDatumEingabe(datumEinzelStart)
             && istGueltigeDatumEingabe(datumEnde)) {

            if (datumEinzelStart.getMonth() == datumEnde.getMonth()
                && datumEinzelStart.getDate() == datumEnde.getDate()) {

                  var hStart = parseInt(uzStart.split(":")[0]);
                  var mStart = parseInt(uzStart.split(":")[1]);

                  var hEnde = parseInt(uzEnde.split(":")[0]);
                  var mEnde = parseInt(uzEnde.split(":")[1]);

                  if(hStart === hEnde){
                    return mStart >= mEnde;
                  }else{
                    return hStart > hEnde;
                  }
            }else{
              return true;
            }
        }
        return false;
    }

    var pruefeAnmeldePhase = function(today, datum){

      if(istGueltigeDatumEingabe(datum)
          && istGueltigeDatumEingabe(today)){
            return today.getTime() >= datum.getTime();
      }
      return false;

      /*
      if(today.getMonth() == datum.getMonth()){
        return today.getDate() >= datum.getDate();
      }
      return today.getMonth() > datum.getMonth() || today.getDate() >= datum.getDate();
      */
    }



    // #######################################################################


    // Schnittstelle - DB-Operationen
    // #######################################################################




    var speicherePraktikaTeamAnmeldeFrist = function(datumStart, datumEnde, uzStart, uzEnde){

      return speichereAnmeldeFrist("Praktikum", "Team", datumStart, datumEnde, uzStart, uzEnde);
    }

    var speichereWPTeamAnmeldeFrist = function(datumStart, datumEnde, uzStart, uzEnde){

      return speichereAnmeldeFrist("WPP", "Team", datumStart, datumEnde, uzStart, uzEnde);
    }

    var speicherePOTeamAnmeldeFrist = function(datumStart, datumEnde, uzStart, uzEnde){
      return speichereAnmeldeFrist("PO", "Team", datumStart, datumEnde, uzStart, uzEnde);

    }

    var speicherePraktikaEinzelAnmeldeFrist = function(datumStart, datumEnde, uzStart, uzEnde){

      return speichereAnmeldeFrist("Praktikum", "Einzel", datumStart, datumEnde, uzStart, uzEnde);

    }

    var speichereWPEinzelAnmeldeFrist = function(datumStart, datumEnde, uzStart, uzEnde){
      return speichereAnmeldeFrist("WPP", "Einzel", datumStart, datumEnde, uzStart, uzEnde);

    }

    var speicherePOEinzelAnmeldeFrist = function(datumStart, datumEnde, uzStart, uzEnde){

      return speichereAnmeldeFrist("PO", "Einzel", datumStart, datumEnde, uzStart, uzEnde);

    }


    var speichereAnmeldeFrist = function(vTyp, aTyp, datumStart, datumEnde, uzStart, uzEnde){

      var args = [vTyp, aTyp, datumStart.getTime(), datumEnde.getTime(), uzStart, uzEnde];
      var success = false;

      $http.post(url+"speichereAnmeldefristen", angular.toJson(args)).
        // Funktion, falls gültige Daten zurückkommen

      then(function(response) {

        DBErrorService.setError(response.data);
        console.log(response.data);

        },

        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });
      // url = gruppErstellen
      return true;

    }


    var getPrakTermine = function(){

      return prakt;
    }

    var getWPTermine = function(){

      return wp;
    }

    var getPOTermine = function(){

      return po;
    }


    // #######################################################################


    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {

      istGueltigeUhrzeitEingabe: istGueltigeUhrzeitEingabe,
      istGueltigeDatumEingabe: istGueltigeDatumEingabe,
      istGueltigerZeitRaumDatum: istGueltigerZeitRaumDatum,
      istGueltigerZeitRaumEinzelDatum,  istGueltigerZeitRaumEinzelDatum,
      istGueltigeStartUhrzeitEinzel: istGueltigeStartUhrzeitEinzel,
      pruefeAnmeldePhase: pruefeAnmeldePhase,
      speicherePraktikaTeamAnmeldeFrist: speicherePraktikaTeamAnmeldeFrist,
      speichereWPTeamAnmeldeFrist, speichereWPTeamAnmeldeFrist,
      speicherePOTeamAnmeldeFrist: speicherePOTeamAnmeldeFrist,
      speicherePraktikaEinzelAnmeldeFrist: speicherePraktikaEinzelAnmeldeFrist,
      speichereWPEinzelAnmeldeFrist: speichereWPEinzelAnmeldeFrist,
      speicherePOEinzelAnmeldeFrist: speicherePOEinzelAnmeldeFrist,
      initTermine: initTermine,
      getPrakTermine: getPrakTermine,
      getWPTermine: getWPTermine,
      getPOTermine: getPOTermine

    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBAnmeldeService", DBAnmeldeService);

  // Code sofort ausführen
}());
