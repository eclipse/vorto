/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
define(["../init/appDirective"], function(repository) {

repository.directive('pwCheck', [function () {
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

repository.directive('emailCheck', [function () {
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

repository.directive( 'dynTemplate', function ( $compile ) {
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

repository.directive('totalSummary', function () {

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

function generateSearchUrl(tableState) {
	var ret = {};
    if(tableState.sort.predicate) {
    	var order = (tableState.sort.reverse ? 'desc': 'asc');
    	ret = {'p': tableState.sort.predicate, 'o': order};
    }  
    if(tableState.search.predicateObject) {
    	if(tableState.search.predicateObject.$) {
    		angular.extend(ret, {'s': 
    			String(tableState.search.predicateObject.$)});
    	}
        if(tableState.search.predicateObject.type) {
        	angular.extend(ret, {'t': 
        		tableState.search.predicateObject.type});
        }
    }
    if(tableState.pagination.start != 0) {
    	ret.start = tableState.pagination.start;
    }
    return ret;
}

repository.directive('stPersist', ['$location', '$rootScope', function ($location, $rootScope) {
    return {
        require: '^stTable',
        link: function (scope, element, attr, ctrl) {
            scope.$watch(function () {
                return ctrl.tableState();
            }, function (newValue, oldValue) {
            	if (newValue.pagination.start2) {
            	    if (newValue.pagination.start2 < newValue.pagination.totalItemCount) {
            		newValue.pagination.start = newValue.pagination.start2;
            		newValue.pagination.start2 = undefined;
            		ctrl.pipe();
            	    }
                }
                if (newValue !== oldValue) {        	
                	$location.search(generateSearchUrl(newValue));
                	$rootScope.searchState = newValue;
                }
            }, true);
            
            // load previous state
            var tableState = ctrl.tableState();
            var savedState = {};
            var hasState = false;
            angular.copy(tableState, savedState);
            var searchState = $location.search();
            if(!savedState.search.predicateObject) {
            	savedState.search.predicateObject = {};
            }
            if(searchState.s) {
            	savedState.search.predicateObject.$ = searchState.s;
            	hasState = true;
            }
            if(searchState.t) {
            	savedState.search.predicateObject.type = searchState.t;
            	hasState = true;
            }
            if(searchState.p) {
            	savedState.sort.predicate = searchState.p;
            	if(searchState.o === 'desc') {
            		savedState.sort.reverse = true;
            	} else {
            		savedState.sort.reverse = false;
            	}
            	hasState = true;
            }
            if(searchState.start) {
            	savedState.pagination.start2 = parseInt(searchState.start);
            	hasState = true;
            }
            angular.extend(tableState, savedState);
            
			if(!hasState) {
				// restore default
				tableState.search.predicateObject = {};
				tableState.pagination.start = 0;
				tableState.sort.predicate = undefined;
			}
			ctrl.pipe();
        }
    };
}]);

repository.directive('stBacklink', ['$rootScope', function ($rootScope) {
	return {
		controller: 'DetailsController',
	    link: function (scope, element, attrs) {
	    	if($rootScope.searchState) {
	    		var savedState = generateSearchUrl($rootScope.searchState);
	    		if(!angular.equals(savedState, {})) {
	    			var hash = decodeURIComponent($.param(savedState));
	    			attrs.$set('href', attrs.href + '?' + hash);
	    		}
	    	}
	    }
	};
}]);

repository.directive('stReset', ['$route', function($route){
	return function(scope, element, attrs) {
		element.bind('click',function(){
			$route.reload();
		});
	}   
}]);

});