/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBLeistService = function($http) {


    // Locals
    //#######################################################################
    var leistungen = []; // Ersetzt später hcLeistungen
    var noten = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];
    var pvls = ["erfolgreich", "nicht erfolgreich", "offen"];
    var faecher = ["GKAP", "ADP", "BWP", "SEP", "BSP"]; // zZ noch hardcoded -> Später Datenbank
    var gruppen = [1, 2, 3, 4, 5, 6]; // zZ noch hardcoded -> Später Datenbank
    var semester = [1, 2, 3, 4, 5, 6]; // zZ noch hardcoded -> Später Datenbank
    var fachbereiche = ["AI", "WI", "TI"]; // zZ noch hardcoded -> Später Datenbank
    var typen = ["Praktikum", "WP", "PO"];
    var grpNr = 2;
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

    // ##########################################################################

    // Vorerst Hardcoded Daten zur Demonstration
    // Später dynamische Ermittlung durch Spring MVC
    // ==========================================================================

    var prof1 = new Angestellter("professor", "Julia", "Padberg");
    var prof2 = new Angestellter("professor", "Martin", "Huebner");
    var prof3 = new Angestellter("professor", "Martin", "Kleine");
    var prof4 = new Angestellter("professor", "Wolfgang", "Gerken");

    var matr1 = new MatrikelNr(243441);
    var matr2 = new MatrikelNr(243442);
    var matr3 = new MatrikelNr(243443);
    var matr4 = new MatrikelNr(243444);
    var matr5 = new MatrikelNr(243445);
    var matr6 = new MatrikelNr(243446);

    var student1 = new Student("student", matr1, "Max", "Mustermann1");
    var student2 = new Student("student", matr2, "Max", "Mustermann2");
    var student3 = new Student("student", matr3, "Max", "Mustermann3");
    var student4 = new Student("student", matr4, "Max", "Mustermann4");
    var student5 = new Student("student", matr5, "Max", "Mustermann5");
    var student6 = new Student("student", matr6, "Max", "Mustermann6");

    var datePVL1 = new Date();
    var datePVL2 = new Date();

    datePVL2.setDate(datePVL1.getDate() - 7);

    var datePVL3 = new Date();
    datePVL3.setDate(datePVL2.getDate() - 7);

    var datePVL4 = new Date();
    datePVL4.setDate(datePVL3.getDate() - 7);

    var datePVL5 = new Date();
    datePVL5.setDate(datePVL4.getDate() - 7);

    var datePVL6 = new Date();
    datePVL6.setDate(datePVL5.getDate() - 7);

    var dateNote1 = new Date();
    var dateNote2 = new Date();
    dateNote2.setDate(dateNote1.getDate() - 7);

    var dateNote3 = new Date();
    dateNote3.setDate(dateNote2.getDate() - 7);

    var dateNote4 = new Date();
    dateNote4.setDate(dateNote3.getDate() - 7);

    var dateNote5 = new Date();
    dateNote5.setDate(dateNote4.getDate() - 7);

    var dateNote6 = new Date();
    dateNote6.setDate(dateNote5.getDate() - 7);

    var note1 = new Note(3);
    var note2 = new Note(4);
    var note3 = new Note(-1);
    var note4 = new Note(11);
    var note5 = new Note(12);
    var note6 = new Note(15);

    var fach1 = new Fach("GKA");
    var fach2 = new Fach("AD");
    var fach3 = new Fach("BS");
    var fach4 = new Fach("BW");

    var leistung1 = new Leistung(fach1, student1, null, datePVL1, dateNote1, false, note1);
    var leistung2 = new Leistung(fach1, student2, prof1, datePVL2, dateNote2, true, note2);
    var leistung3 = new Leistung(fach2, student3, prof3, datePVL3, dateNote3, true, note3);
    var leistung4 = new Leistung(fach3, student4, prof2, datePVL4, dateNote4, false, note4);
    var leistung5 = new Leistung(fach4, student5, prof4, datePVL5, dateNote5, true, note5);
    var leistung6 = new Leistung(fach2, student6, null, datePVL2, dateNote6, false, note6);

    var hcLeistungen = [leistung1, leistung2, leistung3, leistung4, leistung5, leistung6];

    // ==========================================================================


    // ##########################################################################

    // Helper
    // ##########################################################################

    var sucheLeistung = function(matrNr){

    	for(var i = 0; i < hcLeistungen.length; i++){
    		if(hcLeistungen[i].student.matrNr == matrNr){
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

    // ermittelt alle Faecher (Fachkuerzel) aus der Datenbank, für die Parameter und listet sie im Dropdownmenue
    var updateFaecher = function(typ, fachbereich, semester){

    	// url = fachkuerzel

    	return true;
    }

  // ermittelt alle Gruppennummern  aus der Datenbank für die Parameter und listet sie im Dropdownmenue
    var gruppennummernDropDown = function(fachkuerzel, gruppennummer){

    	// url= grNummern

    	return true;
    }

    // Ermittelt alle Leistungen aus der Datenbank
    var zeigeLeistungenAn = function(gruppennummer, fachkuerzel){

    	var args = [fachkuerzel, grpNr]; // Json
    	// url = leistungen

    	return true;
    }

    // Bewertet eine Leistung mit Note in der Datenbank
    var trageNoteEinDB = function(leistung){

    	return true;

      // url = leistungEintragen
    }

    // Bewertet eine Leistung mit PVL in der Datenbank
    var tragePVLEinDB = function(leistung){

      // url = leistungEintragen
      return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    var trageNoteEin = function(index, datum, note){

      var leistung = hcLeistungen[index];
      leistung.datumNote = datum;
      leistung.note = new Note(note);

      if(!trageNoteEinDB(leistung)){
        return false;
      }else{
        hcLeistungen[index] = leistung;
      }

    }

    var tragePVLEin = function(index, datum, pvl){


      var leistung = hcLeistungen[index];
      console.log("Vorher: "+leistung.pvl)
      leistung.datumPVL = datum;
      leistung.pvl = pvl;

      console.log("Nachher: "+leistung.pvl)



      if(!tragePVLEinDB(leistung)){
        return false;
      }else{
        hcLeistungen[index] = leistung;
      }

    }

    // #########################################################################################################################

    var noten = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];
    var pvls = ["erfolgreich", "nicht erfolgreich", "offen"];
    var faecher = ["GKAP", "ADP", "BWP", "SEP", "BSP"]; // zZ noch hardcoded -> Später Datenbank
    var gruppen = [1, 2, 3, 4, 5, 6]; // zZ noch hardcoded -> Später Datenbank
    var semester = [1, 2, 3, 4, 5, 6]; // zZ noch hardcoded -> Später Datenbank
    var fachbereiche = ["AI", "WI", "TI"]; // zZ noch hardcoded -> Später Datenbank
    var typen = ["Praktikum", "WP", "PO"];

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {
      hcLeistungen: hcLeistungen,
      sucheLeistung: sucheLeistung,
      grpNr: grpNr,
      trageNoteEin: trageNoteEin,
      tragePVLEin: tragePVLEin,
      noten: noten,
      pvls: pvls,
      faecher: faecher,
      gruppen: gruppen,
      semester: semester,
      fachbereiche: fachbereiche,
      typen: typen
    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBLeistService", DBLeistService);

  // Code sofort ausführen
}());
