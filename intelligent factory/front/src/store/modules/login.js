const state = {
  userinfo: {},
}
import router from "@/router";
const mutations = {
  SET_USERINFO: (state, userinfo) => {
    if (userinfo) {
      state.userinfo = userinfo;
    }
  },
  LOG_OUT: (state) => {
    state.userinfo = {};
    window.sessionStorage.removeItem("userinfo");
    router.replace('/login');
  }
}

const actions = {
  // changeSetting({ commit }, data) {
  //   commit('CHANGE_SETTING', data)
  // }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

