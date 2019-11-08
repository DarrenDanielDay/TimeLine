<template>
  <div>
    <el-card>
      <el-divider></el-divider>
    </el-card>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

class Comment {
  constructor(
    public id: Number,
    public username: String,
    public time: Date,
    public content: String
  ) {
    console.log(`comment ${id} init`);
  }
  static FromID(id:Number):Comment{
      return new Comment(id,"无名氏",new Date(),"暂无内容");
  }
}


export default Vue.extend({
  props: {
    comment: {
      type: Comment,
      default: Comment.FromID(0)
    }
  }
});
</script>

<style>
</style>