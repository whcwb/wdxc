// let ajaxUrl = 'http://192.168.123.6:81';
// let ajaxUrl = 'http://127.0.0.1:81';
// let ajaxUrl = 'http://192.168.123.13:8080';//伟
// let ajaxUrl = 'http://192.168.1.140:8080';
let ajaxUrl = 'http://www.168car.net:9193'
export default {
	webViewUrl:'http://119.23.242.234:9195/',
	// webViewUrl:'http://127.0.0.1:8080/',
	// webViewUrl:'http://192.168.123.25:8082/#/',
	url: ajaxUrl,
	LOGIN: '/app/user/login',
	REGIST: '/app/user/regist',
	USERINFO: '/api/user/getUserInfo',
	ADDZD: '/app/device/bind',
	ADD_CAR: '/app/car/addCar',
	ZDLIST: '/app/device/list',
	videoPath: 'http://www.168car.net:9091/test/',
	ToTUrl: 'http://www.168car.net:9092/',
	UPLOAD: ajaxUrl + '/upload'
}
