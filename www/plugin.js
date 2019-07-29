/*
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ARCITPlugin';

var ARCITPlugin = {
  bienv: function (name, successCallback, errorCallback){
    console.log("A punto de ejecutar codigo nativo....");
    exec(successCallback, errorCallback, PLUGIN_NAME, "bienvenida", [name]);
  }
};

module.exports = ARCITPlugin;
*/
// Empty constructor
function ARCITPlugin() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
ARCITPlugin.prototype.bienv = function(message, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  cordova.exec(successCallback, errorCallback, 'ARCITPlugin', 'bienvenida', [options]);
}

ARCITPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.arcitPlugin = new ARCITPlugin();
  return window.plugins.arcitPlugin;
};
cordova.addConstructor(ARCITPlugin.install);