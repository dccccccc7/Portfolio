import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter);

const manageFiles = require.context('.', true, /\.js$/);
let configRouters = []
manageFiles.keys().forEach(key => {
    if (key === './index.js') return //    如果是当前文件，则跳过
    configRouters = configRouters.concat(manageFiles(key)) // 读取出文件中的default模块
});
const routes = [
    {
        path: '/',
        name: '',
        // 如果没有后端，注释以下代码 ↓↓↓↓↓↓↓↓
        // redirect: '/login'
        //如果没有后端，注释以上代码 ↑↑↑↑↑↑↑↑↑
        redirect: '/main'
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login'),
        meta: {
            title: '欢迎登录车间管理系统'
        },
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('@/views/Register'),
        meta: {
            title: '欢迎注册车间管理系统'
        },
    },
    {
        path: '/main',
        name: 'main',
        component: () => import('@/views/Main'),
        children: [
            ...configRouters,
        ]
    },
];

const router = new VueRouter({
    base: process.env.BASE_URL,
    routes
});
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

//给浏览器标签添加标题
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title.split("-")[0];
    } else {
        document.title = "未设置标题"
    }

    //如果没有后端，注释以下代码 ↓↓↓↓↓↓↓↓

    let login = window.sessionStorage.getItem("userinfo");
    if (login) {
        if (!store.getters.login) {
            store.commit("login/SET_USERINFO", JSON.parse(window.sessionStorage.getItem("userinfo")));
        }
    }
    console.log(to.name);
    if (to.name !== 'login') {
        if (to.name === 'register') {
            next();
        } else {
            if (login) {
                if (to.name === 'main') {
                    next({name: 'testComponent1'});
                }
                next();
            } else next({name: 'login'});
        }
    } else {
        next();
    }

    //如果没有后端，注释以上代码 ↑↑↑↑↑↑↑↑↑


    next();
});


export default router
