/**
 * Managed die Extensions der einzelnen Tabs
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var ExtController = function($scope, autoscroller, DBVeranstService) {


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
    // ----------------------------------------------

    // Extension templates
    // -----------------------------------------------------------------------------
    var extGruppeErstellen = "resources/extensions/ext_gruppe_erstellen.html";
    var extNewsErstellen = "resources/extensions/ext_news_erstellen.html";
    var extVeranErstellen = "resources/extensions/ext_veranstaltung_erstellen.html";
    // -----------------------------------------------------------------------------


    // Funktionen um die Extension ein- und auszublenden
    // --------------------------------------------------
    $scope.getCurrTemplate = function() {
      return autoscroller.erstellen;
    }

    $scope.getExtGruppeErstellen = function() {
      return extGruppeErstellen;
    }

    $scope.getExtNewsErstellen = function() {
      return extNewsErstellen;
    }

    $scope.getExtVeranErstellen = function() {
      return extVeranErstellen;
    }


    $scope.grErstellen = function() {

      if (autoscroller.erstellen != grErstellen) {
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

    $scope.veranErstellen = function() {

      DBVeranstService.getAlle
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
