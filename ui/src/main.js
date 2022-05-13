import Vue from 'vue'
import App from './App.vue'
import {router} from './router'
import { store } from './store/store'

import vSelect from 'vue-select'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import { VueEditor } from "vue2-editor";

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'vue-select/dist/vue-select.css';

Vue.config.productionTip = false

Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(VueEditor)
Vue.component('v-select', vSelect)

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
