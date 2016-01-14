/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBLeistService = function($http, DBErrorService, URLService, DBProfileService) {


    // Locals
    //#######################################################################
    var leistungen = []; // Ersetzt später hcLeistungen
    var url = URLService.getUrl();
    // #######################################################################

    // POJO Klassen
    // #######################################################################
    function TeamID(id){
    	this.id = id;
    }

    function Team(teamId, minTeiln, maxTeiln, mitglieder){
    	this.teamId = null;
    	this.minTeiln = minTeiln;
    	this.maxTeiln = maxTeiln;
    	this.mitglieder = mitglieder;
    }

    function MatrikelNr(matrNr){
    	this.matrNr = matrNr;
    }

    function Student(type, matrNr, vorname, nachname){

      this.type = type;
    	this.matrNr = matrNr;
    	this.vorname = vorname;
    	this.nachname = nachname;
    	this.vollerName = this.vorname +" "+ this.nachname;
    	this.benutzername = null;
    	this.gebDatum = null;
    	this.gebOrt = null;
    	this.Adresse = null;
    	this.department = null;
    	this.fachbereich = null;
    	this.email = null;
    }

    function Angestellter(type, vorname, nachname){

      this.type = type;
      this.maID = null;
      this.vorname = vorname;
      this.nachname = nachname;
      this.vollerName = this.vorname + " " + this.nachname;
      this.benutzername = null;
      this.gebDatum = null;
      this. gebOrt = null;
      this.adresse = null;
      this.department = null;
      this.fachbereich = null;
      this.email = null;
    }

    function Note(note){
      this.note = note;
    }

    function Leistung(fach, student,
                      professor, datumPVL,
                      datumNote, pvl, note){

      this.fach = fach;
      this.student = student;
      this.professor = professor;
      this.datumPVL = datumPVL;
      this.datumNote = datumNote;
      this.pvl = pvl;
      this.note = note;

    }

    function Fach(fachKuerzel){

      this.fachKuerzel = fachKuerzel;
      this.fachBezeichnung = null;
      this.fachBereich = null;
      this.semester = null;
    }


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


    var initLeistungen = function(){

      leistungen = [];
      var user = DBProfileService.getCurrUser();
      $http.get(url+"leistungenStudent/" + user.matrNr.matrNr).

      then(function(response) {
          var data = response.data;
          for (i = 0; i < response.data.length; i++) {

            var leistung = response.data[i];

            if(leistung.pvl){
              leistung.pvl = "erfolgreich";
            }else{
              leistung.pvl = "nicht erfolgreich";
            }

            leistung.datumPVL = new Date(leistung.datumPVL).getString();
            leistung.datumNote = new Date(leistung.datumNote).getString();
            leistungen.push(response.data[i]);
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
      });
    }

    var getLeistungen = function(){

      return leistungen;
    }


    // #########################################################################################################################

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {

      initLeistungen: initLeistungen,
      getLeistungen: getLeistungen

    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBLeistService", DBLeistService);

  // Code sofort ausführen
}());
