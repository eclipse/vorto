define(["../init/AppConfig"], function(configs) {
  configs.config(function(toastrConfig) {
    angular.extend(toastrConfig, {
      autoDismiss: true,
      containerId: 'toast-container',
      maxOpened: 0,
      newestOnTop: true,
      positionClass: 'toast-bottom-left',
      preventDuplicates: false,
      preventOpenDuplicates: false,
      target: 'body'
    });
  });
});
