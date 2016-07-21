<template lang="pug">
  mixin menuItems
    li: a(href="#loginModal" data-target="loginModal").loginModalTrigger
      b Login
    li: a(href="#signUpModal").signUpModalTrigger: b Sign Up
    li: a(href="#" @click="dev"): b DEV

  login-modal
  sign-up-modal
  nav.color-primary.z-depth-1
    .nav-wrapper
      a.brand-logo
        b MyBnB
      a(href="#" data-activates="side-nav").button-collapse
        i.material-icons menu
      ul.right.hide-on-med-and-down
        +menuItems
      ul.side-nav#side-nav
        +menuItems

  .intro-bar
    img(src="./assets/intro_bar2.jpg")
  .main.container.center
    router-view

</template>

<script>
import Hello from './components/Hello'
import LoginModal from './components/LoginModal'
import SignUpModal from './components/SignUpModal'

export default {
  components: {
    Hello,
    LoginModal,
    SignUpModal
  },

  data () {
    return {
      state: Store.state,
      opened: false,
      loggedIn: false
    }
  },

  events: {
    loggedIn () {
      console.log('User has logged in.')
      this.loggedIn = true
    }
  },

  methods: {
    dev () {
      console.log('DEV')
      console.log(this.state)
    }
  },

  ready () {
    $(document).ready(() => {
      $('.button-collapse').sideNav()
    })
  }
}

</script>

<style lang="stylus">
// @import url(https://fonts.googleapis.com/css?family=Source+Code+Pro)
html
  height: 100%
  
body
  height: 100%
  
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
.intro-bar
  img
    width 100%
    height 150px
</style>