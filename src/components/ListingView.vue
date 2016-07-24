<template lang="pug">
#listingViewComponent
  #introBar
    img(src="../assets/listing-view-intro-placeholder.png" v-show="!introReady")
    img.unsplash(v-bind:src="'https://source.unsplash.com/random/1980x' + parseInt(Math.random() * (600 - 550) + 550) + '/?rooms'" v-show="introReady")
  .container
  .info
    .container
      h3 {{listing.title | titlecase}}
      .location-bar
        b {{address.address | titlecase}}, 
        b {{address.city | titlecase}}, 
        b {{address.country | titlecase}}
  .details
    .container
      h4 About Listing
      p {{listing.description}}
      .amenities-bar
        b Includes the following features: 
        p {{listing.amenities}}

</template>

<script>
import Server from '../utils/network'

export default {
  data () {
    return {
      server: new Server(this),
      listing: {},
      address: {},
      introBarSrc: null,
      introReady: false
    }
  },
  ready () {
    let vm = this
    let id = vm.$route.params.listing_id
    vm.server.getListingById(id).then((res) => {
      vm.listing = res
    }).then(() => {
      vm.server.getAddressByListingId(vm.listing.address_id).then((res) => {
        vm.address = res
      })
    })

    $(document).ready(() => {
      $('img.unsplash').on('load', () => {
        vm.introReady = true
      })
    })
  }
}
</script>

<style lang="stylus" scoped>
#listingViewComponent
  overflow auto !important
  height 100%
#introBar
  img
    width 100%
    max-height 400px
    
.details
  background-color #F5F5F5
  height 100%
  margin-top 1em
  padding-top .25em
</style>
