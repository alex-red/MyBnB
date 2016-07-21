import Vue from 'vue'
import App from './App'
import Router from 'vue-router'
import Resource from 'vue-resource'
import { Store } from './utils/store'
import { configRouter } from './router-config'
// import 'vue-materialize/node_modules/materialize-css/dist/css/materialize.min.css'

Vue.use(require('vue-touch'))
Vue.use(Router)
Vue.use(Resource)
Vue.http.options.xhr = {withCredentials: true}

const router = new Router({history: true})

configRouter(router)

Vue.filter('titlecase', (val) => {
  return val.replace(/\w\S*/g, (txt) => {
    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase()
  })
})

window.Vue = Vue
window.Store = Store
router.start(App, 'app')
