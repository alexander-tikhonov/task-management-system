import Vue from 'vue'
import Router from 'vue-router'
import AppTasks from '@/components/AppTasks'
import AppLogin from '@/components/AppLogin'

import {store} from '@/store/store'
import {AXIOS} from '@/http-common'

Vue.use(Router)

const routes = [
    {
        path: '/tasks',
        name: 'Tasks',
        component: AppTasks,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/login',
        name: 'Login',
        component: AppLogin
    },
]

export const router = new Router({
    routes,
    mode: 'history'
});

router.beforeEach(async (to, from, next) => {
    let userState = store.state.user,
        userIsLoggedIn = store.getters['user/loggedIn']

    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!userIsLoggedIn) {
            next({
                path: '/login',
                query: {redirect: to.fullPath}
            })
            return false
        }
    }

    if (userIsLoggedIn) {
        AXIOS.defaults.headers.common['Authorization'] = "Bearer " + userState.token
    } else {
        AXIOS.defaults.headers.common['Authorization'] = ""
        delete AXIOS.defaults.headers.common['Authorization']
    }

    next()
})