import axios from 'axios'

// axios.interceptors.request.use(config => {
//   console.log(config)
//   // 必须返回请求，否则后端无法获得
//   return config
// }, err => {
//   console.log(err)
// })

export function request(config) {
    return new Promise(((resolve, reject) => {

        const instance = axios.create({
            timeout: 3000,
        });

        instance(config).then(res => {
            resolve(res);
        }).catch(err => {
            reject(err);
        })
    }))
}
