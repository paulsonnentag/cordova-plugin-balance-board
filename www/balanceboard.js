module.exports = {
  find: function(callback) {
    cordova.exec(callback.bind(null, null), callback, "BalanceBoard", "find", []);
  },
};
