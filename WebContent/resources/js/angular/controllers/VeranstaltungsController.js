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
    $scope.veranstaltungen = DBVeranstService.hcVeranstaltungsDaten;
    $scope.fachbereiche = DBVeranstService.fachbereiche;
    $scope.semester = DBVeranstService.getSemester();
    $scope.professoren = DBVeranstService.professoren;

    $scope.va = {}; // Veranstaltungsobject anlegen
    $scope.va.fach = ""; // Attribut Fach anlegen und initialsieren
    $scope.va.dozent = ""; // ...
    $scope.va.assistent = "";
    $scope.va.min = 0;
    $scope.va.min = 10;
    $scope.va.curr = 0;
    $scope.va.max = 60;
    $scope.va.fachbereich = DBVeranstService.getFachbereich();
    $scope.va.sem = $scope.semester[0];
    $scope.va.teamKap = 2;

    // ###########################################################################################################


    //Preconditions -> TODO: Wird noch um weitere erweitert
    // ###########################################################################################################

    $scope.isValidNumber = function(x) {
      return x % 1 === 0 && x > 0;
    }

    $scope.isValidMin = function(min) { // Mehr Bedingungen, damit UI nicht von Fehlern geflutet
      if ($scope.isValidNumber(min) && $scope.isValidNumber($scope.va.max)) {

        return min <= $scope.va.max;
      }

      return true;

    }

    $scope.isValidMax = function(max) { // Mehr Bedingungen, damit UI nicht von Fehlern geflutet
      if ($scope.isValidNumber(max) && $scope.isValidNumber($scope.va.min)) {

        return max >= $scope.va.min;
      }

      return true;

    }

    $scope.isValidAnzTeilnehmer = function(anzTeiln) {
        return anzTeiln > 1 && anzTeiln < 11;
      }

      $scope.isValidProfInput = function(expr){

        for(var i = 0; i < DBVeranstService.professoren.length; i++){

          if(DBVeranstService.professoren[i] == expr){
            return true;
          }
        }
        return false;
      }

    $scope.isValidFachkuerzel = function(fachkuerzel){

    	return fachkuerzel != "" && fachkuerzel.length < 6 && fachkuerzel.length > 1;
    }

    // Bedingung um den Speichern-Button zu aktivieren/deaktivieren
    // TODO: Muss noch um restliche Felder ergänzt werden
    $scope.filledComplete = function() {

      return $scope.isValidMin($scope.va.min)
      			&& $scope.isValidMax($scope.va.max)
      			&& $scope.isValidAnzTeilnehmer($scope.va.teamKap)
            && $scope.isValidProfInput($scope.va.dozent);
    }



    // ###########################################################################################################-



    // Button-klick Funktionen
    // ###########################################################################################################

      // Initialisert die Gruppenübersicht zu einer Veranstaltung
        $scope.initGruppenUebersicht = function(fach) {

          var vaIndex = DBVeranstService.sucheVA(fach);
          var va = DBVeranstService.hcVeranstaltungsDaten[vaIndex];
          if(!DBGruppService.initGruppen(va)){
            // Fehlermeldung
          }

        }

    // Initialisiert das Veranstaltung-Bearbeiten-Popup mit den vorhandenen Werten
    $scope.initVeranBearbeitenPopup = function(fach) {

      indexEdit = DBVeranstService.sucheVA(fach);
      $scope.va.dozent = DBVeranstService.hcVeranstaltungsDaten[indexEdit].professor.vollerName;
      $scope.va.min = DBVeranstService.hcVeranstaltungsDaten[indexEdit].minTm;
      $scope.va.curr = DBVeranstService.hcVeranstaltungsDaten[indexEdit].anzTm;
      $scope.va.max = DBVeranstService.hcVeranstaltungsDaten[indexEdit].maxTm;
    }

    // Fügt eine neue Veranstaltung in die Tabelle ein -> TODO: Preconditions
    $scope.addVeranstaltung = function() {

      va = {};
      va.fach = $scope.va.fach;
      va.dozent = $scope.va.dozent;
      va.semester = $scope.va.semester;
      va.fachbereich = $scope.va.fachbereich;
      va.min = $scope.va.min;
      va.curr = 0; // Später dynamische Ermittlung
      va.max = $scope.va.max;
      va.teamKap = $scope.va.teamKap;

      // TODO: implementiere Berechnung
      va.maxGr = 10;
      va.currGr = 0;

      if (!DBVeranstService.addVeranstaltung(va)) {

        // Fehlermeldung -> TODO: implement

      }

      autoscroller.erstellen = null;
    }

    // Ändert den Tabelleneintrag der Veranstaltung anhand der Benutzereingaben
    $scope.editVeranstaltung = function() {

      var edit = {};
      edit.dozent = $scope.va.dozent;
      edit.min = $scope.va.min;
      edit.max = $scope.va.max;
      edit.teamKap = $scope.va.teamKap;

      if (!DBVeranstService.editVeranstaltung(indexEdit, edit)) {
        // Fehlermeldung
      }

      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#modalBearbeiten').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }

    // Initialisiert den Index der zu löschenden Veranstaltung
    $scope.initVeranstaltungLoeschen = function(fach) {
      loesch = fach;
    }

    $scope.loescheVeranstaltung = function() {

        console.log(loesch);
        if (!DBVeranstService.loescheVeranstaltung(loesch)) {
          // Fehlermeldung
        }

        // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
        $('#gruppeLoeschen').modal('hide');
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
      }


    $scope.getVAFuerFachbereich = function(fachbereich){

      DBVeranstService.getAlleVeranstaltungenFuerFachbereich(fachbereich);

    }

    $scope.getVAFuerSemester = function(semester){

      DBVeranstService.getAlleVeranstaltungenFuerSem(semester);

    }
      // ###########################################################################################################
  };

  // Controller bei der App "anmelden"
  app.controller("VeranstaltungsController", VeranstaltungsController);

  // Code sofort ausführen
}());
