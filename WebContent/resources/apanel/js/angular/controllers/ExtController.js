/**
 * Managed die Extensions der einzelnen Tabs
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var ExtController = function($scope, autoscroller, DBVeranstService, DBGruppService) {


    // Scope Fields
    // -------------------------------------------
    $scope.erstellen = autoscroller.erstellen;
    // -------------------------------------------


    // Extension-Tags
    // -----------------------------------------------
    var grErstellen = "grErstellen";
    var newsErstellen = "newsErstellen";
    var veranErstellen = "veranErstellen";
    // -----------------------------------------------

    // Extension Scroll-IDs
    // ----------------------------------------------
    var scrollGrErstellen = 'grerstellen';
    var scrollNewsErstellen = 'newserstellen';
    var scrollVeranErstellen = 'veranerstellen';
    var scrollTeamErstellen = 'teamerstellen';
    // ----------------------------------------------

    // Extension templates
    // -----------------------------------------------------------------------------
    var extGruppeErstellen = "resources/apanel/extensions/ext_gruppe_erstellen.html";
    var extNewsErstellen = "resources/apanel/extensions/ext_news_erstellen.html";
    var extTeamErstellen = "resources/apanel/extensions/ext_team_erstellen.html";
    var extVeranErstellen = "resources/apanel/extensions/ext_veranstaltung_erstellen.html";
    // -----------------------------------------------------------------------------


    // Funktionen um die Extension ein- und auszublenden
    // --------------------------------------------------
    $scope.getCurrTemplate = function() {
      return autoscroller.erstellen;
    }

    /**
     * Laedt die Extension zum Gruppe erstellen
     */
    $scope.getExtGruppeErstellen = function() {
      return extGruppeErstellen;
    }

    /**
     * Laedt die Extension zum News erstellen
     */
    $scope.getExtNewsErstellen = function() {
      return extNewsErstellen;
    }

    $scope.getExtTeamErstellen = function(){

      return extTeamErstellen;
    }

    /**
     * Laedt die Extension zum Veranstaltung erstellen
     */
    $scope.getExtVeranErstellen = function() {
      return extVeranErstellen;
    }


    $scope.teamErstellen = function(){

      if(autoscroller.erstellen != teamErstellen){
        autoscroller.erstellen = teamErstellen;
        autoscroller.scrollTo(scrollTeamErstellen);
      }
    }

    $scope.grErstellen = function() {

      if (autoscroller.erstellen != grErstellen) {
        DBGruppService.resetAppValues()
        autoscroller.erstellen = grErstellen;
        autoscroller.scrollTo(scrollGrErstellen);
      }
    }

    $scope.newsErstellen = function() {

      if (autoscroller.erstellen != newsErstellen) {
        autoscroller.erstellen = newsErstellen;
        autoscroller.scrollTo(scrollNewsErstellen);
      }
    }

    $scope.grErstellenClose = function() {

      if (autoscroller.erstellen == grErstellen) {
        autoscroller.erstellen = null;
      }
    }

    $scope.veranErstellen = function(typ) {

      DBVeranstService.setVATyp(typ);
      if (autoscroller.erstellen != veranErstellen) {
        autoscroller.erstellen = veranErstellen;
        autoscroller.scrollTo(scrollVeranErstellen);
      }
    }

    $scope.veranErstellenClose = function() {

      if (autoscroller.erstellen == veranErstellen) {
        autoscroller.erstellen = null;
      }
    }

    $scope.newsErstellenClose = function() {

      if (autoscroller.erstellen == newsErstellen) {
        autoscroller.erstellen = null;
      }
    }

    // --------------------------------------------------
  };

  // Controller bei der App "anmelden"
  app.controller("ExtController", ExtController);

  // Code sofort ausführen
}());
