export default class Server {
  constructor (vue) {
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
      if (typeof data !== 'undefined' && data.length > 0) {
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
      if (typeof data !== 'undefined' && data.length > 0) {
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
      console.log('Dealing with: ', res)
      let data = res.json()
      if (typeof data !== 'undefined' && data.length > 0) {
        return data[0]
      } else {
        return null
      }
    }, (res) => {
      return null
    })
  }
}
