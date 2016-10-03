<template lang="pug">
.modal#bookingModal
  .modal-content
    h5 Booking 
    a.btn-floating.close-btn(@click="closeModal"): i.material-icons close
    p Are you sure you wish to book these dates?
    span.error-text(v-show="inputs.error") Could not send your comment!
  .modal-footer
    button.btn(@click="submitConfirm" type="submit").color-primary Confirm
      i.material-icons.right send
    button.btn(@click="closeModal").color-primary close
</template>

<script>
import Server from '../utils/network'

export default {
  props: ['context'],
  data () {
    return {
      state: Store.state,
      msg: 'Main',
      inputs: {
        comment_text: '',
        error: false
      },
      server: new Server(this)
    }
  },
  methods: {
    closeModal () {
      this.inputs.comment_text = ''
      this.inputs.error = false
      $('#commentModal').closeModal()
    },
    submitComment () {
      if (!this.state.user) {
        Materialize.toast('Please login before doing that.', 5000)
        this.inputs.error = true
      }
      let context = this.context.type
      let contextId = this.context.id
      let comment = this.inputs.comment_text
      let userId = this.state.user.user_id
      this.server.postComment(context, contextId, userId, comment).then((res) => {
        if (res) {
          this.$dispatch('addedComment', res)
          this.closeModal()
        } else {
          this.inputs.error = true
          Materialize.toast('Error occured sending your comment.', 5000)
        }
      })
    }
  },

  ready () {
    let vm = this
    $(document).ready(() => {
      $('.commentModalTrigger').leanModal({
        complete: () => {
          vm.closeModal()
        },
        ready: () => {
          if (this.context.name) {
            this.$set('context.type', 'person')
            this.$set('context.id', this.context.user_id)
            this.$set('context.context', this.context.name)
          } else {
            this.$set('context.type', 'listing')
            this.$set('context.id', this.context.listing_id)
            this.$set('context.context', this.context.title)
          }
        }
      })
    })
  }
}
</script>

<style lang="stylus" scoped>
.modal
  overflow hidden !important
.modal-content
  .close-btn
    background-color black
    position absolute
    top 1em
    right 1em
  form
    margin-top 1.5em
    label
      left 0.1rem !important
  .error-text
    color red
    font-size 1.5em
    opacity 1
    transition opacity 1s ease
.modal-footer
  button
    margin .3em !important
</style>
