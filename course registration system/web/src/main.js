import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'
import "@/assets/css/global.css"
import 'dayjs/locale/zh-cn'
import locale from 'element-plus/lib/locale/lang/zh-cn'

router.beforeEach((to, from) => {

    /* 路由发生变化修改页面title */
    if (to.meta.title) {
        document.title = to.meta.title
    }
})

createApp(App).use(store).use(router).use(ElementPlus,{locale,size:"mini"}).mount('#app')
