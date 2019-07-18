require.config({
    paths: {
        "jquery": "../../../../webjars/jquery/jquery.min.js",
        "ace/ext/language_tools": "../../../../webjars/ace/1.2.0/src/ext-language_tools",
        "webjars/ace/1.2.0/src/ace" : "../../../../webjars/ace/1.2.0/src/ace",
        "xtext/xtext-ace": "../../../../xtext/2.15.0/xtext-ace"
    }
});

require(["webjars/ace/1.2.0/src/ace"], function() {
    require(["xtext/xtext-ace"], function(xtext) {
        xtext.createEditor();
    });
});