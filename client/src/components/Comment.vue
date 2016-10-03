<template lang="pug">
.col.s12.m6
  .card
    .card-content
      span.card-title {{user.name | titlecase}}
      p {{comment.comment}}
    .card-action
      a(href="#") Like
</template>

<script>
import Server from '../utils/network'

export default {
  props: ['id'],
  data () {
    return {
      state: Store.state,
      server: new Server(this),
      msg: 'Main',
      comment: {},
      user: {}
    }
  },
  ready () {
    this.server.getCommentsByIds([this.id], true).then((res) => {
      this.$set('comment', res[0])
      this.server.getUser(this.comment.user_id).then((res) => {
        this.$set('user', res)
      })
    })
  }
}
</script>

<style lang="stylus" scoped>
.card-content
  p
    white-space: pre-wrap; /* css-3 */
    white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */
    word-wrap: break-word; /* Internet Explorer 5.5+ */
    white-space : normal;
</style>
