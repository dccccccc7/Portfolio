<template>
  <div id="theBaseMainBoxas">
    <div style="height: 50px;width: 15%;;display: flex; justify-content:flex-end">
      <el-button @click="confirm(1)">同意</el-button>
      <el-button @click="confirm(0)">拒绝</el-button>
    </div>
    <div id="theBaseMainas">
      <div v-if="$route.query.type==='create' && $route.query.workshop==='111'" style="padding-left: 20px">
        <div style="height: 40px;width: 400px;padding-top: 10px;display: flex;justify-content: space-around">
          <span style="line-height: 40px">车间编号:</span>
          <div style="height: 20px;width: 300px">
            <el-input
                placeholder="createInfo.id"
                v-model="createInfo.id"
                :disabled="true">
            </el-input>
          </div>
        </div>
        <div style="height: 40px;width: 400px;padding-top: 10px;display: flex;justify-content: space-around">
          <span style="line-height: 40px">车间名称:</span>
          <div style="height: 20px;width: 300px">
            <el-input
                placeholder="createInfo.name"
                v-model="createInfo.name"
                :disabled="true">
            </el-input>
          </div>
        </div>
        <div style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
          <span style="line-height: 40px">工厂名称:</span>
          <div style="height: 20px;width: 300px">
            <el-input
                placeholder="createInfo.factory_name"
                v-model="createInfo.id"
                :disabled="true">
            </el-input>
          </div>
        </div>
        <!------------------------------------------------------------------>
        <div v-if="createInfo!==null">
        <div class="subMainA" id="main" style="margin-top: 20px">
          <div class="itemA" v-for="(line,index) in createInfo.lines">
            <el-card shadow="hover" class="box-card">
              <div slot="header" class="clearfix">
                                <el-tooltip placement="top">
                  <div slot="content">{{ ' 产线编号 : ' + line.id }}<br>
                    {{'产线工艺：'+line.routing}}
                  </div>
                  <span>{{'产线名称：'+line.name}}</span>
                </el-tooltip>
              </div>

              <!--          没条产线的信息 -->
              <div style="overflow: auto;height: 300px">
                <div style="display: flex;justify-content: space-around;width: inherit"
                     v-for="(o,index1 ) in line.stations" class="text item">
                  <div>

                    <el-tooltip placement="top">
                      <div slot="content">{{ '工位位序 : ' + o.stationOrder + ' 工位编号 : ' + o.id }}
                        <br>{{ '设备编号 : ' + o.equip_id + ' 工人编号 : ' + o.user }}
                      </div>
                      <span>{{ '工位编号:'+o.id + '-' + '工位' + index1 }}</span>
                    </el-tooltip>
                  </div>
                  <div style="display: flex;align-self:end">
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </div>
        </div>
        <!--        ----------------------------------------------------------->
      </div>

      <div v-else>
      <div>
        <div style="width: 100%;height:auto;display: flex; flex-wrap:wrap">
          <h1>变更信息：</h1>
        <div style="height: 1px;width: 100%;"></div>
             <div v-if="updateInfo!==null" style="width: 100%;height: auto;;display: flex;justify-content: space-around; align-items:center">
            <div style="width: 30%;height: auto;">
              <div style="height: 40px;width: 400px;padding-top: 10px;display: flex;justify-content: space-around">
                <span style="line-height: 40px">车间编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].id"
                      v-model="updateInfo[0].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div style="height: 40px;width: 400px;padding-top: 10px;display: flex;justify-content: space-around">
                <span style="line-height: 40px">车间名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].name"
                      v-model="updateInfo[0].name"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">工厂名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].factory_name"
                      v-model="updateInfo[0].factory_name"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].id !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">产线编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].lines[0].id"
                      v-model="updateInfo[0].lines[0].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].name !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">产线名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].lines[0].name"
                      v-model="updateInfo[0].lines[0].name"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].routing !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">产品id:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].lines[0].routing"
                      v-model="updateInfo[0].lines[0].routing"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].stations[0].id !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">工位编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].line[0].stations[0].id"
                      v-model="updateInfo[0].lines[0].stations[0].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].stations[0].stationOrder !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">工位位序:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].lines[0].stations[0].stationOrder"
                      v-model="updateInfo[0].lines[0].stations[0].stationOrder"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].stations[0].equip_id !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">设备编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].lines[0].stations[0].equip_id"
                      v-model="updateInfo[0].lines[0].stations[0].equip_id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[0].lines[0].stations[0].user !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">用户编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[0].lines[0].stations[0].user"
                      v-model="updateInfo[0].lines[0].stations[0].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
            </div>
            <div style="width: 10%;height: 20px;">
              <i class="el-icon-d-arrow-right"></i><br>
              <i class="el-icon-d-arrow-right"></i><br>
              <i class="el-icon-d-arrow-right"></i><br>
            </div>
            <div style="width: 30%;height: auto;">
              <div style="height: 40px;width: 400px;padding-top: 10px;display: flex;justify-content: space-around">
                <span style="line-height: 40px">车间编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].id"
                      v-model="updateInfo[1].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div style="height: 40px;width: 400px;padding-top: 10px;display: flex;justify-content: space-around">
                <span style="line-height: 40px">车间名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].name"
                      v-model="updateInfo[1].name"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">工厂名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].factory_name"
                      v-model="updateInfo[1].factory_name"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].id !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">产线编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].id"
                      v-model="updateInfo[1].lines[0].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].name !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">产线名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].name"
                      v-model="updateInfo[1].lines[0].name"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].routing !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">产线工艺:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].routing"
                      v-model="updateInfo[1].lines[0].routing"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].stations[0].id !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">工位编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].stations[0].id"
                      v-model="updateInfo[1].lines[0].stations[0].id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].stations[0].stationOrder !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">工位位序:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].stations[0].stationOrder"
                      v-model="updateInfo[1].lines[0].stations[0].stationOrder"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].stations[0].equip_id !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">设备编号:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].stations[0].equip_id"
                      v-model="updateInfo[1].lines[0].stations[0].equip_id"
                      :disabled="true">
                  </el-input>
                </div>
              </div>
              <div v-if="updateInfo[1].lines[0].stations[0].user !==null " style="height: 40px;width: 400px;display: flex;padding-top: 10px;justify-content: space-around">
                <span style="line-height: 40px">设备名称:</span>
                <div style="height: 20px;width: 300px">
                  <el-input
                      placeholder="updateInfo[1].lines[0].stations[0].user"
                      v-model="updateInfo[1].lines[0].stations[0].user"
                      :disabled="true">
                  </el-input>
                </div>
              </div>


            </div>
       </div>
        </div>
      </div>


</div>
    </div>
  </div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";

export default {
  name: "applyDeatils",
  data() {
    return {

      createInfo: {},
      updateInfo: [

      ],
      deleteInfo: '',
    }
  },
  created() {
    this.getData()
    console.log(this.$route.query)
  },
  activated() {
    this.getData()
  },
  methods: {
    getData() {
      let req = {
        workshop_id: this.$route.query.workshop_id,
        line_id: this.$route.query.line_id,
        id: this.$route.query.id
      }
      console.log(req)
      //若后面的值是undefined, 在忽视该条记录
      if (this.$route.query.type === 'create'&& this.$route.query.workshop==='111') {
        my_request({
          url: 'workshop/createInfo',
          methods: 'get',
          params: req
        }).then(res => {

          // 返回值：
          // create= {
          //   workchopId:
          //   name:
          //   factory_name:
          //   lines:[
          //     {
          //       id:
          //       name:
          //       stations:[
          //           id：
          //           name:
          //           stationOrder:
          //           equipmengtId:
          //           user:
          //       ]
          //     }
          //   ]
          // }
          console.log('create::',res)
          this.createInfo = res.data.result

        }).catch(err => {
            this.$message.error('服务器异常')
        })
      } else {
        let req = {
          workshopId: this.$route.query.workshop_id,
          lineId: this.$route.query.line_id,
          stationId: this.$route.query.id,
          company_id:this.$store.getters.userinfo.company_id
        }
        my_request({
          url: 'workshop/update_delete_Info',
          methods: 'get',
          params: req
        }).then(res => {

          console.log('修改修改',res)
          this.updateInfo = res.data.result
          console.log('cao',this.updateInfo)
          // 返回值：
          // updateInfo: [ 第一个是 原属性，第二个是修改后的属性，如果只是修改车间编号 车间名称 工程名称，后面的属性都设置为空字符串  不要不写。
          //   {
          //     id: '车间编号',
          //     name: '车间名称',
          //     factory_name: '工厂名称',
          //     lines: {
          //       id: '产线编号',
          //       name: '产线名',
          //       stations: {
          //         id: '工位编号',
          //         name: '工位名称',
          //         stationOrder: '工位位序',
          //         equip_id: '设备编号',
          //         user: '设备名成',
          //       }
          //     }
          //   },
          //   {
          //     id: '新车间编号',
          //     name: '信车间名称',
          //     factory_name: '心工厂名称',
          //     lines: {
          //       id: '心产线编号',
          //       name: '心产线名',
          //       stations: {
          //         id: '新工位编号',
          //         name: '新工位名称',
          //         stationOrder: '新工位位序',
          //         equip_id: '新设备编号',
          //         user: '新设备名成',
          //       }
          //     }
          //   }
          // ],
          // this.updateInfo = res.
        }).catch(err => {

        })
      }


    },
    confirm(mess) {
      let req = {
        workshop_id: this.$route.query.workshop_id,
        line_id: this.$route.query.line_id,
        id: this.$route.query.id,
        user: this.$store.getters.userinfo.id,
        info: mess,
        company_id: this.$store.getters.userinfo.company_id
        //  1 是 true同意  0是false拒绝
      }
      console.log('req::',req)
      my_request({
        url: 'workshop/confirm',
        methods: 'get',
        params: req
      }).then(res=>{
        console.log('conrirm: ' ,res)
        if(res.data.success===true){
          this.$message({
            type:'success',
            message:'修改成功'
          })
          this.$router.go(-1)
        }else {
          this.message.error('操作失败')
        }

      }).catch(err=>{
        console.log(err)
      })
    }
  }
}
</script>

<style scoped>

#theBaseMainBoxas {
  display: flex;
  /*background-color: gray;*/
  height: auto;
  flex-direction: column;
  /*justify-content: center;*/
  margin-top: 20px;
  width: 1400px;
}


#theBaseMainas {
  /*background-color: beige;*/
  height: 700px;
  width: 100%;
  padding: 0 80px;
  /*overflow: auto;*/
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


#header {
  height: 50px;
  width: inherit;
  /*background-color: blanchedalmond;*/
  display: flex;
  flex-direction: row-reverse;
}

.subMainA {
  display: flex;
  width: 100%;
  height: 600px;
  margin: 9px;
  justify-content: flex-start;
  flex-wrap: wrap;
  overflow: auto;
}


.item {
  display: flex;
  /*background-color: whitesmoke;*/
  margin: 20px 20px;
  width: 800px;
  height: 20px;
  align-items: center;
}

.itemA {
  display: flex;
  background-color: lightskyblue;
  margin: 10px 10px;
  width: 18%;
  height: 400px;
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