define(["angular"], function(angular) {
  var app = angular.module('apps.directive', []);

  app.directive('ngUniqueProjectName', ['$http', function($http) {
    return {
      require: 'ngModel',
      link: function(scope, elem, attrs, ctrl) {
        elem.on('blur', function(evt) {
          scope.$apply(function() {
            $http.get('./project/check/' + elem.val()).success(
              function(data, status, headers, config) {
                ctrl.$setValidity('unique', true);
              }).error(function(data, status, headers, config) {
              ctrl.$setValidity('unique', false);
            });
          });
        });
      }
    }
  }]);

  return app;
});
