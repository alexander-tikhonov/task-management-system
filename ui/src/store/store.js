import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from "vuex-persistedstate"

import user from './modules/user'

Vue.use(Vuex);

const vuexStorage = createPersistedState({
    key: 'TaskManagementSystem',
    paths: [
        'user.token',
        'user.user'
    ],
    fetchBeforeUse: true
})

export const store = new Vuex.Store({
    modules: {
        user 
    },
    plugins: [vuexStorage]
});