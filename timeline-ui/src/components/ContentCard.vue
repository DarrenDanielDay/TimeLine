<template>
  <div>
    <el-card>
      <el-row>
        <el-col :span="12">
          <p>{{comment.username}}</p>
        </el-col>
        <el-col :span="12">
          <p>{{time}}</p>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-carousel height="100px" indicator-position="outside" v-if="comment.imgs.length>0">
            <el-carousel-item v-for="(img,index) in comment.imgs" :key="index">
              <img :src="img" height="100px" />
            </el-carousel-item>
          </el-carousel>
        </el-col>
      </el-row>
      <el-row>
        <el-divider></el-divider>
      </el-row>
      <el-row>
        <p v-for="line in lines" :key="line">{{line}}</p>
      </el-row>
    </el-card>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";
import moment from "moment";
const logo = require("../assets/logo.png");
class Comment {
  public static FromID(id: number): Comment {
    return new Comment(id, "无名氏", new Date(), "暂无内容", []);
  }
  public static Default(): Comment {
    return new Comment(-1, "无名氏", new Date(), "这是第一行\n这是第二行", [
      logo,
      logo,
      logo
    ]);
  }
  constructor(
    public id: number,
    public username: string,
    public time: Date,
    public content: string,
    public imgs: string[]
  ) {
    console.log(`comment ${id} init`);
  }
}
export { Comment };
@Component({})
class ContentCard extends Vue {
  @Prop()
  private comment!: Comment;
  private lines: string[];
  constructor() {
    super();
    this.lines = this.comment.content.split("\n");
  }
  get time(): string {
    return moment(this.comment.time).format("YYYY-MM-DD HH:mm:ss");
  }
}

export default ContentCard;
</script>

<style scpped>
p {
  font-size: 10px;
  text-align: left;
}
</style>