
var exec = require('cordova/exec');

var PLUGIN_NAME = 'ARCITPlugin';

var ARCITPlugin = {
  bienv: function (name, successCallback, errorCallback){
    console.log("A punto de ejecutar codigo nativo....");
    exec(successCallback, errorCallback, PLUGIN_NAME, "bienvenida", [name]);
  }
};

module.exports = ARCITPlugin;
