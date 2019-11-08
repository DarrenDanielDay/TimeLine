<template>
  <div id="timeline-app">
    <el-row style="margin-left: 0px; margin-right: 0px;" id="head">
      <el-col :span="16" id="title">
        <div class="center">TimeLine</div>
      </el-col>
      <el-col :span="8" style="display:flex;align-items:center;">
        <el-button @click="forward">刷新</el-button>
      </el-col>
    </el-row>
    <div style="height:450px;border-bottom-style: solid;border-bottom-color: aqua;overflow:auto;">
      <div v-for="(card,index) in cards" :key="index">
        <content-card :comment="card"></content-card>
      </div>
    </div>
    <div style="height:75px;display:flex;align-items:center;justify-content: center;">
      <el-button @click="previous">更多</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import ContentCard from "./ContentCard.vue";
import { Comment } from "./ContentCard.vue";
import Component from "vue-class-component";
@Component({
  components: {
    ContentCard
  }
})
class TimeLine extends Vue {
  public cards: Comment[] = [];
  constructor() {
    super();
  }
  mounted(): void {
    console.log("mounted!");
    for (var i = 0; i < 5; i++) {
      this.cards.push(Comment.Default());
    }
  }
  previous(): void {
    console.log("previous!");
  }
  forward(): void {
    console.log("forward!");
  }
}
export default TimeLine;
</script>

<style scoped>
#timeline-app {
  height: 600px;
  width: 300px;
  border-style: solid;
  border-color: aqua;
}

#head {
  display: flex;
  justify-content: center;
  height: 75px;
  border-bottom-style: solid;
  border-bottom-color: aqua;
}
#title {
  display: flex;

  justify-content: center;
  align-items: center;
}
.center {
  height: 30px;
  font-size: 30px;
}
</style>