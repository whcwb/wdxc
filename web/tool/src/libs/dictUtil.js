
import session from './session.js'
let dictUtil = {
    getValByCode(v,zdlmCode,zdxmCode){
		let dic	= session.getItem('dictMap')
		let zdlm = new Map(dic).get(zdlmCode)
        if (!zdlm)return '';
        for (let r of zdlm){
            if (r.key === zdxmCode){
                return r.val;
            }
        }
        return '';
    },
    getByCode(v,code){
        let dic	= session.getItem('dictMap')
        let val = new Map(dic).get(code)
        if (val){
            return val;
        }else{
            log('字典加载失败，重新登陆',code);
            console.log('------------------字典加载失败,请从新登录初始化字典-----------------------');
            v.$router.push({ name: 'login' });
        }
    },
    reload(v,callback){
        v.$http.get(v.apis.DICTIONARY.QUERY,{params:{pageSize:10000}}).then((res) =>{
            if(res.code===200){
                let dictMap = new Map();
                for (let r of res.page.list){
                    let a = [];
                    if (!r.zdxmList)continue
                    for (let e of r.zdxmList){
                        a.push({key:e.zddm,val:e.zdmc});
                    }
                    dictMap.set(r.lmdm,a)
                }
                v.session.setItem('dictMap',dictMap)
                if (typeof callback === 'function'){
                    callback();
                }
            }
        }).catch((error) =>{
            log(error)
        })
    },
}
export default dictUtil;
