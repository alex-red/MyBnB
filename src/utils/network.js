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
      if (typeof data !== 'undefined' && data && data.length > 0) {
        return data[0].user_id
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }

  // getUser given userId
  // SUCCESS: User Object
  // FAILURE: null
  getUser (userId) {
    return this.http.get(this.serverHost + '/users/' + userId).then((res) => {
      let data = res.json()
      if (typeof data !== 'undefined' && data && data.length > 0) {
        return data[0]
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }

  // Retrieve all listings
  // SUCCESS: List of listing objects
  // FAILURE: null
  getListings () {
    return this.http.get(this.serverHost + '/listings').then((res) => {
      let data = res.json()
      if (typeof data !== 'undefined' && data && data.length > 0) {
        Store.state.listings = data
        return data
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }

  getAddressByListingId (id) {
    let that = this
    return new Promise((resolve, reject) => {
      try {
        if (this.state.addresses) {
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

  getListingById (id) {
    let that = this
    return new Promise((resolve, reject) => {
      try {
        if (this.state.listings) {
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
      if (typeof data !== 'undefined' && data && data.length > 0) {
        Store.state.addresses = data
        return data
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }

  getAddressById (id) {
    try {
      if (this.state.addresses) {
        let res = this.state.addresses.filter(addr => addr.address_id === id)
        return res[0]
      } else {
        this.getAddresses().then(() => {
          let res = this.state.addresses.filter(addr => addr.address_id === id)
          return res[0]
        })
      }
    } catch (e) {
      console.log(`Error: ${e}`)
      return false
    }
  }
}
