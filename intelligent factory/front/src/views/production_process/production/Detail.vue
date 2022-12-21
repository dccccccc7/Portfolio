<template>
  <div id="detail-base">
    <div id="detail-product">
      <h2>产品信息</h2>
      <el-card>
        <div>产品id：{{product_id}}</div>
        <div>品牌：{{product.brand}}</div>
<!--        <div>季节：{{product.season}}</div>-->
        <div>产品类别：{{product.type}}</div>
        <div>客户款号：{{product.customer_id}}</div>
        <div>公司款号：{{product.company}}</div>
        <div>颜色：{{product.color}}</div>
        <div>描述：{{product.comments}}</div>
      </el-card>
    </div>

    <div id="detail-process">
      <h2>工艺流程</h2>
      <el-card>描述：{{process.comments}}</el-card>
      <el-card>成本：{{process.cost}}</el-card>
      <el-card v-for="(item, index) in process.data">
        <div style="font-weight: bold">步骤{{index+1}}：</div>
        <div>工序id：{{item.procedure_id}}</div>
        <div>名称：{{item.name}}</div>
        <div>描述：{{item.comments}}</div>
      </el-card>
    </div>

    <div id="detail-material">
      <h2>物料清单</h2>
      <el-card v-for="(item, index) in material">
        <div style="font-weight: bold">物料{{index+1}}：</div>
        <div>物料id：{{item.material_id}}</div>
        <div>名称：{{item.name}}</div>
        <div>属性：{{item.size}}</div>
<!--        <div>颜色：{{item.color}}</div>-->
        <div>数量：{{item.count}}</div>
        <div>描述：{{item.comments}}</div>
      </el-card>
    </div>
  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "detail",
  props: ['product_id'],
  data() {
    return {
      product: {},
      process: {},
      material: []
    }
  },
  methods: {
    getData() {
      request({
        url: '/process/getInfosByID',
        method: 'get',
        params: {
          product_id: this.product_id
        }
      }).then(res => {
        this.product = res.data.product
        this.process = res.data.process
        this.material = res.data.material
      }).catch(err => {
        console.log(err)
      });
    }
  },

  created() {
    this.getData()
  }
}
</script>

<style scoped>
#detail-base {
  overflow-y: auto;
  position: absolute;
  width: 100%;
  height: 85%;
}
#detail-product {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#detail-process {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#detail-process .el-card {
  margin-bottom: 5px;
}
#detail-material {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#detail-material .el-card {
  margin-bottom: 5px;
}
</style>