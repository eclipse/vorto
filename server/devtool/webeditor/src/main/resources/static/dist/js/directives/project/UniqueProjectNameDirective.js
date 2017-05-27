define(["angular"], function(angular) {
  angular
    .module("apps.directive")
    .controller("ngUniqueProjectName", UniqueProjectNameDirective);

  UniqueProjectNameDirective.$inject = ["$http", "$scope"]

  function UniqueProjectNameDirective($http, $scope){
    var directive = {
      require: 'ngModel',
      link: link
    }

    return directive;

    function link(scope, elem, attrs, ctrl) {
      elem.on('blur', function(evt) {
        scope.$apply(function() {
          if (elem.val()) {
            $http.get('./rest/project/' + elem.val() + '/check').success(
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
});
