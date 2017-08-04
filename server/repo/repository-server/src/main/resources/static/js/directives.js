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

repositoryDirectives.directive('totalSummary', function () {

    return {
    restrict: 'E',
    require: '^stTable',
    template:'<span><b>{{pagination.totalItemCount}} models found</b></span>',
    scope: {},
    link: function (scope, element, attr, ctrl) {
      var listener = function (val) {
        scope.pagination = ctrl.tableState().pagination;
        scope.to = Math.min(scope.pagination.start + scope.pagination.number, scope.total || 0);
      };
      scope.$watch(ctrl.tableState(), listener, true);
    }
  }
});

repositoryDirectives.directive( 'dynTemplate', function ( $compile ) {
  return {
    scope: true,
    link: function ( scope, element, attrs ) {
      var el;

      attrs.$observe( 'src', function ( template ) {
        if ( angular.isDefined( template ) ) {
          el = $compile( template )( scope );
          element.html("");
          element.append( el );
        }
      });
    }
  };
});