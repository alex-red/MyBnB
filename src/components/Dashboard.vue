<template lang="pug">
.section
.row.intro
  .col.m8.s12.media
    h4.label.left-align Toronto 
    img.responsive-img.z-depth-2(src="https://c2.staticflickr.com/8/7386/8727883772_13c295e24d_b.jpg")
  .col.m4.s12.description
    p.left-align Toronto, Ontario's capital city, has a vibrant history of change and growth, ranging from its early occupation over 1,000 years ago to its current status as North America’s fourth largest city. Toronto is Canada's largest municipality and is made up of the former cities of Toronto, North York, Scarborough, York and Etobicoke, and the former borough of East York. The city is home to a large immigrant population, and is a national and international hub for finance, communications and cultural life.

</template>

<script>
export default {
  data () {
    return {
      msg: 'Main'
    }
  }
}
</script>

<style lang="stylus" scoped>
.intro
  .media
    .label
      font-weight 500
    img
      height 400px
  .description
    margin-top 3.5em

.intro-bar
  height 150px
  background-color black
  img
    width 100%
    height 150px
</style>
