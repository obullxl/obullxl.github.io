var crypto = require('crypto');
console.log(crypto.createHash('md5').update('obullxl@gmail.com').digest('hex'));
console.log("888888: " + crypto.createHash('md5').update('888888').digest('hex'));
console.log("\r\n");

/**
 * npm install crypto-js
 */
var md5 = require("crypto-js/md5");
console.log(md5('obullxl@gmail.com') + "");

