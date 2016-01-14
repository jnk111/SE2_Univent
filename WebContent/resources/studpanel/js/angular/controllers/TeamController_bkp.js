/**
 * Managed die einzelnen Tabs
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var TeamController = function($scope, DBTeamService) {

    $scope.teams = DBTeamService.teams;
    $scope.lvStud;


    // Prüft ob die Eingabe einem Studenten im Puffer entspricht
    // return gefunden, nicht gefunden : boolean
    $scope.istGueltigerStudent = function(expr){

      return DBTeamService.istGueltigerStudent(expr);
    }

    // Setzt den Gruppeneintrag auf "Einzelanmeldung"
    // return geklappt, nicht geklappt : boolean
    $scope.registriereEinzelAnmeldung = function(fach, grpNr){

      return DBTeamService.setzeEinzelAnmeldung(fach, grpNr);
    }

    $scope.waehleTeamMitgliedAus = function(expr){

      return DBTeamService.fuegeAusGewStudentenEin(expr);
    }

    $scope.entferneTeamMitgliedAuswahl = function(matrNr){

      DBTeamService.entferneStudAuswahl(matrNr);
    }

    $scope.uebernehmeAusgewMitglieder = function(){

      DBTeamService.uebernehmeAuswahl();
    }
  };

  // Controller bei der app "anmelden"
  app.controller("TeamController", TeamController);

  // Code sofort ausführen
}());
