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
        return data
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
  getAddresses () {
    return this.http.get(this.serverHost + '/addresses').then((res) => {
      let data = res.json()
      for (let obj of data) {
        obj.coordinates = obj.coordinates ? JSON.parse(obj.coordinates) : null
      }
      if (typeof data !== 'undefined' && data && data.length > 0) {
        return data
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }

  getAddressById (id) {
    if (this.state.addresses) {
      let res = this.state.addresses.filter(addr => addr.address_id === id)
      if (res) {
        return res[0]
      }
    }
    return false
  }
}
