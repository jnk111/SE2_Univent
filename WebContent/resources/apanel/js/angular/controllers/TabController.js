/**
 * Managed die einzelnen Tabs
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var TabController = function($scope, DBVeranstService, DBLeistService, DBProfileService, DBDatenImExService, DBAnmeldeService, DBNewsService) {

    // Togglepoint
    $scope.switch = {};
    $scope.viewToggle = $scope.switch.profil;


    // Locals
    // --------------------------------------------------------------------------
    var profil = "profile";
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
    var anmelden = "anmeldeTermine"
    var bkp = "datenbackup";

    // -------------------------------------------------------------------------


    // Tabs
    // -------------------------------------------------------------------------
    var newsTab = "resources/apanel/tabs/news.html";
    var profileTab = "resources/apanel/tabs/profile.html";
    var praktikaTab = "resources/apanel/tabs/pflicht_praktika.html";
    var wahlPflichtTab = "resources/apanel/tabs/wahlpflicht_uebersicht.html";
    var projekteTab = "resources/apanel/tabs/projekte_uebersicht.html";
    var gruppenTab = "resources/apanel/tabs/gruppen.html";
    var leistungenTab = "resources/apanel/tabs/leistungen.html";
    var meldungenTab = "resources/apanel/tabs/meldungen.html";
    var datenExTab = "resources/apanel/tabs/datenex.html";
    var datenImTab = "resources/apanel/tabs/datenim.html";
    var tmTab = "resources/apanel/tabs/tm_uebersicht.html";
    var anmeldeTermineTab = "resources/apanel/tabs/anmeldetermine.html";
    var datenbkpTab = "resources/apanel/tabs/daten_backup.html";
    // -------------------------------------------------------------------------


    // get Tabs
    // -------------------------------------------------------------------------
    $scope.getDatenBkp = function(){

      return datenbkpTab;
    }

    $scope.getCurrTab = function() {
      return $scope.viewToggle;
    }

    $scope.getNews = function() {
      return newsTab;
    }

    $scope.getProfile = function() {
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

      $scope.getAnmeldeTermineTab = function(){

        return anmeldeTermineTab;
      }
      // -------------------------------------------------------------------------


    // Tab-Switch functions
    // -------------------------------------------------------------------------
    $scope.switchToDatenBkp = function(){
      DBDatenImExService.initBackupDateien();
      $scope.viewToggle = bkp;
    }

    /**
     * View laden: Teilnehmeruebersicht fuer eine Gruppe
     */
    $scope.switchToTmUebersicht = function() {
      $scope.viewToggle = tmUebersicht;
    }

    /**
     * View laden: Datenexport
     */
    $scope.switchToDatenEx = function() {
      DBDatenImExService.initDateienEx();
      $scope.viewToggle = datenex;
    }

    /**
     * View laden: Datenimport
     */
    $scope.switchToDatenIm = function() {
      DBDatenImExService.initDateienImp();
      $scope.viewToggle = datenim;
    }
    
    /**
     * View laden: Leistungen Admin
     */
    $scope.switchToLeistungen = function() {
      var faecher = DBLeistService.updateFaecher("AI", "Praktikum", 1);
      DBLeistService.initLeistungenFuerFach("PRP1");
      $scope.viewToggle = leistungen;
    }

    /**
     * View laden: Profil
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
     * View laden: Pflichtpraktikauebersicht - Angestellter
     */
    $scope.switchToPraktika = function() {
      DBVeranstService.initPraktika(1, "AI");
      $scope.viewToggle = praktika;
    }

    /**
     * View laden: Wahlpflichtuebersicht - Angestellter
     */
    $scope.switchToWahlpflicht = function() {
      DBVeranstService.initWP(1, "AI");
      $scope.viewToggle = wahlpflicht;
    }

    /**
     * View laden: Projekteuebersicht - Angestellter
     */
    $scope.switchToProjekte = function() {
      DBVeranstService.initPO(1, "AI");
      $scope.viewToggle = projekte;
    }

    /**
     * View laden: Gruppenuebersicht fuer eine Veranstaltung
     */
    $scope.switchToGruppen = function() {
        $scope.viewToggle = gruppen;
    }

    /**
     * View laden: Anmeldetermine
     */
    $scope.switchToAnmeldeTermine = function(){
      DBAnmeldeService.initTermine();
      $scope.viewToggle = anmelden;
    }
      // -------------------------------------------------------------------------

  };

  // Controller bei der app "anmelden"
  app.controller("TabController", TabController);

  // Code sofort ausführen
}());
