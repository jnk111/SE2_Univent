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
    $scope.teilnehmer = DBLeistService.hcLeistungen;

    // ==================================================================================================================================================


    // Scope-Felder - Interaktion mit View
    // -------------------------------------
    $scope.ls = {};
    $scope.ls.matrNr;
    $scope.ls.vorn;
    $scope.ls.nachn;
    $scope.ls.grpNr = DBLeistService.grpNr;
    $scope.ls.pvl;
    $scope.ls.note;
    $scope.ls.eintrNote = 5;
    $scope.ls.eintrPVL = DBLeistService.pvls[0];
    $scope.ls.fach = DBLeistService.faecher[0];
    $scope.ls.gruppe = DBLeistService.gruppen[1];
    $scope.ls.sem = DBLeistService.semester[2];
    $scope.ls.fb = DBLeistService.fachbereiche[0];
    $scope.ls.typ = DBLeistService.typen[0];
    // -------------------------------------


    // Funktionen um Ergebnisse darzustellen
    // --------------------------------------------------

    // Gibt das Success-Icon zurück
    $scope.getSuccess = function() {
      return "resources/icons/success.html";
    }

    // Gibt das Fail-Icon zurück
    $scope.getFail = function() {
      return "resources/icons/fail.html";
    }

    // Gibt das Open-Icon zurück
    $scope.getOpen = function() {
      return "resources/icons/open.html";
    }

    // Prüft ob Teilnehmer benotet wurde
    $scope.isOpenNote = function(matrNr) {

    	var index = DBLeistService.sucheLeistung(matrNr);
        return $scope.teilnehmer[index].note.note === -1;
      }
      // Prüft ob Teilnehmer Klausur bestanden hat
    $scope.isSuccessNote = function(matrNr) {
      var index = DBLeistService.sucheLeistung(matrNr);
      return $scope.teilnehmer[index].note.note > 4;
    }

    // Prüft ob Teilnehmer die PVL erhalten hat
    $scope.isSuccessPVL = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);
      return $scope.teilnehmer[index].pvl == true;
    }

    // Prüft ob PVL noch offen ist
    $scope.isOpenPVL = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);
      return $scope.teilnehmer[index].pvl == false
              && $scope.teilnehmer[index].datumPVL == null;
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
        discr = $scope.teilnehmer[index].note.note;
      }

      return discr;
    }

    // Sucht den Boolean-Wert für eine gegebene Beschreibung
    var getBooleanWertPVL = function(pvl, index){

    	if(pvl == $scope.pvls[0]){
    		return true;
    	}
    	if(pvl == $scope.pvls[1]){
    		return false;
    	}
    	if(pvl == $scope.pvls[2]){
    		$scope.teilnehmer[index].datumPVL = null;
    		return false;
    	}
    }


    // Iinitlaisiert das Teilnehmer-Detail-Popup
    $scope.initTmDetailsLeist = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);

      $scope.ls.matrNr = $scope.teilnehmer[index].student.matrNr.matrNr;
      $scope.ls.vorn = $scope.teilnehmer[index].student.vorname;
      $scope.ls.nachn = $scope.teilnehmer[index].student.nachname;
      $scope.ls.fach = $scope.teilnehmer[index].fach.fachKuerzel;
      $scope.ls.pvl = getPVLBeschreibung(matrNr);
      $scope.ls.note = getNoteBeschreibung(matrNr);
    }

    // Initialisiert den Index des Teilnehmers, der bewertet werden soll
    $scope.initEintragen = function(matrNr) {

      var index = DBLeistService.sucheLeistung(matrNr);
      eintrIndex = index;
    }

    // Trägt eine Note ein
    $scope.trageNoteEin = function() {


      var datumNote = new Date(); // Heutiges Datum
      var note = $scope.ls.eintrNote;

      if(!DBLeistService.trageNoteEin(eintrIndex, datumNote, note)){
        // Fehlermeldung
      }
    }

    // Trägt eine PVL ein
    $scope.tragePVLEin = function() {

      var datumPVL = new Date(); // Heutiges Datum
      var pvl = getBooleanWertPVL($scope.ls.eintrPVL);

      if(!DBLeistService.tragePVLEin(eintrIndex, datumPVL, pvl)){

        // Fehlermeldung
      }
    }

    // Initisalisiert alle Teilnehemr und deren Leistungen anhand der $scope.ls - Paramter
    $scope.initLeistungen = function(){

    	$scope.updateFacher($scope.ls.typ.toLowerCase(), $scope.ls.fb, $scope.ls.sem);
    	$scope.holeGruppenNummern($scope.ls.typ.toLowerCase(), $scope.ls.fach);
    	$scope.leistungenAnzeigen($scope.ls.gruppe, $scope.ls.fach);
    }

    // Updated die Faecher im Dropdownmenu andhand von typ, fachbereich und semester
    $scope.updateFaecher = function(){

    	DBLeistService.updateFaecher($scope.ls.typ.toLowerCase(), $scope.ls.fb, $scope.ls.sem);
    	$scope.ls.fach = $scope.faecher[0];
    }

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
  };

  // Controller bei der App "anmelden"
  app.controller("LeistungenController", LeistungenController);

  // Code sofort ausführen
}());
