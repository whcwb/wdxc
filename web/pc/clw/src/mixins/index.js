export default{
	created(){
//		alert("公用方法")
		this.rowClassName
	},
	methods:{
		getdate(){
			var getDate = new Date
			return getDate
		},
		getdateStr(){
			var NowDate = new Date
			let Year = NowDate.getFullYear()
			let Month = NowDate.getMonth()+1
			let Day = NowDate.getDate()
			let Hours = NowDate.getHours()
			let Minutes = NowDate.getMinutes()
			let Seconds = NowDate.getSeconds()
			let time = Year+'-'+Month+'-'+Day+' '+Hours+':'+Minutes+':'+Seconds
			return time
		},
		getdateStrD(){
			var NowDate = new Date
			let Year = NowDate.getFullYear()
			let Month = NowDate.getMonth()+1
			let Day = NowDate.getDate()
			let time = Year+'-'+Month+'-'+Day
			return time
		},
		getdatePara(val){//时间转换
			var newDate = new Date();
			newDate.setTime(val)
            log(typeof newDate);
			let Year = val.getFullYear();
			let Month = val.getMonth()+1;
			let Day = val.getDate();
			let Hours = val.getHours();
			let Minutes = val.getMinutes();
			let Seconds = val.getSeconds();
			if(Month<10){
                Month = '0'+Month
			}
			if(Day<10){
                Day = '0'+Day
			}
			let time = Year+'-'+Month+'-'+Day+' '+Hours+':'+Minutes+':'+Seconds
			return time
		},
		getdateParaD(val){//时间转换
			if(val==null||val==""){
				return ''
			}
			var newDate = new Date();
			newDate.setTime(val)
            log(typeof newDate);
            let Year = val.getFullYear()
			let Month = val.getMonth()+1
			let Day = val.getDate()
			if(Month<10){
                Month = '0'+Month
			}
			if(Day<10){
                Day = '0'+Day
			}
			let time = Year+'-'+Month+'-'+Day
			return time
		},
		getDJC(val){
			var newDate = new Date();
			newDate.setTime(val);
            log(typeof newDate);
			return this.getdatePara(newDate);
		},
    	rowClassName (row, index) {
            // if (index%2 === 0) {
            //     return 'demo-table-info-row';
            // }
            return '';
        },
        //根据浏览器高度，设置document高度
        getWindowHeight(){
        	var windowHeight = window.innerHeight
        	return windowHeight
        }
	}
}
