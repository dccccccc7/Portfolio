<template>
  <div id="process">
    <el-table :data="result" border style="margin-left:50px;width:1270px">
        <el-table-column prop="no" label="生产过程单号"></el-table-column>
        <el-table-column prop="step" label="步骤序号"></el-table-column>
        <el-table-column prop="materialArriveTime" label="生产开始时间" :formatter="timeFormat"></el-table-column>
        <el-table-column prop="demandQuantity" label="所需数量"></el-table-column>
        <el-table-column prop="producedQuantity" label="完成数量"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="!scope.row.materialArriveTime && underwaystep==scope.row.step" @click="begin(scope.row.id)">
              开始生产
            </el-button>
            <el-button disabled v-else-if="!scope.row.materialArriveTime " >
              开始生产
            </el-button>
            <div
              v-else-if="
                scope.row.materialArriveTime &&
                scope.row.demandQuantity > scope.row.producedQuantity
              "
            >
              <el-input type="number" min="1" v-model="count[scope.row.id]" />
              <el-button
                @click="
                  add(scope.row.id, scope.row.producedQuantity, scope.row.demandQuantity)
                "
              >
                添加
              </el-button>
            </div>
            <div v-else>已完工！！</div>
            
          </template>  
        </el-table-column>
      </el-table>
    
  </div>
</template>

<script>
import Vue from "vue";
import request from "../../network/Plan/CommondForm.js";

export default {
  name: "Process",
  data() {
    return {
      result: [],
      planId: this.$route.params.id,
      count: {},
      underwaystep:0,//正在进行生产或将要进行生产的工序号
    };
  },
  watch:{
    '$route.params'(newval, oldval) {
      this.planId = newval.id
    }
  },
   created() {

    console.log("created")
    request({
      url: "/process/getProcessesByPlan",
      params: {
        id: this.planId,
      },
    })
      .then((res) => {
        this.result = res.data.result;
        this.underwaystep=0;
        for (let i = 0; i < this.result.length; i++) {
          Vue.set(this.count, this.result[i].id, 0);
          if(this.result[i].materialArriveTime)this.underwaystep=i+1;
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  activated() {

    console.log("activated")
    request({
      url: "/process/getProcessesByPlan",
      params: {
        id: this.planId,
      },
    })
      .then((res) => {
        this.result = res.data.result;
        this.underwaystep=0;
        for (let i = 0; i < this.result.length; i++) {
          Vue.set(this.count, this.result[i].id, 0);
          if(this.result[i].materialArriveTime)this.underwaystep=i+1;
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    timeFormat(row,column){
      if(row.materialArriveTime){
        return row.materialArriveTime.slice(0,10)
      }else{
        return row.materialArriveTime
      }
      },
    begin(val) {
      //if (!confirm("确认收到物料了？")) return;
      
      request({
        url: "/process/materialArrive",
        params: {
          id: val,
        },
      })
        .then((res) => {
          console.log(res);
          request({
            url: "/process/getProcessesByPlan",
            params: {
              id: this.planId,
            },
          })
            .then((res) => {
              this.result = res.data.result;
              this.underwaystep=0;
              for (let i = 0; i < this.result.length; i++) {
                Vue.set(this.count, this.result[i].id, 0);
                if(this.result[i].materialArriveTime)this.underwaystep=i+1;
              }
            })
            .catch((err) => {
              console.log("错误");
            });
        })
        .catch((err) => {
          console.log("错误");
        });
    },
    add(val, produced, demand) {
      produced = Number(produced);
      demand = Number(demand);
      if (Number(this.count[val]) + produced > demand) {
        alert("数量过多");
        return;
      }

      if (!confirm("确定要增加：" + this.count[val])) return;

      request({
        url: "/process/increaseProduct",
        params: {
          id: val,
          count: this.count[val],
        },
      })
        .then((res) => {
          console.log(res);
          request({
            url: "/process/getProcessesByPlan",
            params: {
              id: this.planId,
            },
          })
            .then((res) => {
              this.result = res.data.result;
              for (let i = 0; i < this.result.length; i++) {
                Vue, set(this.count, this.result[i].id, 0);
                if(this.result[i].demandQuantity==this.result[i].producedQuantity)this.underwaystep=i+1;
              }
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>