var repositoryDirectives = angular.module('repositoryDirectives', []);

repositoryDirectives.directive('pwCheck', [function () {
    return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ctrl) {
        var firstPassword = '#' + attrs.pwCheck;
        elem.add(firstPassword).on('keyup', function () {
          scope.$apply(function () {
            var v = elem.val()===$(firstPassword).val();
            ctrl.$setValidity('pwmatch', v);
          });
        });
      }
    }
}]);

repositoryDirectives.directive('emailCheck', [function () {
    return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ctrl) {
        var firstEmail = '#' + attrs.emailCheck;
        elem.add(firstEmail).on('keyup', function () {
          scope.$apply(function () {

            var v = elem.val()===$(firstEmail).val();
            ctrl.$setValidity('emailmatch', v);
          });
        });
      }
    }
}]);