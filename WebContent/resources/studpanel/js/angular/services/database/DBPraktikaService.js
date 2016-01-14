

(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBPraktikaService = function($http, UeberschneidungsPruefer, DBTeamService, DBErrorService, URLService, DBProfileService) {

    // Felder
    // #########################################################################

    var gruppen = [];     // Speicher für Gruppen aus Backend
    var gruppenTemp = [];
    var ausgewaehlteGruppen = [];
    var gefilterteGruppenEintraege = [];
    var gruppenEintraege = [];
    var gruppenEintraegeKopie = [];
    var fachkuerzel = ["Alle"];
    var ueberschneidungen = [];
    var detailGruppe;
    var einzel = false;
    var url = URLService.getUrl();

    // #########################################################################


    // #########################################################################

    var sucheIndexAusgewGruppen = function(gruppe){

      return UeberschneidungsPruefer.sucheGruppeIndex(gruppe, ausgewaehlteGruppen);
    }

    // Helper
    var sucheIndexGruppenEintrag = function(fachkuerzel, grpNr){

      for(var i = 0; i < gruppenEintraege.length; i++){

        if(gruppenEintraege[i].gruppe.fachkuerzel == fachkuerzel
            && gruppenEintraege[i].gruppe.grpNr == grpNr){

              return i;
            }
      }
      return -1;
    }


    var zeigeGruppenEinraege = function(){

      for(var i = 0; i < gruppen.length; i++){


        var grEintr = new Gruppeneintrag(gruppen[i], false);
        gruppenEintraege.push(grEintr);

      }

      for(var i = 0; i < gruppenEintraege.length; i++){

        gruppenEintraegeKopie.push(gruppenEintraege[i]);
      }
    }


    var getDetailGruppe = function(){
      return detailGruppe;
    }


    var gruppeIstAusgewaehlt = function(gruppe){

      return UeberschneidungsPruefer.enthaeltGruppe(gruppe, ausgewaehlteGruppen);
    }

    var getGruppenEintraege = function(){

      return gruppenEintraege;
    }


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

    function Gruppeneintrag(gruppe){

      this.gruppe = gruppe;
      this.ueberschneidungen = [];
      this.ausgewaehlt = false;
    }
    // #########################################################################


    // HardCodedDaten
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

    var grEintr1 = new Gruppeneintrag(gr1);
    var grEintr2 = new Gruppeneintrag(gr2);
    var grEintr3 = new Gruppeneintrag(gr3);
    var grEintr4 = new Gruppeneintrag(gr4);
    var grEintr5 = new Gruppeneintrag(gr5);
    var grEintr6 = new Gruppeneintrag(gr6);

    /*
    gruppenEintraege.push(grEintr1);
    gruppenEintraege.push(grEintr2);
    gruppenEintraege.push(grEintr3);
    gruppenEintraege.push(grEintr4);
    gruppenEintraege.push(grEintr5);
    gruppenEintraege.push(grEintr6);

    for(var i = 0; i < gruppenEintraege.length; i++){

      gruppenEintraegeKopie.push(gruppenEintraege[i]);
    }
    */



    // #########################################################################


    // Schnittstelle
    // #########################################################################

    var waehleGruppeAus = function(fach, grpNr){

      var index = sucheIndexGruppenEintrag(fach, grpNr);
      var gruppe = gruppenEintraege[index].gruppe;

      if(!gruppeIstAusgewaehlt(gruppe)){

        gruppenEintraege[index].ausgewaehlt = true;
        ausgewaehlteGruppen.push(gruppe);
        UeberschneidungsPruefer.pruefeUeberschneidungen(ausgewaehlteGruppen);
        return true;
      }

      var indexSelected = sucheIndexAusgewGruppen(gruppe);
      gruppenEintraege[index].ausgewaehlt = false;
      var temp = ausgewaehlteGruppen.splice(indexSelected, 1);

      var entfGruppe = temp[0];
      var index = sucheIndexGruppenEintrag(entfGruppe.fachkuerzel, entfGruppe.grpNr);
      var eintrag = gruppenEintraege[index];
      var gruppe = gruppenEintraege[index].gruppe;

      for(var i = 0; i < gruppe.termine.length; i++){

        gruppe.termine[i].usGruppen = [];
      }

      UeberschneidungsPruefer.pruefeUeberschneidungen(ausgewaehlteGruppen);

      return false;
    }

    var initGruppenDetails = function(fach, grpNr){

      var index = sucheGruppe(fach, grpNr);
      if(index > -1){

        detailGruppe = gruppenEintraege[index].gruppe;
      }
    }

    var sucheGruppe = function(fach, grpNr){

      for(var i = 0; i < gruppenEintraege.length; i++){

        var gruppe = gruppenEintraege[i].gruppe;

        if(gruppe.fachkuerzel == fach && gruppe.grpNr == grpNr){

          return i;
        }
      }

      return -1;
    }

    var isEinzel = function(){
      return einzel;
    }

    var setEinzel = function(bool){

      einzel = bool;
    }

    var fachBereitsGewaehlt = function(fachkuerzel){

      for(var i = 0; i < ausgewaehlteGruppen.length; i++){

        if(ausgewaehlteGruppen[i].fachkuerzel == fachkuerzel){

          return true;
        }
      }

      return false;
    }
    var getFachkuerzel = function(){

      return fachkuerzel;
    }
    var getTermine = function(termine){

      var termineNeu = [];
      for(var i = 0; i < termine.length; i++){
        var start = new Uhrzeit(termine[i].start.stunden, termine[i].start.minuten);
        var ende = new Uhrzeit(termine[i].ende.stunden, termine[i].ende.minuten);
        var date = new Date(termine[i].datum);
        var dateArr = date.toString().split(" ", 4);
        var dateString = dateArr[2] + " " + dateArr[1] + " " + dateArr[3];
        var termin = new Termin(termine[i].raum, new Date(dateString), start, ende);
        termineNeu.push(termin);
      }
      return termineNeu;
    }


    var enthaeltFachkuerzel = function(fk){

      for(var i = 0; i < fachkuerzel.length; i++){

        if(fachkuerzel[i] == fk){
          return true;
        }
      }

      return false;
    }
    var initPraktikaGruppenDB = function(fk, typ, veranTyp){

      gruppen = [];
      gruppenEintraege = [];
      gruppenEintraegeKopie = [];
      var erfolg = false;
      var fullUrl = null;
      var user = DBProfileService.getCurrUser();
      if(typ == "team"){

        fullUrl = url+"gruppenTeam" + veranTyp + "/" + user.matrNr.matrNr;
      }else{
        fullUrl = url+"gruppenEinzel" + veranTyp + "/" + user.matrNr.matrNr;

      }

      $http.post(fullUrl, fk).

        // Funktion, falls gültige Daten zurückkommen
        then(function(response) {

          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;


            var termine = getTermine(data[i].termine);
            var gruppe = new Gruppe(data[i].fachkuerzel, data[i].grpNr, termine, data[i].professor, data[i].assistent, data[i].maxTeams, data[i].anzTeams);
            gruppen.push(gruppe);

            if(!enthaeltFachkuerzel(gruppe.fachkuerzel)){
              fachkuerzel.push(gruppe.fachkuerzel);
            }

            var grEintr = new Gruppeneintrag(gruppe, false);
            gruppenEintraege.push(grEintr);
            gruppenEintraegeKopie.push(grEintr);
          }

          erfolg = true;


        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

        return true;
    }


    var initPraktikaGruppen = function(fk){

        initPraktikaGruppenDB(fk, "team", "Praktikum");
    }

    var initPraktikaGruppenEinzel = function(fk){

        initPraktikaGruppenDB(fk, "einzel", "Praktikum");
    }


    var initWPGruppen = function(fk){

        initPraktikaGruppenDB(fk, "team", "WP");
    }

    var initWPGruppenEinzel = function(fk){

        initPraktikaGruppenDB(fk, "team", "WP");
    }


    var initPOGruppen = function(fk){

        initPraktikaGruppenDB(fk, "team", "PO");
    }

    var initPOGruppenEinzel = function(fk){

        initPraktikaGruppenDB(fk, "team", "PO");
    }


    var speichereEinzelAnmeldungen = function(){


      for(var i = 0; i < ausgewaehlteGruppen.length; i++){

        var termine = ausgewaehlteGruppen[i].termine;

        for(var i = 0; i < termine.length; i++){
          termine[i].usGruppen =  null;
        }
      }

      var user = DBProfileService.getCurrUser();
      for(var i = 0; i < ausgewaehlteGruppen.length; i++){

        $http.post(url+"einzelanmeldung/" + user.matrNr.matrNr, ausgewaehlteGruppen[i]).

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

    }

    var blendeUeberschneidungenAus = function(){

      UeberschneidungsPruefer.pruefeAlleMoeglichenUeberschneidungen(gruppenEintraege, ausgewaehlteGruppen);
    }

    var hatUeberschneidungen = function(){

      return UeberschneidungsPruefer.hatUeberschneidungen(ausgewaehlteGruppen);
    }

    var getAlleGruppenFuerFach = function(fk){

      // Immer erstmal zurücksetzen falls Änderung
      gruppenEintraege = [];
      for(var i = 0; i < gruppenEintraegeKopie.length; i++){

        gruppenEintraege.push(gruppenEintraegeKopie[i]);
      }

      if(!(fk == fachkuerzel[0])){

        for(var i = 0; i < gruppenEintraegeKopie.length; i++){

          var gruppe = gruppenEintraegeKopie[i].gruppe;
          var eintrIndex = sucheGruppenEintrag(gruppe, gruppenEintraege);

          if(!(gruppe.fachkuerzel == fk) && eintrIndex > -1){

            var eintrag = gruppenEintraege.splice(eintrIndex, 1);
          }
        }
      }
    }

    var sucheGruppenEintrag = function(gruppe, eintraege){

      for(var i = 0; i < eintraege.length; i++){

        if(gruppe == eintraege[i].gruppe){

          return i;
        }
      }
      return -1;
    }

    var enthaeltEintrag = function(gruppenEintrag, eintraege){

      for(var i = 0; i < eintraege.length; i++){

        if(eintraege[i] == gruppenEintrag){

          return true;
        }
      }
      return false;
    }

    var getGruppenEintrage = function(){

      return gruppenEintraege;
    }


    // TODO:
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    var initTeamKonfig = function(){

      DBTeamService.initEintraege(ausgewaehlteGruppen);
      ausgewaehlteGruppen = [];


    }





    var teamAnmeldePhaseAbgeschlossen = function(){

      return false;
    }

    var einzelAnmeldePhaseAbgeschlossen = function(){

      return false;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // #########################################################################

    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // -----------------------------------------------------------------
    return {

      gruppenEintraege: gruppenEintraege,
      fachkuerzel: fachkuerzel,
      waehleGruppeAus: waehleGruppeAus,
      initGruppenDetails: initGruppenDetails,
      initTeamKonfig: initTeamKonfig,
      initPraktikaGruppen: initPraktikaGruppen,
      detailGruppe: detailGruppe,
      speichereEinzelAnmeldungen: speichereEinzelAnmeldungen,
      initPraktikaGruppenEinzel: initPraktikaGruppenEinzel,
      getDetailGruppe: getDetailGruppe,
      initWPGruppen: initWPGruppen,
      initWPGruppenEinzel: initWPGruppenEinzel,
      blendeUeberschneidungenAus: blendeUeberschneidungenAus,
      hatUeberschneidungen: hatUeberschneidungen,
      getAlleGruppenFuerFach: getAlleGruppenFuerFach,
      getGruppenEintrage: getGruppenEintrage,
      teamAnmeldePhaseAbgeschlossen: teamAnmeldePhaseAbgeschlossen,
      einzelAnmeldePhaseAbgeschlossen: einzelAnmeldePhaseAbgeschlossen,
      getFachkuerzel, getFachkuerzel,
      initPOGruppen: initPOGruppen,
      initPOGruppenEinzel: initPOGruppenEinzel,
      fachBereitsGewaehlt: fachBereitsGewaehlt,
      isEinzel: isEinzel,
      setEinzel: setEinzel


    };
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBPraktikaService", DBPraktikaService);

  // Code sofort ausführen
}());
