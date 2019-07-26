
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ARCITPlugin';

var ARCITPlugin = {
  bienv: function (name, successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "bienvenida", [name]);
  }
};

module.exports = ARCITPlugin;
