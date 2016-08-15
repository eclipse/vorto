define(["angular"], function(angular) {
  var app = angular.module('apps.directive', []);

  app.directive('ngUniqueProjectName', ['$http', function($http) {
    return {
      require: 'ngModel',
      link: function(scope, elem, attrs, ctrl) {
        elem.on('blur', function(evt) {
          scope.$apply(function() {
            if (elem.val()) {
              $http.get('./project/' + elem.val() + '/check').success(
                function(data, status, headers, config) {
                  ctrl.$setValidity('unique', true);
                }).error(function(data, status, headers, config) {
                ctrl.$setValidity('unique', false);
              });
            }else{
                ctrl.$setValidity('unique', true);            
            }
          });
        });
      }
    }
  }]);

  return app;
});
