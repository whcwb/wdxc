export default{
  set(key,val){
    let value = JSON.stringify(val)
    localStorage.setItem(key,value)
  },
  get(key){
    let localData = localStorage.getItem(key)
    return JSON.parse(localData)
  }
}
