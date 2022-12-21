<template>
  <div id="ProductionPLanSchedule">
    <el-row>
      <el-button @click="getData(product_id)">更新</el-button>
    </el-row>
    <div id="canvas" style="width: 800px;height:500px;margin-top: 30px"></div>
  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "ProductionPlanSchedule",
  data(){
    return {

      product_id:this.$route.params.id,

      plan_nos: ['A','B'],
      realCounts: [1,2],
      planCounts: [1,2],
      plan_ids:[]

    }
  },
  mounted() {
    this.drawChart();
  },
  methods: {

    //把值赋给category、realCount、planCount
      getData(product_id){

        if (product_id=== 'undefined'){console.log(product_id)
      this.product_id = "空"
      this.plan_nos = []
      this.realCounts = []
      this.planCounts = []
      this.plan_ids =[]
      this.drawChart()
      } else {
        request({
          url: '/dataAnalysis/getPlanByProductId',
          method: 'get',

          params: {
            product_id: this.$route.params.id
          }
        }).then(res => {

          this.plan_ids = res.data.plan_ids
          this.plan_nos = res.data.plan_nos
          this.realCounts = res.data.realCounts
          this.planCounts = res.data.planCounts
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
          text: '产品生产计划进度',
          subtext: '产品号：'+this.product_id
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
            data: this.plan_nos,
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
      myChart.setOption(option);
      myChart.on('click',  (param)=> {
       
        console.log(this.plan_ids)
        var planid;
        console.log(param.dataIndex)
        planid = this.plan_ids[param.dataIndex];
        console.log(planid)
        this.$router.push({
          path: '/productionProcessSchedule/' + planid,

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

#ProductionPLanSchedule{

}

#canvas{
  margin-left: 50px;
}
</style>
