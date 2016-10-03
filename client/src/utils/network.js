import { Store } from './store'

export default class Server {
  constructor (vue) {
    this.state = Store.state
    this.vue = vue
    this.http = vue.$http
    this.serverHost = 'http://192.168.0.11:5000'
  }

  // Login to server
  // SUCCESS: user_id
  // FAILURE: null
  login (payload) {
    return this.http.post(this.serverHost + '/login', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      if (typeof data !== 'undefined' && data && data.length > 0) {
        return data[0].user_id
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }

  // Attempt to register new user
  // SUCCESS: User id
  // FAILURE: null
  signup (payload) {
    return this.http.post(this.serverHost + '/users/signup', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      return data[0].user_id
    }, (res) => {
      return null
    })
  }

  // getUser given userId
  // SUCCESS: User Object
  // FAILURE: null
  getUser (userId, force = true) {
    // return this.http.get(this.serverHost + '/users/' + userId).then((res) => {
    //   let data = res.json()
    //   if (typeof data !== 'undefined' && data && data.length > 0) {
    //     return data[0]
    //   } else {
    //     return null
    //   }
    // }, (res) => {
    //   return null
    // })
    return new Promise((resolve, reject) => {
      if (!userId) { return null }
      try {
        if (this.state.users && !force) {
          let res = this.state.users.filter(user => userId === user.user_id)
          resolve(res[0])
        } else {
          this.getUsers().then(() => {
            let res = this.state.users.filter(user => userId === user.user_id)
            resolve(res[0])
          })
        }
      } catch (e) {
        console.log(`Error: ${e}`)
        reject(e)
      }
    })
  }

  // Retrieve all users
  // SUCCESS: List of users
  // FAILURE: false
  getUsers () {
    return this.http.get(this.serverHost + '/users').then((res) => {
      let data = res.json()
      if (typeof data !== 'undefined' && data && data.length > 0) {
        Store.state.users = data
        return data
      } else {
        return false
      }
    }, (res) => {
      return false
    })
  }

  // Helper Utility to convert string JSON to actual JSON
  convertJSON (data, [...attributes]) {
    for (let obj of data) {
      for (let attr of attributes) {
        obj[attr] = obj[attr] ? JSON.parse(obj[attr]) : null
      }
    }
  }

  getComments () {
    try {
      return this.http.get(this.serverHost + '/comments').then((res) => {
        let data = res.json()
        if (typeof data !== 'undefined' && data && data.length > 0) {
          Store.state.comments = data
          return data
        } else {
          return null
        }
      }, (res) => {
        return null
      })
    } catch (e) {
      return e
    }
  }

  getCommentsByIds (commentIds, force = false) {
    return new Promise((resolve, reject) => {
      if (!commentIds) { return null }
      try {
        if (this.state.comments && !force) {
          let res = this.state.comments.filter(comment => commentIds.includes(comment.comment_id))
          resolve(res)
        } else {
          this.getComments().then(() => {
            let res = this.state.comments.filter(comment => commentIds.includes(comment.comment_id))
            resolve(res)
          })
        }
      } catch (e) {
        console.log(`Error: ${e}`)
        reject(e)
      }
    })
  }

  postComment (ctx, ctxId, user, comment) {
    return new Promise((resolve, reject) => {
      if (!user) { return null }
      try {
        let payload = {
          context: ctx,
          context_id: ctxId,
          user_id: user,
          comment: comment
        }
        this.http.post(this.serverHost + '/comment', {}, { params: {data: payload} }).then((res) => {
          resolve(res.json())
        })
      } catch (e) {
        console.log(`Error: ${e}`)
        reject(e)
      }
    })
  }

  // Retrieve all listings
  // SUCCESS: List of listing objects
  // FAILURE: null
  getListings () {
    let that = this
    return this.http.get(this.serverHost + '/listings').then((res) => {
      let data = res.json()
      console.log(data)
      that.convertJSON(data, ['amenities', 'available_dates', 'comments'])
      Store.state.listings = data
      return data
    }, (res) => {
      return null
    })
  }

  addListing (userId, payload) {
    payload.user_id = userId
    return this.http.post(this.serverHost + '/listings/add', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      return data
    }, (res) => {
      return null
    })
  }

  searchListings (lat, lng, miles, inputs) {
    let payload = {
      lat: lat,
      lng: lng,
      miles: miles,
      price_min: inputs.filter_pricerange[0],
      price_max: inputs.filter_pricerange[1]
    }
    return this.http.post(this.serverHost + '/listings/geo', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      return data
    }, (res) => {
      return null
    })
  }

  getAddressByListingId (id, force = true) {
    return new Promise((resolve, reject) => {
      try {
        if (this.state.addresses && !force) {
          let res = this.state.addresses.filter(address => address.address_id === id)
          resolve(res[0])
        } else {
          this.getAddresses().then(() => {
            let res = this.state.addresses.filter(address => address.address_id === id)
            resolve(res[0])
          })
        }
      } catch (e) {
        console.log(`Error: ${e}`)
        reject(e)
      }
    })
  }

  getListingById (id, force = true) {
    let that = this
    return new Promise((resolve, reject) => {
      try {
        if (this.state.listings && !force) {
          let res = this.state.listings.filter(listing => listing.listing_id === id)
          resolve(res[0])
        } else {
          this.getListings().then(() => {
            let res = this.state.listings.filter(listing => listing.listing_id === id)
            resolve(res[0])
          })
        }
      } catch (e) {
        console.log(`Error: ${e}`)
        reject(e)
      }
    })
  }

  // Retrieve all listings
  // SUCCESS: List of listing objects
  // FAILURE: null
  getAddresses () {
    return this.http.get(this.serverHost + '/addresses').then((res) => {
      let data = res.json()
      for (let obj of data) {
        obj.coordinates = obj.coordinates ? JSON.parse(obj.coordinates) : null
        obj.mapCoordinates = obj.coordinates ? {
          lat: parseFloat(obj.coordinates[0]),
          lng: parseFloat(obj.coordinates[1])
        } : null
      }
      Store.state.addresses = data
      return data
    }, (res) => {
      return null
    })
  }

  getAddressById (id, force = true) {
    return new Promise((resolve, reject) => {
      try {
        if (this.state.addresses && !force) {
          let res = this.state.addresses.filter(addr => addr.address_id === id)
          resolve(res[0])
        } else {
          this.getAddresses().then(() => {
            let res = this.state.addresses.filter(addr => addr.address_id === id)
            resolve(res[0])
          })
        }
      } catch (e) {
        console.log(`Error: ${e}`)
        reject(e)
      }
    })
  }

  addAddress (userId, payload) {
    payload.user_id = userId
    return this.http.post(this.serverHost + '/addresses/add', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      return data
    }, (res) => {
      return null
    })
  }

  // retrieve list of a users wallet
  getWallet (userId) {
    return this.http.get(this.serverHost + '/credit_cards/' + userId).then((res) => {
      let data = res.json()
      Store.state.user.wallets = data
      return data
    }, (res) => {
      return null
    })
  }

  addWallet (userId, payload) {
    payload.user_id = userId
    return this.http.post(this.serverHost + '/credit_cards', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      return data
    }, (res) => {
      return null
    })
  }

  getBookings (userId) {
    return this.http.get(this.serverHost + '/bookings/' + userId).then((res) => {
      let data = res.json()
      this.convertJSON(data, ['available_dates'])
      return data
    }, (res) => {
      return null
    })
  }

  addBooking (payload) {
    return this.http.post(this.serverHost + '/bookings', {}, { params: {data: payload} }).then((res) => {
      let data = res.json()
      return data
    }, (res) => {
      return null
    })
  }
}
