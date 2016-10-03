<template lang="pug">
#listingViewComponent
  comment-modal(v-bind:context.sync="listing")
  #introBar
    img(src="../assets/listing-view-intro-placeholder.png" v-show="!introReady")
    img.unsplash(v-bind:src="'https://source.unsplash.com/random/1980x' + parseInt(Math.random() * (600 - 550) + 550) + '/?rooms'" v-show="introReady")
  .container
  .info.row
    .intro.container.s12.left-align
      h4.truncate {{listing.title | titlecase}}
      .location-bar
        b {{address.address | titlecase}}, 
        b {{address.city | titlecase}}, 
        b {{address.country | titlecase}}
    .checkin.z-depth-1.s12
      .row.price-header
        b ${{listing.price}} 
        b CAD 
        span per night
      .content
        form.bookingInputs
          .row.options
            .col.s12.m2.center(style="margin-top: 2em;")
              b#datesLabel.input-label Dates
            .input-field.col.s4.m3
              input(id="filter_checkin" type="date" v-model="inputs.filter_checkin").datepicker
              label(for="filter_checkin") Check In
            .input-field.col.s4.m3
              input(id="filter_checkout" type="date" v-model="inputs.filter_checkout").datepicker
              label(for="filter_checkout") Check Out
            .input-field.col.s4.m3
              select(v-model="inputs.filter_guests")
                option(selected value="1") 1 Guest
                option(v-for="number in (parseInt(listing.rooms) - 1)" value="{{number + 2}}") {{number + 2}} Guests
              label Guests
          .row.center
            button.btn(@click.prevent="requestBooking(listing.listing_id)").color-primary Request Booking
  .details
    .container
      h4 About Listing
      p {{listing.description}}
      .amenities-bar
        b Includes the following features: 
        .amenities
          .chip.green.white-text(v-for="amenity in listing.amenities" track-by="$index") {{amenity}}
      h4 Comments & Reviews
      .comments.row(v-if="listing.comments && listing.comments.length > 0")
        template(v-for="id of listing.comments" track-by="$index")
          comment(v-bind:id="id")
      p(v-if="listing.comments && listing.comments.length === 0") Newly listed.
      .col.s12
        a.btn(href="#commentModal" data-target="commentModal").commentModalTrigger Leave Comment
</template>

<script>
import Server from '../utils/network'
import CommentModal from './CommentModal'
import Comment from './Comment'

export default {
  components: {
    CommentModal,
    Comment
  },
  data () {
    return {
      server: new Server(this),
      state: Store.state,
      listing: {},
      address: {},
      introBarSrc: null,
      introReady: false,
      inputs: {
        filter_checkin: '',
        filter_checkout: '',
        filter_guests: 1
      }
    }
  },
  events: {
    addedComment (commentId) {
      console.log(commentId)
      this.listing.comments.push(commentId)
    }
  },
  methods: {
    requestBooking (id) {
      this.state.booking = true
      this.inputs.price = this.listing.price
      this.state.bookingDetails = this.inputs
      this.$route.router.go({name: 'confirmBooking', params: {listing_id: id}})
    }
  },
  ready () {
    let vm = this
    let id = vm.$route.params.listing_id
    vm.server.getListingById(id).then((res) => {
      vm.listing = res
      if (typeof vm.listing.amenities !== 'object') {
        console.log(vm.listing.amenities)
        vm.listing.amenities = vm.listing.amenities.replace('[', '').replace(']', '').split(',')
      }
    }).then(() => {
      vm.server.getAddressByListingId(vm.listing.address_id).then((res) => {
        vm.address = res
      })
      vm.server.getCommentsByIds(vm.listing.comments).then((res) => {
        console.log(res)
      })
      // Init select inputs
      $('select').material_select()
      console.log(vm.listing)
    })

    $(document).ready(() => {
      let date = new Date()
      $('img.unsplash').on('load', () => {
        vm.introReady = true
      })
      // Init date inputs
      $('.datepicker').pickadate({
        onSet: function (arg) {
          if ('select' in arg) {
            this.close()
          }
        },
        container: 'body',
        selectMonths: true,
        selectYears: 10,
        min: date.toISOString()
        // max: [date.getFullYear() - 18, date.getUTCMonth(), date.getUTCDate()]
      })
    })
  }
}
</script>

<style lang="stylus" scoped>
#listingViewComponent
  overflow auto !important
  height 100%
  position relative
#introBar
  img
    width 100%
    height 400px
    @media screen and (max-width 1000px)
      height 200px 
.details
  background-color #F5F5F5
  margin-top 1em
  padding-top .25em
  padding-bottom 1em
  .amenities-bar
    .chip
      margin .5em 1em 0 0 
  .comments
    height 100%
    
.info
  .intro
    padding-right 22em
    @media screen and (max-width 1000px)
      padding 0
  
.checkin
  position absolute
  right 6em
  top 23em
  width 400px
  height 250px

  @media screen and (max-width 1000px)
    margin 1em
    position relative
    top 0
    right 0
    width auto
  .price-header
    text-align center
    height 50px
    background-color rgba(84,84,84,0.7)
    padding .5em 1em 0em 1em
    margin-bottom 0
    color white
    b
      font-size 25px
    span
      font-size 14px
  .content
    background-color rgba(255,255,255,1)
    height 100% - 20
    .options
      padding 1em
    
</style>
