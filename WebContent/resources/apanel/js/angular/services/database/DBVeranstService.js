/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var DBVeranstService = function($http, DBErrorService, URLService) {

	   // Locls
	   // ####################################################################################
	   var semester = [1, 2, 3, 4, 5, 6];
	   var fachbereiche = ["AI", "TI", "WI"];

	   var fachbereich = fachbereiche[0];
     var url = URLService.getUrl();
	   var veranstaltungen = []; // Hier werden die ermittelten Daten temporär gespeichert um schnelles Anzeigen zu gewährleisten
     var wps = [];
     var projekte = [];
	   var professoren = []; // Puffer für die Livesuche
     var professorenStr = [];
	   var assistenten = ["Ilona Blank", "Gerhard Oelker", "Hartmut Schulz"];
	   var error = false; // Flag zur Fehlererkennung
     var vaTyp = "";
	    // ####################################################################################

    // POJO-Klassen
    // ====================================================================================

    function Fach(fachKuerzel){

      this.fachKuerzel = fachKuerzel;
      this.fachBezeichnung = null;
      this.fachbereich = null;
      this.semester = null;
      this.fBKuerzel = null;
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

    function Veranstaltung(fach, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam){

      this.fach = fach;
      this.professor = prof;
      this.anzTm = anzTm;
      this.anzGr = anzGr;
      this.minTeilnTeam = minTeilnTeam;
      this.maxTeilnTeam = maxTeilnTeam;
      this.semester = null;
      this.typ = null;
    }

    function MAID(id){

      this.id = id;

    }

    function EMail(email){
      this.email = email;
    }

    function PLZ(plz){
      this.plz = plz;
    }

    function Adresse(strasse, hausNr, plz, stadt, land){

      this.strasse = strasse;
      this.hausNr = hausNr;
      this.plz = plz;
      this.stadt = stadt;
      this.land = land;
    }

    // Helper
    // ====================================================================================

    var removeReduntantProfinfo = function(veranst){

      var prof = veranst.professor;
      prof.maID = null;
      prof.vollerName = null;
      prof.benutzername = null;
      prof.gebDatum = null;
      prof.gebOrt = null;
      prof.adresse = null;
      prof.department = null;
      prof.fachbereich = null;
      prof.email = null;

      return prof;
    }

    var setVATyp = function(typ){
      vaTyp = typ;
    }

    var getVATyp = function(){

      return vaTyp;
    }

    var getVeranstaltungen = function(){
      return veranstaltungen;
    }

    var getWPs = function(){
      return wps;
    }

    var getProjekte = function(){

      return projekte;
    }

      var sucheVA = function(fach){
        for(i = 0; i < veranstaltungen.length; i++){
          if(veranstaltungen[i].fach.fachKuerzel == fach.fachKuerzel){
            return i;
          }
        }
        return -1;
      }

      var getFachbereich = function(){
        return fachbereich;
      }

      var getSemester = function(){
        return semester;
      }

      var getAngestellter = function(typ, vollerName){
        var name = vollerName.split(" ");
        var vorname = name[0];
        var nachname = name[1];
        var prof = new Angestellter(typ, vorname, nachname);
        prof.vollerName = vollerName;

        return prof;

      }


    // Vorerst Hardcoded Daten zur Demonstration
    // Später dynamische Ermittlung durch Spring MVC
    // ====================================================================================

    var gka = new Fach("GKAP");
    var ad = new Fach("ADP");
    var bs = new Fach("BSP");
    var se = new Fach("SEP1");
    var bw = new Fach("BWP2");

    var id = new MAID(10);
    var plz = new PLZ(11111);
    var email = new EMail("test@test.de");
    var gebDatum = new Date();
    var adresse = new Adresse("Straße", 7, 1111, "Stadt", "Land");
    var padberg = new Angestellter("professor", "Julia", "Padberg");
    var kleine = new Angestellter("professor", "Martin", "Kleine");
    var huebner = new Angestellter("professor", "Martin", "Huebner");
    var zukunft = new Angestellter("professor", "Olaf", "Zukunft");
    var gerken = new Angestellter("professor", "Wolfgang", "Gerken");


    var gkap = new Veranstaltung("praktikum", gka, padberg, 20, 4, 2, 3);
    var adp = new Veranstaltung("praktikum", ad, kleine, 20, 4, 2, 3);
    var bsp = new Veranstaltung("praktikum", bs, huebner, 20, 4, 2, 3);
    var sep = new Veranstaltung("praktikum", se, zukunft, 20, 4, 2, 3);
    var bwp = new Veranstaltung("praktikum", bw, gerken, 20, 4, 2, 3);


    // Array aus Veranstaltungsobjekten
    var hcVeranstaltungsDaten = [gkap, adp, bsp, sep, bwp];
    // ====================================================================================


    // SCHNITTSTELLE
    // #########################################################################################################################

    // TODO: Diese Methoden müssen implementiert/ergänzt werden
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Beispiel für eine implementierte Operation der Schnittstelle -> Alle Pflichtpraktika aus der Datenbank ermitteln

    // Alle Pflichtpraktika zu den übergebenen Parametern ermitteln
    // Ruft Backend-Schnittstelle auf und übergibt die Parameter
    // Ermittelte Pfichtpraktika, werden in Array gespeichert
    // Diese werden dann automatisch in der View sichtbar
    var initPraktika = function(sem, fachbereich) {

      var b = false;
      // JSOM Data
      var args = [sem, fachbereich];
      veranstaltungen = [];
      $http.post(url+"pflichtpraktika", angular.toJson(args)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {


          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;
            var praktikum = new Veranstaltung(data[i].fach, data[i].professor, data[i].anzTm, data[i].anzGr, data[i].minTeilnTeam, data[i].maxTeilnTeam);
            veranstaltungen.push(praktikum);
            b = true;
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });



    }



    // Alle WP zu den übergebenen Parametern ermitteln
    // Ruft Backend-Schnittstelle auf und übergibt die Parameter
    // Ermittelte WP, werden in Array gespeichert
    // Diese werden dann automatisch in der View sichtbar TODO: View implementieren
    var initWP = function(sem, fachbereich){

      var b = false;
      // JSOM Data
      var args = [sem, fachbereich];
      veranstaltungen = [];
      $http.post(url+"wp", angular.toJson(args)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {


          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;
            var praktikum = new Veranstaltung(data[i].fach, data[i].professor, data[i].anzTm, data[i].anzGr, data[i].minTeilnTeam, data[i].maxTeilnTeam);
            veranstaltungen.push(praktikum);
            b = true;
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }

    // Alle PO zu den übergebenen Parametern ermitteln
    // Ruft Backend-Schnittstelle auf und übergibt die Parameter
    // Ermittelte WP, werden in Array gespeichert
    // Diese werden dann automatisch in der View sichtbar TODO: View implementieren
    var initProfs = function(fachbereich, semester){

      var fb = fachbereich;
      var sem = semester;

      // JSOM Data
      var args = [sem, fb];
      professoren = [];
      professorenStr = [];
      $http.post(url+"profs", angular.toJson(args)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {

        var data = response.data;
          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {

            //typ, maID, vorname, nachname
            var maid = data[i].maID;
            var vorname = data[i].vorname;
            var nachname = data[i].nachname;
            var prof = new Angestellter("Professor", maid, vorname, nachname);
            professoren.push(prof);
            professorenStr.push(prof.maID.id + " " + prof.vorname + " " + prof.nachname);
          }
        },
        // Funktion bei Fehler
        function(response) {
          DBErrorService.setError(false);
        });

    }

    var sucheProfessor = function(maid, profs){

      for(var i = 0; i < profs.length; i++){

        if(maid.id == profs[i].maID.id){
          return i;
        }
      }

      return -1;
    }
    var istGueltigerProf = function(expr){

      if(expr != "" && expr != undefined && expr != null && expr.indexOf(" ") > -1)           {

        var maid = expr.split(' ');

        if((maid[0].match("[1-9]+"))){

          var maid = new MAID(parseInt(maid[0]));

          var profIndex = sucheProfessor(maid, professoren);
          if(profIndex > -1){
            return professoren[profIndex];
          }
        }
      }
        return null;
    }

    var vaVorhanden = function(fk){

      for(var i = 0; i < veranstaltungen; i++){

        if(veranstaltungen[i].fach.fachKuerzel == fk){
          return true;
        }
      }

      return false;
    }
    var initPO = function(sem, fachbereich){

      var b = false;
      // JSOM Data
      var args = [sem, fachbereich];
      veranstaltungen = [];
      $http.post(url+"po", angular.toJson(args)).
        // Funktion, falls gültige Daten zurückkommen
      then(function(response) {


          // Daten aus dem Response-Object in das Veranstaltungen-Array pushen
          for (i = 0; i < response.data.length; i++) {
            var data = response.data;
            var praktikum = new Veranstaltung(data[i].fach, data[i].professor, data[i].anzTm, data[i].anzGr, data[i].minTeilnTeam, data[i].maxTeilnTeam);
            veranstaltungen.push(praktikum);
            b = true;
          }
        },
        // Funktion bei Fehler
        function(response) {
          b = false;
          DBErrorService.setError(false);
        });


    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    // Speichert eine neue Veranstaltung
    var addVeranstaltung = function(veranstaltungsInfo) {

      var fach = veranstaltungsInfo.fach;
      fach.fBKuerzel = fach.fachbereich;
      var minTeilnTeam = veranstaltungsInfo.minTeilnTeam;
      var maxTeilnTeam = veranstaltungsInfo.maxTeilnTeam;
      var dozent = veranstaltungsInfo.dozent;

      // fach, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam

      var veranstaltung = new Veranstaltung(fach, dozent, 0, 0, minTeilnTeam, maxTeilnTeam);
      veranstaltung.semester = fach.semester;
      veranstaltung.typ = getVATyp();

      veranstaltungen.push(veranstaltung);

      // JSOM Data
      $http.post(url+"vErstellen", angular.toJson(veranstaltung)).
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

    // Editiiert eine vorhndene Veranstaltung
    var editVeranstaltung = function(index, va){

      var prof = va.dozent;
      var maxTeilnTeam = va.maxTeilnTeam;
      var minTeilnTeam = va.minTeilnTeam;

      var veranstaltung = veranstaltungen[index];
      veranstaltung.professor = prof;
      veranstaltung.maxTeilnTeam = maxTeilnTeam;
      veranstaltung.minTeilnTeam = minTeilnTeam;


      // JSOM Data
      $http.post(url+"vbearbeiten", angular.toJson(veranstaltung)).
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

    // Loescht eine vorhandene Veranstaltung
    var loescheVeranstaltung = function(index){

      var veranst = veranstaltungen[index];
      console.log(index);
      veranstaltungen.splice(index, 1);

      // JSOM Data
      $http.post(url+"vLoeschen", angular.toJson(veranst)).
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

    var getProfessoren = function(){
      return professoren;
    }

    var getProfessorenString = function(){
      return professorenStr;
    }

    var getFachbereiche = function(){

      return fachbereiche;
    }



    // #########################################################################################################################

    // Gebe dieses Object zurück und mache die Schnittstelle zugänglich für Client
    // -----------------------------------------------------------------------------------
    return {
      semester: semester,
      fachbereiche: fachbereiche,
      getFachbereiche: getFachbereiche,
      addVeranstaltung: addVeranstaltung,
      editVeranstaltung: editVeranstaltung,
      hcVeranstaltungsDaten: hcVeranstaltungsDaten,
      loescheVeranstaltung: loescheVeranstaltung,
      istGueltigerProf: istGueltigerProf,
      sucheProfessor: sucheProfessor,
      sucheVA: sucheVA,
      getProfessorenString: getProfessorenString,
      getFachbereich: getFachbereich,
      getSemester: getSemester,
      initPraktika: initPraktika,
      vaVorhanden: vaVorhanden,
      initWP: initWP,
      setVATyp: setVATyp,
      getVATyp: getVATyp,
      initPO: initPO,
      getAngestellter: getAngestellter,
      getProfessoren: getProfessoren,
      assistenten: assistenten,
      veranstaltungen: veranstaltungen,
      getVeranstaltungen: getVeranstaltungen,
      removeReduntantProfinfo: removeReduntantProfinfo,
      getWPs: getWPs,
      getProjekte: getProjekte,
      initProfs:  initProfs

    };
    // -----------------------------------------------------------------------------------
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBVeranstService", DBVeranstService);

  // Code sofort ausführen
}());
