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


    // ==================================================================================================================================================


    // Scope-Felder - Interaktion mit View
    // --------------------------------------------------

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
        return parseInt($scope.teilnehmer[index].note.note) === -1;
      }
      // Prüft ob Teilnehmer Klausur bestanden hat
    $scope.isSuccessNote = function(matrNr) {
      var index = DBLeistService.sucheLeistung(matrNr);
      var note = $scope.teilnehmer[index].note.note;

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
    	if(pvl == $scope.pvls[1] || pvl == $scope.pvls[2]){
    		return false;
    	}
    }


    /**
     * Initialisiert mithilfe des Backends die bewerteten Leistungen
     * des aktuell eingeloggten Studenten un zeigt diese an.
     */
    $scope.initLeistungen = function() {

    	DBLeistService.updateFaecher($scope.ls.fb, $scope.ls.typ, $scope.ls.sem);
      $scope.ls.fach = DBLeistService.getFaecher()[0];
      $scope.ls.gruppe = DBLeistService.getGruppen()[0];
      $scope.teilnehmer = DBLeistService.getLeistungen();
    }


    $scope.getLeistungen = function(){
      return DBLeistService.getLeistungen();
    }


  };

  // Controller bei der App "anmelden"
  app.controller("LeistungenController", LeistungenController);

  // Code sofort ausführen
}());
