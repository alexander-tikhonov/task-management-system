import {AXIOS} from '@/http-common'
import {router} from '@/router'

export default {
    namespaced: true,
    state: {
        user: {},
        token: null,
        authorities: [],
        loginError: {}
    },
    getters: {
        loggedIn(state) {
            return state.token !== null
        },
        getUser(state) {
            return state.user;
        }
    },
    mutations: {
        setToken(state, payload) {
            state.token = payload
        },
        setUser(state, payload) {
            state.user = payload;
        },
        setAuthorities(state, payload) {
            state.authorities = payload;
        },
        setLoginError(state, payload) {
            if(!payload)
                payload = {};
            state.loginError = payload;
        }
    },
    actions: {
        login({commit}, payload) {
            let request = AXIOS.post('/signin', {
                username: payload.login,
                password: payload.password
            });

            request.then(response => {
                commit('setToken', response.data.token)
                commit('setUser', response.data.user)
                commit('setAuthorities', response.data.authorities)

                router.push({name: 'TasksAssignee'})
            }).catch(error => {
                console.log('При аутентификации в приложении возникла ошибка', error)
                commit('setLoginError', {message: "Неверно указаны логин или пароль"})
            });
        },
        logout({commit}) {
            commit('setLoginError')
            commit('setUser', {})
            commit('setAuthorities', [])
            commit('setToken', null)

            router.push({name: 'Login'});
        },
    }
}