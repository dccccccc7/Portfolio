import ca from "element-ui/src/locale/lang/ca";
import axios from "axios";

export function my_request_post(config) {

  return new Promise(((resolve, reject) => {

    const instance = axios.create({
      timeout: 1000,
      // headers: {'X-Custom-Header': 'foobar'}
    });

    instance(config).then(res => {
      console.log('成功')
      resolve(res);
    }).catch(err => {
      console.log('失败')
      reject(err);
    })
  }))
}
