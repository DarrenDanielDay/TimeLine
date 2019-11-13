<template>
  <div>
    <el-card>
      <el-row>
        <el-col :span="12">
          <p>{{this.comment.user.username}}</p>
        </el-col>
        <el-col :span="12">
          <p>{{this.time}}</p>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-carousel
            height="100px"
            indicator-position="outside"
            v-if="this.comment.images.length>0"
          >
            <el-carousel-item v-for="(image,index) in this.comment.images" :key="index">
              <img :src="image" height="100px" />
            </el-carousel-item>
          </el-carousel>
        </el-col>
      </el-row>
      <el-row>
        <el-divider></el-divider>
      </el-row>
      <el-row>
        <el-col>
           <p v-for="(line, index) in this.lines" :key="index">{{line}}</p>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";
import Axios from "axios";
import moment from "moment";
import { app } from "../main";

const urlBase = "https://ecnuer996.cn/timeline/";
const logo = require("../assets/logo.png");

class User {
  public static allUsers: { [key: number]: User } = {};
  public username: string = "";

  constructor(public id: number) {
    this.getInfo();
  }

  public getInfo() {
    if (this.id === -1) {
      this.username = "无名氏";
      return;
    }
    if (this.id in User.allUsers) {
      this.username = User.allUsers[this.id].username;
    } else {
      Axios.get(`${urlBase}all-user`)
        .then(response => {
          for (let i in response.data) {
            const json = response.data[i];
            if (!(json.id in User.allUsers)) {
              const id = json.id;
              let user = new User(id);
              user.username = json.nickname;
              User.allUsers[id] = user;
            }
          }
          this.username = User.allUsers[this.id].username;
        })
        .catch(error => {
          console.error(error);
          app.$message({
            message: error,
            type: "error"
          });
        });
    }
  }
}

class Comment {
  public static Parse(json: {
    id: number;
    userId: number;
    time: string;
    content: string;
    images?: string[] | undefined;
  }): Comment {
    let date = moment(json.time).toDate();
    return new Comment(
      json.id,
      json.userId,
      date,
      json.content,
      json.images ? json.images : []
    );
  }

  public static Default(): Comment {
    return new Comment(-1, -1, new Date(), "这是第一行\n这是第二行", [
      logo,
      logo,
      logo
    ]);
  }

  public user: User;

  constructor(
    public id: number,
    uid: number,
    public time: Date,
    public content: string,
    public images: string[]
  ) {
    console.log(`comment ${id} init`);
    this.user = new User(uid);
  }
}

export { Comment, User };

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