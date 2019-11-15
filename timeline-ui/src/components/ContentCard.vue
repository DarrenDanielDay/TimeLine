<template>
  <div>
    <el-card>
      <el-row class="info-row">
        <el-col :span="6" class="info-col">
          <el-avatar :src="this.comment.user.userAvatar"></el-avatar>
        </el-col>
        <el-col :span="6" class="info-col">
          <p>{{ this.comment.user.username }}</p>
        </el-col>
        <el-col :span="12" class="info-col">
          <p>{{ this.time }}</p>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-carousel
            height="100px"
            indicator-position="outside"
            v-if="this.comment.images.length > 0"
          >
            <el-carousel-item
              v-for="(image, index) in this.comment.images"
              :key="index"
            >
              <img :src="image" :alt="image" height="100px" />
            </el-carousel-item>
          </el-carousel>
        </el-col>
      </el-row>
      <el-row>
        <el-divider></el-divider>
      </el-row>
      <el-row>
        <el-col>
          <p v-for="(line, index) in this.lines" :key="index">{{ line }}</p>
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
import { urlBase } from "../App.vue";

// const urlBase = "https://ecnuer996.cn/timeline/";
const logo = require("../assets/logo.png");

class User {
  public static allUsers: { [key: string]: User } = {};
  public username: string = "";
  public userAvatar: string = "";
  constructor(username: string, userAvatar: string) {
    this.username = username;
    this.userAvatar = userAvatar;
    // this.getInfo();
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
              const userAvatar = json.avatar;
              User.allUsers[username] = new User(username, userAvatar);
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
    id: number;
    postTime: string;
    content: string;
    images?: string[] | undefined;
  }): Comment {
    return new Comment(
      json.nickname,
      json.avatar,
      json.id,
      json.postTime,
      json.content,
      json.images ? json.images : []
    );
  }

  public static Default(): Comment {
    return new Comment(
      "",
      logo,
      -1,
      new Date().toString(),
      "这是第一行\n这是第二行",
      [logo, logo, logo]
    );
  }

  public user: User;

  constructor(
    nickname: string,
    avatar: string,
    public id: number,
    public time: string,
    public content: string,
    public images: string[]
  ) {
    console.log(`comment ${nickname} init`);
    this.user = new User(nickname, avatar);
  }
}

export { Comment, User };

@Component({})
class ContentCard extends Vue {
  private lines!: string[];
  @Prop()
  private comment!: Comment;

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

<style scoped>
p {
  font-size: 10px;
  text-align: left;
}
.info-col {
  display: flex;
  justify-content: left;
  align-items: center;
}
.info-row {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
