define(["angular"], function(angular) {
  return angular.module("apps.controller", [
    "apps.directive",
    "apps.service",
    "smart-table"
  ]);
});
