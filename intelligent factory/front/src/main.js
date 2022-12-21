import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/css/global.css'
import axios from 'axios'
import * as echarts from "echarts"

import VueScrollLock from 'vue-scroll-lock'
import htmlToPdf from '@/utils/htmlToPdf'

Vue.use(htmlToPdf)
Vue.use(VueScrollLock)

Vue.prototype.$http = axios;
Vue.prototype.$echarts = echarts

Vue.use(ElementUI);
Vue.config.productionTip = false;
axios.defaults.baseURL = 'http://localhost:8019';

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
