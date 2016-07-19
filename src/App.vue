<template lang="pug">
  mixin menuItems
    li: a(href="#"): b Login
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
    .section
    .row.intro
      .col.m8.s12.media
        h4.label.left-align Toronto 
        img.responsive-img.z-depth-2(src="https://c2.staticflickr.com/8/7386/8727883772_13c295e24d_b.jpg")
      .col.m4.s12.description
        p.left-align Toronto, Ontario's capital city, has a vibrant history of change and growth, ranging from its early occupation over 1,000 years ago to its current status as North America’s fourth largest city. Toronto is Canada's largest municipality and is made up of the former cities of Toronto, North York, Scarborough, York and Etobicoke, and the former borough of East York. The city is home to a large immigrant population, and is a national and international hub for finance, communications and cultural life.
    hello

</template>

<script>
import Hello from './components/Hello'

export default {
  components: {
    Hello,
    'side-nav': require('vue-materialize/side-nav')
  },

  data () {
    return {
      opened: true
    }
  },

  methods: {
    toggle () {
      this.opened = !this.opened
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

.intro
  .media
    .label
      font-weight 500
    img
      height 400px
  .description
    margin-top 3.5em

.intro-bar
  height 150px
  background-color black
  img
    width 100%
    height 150px

</style>

<style lang="stylus" scoped>
.brand-logo
  margin-left .5em
</style>