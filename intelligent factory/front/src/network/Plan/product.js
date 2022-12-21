import axios from 'axios'


export default function getProduct(config) {
    const instance = axios.create({
        // timeout:5000,
        // baseURL:'http://localhost:8100'
    })

    return instance(config)
}