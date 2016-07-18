import Vue from 'vue'
import App from './App'
import 'vue-materialize/node_modules/materialize-css/dist/css/materialize.min.css'
Vue.use(require('vue-touch'))

/* eslint-disable no-new */
new Vue({
  el: 'body',
  components: { App }
})
