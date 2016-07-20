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

window.Store = Store

router.start(App, 'app')
