export function configRouter (router) {
  // normal routes
  router.map({

    // not found handler
    '*': {
      component: require('./components/not-found.vue')
    },

    '/': {
      component: require('./App.vue')
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
