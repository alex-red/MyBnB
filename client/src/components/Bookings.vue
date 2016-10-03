<template lang="pug">
#bookingsViewComponent
  .container.center
    h4 Bookings
  template(v-for="booking of bookings" track-by="booking_id")
    booking-card(v-bind:booking.sync="booking")

</template>

<script>
import Server from '../utils/network'
import BookingCard from './BookingCard'

export default {
  components: {
    BookingCard
  },
  data () {
    return {
      state: Store.state,
      server: new Server(this),
      msg: 'Main',
      bookings: []
    }
  },
  ready () {
    let vm = this
    if (this.state.user) {
      vm.server.getBookings(this.state.user.user_id).then((res) => {
        vm.bookings = res
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
#bookingsViewComponent
  overflow auto !important
  height 100%
  position relative
</style>
