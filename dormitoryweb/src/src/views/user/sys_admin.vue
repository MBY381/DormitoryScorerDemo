<template>
  系统管理员
  <a-form :model="n" >
    <a-form-item label="test">
      <a-input v-model:value="n" />
    </a-form-item>
  </a-form>
  <button @click="test">test 后端</button>
</template>

<script setup >
import {post} from "@/common";

const n='';

const test = ()=>{

  post('/user/sys_admin',n).then((res)=>{

  })

}
</script>

<style>

</style>