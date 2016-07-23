<template lang="pug">
.col.s12.m6
  .card.hoverable
    a(@click="listingPress(listing)")
      .card-image
        img(v-bind:src="listingImage ? listingImage : 'http://placehold.it/400x300'")
        .title-wrapper
          span.title {{listing.title}}
        .price
          span.dollars ${{listing.price}}
          span.currency CAD
    .card-content
      p.truncate {{listing.description}}
    .card-action
      a(href="#") Derp
      a(href="#") Derp2
</template>

<script>
export default {
  props: ['listing'],
  data () {
    return {
      msg: 'Main',
      listingImage: ''
    }
  },

  methods: {
    listingPress (ctx) {
      console.log(ctx)
    }
  },

  ready () {
    this.listingImage = `https://source.unsplash.com/random/500x${parseInt(Math.random() * (450 - 300) + 300)}/?rooms`
  }
}
</script>

<style lang="stylus" scoped>
.card
  background-color white
  font-color black !important
  .title-wrapper
    position absolute
    bottom 1em
    max-width 100%
    text-overflow ellipsis
    overflow hidden !important
    white-space nowrap
    left 10px
    right 10px
    color white !important
    .title
      color white !important
      font-size 20px
      font-weight 400
      line-height 20px !important
      text-shadow 1px 1px black
  .card-image
    img
      min-height 300px !important
      max-height 300px !important
    .price
      position absolute
      top 60%
      width 110px
      height 50px
      background-color rgba(0,0,0,0.6)
      border-top-right-radius 1em
      border-bottom-right-radius 1em
      padding-top 6px
      span.dollars
        color white
        font-size 26px
        margin-left 10px
      span.currency
        color white
        margin-left 5px
  .card-content
    p
      padding-top 5px
</style>
