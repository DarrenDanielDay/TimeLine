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
  public static allUsers: { [key: string]: User } = {};
  public username: string = "";
  public useravatar:string = "";
  constructor(username: string,useravatar:string) {
    this.username = username;
    this.useravatar = useravatar;
    this.getInfo();
  }

  public getInfo() {
    if (this.username === "") {
      this.username = "无名氏";
      return;
    }
    if (!(this.username in User.allUsers)) {
      Axios.get(`${urlBase}all-user`)
        .then(response => {
          for (let i in response.data) {
            const json = response.data[i];
            if (!(json.nickname in User.allUsers)) {
              const username = json.nickname;
              const useravatar = json.avatar;
              let user = new User(username,useravatar);
              User.allUsers[username] = user;
            }
          }
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
    nickname: string;
    avatar: string;
    id:number;
    postTime: string;
    content: string;
    images?: string[] | undefined;
  }): Comment {
    let date = moment(json.postTime).toDate();
    return new Comment(
      json.nickname,
      json.avatar,
      json.id,
      date,
      json.content,
      json.images ? json.images : []
    );
  }

  public static Default(): Comment {
    return new Comment("", logo,-1, new Date(), "这是第一行\n这是第二行", [
      logo,
      logo,
      logo
    ]);
  }

  public user: User;

  constructor(
    nickname: string,
    avatar: string,
    public id:number,
    public time: Date,
    public content: string,
    public images: string[]
  ) {
    console.log(`comment ${nickname} init`);
    this.user = new User(nickname,avatar);
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