/**
 * Managed die möglichen Edits
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var DatenImportExportController = function($scope, DBDatenImExService) {

    $scope.impIndex;

    /**
     * Holt die Ordner der Backups aus dem Backend
     * und liestet Sie in der View auf
     * 
     * @return Liste von Backupdateien
     */
    $scope.getDateien = function(){

      return DBDatenImExService.getDateien();
    }

    /**
     * Stellt das gewaehlte Backup wieder.
     * Ruft dazu die Schnittstelle im Backend auf.
     * Der Vorgang wird im Hintergrund ausgefuehrt.
     * 
     * @return erfolgreich/nicht erfolgreich
     */
    $scope.wiederherstellen = function(index){

      DBDatenImExService.stelleBackupWiederHer(index);
    }

    /**
     * Ruft im Backend die Schnittstelle zum Backup erstellen auf.
     * Diese sichert dann alle Daten der Datenbank und speichert diese
     * als JSON
     * 
     * @return erfolgreich/nicht erfolgreich
     */
    $scope.datenSichern = function(){

      DBDatenImExService.sichereDaten();
    }

    /**
     * Holt alle Importdateinamen aus dem Backend
     * und listet diese in der View auf.
     * 
     * @return Liste von Importdateien
     */
    $scope.getDateienImp = function(){
      return DBDatenImExService.getDateienImp();
    }

    /**
     * Importiert Daten aus einer uebergebenen Datei.
     * Ruft dazu im Backend die entsprechende Schnittstelle auf.
     * 
     * @return boolean : erfolgreich/nicht erfolgreich
     */
    $scope.importiere = function(){
      DBDatenImExService.importiere($scope.impIndex);

    }

    /**
     * Initialisert den Datenimport fuer eine gewaehlte Datei.
     * Sucht dafuer den index der gewahlten Datei und speichert
     * diese zwischen. Im naechsten Schritt werden die Daten
     * importiert.
     * 
     * @return void
     */
    $scope.initImport = function(index){
      $scope.impIndex = index;

    }



    /**
     * Exportiert eine gewaehlte Datei.
     * Ruft dazu im Backend die entsprechende
     * Schnittstelle auf.
     * 
     * @return boolean : erfolgreich/nicht erfolgreich
     */
    $scope.exportiere = function(index){

      DBDatenImExService.exportiere(index);
    }

    /**
     * Holt alle Exportdateinamen aus dem Backend
     * und listet diese in der View auf.
     * 
     * @return Liste von Exportdateien
     */
    $scope.getDateienEx = function(){

      return DBDatenImExService.getDateienEx();
    }

  };

  // Controller bei der App "anmelden"
  app.controller("DatenImportExportController", DatenImportExportController);

  // Code sofort ausführen
}());
