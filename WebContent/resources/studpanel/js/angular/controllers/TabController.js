/**
 * Managed die einzelnen Tabs
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var TabController = function($scope, DBTeamService, DBPraktikaService, DBMeldungenService, DBProfileService, DBLeistService, DBNewsService, DBAnmeldeService) {

    // Togglepoint
    $scope.switch = {};
    $scope.viewToggle = $scope.switch.profil;


    // Locals
    // --------------------------------------------------------------------------
    var profil = "profile";
    var teams = "teams";
    var news = "news";
    var praktika = "praktika";
    var wahlpflicht = "wahlpflicht";
    var projekte = "projekte";
    var gruppen = "gruppen";
    var leistungen = "leistungen";
    var meldungen = "meldungen";
    var datenim = "datenim";
    var datenex = "datenex";
    var tmUebersicht = "tmUebersicht";
    var teamKonfig = "teamKonfig";
    var tests = "tests";
    var anmelden = "anmeldeTermine"
    var einzelAnmeldung = "einzel";
    var meldungen = "meldungen";
    // -------------------------------------------------------------------------


    // Tabs
    // -------------------------------------------------------------------------
    var newsTab = "resources/studpanel/tabs/news.html";
    var profileTab = "resources/studpanel/tabs/profile.html";
    var praktikaTab = "resources/studpanel/tabs/pflicht_praktika.html";
    var wahlPflichtTab = "resources/studpanel/tabs/wahlpflicht_uebersicht.html";
    var projekteTab = "resources/studpanel/tabs/projekte_uebersicht.html";
    var gruppenTab = "resources/apanel/tabs/gruppen.html";
    var leistungenTab = "resources/studpanel/tabs/leistungen.html";
    var meldungenTab = "resources/apanel/tabs/meldungen.html";
    var datenExTab = "resources/apanel/tabs/datenex.html";
    var datenImTab = "resources/apanel/tabs/datenim.html";
    var tmTab = "resources/apanel/tabs/tm_uebersicht.html";
    var teamTab = "resources/studpanel/tabs/teams.html";
    var teamKonfigTab = "resources/studpanel/tabs/teamkonfig.html";
    var testTab = "resources/studpanel/tabs/testTab.html"
    var meldungenTab = "resources/studpanel/tabs/meldungen.html"
    // -------------------------------------------------------------------------


    // get Tabs
    // -----------------------------------------------------------------------
    $scope.getTestSuite = function(){

      return testTab;
    }
    $scope.getCurrTab = function() {
      return $scope.viewToggle;
    }

    $scope.getNews = function() {
      return newsTab;
    }

    $scope.getMeldungenTab = function(){

      return meldungenTab;
    }

    $scope.getProfile = function() {
      DBAnmeldeService.initTermine();
      DBProfileService.initUser();
      return profileTab;
    }

    $scope.getPraktika = function() {
      return praktikaTab;
    }

    $scope.getWahlpflicht = function() {
      return wahlPflichtTab;
    }

    $scope.getProjekte = function() {
      return projekteTab;
    }

    $scope.getGruppen = function() {
      return gruppenTab;
    }

    $scope.getLeistungen = function() {
      return leistungenTab;
    }

    $scope.getMeldungen = function() {
      return meldungenTab;
    }

    $scope.getDatenEx = function() {
      return datenExTab;
    }

    $scope.getDatenIm = function() {
      return datenImTab;
    }

    $scope.getTmUebersicht = function() {
        return tmTab;
      }

    $scope.getTeamUebersicht = function(){
      return teamTab;
    }

    $scope.getTeamKonfig = function(){

      return teamKonfigTab;
    }


      // -------------------------------------------------------------------------


    // Tab-Switch functions
    // -------------------------------------------------------------------------

    $scope.modalID = "";

    $scope.pruefeUs = function(){

      if(!DBPraktikaService.hatUeberschneidungen()){

        if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPrakTermine().datumEnde.addDays(1))
            && !DBPraktikaService.isEinzel()){

          DBPraktikaService.initTeamKonfig();
          $scope.switchToTeamkonfig();

        }else if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPrakTermine().datumEinzelEnde.addDays(1))
                  || DBPraktikaService.isEinzel()){
          modalID = "#einzelBestaetigt";
          $scope.switchToEinzelAnmeldung();

        }else{

        }
      }
    }


    $scope.switchToEinzelAnmeldung = function(){

      DBPraktikaService.speichereEinzelAnmeldungen();
      $scope.viewToggle = teams;
      DBTeamService.initTeamTabelleMeineTabelle();
    }
    $scope.switchToTests = function(){

      $scope.viewToggle = testTab;
    }

    /**
     * View laden: Teamkonfiguration
     */
    $scope.switchToTeamkonfig = function(){
      $scope.viewToggle = teamKonfig;
    }
    $scope.switchToTmUebersicht = function() {
      $scope.viewToggle = tmUebersicht;
    }

    /**
     * View laden: Meine Teams
     */
    $scope.switchToTeams = function(){
      DBTeamService.initTeamTabelleMeineTabelle();
      $scope.viewToggle = teams;
      $('#einlVerschickt').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();

    }

    $scope.switchToDatenEx = function() {
      $scope.viewToggle = datenex;
    }

    $scope.switchToDatenIm = function() {
      $scope.viewToggle = datenim;
    }

    /**
     * View laden: Meine Leistungen
     */
    $scope.switchToLeistungen = function() {
      DBLeistService.initLeistungen();
      $scope.viewToggle = leistungen;
    }

    /**
     * View laden: Mein Profil
     */
    $scope.switchToProfile = function() {

      $scope.viewToggle = profil;
    }

    /**
     * View laden: News
     */
    $scope.switchToNews = function() {
      DBNewsService.initNews();
      $scope.viewToggle = news;
    }

    /**
     * View laden: Gruppenauswahl fuer Praktika
     */
    $scope.switchToPraktika = function() {

      if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPrakTermine().datumEinzelEnde.addDays(1))){
        if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPrakTermine().datumEnde.addDays(1))){

          DBPraktikaService.initPraktikaGruppen("Alle");
        }else if(DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPrakTermine().datumEnde.addDays(1))
                  && !DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPrakTermine().datumEinzelEnde.addDays(1))){

          DBPraktikaService.initPraktikaGruppenEinzel("Alle");
        }
      }


      $scope.viewToggle = praktika;
    }

    /**
     * View laden: Gruppenauswahl fuer Wahlpflichtpraktika
     */
    $scope.switchToWahlpflicht = function() {

      if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getWPTermine().datumEinzelEnde.addDays(1))){
        if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getWPTermine().datumEnde.addDays(1))){


          DBPraktikaService.initWPGruppen("Alle");
        }else if(DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getWPTermine().datumEnde.addDays(1))
                  && !DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getWPTermine().datumEinzelEnde.addDays(1))){

          DBPraktikaService.initWPGruppenEinzel("Alle");
        }
      }
      $scope.viewToggle = wahlpflicht;
    }

    /**
     * View laden: Gruppenauswahl fuer Projekteuebersicht
     */
    $scope.switchToProjekte = function() {

      if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPOTermine().datumEinzelEnde.addDays(1))){
        if(!DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPOTermine().datumEnde.addDays(1))){

          DBPraktikaService.initPOGruppen("Alle");
        }else if(DBAnmeldeService.pruefeAnmeldePhase(new Date(), BAnmeldeService.getPOTermine().datumEnde.addDays(1))
                  && !DBAnmeldeService.pruefeAnmeldePhase(new Date(), DBAnmeldeService.getPOTermine().datumEinzelEnde.addDays(1))){

          DBPraktikaService.initPOGruppenEinzel("Alle");
        }
      }
      $scope.viewToggle = projekte;
    }

    $scope.switchToGruppen = function() {
        $scope.viewToggle = gruppen;
      }

     /**
      * View laden: Meldungen
      */
      $scope.switchToMeldungen = function(){
        var user = DBProfileService.getCurrUser();
        DBMeldungenService.initMeldungen(user.matrNr.matrNr);
        $scope.viewToggle = meldungen;
      }

    $scope.zeigeTeamKonfigTab = function(){

      $scope.viewToggle = teamKonfig;
    }
      // -------------------------------------------------------------------------

  };

  // Controller bei der app "anmelden"
  app.controller("TabController", TabController);

  // Code sofort ausführen
}());
