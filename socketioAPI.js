"use strict";
var FCM = require('fcm-push');
var express = require('express');
var app = express();
var http = require('http').createServer(app).listen(process.env.PORT || 3000);
var io = require('socket.io').listen(http);

app.get('/',(req,res)=>{
    console.log('test');
    res.send('push forum notifications');
});

var serverKey = 'AAAA4ErItao:APA91bHYKmm5PvYVZCqrTrKPdkJlsz_PMbDlDuQCSYDv_EJ-kSEiFstYYkRo31OESVdWxmijZ0j4RZEp2VlD8eopQuIyMJBlRdAiTyFZmya1auDBe1ZzCzt2pVz6FCBVgsIcNwuIC1Hc';

  //  io.on('connection',(socket)=>{
  //     console.log('connected');
  //
  // socket.on('push',(id,dat)=>{
var fcm = new FCM(serverKey);

var message = {
    to: '/topics/news',
    collapse_key: 'new_messages',
    data: {
        your_custom_data_key: 'test'
    },
    notification: {
        body: 'test',
    }
};



//promise style
fcm.send(message)
    .then(function(response){
        console.log("Successfully sent with response: ", response);
    })
    .catch(function(err){
        console.log("Something has gone wrong!");
        console.error(err);
 });

// });
