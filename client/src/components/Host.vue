<template lang="pug">
#hostViewComponent
  .container.center
    .section
    h4.no-margin Hosting Tools
    .section
    .divider
    h5 Create A Listing
    .divider
    .listing-form
      form.col.s12
        .row
          .input-field.col.s12
            i.material-icons.prefix account_circle
            input(id="title" type="text" name="title" v-model="inputs.title" required)
            label(for="title") Title
        .row
          .input-field.col.s12
            i.material-icons.prefix lock
            textarea(id="description" type="text" v-model="inputs.description" required).materialize-textarea
            label(for="description") Description
        .row.dates(style="margin: 0;")
          .input-field.col.s4
            input(id="filter_from" type="date" v-model="inputs.filter_from").datepicker
            label(for="filter_from") From
          .input-field.col.s4
            input(id="filter_to" type="date" v-model="inputs.filter_to").datepicker
            label(for="filter_to") To
          .input-field.col.s4
            select(v-model="inputs.filter_guests")
              option(selected value="1") 1 Guest
              - var n = 2
              while n <= 15
                option(value= n)= n++ + ' Guests'
              option(value="16") 16 Guests++
            label Guests
        .row
          .input-field.col.s12
            i.material-icons.prefix lock
            textarea(id="amenities" type="text" v-model="inputs.amenities" required).materialize-textarea
            label(for="amenities") Amenities
        .row
          .input-field.col.s12.m6
            i.material-icons.prefix edit_location
            input(id="user_address" type="text" v-model="inputs.user_address" )
            label(for="user_address") Address
          .input-field.col.s12.m6
            i.material-icons.prefix map
            input(id="user_city" type="text" v-model="inputs.user_city" )
            label(for="user_city") City
        .row
          .input-field.col.s12.m6
            i.material-icons.prefix satellite
            input(id="user_country" type="text" v-model="inputs.user_country" required)
            label(for="user_country") Country
          .input-field.col.s12.m6
            i.material-icons.prefix place
            input(id="user_zipcode" type="text" v-model="inputs.user_zipcode")
            label(for="user_zipcode") Zip Code
        .row
          .input-field.col.s6
            i.material-icons.prefix edit_location
            input(id="filter_lat" type="text" v-model="inputs.filter_lat" )
            label(for="filter_lat") Latitude
          .input-field.col.s6
            i.material-icons.prefix edit_location
            input(id="filter_lng" type="text" v-model="inputs.filter_lng" )
            label(for="filter_lng") Longitude
        .row
          .input-field.col.s12
            i.material-icons.prefix money
            input(id="user_price" type="text" v-model="inputs.user_price" required)
            label(for="user_price") Price
        .row
          .col.s12
            a.btn(@click.prevent="createListing" href="#") CREATE LISTING
    .divider
    h5 Add an Address
    .divider
    .section
    .row
      .col.s12.search.input-field
        input#addressInput(type="search" placeholder="Search" required)
        label(for="search")
            i.material-icons search
        i.material-icons close
    .row
      .col.s12
        a.btn ADD ADDRESS
</template>

<script>
import Server from '../utils/network'
import moment from 'moment'

export default {
  data () {
    return {
      state: Store.state,
      server: new Server(this),
      msg: 'Main',
      inputs: {
      }
    }
  },
  methods: {
    handleSearchInput (place) {
      console.log(place)
    },
    createListing () {
      let userId = this.state.user.user_id
      let vm = this
      vm.inputs.coordinates = `["${vm.inputs.filter_lat}", "${vm.inputs.filter_lng}"]`
      vm.server.addAddress(userId, vm.inputs).then((res) => {
        vm.inputs.address_id = res
        vm.inputs.available_dates = `["${moment(new Date(vm.inputs.filter_from)).format('YYYY-MM-DD')}", "${moment(new Date(vm.inputs.filter_to)).format('YYYY-MM-DD')}"]`
        vm.inputs.date = moment(new Date()).format('YYYY-MM-DD')
        vm.inputs.amenities = `"[${vm.inputs.amenities.split(' ')}]"`
        vm.server.addListing(userId, vm.inputs).then((res) => {
          console.log(res)
          Materialize.toast('Added Listing!', 5000)
        })
      })
      // this.server.addListing(userId, this.inputs)
    }
  },
  ready () {
    let vm = this
    $(document).ready(() => {
      let date = new Date()
      $('select').material_select()
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
      })
    })

    let searchBar = document.getElementById('addressInput')
    let _search = new google.maps.places.Autocomplete(searchBar)
    _search.addListener('place_changed', () => {
      vm.handleSearchInput(_search.getPlace())
    })
  }
}
</script>

<style lang="stylus" scoped>
#hostViewComponent
  overflow auto !important
  height 100%
  position relative
  background-color #F5F5F5
  .container
    background-color white
    
</style>
