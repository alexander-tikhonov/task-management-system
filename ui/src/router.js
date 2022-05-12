import Vue from 'vue'
import Router from 'vue-router'
import AppTasksAssignee from '@/components/AppTasksAssignee'
import AppTasksCreated from '@/components/AppTasksCreated'
import AppTaskCreate from "@/components/AppTaskCreate";
import AppTaskDetail from "@/components/AppTaskDetail";
import AppLogin from '@/components/AppLogin'
import {store} from '@/store/store'
import {AXIOS} from '@/http-common'

Vue.use(Router)

const routes = [
    {
        path: '/tasks/assignee',
        name: 'TasksAssignee',
        component: AppTasksAssignee,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/tasks/created',
        name: 'TasksCreated',
        component: AppTasksCreated,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/tasks/detail/:id',
        name: 'TaskDetail',
        component: AppTaskDetail,
        props: true,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/tasks/create',
        name: 'TaskCreate',
        component: AppTaskCreate,
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