/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBGruppTmService = function($http, DBGruppService, DBErrorService, URLService) {


    // Locals
    //########################################################################
    var gruppe;
    var teams = []; // hier werden alle Teams gespeichert
    var eintraege = []; // Ersetzt später hcTeilnehmer
    var url = URLService.getUrl();
    //var url = "http://localhost:8080/SE2-Praktikumssoftware/"; // URL um Backend anzusprechen
    // #######################################################################

    // POJO Klassen
    // #######################################################################


    function TeamID(id){
    	this.id = id;
    }

    function Team(teamId, grpNr,  minTeiln, maxTeiln, mitglieder){
      this.grpNr = grpNr;
      this.teamId = teamId;
      this.minTeiln = minTeiln;
      this.maxTeiln = maxTeiln;
      this.mitglieder = mitglieder;
    }

    function MatrikelNr(matrNr){
    	this.matrNr = matrNr;
    }

    function Student(matrNr, vorname, nachname){

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

    function TMEintrag(student, teamID){

    	this.student = student;
    	this.teamID = teamID;
    }


 // #######################################################################


    // Vorerst Hardcoded Daten zur Demonstration
    // Später dynamische Ermittlung durch Spring MVC
    // =================================================================================================================================================

    var matrNr1 = new MatrikelNr(243441);
    var matrNr2 = new MatrikelNr(243442);
    var matrNr3 = new MatrikelNr(243443);
    var matrNr4 = new MatrikelNr(243444);
    var matrNr5 = new MatrikelNr(243445);
    var matrNr6 = new MatrikelNr(243446);

    var student1 = new Student(matrNr1, "Max", "Mustermann1");
    var student2 = new Student(matrNr2, "Max", "Mustermann2");
    var student3 = new Student(matrNr3, "Max", "Mustermann3");
    var student4 = new Student(matrNr4, "Max", "Mustermann4");
    var student5 = new Student(matrNr5, "Max", "Mustermann5");
    var student6 = new Student(matrNr6, "Max", "Mustermann6");

    var paar1 = [student1, student3, student4];
    var paar2 = [student2, student6];
    var paar3 = [student4, student5];

    var teamId1 = new TeamID(1);
    var teamId2 = new TeamID(2);
    var teamId3 = new TeamID(3);

    var team1 = new Team(teamId1, 2, 2, paar1);
    var team2 = new Team(teamId2, 2, 2, paar2);
    var team3 = new Team(teamId3, 2, 2, paar3);

    var eintrag1 = new TMEintrag(student1, teamId1);
    var eintrag2 = new TMEintrag(student3, teamId1);
    var eintrag3 = new TMEintrag(student2, teamId2);
    var eintrag4 = new TMEintrag(student6, teamId2);
    var eintrag5 = new TMEintrag(student4, teamId3);
    var eintrag6 = new TMEintrag(student5, teamId3);


    var hcTeilnehmer = [eintrag1, eintrag2, eintrag3, eintrag4, eintrag5, eintrag6];

    // ==================================================================================================================================================

    //Helper
    // ##########################################################################
    var setFach = function(fa){
      fach = fa;
    }

    var getFach = function(){
      return fach;
    }

    var setGruppe = function(gr){
      gruppe = gr;
    }

    var getGruppe = function(){
      return gruppe;
    }

    var sucheStudent = function(matrNr){

      for(i = 0; i < eintraege.length; i++){
        if(eintraege[i].student.matrNr == matrNr){
          return i;
        }
      }
      return -1;
    }

    var sucheTeamPartner = function(teamId, matrNr){
    	var partner = [];
    	for(i = 0; i < eintraege.length; i++){
    		if(eintraege[i].teamID == teamId
    			&& eintraege[i].student.matrNr != matrNr){

    			partner.push(eintraege[i].student);
    		}
    	}

    	return partner;
    }

    var getTeamID = function(matrNr){

    	for(i = 0; i < hcTeilnehmer.length; i++){
    		if(hcTeilnehmer[i].student.matrNr == matrNr){
    			return hcTeilnehmer[i].teamID;
    		}
    	}

    	return -1;
    }

    var getEintraege = function(){
      return eintraege;
    }
    // ##########################################################################



    // SCHNITTSTELLE
    // ################################################################################################################

    // TODO: Diese Methoden müssen implementiert werden.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Einen Studenten aus der Gruppe (Datenbank) löschen
    // Ruft die Schnittstelle im Backend auf und übergibt die Informationen
    // für den zu löschenden Studenten
    var loescheTmDB = function(student){

      var teamID = getTeamID(student.matrNr);
      var args = [teamID, student.matrNr];	// TeamID und MatrikelNummer
      										// Werden als Json übergeben

      return true;
    }


    // Alle Teams zu einer Gruppe aus der DB ermitteln
    // Ruft die Schnittstelle im Backend auf und holt Teilnehner
    // zu einer übergebenen Gruppe (Json)
    // Teams müssen im Array "teams" gespeichert werden
    // Jeder Teilnehmer für jedes Team wird dann automatisch in der View ausgegeben.
    var initTmUebersicht = function(gruppe){

      eintraege = [];
      teams = [];
    	setGruppe(gruppe);

      // JSOM Data
      var args = gruppe;
      $http.post(url+"tmUebersicht", angular.toJson(args)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {


          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;
            var teamId = data[i].teamID;
            var grpNr = data[i].grpNr;
            var mitglieder = data[i].mitglieder;
            var minTeiln = data[i].minTeiln;
            var maxTeiln = data[i].maxTeiln;
            var team = new Team(teamId, grpNr, minTeiln, maxTeiln, mitglieder);
            teams.push(team);
            erstelleEintraege(teams);
          }
        },
        // Funktion bei Fehler
        function(response) {

          DBErrorService.setError(false);
        });




    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Erstellt Einträge im TeamArray
    // Wird in der View dargestellt
    var erstelleEintraege = function(teams){
    	for(i = 0; i < teams.length; i++){

    		var teamID = teams[i].teamId;
    		var mitglieder = teams[i].mitglieder;

    		for(j = 0; j < mitglieder.length; j++){
    			var student = mitglieder[j];
    			eintraege.push(new TMEintrag(student, teamID));
    		}
    	}
    }


    // Einen Studenten aus der Gruppe löschen
    var loescheTm = function(index){

      var tm = hcTeilnehmer[index].student;

      if(!loescheTmDB(tm)){
        return false;
      }else{
        hcTeilnehmer.splice(index, 1);
      }
    }



    // #########################################################################################################################

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {
      hcTeilnehmer: hcTeilnehmer,
      initTmUebersicht: initTmUebersicht,
      setFach: setFach,
      getFach: getFach,
      getGruppe: getGruppe,
      setGruppe: setGruppe,
      sucheStudent: sucheStudent,
      loescheTm: loescheTm,
      sucheTeamPartner: sucheTeamPartner,
      getEintraege: getEintraege
    };
    // -----------------------------------------------------------------------------------

  };


  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBGruppTmService", DBGruppTmService);

  // Code sofort ausführen
}());
