<template>

  <div id="mainDiv">
    <div id="header">
      <div class="headerRight">
        <el-button @click="submitDialog = true" type="primary" plain>&nbsp;&nbsp;&nbsp;提&nbsp;&nbsp;交&nbsp;&nbsp;&nbsp;</el-button>
        <el-button @click="cancelDialog = true" type="info" plain>&nbsp;&nbsp;&nbsp;取&nbsp;&nbsp;消&nbsp;&nbsp;&nbsp;</el-button>


        <el-dialog
            title="提示"
            :visible.sync="submitDialog"
            width="30%"
            :before-close="handleClose">
          <span>是否申请创建该车间</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="submitDialog = false">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
          </span>
        </el-dialog>
        <el-dialog
            title="提示"
            :visible.sync="cancelDialog"
            width="30%"
            :before-close="handleClose">
          <span>取消后您的信息将不会保存</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="cancelDialog = false">取 消</el-button>
            <el-button type="primary" @click="back">确 定</el-button>
          </span>
        </el-dialog>



      </div>
    </div>
    <div class="subMain">
      <div class="item">
      </div>
        <div class="item">
          <el-form :model="workshopInfo" ref="workshopInfo" label-width="100px" class="demo-dynamic">
            <div class="item">
              <el-form-item
                  prop="id"
                  label="车间编号"
                  style="width: 60%"
                  size="100px"
                  :rules="[
                            { required: true, message: '请输入车间编号', trigger: 'blur' },

                          ]">
                <el-input v-model.trim="workshopInfo.id"></el-input>
              </el-form-item>
            </div>
          <div class="item">
          <el-form-item
              prop="name"
              label="车间名称"
              style="width: 60%"
              size="100px"
              :rules="[
                            { required: true, message: '请输入车间名称', trigger: 'blur' },

                          ]">
            <el-input v-model.trim="workshopInfo.name"></el-input>
          </el-form-item>
          </div>
          <div class="item">
            <el-form-item
                prop="factory_name"
                label="工厂名称"
                style="width: 60%"
                size="100px"
                :rules="[
                            { required: true, message: '请输入工厂名称', trigger: 'blur' },

                          ]">
              <el-input v-model.trim="workshopInfo.factory_name"></el-input>
            </el-form-item>
<!--            <div style="margin-left: 20px"><el-button  @click="submitFormFirst('workshopInfo')">增加产线</el-button></div>-->
          </div>
          <div class="item">
          <el-form-item>

          </el-form-item>
          </div>
        </el-form>
        <el-dialog
            title="增加产线 "
            :visible.sync="addDialog"
            width="670px"
            :before-close="handleClose">


            <el-form :model="theLines" ref="theLines" label-width="100px" class="demo-dynamic">
              <el-form-item
                  prop="id"
                  label="产线编号"
                  style="width: 80%"
                  size="100px"
                  :rules="[
                            { required: true, message: '请输入产线编号', trigger: 'blur' },

                          ]">
                <el-input v-model.trim="theLines.id"></el-input>
              </el-form-item>
              <el-form-item
                  prop="name"
                  label="产线名称"
                  style="width: 80%"
                  size="100px"
                  :rules="[
                            { required: true, message: '请输入产线名称', trigger: 'blur' },

                          ]">
                <el-input v-model.trim="theLines.name"></el-input>
              </el-form-item>
              <el-form-item
                  prop="routing"
                  label="产线工艺"
                  style="width: 80%"
                  size="100px"
                  :rules="[
                            { required: true, message: '请输入产线工艺', trigger: 'blur' },

                          ]">
                <el-input v-model.trim="theLines.routing"></el-input>
              </el-form-item>
              <div v-for="(station, index) in theLines.stations" style="width: inherit">
                <el-form-item
                    :label="index + '-工位位序' "
                    style="width:80%"
                    size="100px"
                    :prop="'stations.' + index + '.stationOrder'"
                    :rules="[
                            {required: true, message: '工位位序不可为空', trigger: 'blur', },
                            { type: 'number',message: '工位位序必须为数字', trigger: 'blur' },
                            { type: 'number',min:1, max:theLines.stationNum,message: '工位必须小于'+ theLines.stationNum}
                            //  {
                            //    validator: checkOrder, trigger: 'blur'
                            //  }

                          ]">
                  <el-input style="width: 82%" v-model.number="station.stationOrder" ></el-input>
                  <el-button @click.prevent="removestation(station)" style="visibility:hidden">删除</el-button>
                </el-form-item>
                <el-form-item
                    :label=" '--工位编号' "
                    style="width:80%"
                    size="100px"
                    :prop="'stations.' + index + '.id'"
                    :rules="[
                            {required: true, message: '工位编号不可为空', trigger: 'blur', },
                            // { type: 'number', message: '工位位序必须为数字', trigger: 'blur' }
                          ]">
                  <el-input style="width: 82%" v-model.trim="station.id" ></el-input>
                  <el-button @click.prevent="removestation(station)" style="visibility:hidden">删除</el-button>
                </el-form-item>
                <el-form-item
                    :label="' ' + '--设备编号' "
                    style="width:80%"
                    :prop="'stations.' + index + '.equip_id'"
                    :rules="{
                            required: true, message: '设备编号不可为空', trigger: 'blur'
                          }">
                  <el-input style="width: 82%" v-model.trim="station.equip_id"></el-input>
                  <el-button @click.prevent="removestation(station)" style="visibility:hidden" >删除</el-button>
                </el-form-item>
                <el-form-item
                    :label="' ' + '--工人编号' "
                    style="width:80%"
                    :prop="'stations.' + index + '.user'"
                    :rules="{
                            required: true, message: '工人编号不可为空', trigger: 'blur'
                          }">
                  <el-input style="width: 82%" v-model.trim="station.user"></el-input>
                  <el-button @click.prevent="removestation(station)" >删除</el-button>
                </el-form-item>

              </div>
              <el-form-item>
                <el-button type="primary" @click="submitForm('theLines')">提交</el-button>
                <el-button @click="addstation">增加工位</el-button>
                <el-button @click="resetForm('theLines')">重置</el-button>
              </el-form-item>
            </el-form>
        </el-dialog>
      </div>
    </div>
    <div style="width: 20px;height: 40px"></div>
    <div style="width: 100%;height:40px;margin-top:10px;display:flex;background-color: whitesmoke"></div>
    <!--    下面为 产线信息-->
    <div class="subMainA">
      <div class="itemA" v-for="(line,index) in workshopInfo.lines">
        <el-card shadow="hover" class="box-card">
          <div slot="header" class="clearfix">
            <el-tooltip placement="top">
            <div slot="content">{{'产线工艺:'+ line.routing}}</div>
              <span>{{"产线名称:"+ line.name}}</span>
            </el-tooltip>
            <!--            删除产线  增加工位-->
            <el-button v-loading.fullscreen.lock="fullscreenLoading" @click="delLine(line.name)"
                       style="float: right; padding: 3px 3px;margin-left: 6px" icon="el-icon-close" circle></el-button>
            <el-button v-loading.fullscreen.lock="fullscreenLoading" @click="showAddStatinDialog(index)"
                       style="float: right; padding: 3px 3px" icon="el-icon-plus" circle></el-button>
            <el-dialog

                title="增加工位"
                :visible.sync="addStationDialogs[index].flag"
                width="25%"
                :before-close="handleClose">


<!--              ----------------------------------------------------------------->
              <el-form :model="stationInfo" ref="stationInfo" :rules="rules"  label-width="100px" class="demo-dynamic">
                <div style="width: inherit">
                  <el-form-item
                      :label="'工位位序' "
                      style="width:80%"
                      size="100px"
                      prop="stationOrder"
                      :rules="[
                            {required: true, message: '工位位序不可为空', trigger: 'blur', },
                            { type: 'number',message: '工位位序必须为数字', trigger: 'blur' },
                            { type: 'number',min:1, max:line.stationNum, message:'工位必须小于'+ line.stationNum}
                          ]">
                    <el-input style="width: 82%" v-model.number="stationInfo.stationOrder" ></el-input>
                  </el-form-item>
                  <el-form-item
                      :label="'工位编号' "
                      style="width:80%"
                      size="100px"
                      prop="id"
                      :rules="[
                            {required: true, message: '工位名称不可为空', trigger: 'blur', },
                            { min: 1, max: 5, message: '长度在 1 到 5 个字符', trigger: 'blur' }
                          ]">
                    <el-input style="width: 82%" v-model.trim="stationInfo.id" ></el-input>
                  </el-form-item>
                  <el-form-item
                      :label="'设备编号' "
                      style="width:80%"
                      prop="equip_id"
                      :rules="{
                            required: true, message: '设备编号不可为空', trigger: 'blur'
                          }">
                    <el-input style="width: 82%" v-model.trim="stationInfo.equip_id"></el-input>
                  </el-form-item>
                  <el-form-item
                      :label="'工人编号' "
                      style="width:80%"
                      prop="user"
                      :rules="{
                            required: true, message: '设备名称不可为空', trigger: 'blur'
                          }">
                    <el-input style="width: 82%" v-model.trim="stationInfo.user"></el-input>
                  </el-form-item>
                </div>
                <el-form-item>
                  <el-button type="primary" @click="submitFormAddSta('stationInfo',index)">提交</el-button>
                </el-form-item>
              </el-form>
            </el-dialog>
          </div>
          <!--          产线具体信息  -->

          <div style="display: flex;justify-content: space-around;width: inherit" v-for="(o,index1 ) in line.stations"
               class="text iteme">
            <div style="width: 15%"></div>
            <div style="width: 20px">
              <el-tooltip placement="top">
                <div slot="content">{{ '工位位序 : '+ o.stationOrder+' 工位编号 : '+o.id}}
                  <br>{{'设备编号 : ' + o.equip_id + ' 工人名称 : ' + o.user }}</div>
                <el-button style="width: 10%" @click="editDialogVisibles[index][index1].flag = true" type="text">{{'工位' + index1}}</el-button>

              </el-tooltip>
<!--              {{ o.stationOrder+'&#45;&#45;'+o.stationName+'&#45;&#45;' + o.stationName + '&#45;&#45;' + o.equipment }}-->
            </div>
            <el-link @click="deleteThisSta(index,index1)" circle type="info" icon="el-icon-close" :underline="false" size="mini"></el-link>
            <div style="width: 15%"></div>

            <el-dialog
                title="工位信息"
                :visible.sync="editDialogVisibles[index][index1].flag"
                width="30%"
                :before-close="handleClose">
              <el-form :model="stationInfo" ref="stationInfo" :rules="rules"  label-width="100px" class="demo-dynamic">
                <div style="width: inherit">
                  <el-form-item
                      :label="'工位位序' "
                      style="width:80%"
                      size="100px"

                      prop="stationOrder"
                      :rules="[

                          ]">
                    <el-input style="width: 82%" :disabled="true" :value="workshopInfo.lines[index].stations[index1].stationOrder" :placeholder="workshopInfo.lines[index].stations[index1].stationOrder" v-model.number="stationInfo.stationOrder" ></el-input>
                  </el-form-item>
                  <el-form-item
                      :label="'工位编号' "
                      style="width:80%"
                      size="100px"
                      prop="id"
                      :rules="[
                            {required: true, message: '工位编号不可为空', trigger: 'blur', },

                          ]">
                    <el-input style="width: 82%" :placeholder="workshopInfo.lines[index].stations[index1].id"  v-model.trim="stationInfo.id " ></el-input>
                  </el-form-item>
                  <el-form-item
                      :label="'设备编号' "
                      style="width:80%"
                      prop="equip_id"
                      :rules="{
                            required: true, message: '设备编号不可为空', trigger: 'blur'
                          }">
                    <el-input style="width: 82%"  :placeholder="workshopInfo.lines[index].stations[index1].equip_id" v-model.trim="stationInfo.equip_id"></el-input>
                  </el-form-item>
                  <el-form-item
                      :label="'工人编号' "
                      style="width:80%"
                      prop="user"
                      :rules="{
                            required: true, message: '工人编号不可为空', trigger: 'blur'
                          }">
                    <el-input style="width: 82%"  :placeholder="workshopInfo.lines[index].stations[index1].user"  v-model.trim="stationInfo.user"></el-input>
                  </el-form-item>
                </div>
                <el-form-item>
                  <el-button type="primary" @click="updateFormAddSta('stationInfo',workshopInfo.lines[index].stations[index1].stationOrder,index,index1)">提交</el-button>
                </el-form-item>
              </el-form>
            </el-dialog>


          </div>
        </el-card>
      </div>
    </div>


  </div>

</template>

<script>

import {my_request} from "@/network/requests/workshop_request";
import {my_request_post} from "@/network/requests/my_request_post";

export default {
  name: "createPro",
  data() {
   return {
     submitDialog:false,
     cancelDialog:false,
     editDialogVisibles:[],
      addDialog: false,
      addStationDialog: false,
      addStationDialogs:[],
      flagsize:0,
      fullscreenLoading: false,

      //计数作用，展示产线卡片
      count: 0,
      //
      workshopInfo: {
       // 改company_id
        company_id:this.$store.getters.userinfo.company_id,
        id:'',
        factory_name:'',
        name:'',
        lines: []
      },
      name: '',


     stationInfo: {
        stationOrder: '',//工位顺序
        user:'',
        id:'',
        equip_id:'',
     },

      openName:'',



      theLines: {
        stations: [{
          id:'',
          stationOrder:'',
          equip_id:'',
          user:'',
        }],
        stationNum:1,
        routing:'',
        name: '',
        id:'',
      },
      rules:{

      }

    }
  },
  activated() {
    this.getData()
  },
  methods: {
    getData() {
      for (let i = 0;i<100;i++){
        this.editDialogVisibles.push([])
        for(let j = 0; j < 100;j++){
          this.editDialogVisibles[i].push({
            flag:false
          })
        }
      }
    },

    submit() {
      let req = this.workshopInfo
      console.log(req)
      my_request({
        url: 'workshop/create',
        data : req,
        method: 'post'
      }).then(res=> {
        console.log('创建返回的结果：',res)
        if(res.data.success===true){
          this.$message({
            type:"success",
            message: '申请成功，等待管理员批准'
          })
          // this.$router.push({
          //   path: '/work/workshop/workshopDetail',
          //   query: {
          //     workshopName: this.workshopInfo.workshopName,
          //     workshopId: this.workshopId.workshopId//-----------------从res中取出工厂ID
          //   }
          // })
        }else{
          this.$message.error('申请失败')
        }
        this.submitDialog = false
      }).catch(err=>{
          this.$message.error('服务器异常')
        console.log('err',err)
        this.submitDialog = false
      })
    },
    back(){
      this.$router.go(-1)
    },

    //  增加产线信息
    addLine(name) {
      this.count++;
      this.addDialog = false;
      this.workshopInfo.lines.push({
        name: name,
        stations: []
      });
      let res = String('asd' + this.flagsize);
      this.flagsize= this.flagsize+1
      this.addStationDialogs.push({
        flag:false
      })

      console.log(this.addStationDialogs,'????');
      this.name = "";
    },

    delLine(name) {
      let i = 0
      console.log('del=', name)
      for (; i < this.workshopInfo.lines.length; i++) {
        if (this.workshopInfo.lines[i].name === name) {
          break
        }
      }
      this.workshopInfo.lines.splice(i, 1)

      this.name = '';
    },

    showAddStatinDialog(index){
      this.addStationDialogs[index].flag= true
    },
    notShowAddStatinDialog(index){
      this.addStationDialogs[index].flag= false
    },

    addStation(line,index, stationName, stationOrder, equipment, equipmentId) {
      this.addStationDialogs[index].flag = false;
      let i = 0
      console.log('add==', line);
      for (; i < this.workshopInfo.lines.length; i++) {
        if (this.workshopInfo.lines[i].name === line.name) {
          break
        }
      }
      console.log(i,line.name)
      let indx = stationOrder -1
      this.workshopInfo.lines[i].stations.splice( indx, 0, {
        stationName: stationName,
        stationOrder: stationOrder,
        equipment: equipment,
        equipmentId: equipmentId,

      })
      this.count = this.count + 1;
      this.addStationDialog = false;
      this.stationOrder = '';
      this.stationName = '';
      this.equipmentId = '';
      this.equipment = '';
    },

    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
    },

    // -----------------------------------

    submitFormFirst(formName) {
      if (this.workshopInfo.lines === undefined || this.workshopInfo.lines.length <= 0){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // this.workshopInfo.lines.push(this.theLines)
            this.addDialog = true;
            // this.workshopInfo.lines.stationNum = this.workshopInfo.lines.stationNum +1
            console.log(this.workshopInfo)
          } else {
              return false
          }
        });
      }else {
        this.addDialog = true;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          var o = JSON.parse(JSON.stringify(this.theLines));
          o.stationNum = o.stationNum+1
          this.workshopInfo.lines.push(o)
          // this.workshopInfo.lines.push(this.theLines)
          // this.workshopInfo.lines.stationNum = this.workshopInfo.lines.stationNum + 1

          console.log(this.workshopInfo)
          // 向数据库提交这么一个产线信息
          let req = {

          }
          this.resetForm('theLines')

          this.addDialog = false;

          this.addStationDialogs.push({
            flag:false
          })

        } else {

          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    removestation(item) {
      var index = this.theLines.stations.indexOf(item)
      if (index !== -1) {
        this.theLines.stations.splice(index, 1)
      }

      this.theLines.stationNum = this.theLines.stationNum - 1

    },

    addstation() {
      this.theLines.stations.push({
        value: '',
        stationOrder:'',
        equipmentId:'',
        equipment:'',
        stationName:''
        // key: Date.now()
      });
      this.theLines.stationNum = this.theLines.stationNum +1
    },

    submitFormAddSta(formName,index){
      console.log(this.workshopInfo)

      this.$refs[formName][index].validate((valid) => {
        if (valid) {
          console.log(this.theLines)
          var o = JSON.parse(JSON.stringify(this.stationInfo));
          this.workshopInfo.lines[index].stations.push(o)
          // this.workshopInfo.lines.push(this.theLines)
          console.log(this.workshopInfo)
          console.log(this.workshopInfo.lines[index].stationNum);
          this.workshopInfo.lines[index].stationNum = Number(this.workshopInfo.lines[index].stationNum + 1);
          console.log(this.workshopInfo.lines[index].stationNum);
          // 向数据库提交这么一个产线信息
          let req = {

          }
          this.resetForm('theLines')

          this.addDialog = false;

          this.addStationDialogs.push({
            flag:false
          })

        } else {

          return false;
        }
        this.notShowAddStatinDialog(index)
        this.$refs[formName][index].resetFields();
        console.log(this.workshopInfo);
      });

    },

  //  修改某条工委的信息
    updateFormAddSta(formName,value,index,index1){
      this.$refs[formName][index].validate((valid) => {
        if (valid) {
          this.stationInfo.stationOrder = value
          let o = JSON.parse(JSON.stringify(this.stationInfo));

          this.workshopInfo.lines[index].stations[index1] = o
          // this.workshopInfo.lines.push(this.theLines)
          this.editDialogVisibles[index][index1].flag = false


          // 向数据库提交这么一个产线信息

          this.resetForm('theLines')

          this.$refs[formName][0].resetFields();

        } else {

          return false;
        }

      });


    },

  //  删除某条工位
    deleteThisSta(index,index1){
      this.workshopInfo.lines[index].stations.splice(index1,1)
      // this.theLines.stations.splice(index, 1)
    }
  },
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

.headerRight {
  width: 16%;
  margin: 5px 50px;
  display: flex;
  justify-content: space-around;
  /*align-self: center;*/
  /*align-items: center;*/
}

.buttonDiv {
  width: 22.5%;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  /*align-self: center;*/
  /*align-items: center;*/
}

.lineItem {
  display: flex;
  align-items: flex-start;
  height: 40px !important;
  width: 20px;
  padding-left: 22px;
  margin: 10px 0;
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
  height: 50px;
  align-items: center;
}

.inputBox {
  display: flex;
  width: 300px;
  padding: 0 20px;
}

.itemA {
  display: flex;
  background-color: lightskyblue;
  margin: 10px 10px;
  width: 18%;
  /*width: 100px;*/
  /*height: 50px;*/
  justify-content: flex-start;
}

.subMainA {
  display: flex;
  width: 1500px;
  height: auto;
  margin: 9px;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.lineItem {
  /*background-color: #FFFFFF;*/
  width: 100%;
  height: 100px;
}

.text {
  font-size: 14px;
}
.iteme {
  margin-bottom: 1px;

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