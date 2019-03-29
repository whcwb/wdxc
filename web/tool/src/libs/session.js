export default{
		setItem(key,val){
			let value = JSON.stringify(val)
			sessionStorage.setItem(key,value)
		},
		getItem(key){
			let sessData = sessionStorage.getItem(key)
			return JSON.parse(sessData)
		}
}
