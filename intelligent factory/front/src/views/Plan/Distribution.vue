<template>
  <div id="distribution">
    <div>
      <h2>分配生产计划</h2>
      <h3>需求单</h3>
      <ul>
        <li>单号：{{ item.no }}</li>
        <li>产品编号：{{ item.product.id }}</li>
        <li>产品品牌：{{ item.product.brand}}</li>
        <li>产品季节：{{ item.product.season}}</li>
        <li>产品样式：{{ item.product.style }}</li>
        <li>产品颜色：{{ item.product.color }}</li>
        <li>数量：{{ item.quantity }}</li>
      </ul>
    </div>
    <div>
      <el-form :inline="true" label-width="90px">
        <el-form-item label="分配数量">
          <el-input type="number" v-model="quantity" min="1"></el-input>
        </el-form-item>
        <el-form-item label="生产线">
          <el-select v-model="selectProductionLine"  @change="getPlan">
            <el-option v-for="item in productionLine" :label="item.desc" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div>
        <el-button @click="commit">提交</el-button>
      </div>
    </div>
    <div>
      <el-table :data="plan" border style="margin-left:50px;width:1270px">
        <el-table-column prop="no" label="计划单单号"></el-table-column>
        <el-table-column prop="productionLine.id" label="生产线"></el-table-column>
        <el-table-column prop="expectedQuantity" label="分配数量"></el-table-column>
        
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="deletePlan(scope.row.id)">删除</el-button>
          </template>  
        </el-table-column>
      </el-table>
      
    </div>
    <div>
      <el-button @click="distribute">分配</el-button>
    </div>
    <div v-if="processesList[0]">
        <p>该生产线负载:共有{{processesList.length}}条</p>
        <el-table :data="processesList" border style="margin-left:50px;width:1270px">
          <el-table-column prop="plan.no" label="计划单单号"></el-table-column>
          <el-table-column prop="no" label="过程单单号"></el-table-column>
          <el-table-column prop="step" label="步骤"></el-table-column>
          <el-table-column prop="demandQuantity" label="预期数量"></el-table-column>
          <el-table-column prop="producedQuantity" label="已完成数量"></el-table-column>
        </el-table>
        
      </div>
  </div>
</template>

<script>
import request from "../../network/Plan/CommondForm.js";
import { user } from "../../network/Plan/variable.js";
import getProduct from "../../network/Plan/product.js";

export default {
  name: "Distribution",
  data() {
    return {
      item: this.$route.query,
      selectProductionLine: "",
      productionLine: [],
      processesList: [],
      plan: [],
      quantity: 0,
      procedureCount: 0,
    };
  },
  watch:{
    '$route.query'(newval, oldval) {
      this.item = newval
    }
  },
  activated() {
    try{
      this.productionLine=[]
      request({
        url: "/demandForm/getLinesByProduct",
        params: {
          id: this.item.product.id
        },
      })
          .then((res) => {
            console.log('生产线')
            console.log(res)
            if(res.data.result.length==0){
              this.$message.error("该产品无对应生产线")
              return
            }
            for(let i = 0;i<res.data.result.length;i++){
              let line = res.data.result[i]
              let lineDesc = "车间名:"+line.workshopName+"  生产线名:"+line.name

              line.desc=lineDesc
              this.productionLine.push(line)
            }
            //console.log(this.productionLine);
          })
          .catch((err) => {

            console.log("请求生产线错误");
          });
      request({
        url: "/plan/getPlansByDemandForm",
        params: {
          id: this.item.id,
        },
      })
          .then((res) => {
            this.plan = res.data.result;
          })
          .catch((err) => {
            console.log("请求计划单出错");
          });
    }catch (e){
      this.$message.error('发生错误');
      console.log(e);
    }

    
    /*
    //原查询方法，因为新需求单页面有产品id，可少一次查询
    console.log('id为'+this.item.id)
    request({
      url: "/demandForm/getDemandFormById",
      params: {
        id: this.item.id,
      },
    })
      .then((res) => {
        console.log('查询需求单')
        console.log(res)
        //productionLine=[]
        let productId = res.data.result.product.id
        console.log('产品id为'+productId);
        request({
          url: "/demandForm/getLinesByProduct",
          params: {
            id: productId
          },
        })
          .then((res) => {
            console.log('生产线')
            console.log(res)
            for(let i = 0;i<res.data.result.length;i++){
              let line = res.data.result[i]
              let lineDesc = "车间名:"+line.workshopName+"  生产线名:"+line.name
              
              line.desc=lineDesc
              this.productionLine.push(line)
            }
            //console.log(this.productionLine);
          })
          .catch((err) => {

            console.log("请求生产线错误");
          });
      })
      .catch((err) => {

        console.log("请求需求单错误");
      });
    request({
      url: "/plan/getPlansByDemandForm",
      params: {
        id: this.item.id,
      },
    })
      .then((res) => {
        this.plan = res.data.result;
      })
      .catch((err) => {
        console.log("请求计划单出错");
      });
    */

  },
  created() {
    try{
      this.productionLine=[]
      request({
        url: "/demandForm/getLinesByProduct",
        params: {
          id: this.item.product.id
        },
      })
          .then((res) => {
            console.log('生产线')
            console.log(res)
            if(res.data.result.length==0){
              this.$message.error("该产品无对应生产线")
              return
            }
            for(let i = 0;i<res.data.result.length;i++){
              let line = res.data.result[i]
              let lineDesc = "车间名:"+line.workshopName+"  生产线名:"+line.name

              line.desc=lineDesc
              this.productionLine.push(line)
            }
            //console.log(this.productionLine);
          })
          .catch((err) => {

            console.log("请求生产线错误");
          });
      request({
        url: "/plan/getPlansByDemandForm",
        params: {
          id: this.item.id,
        },
      })
          .then((res) => {
            this.plan = res.data.result;
          })
          .catch((err) => {
            console.log("请求计划单出错");
          });
    }catch (e){
      this.$message.error('发生错误');
      console.log(e);
    }
  },


  methods: {
    deletePlan(val) {
      if (!confirm("你要删除？")) return;

      request({
        url: "/plan/deletePlan",
        params: {
          id: val,
        },
      })
        .then((res) => {
          console.log(res);
          // this.$parent.step();
          request({
            url: "/plan/getPlansByDemandForm",
            params: {
              id: this.item.id,
            },
          })
            .then((res) => {
              this.plan = res.data.result;
            })
            .catch((err) => {
              console.log("错误");
            });
        })
        .catch((err) => {
          console.log("错误");
        });
    },
    distribute() {
      let count = 0;
      for (let i = 0; i < this.plan.length; i++) {
        count = count + this.plan[i].expectedQuantity;
      }

      if (this.item.quantity - count != 0) {
        alert("分配未完成");
        return;
      }

      if (!confirm("确认提交？")) return;

      getProduct({
        url: "/getRoutingInfosByID",
        params: {
          product_id: this.item.product.id,
        },
      })
        .then((res) => {
          let date = new Date();
          this.procedureCount = res.data.length;
          request({
            url: "/plan/distributeDemand",
            params: {
              procedureCount: this.procedureCount,
              id: this.item.id,
              dateStr:
                date.getFullYear() +
                (date.getMonth() + 1).toString().padStart(2, "0") +
                date.getDate().toString().padStart(2, "0"),
            },
          })
            .then((res) => {
              console.log("wafdsfr");
              this.$router.push("/commondform")
              // this.$router.back(-1)
              this.$router.go(0)
              // this.$parent.step()
            })
            .catch((err) => {
              console.log("111");
            });
        })
        .catch((err) => {
          console.log("2222");
        });
    },
    getPlan() {
      console.log(this.selectProductionLine)
      try {
        request({
          url: "/plan/queryLoad",
          params: {
            id: this.selectProductionLine,
          },
        })
            .then((res) => {
              console.log('选择的生产线为'+this.selectProductionLine);
              let planList = res.data.result;
              console(planList)
              for (let i = 0; i < planList.length; i++) {
                for (let j = 0; j < planList[i].processesList.length; j++) {

                  let process = planList[i].processesList[j];
                  if(process.demandQuantity == process.producedQuantity){
                    continue
                  }

                  if (!process.plan) {
                    process.plan = {};
                  }
                  process.plan.no = planList[i].no;
                  this.processesList.push(process);
                }
              }
            })
            .catch((err) => {
              console.log("请求计划单错误");
            });
      }catch (e) {
        console.log("发生错误");
        console.log(e);
      }

    },
    commit() {
      if (this.selectProductionLine == "" || this.quantity < 1) {
        alert("数据未填完整");
        return;
      }
      let count = 0;
      for (let i = 0; i < this.plan.length; i++) {
        count = count + this.plan[i].expectedQuantity;
      }
      if (this.quantity > this.item.quantity - count) {
        alert("填写数量过多");
        return;
      }

      let date = new Date();

      request({
        url: "/plan/savePlan",
        params: {
          expectedQuantity: this.quantity,
          "demandForm.id": this.item.id,
          "productionLine.id": this.selectProductionLine,
          no:
            "18" +
            date.getFullYear() +
            (date.getMonth() + 1).toString().padStart(2, "0") +
            date.getDate().toString().padStart(2, "0") +
            Date.now().toString().slice(-8),
          status: "0",
          deleted: "0",
          createdBy: user,
          company_id:this.$store.getters.userinfo.company_id
        },
      })
        .then((res) => {
          console.log(res);
          // this.$parent.step();
          //this.$router.go(0)
          request({
            url: "/plan/getPlansByDemandForm",
            params: {
              id: this.item.id,
            },
          })
            .then((res) => {
              console.log(res);
              this.plan = res.data.result;
            })
            .catch((err) => {
              console.log("错误");
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style>
  ul{
    list-style: none;
  }
</style>
