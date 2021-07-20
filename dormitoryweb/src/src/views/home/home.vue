<template>
  <!-- 顶部导航栏 start  -->
  <div class="home-nav">
    <!-- 顶部导航栏左侧logo和标题  -->
    <div
      style="
        box-sizing: border-box;
        border-bottom: 1px solid rgb(240, 240, 240);
        height: 48.38px;
        font-weight: bold;
        font-size: 24px;
        margin-left: 0px;
        flex: auto;
      "
    >
      <img
        style="height: 30px; vertical-align: middle"
        src="/favicon.ico"
      />
      <span style="vertical-align: middle">宿易生</span>
    </div>

    <!-- 顶部导航栏 导航菜单 
    <a-menu mode="horizontal" @select="selectMenu">
      <a-sub-menu v-for="menu in menus" :key="menu.id">
        <template #title>
          {{ menu.name }}
        </template>
        <a-menu-item v-for="child in menu.children" :key="child.url">{{
          child.name
        }}</a-menu-item>
      </a-sub-menu>
    </a-menu> -->

    <!-- 顶部导航栏 最右侧 当前用户信息和菜单  -->
    <a-menu
      mode="horizontal"
      style="margin-right: 10px"
      @select="selectCurrUser"
    >
      <a-sub-menu>
        <template #title>
          <a-avatar
            v-if="currImageUrl"
            @click="setAvatar"
            :src="currImageUrl"
            style="vertical-align: middle"
          />
          <span
            v-else
            @click="setAvatar"
            style="
              vertical-align: middle;
              font-size: 14px;
              color: #aa0000;
              cursor: pointer;
            "
            >设置头像</span
          >
          <span>&nbsp;&nbsp;</span>
          <span style="vertical-align: middle"
            >{{ currUser.userId }}【{{ currUser.userName }}】</span
          >
        </template>
        <a-menu-item key="people">个人信息</a-menu-item>
        <a-menu-item key="password">密码修改</a-menu-item>
        <a-menu-item key="exit">退出系统</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </div>
  <!-- 顶部导航栏 end  -->

  <!-- 主体部分 start -->
  <div style="padding: 10px; min-width: 1024px; min-height: 600px">
    <router-view></router-view><!-- 路由视图，用于显示home页面内部视图组件 -->
  </div>
  <!-- 主体部分 end -->

  <!-- 设置头像弹框 -->
  <a-modal   v-model:visible="visible" title="设置新头像" @ok="execUpload">
    <a-upload
      list-type="picture-card"
      class="avatar-uploader"
      :show-upload-list="false"
      :before-upload="() => false"
      @change="handleChange"
    >
      <img v-if="imageUrl" :src="imageUrl" />
      <div v-else>
        <plus-outlined />
        <div class="ant-upload-text">选择文件或拖动至此处</div>
      </div>
    </a-upload>
  </a-modal>

</template>

<script setup>

import { reactive, ref } from "vue";

import { useRoute, useRouter } from "vue-router";

import { get, del, post, getBlob, tip, removeLocalToken } from "@/common";


const router = useRouter();


const currUser = ref({});
const getCurrUser = () => {
  get("/common/get_curr-user",{},{loading:false}).then((res) => {
    currUser.value = res.data;
    getAvatar();
  });
};

getCurrUser();

const currImageUrl = ref("");
const getAvatar = () => {
  if (!currUser.value || !currUser.value.avatar) {
    return;
  }
  getBlob("/common/avatar/" + currUser.value.avatar,{},{loading:false}).then((blob) => {
    const reader = new FileReader();
    reader.addEventListener("load", () => {
      currImageUrl.value = reader.result;
    });
    reader.readAsDataURL(blob);
  });
};


const selectMenu = ({ item, key, selectedKeys }) => {
  console.log(item, key, selectedKeys);
  router.push({ path: key });
};


const selectCurrUser = ({ item, key, selectedKeys }) => {
  switch (key) {
    case "exit": //退出系统
      del("/common/exit").then((res) => {
        tip.success("您已成功退出系统，请重新登录！");
        removeLocalToken();
        router.push({ path: "/security/login" });
      });
      break;
    case "password"://修改密码简易版
      router.push({ path: "/security/changePwd"})
      break;

  }
};


const avatarFile = ref(null);
const imageUrl = ref("");
const visible = ref(false);

const setAvatar = () => {
  visible.value = true;
};

const handleChange = (info) => {
  avatarFile.value = info.file;
  const reader = new FileReader();
  reader.addEventListener("load", () => {
    imageUrl.value = reader.result;
  });
  reader.readAsDataURL(info.file);
};


const execUpload = () => {
  if (!avatarFile.value) {
    tip.error("请选择头像！");
    return;
  }
  const formData = new FormData();
  formData.append("avatar", avatarFile.value);
  post("/common/avatar", formData).then((res) => {
    tip.success("头像设置成功！");
    visible.value = false;
    getCurrUser();
  });

};

  const route = useRoute();
  setTimeout(()=>{

    const jsonStr = decodeURIComponent(route.query.data)
    const obj = JSON.parse(jsonStr);


    console.log(obj);


  },1000)



</script>

<style>
.home-nav {
  min-width: 1024px;

  display: flex;
  align-items: center;
}

/* 上传头像样式 */
.avatar-uploader {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}
.avatar-uploader > .ant-upload {
  width: 300px;
  height: 300px;
}
.avatar-uploader img {
  max-width: 300px;
  max-height: 300px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>