/**
 * Managed die einzelnen Tabs
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var PraktikaController = function($scope, DBPraktikaService) {


    $scope.gruppenEintraege = DBPraktikaService.getGruppenEintrage();
    $scope.gr = {};
    $scope.gr.ausgewaehlt = false;
    $scope.gr.fachkuerzel = DBPraktikaService.fachkuerzel;
    $scope.gr.currFachkuerzel = DBPraktikaService.fachkuerzel[0];
    $scope.gr.detailGruppe;
    $scope.einzel = false;

    $scope.findeAlleUeberschneidungen = function(){

      DBPraktikaService.blendeUeberschneidungenAus();

    }

    /**
     * Waehlt eine Gruppe aus und speichert diese in ein Array
     * fuer die aktuell ausgewaehlten Gruppen (Gruppenuebersicht Anmeldung)
     */
    $scope.gruppeAuswaehlen = function(fach, grpNr){


      if(DBPraktikaService.waehleGruppeAus(fach, grpNr)){
        $scope.gr.ausgewaehlt = true;
      }else{
        $scope.gr.ausgewaehlt = false;
      }
    }

    /**
     * Initialsiert mithilfe des Backends die Details der gewaehlten Gruppe
     * Ruft dazu die entsprechende Schnittstelle im Backend auf.
     */
    $scope.initGruppenDetails = function(fach, grpNr){

      DBPraktikaService.initGruppenDetails(fach, grpNr);
      $scope.gr.detailGruppe = DBPraktikaService.getDetailGruppe();
    }

    /**
     * holt alle Gruppen fuer ein Fachkuerzel aus dem Backend
     *
     */
    $scope.getAlleGruppenFuerFach = function(fachkuerzel){

      if(!DBPraktikaService.getAlleGruppenFuerFach(fachkuerzel)){

        // error
      }

      $scope.gruppenEintraege = DBPraktikaService.getGruppenEintrage();
    }

    /**
     * Initialisiert die Teamkonfiguration anhand der ausgewaehlten Gruppen
     *
     */
    $scope.initTeamKonfig = function(){

      DBPraktikaService.initTeamKonfig();

      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#ueberchn').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }

    $scope.initEinzelAnmeldung = function(){

      DBPraktikaService.speichereEinzelAnmeldungen();

      $('#ueberchn_einzel').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();


    }

    $scope.mindEineGruppeAusgewaehlt = function(){
      return $scope.gr.ausgewaehlt;
    }

    $scope.teamAnmeldePhaseAbgeschlossen = function(){

      return DBPraktikaService.teamAnmeldePhaseAbgeschlossen();
    }

    $scope.einzelAnmeldePhaseAbgeschlossen = function(){

      return DBPraktikaService.einzelAnmeldePhaseAbgeschlossen();
    }

    $scope.getGruppenEintraege = function(){

      return DBPraktikaService.getGruppenEintrage();
    }

    $scope.fachBereitsGewaehlt = function(fachkuerzel){

      return DBPraktikaService.fachBereitsGewaehlt(fachkuerzel);
    }

    $scope.wechseleAnmeldeTyp = function(){

      if(DBPraktikaService.isEinzel()){
        DBPraktikaService.setEinzel(false);
      }else{
        DBPraktikaService.setEinzel(true);
      }
    }

    $scope.isEinzel = function(){
      return DBPraktikaService.isEinzel();
    }


  };

  // Controller bei der app "anmelden"
  app.controller("PraktikaController", PraktikaController);

  // Code sofort ausführen
}());
