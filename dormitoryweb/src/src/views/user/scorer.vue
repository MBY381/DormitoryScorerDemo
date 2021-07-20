<template>
<div id="menu">
<a-menu
    id="dddddd"
    style="width: 178px;
    height: 100%;
    position:relative;
    top:15px;"
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
  <a-menu-item key="3" >
        <span>单独增加记录</span>
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
    top:50px;">
      <a-page-header
        style="margin-left:0px;"
        title="选择周次"
        sub-title="在下方输入评分周次得到待评分表单"
      />
      <a-input v-model:value="getObj.sr_weekno" placeholder="请输入周次" style="width:120px;margin-left:22px;" />
      <a-button  @click="changeWeekno" style="margin-left:10px;">确认</a-button>
 <br>
 <br>
    <a-table
          :columns="columns1"
          :data-source="page1.list"
          :pagination="pagination1"
          @change="handleTableChange1"
          bordered
          style="margin-left:20px;"
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
      top:50px;">
    <a-page-header
     style="margin-left:0px;"
     title="历史记录"
     sub-title="所有有效记录"
    />
     <br>  
     <a-table
        :columns="columns"
        :data-source="page.list"
        :pagination="pagination"
        @change="handleTableChange"
        style="margin-left:20px;"
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


<!-- 单独增添记录窗口 -->
<a-modal v-model:visible="visible6" title="单独增添记录" @ok="temp">
    <a-form :model="addObj"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 14 }"
      >
      <a-form-item label="宿舍号">
        <a-input v-model:value="addObj.dorm_id" style="width: 300px;border-bottom: 1px solid rgb(240, 240, 240);" />
      </a-form-item>
      <a-form-item label="分数">
      
           <a-rate v-model:value="addObj.sr_score" :tooltips="desc1" allow-half :allow-clear="false" />
           <span class="ant-rate-text">{{ desc[addObj.sr_score*2 - 1] }}</span>
        
      </a-form-item>
      <a-form-item label="周次">
        <a-input v-model:value="addObj.sr_weekno" style="width: 300px;border-bottom: 1px solid rgb(240, 240, 240);" />
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea v-model:value="addObj.sr_remark" style="width: 500px;border-bottom: 1px solid rgb(240, 240, 240);" :rows="3"/>
      </a-form-item>
    </a-form>
</a-modal>

</template>


<script setup>

//非最终版


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
      },
      {
        dataIndex: "sr_weekno", 
        title: "周次",
      },
      {
        key: "opr1",
        title: "评分",
        slots: {
          customRender: "opr1", 
        },
      },
      {
        key: "opr2",
        title: "备注", 
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
      pageSize: 6, 
      total: 0, 
      pageSizeOptions: ["5","6", "10", "20"], 
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
  if(key==3){
    tip.success("已扩展单独记录界面")
    visible6.value = true;
  }

};
const excRecordList= () =>{
  visible3.value = false;
  console.log(visible3.value);
  getPage1(1);
}



//处理单个记录提交
const execSingleUpd = (score) =>{
  addObj.dorm_id = score.dorm_id;
  addObj.sr_score = score.sr_score*2;
  console.log(addObj.sr_score);
  addObj.sr_remark = score.sr_remark;
  addObj.sr_weekno = score.sr_weekno;

  post("/scorer/setscorerecord/single",addObj).then((res)=>{

    tip.success("提交成功！");
    getPage1(page1.value.current);
    addObj.sr_score = 3;
  })

}


//分页评分提交
const uploadList = () => {

  getPage1(1);
  console.log(page1.list);
  post("/scorer/setscore",page1.list).then((res)=>{
    tip.success("上传成功！");
    getPage1(1);
  })
  
}




//分页历史记录表
const currPage = ref("score");
const columns = [
      {
        dataIndex: "dorm_id",
        title: "宿舍号", 
      },
      {
        dataIndex: "sr_weekno", 
        title: "周次", 
      },
      {
        dataIndex: "sr_score", 
        title: "该周分数", 
      },
      {
        dataIndex: "sr_remark", 
        title: "备注",
      },
      {
        dataIndex: "scorer_id",
        title: "评分人", 
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
    getPage(1); 


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
