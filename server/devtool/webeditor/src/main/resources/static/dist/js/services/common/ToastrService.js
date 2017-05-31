define(["../init/AppService"], function(services) {
  services.factory("ToastrService", ToastrService);

  ToastrService.$inject = ["toastr", "$animate"];

  function ToastrService(toastr) {

    var toastConfig = {
      closeButton: true
    }

    var service = {
      createSuccessToast: createSuccessToast,
      createErrorToast: createErrorToast,
      createWarningToast: createWarningToast
    }

    return service;

    function createSuccessToast(params) {
      var message  = params.message;
      toastr.success(message, "Success", toastConfig);
    }

    function createErrorToast(params) {
      var message  = params.message;
      toastr.error(message, "Error", toastConfig);
    }

    function createWarningToast(params) {
      var message  = params.message;
      toastr.warning(message, "Warning", toastConfig);
    }
  }
});
