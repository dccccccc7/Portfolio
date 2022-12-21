<!--
表单：用于增改瑕疵
-->

<template>
  <el-container>
    <el-header style="display: flex;height: 20%">
      <el-form :inline="true" :model="echartsForm" :rules="rulesForm" ref="echartsForm" class="demo-form-inline" style="width: 100%">
        <el-form-item label="日期" prop="date" style="float: left">
          <el-date-picker
              type="date"
              v-model="echartsForm.date"
              placeholder="选择一个日期"
              format="yyyy 年 MM 月 dd 日 "
              value-format="yyyy-MM-dd"
              :picker-options="pickerBeginDateBefore">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="计划单号" prop="workshop" style="float: left">
          <el-input  placeholder="计划单号" clearable v-model="echartsForm.workshop"></el-input>
        </el-form-item>
        <el-form-item label="生产线" prop="productionLine" style="float: left">
          <el-input  placeholder="生产线" clearable v-model="echartsForm.productionLine"></el-input>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" @click="onSubmit()">查询</el-button>
          <el-button @click="onCancel()">重 置</el-button>
        </el-form-item>
      </el-form>
<!--      <el-divider></el-divider>-->
    </el-header>
    <el-main style="height: 50%">
      <div style="width: 100%;height: 300px;background-color: #FFFFFF" id="container1"></div>
    </el-main>
    <el-footer style="display: inline-flex;height: 30%">
      <div style="width: 50%;height: 300px;border:1px solid rgb(180,180,180);margin-top: -20px" id="echartss"></div>
      <div style="width: 50%;height: 300px;border:1px solid rgb(180,180,180);margin-top: -20px" id="echartss2"></div>
    </el-footer>
  </el-container>
</template>

<script>

import {request} from "@/network/request";

export default {
  name: 'qualityEcharts',
  data() {
    return {
      echartsForm:{
        date:'',
        workshop:'',
        productionLine:'',
      },
      pickerBeginDateBefore:{
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      },
      rulesForm:{
        date:[
          {required: true, message: '请至少选择一个日期', trigger: 'change' }
        ],
        workshop: [
          {required: true, message: '请填写单号', trigger: 'blur' }
        ],
        productionLine: [
          {required: true, message: '请填写生产线', trigger: 'blur' }
        ]
      },
      option0:{
        backgroundColor: '#45515a',
        //标题
        title: {
          text: '瑕疵来源',
          left: 'center',
          top: 20,
          textStyle: {
            color: '#ccc',
            fontStyle:'italic'//标题字体
          }
        },
        //弹窗，响应鼠标指向，显示具体细节
        tooltip : {
          trigger: 'item',//以具体项目触发弹窗
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        //图例，选择要显示的项目
        legend:{
          orient:'vertical',
          left:'left',
          textStyle:{
            color:'#c8c8d0'
          },
          data:['先选择生产线']  //注意要和数据的name相对应
        },
        //工具箱
        toolbox:{
          show:true,//显示工具箱
          feature:{
            dataView:{show:true}, //以文字形式显示数据
            restore:{show:true},   //还原
            //dataZoom:{show:true}, //区域缩放
            saveAsImage:{show:true},  //保存图片
            //magicType:{type:['line','bar']}//动态数据切换，数据显示可以在该规定内容中切换显示方式，
          }
        },
        //视觉映射组件，将数据映射到视觉元素上
        visualMap: {
          show: false,
          min: 10,
          max: 650,
          dimension: 0, //选取数据的维度，如人数据：[身高，体重]，则1代表将体重进行映射，默认值为数组的最后一位
          // seriesIndex: 4, //选取数据集合中的哪个数组，如{一班}，{二班}，默认选取data中的所有数据集
          inRange: {
            //选定了要映射的对象，用inRange详细写要渲染的具体细节，[x，y]中x指最小值对应的量（亮度，饱和度等），y指最大值对应的量，其余的按各自value线性渲染
            color:['red'],
            colorLightness: [0.5,1],
            colorSaturation:[0.5,1]
          }
        },
        //数据
        series : [
          {
            name:'瑕疵来源',
            type:'pie',
            radius : '55%',
            center: ['50%', '50%'],
            data:[
              {value:100, name:'先选择生产线'},
            ],//.sort(function (a, b) { return a.value - b.value; }),
            roseType: 'radius',//角度和半径展现百分比，'area'只用半径展现
            label: { //饼图图形的文本标签
              normal: {  //下同，normal指在普通情况下样式，而非高亮时样式
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                }
              }
            },
            labelLine: {  //引导线样式
              normal: {
                lineStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                },
                smooth: 0.5, //0-1，越大越平滑弯曲
                length: 10,  //从块到文字的第一段长
                length2: 20  //拐弯到文字的段长
              }
            },
            itemStyle: { //图例样式
              normal: {
                color: '#97413c',
                shadowBlur: 50,//阴影模糊程度
                shadowColor: 'rgba(0, 0, 0, 0.5)'//阴影颜色，一般黑
              }
            },

            animationType: 'scale', //初始动画效果，scale是缩放，expansion是展开
            animationEasing: 'elasticOut', //初始动画缓动效果
            animationDelay: function (idx) {  //数据更新动画时长，idx限定了每个数据块从无到有的速度
              return Math.random() * 200;
            }
          }
        ]
      },
      option:{
        color: ['#2DC6C8', '#B6A2DD'],
        //标题
        title: {
          text: '不合格数目',
          left: 'center',
          top: 20,
          textStyle: {
            color: '#222',
            fontStyle:'italic'//标题字体
          }
        },
        // tooltip: { trigger: 'item', formatter: "{a} : {c}" },
        tooltip: {  },
        //右侧数据视图、折线图、还原、保存显示标志
        toolbox: {
          feature: {
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        legend: {
          bottom: '5',
          data: ['所有时间', '当前日期']
        },
        grid: {
          // top: '40'
        },
        xAxis: [
          {
            type: 'category',
            data: ['先选择计划单'],
            axisLine: { lineStyle: { color: '#7DABB0' }} // x轴刻度线颜色
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisLine: {
              lineStyle: { color: '#7DABB0' } // y轴坐标轴颜色
            },
            axisTick: {
              lineStyle: { color: '#7DABB0' } // y轴坐标刻度颜色
            }
          }
        ],
        series: [
          {
            name: '所有时间',
            type: 'bar',
            stack:'111',//堆叠
            barMaxWidth: '100',//柱状图最大宽度
            data: [50]
          },
          {
            name: '当前日期',
            type: 'bar',
            stack:'111',//堆叠
            barMaxWidth: '100',//柱状图最大宽度
            data: [50]
          }
        ]
      },
      option2:{
        tooltip: {              //设置tip提示
          trigger: 'axis'
        },

        legend: {               //设置区分（哪条线属于什么）
          data:['平均不良率','当前日期不良率']
        },
        // 设置网格样式
        grid: {
          left: '0%',
          top: '15%',
          right: '8%',
          bottom: '3%',
          show: true, // 显示边框
          boderColor: '#012f4a', // 边框颜色
          containLabel: true
        },
        color: ['#8AE09F','#FA6F53'],       //设置区分（每条线是什么颜色，和 legend 一一对应）
        xAxis: {                //设置x轴
          type: 'category',
          boundaryGap: false,     //坐标轴两边不留白
          data: ['1', '2', '3', '4', '5', '6', '7','8'],
          name: '生产天数',           //X轴 name
          nameTextStyle: {        //坐标轴名称的文字样式
            color: '#FA6F53',
            fontSize: 16,
            padding: [0, 0, 0, 20]
          },
          axisLine: {             //坐标轴轴线相关设置。
            lineStyle: {
              color: '#FA6F53',
            }
          }
        },
        yAxis: {
          name: '不良率',
          nameTextStyle: {
            color: '#FA6F53',
            fontSize: 16,
            padding: [0, 0, 10, 0]
          },
          axisLine: {
            lineStyle: {
              color: '#FA6F53',
            }
          },
          type: 'value'
        },
        series: [
          {
            name: '平均不良率',
            data:  [0, 0, 0, 0, 0, 0, 0,0],
            type: 'line',               // 类型为折线图
            lineStyle: {                // 线条样式 => 必须使用normal属性
              normal: {
                color: '#8AE09F',
              }
            },
          },
          {
            name: '当前日期不良率',
            data: [0, 0, 0, 0, 0, 0, 0,0],
            type: 'line',
            lineStyle: {
              normal: {
                color: '#FA6F53',
              }
            },
          }
        ]
      },
      myChart0:'',
      myChart:'',
      myChart2:'',
    }
  },
  mounted() {
    this.loadEcharts()
  },
  methods: {
    loadEcharts() {
      this.myChart0 = this.$echarts.init(document.getElementById('echartss'))
      this.myChart = this.$echarts.init(document.getElementById('container1'))
      this.myChart2 = this.$echarts.init(document.getElementById('echartss2'))
      this.myChart0.setOption(this.option0)
      this.myChart.setOption(this.option2)
      this.myChart2.setOption(this.option)
    },
    validateForm() {
      this.$refs['echartsForm'].validate((valid) => {
        if (valid) {
          const date = this.echartsForm.date;
          const workshop = this.echartsForm.workshop;
          const productionLine = this.echartsForm.productionLine;
          request({
            url:'/quality/echarts?keywords=' + date +'~'+workshop+'~'+productionLine,
            methods: 'get',
          }).then(resp => {
            this.$emit('onSubmit')
            let data =resp.data;
            //设置折线图数据：平均合格率和当前日期合格率
            this.option2.series[0].data =data[0];
            this.option2.series[1].data =data[1];

            let legendData=[], seriesData=[{}];
            let seriesDataA = [], seriesDataB = [];
            //设置饼图：当前生产线的瑕疵组成
            for(let i=0; i<data[3].length; i++){
              const temp = data[3][i].split("-");
              legendData.push(temp[0]);
              seriesData.push({
                value:parseInt(temp[1]),
                name:temp[0],
              });
              seriesDataA.push(parseInt(temp[1]));
            }
            this.option0.legend.data = legendData;
            this.option0.series[0].data = seriesData;

            //设置柱状图：
            for(let i=0; i<data[2].length; i++){
              const temp = data[2][i].split("-");
              seriesDataB.push(parseInt(temp[1]));
            }
            this.option.xAxis[0].data = legendData;
            this.option.series[0].data = seriesDataB;
            this.option.series[1].data = seriesDataA;
            this.loadEcharts()
          }).catch(err => {
            this.$message.error('网络出错，请稍后再试');
          });
        } else {
          return false;
        }
      });
    },
    resetDataForm() {
      this.$refs['echartsForm'].clearValidate();
      this.$refs['echartsForm'].resetFields();
    },
    onSubmit(){
      this.validateForm();
    },
    onCancel(){
      this.resetDataForm();
    },
  }
}
</script>

<style scoped>
.box-card1,.box-card2,.box-card3,.box-card4{
  width:22%;
  height:100%;
  margin-right: 3%;
}
.box-card5,.box-card6,.box-card7{
  width:30%;
  height:100%;
  margin-right: 3%;
}
</style>


