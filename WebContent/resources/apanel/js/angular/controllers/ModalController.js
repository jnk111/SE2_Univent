/**
 * Managed die Popups
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope: Model Object
  var ModalController = function($scope, DBVeranstService) {

    // Getter für die einzelnen Popups
    // -------------------------------------------------------------

	/**
	 * Popup laden: Export lauft
	 */
    $scope.getExpRunning = function(){

      return "resources/apanel/popups/modal_exp_running.html";
    }

	/**
	 * Popup laden: Import-Warnung
	 */
    $scope.getImpWarnung = function(){
      return "resources/apanel/popups/modal_imp_warnung.html";
    }

	/**
	 * Popup laden: Wiederherstellung laeuft
	 */
    $scope.getWDHRunning = function(){
      return "resources/apanel/popups/modal_wdh_running.html";
    }

	/**
	 * Popup laden: Anmeldetermine gespeichert
	 */
    $scope.getTermineSave = function(){
      return "resources/apanel/popups/modal_termine_save.html";
    }

    $scope.getError = function(){
      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#modalError').modal('show');
      return "resources/global/popups/modal_error.html";
    }

	/**
	 * Popup laden: Importhinweise
	 */
    $scope.getImpHinweise = function(){

      return "resources/apanel/popups/modal_imp_hinweise.html";
    }

	/**
	 * Popup laden: Backup lauft
	 */
    $scope.getBKPRunning = function(){

      return "resources/apanel/popups/modal_bkp_running.html";
    }

	/**
	 * Popup laden: Veranstaltung bearbeiten
	 */
    $scope.getVeranstaltungEdit = function() {
      return "resources/apanel/popups/modal_veranstaltung_aendern.html";
    }

	/**
	 * Popup laden: Veranstaltung leoschen
	 */
    $scope.getVeranstaltungLoesch = function() {
      return "resources/apanel/popups/modal_veranstaltung_loeschen.html";
    }

	/**
	 * Popup laden: Gruppe bearbeiten
	 */
    $scope.getGruppeEdit = function() {
      return "resources/apanel/popups/modal_gruppe_aendern.html";
    }

	/**
	 * Popup laden: Gruppendetails
	 */
    $scope.getGruppeDetails = function() {
      return "resources/apanel/popups/modal_gruppe_details.html";
    }

	/**
	 * Popup laden: Veranstaltung erstellen
	 */
    $scope.getModalNew = function() {
      return "resources/apanel/popups/modal_veranstaltung_erstellen.html";
    }

	/**
	 * Popup laden: PVL eintragen
	 */
    $scope.getModalPVL = function() {
        return "resources/apanel/popups/modal_pvl.html";
      }
      // -------------------------------------------------------------

	/**
	 * Popup laden: note eintragen
	 */
    $scope.getModalNote = function() {
      return "resources/apanel/popups/modal_note.html";
    }

	/**
	 * Popup laden: Teilnehmerdetails Praktika
	 */
    $scope.getModalTmDetailsPrakt = function() {
      return "resources/apanel/popups/modal_teilnehmer_details_prakt.html";
    }

	/**
	 * Popup laden: Teilnehmerdetails Leistungen
	 */
    $scope.getModalTMLeistungen = function() {
      return "resources/apanel/popups/modal_teilnehmer_details_leist.html";
    }

    $scope.getModalTmEntfernen = function() {
      return "resources/apanel/popups/modal_tm_entfernen.html";
    }

	/**
	 * Popup laden: Gruppe loeschen
	 */
    $scope.getGruppeLoeschen = function() {
      return "resources/apanel/popups/modal_gruppe_loeschen.html";
    }
  };

  // Controller bei der App "anmelden"
  app.controller("ModalController", ModalController);

  // Code sofort ausführen
}());
