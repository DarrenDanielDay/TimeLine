<template>
  <div id="timeline-app">
    <el-row style="margin-left: 0px; margin-right: 0px;" id="head">
      <el-col :span="16" id="title">
        <div class="center">TimeLine</div>
      </el-col>
      <el-col :span="8" style="display:flex;align-items:center;">
        <el-button @click="this.forward">刷新</el-button>
      </el-col>
    </el-row>
    <div style="height:450px;border-bottom-style: solid;border-bottom-color: aqua;overflow:auto;">
      <div v-for="(card,index) in this.cards" :key="index">
        <content-card :comment="card"></content-card>
      </div>
    </div>
    <div style="height:75px;display:flex;align-items:center;justify-content: center;">
      <el-button @click="this.previous">更多</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import ContentCard from "./ContentCard.vue";
import { Comment } from "./ContentCard.vue";
import Component from "vue-class-component";
import Axios from "axios";
import { app } from "../main";
import {urlBase}from "../App.vue";

@Component({
  components: {
    ContentCard
  }
})
class TimeLine extends Vue {
  public static urlBase:string=urlBase;
  public urlBase:string=urlBase;
  public cards: Comment[] = new Array<Comment>();
  constructor() {
    super();
    console.log("init TimeLine");
    console.log(urlBase);
  }
  mounted(): void {
    console.log("mounted!");
    console.log(`${this.urlBase}more-posts`);

    Axios.get(`${this.urlBase}more-posts`, {  })
      .then(response => {
        this.add(response);
      })
      .catch(error => {
        console.log("error in mounted");
        console.log(error);
        app.$message({
          message: error,
          type: "error"
        });
      });
  }
  previous(): void {
    console.log("previous!");
    let lastTime = "" ;
    if (this.cards.length > 0) {
      lastTime = this.cards.slice(-1)[0].time;
    }
    console.log(`${urlBase}more-posts?time=${lastTime}`);
    console.log(lastTime);
    Axios.get(`${urlBase}more-posts`, { params: { time: lastTime } })
      .then(response => {
        let cnt=this.cards.length;
        this.add(response);
        app.$message({
          message:`增加了${this.cards.length-cnt}条信息`,
          type:"success"
        })
      })
      .catch(error => {
        console.log("error in previous");
        console.error(error);
        app.$message({
          message: error,
          type: "error"
        });
      });
  }
  forward(): void {
    console.log("forward!");
    let latestTime = "";
    if (this.cards.length > 0) {
      latestTime = this.cards[0].time.toString();
    }
    console.log(`${urlBase}refresh?time=${latestTime}`);
    Axios.get(`${urlBase}refresh`, { params: { time: latestTime } })
      .then(response => {
        this.add(response);
        this.cards.sort((a: Comment, b: Comment): number => {
          return b.time > a.time?1:-1;
        });
        this.cards = this.cards.slice(0,10);
        app.$message({
          message:`刷新成功`,
          type:"success"
        })
      })
      .catch(error => {
        console.log("error in previous");
        console.error(error);
        app.$message({
          message: error,
          type: "error"
        });
      });
  }
  private add(response: { data: { posts:any[] } }) {
    console.log("add response:");
    console.log(response);
    let json = response.data.posts;
    for (let i in json) {
      let obj = json[i];
      this.cards.push(Comment.Parse(obj));
    }
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