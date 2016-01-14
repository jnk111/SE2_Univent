/**
 * Service um automatisch zu den aufgeklappten Extensions zu scrollen
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var autoscroller = function($document) {

    // Locals und Scrollfunktionen
    // ----------------------------------------------------------------
    var erstellen = null;
    var scrollTo = function(element) {
        var position = angular.element(document.getElementById(element));
        $document.scrollToElement(position, 30, 500);
      }
      // ----------------------------------------------------------------

    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // -----------------------------------------------------------------
    return {
      scrollTo: scrollTo,
      erstellen: erstellen
    };
    // ----------------------------------------------------------------
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("autoscroller", autoscroller);

  // Code sofort ausführen
}());
