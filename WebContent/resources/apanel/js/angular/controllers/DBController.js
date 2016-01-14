/**
 * Controller um eine Verbindung zur Datenbank herzustellen
 * TODO: Vorerst nur für das Datenbank
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var DBController = function($scope, $http) {

    var url = "http://localhost:8080/SE2-Praktikumssoftware/students";
    $scope.students = [];
    $scope.currStudent;
    $scope.clicked = false;
    $scope.error = false;

    $scope.getAllStudents = function(){

      $http.get(url).
      	// Funktion, falls gültige Daten zurückkommen
        then(function(response){
        	for(i = 0; i < response.data.length; i++){
        		var data = response.data;
        		$scope.students.push({matrNr: data[i].matrNr, vorName: data[i].vorName, nachName: data[i].nachName});
        	}
        	$scope.clicked = true;
        },
        // Funktion bei Fehler
        function(response){
        	$scope.error = true;
        });
    }

    $scope.isClicked = function(){
      return $scope.clicked;
    }

    $scope.isError = function(){
    	return $scope.error;
    }



  };

  // Controller bei der App "anmelden"
  app.controller("DBController", DBController);

  // Code sofort ausführen
}());
