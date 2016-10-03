<template lang="pug">
.modal#addCardModal
  .modal-content
    h4 Add Credit Card
    a.btn-floating.close-btn(@click="closeModal"): i.material-icons close
    form.col.s12
      .row
        .input-field
          i.material-icons.prefix credit_card
          input(id="card_number" pattern="[0-9]{13,16}" type="text" v-model="inputs.card_number" maxlength="16" required).validate
          label(for="card_number" data-error="Invalid Card") Card #
      .row
        .input-field.col.s6
          i.material-icons.prefix date_range
          input(id="card_expiry" type="date" v-model="inputs.card_expiry" required)
          label(for="card_expiry") Expiry
        .input-field.col.s6
          i.material-icons.prefix security
          input(id="card_cvc" pattern="[0-9]{3}" type="text" v-model="inputs.card_cvc" maxlength="3" required).validate
          label(for="card_cvc") CVC
      .row
        .input-field
          i.material-icons.prefix person
          input(id="card_name" type="text" v-model="inputs.card_name" required)
          label(for="card_name" data-error="Invalid Card") Name on Card
    span.error-text(v-show="inputs.error") Could not login with those credentials!
  .modal-footer
    button.btn(@click="addCardSubmit" type="submit").green Add
      i.material-icons.right send
    button.btn(@click="closeModal").orange close
</template>

<script>
import Server from '../utils/network'
import moment from 'moment'

export default {
  data () {
    return {
      state: Store.state,
      inputs: {
        card_name: '',
        card_cvc: '',
        card_expiry: '',
        card_number: '',
        error: false
      },
      server: new Server(this)
    }
  },

  events: {
    closeModal () {
      console.log('test')
      this.closeModal()
    }
  },

  methods: {
    addCardSubmit () {
      for (let i in this.inputs) {
        if (!this.inputs[i] && i !== 'error') {
          this.inputs.error = true
          Materialize.toast('Please fill in all form inputs!', 5000)
          return false
        } else if (i === 'card_expiry') {
          this.inputs[i] = moment(new Date(this.inputs[i])).format('YYYY-MM-DD')
        }
      }
      let vm = this
      vm.server.addWallet(Store.state.user.user_id, this.inputs).then((res) => {
        if (res) {
          this.$dispatch('cardAdded')
          vm.closeModal()
        } else {
          Materialize.toast('Error adding credit card.')
        }
      })
    },
    closeModal () {
      this.inputs.card_name = ''
      this.inputs.card_number = ''
      this.inputs.card_cvc = ''
      this.inputs.card_expiry = ''
      this.inputs.error = false
      $('#addCardModal').closeModal()
    }
  },

  ready () {
    let vm = this
    let date = new Date()
    $(document).ready(function () {
      $('.addCardModalTrigger').leanModal({
        complete: () => {
          vm.closeModal()
        }
      })
      $('#card_expiry').pickadate({
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
        min: [date]
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
