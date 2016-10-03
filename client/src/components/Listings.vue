<template lang="pug">
.row.no-margin.listingsComponent
  .listingsContainer.col.s12.m7
    form.listingsFilters
      .row.dates
        .col.s12.m3.center(style="margin-top: 2em;")
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
            - var n = 2
            while n <= 15
              option(value= n)= n++ + ' Guests'
            option(value="16") 16 Guests++
          label Guests
      .divider
      .row.roomTypes
        .col.s12.m3.center(style="margin-bottom: 1em;")
          b.input-label#roomTypesLabel Room Type
        .col.s4.m3
          input(type="checkbox" v-model="inputs.filter_types.entire")#entire
          label(for="entire") Entire Home/Apt
        .col.s4.m3
          input(type="checkbox" v-model="inputs.filter_types.private")#private
          label(for="private") Private Room
        .col.s4.m3
          input(type="checkbox" v-model="inputs.filter_types.shared")#shared
          label(for="shared") Shared Room
      .divider
      .row.priceRange
        .col.s12.center
          b.input-label#priceRangeLabel Price Range: 
            span.prices ${{inputs.filter_pricerange[0]}} 
            span - 
            span.prices ${{inputs.filter_pricerange[1] === 1500 ? '1500+' : inputs.filter_pricerange[1] }}
        .col.s12.priceRangeSliderContainer
          #priceRangeSlider
      .section
      .row.geo.center
        .input-field.col.s4
          i.material-icons.prefix edit_location
          input(id="filter_lat" type="text" v-model="inputs.filter_lat" )
          label(for="filter_lat") Latitude
        .input-field.col.s4
          i.material-icons.prefix edit_location
          input(id="filter_lng" type="text" v-model="inputs.filter_lng" )
          label(for="filter_lng") Longitude
        .input-field.col.s4
          i.material-icons.prefix edit_location
          input(id="filter_miles" type="text" v-model="inputs.filter_miles" value="20")
          label(for="filter_miles") Miles
      .row.center
        button.btn(@click.prevent="searchPress" style="margin-top: 2em !important;") Search
      .divider
    .section
    template(v-for="listing of listings" track-by="listing_id")
      listing-card(v-bind:listing.sync="listing")
  .mapContainer.col.s12.m5.no-padding
    #map
</template>

<script>
import ListingCard from './ListingCard'
import Server from '../utils/network'

export default {
  components: {
    ListingCard
  },
  data () {
    return {
      server: new Server(this),
      state: Store.state,
      inputs: {
        filter_checkin: '',
        filter_checkout: '',
        filter_guests: 1,
        filter_types: {
          entire: true,
          private: true,
          shared: true
        },
        filter_pricerange: [0, 300],
        filter_lat: '',
        filter_lng: ''
      },
      map: null,
      listings: []
    }
  },

  events: {
    mapPress (coordinates) {
      this.map.setCenter(coordinates)
    }
  },

  methods: {
    searchPress () {
      let lat = this.inputs.filter_lat
      let lng = this.inputs.filter_lng
      let miles = this.inputs.filter_miles
      this.server.searchListings(lat, lng, miles, this.inputs).then((res) => {
        console.log(res)
        this.listings = res
        this.init()
      })
    },
    updatePriceRange (values, handle, unencoded, tap, position) {
      this.inputs.filter_pricerange = [parseInt(values[0].replace('$', '')),
                                      parseInt(values[1].replace('$', ''))]
    },
    handleSearchInput (place) {
      console.log(place)
      // If the place has a geometry, then present it on a map.
      let bounds = new google.maps.LatLngBounds()
      if (place.geometry && place.geometry.viewport) {
        this.map.fitBounds(place.geometry.viewport)
        this.map.setZoom(13)
      } else {
        this.map.setCenter(place.geometry.location)
        this.map.setZoom(13)
      }
      return new Promise((resolve, reject) => {
        bounds.extend(place.geometry.location)
        bounds = bounds.toJSON()
        resolve(bounds)
      }).then((res) => {
        this.inputs.filter_lat = res.north
        this.inputs.filter_lng = res.east
      })
    },
    init () {
      let vm = this
      let toronto = new google.maps.LatLng(43.6532, -79.3832)
      vm.server.getAddresses().then((res) => {
        vm.state.addresses = res
      }).then(() => {
        // Load our listings
        vm.server.getListings().then((res) => {
          vm.listings = res
          for (let listing of vm.listings) {
            vm.server.getAddressById(listing.address_id).then((res) => {
              let address = res
              listing.address = address
              let infoWindow = new google.maps.InfoWindow({
                content: `
                  <b> ${listing.title} </b>
                `
              })

              let marker = new MarkerWithLabel({
                position: address.mapCoordinates,
                map: vm.map,
                title: listing.title,
                labelContent: `$${listing.price}`,
                labelAnchor: new google.maps.Point(22, 0),
                labelClass: 'map-label'
              })
              marker.addListener('click', () => {
                infoWindow.open(vm.map, marker)
              })
            })
          }
        }).then(() => {
          // Set map back to Toronto
          vm.map.setCenter(toronto)
        })
      })
    }
  },

  ready () {
    let vm = this
    $(document).ready(function () {
      let date = new Date()
      // Init select inputs
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
        // max: [date.getFullYear() - 18, date.getUTCMonth(), date.getUTCDate()]
      })
      // Init pricerange ui
      let priceRangeSlider = document.getElementById('priceRangeSlider')
      noUiSlider.create(priceRangeSlider, {
        start: vm.inputs.filter_pricerange,
        connect: true,
        range: {
          min: [0],
          max: [1500]
        },
        step: 1,
        format: wNumb({
          decimals: 1,
          prefix: '$'
        })
      })
      priceRangeSlider.noUiSlider.on('slide', vm.updatePriceRange)
      // Init Google Maps
      let mapContainer = document.getElementById('map')
      let toronto = new google.maps.LatLng(43.6532, -79.3832)
      vm.map = new google.maps.Map(mapContainer, {
        center: toronto,
        zoom: 10
      })

      // Init search bar
      let searchBars = document.getElementsByClassName('searchInput')
      for (let searchBar of searchBars) {
        let _search = new google.maps.places.Autocomplete(searchBar)
        _search.addListener('place_changed', () => {
          vm.handleSearchInput(_search.getPlace())
        })
      }
      vm.init()
    })
  },

  beforeDestroy () {
  }
}
</script>

<style lang="stylus" scoped>
.listingsComponent
  overflow auto !important
  height 100%
.listingsContainer
  background-color #F5F5F5 !important
  height 93vh
  padding-top .5em
  overflow auto !important
  .input-label
    font-size 16px
  .dates
    margin 0 !important
  .roomTypes
    margin 0
    margin-top 1em
  .priceRange
    margin-top 1em
    margin-bottom 0
  .priceRangeSliderContainer
    margin-top 1em
  .prices
    color green
  #datesLabel
    top 1em
  #roomTypesLabel
    margin 0
.mapContainer
  height 93vh
  width 100%
  #map
    height 100%
</style>

<style lang='stylus'>
.noUi-target .noUi-active .range-label {
  border-radius 15px 15px 15px 0 !important
  transform scale(1.8) rotate(-45deg) translate(23px, -25px) !important
}
.range-label span
  font-size 8px !important
  padding-left 5px

.map-label
  font-size 18px
  font-weight 600
  color white
  background #F76458
  padding 2px
  padding-right 4px !important
  margin-top -2.5em !important
  margin-left -25px !important
  border-radius 5px
</style>
