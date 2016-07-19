import Vue from 'vue'
import App from './App'
import Router from 'vue-router'
import { configRouter } from './router-config'
import 'vue-materialize/node_modules/materialize-css/dist/css/materialize.min.css'

Vue.use(require('vue-touch'))
Vue.use(Router)

const router = new Router({history: true})

configRouter(router)

router.start(App, 'app')
