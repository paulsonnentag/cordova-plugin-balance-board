"use strict";

var noop = function () {}


function getEvent (success, error) {
  cordova.exec(success, error, "BalanceBoard", "getEvent", [])
}



function eventLoop () {
  getEvent(function (event) {

    if (event !== null) {
      alert(event.type);
    }

    setTimeout(eventLoop, 1000);

  }, function () {

    setTimeout(eventLoop, 1000);

  });
}

document.addEventListener("deviceready", eventLoop, false);

module.exports = {
  connect: function() {
    cordova.exec(noop, function (err) {
      alert("err connect");
    }, "BalanceBoard", "connect", []);
  },
};
