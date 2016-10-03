export function configRouter (router) {
  // normal routes
  router.map({

    // not found handler
    '*': {
      component: require('./components/not-found.vue')
    },

    '/': {
      component: require('./components/Dashboard.vue')
    },

    '/listings': {
      name: 'listings',
      component: require('./components/Listings.vue')
    },

    '/listings/:listing_id': {
      name: 'listing',
      component: require('./components/ListingView.vue')
    },

    '/bookings/confirm/:listing_id': {
      name: 'confirmBooking',
      component: require('./components/BookingConfirm.vue')
    },

    '/bookings': {
      name: 'bookings',
      component: require('./components/Bookings.vue')
    },

    '/host': {
      name: 'host',
      component: require('./components/Host.vue')
    }
    // advanced example
  })

  // redirect
  // router.redirect({
  //   '/info': '/about',
  //   '/hello/:userId': '/user/:userId'
  // })

  // global before
  // 3 options:
  // 1. return a boolean
  // 2. return a Promise that resolves to a boolean
  // 3. call transition.next() or transition.abort()
  router.beforeEach((transition) => {
    if (transition.to.path === '/forbidden') {
      router.app.authenticating = true
      setTimeout(() => {
        router.app.authenticating = false
        alert('this route is forbidden by a global before hook')
        transition.abort()
      }, 3000)
    } else {
      transition.next()
    }
  })
}
