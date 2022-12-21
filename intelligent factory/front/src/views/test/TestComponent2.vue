<template>
  <div id="tc2">
    <h1>mockjs测试</h1>
    <div>mock数据方法：</div>
    <div>1 在mock目录下面创建相关js文件，例如mock/systemManagement.js，在自己的js文件里编写测试数据</div>
    <div>2 mock/index.js里注册你自己创建的js文件(例如systemManagement.js)，</div>
    <div>----> 开头加上 const test = require('./systemManagement')</div>
    <div>----> 修改其中一行为 const mocks = [...test, ...systemManagement]</div>
    <div>3 使用axios观察是否能够访问测试数据</div>
    <el-button @click="getData">刷新数据</el-button>
    <div v-for="(item,index) in testData" :key="item.name">{{ item.name }} - {{ item.age }} - {{ item.sex }}</div>
  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "TestComponent2",
  data() {
    return {
      testData: []
    }
  },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      request({
        url: 'data/testApi1',
        method: 'get',
      }).then(res => {
        let {code, data} = res.data;
        console.log(data);
        this.testData = data.userList;
      }).catch(err => {
        console.log("fail")
      });
    }
  }
}
</script>

<style scoped>
#tc2 {
  text-align: left;
  padding-left: 50px;
}
</style>
