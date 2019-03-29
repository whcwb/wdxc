
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function GetQueryStringWithEncode(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    let p = decodeURI(window.location.search)
    var r = p.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}