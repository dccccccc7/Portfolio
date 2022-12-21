<template>
  <div id="base">
    <el-card class="box-card" shadow="hover">
      <div class="header">
        <span>产品信息</span>
      </div>
      <el-divider style="margin: 10px 0px 10px 0px;"></el-divider>
      <div class="content">
<!--        <div>产品id：{{product_id}}</div>-->
        <div>品牌：{{product.brand}}</div>
<!--        <div>季节：{{product.season}}</div>-->
        <div>产品类别：{{product.type}}</div>
        <div>客户款号：{{product.customer_id}}</div>
        <div>公司款号：{{product.company}}</div>
        <div>颜色：{{product.color}}</div>
        <div>描述：{{product.comments}}</div>
      </div>
    </el-card>

    <el-card class="box-card" shadow="hover">
      <div class="header">
        <span>工艺流程</span>
      </div>
      <el-divider style="margin: 10px 0px 10px 0px;"></el-divider>
      <div class="content">
        描述：{{process.comments}}
      </div>
      <div class="content" v-for="(item, index) in process.data">
        步骤{{index+1}}：{{item.content}}
      </div>
    </el-card>

    <el-card class="box-card" shadow="hover">
      <div class="header">
        <span>物料清单</span>
      </div>
      <el-divider style="margin: 10px 0px 10px 0px;"></el-divider>
      <div class="content" v-for="(item, index) in material">
        物料{{index+1}}：{{item.name}} / {{item.size}} / {{item.color}}，需要{{item.count}}个
      </div>
    </el-card>
  </div>
</template>
import {request} from "@/network/request";
<script>
export default {
  name: "AddEnsure",
  props: ['product', 'process', 'material']
}
</script>

<style scoped>
.box-card {
  margin-bottom: 10px;
}

.header {
  font-weight: bolder;
  font-size: 20px;
}

.content {
  display: flex;
  flex-direction: column;
}
</style>
