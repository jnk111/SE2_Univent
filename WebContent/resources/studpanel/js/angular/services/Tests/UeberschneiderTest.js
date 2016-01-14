

(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var UeberschneiderTest = function(UeberschneidungsPruefer) {


    // Felder
    // #########################################################################
    var testErgebnisse = [];
    // #########################################################################


    // Helper
    // #########################################################################

    var getTestErgebnisse = function(){

      return testErgebnisse;
    }


    // POJO
    // #########################################################################

    // POJO
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

      function MAID(id){

        this.id = id;

      }

    function Angestellter(type, maID, vorname, nachname){

      this.type = type;
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

    function Uhrzeit(stunden, minuten){
      this.stunden = stunden,
      this.minuten = minuten
      this.string = this.stunden+":"+this.minuten;
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

    function Gruppe(fachkuerzel, grNr, termine, prof, assist,
               minTeams, maxTeams,
               resTeams, anzTeams){

      this.fachkuerzel = fachkuerzel;
      this.grpNr = grNr;
      this.termine = termine;
      this.teams = null;
      this.dozent = prof;
      this.assistent = assist;
      this.minTeams = minTeams;
      this.maxTeams = maxTeams;
      this.reservTeams = resTeams;
      this.anzTeams = anzTeams;
      this.termineString = null;
    }

    // #########################################################################



    // TestDaten
    // #########################################################################

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

    var date5 = new Date(date1.getTime() + 15*24*60*60*1000);
    var date6 = new Date(date2.getTime() + 15*24*60*60*1000);
    var date7 = new Date(date3.getTime() + 15*24*60*60*1000);
    var date8 = new Date(date4.getTime() + 15*24*60*60*1000);

    var date9 = new Date(date8.getTime() + 7*24*60*60*1000);
    var date10 = new Date(date9.getTime() + 7*24*60*60*1000);
    var date11 = new Date(date10.getTime() + 7*24*60*60*1000);
    var date12 = new Date(date11.getTime() + 7*24*60*60*1000);

    var raum1 = "0660";
    var raum2 = "1080";
    var raum3 = "0760";
    var raum4 = "1002";

    var termin1 = new Termin(raum1, date1, start1, end1);
    var termin2 = new Termin(raum2, date2, start2, end2);
    var termin3 = new Termin(raum3, date3, start3, end3);
    var termin4 = new Termin(raum4, date4, start1, end1);

    var termin5 = new Termin(raum1, date5, start1, end1);
    var termin6 = new Termin(raum2, date6, start2, end2);
    var termin7 = new Termin(raum3, date7, start3, end3);
    var termin8 = new Termin(raum4, date8, start1, end1);

    var termin9 = new Termin(raum1, date9, start1, end1);
    var termin10 = new Termin(raum2, date10, start2, end2);
    var termin11 = new Termin(raum3, date11, start3, end3);
    var termin12 = new Termin(raum4, date12, start1, end1);

    var termin13 = new Termin(raum4, date1, start1, end1);
    var termin14 = new Termin(raum4, date8, start1, end1);
    var termin15 = new Termin(raum4, date10, start1, end1);
    var termin16 = new Termin(raum4, date12, start1, end1);

    var termin17 = new Termin(raum4, date5, start1, end1);
    var termin18 = new Termin(raum4, date9, start1, end1);
    var termin19 = new Termin(raum4, date10, start1, end1);
    var termin20 = new Termin(raum4, date12, start1, end1);

    var termin21 = new Termin(raum4, date1, start1, end1);
    var termin22 = new Termin(raum4, date5, start1, end1);
    var termin23 = new Termin(raum4, date8, start1, end1);
    var termin24 = new Termin(raum4, date12, start1, end1);


    function Angestellter(type, maID, vorname, nachname){

      this.type = type;
      this.maID = maID;
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

    var typProf = "professor";
    var typAssist = "assistent";

    var prof1 = new Angestellter(typProf, null, "Julia", "Padberg");
    var prof2 = new Angestellter(typProf, null, "Martin", "Kleine");
    var prof3 = new Angestellter(typProf, null, "Wolfgang", "Gerken");
    var prof4 = new Angestellter(typProf, null, "Olaf", "Zukunft");

    var assist1 = new Angestellter(typAssist, null, "Ilona", "Blank");
    var assist2 = new Angestellter(typAssist, null, "Hartmut", "Schulz");
    var assist3 = new Angestellter(typAssist, null, "Gerhard", "Oelker");

    var termine = [termin1, termin2, termin3, termin4];
    var termine2 = [termin5, termin6, termin7, termin8];
    var termine3 = [termin9, termin10, termin11, termin12];
    var termine4 = [termin13, termin14, termin15, termin16];
    var termine5 = [termin17, termin18, termin19, termin20];
    var termine6 = [termin21, termin22, termin23, termin24];

    var gr1 = new Gruppe("GKAP", 1, termine,
               prof1, assist1,
                 10, 20,
                 10, 0);

    var gr2 = new Gruppe("ADP", 2, termine2,
               prof2, assist2,
               10, 20,
               10, 0);

    var gr3 = new Gruppe("BWP", 3, termine3,
               prof3, assist3,
               10, 20,
               10, 0);

    var gr4 = new Gruppe("SEP2", 4, termine4,
               prof4, assist1,
               10, 20,
               10, 0);

    var gr5 = new Gruppe("PRP2", 4, termine5,
                prof2, assist2,
                10, 20,
                10, 0);

    var gr6 = new Gruppe("MGU", 4, termine6,
                          prof2, assist2,
                          10, 20,
                          10, 0);


    // #########################################################################

    // Klasse fuer Testergebnisse
    // #########################################################################
    function TestErgebnis(input, expected, actual){

      this.input = input;
      this.expected = expected;
      this.pass = input === expected;
      this.description = false;

      if(this.pass){
          this.description = "OK";
      }else{
        this.description = "FAIL";
      }

    }

    // #########################################################################


    var zweiAusgewaehltKeineUeberschn = [gr1, gr2];

    var starteTestRoutine = function(){

      var input = UeberschneidungsPruefer.hatUeberschneidungen(zweiAusgewaehltKeineUeberschn);
      var ergebnis = new TestErgebnis(zweiAusgewaehltKeineUeberschn, false, input);
      testErgebnisse.push(ergebnis);
    }


    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // -----------------------------------------------------------------
    return {// #########################################################################
      starteTestRoutine: starteTestRoutine,
      getTestErgebnisse: getTestErgebnisse
    };
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("UeberschneiderTest", UeberschneiderTest);

  // Code sofort ausführen
}());
