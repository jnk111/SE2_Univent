

(function() {

  // Hauptapp referenzieren damit Service sichtbar wird
  var app = angular.module("SE2-Software");

  // Servicedefinition
  var UeberschneidungsPruefer = function($http) {

    // Felder
    // #########################################################################

    var alleUsMarkieren = false;
    var gruppenTemp = []; // Zwischenspeicher für Alle Überschneidungen ausblenden
    var ueberschneidung = false;

    // #########################################################################


    // Helper
    // #########################################################################

    /**
     * Sucht den index einer Gruppe in einem GruppenArray
     */
    var sucheGruppeIndex = function(gruppe, gruppen){

      for(var i = 0; i < gruppen.length; i++){

        if(gruppen[i] == gruppe){

          return i;
        }
      }

      return -1;
    }

    /**
     * Prüft ob zwei Termine gleich sind
     * param: termin1: Der erste Termin, termin2: der zweite Termin
     *
     */
    var istTerminGleich = function(termin1, termin2){

      return termin1.datum.getDate() == termin2.datum.getDate()
              && termin1.datum.getMonth() == termin2.datum.getMonth()
                && termin1.datum.getFullYear() == termin2.datum.getFullYear()
                 && parseInt(termin1.start.stunden) === parseInt(termin2.start.stunden)
                  && parseInt(termin1.start.minuten) === parseInt(termin2.start.minuten);
    }


    /**
     * Prueft ob eine Gruppe in dem Gruppenarray enthalten ist und gibt
     * dann den Index dieser Gruppe in dem Array zurueck.
     */
    var enthaeltGruppe = function(gruppe, gruppen){

      for(var i = 0; i < gruppen.length; i++){

        if(gruppe == gruppen[i]){
          return true;
        }
      }
      return false;
    }


    /**
     * Prueft ob eine Gruppe in dem Array welches nur Gruppen mit Ueberschneidungen
     * enthaelt, enthalten ist.
     */
    var usEnthaeltGruppe = function(usGruppen, gruppe){

      for(var i = 0; i < usGruppen.length; i++){

        if(usGruppen[i] == gruppe){
          return true;
        }
      }
      return false;
    }

    // #########################################################################



    // SCHNITTSTELLE
    // #########################################################################

    /**
     * Prueft alle Terminueberschneidungen in einem Gruppenarray und markiert diese
     * auf der View
     */
    var markiereUeberschneidungen = function(gruppen){

      if(gruppen.length > 1){                         // wenn GruppenArray > 1, falls nicht, keine Ueberschneidung möglich

        for(var i = 0; i < gruppen.length; i++){      // Für jede Gruppen

          var gruppe1 = gruppen[i];
          var termine1 = gruppe1.termine;             // hole die Termine

          for(var j = 0; j < termine1.length; j++){   // Für jeden Termin dieser Gruppe

            var t1 = termine1[j];

            for(var z = i+1; z < gruppen.length; z++){  // hole weitere Gruppe, die nicht die gleiche ist

              var gruppe2 = gruppen[z];

              if(!(gruppe1 == gruppe2)){

                var termine2 = gruppe2.termine;

                for(var u = 0; u < termine2.length; u++){ // Für jeden Termin dieser anderen Gruppe

                  var t2 = termine2[u];

                  if(istTerminGleich(t1, t2)){            // Wenn Termin gleich

                    if(!(usEnthaeltGruppe(t1.usGruppen, gruppe2))){ // und die Gruppe2 noch nicht
                      t1.usGruppen.push(gruppe2);                   // in den Ueberschneidungen von Gruppe 1 markiert ist
                                                                    // markiere iie Ueberschneidung (push ins Array)
                    }

                    if(!(usEnthaeltGruppe(t2.usGruppen, gruppe1))){ // und die Gruppe1 noch nicht
                        t2.usGruppen.push(gruppe1);                 // in den Ueberschneidungen von Gruppe 2 markiert ist
                                                                    // markiere iie Ueberschneidung (push ins Array)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }


    // Prüft alle Gruppeneinträge auf mögliche Überschneidungen und markiert diese, falls Checkbox aktiviert
    // Wenn Checkbox aktiviert und danach deaktiviert wird, werden alle markierten Ueberschneidungen aus nicht
    // ausgewählten Einträgen entfernt
    // param: gruppenEinträge: GruppenEinträge, gruppen: GruppenArray
    var pruefeAlleMoeglichenUeberschneidungen = function(gruppenEintraege, gruppen){

      if(!alleUsMarkieren){

        alleUsMarkieren = true;
        for(var i = 0; i < gruppenEintraege.length; i++){   // Hole alle Gruppen

          var gruppe = gruppenEintraege[i].gruppe
          gruppenTemp.push(gruppe);
        }

        pruefeUeberschneidungen(gruppenTemp);   // Prüfe Überschneidungen für alle GruppenEinträge (ausgewahlt und nicht ausgewählt)

      }else{

        alleUsMarkieren = false;

        for(var i = 0; i < gruppenEintraege.length; i++){ // Entferne alle Überschneidungen in nicht ausgewählten GruppenEinträgen

          var gruppe = gruppenEintraege[i].gruppe;

          if(!(enthaeltGruppe(gruppe, gruppen))){

            var termine = gruppe.termine;

            for(var j = 0; j < termine.length; j++){

              termine[j].usGruppen = [];
            }
          }
        }
        pruefeUeberschneidungen(gruppen);                 // Stelle Überschneidungen in ausgewählten Einträgen wieder her
        gruppenTemp = [];                                 // Temp zurücksetzen
      }
    }


    /**
     * Prueft und markiert alle moeglichen Terminueberschneidungen in
     * einem Gruppenarray
     */
    var pruefeUeberschneidungen = function(gruppen){

      setzeUeberschneidungenZurueck(gruppen); // Erst zurücksetzen falls GruppenArray vorher verändert wurde
      markiereUeberschneidungen(gruppen);   // Ueberschneidungen erneut prüfen
    }


    // Setzt die Überschneidungen in den Terminen eines GruppenArrays zurück
    // param: Das GruppenArray
    var setzeUeberschneidungenZurueck = function(gruppen){

      for(var i = 0; i < gruppen.length; i++) {

        var termine = gruppen[i].termine;

        for(var j = 0; j < termine.length; j++){

          termine[j].usGruppen = [];
        }
      }
    }

    /**
     * Prueft auf das erste Vorkommen einer Terminueberschneidung in einem
     * Gruppenarray.
     */
    var hatUeberschneidungen = function(gruppen){

      for(var i = 0; i < gruppen.length; i++){

        var gruppe = gruppen[i];
        var termine = gruppe.termine;

        for(var j = 0; j < termine.length; j++){

          if(termine[j].usGruppen.length > 0){

            return true;
          }
        }
      }
      return false;
    }

    // #########################################################################

    // Gebe dieses Object zurück und mache die Methoden zugänglich
    // -----------------------------------------------------------------
    return {

      sucheGruppeIndex: sucheGruppeIndex,
      enthaeltGruppe: enthaeltGruppe,
      pruefeAlleMoeglichenUeberschneidungen: pruefeAlleMoeglichenUeberschneidungen,
      pruefeUeberschneidungen: pruefeUeberschneidungen,
      markiereUeberschneidungen: markiereUeberschneidungen,
      usEnthaeltGruppe: usEnthaeltGruppe,
      alleUsMarkieren: alleUsMarkieren,
      hatUeberschneidungen: hatUeberschneidungen

    };
  };

  // Service bei der Hauptapp als "Service" über factory-Methode anmelden
  app.factory("UeberschneidungsPruefer", UeberschneidungsPruefer);

  // Code sofort ausführen
}());
