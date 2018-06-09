var repositoryServices = angular.module('repositoryServices', []);

repositoryServices.service('userService', function(){
	this.users = ['John', 'James', 'Jake'];
});