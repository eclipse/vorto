ace.define("ace/mode/vorto", function(require, exports, module) {

var oop = require("ace/lib/oop");
var TextMode = require("ace/mode/text").Mode;
var ExampleHighlightRules = require("ace/mode/example_highlight_rules").ExampleHighlightRules;

var Mode = function() {
    this.HighlightRules = ExampleHighlightRules;
};
oop.inherits(Mode, TextMode);

(function() {
    
}).call(Mode.prototype);

exports.Mode = Mode;
});

ace.define("ace/mode/example_highlight_rules", function(require, exports, module) {

var oop = require("ace/lib/oop");
var TextHighlightRules = require("ace/mode/text_highlight_rules").TextHighlightRules;

var ExampleHighlightRules = function() {

    var keywords = "as|functionblocks|infomodel|dictionary|MAX|MIMETYPE|MIN|REGEX|SCALING|STRLEN|as|base64Binary|boolean|byte|category|configuration|dateTime|description|displayname|double|entity|enum|eventable|events|extends|false|fault|float|functionblock|int|long|mandatory|measurementUnit|multiple|namespace|operations|optional|readable|returns|short|status|string|true|using|version|with|writable|targetplatform|extension";
		this.$rules = {
			"start": [
				{token: "comment", regex: "\\/\\/.*$"},
				{token: "comment", regex: "\\/\\*", next : "comment"},
				{token: "string", regex: '["](?:(?:\\\\.)|(?:[^"\\\\]))*?["]'},
				{token: "string", regex: "['](?:(?:\\\\.)|(?:[^'\\\\]))*?[']"},
				{token: "constant.numeric", regex: "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"},
				{token: "lparen", regex: "[\\[({]"},
				{token: "rparen", regex: "[\\])}]"},
				{token: "keyword", regex: "\\b(?:" + keywords + ")\\b"}
			],
			"comment": [
				{token: "comment", regex: ".*?\\*\\/", next : "start"},
				{token: "comment", regex: ".+"}
			]
		};
    
};

oop.inherits(ExampleHighlightRules, TextHighlightRules);

exports.ExampleHighlightRules = ExampleHighlightRules;
});