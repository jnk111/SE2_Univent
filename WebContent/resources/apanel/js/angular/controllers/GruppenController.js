/**
 * Managed die Veranstaltungen
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object, $http: holt JSON Object vom SpringMVC Backend -> folgt später
  var GruppenController = function($scope, autoscroller, DBGruppTmService, DBGruppService, DBVeranstService /*, $http*/ ) {


    // Locals
    // ############################################################################################################
    var indexGrLoeschen;
    var indexGrEdit;
    // ############################################################################################################


    $scope.termine = DBGruppService.termine;
    $scope.grNummern = DBGruppService.grNummern;
    $scope.anzTmTeam = DBGruppService.anzTmTeam;
    $scope.startZeiten = [DBGruppService.timesStart[0], DBGruppService.timesStart[1], DBGruppService.timesStart[2]];
    $scope.endZeiten = [DBGruppService.timesEnd[0], DBGruppService.timesEnd[1], DBGruppService.timesEnd[2]];
    $scope.gruppen = DBGruppService.gruppen;
    $scope.raeume = DBGruppService.raeume;
    $scope.professoren = DBVeranstService.professoren;
    $scope.assistenten = DBVeranstService.assistenten;



    // SelectBox-Titles
    // ############################################################################################################
    $scope.selBoxAnzTeamTitle = "Anzahl Teammitglieder: ";
    $scope.termWaehlenTitle = "Termne waehlen (KW) ... ";
    $scope.tagWaehlenTitle = "Tag: ";
    $scope.uhrzBeginTitle = "Uhrzeit (Beginn): ";
    $scope.uhrzEndeTitle = "Uhrzeit (Ende): ";
    $scope.raumWaehlenTitle = "Raum:";
    $scope.selBoxFBTitle = "Fachbereich:"
    $scope.selBoxGrpNrTitle = "Gruppenummer eingeben: ";

    var terminTitle = ". Termin";
    $scope.tm1Title = "1" + terminTitle;
    $scope.tm2Title = "2" + terminTitle;
    $scope.tm3Title = "3" + terminTitle;
    $scope.tm4Title = "4" + terminTitle;

    $scope.profGrEdit = "";
    $scope.assistGrEdit = "";

    // ############################################################################################################



    // Felder initialisiern
    // ############################################################################################################
    $scope.gr = {};
    $scope.gr.fach = DBGruppService.getFach();
    $scope.gr.sem;
    $scope.gr.grpNr = DBGruppService.grNummern[0];
    $scope.gr.termine = $scope.termine;
    $scope.gr.dozent;
    $scope.gr.assistent;
    $scope.gr.raum = [];
    $scope.gr.maxAnzTeiln = 20;
    $scope.gr.maxTeams;
    $scope.gr.anzTeams = 0;
    // ############################################################################################################


    $scope.grd = {};
    $scope.grd.fach = DBGruppService.getFach();
    $scope.grd.sem;
    $scope.grd.grpNr = DBGruppService.grNummern[0];
    $scope.grd.termine = $scope.termine;
    $scope.grd.dozent;
    $scope.grd.assistent;
    $scope.grd.raum = [];
    $scope.grd.maxAnzTeiln = 20;
    $scope.grd.maxTeams;
    $scope.grd.anzTeams = 0;


    $scope.gre = {};
    $scope.gre.fach = DBGruppService.getFach();
    $scope.gre.sem;
    $scope.gre.grpNr = DBGruppService.grNummern[0];
    $scope.gre.termine = [];
    $scope.gre.dozent;
    $scope.gre.assistent;
    $scope.gre.raum = [];
    $scope.gre.maxAnzTeiln = 20;
    $scope.gre.maxTeams;
    $scope.gre.anzTeams = 0;

    //Preconditions
    // ############################################################################################################

    // Prüft die Eingaben auf Korrektheit -> Wird noch um einige Bedingungen erweitert TODO
    $scope.isValidNumber = function(x) {
      return x == "" || (x % 1 === 0 && x > 0);
    }

    $scope.isNotEmpty = function(x) {
      return x != "";
    }


    $scope.isValidGrpNumber = function(x) {
      var valid = false;

      if ($scope.isValidNumber(x)) {
        var i;
        for (i = 0; i < DBGruppService.hcGruppenDaten.length; i++) {
          if ($scope.gr.grpNr == DBGruppService.hcGruppenDaten[i].grpNr) {
            return false;
          }
        }
        valid = true;
      }
      return valid;
    }

    $scope.isValidNaxTeiln = function(anzTm){

      return anzTm % 2 == 0 && anzTm > 5;
    }

    $scope.isValidDate = function(index) {

      if (index < $scope.gr.termine.length - 1) {
        if ($scope.gr.termine[index].datum.getMonth() == $scope.gr.termine[index + 1].datum.getMonth()) {
          return $scope.gr.termine[index].datum.getDate() < $scope.gr.termine[index + 1].datum.getDate()
                  && $scope.gr.termine[index].datum.getTime() < $scope.gr.termine[index + 1].datum.getTime();
        }
        return $scope.gr.termine[index].datum.getTime() < $scope.gr.termine[index + 1].datum.getTime();
      }

      return true;
    }

    $scope.isValidDateE = function(index) {

      if (index < $scope.gre.termine.length - 1) {
        if ($scope.gre.termine[index].datum.getMonth() == $scope.gre.termine[index + 1].datum.getMonth()) {
          return $scope.gre.termine[index].datum.getDate() < $scope.gre.termine[index + 1].datum.getDate()
                  && $scope.gre.termine[index].datum.getTime() < $scope.gre.termine[index + 1].datum.getTime();
        }
        return $scope.gre.termine[index].datum.getTime() < $scope.gre.termine[index + 1].datum.getTime();
      }

      return true;
    }

    $scope.isValidStartTime = function(ngIndex) {

      var start = $scope.gr.termine[ngIndex].start;
      var end = $scope.gr.termine[ngIndex].ende;

      return start.stunden < end.stunden;

    }

    $scope.isValidStartTimeE = function(ngIndex) {

      var start = $scope.gre.termine[ngIndex].start;
      var end = $scope.gre.termine[ngIndex].ende;

      return start.stunden < end.stunden;

    }

    $scope.isValidStartTimeComplete = function(index) {
      for (i = 0; i < $scope.gr.termine.length; i++) {
        if (!$scope.isValidStartTime(i)) {
          return false;
        }
      }
      return true;
    }

    $scope.isValidStartTimeCompleteE = function(index) {
      for (i = 0; i < $scope.gre.termine.length; i++) {
        if (!$scope.isValidStartTimeE(i)) {
          return false;
        }
      }
      return true;
    }

    $scope.isValidDateComplete = function() {

      for (i = 0; i < $scope.gr.termine.length; i++) {
        if (!$scope.isValidDate(i)) {
          return false;
        }
      }

      return true;
    }

    $scope.isValidDateCompleteE = function() {

      for (i = 0; i < $scope.gre.termine.length; i++) {
        if (!$scope.isValidDateE(i)) {
          return false;
        }
      }

      return true;
    }

    $scope.isValidProfInputGr = function(expr){

      $scope.gr.dozent = DBVeranstService.istGueltigerProf(expr);

      return $scope.gr.dozent != null;
    }

    $scope.isValidProfInputGre = function(expr){

      $scope.gre.dozent = DBVeranstService.istGueltigerProf(expr);

      return $scope.gre.dozent != null;
    }


    $scope.isValidAssistInputGr = function(expr){

      $scope.gr.assistent = DBGruppService.istGueltigerAssistent(expr);
      return $scope.gr.assistent != null;
    }

    $scope.isValidAssistInputGre = function(expr){

      $scope.gre.assistent = DBGruppService.istGueltigerAssistent(expr);
      return $scope.gre.assistent != null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Aktiviert/Deaktiviert den Speichern-Button
    $scope.isFilledCompleteErstellen = function() {

      return $scope.isValidDateComplete() && $scope.isValidNaxTeiln($scope.gr.maxTeams)
             && $scope.gr.dozent != null && $scope.gr.assistent != null
             && $scope.isValidProfInputGr($scope.gr.dozent.maID.id + " " +"platzhalter")
             && $scope.isValidAssistInputGr($scope.gr.assistent.maID.id + " " + "platzhalter");
    }

    $scope.isFilledCompleteErstellenE = function() {

      return $scope.isValidDateCompleteE() && $scope.isValidNaxTeiln($scope.gre.maxTeams)
            && $scope.gre.dozent != null && $scope.gre.assistent != null
             && $scope.isValidProfInputGre($scope.gre.dozent.maID.id + " " +"platzhalter")
             && $scope.isValidAssistInputGre($scope.gre.assistent.maID.id + " " + "platzhalter");
    }


      // ############################################################################################################



    // Button-klick Funktionen
    // ############################################################################################################

    /**
     * einen überflüssigen Termin aus der Auswahl loeschen
     * (Gruppe erstellen oder bearbeiten)
     */
    $scope.terminLoeschen = function(ngIndex) {
        $scope.gr.termine.splice(ngIndex, 1);
      }

      // einen überflüssigen Termin aus der Auswahl löschen
      $scope.terminLoeschenE = function(ngIndex) {
          $scope.gre.termine.splice(ngIndex, 1);
        }


    /**
     * Fuegt einen weiteren Termin hinzu
     * (Gruppe erstellen oder bearbeiten)
     */
    $scope.terminHinzufuegen = function() {
        var lastAppDate = $scope.gr.termine[$scope.gr.termine.length - 1].datum;
        var ndTemp = DBGruppService.getDate(lastAppDate);
        ndTemp.setDate(lastAppDate.getDate() + 1);
        var newAppDate = DBGruppService.getDate(ndTemp);
        var start = $scope.gr.termine[$scope.gr.termine.length - 1].start;
        var ende = $scope.gr.termine[$scope.gr.termine.length - 1].ende;
        var app = DBGruppService.getAppointment($scope.raeume[0], newAppDate, start, ende);
        $scope.gr.termine.push(app);
      }

      // Fügt einen weiteren Termin hinzu
      $scope.terminHinzufuegenE = function() {
          var lastAppDate = $scope.gre.termine[$scope.gre.termine.length - 1].datum;
          var ndTemp = DBGruppService.getDate(lastAppDate);
          ndTemp.setDate(lastAppDate.getDate() + 1);
          var newAppDate = DBGruppService.getDate(ndTemp);
          var start = $scope.gre.termine[$scope.gre.termine.length - 1].start;
          var ende = $scope.gre.termine[$scope.gre.termine.length - 1].ende;
          var app = DBGruppService.getAppointment($scope.raeume[0], newAppDate, start, ende);
          $scope.gre.termine.push(app);
        }

      /**
       * Ruft die Backendschnittstelle zum Gruppe erstellen auf und
       * aktualisiert die View. Die zu erstelle Gruppe wird dem Backend
       * uebergeben.
       */
    $scope.addGruppe = function() {

      // Zufällige Dozenten erzeugen -> Nur vorläufig
      //$scope.gr.dozent = DBGruppService.dozenten[DBGruppService.genRdIndex(DBGruppService.dozenten)];
      //$scope.gr.assistent = DBGruppService.assistenten[DBGruppService.genRdIndex(DBGruppService.assistenten)];

      // Abgabetermine erzeugen
      var term = DBGruppService.getAppointments($scope.gr.termine);

      var gr = {};
      gr.termine = term;
      gr.dozent = $scope.gr.dozent;
      gr.assistent = $scope.gr.assistent;
      gr.maxTeams = $scope.gr.maxTeams;

      if (!DBGruppService.addGruppe(gr)) {
        // Fehlermeldung -> TODO: implement
      }

      autoscroller.erstellen = null;

      // Termine zurücksetzen
      $scope.gr.termine = DBGruppService.resetAppValues();
    }


    /**
     * Initialisiert das Gruppe bearbeiten Popup mit den aktuellen
     * Werten
     */
    $scope.initGruppeEdit = function(grpNr) {
      indexGrEdit = DBGruppService.sucheGruppe(grpNr);
      $scope.gre.grpNr = DBGruppService.getGruppen()[indexGrEdit].grpNr;
      $scope.gre.termine = DBGruppService.getGruppen()[indexGrEdit].termine;

      for(var i = 0; i < $scope.gre.termine.length; i++){

        var termin = $scope.gre.termine[i];

        termin.start = DBGruppService.sucheUhrzeit(termin.start, "start");
        termin.ende = DBGruppService.sucheUhrzeit(termin.ende, "ende");
      }

      $scope.gre.dozent = DBGruppService.getGruppen()[indexGrEdit].dozent;
      $scope.gre.assistent = DBGruppService.getGruppen()[indexGrEdit].assistent;

      $scope.profGrEdit = $scope.gre.dozent.maID.id + " " + $scope.gre.dozent.vorname + " " + $scope.gre.dozent.nachname;
      $scope.assistGrEdit = $scope.gre.assistent.maID.id + " " + $scope.gre.assistent.vorname + " " + $scope.gre.assistent.nachname;
      $scope.gre.maxTeams = DBGruppService.getGruppen()[indexGrEdit].maxTeams;
    }

    /**
     * Initialsiert das Gruppendetail Popup fuer die ausgewaehlte Gruppe
     * auf der View.
     */
    $scope.initGruppeDetails = function(grpNr) {

      var ngIndex = DBGruppService.sucheGruppe(grpNr);
      $scope.grd.grpNr = DBGruppService.getGruppen()[ngIndex].grpNr;
      $scope.grd.termine = DBGruppService.getGruppen()[ngIndex].termine;
      $scope.grd.dozent = DBGruppService.getGruppen()[ngIndex].dozent;
      $scope.grd.assistent = DBGruppService.getGruppen()[ngIndex].assistent;
      $scope.grd.raum = DBGruppService.getGruppen()[ngIndex].raum;
    }


    /**
     * Ruft die Backendschnittstelle zum Gruppe bearbeiten auf und
     * aktualisiert die View. Die zu bearbeitende Gruppe wird dem Backend
     * uebergeben.
     */
    $scope.editGruppe = function() {
      var term = DBGruppService.getAppointments($scope.gre.termine);

      var gr = {};
      gr.grpNr = $scope.gre.grpNr;
      gr.termine = term;
      gr.dozent = $scope.gre.dozent;
      gr.assistent = $scope.gre.assistent;
      gr.maxTeams = $scope.gre.maxTeams;

      if(!DBGruppService.editGruppe(indexGrEdit, gr)){

        // Fehlermeldung -> TODO: implement
      }

      // Termine zurücksetzen
      $scope.gre.termine = DBGruppService.resetAppValues();

      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#gruppeBearbeiten').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }

    /**
     * Initialisiert die Gruppe die geloescht werden soll.
     * Sucht dazu den index in der Tabelle
     */
    $scope.initGruppeLoeschen = function(grpNr) {
      indexGrLoeschen = DBGruppService.sucheGruppe(grpNr);
    }


    /**
     * Loescht die zuvor initialisierte Gruppe
     */
    $scope.gruppeLoeschen = function() {

      if(!DBGruppService.gruppeLoeschen(indexGrLoeschen)){
        // TODO: Fehlermeldung
      }
      $scope.indexGrLoeschen = 0;

      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#gruppeLoeschen').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }


    /**
     * Initialisiert mithilfe des Backends die Teilnehmeruebersicht
     * fuer die gewaehlte Gruppe
     */
    $scope.initTmUebersicht = function(grpNr) {

      var index = DBGruppService.sucheGruppe(grpNr);
      var gruppe = DBGruppService.getGruppen()[index];
      DBGruppTmService.initTmUebersicht(gruppe);
    }

    $scope.getGruppen = function(){
      return DBGruppService.getGruppen();
    }

    $scope.getFach = function(){
      return DBGruppService.getFach();
    }

    $scope.getProfessoren = function(){
      return DBVeranstService.getProfessorenString();
    }

    $scope.getAssistenten = function(){

      return DBGruppService.getAssistentenString();
    }
    // ############################################################################################################
  };

  // Controller bei der App "anmelden"
  app.controller("GruppenController", GruppenController);

  // Code sofort ausführen
}());
