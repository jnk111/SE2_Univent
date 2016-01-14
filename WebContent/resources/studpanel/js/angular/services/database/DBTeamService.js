

(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBTeamService = function($http, $log, DBMeldungenService, DBProfileService, UeberschneidungsPruefer, DBErrorService, URLService) {

    var url = URLService.getUrl();

    // Felder
    var teams = [];
    var teamEintraege = [];
    var meineTeams = [];
    var meineAnmeldungen = [];
    var gruppen = [];
    var studenten = [];
    var studentenStr = [];
    var aktuelleAuswahl = [];
    var teamEintraegeKonfig = [];
    var aktuellerEintrag;
    var studStrIndex = 0;


    // Helper
    // #########################################################################

    var faecherToString = function(faecher){

      var faecherString = "";

      for(var i = 0; i < faecher.length; i++){

        faecherString += faecher[i].fachKuerzel;

        if(!(i == faecher.length-1)){

          faecherString += ", ";
        }
      }

      return faecherString;

    }
    var teamToString = function(mitglieder){

      var teamString = "";

      for(var i = 0; i < mitglieder.length; i++){

        teamString += mitglieder[i].vollerName;

        if(!(i == mitglieder.length-1)){

          teamString += ", ";
        }
      }

      return teamString;

    }

    var studentenToString = function(studenten){

        for(i=0; i < studenten.length; i++){
        	studentenStr.push(studenten[i].matrNr.matrNr + " " + studenten[i].vorname + " " +studenten[i].nachname);
        }
    }


    var sucheStudent = function(matrNr, studentenListe){

        for(var i = 0; i < studentenListe.length; i++){

          if(istGleicheMatrNr(matrNr, studentenListe[i].matrNr)){
            return i;
          }
        }

        return -1;
    }

    var istGleicheMatrNr = function(matrNr1, matrNr2){

      return matrNr1.matrNr === matrNr2.matrNr;
    }


    var getAktuelleAuswahl = function(){

      return aktuelleAuswahl;
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

    function TeamID(id){
      this.id = id;
    }

    function Teameintrag(gruppe, team){
      this.gruppe = gruppe;
      this.team = team;
    }

    function Team(teamId, fachkuerzel, minTeiln, maxTeiln, grpNr, vorgGrpNr){
      this.fachkuerzel = fachkuerzel;
      this.teamID = teamId;
      this.minTeiln = minTeiln;
      this.maxTeiln = maxTeiln;
      this.mitglieder = [];
      this.mgString = teamToString(this.mitglieder);
      this.grpNr = grpNr;
      this.vorgGrpNr = vorgGrpNr;
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
      this.bestaetigt;
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

    function Fach(fachKuerzel){

      this.fachKuerzel = fachKuerzel;
      this.fachBezeichnung = null;
      this.fachBereich = null;
      this.semester = null;
    }





    // Harccoded datene
    // #########################################################################

    var matr1 = new MatrikelNr(2434411);
    var matr2 = new MatrikelNr(2434422);
    var matr3 = new MatrikelNr(2434433);
    var matr4 = new MatrikelNr(2434444);
    var matr5 = new MatrikelNr(2434455);
    var matr6 = new MatrikelNr(2434466);

    var student1 = new Student("student", matr1, "Maria", "Mueller");
    var student2 = new Student("student", matr2, "Willy", "Wonka");
    var student3 = new Student("student", matr3, "Max", "Mustermann");
    var student4 = new Student("student", matr4, "Alfred", "Futterkiste");
    var student5 = new Student("student", matr5, "Max", "Mustermann5");
    var student6 = new Student("student", matr6, "Max", "Mustermann6");

    var teams = [];


    // #########################################################################

    var einladungAblehnen = function(einladung){

      DBMeldungenService.loescheMeldung(einladung);
      $http.post(url+"einlAblehnen", angular.toJson(einladung)).

      then(function(response) {
          var data = response.data;
          for (i = 0; i < response.data.length; i++) {

              DBErrorService.setError(response.data);
              console.log(response.data);
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
      });
    }

    var isEinzelAnmelder = function(team){
      var mitglieder = team.mitglieder;
      var user = DBProfileService.getCurrUser();

      for(var i = 0; i < mitglieder.length; i++){

        if(mitglieder[i].matrNr.matrNr === user.matrNr.matrNr) {

          return mitglieder[i].einzel;

        }
      }

      return false;

    }



    //initialisiert Studentenarray
    var initStudenten = function(fk){

        var studiesString = [];
        var user = DBProfileService.getCurrUser();
        $http.post(url+"studenten/" + user.matrNr.matrNr, fk).

        then(function(response) {
            var data = response.data;
            for (i = 0; i < response.data.length; i++) {

              var student = new Student("student", data[i].matrNr, data[i].vorname, data[i].nachname);
              student.bestaetigt = data[i].bestaetigt;
              studenten.push(student);
              studiesString.push(student.matrNr.matrNr + " " + student.vorname + " " + student.nachname);

            }
          },
          // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);
        });

        studentenStr.push(studiesString);
    }

    var sucheTeam = function(team){

      for(var i = 0; i < meineTeams.length; i++){
        if(team.teamID.id === meineTeams[i].teamID.id){

          return i;
        }
      }

      return -1;
    }


    var verlasseTeam = function(index){

      var team = meineTeams[index];
      meineTeams.splice(index, 1);
      var user = DBProfileService.getCurrUser();
      $http.post(url+"verlasseTeam/" + user.matrNr.matrNr, angular.toJson(team)).
         then(function(response) {

            DBErrorService.setError(response.data);
            console.log(response.data);


          },
         // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);

        });

    }

    var alleBestaetigt = function(team){

      for(var i = 0; i < team.mitglieder.length; i++){

        if(!team.mitglieder[i].bestaetigt){

          return false;
        }
      }
      return true;
    }

    // Erzeugt neue Abgabetermine anhand des Models
    function getTermine(termine) {
      var appointments = [];
      for (i = 0; i < termine.length; i++) {
        appointments.push(new Termin(termine[i].raum,
                                new Date(termine[i].datum),
                                new Uhrzeit(termine[i].start.stunden, termine[i].ende.minuten),
                                new Uhrzeit(termine[i].ende.stunden, termine[i].ende.minuten)));
      }
      return appointments;
    }

    var getGruppe = function(team){

      $http.post(url+"gruppe", angular.toJson(team)).
         then(function(response) {

            var gruppe = response.data;
            gruppe.termine = getTermine(gruppe.termine);
            gruppen.push(response.data);


          },
         // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);

        });
    }

    //Listet alle Teams auf in denen der Student ist.
    var initTeamTabelleMeineTabelle = function(student){

      meineTeams = [];
      var user = DBProfileService.getCurrUser();
      $http.get(url+"getAlleTeams/" + user.matrNr.matrNr).
       then(function(response) {
         var data = response.data;
           for (i = 0; i < response.data.length; i++) {

             // teamId, fachkuerzel, minTeiln, maxTeiln, grpNr, vorgGrpNr
             var team = new Team(data[i].teamID, data[i].fachkuerzel, data[i].minTeiln, data[i].maxTeiln, data[i].grpNr, data[i].vorgGrpNr);
             team.mitglieder = data[i].mitglieder;
             meineTeams.push(team);
             getGruppe(team);
           }
         },
         // Funktion bei Fehler
         function(response) {
           DBErrorService.setError(false);
        });
    }


    var getGruppen = function(){
      return gruppen;
    }

    var sucheStudentTeamDetails = function(mitglieder, stud){

      for(var i = 0; i < mitglieder.length; i++){

        if(mitglieder[i].matrNr.matrNr === stud.matrNr.matrNr){

          return i;
        }
      }
      return -1;
    }
    var zieheEinladungZurueck = function(team, stud){

      var arg = team.teamID.id + ":" + stud.matrNr.matrNr;
      $http.post(url+"einlZurueck", arg).
       then(function(response) {

         DBErrorService.setError(response.data);
         console.log(response.data);

         },
         // Funktion bei Fehler
         function(response) {
           DBErrorService.setError(false);
        });

        var index = sucheStudentTeamDetails(team.mitglieder, stud);
        team.mitglieder.splice(index, 1);
    }

    // Prüft ob die Eingabe einem Studenten im Puffer entspricht
    // return gefunden, nicht gefunden : boolean
    var istGueltigerStudent = function(expr){

      if(expr != "" && expr != undefined && expr != null)           {

        var matrNr = expr.split(' ');

        if((matrNr[0].match("[0-9]{7}")) && (matrNr[0].length == 7)){

          var matrNr = new MatrikelNr(parseInt(matrNr[0]));
          var studIndex = sucheStudent(matrNr, studenten);

          if(studIndex > -1
              && !(aktAuswahlEnthaeltStudent(studenten[studIndex]))
                && !maxMitgliederErreicht()){
            return true;
          }
        }
      }
        return false;
    }

    var maxMitgliederErreicht = function(){

      if(aktuellerEintrag){
        var team = aktuellerEintrag.team;
        return aktuelleAuswahl.length >= team.maxTeiln - 1;
      }
    }


    var alleTeamsVollstaendig = function(){

      for(var i = 0; i < teams.length; i++){

        var team = teams[i].team;

        if(team.mitglieder.length < team.minTeiln - 1){
          return false;
        }
      }
      return true;
    }

    //Uebernimmt Stundenten aus Suchfeld und pusht ihn in das aktuelleAuswahl-Array
    //return Studenten
    var waehleTeamMitgliedAus = function(expr){

        var matrNr = expr.split(' ');
        var matrNr = new MatrikelNr(parseInt(matrNr[0]));
        var studIndex = sucheStudent(matrNr, studenten);
        var student = studenten[studIndex];

        aktuelleAuswahl.push(student);
        return true;

    }


    var aktAuswahlEnthaeltStudent = function(student){

      for(var i = 0; i < aktuelleAuswahl.length; i++){

        if(student.matrNr == aktuelleAuswahl[i].matrNr){
          return true;
        }
      }

      return false;
    }

    var entferneTeamMitgliedAuswahl = function(matrNr){

        var studIndex = sucheStudent(matrNr, aktuelleAuswahl);
        var student = aktuelleAuswahl.splice(studIndex, 1);

        entferneMitgliedAusTeam(student[0]);
    }

    var entferneMitgliedAusTeam = function(student){

      var mitglieder = aktuellerEintrag.team.mitglieder;
      for(var i = 0; i < mitglieder.length; i++){

        if(istGleicheMatrNr(student.matrNr, mitglieder[i].matrNr)){

          mitglieder.splice(i, 1);
        }
      }
    }

    var uebernehmeAusgewMitglieder = function(){

        for(var i = 0; i < aktuelleAuswahl.length; i++){

          if(!(mitgliedBereitsInTeam(aktuelleAuswahl[i]))){
            aktuellerEintrag.team.mitglieder.push(aktuelleAuswahl[i]);
          }

        }
        aktuellerEintrag.team.mgString = teamToString(aktuellerEintrag.team.mitglieder);
        aktuelleAuswahl = [];
    }

    var mitgliedBereitsInTeam = function(student){

      var mitglieder = aktuellerEintrag.team.mitglieder;

      for(var i = 0; i < mitglieder.length; i++){

        if(mitglieder[i] == student){
          return true;
        }
      }

      return false;

    }

    var sucheGruppe = function(fachkuerzel, grpNr){

      for(var i = 0; i < gruppen.length; i++){
        if(gruppen[i].fachkuerzel == fachkuerzel
            && gruppen[i].grpNr == grpNr){
              return i;
            }
      }

      return -1;
    }

    var sucheTeamEintrag = function(team){

      for(var i = 0; i < teams.length; i++){

        if(teams[i].team.teamID.id === team.teamID.id){

          return teams[i];
        }
      }

      return null;
    }


    var sucheTeamEintragIndex = function(team){

      for(var i = 0; i < teams.length; i++){

        if(teams[i].team.fachkuerzel == team.team.fachkuerzel){

          return i;
        }
      }

      return -1;
    }
    var initSucheNeuesMitglied = function(meldung){

      var team = meldung.team;
      ausgewaehlteGruppen = [];
      gruppen = [];
      $http.post(url+"gruppe", angular.toJson(team)).
         then(function(response) {

            var gruppe = response.data;
            gruppe.termine = getTermine(gruppe.termine);
            gruppen.push(gruppe);
            gruppe.teamID = team.teamID;
            initEintraegeNeuesMitglied(gruppen, team);

          },
         // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);

        });



    }
    var entferneTeilnehmer = function(team){

      var user = DBProfileService.getCurrUser();
      var mitglieder = team.mitglieder;
      var indexRm
      for(var i = 0; i < team.mitglieder.length; i++){

        if(mitglieder[i].matrNr.matrNr === user.matrNr.matrNr){

          indexRm = i;
          break;
        }
      }

      mitglieder.splice(indexRm, 1);
    }


    var initEintraegeNeuesMitglied = function(ausgewaehlteGruppen, team){

      teams = [];
      gruppen = ausgewaehlteGruppen;
      var fachkuerzel = [];
      var veranstaltungen = [];
      for(var i = 0; i < ausgewaehlteGruppen.length; i++){

        initStudenten(ausgewaehlteGruppen[i].fachkuerzel);
        fachkuerzel.push(ausgewaehlteGruppen[i].fachkuerzel);
      }

      $http.post(url+"getVeranstaltungen", angular.toJson(fachkuerzel)).

        then(function(response) {
          veranstaltungen.push(response.data[i]);
          for(var i = 0; i < ausgewaehlteGruppen.length; i++){
            var gruppe = ausgewaehlteGruppen[i];
            var minTeam = getMinTeam(response.data, gruppe);
            var maxTeam = getMaxTeam(response.data, gruppe);
            entferneTeilnehmer(team);

            for(var i = 0; i < team.mitglieder.length; i++){

              team.mitglieder[i].bestaetigt = true;
            }

            var te = new Teameintrag(gruppe, team);
            teams.push(te);
            aktuellerEintrag = te;
            // team.mitglieder.splice(0, 1);  // später entferneUser
          }
      },
          // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);
        });
    }
    // Laedt die Teammitglieder des ausgewaehlten Teams damit diese bspw. im Teamkonfigurator
    // angezeigt werden können
    var initTeamTabelleKonfig = function(teamEintrag){

        studStrIndex = sucheTeamEintragIndex(teamEintrag);
        aktuellerEintrag = teamEintrag;
        for(var l = 0; l < aktuellerEintrag.team.mitglieder.length; l++){
            aktuelleAuswahl.push(aktuellerEintrag.team.mitglieder[l]);

        }
    }

    var entferneGruppe = function(team, usGruppen){

      var entfIndex = -1;
      for(var i = 0; i < usGruppen.length; i++){
        if(team.fachkuerzel == usGruppen[i].fachkuerzel
            && team.vorgGrpNr == usGruppen[i].grpNr){

              entfIndex = i;
              break;
        }
      }

      usGruppen.splice(entfIndex, 1);
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

    var termineInit = false;

    var getUeberschneidungen = function(){

      return gruppen;
    }

    var initUeberschneidungen = function(team){

      gruppen = [];
      meineAnmeldungen = [];
      getGruppe(team);
      initTeamTabelleMeineTabelle();

      for(var i = 0; i < meineTeams.length; i++){

        getGruppe(meineTeams[i]);
      }

      for(var i = 0; i < gruppen.length; i++){
        gruppen[i].termine = getTermine(gruppen[i].termine);
      }
    }


    var sucheGewaehlteGruppe = function(team, gruppArr){

      for(var i = 0; i < gruppArr.length; i++){

        var grupp = gruppArr[i];

        if(grupp.fachkuerzel == team.fachkuerzel
            && grupp.grpNr == team.vorgGrpNr){

              return i;
        }
      }

      return -1;
    }


    var pruefeUeberschneidungen = function(team){

      UeberschneidungsPruefer.pruefeUeberschneidungen(gruppen);

      for(var i = 0; i < gruppen.length; i++){
        var gruppe = gruppen[i];
        gruppe.usGruppen = [];
        var termine = gruppe.termine;
        for(var j = 0; j < termine.length; j++){

          gruppe.usGruppen = gruppe.usGruppen.concat(termine[j].usGruppen);
        }
      }

      var grIndex = sucheGewaehlteGruppe(team, gruppen);
      var usGruppe = gruppen[grIndex];

      if(typeof usGruppe.usGruppen === "undefined" || usGruppe.usGruppen.length == 0){

        gruppen = [usGruppe];
      }

    }

    var getStudStrIndex = function(){

      return studStrIndex;
    }

    var enthaeltFachkuerzel = function(fachkuerzelArr, fk){

      for(var i = 0; i < fachkuerzelArr.length; i++){

        if(fachkuerzelArr[i] == fk){

          return true;
        }
      }

      return false;
    }

    var getMinTeam = function(veranstaltungen, gruppe){

      for(var i = 0; i < veranstaltungen.length; i++){
        if(gruppe.fachkuerzel == veranstaltungen[i].fach.fachKuerzel){
          return veranstaltungen[i].minTeilnTeam;
        }
      }
      return -1;
    }

    var getMaxTeam = function(veranstaltungen, gruppe){

      for(var i = 0; i < veranstaltungen.length; i++){
        if(gruppe.fachkuerzel == veranstaltungen[i].fach.fachKuerzel){
          return veranstaltungen[i].maxTeilnTeam;
        }
      }
      return -1;
    }

    var getTeameintraege = function(){

      return teams;
    }


    var initEintraege = function(ausgewaehlteGruppen){

      teams = [];
      gruppen = ausgewaehlteGruppen;
      var fachkuerzel = [];
      var veranstaltungen = [];

      for(var i = 0; i < ausgewaehlteGruppen.length; i++){

        initStudenten(ausgewaehlteGruppen[i].fachkuerzel);
        fachkuerzel.push(ausgewaehlteGruppen[i].fachkuerzel);
      }

      $http.post(url+"getVeranstaltungen", angular.toJson(fachkuerzel)).

        then(function(response) {
          veranstaltungen.push(response.data[i]);
          for(var i = 0; i < ausgewaehlteGruppen.length; i++){
            var gruppe = ausgewaehlteGruppen[i];
            var minTeam = getMinTeam(response.data, gruppe);
            var maxTeam = getMaxTeam(response.data, gruppe);
            var team = null;
            if(gruppe.teamID != undefined){
              team = new Team(gruppe.teamID, gruppe.fachkuerzel, minTeam, maxTeam, 0, gruppe.grpNr);
            }else{
              team = new Team(null, gruppe.fachkuerzel, minTeam, maxTeam, 0, gruppe.grpNr);
            }
            teams.push(new Teameintrag(gruppe, team));

          }
      },
          // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);
        });
    }



    var setzeTeamZurueck = function(teamEintrag){

      teamEintrag.team.mitglieder = [];
    }

    var setzeAlleTeamsZurueck = function(){

      aktuelleAuswahl = [];
      for(var i = 0; i < teams.length; i++){
        var team = teams[i].team;
        team.mitglieder = [];
      }
    }



    // Uebernimmt die Teamkonfiguration und gibt sie ans Backend weiter
    var bestaetigeTeamkonfig = function(){

      var sendTeams = [];

      for(var i = 0; i < teams.length; i++){

        sendTeams.push(teams[i].team);
      }

      for(var i = 0; i < sendTeams.length; i++){

        var user = DBProfileService.getCurrUser();
        $http.post(url+"einladung/" + user.matrNr.matrNr, angular.toJson(sendTeams[i])).

          then(function(response) {
              var data = response.data;
              for (i = 0; i < response.data.length; i++) {

                DBErrorService.setError(response.data);
                console.log(response.data);
              }
            },
            // Funktion bei Fehler
            function(response) {
              DBErrorService.setError(false);
          });
      }

    }


    var einladungAnnehmen = function(einladung){


      var andereEinladungen = DBMeldungenService.getAndereEinladungenFuerFach(einladung);

      $http.post(url+"einlAnnehmen", angular.toJson(einladung)).

        then(function(response) {
            var data = response.data;
            for (i = 0; i < response.data.length; i++) {

              DBErrorService.setError(response.data);
              console.log(response.data);
            }
          },
          // Funktion bei Fehler
          function(response) {
            DBErrorService.setError(false);
        });

        DBMeldungenService.loescheMeldung(einladung);

        for(var i = 0; i < andereEinladungen.length; i++){

          einladungAblehnen(andereEinladungen[i]);
          DBMeldungenService.loescheMeldung(andereEinladungen[i]);

        }
    }


    var getStudentenString = function(){

      return studentenStr[studStrIndex];
    }

    var getMeineTeams = function(){
      return meineTeams;
    }


    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // -----------------------------------------------------------------
    return {

      teams: teams,
      studentenToString : studentenToString,
      initStudenten : initStudenten,
      waehleTeamMitgliedAus : waehleTeamMitgliedAus,
      istGueltigerStudent : istGueltigerStudent,
      studentenStr : studentenStr,
      aktuelleAuswahl : aktuelleAuswahl,
      getMeineTeams: getMeineTeams,
      entferneTeamMitgliedAuswahl : entferneTeamMitgliedAuswahl,
      uebernehmeAusgewMitglieder : uebernehmeAusgewMitglieder,
      initTeamTabelleKonfig : initTeamTabelleKonfig,
      initTeamTabelleMeineTabelle: initTeamTabelleMeineTabelle,
      bestaetigeTeamkonfig : bestaetigeTeamkonfig,
      alleBestaetigt: alleBestaetigt,
      initEintraege: initEintraege,
      getStudStrIndex, getStudStrIndex,
      getTeameintraege: getTeameintraege,
      getGruppe: getGruppe,
      verlasseTeam: verlasseTeam,
      sucheTeam: sucheTeam,
      initSucheNeuesMitglied: initSucheNeuesMitglied,
      sucheGruppe: sucheGruppe,
      getGruppen: getGruppen,
      getUeberschneidungen: getUeberschneidungen,
      einladungAnnehmen: einladungAnnehmen,
      getAktuelleAuswahl: getAktuelleAuswahl,
      maxMitgliederErreicht: maxMitgliederErreicht,
      alleTeamsVollstaendig: alleTeamsVollstaendig,
      setzeAlleTeamsZurueck: setzeAlleTeamsZurueck,
      setzeTeamZurueck: setzeTeamZurueck,
      getStudentenString: getStudentenString,
      einladungAblehnen: einladungAblehnen,
      zieheEinladungZurueck: zieheEinladungZurueck,
      initUeberschneidungen: initUeberschneidungen,
      pruefeUeberschneidungen: pruefeUeberschneidungen,
      isEinzelAnmelder: isEinzelAnmelder

    };
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBTeamService", DBTeamService);

  // Code sofort ausführen
}());
