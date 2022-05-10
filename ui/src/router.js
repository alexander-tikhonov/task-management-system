import Vue from 'vue'
import Router from 'vue-router'
import AppTasks from '@/components/AppTasks'

Vue.use(Router)

export default new Router({
   mode: 'history',
   routes: [
    //  {
    //    path: '/',
    //    name: 'Greeting',
    //    component: Greeting
    //  },
    {
        path: '/tasks',
        name: 'Tasks',
        component: AppTasks
    },
   ]
})