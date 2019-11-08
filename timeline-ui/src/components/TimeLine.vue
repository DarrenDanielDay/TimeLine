<template>
  <div id="timeline-app">
    <el-row>
      <el-col v-for="i in 4" :span="6" :key="i">
        {{i}}
      </el-col>
    </el-row>
  </div>
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