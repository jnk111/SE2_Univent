/**
 * Managed die Popups
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope: Model Object
  var ModalController = function($scope) {



    $scope.getGruppeDetails = function(){

      return "resources/studpanel/popups/detailgruppe.html";
    }

    $scope.getUeberschnEinladung = function(){

      return "resources/studpanel/popups/ueberschn_einladung.html";
    }

    $scope.getTeamDetails = function(){

      return "resources/studpanel/popups/modal_team_details.html";
    }

    $scope.getGruppeDetailsTeam = function(){

      return "resources/studpanel/popups/detailgruppe_team.html";
    }

    $scope.getTeamLoeschen = function(){

      return "resources/studpanel/popups/team_austreten.html";
    }

    $scope.getEinlVerschickt = function(){

      return "resources/studpanel/popups/modal_einl_verschickt.html";
    }
    $scope.getTeamErstellen = function(){
      return "resources/studpanel/popups/modal_team_erstellen.html";
    }

    $scope.getModalUs = function(){
      return "resources/studpanel/popups/modal_ueberschn.html";
    }

    $scope.getModalUsEinzel = function(){

      return "resources/studpanel/popups/modal_ueberschn_einzel.html"
    }

    $scope.getModalUsEinzelBest = function(){

      return "resources/studpanel/popups/modal_einzel_bestaetigt.html"

    }

  };

  // Controller bei der App "anmelden"
  app.controller("ModalController", ModalController);

  // Code sofort ausführen
}());
