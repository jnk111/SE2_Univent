/**
 * Schnittstelle und Service für Datenbankinteraktion mit Spring MVC
 * Vorerst Hardcoded Daten -> später dynamische Ermittlung durch SpringMVC
 **/
(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Login");

  // Servicedefinition
  var DBLoginService = function($http, $window) {

    // Locals
    // #########################################################################
    var url = "http://localhost:8080/SE2-Praktikumssoftware/"; // URL um Backend anzusprechen
    // #########################################################################

    // SCHNITTSTELLE
   // #########################################################################
    // Loggt einen Benutzer ein und lädt das entsprechende panel
    // Übergibt Username und Passwort ans Backend und Bekommt einen String
    // der den Panel beschreibt, der geladen werden soll zurück
    // return true = erfolgreich, false = nicht erfolgreich
    var einloggen = function(username, passwort){

      var args = [username, passwort];
      $http.post(url+"login",angular.toJson(args)).
    	// Funktion, falls gültige Daten zurückkommen
      	then(function(response){;
    	    var panel = response.data;
    	    return loadPanel(panel);
      	},
      	// Funktion bei Fehler
      	function(response){
      		return false;
      	});
    }

    // Lädt das entsprechende Panel, je nach dem welcher User einloggt
    // Student pder Angestellter
    var loadPanel = function(panel){
    	if(panel != ""){
    		$window.location = '/SE2-Praktikumssoftware/'+panel;
    		return true;
    	}
    	return false;
    }

 // #########################################################################

    // Gebe dieses Objekt und mache die Methoden sichtbar
    return {
    	einloggen: einloggen
    }

  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("DBLoginService", DBLoginService);

  // Code sofort ausführen
}());
