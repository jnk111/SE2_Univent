/**
 * Managed den Datepicker
 */
(function() {

  // Controllerdefinition
  var app = angular.module("SE2-Software");

  // $scope = model object
  var DateController = function($scope) {


    var praktStart = false;
    var praktEnde = false;

    var wpStart = false;
    var wpEnde = false;

    var poStart = false;
    var poEnde = false;

    var praktStartEinzel = false;
    var praktEndeEinzel = false;

    var wpStartEinzel = false;
    var wpEndeEinzel = false;

    var poStartEinzel = false;
    var poEndeEinzel = false;

    var prakt = [praktStart, praktEnde];
    var wp = [wpStart, wpEnde];
    var po = [poStart, poEnde];

    var praktEinzel = [praktStartEinzel, praktEndeEinzel];
    var wpEinzel = [wpStartEinzel, wpEndeEinzel];
    var poEinzel = [poStartEinzel, poEndeEinzel];

    $scope.anmeldung = [];
    $scope.anmeldung.push(prakt);
    $scope.anmeldung.push(wp);
    $scope.anmeldung.push(po);
    $scope.anmeldung.push(praktEinzel);
    $scope.anmeldung.push(wpEinzel);
    $scope.anmeldung.push(poEinzel);

    $scope.ameldungEinzel = [];




    $scope.today = function() {
      $scope.dt = new Date();
    }

    $scope.today();

    $scope.clear = function() {
      $scope.dt = null;
    }

    // Sonntag deaktivieren
    $scope.disabled = function(date, mode) {
      return (mode === 'day' && (date.getDay() === 0));
    }

    $scope.toggleMin = function() {
      $scope.minDate = $scope.minDate ? null : new Date();
    }
    $scope.toggleMin();
    $scope.maxDate = new Date(2020, 5, 22);


    $scope.openCal = function(index) {
      $scope.status[index].opened = true;
    }


    $scope.openCalAnmeldung = function(index, index2){

      $scope.anmeldung[index][index2] = true;
    }

    $scope.addCal = function() {
      $scope.status.push({
        opened: false
      });
    }


    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    }

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[2];

    $scope.status = [];
    $scope.status.push({
      opened: false
    });
    $scope.status.push({
      opened: false
    });
    $scope.status.push({
      opened: false
    });
    $scope.status.push({
      opened: false
    });



    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 2);
    $scope.events = [{
      date: tomorrow,
      status: 'full'
    }, {
      date: afterTomorrow,
      status: 'partially'
    }];

    $scope.getDayClass = function(date, mode) {
      if (mode === 'day') {
        var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

        for (var i = 0; i < $scope.events.length; i++) {
          var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

          if (dayToCheck === currentDay) {
            return $scope.events[i].status;
          }
        }
      }
    }
  };

  // Controller bei der App "anmelden"
  app.controller("DateController", DateController);

  // Code sofort ausführen
}());
