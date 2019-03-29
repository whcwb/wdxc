let lib ={}
lib.getUserInfo = (v)=>{
	console.log(v.$http)
	v.$http.post(v.apis.USERINFO).then(res=>{
		console.log('数据',res)
	}).catch(err=>{})
}
export default lib