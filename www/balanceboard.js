"use strict";

var noop = function () {}

function getEvents (callback) {
  cordova.exec(callback, noop, "BalanceBoard", "getEvents", [])
}


function eventLoop () {
  getEvents(function (events) {


    events.forEach(function (event) {

      alert(event.type);

    });

    setTimeout(eventLoop, 10);
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
