/**
 * Service
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBGruppService = function($http, DBVeranstService, DBErrorService, URLService) {

    // Locals
    // ########################################################################################################
    var fach; // aktuelles Fach
    var raeume = ["0660", "1080", "0760", "1002"];
    var dozenten = ["Padberg", "Zukunft", "Kleine", "Gerken", "Huebner"];
    var assistenten = [];
    var assistentenString = [];
    var semester = [1,2,3,4,5,6];
    var grNummern = [1,2,3,4,5,6]; // Muss ermittelt werden durch DB
    var va; // Die zugehörige Veranstaltung -> Wird als Paramter übergeben (initGruppen())r
    var currGrpNr = 0;

    var url = URLService.getUrl();
    var gruppen = []; // Hier werden die ermittelten Daten temporär gespeichert um schnelles Anzeigen zu gewährleisten
                              // Ersetzt HC-Gruppendaten
    var error = false; // Flag zur Fehlererkennung

    // #######################################################################################################


    // Benutzerdefinierte Klassen
    // ########################################################################################################

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




    // ###########################################################################################################


    // Helper
    // ###########################################################################################################

    var getGruppen = function(){
      return gruppen;
    }

    var getMaxTeams = function(maxAnzTeiln){

      var anzTeams = maxAnzTeiln / va.teamKap;

    }
    function getVeranstaltung(){
      return va;
    }

    function getFach(){
      return fach;
    }

    function setFach(f){
      fach = f;
    }

    var sucheGruppe = function(grpNr){
      for(i = 0; i < gruppen.length; i++){
        if(gruppen[i].fachkuerzel == fach && gruppen[i].grpNr == grpNr){
          return i;
        }
      }
      return -1;
    }

        // Setzt die Termine nach Erstellung der neuen Gruppe wieder auf die Standardwerte zurück
    function resetAppValues() {

      var term = [];
      var dateTm1 = new Date();
      var dateTm2 = new Date();
      dateTm2.setDate(dateTm1.getDate() + 7);

      var dateTm3 = new Date();
      dateTm3.setDate(dateTm2.getDate() + 7);

      var dateTm4 = new Date();
      dateTm4.setDate(dateTm3.getDate() + 7);

      var tm1 = new Termin(raum1, dateTm1, start1, end1);
      var tm2 = new Termin(raum2, dateTm2, start2, end2);
      var tm3 = new Termin(raum3, dateTm3, start3, end3);
      var tm4 = new Termin(raum4, dateTm4, start1, end1);

      term.push(tm1);
      term.push(tm2);
      term.push(tm3);
      term.push(tm4);

      return term;
    }

    // Erzeugt neue Abgabetermine anhand des Models
    function getAppointments(termine) {
      var appointments = [];
      for (i = 0; i < termine.length; i++) {
        appointments.push(new Termin(termine[i].raum, termine[i].datum, termine[i].start, termine[i].ende));
      }
      return appointments;
    }

    //Abgabetermine zur Stringdarstellung konvertieren
    function toStr(termine) {

      var term = "";
      var i;
      for (i = 0; i < termine.length; i++) {
        term += termine[i].kw;

        if (i < termine.length - 1) {
          term += ", ";
        }
      }
      return term;
    }

    // Generiert einen zufälligen Index für ein Array
    function genRdIndex(arr) {
      var index;
      index = Math.floor(Math.random() * arr.length);
      return index;
    }

    function getDate(date){
      return new Date(date);
    }

    function getAppointment(raum, newAppDate, start, end){
      return new Termin(raum, newAppDate, start, end);
    }

    function initFaecherTabelle(va){
      for(i = 0; i < gruppen.length; i++){
        gruppen[i].fachkuerzel = va.fach.fachKuerzel;
      }
    }

// ###########################################################################################################


// Vorerst Hardcoded Daten zur Demonstration
// Später dynamische Ermittlung durch Spring MVC
// ###########################################################################################################


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

function Termin(raum, datum, start, ende){
  var days = ["So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."];
  this.datum = datum;
  this.start = start;
  this.ende = ende;
  this.kw = datum.getWeek();
  this.raum = raum;
  this.formattedDateString = days[this.datum.getDay()] + ", " + this.datum.getString();
  this.usGruppen = [];
}

function Gruppe(fachkuerzel, grNr,
                termine, prof,
                assist, maxTeams, anzTeams){

  this.fachkuerzel = fachkuerzel;
  this.grpNr = grNr;
  this.termine = termine;
  this.teams = null;
  this.dozent = prof;
  this.assistent = assist;
  this.maxTeams = maxTeams;
  this.anzTeams = anzTeams;
}

function MAID(id){

  this.id = id;

}

function Angestellter(typ, maID, vorname, nachname){

  this.maID = maID;
  this.vorname = vorname;
  this.nachname = nachname;
  this.vollerName = this.vorname + " " + this.nachname;
  this.benutzername = null;
  this.gebDatum = null;
  this.gebOrt = null;
  this.adresse = null;
  this.department = null;
  this.fachbereich = null;
  this.email = null;
}



var start1 = new Uhrzeit(8, 15);
var end1 = new Uhrzeit(11, 30);
var start2 = new Uhrzeit(12, 30);
var end2 = new Uhrzeit(15, 45);
var start3 = new Uhrzeit(16, 30);
var end3 = new Uhrzeit(19, 30);

var timesStart = [start1, start2, start3];
var timesEnd = [end1, end2, end3];


var date1 = new Date();
var date2 = new Date(date1.getTime() + 7*24*60*60*1000);
var date3 = new Date(date2.getTime() + 7*24*60*60*1000);
var date4 = new Date(date3.getTime() + 7*24*60*60*1000);



var raum1 = "0660";
var raum2 = "1080";
var raum3 = "0760";
var raum4 = "1002";

var termin1 = new Termin(raum1, date1, start1, end1);
var termin2 = new Termin(raum2, date2, start2, end2);
var termin3 = new Termin(raum3, date3, start3, end3);
var termin4 = new Termin(raum4, date4, start1, end1);

var prof1 = DBVeranstService.getAngestellter("professor", "Julia Padberg");
var assist1 = DBVeranstService.getAngestellter("assistent", "Gerhard Oelker");

var prof2 = DBVeranstService.getAngestellter("professor","Olaf Zukunft");
var assist2 = DBVeranstService.getAngestellter("assistent","Ilona Blank");

var prof3 = DBVeranstService.getAngestellter("professor","Martin Kleine");
var assist3 = DBVeranstService.getAngestellter("assistent","Ilona Blank");

var termine = [termin1, termin2, termin3, termin4];


var gr1 = new Gruppe("", 1, termine,
           prof1, assist1, 20);

var gr2 = new Gruppe("", 2, termine,
           prof2, assist2, 20);

var gr3 = new Gruppe("", 3, termine,
           prof3, assist3, 20);

var gr4 = new Gruppe("", 4, termine,
           prof1, assist1, 20);

gr1.kw = toStr(gr1.termine);
gr2.kw = toStr(gr2.termine);
gr3.kw = toStr(gr3.termine);
gr4.kw = toStr(gr4.termine);

// Vorhandene Hardcoded Daten (in Tabelle gelistet)
var hcGruppenDaten = [gr1, gr2, gr3, gr4];

// ###########################################################################################################

// SCHNITTSTELLE
// ###########################################################################################################

// TODO: Diese Methoden müssen implementiert werden
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var sucheUhrzeit = function(zeit, typ){

  var times = null;
  if(typ == "start"){

    times = timesStart;

  }else if(typ == "ende"){
    times = timesEnd;
  }

  for(var i = 0; i < times.length; i++){

    var uz = times[i];
    if(zeit.stunden === uz.stunden
        && zeit.minuten === uz.minuten){

          return uz;
    }
  }

  return null;
}

var initAssist = function(){

  assistenten = [];
  assistentenString = [];
  $http.get(url+"assistenten").
    // Funktion, falls gültige Daten zurückkommen
  then(function(response) {

    var data = response.data;
      // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
      for (i = 0; i < response.data.length; i++) {

        //typ, maID, vorname, nachname
        var maid = data[i].maID;
        var vorname = data[i].vorname;
        var nachname = data[i].nachname;
        var prof = new Angestellter("Assistent", maid, vorname, nachname);
        assistenten.push(prof);
        assistentenString.push(prof.maID.id + " " + prof.vorname + " " + prof.nachname);
      }
    },
    // Funktion bei Fehler
    function(response) {
      DBErrorService.setError(false);
    });

}
    // Gruppe zur Datenbank hinzufügen
    // Ruft die Schnittstelle im Backend auf und übergibt die Informationen
    // der Gruppe + Veranstaltung als Json-Array
    var addGruppeDB = function(gruppe){

      // JSOM Data
      $http.post(url+"gruppErstellen", angular.toJson(gruppe)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

      // url = gruppErstellen
      return true;
    }

    // Gruppe in der Datenbank bearbeiten
    // Ruft die Schnittstelle im Backend auf und übergibt die Informationen
    // der Gruppe + Veranstaltung als Json-Array
    // Geänderte gruppe wird automatisch in der View aktualisiert
    var editGruppeDB = function(gruppe){

      var veranstaltung = getVeranstaltung(); // Muss dem Backend mit übergeben werden

      return true;
    }

    // gruppe aus der Datenbank löschen
    // Ruft die Schnittstelle im Backend auf und übergibt die Informationen
    // der Gruppe als Json-Array
    // Gruppe wird automatisch aus der View gelöscht
    var loescheGruppeDB = function(gruppe){


      // url = gruppLoeschen
      return true;
    }

    // Alle Gruppen zu einer Veranstaltung aus der DB ermitteln
    // Ruft die Schnittstelle im Backend auf und holt Die Gruppen
    // zu einer übergebenen Veranstaltung
    // Gruppen müssen in Array "gruppen" gespeichert werden (Json-Format)
    // Werden dann automatisch in der View ausgegeben
    // =========================================================================
    // ACHTUNG: Feld fach für jedes Gruppenobjekt ist kein Feld der Java-Klasse
    // "Gruppe" (Dient hier nur zur Darstellung)
    // =========================================================================



    var professorTest = function(veranst){

      var prof = veranst.professor;
      $http.post(url+"professor", angular.toJson(prof)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {


          }
        },
        // Funktion bei Fehler
        function(response) {

        });

      return true;
    }


    var fachTest = function(veranst){

      var fach = veranst.fach;
      $http.post(url+"fach", angular.toJson(fach)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;

          }
        },
        // Funktion bei Fehler
        function(response) {
          error = true; // Setze error-flag, dass von der view abgefragt wird
        });

      return true;
    }




    var initGruppen = function(veranstaltung){
      va = veranstaltung;
      fach = va.fach.fachKuerzel;
      gruppen = [];
      var json = angular.toJson(va);
      $http.post(url+"gruppUebersicht", json).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;
            var termine = getTermine(data[i].termine);
            var gruppe = new Gruppe(data[i].fachkuerzel, data[i].grpNr, termine, data[i].professor, data[i].assistent, data[i].maxTeams, data[i].anzTeams);
            gruppen.push(gruppe);

          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

      return true;

    }


    var sucheAngestellten = function(maid, profs){

      for(var i = 0; i < profs.length; i++){
        if(maid.id == profs[i].maID.id){
          return i;
        }
      }

      return -1;
    }


    var istGueltigerAssistent = function(expr){

      if(expr != "" && expr != undefined && expr != null && expr.indexOf(" ") > -1)           {

        var maid = expr.split(' ');

        if((maid[0].match("[1-9]+"))){

          var maid = new MAID(parseInt(maid[0]));

          var profIndex = sucheAngestellten(maid, assistenten);
          if(profIndex > -1){
            return assistenten[profIndex];
          }
        }
      }
        return null;
    }

    var getTermine = function(termine){

      var termineNeu = [];
      for(var i = 0; i < termine.length; i++){
        var start = new Uhrzeit(termine[i].start.stunden, termine[i].start.minuten);
        var ende = new Uhrzeit(termine[i].ende.stunden, termine[i].ende.minuten);
        var termin = new Termin(termine[i].raum, new Date(termine[i].datum), start, ende);
        termineNeu.push(termin);
      }

      termineNeu.sort(function(a,b){
        // Turn your strings into dates, and then subtract them
        // to get a value that is either negative, positive, or zero.
        return new Date(a.datum.getTime()) - new Date(b.datum.getTime());
      });

      return termineNeu;
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    var getGrpNr = function(){

      var min = 0;

      for(var i = 0; i < gruppen.length; i++){
        if(gruppen[i].grpNr > min){
          min = gruppen[i].grpNr;
        }
      }

      return min;
    }

    // Gruppe einer Veranstaltung hinufügen
    var addGruppe = function(gruppenInfo){

      var fachkuerzel = fach;
      var grpNr = 1 + getGrpNr();
      var termine = getAppointments(gruppenInfo.termine);
      var dozent = gruppenInfo.dozent;
      var assistent = gruppenInfo.assistent;
      var maxTeams = gruppenInfo.maxTeams;

      var gruppe = new Gruppe(fachkuerzel, grpNr,
    		  				  termine, dozent,
    		  				  assistent, maxTeams);

      gruppe.anzTeams = 0;

      $http.post(url+"gruppErstellen", angular.toJson(gruppe)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {


          DBErrorService.setError(response.data);
          console.log(response.data);
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    gruppen.push(gruppe);

    }

    // gruppe einer Veranstaltung editieren
    var editGruppe = function(index, gruppenInfo){

      var gruppe = gruppen[index];
      var termine = getAppointments(gruppenInfo.termine);
      var dozent = gruppenInfo.dozent;
      var assistent = gruppenInfo.assistent;
      var maxTeams = gruppenInfo.maxTeams;

      gruppe.termine = termine;
      gruppe.dozent = dozent;
      gruppe.assistent = assistent;
      gruppe.maxTeams = maxTeams;

      $http.post(url+"gruppBearbeiten", angular.toJson(gruppe)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {


          DBErrorService.setError(response.data);
          console.log(response.data);
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });



    }

    // Gruppe einer Veranstaltung löschen
    var gruppeLoeschen = function(index){

      var gruppe = gruppen[index];
      gruppen.splice(index, 1);

      $http.post(url+"gruppLoeschen", angular.toJson(gruppe)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {

        DBErrorService.setError(response.data);
        console.log(response.data);
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }

    var getAssistentenString = function(){

      return assistentenString;
    }
    // ###########################################################################################################


    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // ###########################################################################################################
    return {
      resetAppValues: resetAppValues,
      getAppointments: getAppointments,
      getAssistentenString: getAssistentenString,
      toStr:  toStr,
      genRdIndex: genRdIndex,
      timesStart: timesStart,
      timesEnd: timesEnd,
      addGruppe: addGruppe,
      editGruppe: editGruppe,
      initAssit: initAssist,
      raeume: raeume,
      dozenten: dozenten,
      grNummern: grNummern,
      assistenten: assistenten,
      hcGruppenDaten: hcGruppenDaten,
      termine: termine,
      istGueltigerAssistent: istGueltigerAssistent,
      gruppeLoeschen: gruppeLoeschen,
      getDate: getDate,
      getAppointment: getAppointment,
      getFach: getFach,
      setFach: setFach,
      getVeranstaltung: getVeranstaltung,
      initGruppen: initGruppen,
      sucheGruppe: sucheGruppe,
      semester: semester,
      currGrpNr: currGrpNr,
      getGruppen: getGruppen,
      gruppen: gruppen,
      sucheUhrzeit: sucheUhrzeit
    };
    // ###########################################################################################################
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBGruppService", DBGruppService);

  // Code sofort ausführen
}());
