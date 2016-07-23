<template lang="pug">
.modal#signUpModal
  .modal-content
    h4 Sign Up
    a.btn-floating.close-btn(@click="closeModal"): i.material-icons close
    form.col.s12
      .row
        .input-field.col.s12.m6
          i.material-icons.prefix account_circle
          input(id="user_email" type="email" name="user_email" v-model="inputs.user_email" required).validate
          label(for="user_email" data-error="Invalid Email") Email Address
        .input-field.col.s12.m6
          i.material-icons.prefix lock
          input(id="user_password" type="password" v-model="inputs.user_password" required).validate
          label(for="user_password") Password
      .row
        .input-field.col.s12.m6
          i.material-icons.prefix perm_identity
          input(id="user_name" type="text" v-model="inputs.user_name" required)
          label(for="user_name") Name
        .input-field.col.s12.m6
          i.material-icons.prefix perm_contact_calendar
          input(id="user_dob" type="date" v-model="inputs.user_dob" required).datepicker
          label(for="user_dob") Birthday
      .row
        .input-field.col.s12.m6
          i.material-icons.prefix work
          input(id="user_occupation" type="text" v-model="inputs.user_occupation")
          label(for="user_occupation") Occupation
        .input-field.col.s12.m6
          i.material-icons.prefix security
          input(id="user_sin" type="text" v-model="inputs.user_sin")
          label(for="user_sin") SIN/SSN
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

    span.error-text(v-show="inputs.error") Could not sign up! Please check your entries.
  .modal-footer
    button.btn(@click="signupSubmit" type="submit").color-primary submit
      i.material-icons.right send
    button.btn(@click="closeModal").color-primary close
</template>

<script>
import Server from '../utils/network'
import moment from 'moment'

export default {
  data () {
    return {
      state: Store.state,
      msg: 'Main',
      inputs: {
        user_email: '',
        user_password: '',
        user_zipcode: '',
        user_country: '',
        user_dob: '',
        user_sin: '',
        user_address: '',
        user_city: '',
        user_name: '',
        user_occupation: '',
        error: false
      },
      server: new Server(this)
    }
  },

  methods: {
    closeModal () {
      for (let i in this.inputs) {
        if (i !== 'error') {
          this.inputs[i] = ''
        }
      }
      this.inputs.error = false
      $('#signUpModal').closeModal()
    },
    signupSubmit () {
      for (let i in this.inputs) {
        if (!this.inputs[i] && i !== 'error') {
          this.inputs.error = true
          Materialize.toast('Please fill in all form inputs!', 5000)
          return false
        } else if (i === 'user_dob') {
          this.inputs[i] = moment(new Date(this.inputs[i])).format('YYYY-MM-DD')
        }
      }
      let vm = this
      vm.server.signup(this.inputs).then((res) => {
        if (res) {
          vm.server.getUser(res).then((res) => {
            if (res) {
              vm.state.user = res
              vm.$dispatch('loggedIn')
              vm.closeModal()
              return true
            }
          })
        } else {
          vm.inputs.error = true
          return false
        }
      })
      return true
    }
  },

  ready () {
    let vm = this
    $(document).ready(function () {
      $('.signUpModalTrigger').leanModal({
        complete: () => {
          vm.closeModal()
        }
      })
      let date = new Date()
      $('#user_dob').pickadate({
        onSet: function (arg) {
          if ('select' in arg) {
            this.close()
          }
        },
        container: 'body',
        selectMonths: true,
        selectYears: 50,
        // format: 'yyyy-mm-dd',
        formatSubmit: 'yyyy-mm-dd',
        max: [date.getFullYear() - 18, date.getUTCMonth(), date.getUTCDate()]
      })
    })
  }
}
</script>

<style lang="stylus" scoped>
.modal
  overflow-x hidden !important
.modal-content
  .close-btn
    background-color black
    position absolute
    top 1em
    right 1em
  form
    margin-top 1.5em
    label
      // left 0.1rem !important
    i
      margin-top .3em
  .error-text
    color red
    font-size 1.5em
    opacity 1
    transition opacity 1s ease
.modal-footer
  button
    margin .3em !important
</style>
