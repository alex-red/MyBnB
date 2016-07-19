<template lang="pug">
  mixin menuItems
    li: a(href="#")
      b Login
      modal
        .modal-content
          h4 Login
        .modal-footer
          button.btn(@click="closeModal") close

    li: a(href="#"): b Sign Up

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

export default {
  components: {
    Hello,
    'side-nav': require('vue-materialize/side-nav'),
    'modal': require('vue-materialize/modal')
  },

  data () {
    return {
      opened: false
    }
  },

  methods: {
    toggle () {
      this.opened = !this.opened
    },
    closeModal () {
      this.$broadcast('close')
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
  
.main
  // border 1px solid black
  padding 1em 1em
  font-family 'Source Code Pro',;

.no-padding
  padding 0 !important
.no-margin
  margin 0 !important

.color-primary
  color #01579b !important
  background-color #01579b !important


</style>

<style lang="stylus" scoped>
.brand-logo
  margin-left .5em
</style>