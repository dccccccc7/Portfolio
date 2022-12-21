import {request} from "@/network/request";
import store from "@/store";

export function my_request(vue, config) {
  if (config.data instanceof Object) {
    let d = store.getters.userinfo;
    delete d.accessList;
    console.log(d);
    config.data.user = store.getters.userinfo;
  }
  config.timeout = config.timeout ? config.timeout : 2500;
  // config.baseURL = 'dev-api/';
  config.baseURL = '/api/';
  return new Promise(((resolve, reject) => {
    request(config)
      .then(res => {
        let code = res.data.code;
        if (code === 200) {
          resolve(res);
        } else if (code === -1) {
          let message = res.data.message;
          vue.$message.error(message);
          // reject(code);
        } else if (code === 100) {
          reject(res);
        }
      })
      .catch(err => {
        vue.$message.error("服务器开小差了!")
      });
  }));
}

export function time_formatter(time) {
  if (!time) {
    return ""
  }
  let date = new Date(time);
  return date.format("yyyy-MM-dd");
}






Date.prototype.format = function(fmt)
{ //author: meizz
  
  var o = {
    "M+" : this.getMonth()+1,                 //月份
    "d+" : this.getDate(),                    //日
    "h+" : this.getHours(),                   //小时
    "m+" : this.getMinutes(),                 //分
    "s+" : this.getSeconds(),                 //秒
    "q+" : Math.floor((this.getMonth()+3)/3), //季度
    "S"  : this.getMilliseconds()             //毫秒
  };
  if(/(y+)/.test(fmt))
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)
    if(new RegExp("("+ k +")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
  return fmt;
}
