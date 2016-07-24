<template lang="pug">
  mixin menuItems
    li.search(v-show="$route.name === 'listings'")
      form(@submit.prevent='')
        .input-field
          input.searchInput(type="search" placeholder="Search" required)
          label(for="search")
              i.material-icons search
          i.material-icons close
    li: a(v-link="'/'"): b Home
    li: a(v-link="'/listings'"): b Browse
    template(v-if="!loggedIn")
      li: a(href="#loginModal" data-target="loginModal").loginModalTrigger
        b Login
      li: a(href="#signUpModal").signUpModalTrigger: b Sign Up
    template(v-if="loggedIn")
      li.account-button
        a(href="#")
          i.material-icons.left account_circle
          b {{state.user.name | titlecase }}
    li: a(href="#" @click="dev"): b DEV

  login-modal
  sign-up-modal
  .navbar-fixed
    nav.color-primary.z-depth-1
      .nav-wrapper
        a.brand-logo(v-link="'/'")
          b MyBnB

        a(href="#" data-activates="side-nav").button-collapse
          i.material-icons menu
        ul.nav-menu.right.hide-on-med-and-down
          +menuItems
        ul.side-nav#side-nav
          +menuItems

  .main
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
      Materialize.toast(`Welcome ${this.state.user.name}!`, 5000)
    }
  },

  methods: {
    dev () {
      console.log('DEV')
      console.log(this.state)
    }
  },

  ready () {
    let vm = this
    $(document).ready(() => {
      $('.button-collapse').sideNav({
        closeOnClick: true
      })
    })
  }
}

</script>

<style lang="stylus">
// @import url(https://fonts.googleapis.com/css?family=Source+Code+Pro)
html
  height: 100%
  overflow hidden
body
  padding-bottom 64px
  height: 100%
.main
  // border 1px solid black
  // padding 1em 1em
  height 100%
  position relative
  font-family 'Source Code Pro',;

.no-padding
  padding 0 !important
.no-margin
  margin 0 !important

.color-primary
  background-color #01579b !important
.color-primary-text
  color #01579b !important
  
.nav-menu
  .search
    form .input-field
      position absolute
      left 9.5em
      width 500px
      height 64px !important
      padding-top 10px
      padding-bottom 10px
.side-nav
  .search
    form .input-field
      height 64px !important
      padding-top 10px
      padding-bottom 10px
    i
      color black !important

</style>

<style lang="stylus" scoped>
.brand-logo
  margin-left .5em
.account-button
  .btn-flat
    color white !important
</style>