/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBLeistService = function($http, DBErrorService, URLService) {


    // Locals
    //#######################################################################
    var leistungen = []; // Ersetzt später hcLeistungen
    var noten = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];
    var pvls = ["erfolgreich", "nicht erfolgreich", "offen"];
    var faecher = []; // zZ noch hardcoded -> Später Datenbank
    var gruppen = [1]; // zZ noch hardcoded -> Später Datenbank
    var semester = [1, 2, 3, 4, 5, 6]; // zZ noch hardcoded -> Später Datenbank
    var fachbereiche = ["AI", "WI", "TI"]; // zZ noch hardcoded -> Später Datenbank
    var typen = ["Praktikum", "WPP", "PO"];
    var grpNr = 2;
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


    // ##########################################################################



    // ##########################################################################

    // Helper
    // ##########################################################################

    var sucheLeistung = function(matrNr){

    	for(var i = 0; i < leistungen.length; i++){
    		if(leistungen[i].student.matrNr.matrNr === matrNr.matrNr){
    			return i;
    		}
    	}
    	return -1;
    }
    // ##########################################################################


    // SCHNITTSTELLE
    // ################################################################################################################

    // TODO: Diese Methoden müssen implementiert werden.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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


    // ermittelt alle Gruppennummern  aus der Datenbank für die Parameter und listet sie im Dropdownmenue
      var holeGruppenNummern = function(fachkuerzel){

        gruppen = [1];
        $http.post(url+"grNummern", fachkuerzel).
          // Funktion, falls gültige Daten zurückkommen
          then(function(response) {

            // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
            var data = response.data;
            for (var i = 0; i < data.length; i++) {
              gruppen.push(parseInt(data[i]));

            }

            zeigeLeistungenAn(gruppen[0], fachkuerzel);
          },
          // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);
          });

        return true;
      }

    // ermittelt alle Faecher (Fachkuerzel) aus der Datenbank, für die Parameter und listet sie im Dropdownmenue
    var updateFaecher = function(fachbereich, typ, semester){

      faecher = [];
      var json = angular.toJson([fachbereich, typ, semester]);
      $http.post(url+"fachkuerzel", json).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          var data = response.data;
          for (i = 0; i < data.length; i++) {
            faecher.push(data[i]);
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

      return faecher;
    }


    var initLeistungenFuerFach = function(fachkuerzel){

      leistungen = [];
      $http.post(url+"leistungen", fachkuerzel).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
        var data = response.data;
        for (i = 0; i < data.length; i++) {
          var datumNote = null;
          var datumPVL = null;
          var note = data[i].note;
          var pvl = data[i].pvl;

          if(data[i].datumNote != null){
            datumNote = new Date(data[i].datumNote);

          }else{
            note.note = -1;
          }

          if(data[i].datumPVL != null){
            datumPVL = new Date(data[i].datumPVL);
          }

          var fach = data[i].fach;
          var student = data[i].student;
          var professor = data[i].professor;

          var leist = new Leistung(fach, student, professor,
                                    datumPVL, datumNote, pvl,  note);

          leistungen.push(leist);

        }

        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }
    var getFaecher = function(){

      return faecher;
    }



    var getGruppen = function(){

      return gruppen;
    }


    var getLeistungen = function(){
      return leistungen;
    }



    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    var trageNoteEin = function(index, datum, note){

      var leistung = leistungen[index];
      leistung.datumNote = datum;
      leistung.note = new Note(note);

      $http.post(url+"noteEintragen", angular.toJson(leistung)).
        // Funktion, falls gültige Daten zurückkommen
        then(function(response) {

          DBErrorService.setError(response.data);
          console.log(response.data);

          if(datum != null){

            leistung.datumNote = new Date(datum.getTime());
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });



    }

    var tragePVLEin = function(index, datum, pvl){


      var leistung = leistungen[index];
      leistung.datumPVL = datum;
      leistung.pvl = pvl;

      $http.post(url+"pvlEintragen", angular.toJson(leistung)).
        // Funktion, falls gültige Daten zurückkommen
        then(function(response) {

          DBErrorService.setError(response.data);
          console.log(response.data);

          if(datum != null){
            leistung.datumPVL = new Date(datum.getTime());
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });


    }

    // #########################################################################################################################

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {
      sucheLeistung: sucheLeistung,
      updateFaecher: updateFaecher,
      getLeistungen: getLeistungen,
      grpNr: grpNr,
      trageNoteEin: trageNoteEin,
      tragePVLEin: tragePVLEin,
      noten: noten,
      pvls: pvls,
      faecher: faecher,
      gruppen: gruppen,
      semester: semester,
      initLeistungenFuerFach: initLeistungenFuerFach,
      fachbereiche: fachbereiche,
      typen: typen,
      getFaecher, getFaecher,
      getGruppen, getGruppen,
      holeGruppenNummern: holeGruppenNummern
    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBLeistService", DBLeistService);

  // Code sofort ausführen
}());
