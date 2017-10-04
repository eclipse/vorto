define(["../init/AppService"], function(services) {
  services.factory("ShareDataService", ShareDataService);

  function ShareDataService() {
    var addEditorTypes;
    var closeEditorIndex;
    var deleteEditorTab;
    var describeEditorType;
    var unsavedFilesList;
    var publishResult;
    var deleteProjectName;
    var isEditorServiceToggled = false;

    var service = {
      getAddEditorTypes: getAddEditorTypes,
      getCloseEditorIndex: getCloseEditorIndex,
      getDeleteEditorTab: getDeleteEditorTab,
      getDescribeEditorType: getDescribeEditorType,
      getUnsavedFiles: getUnsavedFiles,
      getPublishResult: getPublishResult,
      getDeleteProjectName: getDeleteProjectName,
      getIsEditorServiceToggled: getIsEditorServiceToggled,

      setAddEditorTypes: setAddEditorTypes,
      setCloseEditorIndex: setCloseEditorIndex,
      setDeleteEditorTab: setDeleteEditorTab,
      setDescribeEditorType: setDescribeEditorType,
      setUnsavedFiles: setUnsavedFiles,
      setPublishResult: setPublishResult,
      setDeleteProjectName: setDeleteProjectName,
      setIsEditorServiceToggled: setIsEditorServiceToggled
    }

    return service;

    function getAddEditorTypes() {
      return addEditorTypes;
    }

    function getCloseEditorIndex() {
      return closeEditorIndex;
    }

    function getDeleteEditorTab() {
      return deleteEditorTab;
    }

    function getDescribeEditorType() {
      return describeEditorType
    }

    function getUnsavedFiles() {
      return unsavedFilesList;
    }

    function getPublishResult() {
      return publishResult;
    }

    function getDeleteProjectName() {
      return deleteProjectName;
    }

    function getIsEditorServiceToggled() {
      return isEditorServiceToggled;
    }

    function setAddEditorTypes(editorTypes) {
      addEditorTypes = editorTypes;
    }

    function setCloseEditorIndex(index) {
      closeEditorIndex = index;
    }

    function setDeleteEditorTab(editorTab) {
      deleteEditorTab = editorTab;
    }

    function setDescribeEditorType(editorType) {
      describeEditorType = editorType;
    }

    function setUnsavedFiles(unsavedFiles) {
      unsavedFilesList = unsavedFiles;
    }

    function setPublishResult(result) {
      publishResult = result;
    }

    function setDeleteProjectName(projectName) {
      deleteProjectName = projectName;
    }

    function setIsEditorServiceToggled(toggle) {
      isEditorServiceToggled = toggle;
    }
  }
});
