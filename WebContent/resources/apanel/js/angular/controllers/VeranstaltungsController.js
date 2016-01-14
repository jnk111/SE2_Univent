/**
 * Managed die Veranstaltungen
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object, $http: holt JSON Daten via SpringMVC Backend -> folgt später
  var VeranstaltungsController = function($scope, autoscroller, DBVeranstService, DBGruppService /*, $http*/ ) {

    // Locals
    // ###########################################################################################################
    var loesch;
    var indexEdit;
    // ###########################################################################################################


    // Felder initialisiern
    // ###########################################################################################################
    $scope.veranstaltungen = DBVeranstService.getVeranstaltungen();
    $scope.wahlpflicht = DBVeranstService.getWPs();
    $scope.fachbereiche = DBVeranstService.fachbereiche;
    $scope.semester = DBVeranstService.getSemester();
    $scope.professoren = DBVeranstService.getProfessoren();
    $scope.professorenString = DBVeranstService.getProfessorenString();
    $scope.profEdit;

    $scope.va = {}; // Veranstaltungsobject anlegen
    $scope.va.fach = {}; // Attribut Fach anlegen und initialsieren
    $scope.va.fach.fachBezeichnung = "";
    $scope.va.fach.fachKuerzel = "";
    $scope.va.fach.semester = DBVeranstService.getSemester()[0];
    $scope.va.fach.fachbereich = DBVeranstService.getFachbereiche()[0];
    $scope.va.dozent = {}; // ...
    $scope.va.assistent = {};
    $scope.va.curr = 0;
    $scope.va.minTeilnTeam = 2;
    $scope.va.maxTeilnTeam = 3;

    // ###########################################################################################################


    //Preconditions -> TODO: Wird noch um weitere erweitert
    // ###########################################################################################################

    $scope.isValidNumber = function(x) {
      return x % 1 === 0 && x > 0;
    }

    $scope.isValidMin = function(min, max){

      return min <= max;
    }

    $scope.isValidMax = function(min, max){

      return max >= min;
    }

    $scope.isValidAnzTeilnehmer = function(anzTeiln) {
        return anzTeiln > 1 && anzTeiln < 11;
      }

      $scope.isValidProfInput = function(expr){

        $scope.va.dozent = DBVeranstService.istGueltigerProf(expr);
        return $scope.va.dozent != null;
      }

    $scope.isValidFachkuerzel = function(fachkuerzel){

    	return fachkuerzel != "" && fachkuerzel.length < 6 && fachkuerzel.length > 1;
    }

    $scope.vaVorhanden = function(fk){

      return DBVeranstService.vaVorhanden(fk);
    }

    $scope.isValidFachbezeichnung = function(fachb){

      return fachb != null && fachb != undefined && fachb != "";
    }

    // Bedingung um den Speichern-Button zu aktivieren/deaktivieren
    // TODO: Muss noch um restliche Felder ergänzt werden
    $scope.filledComplete = function() {

        return $scope.isValidAnzTeilnehmer($scope.va.minTeilnTeam)
                && $scope.isValidAnzTeilnehmer($scope.va.maxTeilnTeam)
                && $scope.isValidMin($scope.va.minTeilnTeam, $scope.va.maxTeilnTeam)
                && $scope.isValidMax($scope.va.minTeilnTeam, $scope.va.maxTeilnTeam)
                && $scope.va.dozent != null && $scope.isValidProfInput($scope.va.dozent.maID.id + " " + "platzhalter")
                && $scope.isValidFachkuerzel($scope.va.fach.fachKuerzel)
                && !$scope.vaVorhanden($scope.va.fach.fachKuerzel)
                && $scope.isValidFachbezeichnung($scope.va.fach.fachBezeichnung);


    }


    $scope.filledCompleteBearbeiten = function(profEdit) {

        return $scope.isValidProfInput(profEdit)
                 && $scope.isValidAnzTeilnehmer($scope.va.maxTeilnTeam);


    }



    // ###########################################################################################################-



    // Button-klick Funktionen
    // ###########################################################################################################



      	/**
      	 * Initialisiert di Gruppenuebersicht fuer eine Veranstaltung
      	 * mithilfe der Backend-Schnittstelle (Aufruf)
      	 */
        $scope.initGruppenUebersicht = function(fach) {

          var vaIndex = DBVeranstService.sucheVA(fach);
          var va = DBVeranstService.getVeranstaltungen()[vaIndex];
          DBVeranstService.initProfs(fach.fBKuerzel, fach.semester);
          DBGruppService.initAssit();
          if(!DBGruppService.initGruppen(va)){
            // Fehlermeldung
          }

        }


    /**
     * Initialisiert das Veranstaltung bearbeiten Popup mit den aktuellen
     * Werten
     */
    $scope.initVeranBearbeitenPopup = function(fach) {

      indexEdit = DBVeranstService.sucheVA(fach);
      DBVeranstService.initProfs(fach.fBKuerzel, fach.semester);
      $scope.va.maxTeilnTeam = DBVeranstService.getVeranstaltungen()[indexEdit].maxTeilnTeam;
      $scope.va.minTeilnTeam = DBVeranstService.getVeranstaltungen()[indexEdit].minTeilnTeam;
      $scope.va.dozent = DBVeranstService.getVeranstaltungen()[indexEdit].professor;
      $scope.profEdit = $scope.va.dozent.maID.id + " " + $scope.va.dozent.vorname + " " + $scope.va.dozent.nachname;
      $scope.va.fach.fachKuerzel = DBVeranstService.getVeranstaltungen()[indexEdit].fach.fachKuerzel;
      $scope.va.fach.fachBezeichnung = DBVeranstService.getVeranstaltungen()[indexEdit].fach.fachBezeichnung;
    }


    /**
     * Ruft die Backendschnittstelle zum Veranstaltung erstellen auf und
     * aktualisiert die View. Die zu erstelle Veranstaltung wird dem Backend 
     * uebergeben.
     */
    $scope.addVeranstaltung = function() {

      var va = {};
      va.fach = $scope.va.fach;
      va.dozent= $scope.va.dozent;
      va.minTeilnTeam = $scope.va.minTeilnTeam;
      va.maxTeilnTeam = $scope.va.maxTeilnTeam;

      DBVeranstService.addVeranstaltung(va);

      autoscroller.erstellen = null;
    }

    /**
     * Ruft die Backendschnittstelle zum Veranstaltung bearbeiten auf und
     * aktualisiert die View. Die zu bearbeitende Veranstaltung wird dem Backend 
     * uebergeben.
     */
    $scope.editVeranstaltung = function() {

      var edit = {};
      edit.dozent = $scope.va.dozent;
      edit.maxTeilnTeam = $scope.va.maxTeilnTeam;
      edit.minTeilnTeam = $scope.va.minTeilnTeam;

      DBVeranstService.editVeranstaltung(indexEdit, edit);

      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#modalBearbeiten').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }

    /**
     * Initialisiert die Veranstaltung die geloescht werden soll.
     * Sucht dazu den index in der Tabelle
     */
    $scope.initVeranstaltungLoeschen = function(fach) {
      loesch = DBVeranstService.sucheVA(fach);
    }

    /**
     * Loescht die zuvor initialisierte Veranstaltung
     */
    $scope.loescheVeranstaltung = function() {

        if (!DBVeranstService.loescheVeranstaltung(loesch)) {
          // Fehlermeldung
        }

        // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
        $('#gruppeLoeschen').modal('hide');
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
      }

    
      /**
       * Ruft die Backendschnittstelle tum Anzeigen aller Pflichtprakika auf
       * und zeigt diese auf der View an.
       */
      $scope.initPraktika = function(sem, fachbereich){
        DBVeranstService.initPraktika(sem, fachbereich);
        $scope.veranstaltungen = DBVeranstService.getVeranstaltungen();
      }

      /**
       * Ruft die Backendschnittstelle tum Anzeigen aller Wahlpflichtprakika auf
       * und zeigt diese auf der View an.
       */
      $scope.initWP = function(sem, fachbereich){

        DBVeranstService.initWP(sem, fachbereich);
        $scope.veranstaltungen = DBVeranstService.getVeranstaltungen();
      }

      /**
       * Ruft die Backendschnittstelle zum Anzeigen aller Projekte auf
       * und zeigt diese auf der View an.
       */
      $scope.initPO = function(sem, fachbereich){
        DBVeranstService.initPO(sem, fachbereich);
        $scope.veranstaltungen = DBVeranstService.getVeranstaltungen();
      }

      $scope.initProfessoren = function(fachbereich, semester){

        DBVeranstService.initProfs(fachbereich, semester);
      }

      $scope.getProfessoren = function(){

        return  DBVeranstService.getProfessoren();
      }

      $scope.resetVAValues = function(){

        $scope.va.dozent = {}; // ...
        $scope.va.assistent = {};
        $scope.va.minTeilnTeam = 2;
        $scope.va.maxTeilnTeam = 3;
        $scope.va.fach.fachBezeichnung = "";
        $scope.va.fach.fachKuerzel = "";
      }


      $scope.getProfString = function(){

        $scope.professorenString = DBVeranstService.getProfessorenString();
        return $scope.professorenString;
      }


      $scope.getFachbereiche = function(){
        return DBVeranstService.getFachbereiche();
      }

      $scope.getSemester = function(){
        return DBVeranstService.getSemester();
      }
      // ###########################################################################################################
  };

  // Controller bei der App "anmelden"
  app.controller("VeranstaltungsController", VeranstaltungsController);

  // Code sofort ausführen
}());
