<template>
  <div id="mainDiv">
    <div id="header">

    </div>
    <div class="subMain">
      <div class="item">
        <span>车间编号:</span>
        <div class="inputBox">
          <el-input
              :disabled="isDisabled"
              placeholder="workshopInfo.id"
              v-model="workshopInfo.id"
              clearable>
          </el-input>
        </div>

      </div>
      <div class="item">
        <span>车间名称:</span>
        <div class="inputBox">
          <el-input
              :disabled="isDisabled"
              placeholder="workshopInfo.name"
              v-model="workshopInfo.name"
              clearable>
          </el-input>
        </div>

      </div>
      <div class="item">
        <span>工厂名称:</span>
        <div class="inputBox">
          <el-input
              :disabled="isDisabled"
              placeholder="workshopInfo.factory"
              v-model="workshopInfo.factory"
              clearable>
          </el-input>
        </div>
        <el-button @click="addDialog = true" plain>增加产线</el-button>

        <el-dialog
            title="增加工位"
            :visible.sync="addDialog"
            width="25%"
            :before-close="handleClose">

          <div class="item">
            <span>产线位序:</span>
            <div class="inputBox">
              <el-input
                  placeholder="产线位序"
                  v-model="newLineOrder"
                  clearable>
              </el-input>
            </div>
          </div>
          <div style="height: 10px"></div>
          <div class="item">
            <span>产线名称:</span>
            <div class="inputBox">
              <el-input

                  placeholder="产线名称"
                  v-model="newLineName"
                  clearable>
              </el-input>
            </div>
          </div>

          <span slot="footer" class="dialog-footer">
              <el-button @click="addDialog = false">取 消</el-button>
              <el-button type="primary" @click="addLine(workshopInfo.id,newLineOrder,newLineName)">确 定</el-button>
            </span>
        </el-dialog>


      </div>
    </div>
    <div style="width: 100%;height:40px;display:flex;background-color: whitesmoke"></div>


    <!--  下方产线信息  -->
    <div class="subMainA" id="main">
      <div class="itemA" v-for="(line,index) in workshopInfo.line">
        <el-card shadow="hover"  class="box-card">
          <div slot="header" class="clearfix">
            <span>{{line.lineId}}-{{line.lineName}}:</span>
            <!--            删除产线  增加工位-->
            <el-button  v-loading.fullscreen.lock="fullscreenLoading" @click="delLine(workshopInfo.id,line.lineId)"  style="float: right; padding: 3px 3px;margin-left: 6px"  icon="el-icon-close" circle></el-button>
            <el-button  v-loading.fullscreen.lock="fullscreenLoading" @click="shouwAddStaDialog(index)"  style="float: right; padding: 3px 3px"  icon="el-icon-plus" circle></el-button>
            <el-dialog
                title="增加工位"
                :visible.sync="addStationDialogs[index].flag"
                width="25%"
                :before-close="handleClose">

              <div class="item">
                <span>工位顺序:</span>
                <div class="inputBox">
                  <el-input
                      placeholder="产线顺序"
                      v-model="staOrder"
                      clearable>
                  </el-input>
                </div>
              </div>
              <div style="height: 10px"></div>
              <div class="item">
                <span>设备名称:</span>
                <div class="inputBox">
                  <el-input

                      placeholder="设备名称"
                      v-model="staEqu"
                      clearable>
                  </el-input>
                </div>
              </div>

              <span slot="footer" class="dialog-footer">
              <el-button @click="notShowAddStatinDialog(index)">取 消</el-button>
              <el-button type="primary" @click="addStation(workshopInfo.id,line.lineId,index)">确 定</el-button>
            </span>
            </el-dialog>
          </div>
          <div><span style="font-size: small;font-family: 黑体;color: #ff7300">工位位序-工位编号-设备编号</span></div>
          <!--          没条产线的信息 -->
          <div style="display: flex;justify-content: space-around;width: inherit" v-for="(o,index1 ) in line.station"  class="text item">
            <div>
              {{ o.stationOrder+'-'+ o.stationId+'-'+ o.equipment }}
            </div>
            <div style="display: flex;align-self:end">
            </div>
          </div>
        </el-card>
      </div>
    </div>




  </div>
</template>

<script>
// import {request} from "@/network/request";
import {my_request} from "@/network/requests/workshop_request";

export default {

  data() {
    return {
      //用来存储传递的数据
      transferId : '',
      transferName : '',

      // 增加产线的信息
      linOrder:'',
      linName: '',

      //增加工位的信息
      staOrder:'',
      staEqu: '',
      staEquName: '',

      newLineOrder:'',
      newLineName:'',

      showDialog: false,

      isDisabled: true,

      //展示的主要信息
      workshopInfo: {
        id:1,
        name:'try',
        factory:'史丹利',
        lineNum : 3,
        line:[
          {
            lindId:1,
            lineName:'first',
            stationNum:3,
            station:[
              {
                stationId:1,
                stationOrder:1,
                equipmentId:1,
                equitment:'yi'
              },
              {
                stationId:2,
                stationOrder:2,
                equipmentId:2,
                equitment:'er'
              },
              {
                stationId:3,
                stationOrder:3,
                equipmentId:3,
                equitment:'san'
              }
            ]
          },
          {
            lindId:1,
            lineName:'first',
            stationNum:3,
            station:[
              {
                stationId:1,
                stationOrder:1,
                equipmentId:1,
                equitment:'yi'
              },
              {
                stationId:2,
                stationOrder:2,
                equipmentId:2,
                equitment:'er'
              },
              {
                stationId:3,
                stationOrder:3,
                equipmentId:3,
                equitment:'san'
              }
            ]
          },
          {
            lindId:1,
            lineName:'first',
            stationNum:3,
            station:[
              {
                stationId:1,
                stationOrder:1,
                equipmentId:1,
                equitment:'yi'
              },
              {
                stationId:2,
                stationOrder:2,
                equipmentId:2,
                equitment:'er'
              },
              {
                stationId:3,
                stationOrder:3,
                equipmentId:3,
                equitment:'san'
              }
            ]
          }
        ]
      },
      dialogFormVisible: false,
      lineName: '',
      formLabelWidth: '80px',
      fullscreenLoading : false,

      //  增加产线 内容是否展示
      addStationDialog: false,
      addStationDialogs:[],
      addStationfiags:0,
      //没调产线内部的信息。二维数组 【第几条产线。第几个工位】
      stationInfo:[],

      showEditDialog: false,
      addDialog: false,

    };


  },
  name: "workshopDetail",

  activated() {

    // ---------------------------  要根据这个 查询对应仓库的信息
    this.getData()
  },

  methods: {

    getData() {
        // 产线的总数
        this.addStationfiags =10;
      for(let i = 0 ;i <10;i++) {
        this.addStationDialogs.push({flag: false});
      }
        for(let  i = 0;i<3;i++){
          this.stationInfo.push([])
          //每条产线上面的工位总数。
          for(let j=0;j<3;j++){
            this.stationInfo[i].push({flag: false})
            // console.log('this.stationInfo',this.stationInfo)
            // console.log('this.stationInfo[i]',this.stationInfo)
          }
        }
        console.log(this.stationInfo)
        console.log('?????')
    },
    //删除产线ok
    delLine(workshopId,lineId){

      console.log('删除产线的车间id',workshopId,'待删除的产线id',lineId);
      //  删除 对应车间的 对应 路线
      //  再次返回 对应车间编号的所有信息
      this.fullscreenLoading = true;
      let req = {
        // type: 'delLine',
        // workshopId:workshopId,
        // lineId:lineId

        workshop_id:1,
        line_id:1
      };
      my_request({
        // url: 'line/details',
        url: 'line/delete',
        params: req,
        methods: 'get',
      }).then(res=>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '删除成功'
        })

      }).catch(err =>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('删除失败')

      })
    },
    //增加ok
    addLine(workshopId,newLineOrder,newLineName){

      console.log('增加产线的车间id',workshopId);
      this.fullscreenLoading = true;
      this. addDialog = false
      let req = {
        type: 'addLine',
        workshopId:workshopId,
        // 创建的工位
        lineOrder:newLineOrder,
        lineName:newLineName,
      }
      request({
        url: 'line/details',
        params: req,
        methods: 'get',
      }).then(res=>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '增加成功'
        })
        this.getData()
      }).catch(err =>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('增加失败')
        this.getData()
      })
    },

    shouwAddStaDialog(index){
      this.addStationDialogs[index].flag = true
    },
    notShowAddStatinDialog(index) {
      this.addStationDialogs[index].flag = false
    },
    // 不行，
    addStation(workshopId,lineId,index){
      this.notShowAddStatinDialog(index)
      console.log('增加工位的车间',workshopId,'增加的产线',lineId);
      this.fullscreenLoading = true;
      this.addStationDialog = false;
      let req = {
        type: 'addStation',
        workshopId: workshopId,
        lineId: lineId,
        // 创建的工位
        staOrd: this.staOrd,
        staEqu: this.staEqu,
        staEquName: this.staEquName
      };
      request({
        url: 'line/details',
        params:req,
        methods: 'get',
      }).then(res=>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.workshopInfo = res.deta
        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '增加成功'
        })
      }).catch(err =>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('增加失败')
      })
    },
    //报错
    delStation(workshopId,lineId,staId){
      console.log('待删除工位所属车间id',workshopId);
      console.log('带删除产线di',lineId);
      console.log('待删除工位id:',staId);
      this.fullscreenLoading = true;
      let req = {
        type: 'delStation',
        workshopId:workshopId,
        lineId:lineId,
        stationId: staId
      }
      request({
        url: 'line/details',
        params: req,
        methods: 'post',
      }).then(res=>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // 需要返回数据
        this.workshopInfo = res.deta
        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '删除成功'
        })
      }).catch(err =>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('删除失败')
      })

    },

    showEditStation(row,column){
      this.stationInfo[row][column].flag = true
    },
    notShowEditStation(row, column) {
      this.stationInfo[row][column].flag = false

    },

    editStation(workshopId,lineId,staId,row,column){
      this.notShowEditStation(row,column)
      console.log('待编辑的产线id：',lineId,' 待编辑的工位id：',staId);
      this.fullscreenLoading = true;
      this.showEditDialog = false;
      let req = {
        //传送过去的信息
        //定位
        type: 'addStation',
        workshopId:workshopId,
        lineId:lineId,
        stationId: staId,
        //传送的数据
        staOrd: this.staOrd,
        staEqu: this.staEqu,
        staEquName: this.staEquName
      }
      request({
        url: 'line/details',
        params: req,
        methods: 'get',
      }).then(res=>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // 需要返回数据
        this.workshopInfo = res.deta
        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '修改成功'
        })
      }).catch(err =>{
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('修改失败')
      })

    },

    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    }
  }
}

</script>

<style scoped>
#header {
  height: 50px;
  width: inherit;
  /*background-color: blanchedalmond;*/
  display: flex;
  flex-direction: row-reverse;
}

#mainDiv {
  display: flex;
  /*background-color: #42b983;*/
  width: inherit;
  padding: 0 80px;
  flex-direction: column;

}

.subMain {
  display: flex;
  width: 100%;
  height: auto;
  flex-direction: column;
  margin: 9px;
}
.subMainA {
  display: flex;
  width: 1500px;
  height: 600px;
  margin: 9px;
  justify-content: flex-start;
  flex-wrap: wrap;
  overflow: scroll;
}

#main {
  display: flex;
  width: 1500px;
  height: 580px;
  margin: 9px;
  justify-content: flex-start;
  overflow-x: visible;
  flex-wrap-y: wrap;
}

.headerRight {
  width: 15%;
  margin: 10px 50px 0 0;
  display: flex;
  justify-content: flex-start;
  /*align-self: center;*/
  align-items: center;
}

.buttonDiv {
  width: 28%;
  margin-bottom: 20px;
  padding-left: 15px;
  display: flex;
  justify-content: space-between;
  /*align-self: center;*/
  /*align-items: center;*/
}

.lineItem {
  display: flex;
  align-items: flex-start;
  height: 40px !important;
  width: 20px;
  margin: 10px 0;
  padding-left: 22px;
  padding-bottom: 10px;
  border-bottom: solid 1px whitesmoke;
}

.lineItemdetail {
  display: flex;
  /*background-color: blanchedalmond;*/
  width: 500px;
  height: 100%;
  align-items: center;
}

.item {
  display: flex;
  /*background-color: whitesmoke;*/
  margin: 20px 20px;
  width: 800px;
  height: 20px;
  align-items: center;
}
.itemA{
  display: flex;
  background-color: lightskyblue;
  margin: 10px 10px;
  width: 18%;
  /*width: 100px;*/
  /*height: 50px;*/
  justify-content: flex-start;
}

.inputBox {
  display: flex;
  width: 300px;
  padding: 0 20px;
}

.lineItem {
  /*background-color: #FFFFFF;*/
  width: 100%;
  height: 100px;
}

.incre_or_delete {
  /*background-color: #ff7300;*/
  display: flex;
  justify-content: flex-end;
  width: 100px;
}
.elinput {
  flex-direction: row;
  align-self: flex-start;
  width: 300px;
}

#theSubTable th {
  padding: 10px 20px !important;
  /*background-color: aliceblue;*/
  font-size: large;
  /*border: 1px solid red;*/
}

/deep/ #theSubTable td {
  padding: 6px 20px !important;
  /*border: 1px solid blue;*/

}


#theMainTable th {
  padding: 10px 20px !important;
  /*background-color: aliceblue;*/
  font-size: large;
  /*border: 1px solid red;*/
}

/deep/ #theMainTable td {
  padding: 6px 20px !important;
  /*border: 1px solid blue;*/

}
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
  /*size: ;*/
}



/*  卡片  */
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 480px;
}
</style>