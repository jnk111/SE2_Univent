/**
 * Service um zwischen Gruppencontroller und TMUebersichtController zu vermitteln
 *
 * TODO: Wird erst implementiert, wenn wir eine reale Datenbank haben
 *
 */
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var tmGruppenService = function() {

    var fach;
    var gruppe;

    var getFach = function() {
      return fach;
    }

    var setFach = function(fa) {
      fach = fa;
    }

    var getGruppe = function() {
      return gruppe;
    }

    var setGruppe = function(grNr) {
      gruppe = grNr;
    }

    return {
      getFach: getFach,
      setFach: setFach,
      getGruppe: getGruppe,
      setGruppe: setGruppe
    };



  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("tmGruppenService", tmGruppenService);

  // Code sofort ausführen
}());
