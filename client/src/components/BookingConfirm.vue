<template lang="pug">
#bookingViewComponent
  add-card-modal
  .container.wrapper.z-depth-1
    h4.center.no-margin(style="padding-bottom:1em;") Booking Confirmation
    .details
      .row.center
        .col.s5
          b.green-text Address:
        .col.s6
          b {{address.address | titlecase}}, 
          b {{address.zipcode ? address.zipcode.toUpperCase() : ''}}, 
          b {{address.city | titlecase}}, 
          b {{address.country | titlecase}}
      .row.center
        .col.s5
          b.green-text Length of Stay:
        .col.s6
          b {{details.filter_checkin}} - 
          b {{details.filter_checkout}} 
          b.blue [{{days}} Nights]
      .row.center
        .col.s5
          b.green-text Details:
        .col.s6
          b {{details.filter_guests}} Guests
      .row.center
        .col.s5
          b.green-text Price:
        .col.s6
          b ${{days * details.price}} CAD
    .payment
      .row.center
        h4 Payment
        .col.s12(v-show="wallet.length > 0" style="text-align: center;")
          label Credit Card
          select.browser-default.credit-card-select(v-model="inputs.credit_card")
            option(v-for="card of wallet" track-by="$index" value="{{card.card_number}}") Credit Card - {{card.card_number}} 
      .row.center(v-show="wallet.length === 0")
        h5 You have no payment methods
      .row.center
        a.btn(href="#addCardModal" data-target="addCardModal").green#addCardModalTrigger Add Card
    .confirm
      .row.center
        button.btn-large(@click.prevent="completeBooking" v-bind:class="{'disabled': inputs.credit_card.length <= 0}").color-primary COMPLETE
</template>

<script>
import Server from '../utils/network'
import AddCardModal from './AddCardModal'
import moment from 'moment'

export default {
  components: {
    AddCardModal
  },
  data () {
    return {
      listing_id: this.$route.params.listing_id,
      details: Store.state.bookingDetails,
      state: Store.state,
      server: new Server(this),
      msg: 'Main',
      address: {},
      inputs: {
        credit_card: ''
      },
      user: Store.state.user,
      wallet: [],
      days: 0
    }
  },
  events: {
    cardAdded () {
      this.server.getWallet(this.state.user.user_id).then((res) => {
        this.wallet = res
        console.log(res)
      })
    }
  },
  methods: {
    completeBooking () {
      let payload = {
        host_id: this.listing.user_id,
        renter_id: this.state.user.user_id,
        listing_id: this.listing.listing_id,
        available_dates: `["${moment(new Date(this.details.filter_checkin)).format('YYYY-MM-DD')}", "${moment(new Date(this.details.filter_checkout)).format('YYYY-MM-DD')}"]`,
        price: this.days * this.details.price,
        days: this.days
      }
      this.server.addBooking(payload).then((res) => {
        if (res) {
          Materialize.toast('Completed Booking.', 5000)
          this.$route.router.go({name: 'bookings'})
        } else {
          Materialize.toast('Failed to book listing.', 5000)
        }
      })
    }
  },
  ready () {
    let vm = this
    vm.server.getListingById(this.listing_id).then((res) => {
      vm.listing = res
      vm.server.getAddressByListingId(vm.listing.address_id).then((res) => {
        vm.address = res
      })
    })
    if (this.state.user) {
      vm.server.getWallet(this.state.user.user_id).then((res) => {
        vm.wallet = res
        console.log(res)
      })
    }
    $(document).ready(() => {
      $('#addCardModalTrigger').leanModal({
        complete: () => {
          this.$dispatch('closeModal')
        }
      })
      let a = moment(new Date(vm.details.filter_checkin))
      let b = moment(new Date(vm.details.filter_checkout))
      vm.days = b.diff(a, 'days')
    })
  }
}
</script>

<style lang="stylus" scoped>
#bookingViewComponent
  overflow auto !important
  height 100%
  position relative
  background-color #F5F5F5
  
.wrapper
  background-color white
  padding-top 1em
  padding-bottom 2em
.details
  font-size 20px
  
.credit-card-select
  width 80%
  margin 0 auto
</style>
