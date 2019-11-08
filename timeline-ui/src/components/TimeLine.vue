<template>
  <div id="timeline-app">hello,ts!{{msg}}</div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  name: "TimeLine",
  methods: {},
  data() {
    return {
      msg: "i'm message"
    };
  },
});
</script>

<style scoped>
#timeline-app {
  background-color: aqua;
}
</style>