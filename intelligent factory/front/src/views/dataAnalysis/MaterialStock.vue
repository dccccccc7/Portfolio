<template>
  <div id="MaterialStock">
    <el-form label-position="right">
      <div style="display: flex; flex-direction: row;margin-left: 5px">
        <el-form-item :label="'请选择筛选条件'"
                      :rules="{required: false, message: '请选择物料', trigger: 'change'}" label-width="150px">
          <el-cascader placeholder="请选择物料"
                       :options="options"
                       @change="getMaterialStockByInfo($event)"
                       clearable>
          </el-cascader>
        </el-form-item>
      </div>
    </el-form>
    <div id="list-main" style="float: left;margin-top: 20px">
      <el-table :data="materials" border style="margin-left: 20px;width: 581px">
        <el-table-column prop="material_id" label="编号" width="100" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="name" label="名称" width="100" align="center"></el-table-column>
        <el-table-column prop="size" label="规格" width="100" align="center"></el-table-column>
       
        <el-table-column prop="count" label="库存" width="100" align="center"></el-table-column>
        <el-table-column label="查看" id="tool" width="80" align="center">
          <template slot-scope="scope">
            <el-button type="primary" circle size="mini" icon="el-icon-more" @click="showChanges(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <div id="list-page" style="float: left;margin-left: 20px;margin-top: 50px">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[5, 10, 15]"
            :page-size="size"
            :pager-count="5"
            layout="total, sizes, prev, pager, next, jumper"
            :total="count">
        </el-pagination>
      </div>
    </div>
    <div id="canvas" style="width: 600px;height:450px;float: left;margin-left: 30px"></div>


  </div>


</template>

<script>
import {request} from "@/network/request";
export default {
  name: "MaterialStock",
  data() {
    return {
      currentPage: 1,
      size: 5,
      count: 0,
      material: {
        name: '',
        size: '',
        
      },
      options: [],
      materials: [],
      dates: ['第1天','第2天','第3天','第4天','第5天','第6天','第7天',],
      stocks: [0,10,20,30,40,50,60],
      material_id: ''
    }
  },
  methods: {
    handleSizeChange(val) {
      this.size = val
      this.getMaterialStock(this.currentPage, val)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getMaterialStock(val, this.size)
    },
    getMaterialStock(pageOffset,pageSize){
      request({
        url: '/dataAnalysis/getMaterialStock',
        method: 'get',
        params: {
          pageOffset: pageOffset,
          pageSize: pageSize,
          company_id:this.$store.getters.userinfo.company_id
        }
      }).then(res => {
        this.count = res.data.count
        this.materials = res.data.materials
      }).catch(err => {
        console.log(err)
      });
    },
    getMaterialStockByInfo(event){
      if(!event[0]) {
        this.getMaterialStock(this.currentPage,this.size)
      }else {
        request({
          url: '/dataAnalysis/getMaterialStockByInfo',
          method: 'get',
          params: {
            name: event[0],
            size: event[1],

          }
        }).then(res => {
          this.count = res.data.count
          this.materials = res.data.materials
          console.log(this.materials)
        }).catch(err => {
          console.log(err)
        });
      }

    },
    getMaterial() {
      request({
        url: '/getOptionMaterial',
        method: 'get',
      }).then(res => {
        this.options = res.data
      }).catch(err => {
        console.log(err)
      });
    },
    showChanges(row){
      this.material_id=row.material_id
      request({
        url: '/dataAnalysis/getMaterialStockChanges',
        method: 'get',
        params: {
          material_id: this.material_id
        }
      }).then(res =>{
        this.dates =['7-9','7-10','7-11','7-12','7-13','7-14','当前',],
        this.stocks = res.data.stocks,
        this.drawChart()
      }).catch(err =>{
        console.log(err)
      })
    },
    drawChart() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.getInstanceByDom(document.getElementById("canvas"))
      if (myChart == null) { // 如果不存在，就进行初始化
        myChart = this.$echarts.init(document.getElementById("canvas"));
      }
      let option = {
        title: {
          text: '过去几天物料库存变化',
          subtext: '物料编号： '+this.material_id

        },
        tooltip: {
          trigger: 'axis'
        },
        toolbox: {
          show: true,
          feature: {
            dataView: {readOnly: false},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          data: this.dates,
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '物料库存',
          data: this.stocks,
          type: 'line'
        }]
      }
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }

  },
  created() {
    this.getMaterialStock(this.currentPage, this.size)
    this.getMaterial()
    this.drawChart()
  },
  activated() {
    this.getMaterialStock(this.currentPage, this.size)
    this.drawChart()

  }
}
</script>

<style scoped>
#MaterialStock {
  padding: 50px 50px 0px 50px;
}
#list-page {
  margin-top: 20px;
}
</style>
