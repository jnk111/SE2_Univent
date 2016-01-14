/**
 * Managed die Extensions der einzelnen Tabs
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var LeistungenController = function($scope, DBLeistService) {

    // Locals
    // ------------------
    var eintrIndex;
    var noteRegex = /^([0-5]\.\d{1,2})|([6]\.[0])$/;
    var punkteRegex = /^([0-9])|([1][0-5])$/;
    var noteAlphRegex = /^[A-Fa-f]$/;
    // ------------------


    // Vorerst Hardcoded Daten zur Demonstration
    // Später dynamische Ermittlung durch Spring MVC
    // =================================================================================================================================================
    $scope.noten = DBLeistService.noten;
    $scope.pvls = DBLeistService.pvls;
    $scope.faecher = DBLeistService.faecher;
    $scope.gruppen = DBLeistService.gruppen;
    $scope.semester = DBLeistService.semester;
    $scope.fachbereiche = DBLeistService.fachbereiche;
    $scope.typen = DBLeistService.typen;
    $scope.teilnehmer = DBLeistService.getLeistungen();

    // ==================================================================================================================================================


    // Scope-Felder - Interaktion mit View
    // --------------------------------------------------
    $scope.ls = {};
    $scope.ls.matrNr;
    $scope.ls.vorn;
    $scope.ls.nachn;
    $scope.ls.grpNr = DBLeistService.grpNr;
    $scope.ls.pvl;
    $scope.ls.note;
    $scope.ls.eintrNote;
    $scope.ls.eintrPVL = DBLeistService.pvls[0];
    $scope.ls.datumNote;
    $scope.ls.datumPVL;
    $scope.ls.fach;
    $scope.ls.gruppe = DBLeistService.getGruppen()[0];
    $scope.ls.sem = DBLeistService.semester[0];
    $scope.ls.fb = DBLeistService.fachbereiche[0];
    $scope.ls.typ = DBLeistService.typen[0];
    $scope.ls.sys = "punkte";
    // --------------------------------------------------


    // Funktionen um Ergebnisse darzustellen
    // --------------------------------------------------

    // Gibt das Success-Icon zurück
    $scope.getSuccess = function() {
      return "resources/apanel/icons/success.html";
    }

    // Gibt das Fail-Icon zurück
    $scope.getFail = function() {
      return "resources/apanel/icons/fail.html";
    }

    // Gibt das Open-Icon zurück
    $scope.getOpen = function() {
      return "resources/apanel/icons/open.html";
    }

    // Prüft ob Teilnehmer benotet wurde
    $scope.isOpenNote = function(matrNr) {

    	 var index = DBLeistService.sucheLeistung(matrNr);
        return parseInt(DBLeistService.getLeistungen()[index].note.note) === -1;
      }
      // Prüft ob Teilnehmer Klausur bestanden hat
    $scope.isSuccessNote = function(matrNr) {
      var index = DBLeistService.sucheLeistung(matrNr);
      var note = DBLeistService.getLeistungen()[index].note.note;

      if(noteRegex.test(note)){
        return parseFloat(note) <= 4.0;
      }

      if(punkteRegex.test(note)){
        return parseInt(note) > 4;
      }

      if(noteAlphRegex.test(note)){
        return (note >= 'A' && note < 'E') || (note >= 'a' && note < 'e');
      }
    }

    // Prüft ob Teilnehmer die PVL erhalten hat
    $scope.isSuccessPVL = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);
      return DBLeistService.getLeistungen()[index].pvl == true;
    }

    // Prüft ob PVL noch offen ist
    $scope.isOpenPVL = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);
      return DBLeistService.getLeistungen()[index].pvl == false &&
              DBLeistService.getLeistungen()[index].datumPVL == null;
    }

    // Gibt die Beschreibung für einen PVLTyp (erfolgreich, nicht erfolgreich, offen)
    var getPVLBeschreibung = function(matrNr){

      var index = DBLeistService.sucheLeistung(matrNr);
      var discr = "";
      if($scope.isSuccessPVL(matrNr)){
        discr = "bestanden";
      }else if($scope.isOpenPVL(matrNr)){
        discr = "offen";
      }else if(!$scope.isSuccessPVL(matrNr)){
        discr = "nicht erfolgreich";
      }

      return discr;
    }

 // Gibt die Beschreibung für eine Note (offen oder konkrete Note)
    var getNoteBeschreibung = function(matrNr){
      var index = DBLeistService.sucheLeistung(matrNr);
      var discr = "";

      if($scope.isOpenNote(matrNr)){
        discr = "offen";
      }else{
        discr = DBLeistService.getLeistungen()[index].note.note;
      }

      return discr;
    }

    // Sucht den Boolean-Wert für eine gegebene Beschreibung
    var getBooleanWertPVL = function(pvl, index){

    	if(pvl == $scope.pvls[0]){
    		return true;
    	}
    	if(pvl == $scope.pvls[1] || pvl == $scope.pvls[2]){
    		return false;
    	}
    }


    /**
     * Initialisiert die Teilnehmerdetails in der Leistungen-View - Angestellter
     */
    $scope.initTmDetailsLeist = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);

      $scope.ls.matrNr = DBLeistService.getLeistungen()[index].student.matrNr.matrNr;
      $scope.ls.vorn = DBLeistService.getLeistungen()[index].student.vorname;
      $scope.ls.nachn = DBLeistService.getLeistungen()[index].student.nachname;
      $scope.ls.fach = DBLeistService.getLeistungen()[index].fach.fachKuerzel;
      $scope.ls.datumPVL = DBLeistService.getLeistungen()[index].datumPVL;
      $scope.ls.datumNote = DBLeistService.getLeistungen()[index].datumNote;
      $scope.ls.pvl = getPVLBeschreibung(matrNr);
      $scope.ls.note = getNoteBeschreibung(matrNr);
    }

    /**
     * Initialisiert den Tabelleneintrag fuer den eine Note eingetragen
     * werden soll
     */
    $scope.initEintragen = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);
      eintrIndex = index;
    }

    /**
     * Ruft die Backendschnittstelle zum Noteneintragen auf und uebergibt
     * die einzutragen Note
     */
    $scope.trageNoteEin = function() {


      var datumNote = new Date(); // Heutiges Datum
      var note = $scope.ls.eintrNote;

      DBLeistService.trageNoteEin(eintrIndex, datumNote, note);

    }

    /**
     * Ruft die Backendschnittstelle zum PVL-eintragen auf und uebergibt
     * den einzutragenen PVL-Wert
     */
    $scope.tragePVLEin = function() {

      var datumPVL = new Date(); // Heutiges Datum
      var pvl = getBooleanWertPVL($scope.ls.eintrPVL);

      if($scope.ls.eintrPVL == $scope.pvls[2]){

        datumPVL = null;
      }

      DBLeistService.tragePVLEin(eintrIndex, datumPVL, pvl);
    }

    /**
     * Ruft die Backendschnittstelle zum Initialisieren der bewerteten und 
     * unbewerteten Leistungen und listet diese in der View auf
     * Parameter sind der Fachbereich, der Veranstaltungstyp und das
     * Semester
     */
    $scope.initLeistungen = function() {

      $scope.ls.fach = DBLeistService.updateFaecher($scope.ls.fb, $scope.ls.typ, $scope.ls.sem)[0];


    }

    $scope.initLeistungenFuerFach = function(fach){

      DBLeistService.initLeistungenFuerFach(fach);
    }

    $scope.getLeistungen = function(){
      return DBLeistService.getLeistungen();
    }



    $scope.getFaecher = function(){

      return DBLeistService.getFaecher();
    }

    $scope.getGruppen = function(){

      //$scope.ls.gruppe = DBLeistService.getGruppen()[0];
      return DBLeistService.getGruppen();
    }


    $scope.setNoteSys = function(){

      $scope.ls.sys = "note";

    }

    $scope.setPunkteSys = function(){

      $scope.ls.sys = "punkte";

    }

    $scope.setNoteAlphSys = function(){

      $scope.ls.sys = "alphNote";

    }


    $scope.istGueltigeNote = function(note){

      var regEx;
      if($scope.ls.sys == "note"){
        regEx = noteRegex;

        return regEx.test(note) && note.length < 5;
      }

      if($scope.ls.sys == "punkte"){
        regEx = punkteRegex;

        return regEx.test(note) && note.length < 3 && parseInt(note) < 16;
      }

      if($scope.ls.sys == "alphNote"){
        regEx = noteAlphRegex;

        return regEx.test(note) && note.length < 2;
      }

      return false;

    }

    $scope.isFilledComplete = function(){

      return $scope.istGueltigeNote($scope.ls.eintrNote);
    }

    /*
    // Zeigt die Teilnehmner mit ihren Leistungen für eine gegebene Guppe und Fachkuerzel
    $scope.leistungenAnzeigen = function(){

    	if(!DBLeistService.zeigeLeistungenAn($scope.ls.gruppe, $scope.ls.fach)){
    		// Fehlermeldung
    	}
    }

    // Holt alle eingetragenen Gruppennummern für ein fach (fachkuerzel)
    $scope.holeGruppenNummern = function(){

    	if(!DBLeistService.gruppennummernDropDown($scope.ls.fach, $scope.ls.gruppe)){

    		// Fehlermeldung
    	}
    }
      // --------------------------------------------------
      */
  };

  // Controller bei der App "anmelden"
  app.controller("LeistungenController", LeistungenController);

  // Code sofort ausführen
}());
