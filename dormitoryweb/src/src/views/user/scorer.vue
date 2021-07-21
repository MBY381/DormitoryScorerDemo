<template>
<div id="menu">
<a-menu
    id="dddddd"
    style="width: 178px;
    height: 100%;
    position:relative;
    top:10px;"
    v-model:openKeys="openKeys"
    v-model:selectedKeys="selectedKeys"
    mode="inline"
    @select="selectMenu1"
  >
  <a-menu-item key="1">
        <span>周次宿舍评分</span>
  </a-menu-item>
  <a-menu-item key="2" >
        <span>历史记录</span>
  </a-menu-item>
  </a-menu> 
  <br>
  <br>
</div>


<!-- 评分 -->
<div id="score" v-show="visible3" style="
    width: 85%;
    height: 85%;
    position:absolute;
    left:192px;
    top:43px;">
      <a-page-header
        style="margin-left:0px;"
        title="选择周次"
        sub-title="在下方输入评分周次得到待评分表单"
      />
      <a-input v-model:value="getObj.sr_weekno" placeholder="请输入周次" style="width:120px;margin-left:22px;" />
      <a-button  @click="changeWeekno" style="margin-left:10px;">确认</a-button>
      <span>
        <a-button  
        @click="openDrawer" 
        style="position:relative;
        left:950px;" type="danger" ghost>单独增加记录</a-button>
      </span>  
    <a-table
          :columns="columns1"
          :data-source="page1.list"
          :pagination="pagination1"
          @change="handleTableChange1"
          bordered
          style="margin-left:20px;
          position:relative;
          top:10px;"
          >
           <template #opr1="{ record }">
              <a-rate v-model:value="record.sr_score" allow-half :tooltips="desc1"></a-rate>
              <span class="ant-rate-text1">{{ desc[record.sr_score*2 - 1] }}</span>
           </template>
           <template #opr2="{ record }">
              <a-input v-model:value="record.sr_remark" placeholder="请输入备注" style="width: 300px;border-bottom: 1px solid rgb(240, 240, 240);" />
           </template>
           <template #opr3="{ record }">
              <a-button type="primary" @click="execSingleUpd(record)" ghost >提交</a-button>
           </template>
     </a-table>
</div>


<!-- 历史记录表 -->
<div id="record" v-show="visible4" style="
      width: 85%;
      height: 85%;
      position:absolute;
      left:192px;
      top:43px;">
    <a-page-header
     style="margin-left:0px;"
     title="历史记录"
     sub-title="所有有效记录"
    />
     <a-input-search
         v-model:value="dormIDtarget.dorm_id"
         placeholder="输入宿舍号搜索"
         style="width: 200px;
         position:relative;
         left:20px;"
         @search="searchAndUpdateList"
        />
     <a-table
     
        :columns="columns"
        :data-source="page.list"
        :pagination="pagination"
        @change="handleTableChange"
        style="margin-left:20px;
        position:relative;
        top:10px;" 
        bodered
       >
     <template #opr="{ record }">
        <a-button @click="openUpd(record)">修改</a-button>
        <a-divider type="vertical" />
        <a-button type="danger" @click="execDel(record)">删除</a-button>
     </template>
     </a-table>
</div>


<!-- 修改窗口 -->
<a-modal v-model:visible="visible2" title="修改记录信息" @ok="execChange">
    <a-form
      :model="changeObj"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 14 }"
    >
      <a-form-item label="新的分数">
        <a-input v-model:value="changeObj.sr_score"  />
      </a-form-item>
      <a-form-item label="新的备注">
        <a-textarea v-model:value="changeObj.sr_remark" :rows="3" />
      </a-form-item>
    </a-form>
</a-modal>


<!-- 单独增添记录抽屉 -->
<a-drawer
    title="单独增添记录 (需确保不重复)"
    :width="520"
    :visible="visible6"
    :body-style="{ paddingBottom: '80px' }"
    @close="onClose"
  ><a-form :model="addObj"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 14 }"
      >
      <a-form-item label="宿舍号">
        <a-input v-model:value="addObj.dorm_id" 
        style="width: 300px;border-bottom: 1px solid rgb(240, 240, 240);" 
        placeholder="输入宿舍号"/>
      </a-form-item>
      <a-form-item label="分数">
           <a-rate v-model:value="addObj.sr_score" :tooltips="desc1" allow-half :allow-clear="false" />
           <span class="ant-rate-text">{{ desc[addObj.sr_score*2 - 1] }}</span>
      </a-form-item>
      <a-form-item label="周次">
        <a-input v-model:value="addObj.sr_weekno" 
        style="width: 300px;border-bottom: 1px solid rgb(240, 240, 240);"
        placeholder="请输入分数周次" />
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea v-model:value="addObj.sr_remark" style="width: 500px;border-bottom: 1px solid rgb(240, 240, 240);" :rows="3"/>
      </a-form-item>
    </a-form>
    <a-button type="primary" @click="uploadAndCLose" >确定</a-button>
    <span>
      <a-button type="primary" @click="onClose" ghost style="margin-left:20px;">取消</a-button>
    </span>
</a-drawer>

</template>




<script setup>

import {  reactive, ref } from 'vue';
import { useRoute, useRouter } from "vue-router";
import { get, del, post, getBlob, tip, removeLocalToken } from "@/common";


//获取当前用户
const currUser = ref({});
const getCurrUser = () => {
  get("/common/get_curr-user").then((res) => {
    currUser.value = res.data;
  });
};

getCurrUser();


//分页评分表
const currPage1 = ref("score1")
const columns1 = [
      {
        dataIndex: "dorm_id", 
        title: "宿舍号",
        width:180,
        sorter: (a, b) => a.dorm_id - b.dorm_id,
        sortDirections: ['descend','ascend'],
      },
      {
        dataIndex: "sr_weekno", 
        title: "周次",
        width:180,
      },
      {
        key: "opr1",
        title: "评分",
        width:300,
        slots: {
          customRender: "opr1", 
        },
      },
      {
        key: "opr2",
        title: "备注", 
        width:450,
        slots: {
          customRender: "opr2",
        },
      },
      {
        key: "opr3",
        title: "  ", 
        slots: {
          customRender: "opr3",
        },
      },
    ];

const getObj=reactive({
  sr_weekno:1
})


//切换评分表的周次
const changeWeekno = () =>{
  post("/scorer/setweekno",getObj).then((res)=>{
    tip.success("已更改至第"+getObj.sr_weekno+"周评分表")
    getPage1(1)
  })
}



//评分表控制
const query1 = reactive({}); 
const page1 = ref({}); 
const pagination1 = reactive({

      current: 1, 
      pageSize: 7, 
      total: 0, 
      pageSizeOptions: ["5","7", "10", "20"], 
      showSizeChanger: true, 
      // showQuickJumper: true, 

      showTotal: (total, range) => {
        return (
          "总计" +
          total +
          "条记录,当前第[" +
          pagination1.current +
          "]页 共[" +
          (range[1] - range[0] + 1) +
          "]条记录"
        );
      },
    });


//获取评分表
const getPage1 = (pageNum = 1) => {
      query1.pageNum = pageNum;
      query1.pageSize = 5;
      console.log(query1.pageSize);
      get("/scorer/getdraftlist", query1).then((res) => {
        page1.value = res.data; 
        //更新分页
        pagination1.current = page1.value.current;
        pagination1.pageSize = page1.value.pageSize;
        pagination1.total = page1.value.total;
      });
    };

getPage1(1);

//评分表表换
const handleTableChange1 = (
      pagi1,
      filters,
      sorter,
      { currentDataSource }
    ) => {
      pagination1.current = pagi1.current;
      pagination1.pageSize = pagi1.pageSize;
      pagination1.total = pagi1.total;
      // getPage(pagination1.current);
    };



//菜单操作切换
const visible3 = ref(true);
const visible4 = ref(false);
const visible6 = ref(false);
const selectMenu1 = ({ item, key, selectedKeys }) => {
  console.log(item, key, selectedKeys);
  if(key==1){ 
    visible3.value = true;
    visible4.value = false;
    visible6.value = false;
    getPage1(1);
  }
  if(key==2){
    visible4.value = true;
    visible3.value = false;
    visible6.value = false;
    getPage(1);
  }
 
};




//处理单个记录提交
const execSingleUpd = (score) =>{
  addObj.dorm_id = score.dorm_id;
  addObj.sr_remark = score.sr_remark;
  addObj.sr_weekno = score.sr_weekno;
  console.log(score.sr_score);
  if(score.sr_score == null){
    tip.error("请指定分数");
    return;
  }
  addObj.sr_score = score.sr_score*2;
 
  post("/scorer/setscorerecord/single",addObj).then((res)=>{

    tip.success("提交成功！");
    getPage1(page1.value.current);
    addObj.sr_score = 3;
  })

}



//分页历史记录表
const currPage = ref("score");
const columns = [
      {
        dataIndex: "dorm_id",
        title: "宿舍号", 
        width:180,
        sorter: (a, b) => a.dorm_id - b.dorm_id,
        sortDirections: ['descend','ascend'],
      },
      {
        dataIndex: "sr_weekno", 
        title: "周次", 
        width:180,
        sorter: (a, b) => a.sr_weekno - b.sr_weekno,
        sortDirections: ['descend','ascend'],
      },
      {
        dataIndex: "sr_score", 
        title: "该周分数", 
        width:200,
      },
      {
        dataIndex: "sr_remark", 
        title: "备注",
        width:350,
      },
      {
        dataIndex: "scorer_id",
        title: "评分人", 
        width:150,
      },
      {
        key: "opr",
        title: "操作", 

        slots: {
          customRender: "opr", 
        },
      },
    ];


//历史记录表
const query = reactive({}); 
const page = ref({}); 
const pagination = reactive({
      current: 1, 
      pageSize: 7, 
      total: 0, 
      pageSizeOptions: ["5","7", "10", "20", "50"], 
      showSizeChanger: true, 
      // showQuickJumper: true, 

      showTotal: (total, range) => {
        return (
          "总计" +
          total +
          "条记录,当前第[" +
          pagination.current +
          "]页 共[" +
          (range[1] - range[0] + 1) +
          "]条记录"
        );
      },
    });


//记录表获取表元素
const getPage = (pageNum = 1) => {
      query.pageNum = pageNum;
      query.pageSize = pagination.pageSize;

      get("/scorer/getscore", query).then((res) => {
        page.value = res.data; 
        //更新分页
        pagination.current = page.value.current;
        pagination.pageSize = page.value.pageSize;
        pagination.total = page.value.total;
      });
    };  

//记录表更改
const handleTableChange = (
      pagi,
      filters,
      sorter,
      { currentDataSource }
    ) => {
      pagination.current = pagi.current;
      pagination.pageSize = pagi.pageSize;
      pagination.total = pagi.total;

      getPage(pagination.current);
    };


//修改记录部分
const visible2 = ref(false);
const changeObj = reactive({
      dorm_id: "",
      sr_score:0,
      sr_remark:"",
      scorer_id:"",
      sr_weekno:0
    });


//打开修改记录窗口
const openUpd = (score) => {
      changeObj.dorm_id = score.dorm_id;
      changeObj.sr_weekno = score.sr_weekno;
      changeObj.sr_score = score.sr_score;
      changeObj.sr_remark = score.sr_remark;
      //打开窗口
      visible2.value = true;
    };


//修改记录
const execChange = () => {
      //执行修改
      post("/scorer/updaterecord", changeObj).then((res) => {
        changeObj.dorm_id = "";
        changeObj.sr_weekno = "";
        changeObj.sr_score = 0;
        changeObj.sr_remark = "";
        visible2.value = false;
        tip.success("修改记录成功！");
        getPage(page.value.current);
      });
    };



//删除记录
const delObj = reactive({
  dorm_id:"",
  sr_weekno:0
})

const execDel = (score) =>{

  delObj.dorm_id = score.dorm_id;
  delObj.sr_weekno = score.sr_weekno;
  console.log(delObj.dorm_id);
  post("/scorer/deleterecord",delObj).then((res)=>{
    tip.success("已删除记录")
    delObj.dorm_id = "";
    delObj.sr_weekno = 0;
    getPage(page.value.current);
  })
}


//单次新增变量
const addObj = reactive({
  dorm_id:"",
  sr_score:3,
  sr_remark:"",
  scorer_id:"",
  sr_weekno:0
})

//单独提交记录窗口“ok”
const temp = () =>{
  // getCurrUser();
  console.log(currUser.value.userName);
  addObj.sr_score = addObj.sr_score;
  // addObj.scorer_id = currUser.value.userName;
  console.log(addObj.scorer_id);
  visible6.value = false;

  post("/scorer/setscorerecord/single",addObj).then((res) =>{
      tip.success("评分成功！");
      addObj.sr_score = 3;
      console.log("新增记录成功");
      getPage(page.value.current);
      getPage1(page1.value.current);
  })
}


const router = useRouter();


const route = useRoute();
  setTimeout(()=>{

    const jsonStr = decodeURIComponent(route.query.data)
    const obj = JSON.parse(jsonStr);


    console.log(obj);


  },1000)


//评分星星数据
const starvalue = ref(3);
const desc = ref(['  极差:1分', '  极差:2分','  差:3分','  较差:4分','  较差:5分', '  及格:6分', '  良好:7分', '  良好:8分','  优秀:9分','  优秀:10分']);
const desc1 = ref(['极差','较差','及格','良好','优秀'])

const onClose = () =>{
  addObj.sr_score = 3,
  visible6.value = false;
}

const openDrawer = () =>{
  addObj.dorm_id="",
  addObj.sr_score = 3;
  addObj.sr_remark = "NULL";
  addObj.sr_weekno = null;
    tip.success("已扩展单独增添记录界面");
    visible6.value = true;
}


const uploadAndCLose = () =>{
  if(addObj.dorm_id==""&&addObj.sr_weekno==null){
    tip.error("请输入完整记录！");
    return;
  }
  addObj.sr_score = addObj.sr_score*2;
  post("/scorer/setscorerecord/single",addObj).then((res)=>{
    tip.success("提交记录成功！");
    visible6.value=false;
    addObj.sr_score = 3;
  })
}

//历史记录搜索
const dormIDtarget =reactive({
  dorm_id:""
});

const searchAndUpdateList = () =>{
      // getPage(1);
      console.log("list:👇");
      console.log( page.value.list );
      console.log("搜索的宿舍号：↓");
      console.log(dormIDtarget.dorm_id);
      console.log("总记录条数：");
      console.log(page.value.total)
      var j=0;
      for(var i=0;i<page.value.pageSize;i++){
        console.log("第几条记录（j）：");
        console.log(j);
        console.log("此条记录值");
        console.log(page.value.list[j]);
        console.log("此条记录宿舍：");
        console.log(page.value.list[j].dorm_id);
        console.log("是否相等于搜索值：");
        console.log(page.value.list[j].dorm_id == dormIDtarget.dorm_id);
        if(page.value.list[j].dorm_id != dormIDtarget.dorm_id){
          page.value.list.splice(j,1);
          console.log("更新后的同位置记录：");
          console.log(page.value.list[j]);
          page.value.total = page.value.total-1;
          console.log("剩余总记录数");
          console.log(page.value.total);
          j=j-1;
        }
        j=j+1;
        console.log("查询到的记录数");
        console.log(j);
      }
      pagination.current = page.value.current;
      pagination.pageSize = page.value.pageSize;
      pagination.total = page.value.total;
     
    }




//尝试

</script>



<style>

#components-layout-demo-responsive .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}

.site-layout-sub-header-background {
  background: #fff;
}

.site-layout-background {
  background: #fff;
}

[data-theme='dark'] .site-layout-sub-header-background {
  background: #e2dfdf;
}

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
  justify-content: center;
}

.ant-upload-select-picture-card i {
  font-size: 32px;
  color: rgb(207, 40, 40);
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: rgb(0, 153, 69);
}

/* .addscore{

} */
</style>