<template>
  <div id="ProductionProcessSchedule">
    <el-row>
      <el-button @click="getData(plan_id)"type="primary">更新</el-button>
    </el-row>
    <div id="canvas" style="width: 800px;height:500px;margin-top: 30px"></div>

  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "ProductionSchedule",
  data(){
    return {
      plan_id:this.$route.params.id,

      process_names: ['A','B'],
      realCounts: [1,2],
      planCounts: [1,2],
      process_ids:[]

    }
  },
  mounted() {
    this.drawChart();
  },
  methods: {
    //把值赋给category、realCount、planCount
    getData(plan_id){

      if (plan_id=== 'undefined'){
        this.plan_id = "空"
        this.process_ids = []
        this.realCounts = []
        this.planCounts = []
        this.process_names = []
        this.drawChart()
      } else {
        request({
          url: '/dataAnalysis/getProcessByPlanId',
          method: 'get',

          params: {
            plan_id: this.$route.params.id
          }
        }).then(res => {

          this.process_ids = res.data.process_ids
          this.process_names = res.data.process_names
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
          text: '过程单完成情况',
          subtext: '计划单号：'+this.plan_id
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
            data: this.process_names,
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
    }
  },
};
</script>

<style scoped>
*{
  margin: 0px;
  padding: 0px;
}
#ProductionProcessSchedule {

}

#canvas{
  margin-left: 50px;
}

</style>
