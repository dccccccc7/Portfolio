<template>
  <div id="ProductionSchedule">
    <div id="searchBar">
      <form style="width: 400px;float: left">
        <label style="float: left;margin-left: 20px;font-size: 20px">
          请选择日期：
          <input type="date" v-model="currentDate" style="font-size: 20px" @input="getDemand_ids(currentDate,company_id)"></input>
        </label>
      </form>
      <select style="width: 400px;height:34px;font-size: 20px;float: left" v-model="demand_id" @change="getData(demand_id)">
        <option>请选择客户需求订单编号</option>
        <option v-for="item in demand_ids">{{item}}</option>
      </select>
    </div>
    <div id="canvas" style="width: 800px;height:500px;margin-top: 30px"></div>

  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "ProductionSchedule",
  data(){
    return {
      currentDate: '',
      demand_ids: [],
      demand_id: '请选择客户需求订单编号',
      categorys: ['A','B'],
      realCounts: [1,2],
      planCounts: [1,2],
      product_ids:[],
      company_id:this.$store.getters.userinfo.company_id

    }
  },
  mounted() {
    this.drawChart();
  },
  methods: {
    getDemand_ids(date,company_id){
      this.demand_id = '请选择客户需求订单编号'
      this.categorys = []
      this.realCounts = []
      this.planCounts = []
      this.product_ids = []
      this.drawChart()
      request({
        url: '/dataAnalysis/getDemandFormNosByDate',
        method: 'get',
        params: {
          date: date,
          company_id:company_id
        }
      }).then(res => {
        this.demand_ids = res.data
        console.log(this.demand_ids)
      }).catch(err =>{
        console.log(err)
      })
    },
    //把值赋给category、realCount、planCount
    getData(demand_id){
      if (demand_id === '请选择客户需求订单编号'){
        this.categorys = []
        this.realCounts = []
        this.planCounts = []
        this.product_ids = []
        this.drawChart()
      } else {
        request({
          url: '/dataAnalysis/getFinishInfoById',
          method: 'get',
          params: {
            demand_id: demand_id
          }
        }).then(res => {
          this.categorys = res.data.categorys
          this.realCounts = res.data.realCounts
          this.planCounts = res.data.planCounts
          this.product_ids = res.data.product_ids
          console.log(this.product_ids)
          this.drawChart()
        }).catch(err =>{
          console.log(err)
        })
      }

    },
    drawChart() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.getInstanceByDom(document.getElementById("canvas"))
      if (myChart == null) { // 如果不存在，就进行初始化
        myChart = this.$echarts.init(document.getElementById("canvas"));
      }
      // let myChart = this.$echarts.init(document.getElementById("canvas"));
      // 指定图表的配置项和数据
      let option = {
        title: {
          text: '生产计划进度',
          subtext: '需求单号：'+this.demand_id
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['实际生产数量', '计划生产数量']
        },
        toolbox: {
          show: true,
          feature: {
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['bar']},
            saveAsImage: {show: true}
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            data: this.categorys,
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '实际生产数量',
            type: 'bar',
            data: this.realCounts
          },
          {
            name: '计划生产数量',
            type: 'bar',
            data: this.planCounts
          }
        ]
      };
      
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option,true);
      myChart.on('click',  (param)=> {

        console.log(this.product_ids)
        var productid;
        console.log(param.dataIndex)
        productid = this.product_ids[param.dataIndex];
        console.log(productid)
        this.$router.push({
          path: '/productionPlanSchedule/' + productid,

        });

       });




    }

    },


  };

</script>

<style scoped>
*{
  margin: 0px;
  padding: 0px;
}
#ProductionSchedule {

}
#searchBar{
  padding-top: 20px;
  height: 50px;
}
#canvas{
  margin-left: 50px;
}
</style>
