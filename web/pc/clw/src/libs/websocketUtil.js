import api from '../axios/api'
import Stomp from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let sockJS = new SockJS(api.url + "/gps");
let stompClient = null;
let connecting = false;
let connected = false;
let connectedCallbacks = [];
let reconnectCount = 0;
let maxReconnectCount = 10;

let util = {
    init :()=>{
        console.log('init');
        if (stompClient == null){
            try{
                sockJS.onopen = function () {console.log('onopen');};
                sockJS.onclose = function () {
                    if (reconnectCount >= maxReconnectCount){
                        return;
                    }
                    reconnectCount++
                    util.connect()
                    console.log('onclose');
                };
                sockJS.onmessage = function (e) {
                    if (reconnectCount >= maxReconnectCount){
                        return;
                    }
                    reconnectCount++
                    util.connect()
                    console.log('onmessage',e);};
                stompClient = Stomp.over(sockJS);
            }catch (e) {
                console.error(e);
            }
        }
    },
    onConnected : (callback) =>{
        console.log('onConnected');
        if (connected){
            callback();
            return;
        }
        console.log('not connect');
        connectedCallbacks.push(callback)
    },
    connect : (callback)=>{
        if (connecting)return;
        connecting = true;
        console.log('connect');

        stompClient.connect({}, function (frame) {
            console.log("connected");
            connecting = false;
            connected = true;
            if (typeof callback == 'function'){
                callback();
            }
            for (let r of connectedCallbacks){
                r()
            }
        });
        setTimeout(()=>{
            connecting = false;
        },10000);
    },
    isConnected : ()=>{
        connected = stompClient != null && stompClient.connected;
        return connected;
    },
    disConnect : ()=>{
        console.log('disConnect');
        return stompClient.disConnect();
    },
    subscribe : (url,callback)=>{
        return stompClient.subscribe(url, callback);
    },
    info : ()=>{
        console.log('stompClient',stompClient);
    }
}
util.init();
export default util;
