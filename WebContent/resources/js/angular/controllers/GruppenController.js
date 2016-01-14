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
    $scope.gruppen = DBGruppService.hcGruppenDaten;
    $scope.raeume = DBGruppService.raeume;
    $scope.professoren = DBVeranstService.professoren;
    $scope.assistenten = DBVeranstService.assistenten;



    // SelectBox-Titles
    // ############################################################################################################
    $scope.selBoxAnzTeamTitle = "Anzahl Teammitglieder: ";
    $scope.termWaehlenTitle = "Termne wählen (KW) ... ";
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

    // ############################################################################################################



    // Felder initialisiern
    // ############################################################################################################
    $scope.gr = {};
    $scope.gr.sem;
    $scope.gr.grpNr = DBGruppService.grNummern[0];
    $scope.gr.termine = $scope.termine;
    $scope.gr.dozent = "";
    $scope.gr.assistent = "";
    $scope.gr.raum = DBGruppService.raeume[0];
    $scope.gr.minTeams = 0;
    $scope.gr.maxTeams = 10;
    $scope.gr.resTeams = 5;
    $scope.gr.anzTeams = 2;
    // ############################################################################################################



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

    $scope.isValidStartTime = function(ngIndex) {

      var start = $scope.gr.termine[ngIndex].start;
      var end = $scope.gr.termine[ngIndex].ende;

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

    $scope.isValidDateComplete = function() {

      for (i = 0; i < $scope.gr.termine.length; i++) {
        if (!$scope.isValidDate(i)) {
          return false;
        }
      }

      return true;
    }

    $scope.isValidProfInput = function(expr){

        for(var i = 0; i < DBVeranstService.professoren.length; i++){

          if(DBVeranstService.professoren[i] == expr){
            return true;
          }
        }
        return false;
      }

    $scope.isValidAssistInput = function(expr){

        for(var i = 0; i < DBVeranstService.assistenten.length; i++){

          if(DBVeranstService.assistenten[i] == expr){
            return true;
          }
        }
        return false;
      }

    // TODO:
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    $scope.isValidRoom = function(room) {
      return true;
    }

    // TODO: Wenn alle Gruppen für dieses Fach bereits vorhanden -> ungültig
    $scope.isValidSubject = function(subject) {
      return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Aktiviert/Deaktiviert den Speichern-Button
    $scope.isFilledCompleteErstellen = function() {
      return $scope.isValidDateComplete() && $scope.isValidStartTimeComplete()
      			&& $scope.isValidProfInput($scope.gr.dozent)
      			 && $scope.isValidAssistInput($scope.gr.assistent);
    }


    $scope.isFilledCompleteBearbeiten = function() {
        return $scope.isValidDateComplete() && $scope.isValidStartTimeComplete();
      }
      // ############################################################################################################



    // Button-klick Funktionen
    // ############################################################################################################

    // einen überflüssigen Termin aus der Auswahl löschen
    $scope.terminLoeschen = function(ngIndex) {
        $scope.gr.termine.splice(ngIndex, 1);
      }


    // Fügt einen weiteren Termin hinzu
    $scope.terminHinzufuegen = function() {
        var lastAppDate = $scope.gr.termine[$scope.gr.termine.length - 1].datum;
        var ndTemp = DBGruppService.getDate(lastAppDate);
        ndTemp.setDate(lastAppDate.getDate() + 1);
        var newAppDate = DBGruppService.getDate(ndTemp);
        var start = $scope.gr.termine[$scope.gr.termine.length - 1].start;
        var ende = $scope.gr.termine[$scope.gr.termine.length - 1].ende;
        var app = DBGruppService.getAppointment(newAppDate, start, ende);
        $scope.gr.termine.push(app);
      }

      // Fügt eine neue Gruppe in die Tabelle ein
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
      gr.minTeams = $scope.gr.minTeams;
      gr.maxTeams = $scope.gr.maxTeams
      gr.resTeams = $scope.gr.resTeams;
      gr.anzTeams = $scope.gr.anzTeams;
      if (!DBGruppService.addGruppe(gr)) {
        // Fehlermeldung -> TODO: implement
      }

      autoscroller.erstellen = null;

      // Termine zurücksetzen
      $scope.gr.termine = DBGruppService.resetAppValues();
    }


    // initialisiert das Popup mit den vorhandenen Werten
    $scope.initGruppeEdit = function(grpNr) {
      indexGrEdit = DBGruppService.sucheGruppe(grpNr);
      $scope.gr.grpNr = DBGruppService.hcGruppenDaten[indexGrEdit].grpNr;
      $scope.gr.termine = DBGruppService.hcGruppenDaten[indexGrEdit].termine;
      $scope.gr.dozent = DBGruppService.hcGruppenDaten[indexGrEdit].dozent.vollerName;
      $scope.gr.assistent = DBGruppService.hcGruppenDaten[indexGrEdit].assistent.vollerName;
      $scope.gr.minTeams = DBGruppService.hcGruppenDaten[indexGrEdit].minTeams;
      $scope.gr.maxTeams = DBGruppService.hcGruppenDaten[indexGrEdit].maxTeams
      $scope.gr.resTeams = DBGruppService.hcGruppenDaten[indexGrEdit].resTeams;
      $scope.gr.anzTeams = DBGruppService.hcGruppenDaten[indexGrEdit].anzTeams;


    }

    // Initialisert das popup mit den Gruppendetails mit den vorhandenen Werten
    $scope.initGruppeDetails = function(grpNr) {

      var ngIndex = DBGruppService.sucheGruppe(grpNr);
      $scope.gr.grpNr = DBGruppService.hcGruppenDaten[ngIndex].grpNr;
      $scope.gr.termine = DBGruppService.hcGruppenDaten[ngIndex].termine;
      $scope.gr.dozent = DBGruppService.hcGruppenDaten[ngIndex].dozent;
      $scope.gr.assistent = DBGruppService.hcGruppenDaten[ngIndex].assistent;
      $scope.gr.raum = DBGruppService.hcGruppenDaten[ngIndex].raum;
    }


    // Ändert den Tabelleneintrag für eine Gruppe anhand der Benutzereingaben
    $scope.editGruppe = function() {
      var term = DBGruppService.getAppointments($scope.gr.termine);

      var gr = {};
      gr.grpNr = $scope.gr.grpNr;
      gr.termine = term;
      gr.dozent = DBVeranstService.getAngestellter("professor", $scope.gr.dozent);
      gr.assistent = DBVeranstService.getAngestellter("assistent", $scope.gr.assistent);
      gr.minTeams = $scope.gr.minTeams;
      gr.maxTeams = $scope.gr.maxTeams
      gr.resTeams = $scope.gr.resTeams;
      gr.anzTeams = $scope.gr.anzTeams;

      if(!DBGruppService.editGruppe(indexGrEdit, gr)){

        // Fehlermeldung -> TODO: implement
      }

      // Termine zurücksetzen
      $scope.gr.termine = DBGruppService.resetAppValues();

      // Modal schließen forcieren, bug über normalen Weg (data-dismiss-tag) TODO: FIX
      $('#gruppeBearbeiten').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }

    // Speichert den Index des zu loeschenden Gruppeneintrags
    $scope.initGruppeLoeschen = function(grpNr) {
      indexGrLoeschen = DBGruppService.sucheGruppe(grpNr);
    }


    // Löscht den Gruppeneintrag in der Tabelle
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


    // Initialsiert die Teilnehmerübersicht für die gewählte Gruppe
    // TODO: Wird noch um einige Funktionen erweitert
    $scope.initTmUebersicht = function(grpNr) {

      var index = DBGruppService.sucheGruppe(grpNr);
      var gruppe = DBGruppService.hcGruppenDaten[index];
      DBGruppTmService.initTmUebersicht(gruppe);

    }
    // ############################################################################################################
  };

  // Controller bei der App "anmelden"
  app.controller("GruppenController", GruppenController);

  // Code sofort ausführen
}());
