import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import settings from './modules/settings'
import login from './modules/login'

Vue.use(Vuex)

const store = new Vuex.Store({
    modules: {
        settings,
        login,
    },
    getters
})

export default store
