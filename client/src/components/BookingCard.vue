<template lang="pug">
.row
  .col.s12
    .card.color-primary.white-text
      .card-content
        span.card-title {{listing.title}} 
        .divider(style="margin-bottom: 1em;")
        .details
          .row
            b.green-text Host: 
            b {{owner.name}}
          .row
              b.green-text Address:  
              b {{address.address | titlecase}}, 
              b {{address.zipcode ? address.zipcode.toUpperCase() : ''}}, 
              b {{address.city | titlecase}}, 
              b {{address.country | titlecase}}
          .row
              b.green-text Length of Stay: 
              b {{booking.available_dates[0]}} - 
              b {{booking.available_dates[1]}} [ {{booking.days}} Nights ]
          .row
              b.green-text Price:  
              b ${{booking.price}} CAD
        .chip.green(style="margin-top: 1em;")
          span {{ booking.status }}
        .row(style="margin-top: 2em;")
          .col.s12
            a.btn(v-link="{ name: 'listing', params: {listing_id: listing.listing_id}}") Details
          .col.s12(style="margin-top: 1em")
            a.btn.red Cancel
</template>

<script>
import Server from '../utils/network'

export default {
  props: ['booking'],
  data () {
    return {
      state: Store.state,
      server: new Server(this),
      listing: {},
      owner: {},
      address: {},
      address_full: '',
      msg: 'Main'
    }
  },
  methods: {
    pressDetails () {
      console.log('det')
    }
  },
  ready () {
    let vm = this
    vm.server.getListingById(this.booking.listing_id).then((res) => {
      vm.listing = res
      vm.server.getAddressByListingId(vm.listing.address_id).then((res) => {
        vm.address = res
      })
    })
    vm.server.getUser(this.booking.host_id).then((res) => {
      vm.owner = res
    })
  }
}
</script>

<style lang="stylus" scoped>
</style>
