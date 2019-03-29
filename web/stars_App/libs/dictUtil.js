import store from '../store'
let dictMapMess = store.state.dictMap
let dictUtil = {
	text(){
		console.log('*******',store.state.dictMap)
	},
	getValByCode(v, zdlmCode, zdxmCode) {
		try {
			let dictList = store.state.dictMap
			let dictMap = new Map();
			for (let r of dictList) {
				let a = [];
				if (!r.zdxmList) continue
				for (let e of r.zdxmList) {
					a.push({
						key: e.zddm,
						val: e.zdmc
					});
				}
				dictMap.set(r.lmdm, a)
			}
			let zdlm = dictMap.get(zdlmCode)
			if (!zdlm) return '';
			for (let r of zdlm) {
				if (r.key === zdxmCode) {
					return r.val;
				}
			}
		} catch (e) {

		}

		return '';
	},
	getItemByCode(v, zdlmCode, zdxmCode) {
		try {
			let dictList = store.state.dictMap
			let dictMap = new Map();
			for (let r of dictList) {
				let a = [];
				if (!r.zdxmList) continue
				for (let e of r.zdxmList) {
					a.push({
						key: e.zddm,
						val: e.zdmc
					});
				}
				dictMap.set(r.lmdm, a)
			}
			let zdlm = dictMap.get(zdlmCode)
			if (!zdlm) return '';
			for (let r of zdlm) {
				if (r.key === zdxmCode) {
					return r;
				}
			}
		} catch (e) {

		}

		return '';
	},
	getByCode(v, code) {
		try {
			let dictList = store.state.dictMap
			let dictMap = new Map();
			for (let r of dictList) {
				let a = [];
				if (!r.zdxmList) continue
				for (let e of r.zdxmList) {
					a.push({
						key: e.zddm,
						val: e.zdmc
					});
				}
				dictMap.set(r.lmdm, a)
			}
			let val = dictMap.get(code)
			if (val) {
				return val;
			} else {
				// v.$router.push({ name: 'login' });
				console.log('字典丢失');
			}
		} catch (e) {

		}
	},
	reload(v, callback) {
		v.$http.get(v.apis.DICTIONARY.QUERY, {
			params: {
				pageSize: 10000,
				timers: new Date().getTime()
			}
		}).then((res) => {
			if (res.code === 200) {
				let dictMap = new Map();
				for (let r of res.page.list) {
					let a = [];
					if (!r.zdxmList) continue
					for (let e of r.zdxmList) {
						a.push({
							key: e.zddm,
							val: e.zdmc
						});
					}
					dictMap.set(r.lmdm, a)
				}
				store.commit('dictMap',dictMap)
				if (typeof callback === 'function') {
					callback();
				}
			}
		}).catch((error) => {
			console.log(error)
		})
	}
}
export default dictUtil;
