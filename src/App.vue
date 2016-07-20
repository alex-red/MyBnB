<template lang="pug">
  mixin menuItems
    li: a(href="#loginModal" data-target="loginModal").modal-trigger
      b Login
    li: a(href="#"): b Sign Up
    li: a(href="#" @click="dev"): b DEV

  login-modal
  nav.color-primary.z-depth-1
    .nav-wrapper
      a.brand-logo
        b MyBnB
      a(href="#" @click="toggle").button-collapse
        i.material-icons menu
      ul.right.hide-on-med-and-down
        +menuItems

  side-nav( :is-opened.sync="opened" v-ref:side-nav )
    +menuItems

  .intro-bar
    img(src="./assets/intro_bar2.jpg")
  .main.container.center
    router-view

</template>

<script>
import Hello from './components/Hello'
import LoginModal from './components/LoginModal'

export default {
  components: {
    Hello,
    LoginModal
  },

  data () {
    return {
      state: Store.state,
      opened: false
    }
  },

  methods: {
    dev () {
      console.log('DEV')
      console.log(this.state)
    }
  },

  ready () {

  }
}

</script>

<style lang="stylus">
// @import url(https://fonts.googleapis.com/css?family=Source+Code+Pro)
html
  height: 100%
  
body
  height: 100%
.modal
  overflow hidden !important
  
.main
  // border 1px solid black
  padding 1em 1em
  font-family 'Source Code Pro',;

.no-padding
  padding 0 !important
.no-margin
  margin 0 !important

.color-primary
  background-color #01579b !important
.color-primary-text
  color #01579b !important
</style>

<style lang="stylus" scoped>
.brand-logo
  margin-left .5em
</style>