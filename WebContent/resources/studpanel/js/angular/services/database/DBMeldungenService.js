

(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBMeldungenService = function($http, DBErrorService, URLService) {

    var url = URLService.getUrl();
    var index = 0;
    var meldungen = [];


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

    function Teameinladung(absender, empfaenger, versandDatum, bestaetigt, bestaetDatum){

      this.absender = absender;
      this.empfaenger = empfaenger;
      this.versandDatum = versandDatum;
      this.bestaetigt = bestaetigt;
      this.bestaetDatum = bestaetDatum;
      this.fach = "GKAP";
      this.grpNr = 2;
      this.visible = true;
      this.typ = "Teameinladung";
    }

    function TeamID(id){
      this.id = id;
    }

    function Team(teamId, fachkuerzel, minTeiln, maxTeiln, grpNr){
      this.fachkuerzel = fachkuerzel;
      this.teamID = teamId;
      this.minTeiln = minTeiln;
      this.maxTeiln = maxTeiln;
      this.mitglieder = [];
      this.mgString = teamToString(this.mitglieder);
      this.grpNr = grpNr;
    }

    function MatrikelNr(matrNr){
      this.matrNr = matrNr;
    }

    function Student(type, matrNr, vorname, nachname){

      this.type = type;
      this.matrNr = matrNr;
      this.vorname = vorname;
      this.nachname = nachname;
      this.vollerName = this.vorname + " " +this.nachname;
    }


    var getAndereEinladungenFuerFach = function(einladung){

      var andereEinladungen = [];
      for(var i = 0; i < meldungen.length; i++){

        if(meldungen[i].typ == "Teameinladung"
            && meldungen[i].team.fachkuerzel == einladung.team.fachkuerzel
            && meldungen[i].team.vorgGrpNr != einladung.team.vorgGrpNr){

          andereEinladungen.push(meldungen[i]);

        }
      }

      return andereEinladungen;
    }

    var getMeldungen = function(){


      return meldungen;
    }


    var getTermine = function(termine){

      var termineNeu = [];
      for(var i = 0; i < termine.length; i++){
        var start = new Uhrzeit(termine[i].start.stunden, termine[i].start.minuten);
        var ende = new Uhrzeit(termine[i].ende.stunden, termine[i].ende.minuten);
        var date = new Date(termine[i].datum);
        var dateString = dateArr[2] + " " + dateArr[1] + " " + dateArr[3];
        var termin = new Termin(termine[i].raum, new Date(dateString), start, ende);
        termineNeu.push(termin);
      }
      return termineNeu;
    }

    var getDatum = function(date){

      var aDate = new Date(date);
      var dateArr = date.toString().split(" ", 4);
      var dateString = dateArr[2] + " " + dateArr[1] + " " + dateArr[3];
      var newDate = new Date(dateString);
      return newDate;
    }


    function Teameinladung(absender, empfaenger, versandDatum, team, bestaetigt, bestaetDatum){

      this.absender = absender;
      this.empfaenger = empfaenger;
      this.versandDatum = versandDatum;
      this.team = team;
      this.bestaetigt = bestaetigt;
      this.bestaetDatum = bestaetDatum;
      this.typ = "Teameinladung";
    }

    function TEAblehnung(absender, empfaenger, versandDatum, team){

      this.absender = absender;
      this.empfaenger = empfaenger;
      this.versandDatum = versandDatum;
      this.team = team;
      this.typ = "TEAblehnung";
    }

    function TeamAustrittsmeldung(absender, empfaenger, versandDatum, team){

      this.absender = absender;
      this.empfaenger = empfaenger;
      this.versandDatum = versandDatum;
      this.team = team;
      this.typ = "TeamAustrittsmeldung";
    }

    var sucheMeldung = function(meldung){

      for(var i = 0; i < meldungen.length;i++){

        if(meldung.typ == meldungen[i].typ
            && meldung.absender.matrNr.matrNr === meldungen[i].absender.matrNr.matrNr
             && meldung.empfaenger.matrNr.matrNr === meldungen[i].empfaenger.matrNr.matrNr){

               return i;
             }
      }

      return -1;
    }
    var loescheMeldung = function(meldung){

      var index = sucheMeldung(meldung);
      meldungen.splice(index, 1);
    }


    /**
     * Initialisiert mithilfe des Backends die Meldungen
     * des aktuell eingloggten Users.
     */
    var initMeldungen = function(matrNr){

      meldungen = [];
      $http.get(url+"meldungen/" + matrNr).

        // Funktion, falls gültige Daten zurückkommen
        then(function(response) {

          var data = response.data;
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {

            var meldung;
            var absender = new Student("student", data[i].absender.matrNr, data[i].absender.vorname, data[i].absender.nachname);
            var empfaenger = data[i].empfaenger;
            var versandDatum = new Date(data[i].versandDatum);
            var typ = data[i].typ;

            if(typ == "Teameinladung"){

              var team = data[i].team;
              var bestaetigt = data[i].bestaetigt;
              var bestaetDatum = data[i].bestaetDatum;
              meldung = new Teameinladung(absender, empfaenger, versandDatum, team, bestaetigt, bestaetDatum);
              meldung.descr = "Teameinladung";
            }else if(typ == "TEAblehnung" || typ == "TeamAustrittsmeldung"){
              var team = data[i].team;
              if(typ == "TEAblehnung"){
                meldung = new TEAblehnung(absender, empfaenger, versandDatum, team);
                meldung.descr = "Einladung abgelehnt";
              }else if(typ == "TeamAustrittsmeldung"){
                meldung = new TeamAustrittsmeldung(absender, empfaenger, versandDatum, team);
                meldung.descr = "Mitglied ausgetreten";
              }
            }

            meldungen.push(meldung);
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);

        });

        return true;
    }



    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // -----------------------------------------------------------------
    return {
      getMeldungen: getMeldungen,
      initMeldungen: initMeldungen,
      loescheMeldung: loescheMeldung,
      sucheMeldung: sucheMeldung,
      getAndereEinladungenFuerFach: getAndereEinladungenFuerFach
    };
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBMeldungenService", DBMeldungenService);

  // Code sofort ausführen
}());
