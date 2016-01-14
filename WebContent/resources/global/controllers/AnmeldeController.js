/**
 * Managed die Extensions der einzelnen Tabs
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var AnmeldeController = function($scope, DBAnmeldeService) {

    // Locals
    // ------------------

    // ------------------

    // Helper
    // ------------------



    // Scope-Felder - Interaktion mit View
    // --------------------------------------------------

    $scope.prakt = DBAnmeldeService.getPrakTermine();
    $scope.wp = DBAnmeldeService.getWPTermine();
    $scope.po = DBAnmeldeService.getPOTermine();
    $scope.error = false;


    // --------------------------------------------------


    // Funktionen um Ergebnisse darzustellen
    // --------------------------------------------------

    // Gibt das Success-Icon zurück
    $scope.getFinished = function() {
      return "resources/apanel/icons/finished.html";
    }

    // Gibt das Fail-Icon zurück
    $scope.getOpen = function() {
      return "resources/apanel/icons/open_reg.html";
    }

    // Gibt das Open-Icon zurück
    $scope.getRunning = function() {
      return "resources/apanel/icons/running.html";
    }

    $scope.istGueltigeUhrzeitEingabe = function(uhrzeit){

      return DBAnmeldeService.istGueltigeUhrzeitEingabe(uhrzeit);
    }


    $scope.istGueltigeDatumEingabe = function(datum){

      return DBAnmeldeService.istGueltigeDatumEingabe(datum);
    }

    $scope.istGueltigerZeitRaumDatum = function(start, ende){

      return DBAnmeldeService.istGueltigerZeitRaumDatum(start, ende);

    }

    /**
     * Ruft die Backend-Schnittstelle zum Anmeldefristen speichern auf und uebergibt
     * die zu speichernden Daten.
     */
    $scope.speichereAnmeldeDaten = function(){

      DBAnmeldeService.speicherePraktikaTeamAnmeldeFrist($scope.prakt.datumStart, $scope.prakt.datumEnde, $scope.prakt.uzStart, $scope.prakt.uzEnde);
      DBAnmeldeService.speichereWPTeamAnmeldeFrist($scope.wp.datumStart, $scope.wp.datumEnde, $scope.wp.uzStart, $scope.wp.uzEnde);
      DBAnmeldeService.speicherePOTeamAnmeldeFrist($scope.po.datumStart, $scope.po.datumEnde, $scope.po.uzStart, $scope.po.uzEnde);

      DBAnmeldeService.speicherePraktikaEinzelAnmeldeFrist($scope.prakt.datumEinzelStart, $scope.prakt.datumEinzelEnde, $scope.prakt.uzEinzelStart, $scope.prakt.uzEinzelEnde);
      DBAnmeldeService.speichereWPEinzelAnmeldeFrist($scope.wp.datumEinzelStart, $scope.wp.datumEinzelEnde, $scope.wp.uzEinzelStart, $scope.wp.uzEinzelEnde);
      DBAnmeldeService.speicherePOEinzelAnmeldeFrist($scope.po.datumEinzelStart, $scope.po.datumEinzelEnde, $scope.po.uzEinzelStart, $scope.po.uzEinzelEnde);
    }

    $scope.istGueltigeStartUhrzeitEinzel = function(datumEinzelStart, datumEnde, uzStart, uzEnde){

      return DBAnmeldeService.istGueltigeStartUhrzeitEinzel(datumEinzelStart, datumEnde, uzStart, uzEnde);
    }

    $scope.istGueltigerZeitRaumEinzelDatum = function(datumEinzelStart, datumEnde){

      return DBAnmeldeService.istGueltigerZeitRaumEinzelDatum(datumEinzelStart, datumEnde);
    }

    /**
     * Prueft ob eine Anmeldephase abelaufen ist.
     */
    $scope.pruefeAnmeldePhase = function(datum){


      return DBAnmeldeService.pruefeAnmeldePhase(new Date(), datum);
    }


    /**
     * holt die Anmeldefristen (Team und Einzel) fuer Pflichtpraktika aus 
     * dem Backend und zeigt diese auf der View an.
     */
    $scope.getPrakTermine = function(){

      $scope.prakt = DBAnmeldeService.getPrakTermine();
      return $scope.prakt;
    }

    /**
     * holt die Anmeldefristen (Team und Einzel) fuer Wahlpflichtpraktika aus 
     * dem Backend und zeigt diese auf der View an.
     */
    $scope.getWPTermine = function(){

      $scope.wp = DBAnmeldeService.getWPTermine();
      return $scope.wp;
    }

    /**
     * holt die Anmeldefristen (Team und Einzel) fuer Projekte aus 
     * dem Backend und zeigt diese auf der View an.
     */
    $scope.getPOTermine = function(){

      $scope.po = DBAnmeldeService.getPOTermine();
      return $scope.po;
    }
    $scope.isFilledComplete = function(){

      return $scope.istGueltigeDatumEingabe($scope.prakt.datumStart)
               && $scope.istGueltigeDatumEingabe($scope.prakt.datumEnde)
                && $scope.istGueltigeDatumEingabe($scope.wp.datumStart)
                 && $scope.istGueltigeDatumEingabe($scope.wp.datumEnde)
                  && $scope.istGueltigeDatumEingabe($scope.po.datumStart)
                   && $scope.istGueltigeDatumEingabe($scope.po.datumEnde)
                    && $scope.istGueltigerZeitRaumDatum($scope.prakt.datumStart, $scope.prakt.datumEnde)
                     && $scope.istGueltigerZeitRaumDatum($scope.wp.datumStart, $scope.wp.datumEnde)
                      && $scope.istGueltigerZeitRaumDatum($scope.po.datumStart, $scope.po.datumEnde)
                       && $scope.istGueltigeUhrzeitEingabe($scope.prakt.uzStart)
                        && $scope.istGueltigeUhrzeitEingabe($scope.prakt.uzEnde)
                         && $scope.istGueltigeStartUhrzeitEinzel($scope.prakt.datumEinzelStart, $scope.prakt.datumEnde, $scope.prakt.uzEinzelStart, $scope.prakt.uzEnde)
                          && $scope.istGueltigeUhrzeitEingabe($scope.wp.uzStart)
                           && $scope.istGueltigeUhrzeitEingabe($scope.wp.uzEnde)
                            && $scope.istGueltigeStartUhrzeitEinzel($scope.wp.datumEinzelStart, $scope.wp.datumEnde, $scope.wp.uzEinzelStart, $scope.wp.uzEnde)
                             && $scope.istGueltigeUhrzeitEingabe($scope.po.uzStart)
                              && $scope.istGueltigeUhrzeitEingabe($scope.po.uzEnde)
                               && $scope.istGueltigeStartUhrzeitEinzel($scope.po.datumEinzelStart, $scope.po.datumEnde, $scope.po.uzEinzelStart, $scope.po.uzEnde)
    }

  };

  // Controller bei der App "anmelden"
  app.controller("AnmeldeController", AnmeldeController);

  // Code sofort ausführen
}());
