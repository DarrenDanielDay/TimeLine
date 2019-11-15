<template>
  <div id="app">
    <time-line></time-line>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import TimeLine from "./components/TimeLine.vue";

@Component({
  components: {
    TimeLine
  }
})
export default class App extends Vue {}
const urlBase:string = "https://ecnuer996.cn/timeline/";
export {urlBase};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
