<template lang="pug">
#bookingViewComponent
  h4 Booking

</template>

<script>
import Server from '../utils/network'

export default {
  data () {
    return {
      state: Store.state,
      server: new Server(this),
      msg: 'Main'
    }
  }
}
</script>

<style lang="stylus" scoped>
#bookingViewComponent
  overflow auto !important
  height 100%
  position relative
</style>
