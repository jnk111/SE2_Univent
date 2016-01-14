/**
 * Managed die einzelnen Tabs
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");


  // $scope = model object
  var TeamController = function($scope, $log, DBTeamService, DBPraktikaService, $document) {

    $scope.teams = DBTeamService.teams;
    $scope.studentenStr = DBTeamService.studentenStr;
    $scope.aktuelleAuswahl = DBTeamService.getAktuelleAuswahl();
    $scope.aktuellerEintrag;
    $scope.gr = {};
    $scope.gr.detailGruppe = DBPraktikaService.getDetailGruppe();
    $scope.detailTeam = {};
    $scope.gr.usEinladung;
    var indexVerlassen = -1;


    // Prüft ob die Eingabe einem Studenten im Puffer entspricht
    // return false fue gefunden, true fuer nicht gefunden : boolean
    $scope.istGueltigerStudent = function(expr){

        return DBTeamService.istGueltigerStudent(expr);
    }

    /**
     * Fuegt ein ausgewaelhtes Teammitglied in die Liste Teammitglieder ein
     * (Teamkonfiguration)
     */
    $scope.waehleTeamMitgliedAus = function(expr){


        return DBTeamService.waehleTeamMitgliedAus(expr);
    }

    // Setzt den Gruppeneintrag auf "Einzelanmeldung"
    // return geklappt, nicht geklappt : boolean
    $scope.registriereEinzelAnmeldung = function(teamEintrag, index){

      if($scope.einzelAnmeldungen[index]){
        $scope.einzelAnmeldungen[index] = false;

      }else{
        $scope.einzelAnmeldungen[index] = true;
        DBTeamService.setzeTeamZurueck(teamEintrag);
      }
    }

    /**
     * Entfernt ein Mitglied aus der Liste der ausgewaehlten Mitglieder
     * (Teamkonfigurationspopup)
     */
    $scope.entferneTeamMitgliedAuswahl = function(matrNr){

      return DBTeamService.entferneTeamMitgliedAuswahl(matrNr);
    }

    /**
     * Uebernimmt die im Teamkonfigurationspopup ausgewaehlten Mitglieder
     * in die Teamkonfiguration
     */
    $scope.uebernehmeAusgewMitglieder = function(){

      var erfolg = DBTeamService.uebernehmeAusgewMitglieder();
      $scope.aktuelleAuswahl = DBTeamService.getAktuelleAuswahl();
      return erfolg;
    }

    /**
     * Initialisiert den ausgewaehlten Gruppen als Teameintraege
     * im Teamkonfigurator. 
     */
    $scope.initTeamTabelleKonfig = function(teamEintrag)
    {
        $scope.aktuellerEintrag = teamEintrag;
        DBTeamService.initTeamTabelleKonfig(teamEintrag);
        $scope.aktuelleAuswahl = DBTeamService.getAktuelleAuswahl();
    }

    /**
     * Nimmt mithife der Backendschnittstelle die empfangene Teameinladung
     * fuer ein Team an.
     */
    $scope.einladungAnnehmen = function(einladung){

      DBTeamService.einladungAnnehmen(einladung);
    }

    /**
     * Bestaetigt die Teamkonfiguration fuer die gewaehlten Gruppen
     * und sendet mithilfe des Backends Teameinladung an alle Mitglieder
     * jedes Teams, bis auf den Absender
     */
    $scope.bestaetigeTeamkonfig = function(){

        return DBTeamService.bestaetigeTeamkonfig();
    }

    /**
     * Setzt die aktuelle Teamkonfiguration vollstaendig zurueck.
     */
    $scope.setzeAlleTeamsZurueck = function(teamEintrag){

      DBTeamService.setzeAlleTeamsZurueck(teamEintrag);

    }
    
    /**
     * Prueft ob jede Teamkonfiguration die minimale Anzahl an
     * benoetigten Mitgliedern erreicht hat. Der Button
     * zum Teamkonfiguration speichern wird dann aktiviert.
     */
    $scope.alleTeamsVollstaendig = function(){

      return DBTeamService.alleTeamsVollstaendig();
    }

    /**
     * Initialisiert mithilfe des Backends die Detailinformationen
     * fuer die gewaehlte Gruppe des Teams.
     */
    $scope.initGruppeDetails = function(fachkuerzel, grpNr){

      DBPraktikaService.initGruppenDetails(fachkuerzel, grpNr);
      $scope.gr.detailGruppe = DBPraktikaService.getDetailGruppe();

    }

    /**
     * Initialisiert die Teamkonfiguration mit dem Team aus
     * der aktuellen Meldung um ein neues Mitglied zu suchen.
     */
    $scope.findeNeuesMitglied = function(meldung){
      DBTeamService.initSucheNeuesMitglied(meldung);

    }

    /**
     * Lehnt mithilfe des Backends die empfangene Teameinladung ab.
     * Ruft dazu die entsprechende Backendschnittstelle auf.
     */
    $scope.einladungAblehnen = function(meldung){

      DBTeamService.einladungAblehnen(meldung);

    }

    /**
     * Prueft ob jede Teamkonfiguration die maximale Anzahl an
     * benoetigten Mitgliedern erreicht hat. Der Butto zum Mitglied
     * hinzufuegen wird dann deaktiviert.
     */
    $scope.maximaleAnzahlMitgliederTeamErreicht = function(teamEintrag){

      var team = teamEintrag.team;
      return team.mitglieder.length >= team.maxTeiln;
    }

    $scope.maximaleAnzahlMitgliederAuswahlErreicht = function(){

      return DBTeamService.maxMitgliederErreicht();
    }


    $scope.getStudentenString = function(index){

      return DBTeamService.getStudentenString();
    }
    $scope.initTeamTabelle = function(){

      DBTeamService.initTeamTabelleMeineTabelle();

    }
    
    /**
     * Initialisiert mithilfe des Backends die Teams des aktuell
     * eingeloggten Studenten und listet diese auf der View auf
     * (vorgemerkte und angemeldete Teams)
     */
    $scope.getMeineTeams = function(){
        return DBTeamService.getMeineTeams();
    }

    $scope.getTeameintraege = function(){

      return DBTeamService.getTeameintraege();
    }

    $scope.getGruppenDetails = function(team){

      $scope.gr = DBTeamService.getGruppen()[DBTeamService.sucheGruppe(team.fachkuerzel, team.vorgGrpNr)];
    }

    $scope.getNichtBestaetigt = function(){

      return "resources/studpanel/icons/fail.html"
    }

    $scope.getBestaetigt = function(){

      return "resources/studpanel/icons/success.html"
    }

    /**
     * Ruft im Backend die schnittstelle zum Team verlassen auf und aktualisiert
     * die View
     */
    $scope.verlasseTeam = function(){

      DBTeamService.verlasseTeam(indexVerlassen);

      $('#austreten').modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
    }

    /**
     * Initialisert das Team, das vom aktuell eingeloggten Studenten
     * verlassen werden soll
     */
    $scope.initTeamVerlassen = function(team){

      indexVerlassen = DBTeamService.sucheTeam(team);

    }

    /**
     * Prueft ob alle Mitglieder eine Teams bestaetigt sind.
     * Der Button zum Team verlassen wird dann deaktiviert.
     */
    $scope.alleBestaetigt = function(team){
      return DBTeamService.alleBestaetigt(team);
    }

    /**
     * Prueft ob der aktuelle Student ein Einzelanmelder ist.
     * Ein Team zu verlassen ist dann waehrend der gesamten Einzel-
     * anmeldephase möglich.
     */
    $scope.einzelAnmelder = function(team){

      return DBTeamService.isEinzelAnmelder(team);
    }

    /**
     * Zieht mithilfe des Backends eine Teameinladung zurück
     * Ruft dazu die entsprechende Schnittstelle im Backend auf.
     * Die View wird danach aktualisiert und das Team nicht mehr
     * angezeigt.
     */
    $scope.einladungZurueckziehen = function(stud){

      DBTeamService.zieheEinladungZurueck($scope.detailTeam, stud);
    }

    /**
     * Initialsiert die Details des aktuell gewaehlten Teams.
     */
    $scope.initTeamDetails = function(index){

      $scope.detailTeam = DBTeamService.getMeineTeams()[index];

    }

    /**
     * Gibt die Details des gewaehlten Teams zurueck.
     */
    $scope.getDetailTeam = function(){

      return $scope.detailTeam;
    }

    /**
     * Prueft die Ueberschneidungen fuer das Team, fuer das eine Einladung vorliegt.
     * Ruft dazu die entsprechende Schnittstelle im Backend auf, um alle Anmeldungen
     * des aktuellen Studenten zu ermitteln.
     */
    $scope.initUeberschneidungen = function(meldung){

      $scope.usEinladung = meldung;
      DBTeamService.initUeberschneidungen(meldung.team);
    }

    /**
     * Gibt die Gruppen die Ueberschneidungen mit einem Teambeitritt verursachen
     * wuerden zurueck und zeigt diese auf der View an.
     */
    $scope.getUSGruppen = function(){

      if($scope.usEinladung != undefined){
        DBTeamService.pruefeUeberschneidungen($scope.usEinladung.team);
      }

      return DBTeamService.getUeberschneidungen();
    }




  };


  // Controller bei der app "anmelden"
  app.controller("TeamController", TeamController);

  // Code sofort ausführen
}());
