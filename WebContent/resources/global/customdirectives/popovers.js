/**
 + Benutzderdefinierte AngularJS Direktive um mit Boostrap popovers anzeigen zu können
**/

(function(){
  var app = angular.module('SE2-Software');
  app.directive('customPopover', function () {
      return {
          restrict: 'A',
          template: '<span class="glyphicon glyphicon-pencil"></span>',
          link: function (scope, el, attrs) {
              scope.label = attrs.popoverLabel;

              $(el).popover({
                  trigger: 'click',
                  html: true,
                  content: attrs.popoverHtml,
                  placement: attrs.popoverPlacement
              });
          }
      };
  });
}());
