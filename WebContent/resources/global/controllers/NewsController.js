/**
 * Managed den Loginvorgang
 */
(function() {

  // Controllerdefintion
  // $scope = model: das Model-Objekt
  var app = angular.module("SE2-Software");

  var NewsController = function($scope, DBNewsService) {

	  // Felder
    // #########################################################################
    $scope.news = {};
    $scope.news.header = DBNewsService.getHeader();
    $scope.news.bod = DBNewsService.getBody();
    // #########################################################################


    /**
     * Laedt mithilfe des Backends die aktuellen News und
     * zeigt diese auf der View an.
     */
    $scope.initCurrNews = function(){

      DBNewsService.initNews();
    }

    /**
     * holt die News-Ueberschrift aus dem Backend
     */
    $scope.getNewsHeader = function(){


        return DBNewsService.getHeader();
    }

    /**
     * Holt den News-Body aus dem Backend
     */
    $scope.getNewsBody = function(){

      return DBNewsService.getBody();

    }

    /**
     * Speichert die zuvor erstelle News im
     * Backend. Ruft dazu die entsprechende
     * Schnittstelle auf.
     */
    $scope.speichereNews = function(){

      DBNewsService.speichereNews($scope.news.header, $scope.news.bod);
    }


  };

  // Controller bei der App "anmelden"
  app.controller("NewsController", NewsController);

  // Code sofort ausführen
}());
