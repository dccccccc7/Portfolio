<template>
  <div id="mainDiv">
    <div id="header">
        <div class="headerRight">
<!--          <el-button @click="submitDialog = true" type="primary" plain>&nbsp;&nbsp;&nbsp;删&nbsp;&nbsp;除&nbsp;&nbsp;&nbsp;</el-button>-->
          <el-button style="margin-left: 70%;margin-top: 10%" @click="returnLastPage" type="primary" plain>&nbsp;&nbsp;&nbsp;返&nbsp;&nbsp;回&nbsp;&nbsp;&nbsp;</el-button>

<!--          <el-dialog-->
<!--              title="提示"-->
<!--              :visible.sync="submitDialog"-->
<!--              width="30%"-->
<!--              :before-close="handleClose">-->
<!--            <span>是否删除该车间</span>-->
<!--            <span slot="footer" class="dialog-footer">-->
<!--            <el-button @click="submitDialog = false">取 消</el-button>-->
<!--            <el-button type="primary" @click="delWrokshop">确 定</el-button>-->
<!--          </span>-->
<!--          </el-dialog>-->

        </div>
    </div>
    <div class="subMain">
      <div class="item">
        <span>车间编号:  {{workshopInfo.id}}</span>
      </div>
      <div class="item">
        <span>车间名称: {{workshopInfo.name}}</span>
      </div>
      <div class="item">
        <span>工厂名称: {{workshopInfo.factory_name}}</span>
        <el-button style="margin-left: 30%" @click="addDialog = true" plain>增加产线</el-button>

<!--      批量增加产线  -->


        <el-dialog
            title="增加产线"
            :visible.sync="addDialog"
            width="37%"
            :before-close="handleClose">

          <el-form :model="theLines" ref="theLines" label-width="100px" class="demo-dynamic">
            <div v-for="(line, index) in theLines.lines" style="width: inherit">
              <el-form-item
                  :prop="'lines.' + index + '.newLineId'"
                  :label="index + '产线编号' "

                  style="width: 80%"
                  size="100px"
                  :rules="{ required: true, message: '产线编号不能为空', trigger: 'blur' }
                          ">
                <el-input v-model.trim="line.newLineId"  placeholder="请输入产线编号"></el-input>
                <el-button @click.prevent="removestation(line)" style="visibility:hidden">删除</el-button>
              </el-form-item>

              <el-form-item
                  :prop="'lines.' + index + '.newLineName'"
                  :label="'产线名称'"
                  style="width: 80%"
                  size="100px"
                  :rules="[
                            { required: true, message: '请输入产线名称', trigger: 'blur' },
                          ]">
                <el-input v-model.trim="line.newLineName"  placeholder="请输入产线名称"></el-input>
                <el-button @click.prevent="removestation(line)" style="visibility:hidden">删除</el-button>
              </el-form-item>
<!--              <el-form-item-->
<!--                  :prop="'lines.' + index + '.newLineRouting'"-->
<!--                  :label="'产品id'"-->
<!--                  style="width: 80%"-->
<!--                  size="100px"-->
<!--                  :rules="[-->
<!--                            { required: true, message: '请输入产线工艺', trigger: 'blur' },-->
<!--                          ]">-->
<!--                <el-input v-model.trim="line.newLineRouting"  placeholder="请输入产线工艺"></el-input>-->
<!--                <el-button style="margin-left: 101%;margin-top: 5%" @click.prevent="removestation(line)" >删除</el-button>-->
<!--              </el-form-item>-->
              <el-form-item
                  :prop="'lines.' + index + '.newLineRouting'"
                  :label="'产品编号'"
                  style="width: 80%"
                  size="100px">
                <el-select
                    v-model="line.newLineRouting"
                    filterable
                    remote
                    reserve-keyword
                    placeholder="输入关键词快速查找"
                    :remote-method="remoteMethod"
                    :loading="loading"
                    style="width: 100%">
                  <el-option
                      v-for="item in options2"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
                <el-button style="margin-left: 101%;margin-top: 5%" @click.prevent="removestation(line)" >删除</el-button>
              </el-form-item>

            </div>

            <el-form-item>
              <el-button type="primary" @click="submitForm('theLines')">提交</el-button>
              <el-button type="primary" @click="addLines">增加产线</el-button>
              <el-button @click="resetForm('theLines')">重置</el-button>
            </el-form-item>
          </el-form>

<!--          --------------------------------------------------------------------------------------------------->
        </el-dialog>
      </div>
    </div>
    <div style="width: 100%;height:20px;display:flex;background-color: whitesmoke"></div>


<!--  下方产线信息  -->
    <div class="subMainA" id="main">
      <div class="itemA" v-for="(line,index) in tableLineData">
        <el-card style="width: 300px;height: 400px"
                 class="card"
                 bodyStyle="padding:10px"
                 shadow="hover">
          <div slot="header" class="clearfix">
<!--            <el-tooltip placement="top">-->
<!--              <div slot="content">{{'产线工艺:'+ line.routing}}</div>-->
<!--              <el-button style="width: 10%" @click="editLineName[index].flag = true" type="text">{{line.name}}</el-button>-->

              <span style="float: left">产线名称: {{line.name}}</span>
<!--            </el-tooltip>-->
<!--            <span style="font-size: smaller">产线编号:{{line.id}}产线名称</span>-->
<!--            <el-button style="width: 10%" @click="editLineName[index].flag = true" type="text">{{line.name}}</el-button>-->
<!--            -------------------------------------------================================================================------->
            <el-dialog
                title="提示"
                :visible.sync="editLineName[index].flag"
                width="30%"
                :before-close="handleClose">

              <el-form :model="updateLineName" ref="updateLineName" label-width="100px" class="demo-ruleForm">
                <el-form-item
                    label="产线名称"
                    prop="newLineName"
                    :rules="[
                              { required: true, message: '产线名称不能为空'},
                            ]">
                  <el-input type="age" v-model.number="updateLineName.newLineName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item
                    label="产品编号"
                    prop="newLineRouting"
                    >
                  <el-select
                      v-model="updateLineName.newLineRouting"
                      filterable
                      remote
                      reserve-keyword
                      placeholder="输入关键词快速查找"
                      :remote-method="remoteMethod"
                      :loading="loading"
                      style="width: 100%">
                    <el-option
                        v-for="item in options2"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                  </el-select>
<!--                  <el-input type="age" v-model.number="updateLineName.newLineRouting" autocomplete="off"></el-input>-->
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updatetheLineName('updateLineName',workshopInfo.id,line.id,index)">修改</el-button>
                </el-form-item>
              </el-form>

            </el-dialog>
<!--            ---------------------------------------------------------------------------------------------------------------------->
            <!--          产线工艺  删除产线  增加工位-->
            <el-button
                v-loading.fullscreen.lock="fullscreenLoading"
                @click="showDetail(line.routing)"
                style="float: right; padding: 3px 3px;margin-left: 6px"
                icon="el-icon-document"
                circle
            ></el-button>
            <el-button
                v-loading.fullscreen.lock="fullscreenLoading"
                @click="editLineName[index].flag = true"
                style="float: right; padding: 3px 3px;margin-left: 6px"
                icon="el-icon-edit"
                circle
            ></el-button>

            <el-button
                v-loading.fullscreen.lock="fullscreenLoading"
                @click="delLine(workshopInfo.id,line.id)"
                style="float: right; padding: 3px 3px;margin-left: 6px"
                icon="el-icon-delete"
                circle
            ></el-button>
<!--            增加工位-->
<!--            <el-button-->
<!--                v-loading.fullscreen.lock="fullscreenLoading"-->
<!--                @click="shouwAddStaDialog(index)"-->
<!--                style="float: right; padding: 3px 3px"-->
<!--                icon="el-icon-plus"-->
<!--                circle></el-button>-->
            <el-dialog
                title="增加工位"
                :visible.sync="addStationDialogs[index].flag"
                width="40%"
                :before-close="handleClose">
              <el-form :model="theStations" ref="theStations" label-width="100px" class="demo-ruleForm">
                <div v-for="(domain, index) in theStations.addStationInfo" style="width: inherit">
                  <el-form-item
                      :label="index + '-工位编号' "
                      :prop="'addStationInfo.' + index + '.stationId'"
                      :rules="[
                              { required: true, message: '工位编号不能为空'},
                            ]">
                    <el-input v-model="domain.stationId" autocomplete="off"></el-input>
                    <el-button @click.prevent="removestation(domain)" style="visibility:hidden">删除</el-button>

                  </el-form-item>
                  <el-form-item
                      :label="' '+'-工位位序'"
                      :prop="'addStationInfo.' + index + '.staOrd'"
                      :rules="[
                              { required: true, message: '工位位序不能为空'},
                            ]">
                    <el-input v-model.number="domain.staOrd" autocomplete="off"></el-input>
                    <el-button @click.prevent="removestation(domain)" style="visibility:hidden">删除</el-button>

                  </el-form-item>
                  <el-form-item
                      :label="' ' +'-工人编号'"
                      :prop="'addStationInfo.' + index + '.user'"
                      :rules="[
                              { required: true, message: '工人编号不能为空'},
                            ]">
                    <el-input v-model="domain.user" autocomplete="off"></el-input>
                    <el-button @click.prevent="removestation(domain)" style="visibility:hidden">删除</el-button>

                  </el-form-item>
                  <el-form-item
                      :label="' '+'设备编号'"
                      :prop="'addStationInfo.' + index + '.staEqu'"
                      :rules="[
                              { required: true, message: '设备编号不能为空'},
                            ]">
                    <el-input v-model="domain.staEqu" autocomplete="off"></el-input>
                    <el-button @click.prevent="removestation(domain)" style="visibility:hidden" >删除</el-button>

                  </el-form-item>
                </div>
                <el-form-item>
                  <el-button @click="addAllStation('theStations',line.id)">提交</el-button>
                  <el-button @click="addStations">增加工位</el-button>
                  <!--                  <button @click="add221AStation('addStationInfo',workshopInfo.id,line.id,index)">提交</button>-->
                  <!--                  <el-button type="primary" @click="addStation(1,1,1,1)">点击</el-button>-->
                  <el-button @click="notShowAddStatinDialog(index)">取 消</el-button>
                </el-form-item>
              </el-form>

              <!------------------------------------------------->
              <!--              <span slot="footer" class="dialog-footer">-->
              <!---->
              <!--              <el-button type="primary" @click="addStation(workshopInfo.id,line.lineId,index)">确 定</el-button>-->
              <!--            </span>-->
            </el-dialog>
          </div>




<!--          没条产线的信息 -->
          <div style="overflow: auto;height: 300px" >
<!--                        增加工位-->
<!--                        <el-button-->
<!--                            v-loading.fullscreen.lock="fullscreenLoading"-->
<!--                            @click="shouwAddStaDialog(index)"-->
<!--                            style="float: right; padding: 3px 3px"-->
<!--                            icon="el-icon-plus"-->
<!--                            circle></el-button>-->
          <div style="display: flex;justify-content: space-around;width: 60%" v-for="(o,index1) in line.stations"  class="text item" >
            <div>

              <el-tooltip placement="top">
                <div slot="content">{{'工位编号 : '+ o.id+' 工位位序 : '+o.stationOrder}}
                  <br>{{'设备编号 : ' + o.equip_id + ' 工人编号 : ' + o.user }}</div>
                <span style = "margin-left: 10px">{{o.name+'--'+'工位' + index1}}</span>


              </el-tooltip>

            </div>
            <div style="display: flex;align-self:end">
              <!--                  :visible.sync="stationInfo[index][index1].flag"-->
              <!--                  :visible.sync="tryflag"-->

              <el-button style="margin-left: 80%" size="small" @click="showEditStation(index,index1)" icon="el-icon-edit" type="text"></el-button>

              <el-dialog
                  title="修改信息"
                  :visible.sync="tryflag[index][index1].flag"
                  width="25%"
                  :before-close="handleClose">
<!--                -------------------------------------------------------->
                <el-form :model="newStation" ref="newStation" :rules="rules"  label-width="100px" class="demo-dynamic">
                  <div style="width: inherit">
                    <el-form-item
                        :label="'工位位序' "
                        style="width:80%"
                        size="100px"
                        prop="staOrder"
                    >
                      <el-input v-model="o.stationOrder" disabled style="width: 82%;"></el-input>
                    </el-form-item>
                    <el-form-item
                        :label="'工位编号' "
                        style="width:80%"
                        prop="id"
                        >
                      <el-input v-model="o.id" disabled style="width: 82%;"></el-input>
                    </el-form-item>
                    <el-form-item
                        :label="'设备编号' "
                        style="width:80%"
                        prop="staEqu"
                        :rules="{
                            required: true, message: '设备编号不可为空', trigger: 'blur'
                          }">
                      <el-input style="width: 82%" v-model="newStation.staEqu"></el-input>
                    </el-form-item>
                    <el-form-item
                        :label="'工人编号' "
                        style="width:80%"
                        prop="user"
                        :rules="{
                            required: true, message: '工人编号不可为空', trigger: 'blur'
                          }">
                      <el-input style="width: 82%" v-model="newStation.user"></el-input>
                    </el-form-item>
                  </div>
                  <el-form-item>
                    <el-button type="primary" @click="updateFormAddSta('newStation',workshopInfo.id,line.id,o.id,o.stationOrder,index,index1)">提交</el-button>
                  </el-form-item>
                </el-form>
<!--                --------------------------------------------------------->
              </el-dialog>
<!--              <el-button  v-loading.fullscreen.lock="fullscreenLoading"  style="margin-left: 30%" size="mini" @click="delStation(workshopInfo.id,line.id,o.id)" icon="el-icon-delete" type="text" circle></el-button>-->
            </div>
          </div>
          </div>
        </el-card>
      </div>
    </div>

    <div class="block" style="padding: 10px">
      <el-pagination
          @size-change="current-change"
          @prev-click="preclick"
          @next-click="nextclick"
          @current-change="curChange"
          :hide-on-single-page=false
          :total="total"
          background
          layout="total,prev, pager, next, jumper"
          :page-size="page.pagesize">
      </el-pagination>
    </div>


    <el-drawer title="详情" :visible.sync="isShowDetail" size="40%" :destroy-on-close="true">
      <detail-page :product_id="product_id"></detail-page>
    </el-drawer>


  </div>
</template>

<script>
import {request} from "@/network/request";
import detailPage from "@/views/production_process/production/Detail"
import {my_request} from "@/network/requests/workshop_request";
import {my_request_post} from "@/network/requests/my_request_post";
import editPage from "@/views/production_process/production/Edit";

export default {
  components: {
    detailPage,editPage
  },

  data() {
    return {

      //分页查看产线
      tableLineData: [],

      //分页
      total:1,
      page: {
        pagesize: 4,
        curpage: 1,
      },

      //查询产品ID
      options2: [],
      list: [],
      loading: false,

      product_id: '',

      lineInfo: {
        lines: []
      },

      // 批量增加产线传输的数据
      theLines: {
        lines: [{
          newLineId:'',
          newLineName:'',
          newLineRouting:'',
        }],
        lineNum:1,
        theWorkshopId:'',
        company_id:this.$store.getters.userinfo.company_id,
      },

      isShowDetail: false,
      //用来存储传递的数据

      transferId: '',
      transferName: '',

      // 增加产线的信息
      linOrder: '',
      linName: '',

      submitDialog: false,
      //增加工位的信息
      staOrder: '',
      staEqu: '',
      staEquName: '',

      theStations: {
        addStationInfo: [{
          stationId: '',
          staOrd: '',
          user: '',
          staEqu: '',
          company_id:this.$store.getters.userinfo.company_id,
        }],
        workshopId:'',
        line:'',
      },

      newStation: {
        staOrder: '',
        user:'',
        staEqu: '',
        id:'',
        staName: '',
        staEquName: '',
      },

      updateLineName: {newLineName: '',newLineRouting:''},
      newLineOrder: '',
      newLineName: {
        newLineName: '',
        newLineId: '',
        newLineRouting:''
      },

      showDialog: false,
      editLineName: [],
      isDisabled: true,

      //展示的主要信息
      workshopInfo: '',
      dialogFormVisible: false,
      lineName: '',
      formLabelWidth: '80px',
      fullscreenLoading: false,

      //  增加产线 内容是否展示
      addStationDialog: false,
      addStationDialogs: [],
      addStationfiags: 0,
      //没调产线内部的信息。二维数组 【第几条产线。第几个工位】
      stationInfo: [],
      tryflag: [],
      showEditDialog: false,
      addDialog: false,

      //------------------
      nowShopworkId: '',
      workshop: {
        workshopId: '',
        workshopName: '',
        factory_name: '',
      },
      rules: {}
    }


  },
  name: "workshopDetail",
  created () {
    this.nowShopworkId = this.$route.query.workshopId
    this.getData();
    this.loadCode();
    this.getLineData();
  },

  activated () {
    this.nowShopworkId = this.$route.query.workshopId
    this.getData();
    this.loadCode();
    this.getLineData();
  },

  methods: {

    getLineData(){
      let req = {
        currentPage: this.page.curpage,
        pagesize: this.page.pagesize,
        workshopId: this.nowShopworkId,
        company_id: this.$store.getters.userinfo.company_id
      };
      this.loading = true
      my_request({
        //设置一下让他请求 从 curpage *  ( pageSize - 1 ) 开始  一共 pageSize个数据 -------------------------------

        url: 'line/getLineItem',
        params: req,
        methods: 'get'
      }).then(res => {
        console.log('产线新细',res)
        this.tableLineData = res.data.result.records
        this.total = res.data.result.total
      }).catch(err => {
        this.$message.error('服务器异常')
      })
      this.loading = false
    },


    //  翻页  ----------------------------
    preclick(curpage) {
      this.page.curpage = curpage
      this.loading = true
      this.getLineData()
      this.loading = false

    },
    nextclick(curpage) {
      this.page.curpage = curpage
      this.loading = true
      this.getLineData()
      this.loading = false


    },
    curChange(curpage) {
      this.page.curpage = curpage
      this.loading = true
      setTimeout(() => {
        this.getLineData()
        this.loading = false
      }, 1000)
    },


    //重置
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    //根据输入关键词查询产品id
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.options2 = this.list.filter(item => {
            return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
          });
        }, 200);
      } else {
        this.options2 = [];
      }
    },

    //查询所有的产品id
        loadCode(){
          let req = {
            company_id:this.$store.getters.userinfo.company_id,
          };
          request({
            url:'/line/searchProductId',
            method:'get',
            params: req,
          }).then(resp =>{
            if(resp && resp.status === 200){
              let data = resp.data;
              this.list = data.map(item => {
                return { value: `${item.product_id}`, label: `${item.product_id}:${item.type}` };
              });
            }
          }).catch(err =>{
            this.$message.error('网络出错，请稍后再试');
          });
        },


    //批量增加工位
    addAllStation(formName,lineId) {
      this.$router.push("/work/apply/lineInfo")
      this.$refs[formName][0].validate((valid) => {
        if (valid) {
          let req = this.theStations
          req.workshopId = this.nowShopworkId
          req.lineId = lineId
          // req.user = 1
          console.log(req)
          my_request({
            url: 'station/create',
            method: 'post',
            data: req
          }).then(res => {
            if (res.data.success===true){
              this.$message({
                type: 'success',
                message: '创建成功'
              })
            }else {
              this.$message.error('创建失败')
            }
            //还得给他跳转到主页去
          }).catch(err => {
            this.$message.error('服务器异常')

          })
          console.log(this.storage)
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    addStations() {
      this.theStations.addStationInfo.push({
        shelf_id: '',
      });
    },


//批量增加产线
    addLines() {
      this.theLines.lines.push({
       // value: '',
       //  line_id:'',
       //  line_name:'',
       //  line_routing:'',
        // key: Date.now()
      });
      this.theLines.lineNum = this.theLines.lineNum +1
    },

    removestation(item) {
      var index = this.theLines.lines.indexOf(item)
      if (index !== -1) {
        this.theLines.lines.splice(index, 1)
      }

      this.theLines.lineNum = this.theLines.lineNum - 1

    },

    returnLastPage (){

      this.$router.go(-1)
    },

    showDetail(routing) {
      this.product_id = routing
      this.isShowDetail = true
    },

    getData() {
      // 获取当前传送来的信息的数据，
      let req = {
        workshopId: this.nowShopworkId
      }
      console.log('...',req)
      // my_request({
      my_request({   // -------------------------暂时代替上面
        url: 'workshop/mainInfo',
        method: 'get',
        params: req
      }).then(res => {
        console.log('getData:',res)
        if(res.data.success===true){
          this.workshopInfo = res.data.result
        }else {
          this.$message.error('服务器异常')

        }
        console.log('返回的结果',res)
        // 暂时 设置为10  这里应该是产线总数的
        // this.addStationfiags = this.workshopInfo.lineNum;

      }).catch(err => {
        this.$message.error('服务器异常')


      })
      this.addStationfiags = 10;
      // 这里的10  应该是  this.workshop.lineNum   产线的总数
      for (let i = 0; i < 10; i++) {
        this.addStationDialogs.push({flag: false});
        this.editLineName.push({flag: false})
      }
      // 产线的总数
      for (let i = 0; i < 20; i++) {
        this.stationInfo.push([])
        this. tryflag.push([])

        //每条产线上面的工位总数。
        for (let j = 0; j < 20; j++) {
          this.stationInfo[i].push({flag: false})
          this.tryflag[i].push({flag: false})
        }
      }

    },

    //删除车间
    delWrokshop() {
      let req = {
        workshopId: this.nowShopworkId
      }
      request({
        url: 'workshop/delete',
        params: req,
        method: 'get'
      }).then(res => {
        console.log(res,'删除')
        if (res.data.success === true) {
          this.$message({
            type: 'success',
            message: '申请成功'
          })
          this.$router.go(-1)
        } else {
          this.$message({
            type: 'error',
            message: '申请失败'
          })
          this.submitDialog = false
        }
      }).catch(err => {
        this.$message.error("服务器异常")
        this.submitDialog = false
      })


    },
    //删除产线ok
    delLine(workshopId, lineId) {

      //  删除 对应车间的 对应 路线
      //  再次返回 对应车间编号的所有信息
      this.fullscreenLoading = true;
      let req = {

        // workshopId:workshopId,
        // lineId:lineId

        workshopId: workshopId,
        lineId: lineId
      };
      console.log('删除产线',req)
      my_request({
        // url: 'line/details',
        url: 'line/deleteAll',
        params: req,
        method: 'post',

      }).then(res => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // -----------------------------------这里应重新渲染页面
        if(res.data.success===true){
          this.$message({
            type:'success',
            message:'申请成功'
          })
        }else {
          this.$message.error('申请失败')
        }
        console.log('删除产线::::::',res)

      }).catch(err => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('服务器异常')

      })
    },
    //增加ok
    addLine(workshopId, newLineId, newLineName,routing) {

      this.fullscreenLoading = true;
      this.addDialog = false
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
        workshopId: workshopId,
        // 创建的工位
        lineName: newLineName,
        lineId: newLineId,
        routing:routing,
      }
      my_request({
        url: 'line/create',
        params: req,
        method: 'get',
      }).then(res => {
        console.log('增加产线：',res)
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '申请成功'
        })
      }).catch(err => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('申请失败')

      })
    },

    shouwAddStaDialog(index) {
      this.addStationDialogs[index].flag = true
    },
    notShowAddStatinDialog(index) {
      this.addStationDialogs[index].flag = false
    },
    updatetheLineName(formName, workshopId, lineId, index) {
      this.$refs[formName][index].validate((valid) => {
        if (valid) {
          let req = {
            company_id:this.$store.getters.userinfo.company_id,
            workshopId: workshopId,
            lineId: lineId,
            newName: this.updateLineName.newLineName,
            routing : this.updateLineName.newLineRouting
          }
          console.log('修改产线名：',req)
          my_request({
            url: 'line/update',
            method: 'get',
            params: req
          }).then(res => {

            console.log('修改产线名：',res)
            if(res.data.success === true){
              this.$message({
                type:'success',
                message:'申请成功'
              })
            }else {
              this.$message.error('申请失败！产线编号已存在或正在修改中')
            }

          }).catch(err => {

            this.$message.error('服务器异常')
          })

        } else {
          return false;
        }
      });
      this.editLineName[index].flag = false
    },
    // 不行，
    addAStation(formName, workshopId, lineId, index) {
      this.$refs[formName][0].validate((valid) => {
        if (valid) {
          this.notShowAddStatinDialog(index)
          this.fullscreenLoading = true;
          this.addStationDialog = false;
          let req = {
            company_id:this.$store.getters.userinfo.company_id,
            workshopId: workshopId,
            lineId: lineId,
            // 创建的工位
            // staOrd: this.staOrd,
            // staEqu: this.staEqu,
            stationId: this.addStationInfo.stationId,
            staOrd: this.addStationInfo.staOrd,
            user: this.addStationInfo.user,
            staEqu: this.addStationInfo.staEqu
          };
          request({
            url: 'station/create',
            params: req,
            method: 'get',
          }).then(res => {
            this.fullscreenLoading = false   // 这个 我不确定放在那里
            this.workshopInfo = res.data
            // -----------------------------------这里应重新渲染页面
            this.$message({
              message: '申请成功'
            })
          }).catch(err => {
            this.fullscreenLoading = false   // 这个 我不确定放在那里
            this.$message.error('申请失败')
          })

          this.fullscreenLoading = false
        } else {
          return false;
        }
      });
    },

    //报错
    delStation(workshopId, lineId, staId) {
      this.fullscreenLoading = true;
      let req = {

        workshopId: workshopId,
        lineId: lineId,
        stationId: staId
      }
      request({
        url: 'station/delete',
        params: req,
        method: 'get',
      }).then(res => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // 需要返回数据
        if(res.data.success===true) {
          // -----------------------------------这里应重新渲染页面
          this.$message({
            message: '申请成功'
          })
        }else {
          this.$message.error('申请失败')
        }
      }).catch(err => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('申请失败')
      })

    },

    showEditStation(row, column) {
      this.stationInfo[row][column].flag = true
      this.tryflag[row][column].flag = true
    },
    notShowEditStation(row, column) {
      this.tryflag[row][column].flag = false

    },

    editStation(workshopId, lineId, staId,stationOrder, row, column) {
      this.notShowEditStation(row, column)
      this.fullscreenLoading = true;
      this.fullscreenLoading = true;
      this.showEditDialog = false;
      let req = {
        //传送过去的信息
        //定位
        company_id: this.$store.getters.userinfo.company_id,
        workshopId: workshopId,
        lineId: lineId,
        stationId: staId,
        //传送的数据

        staOrd:stationOrder,
        user: this.newStation.user,
        staEqu:this.newStation.staEqu,
        id:staId

      }
      my_request({
        url: 'station/update',
        params: req,
        method: 'get',
      }).then(res => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        // 需要返回数据

        // -----------------------------------这里应重新渲染页面
        this.$message({
          message: '申请成功'
        })

      }).catch(err => {
        this.fullscreenLoading = false   // 这个 我不确定放在那里
        this.$message.error('服务器异常')
      })

    },

    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
    },
    //  ------------------------------------

    submitToUpdate() {

      let req = {
        company_id:this.$store.getters.userinfo.company_id,
        workshopId: this.workshopInfo.id,
        newWorkshopId: this.workshop.workshopId,
        newWorkshopName: this.workshop.workshopName,
        newFactory: this.workshop.factory_name
      }
      console.log('reqqq,',req)
      my_request({
        url: 'workshop/update',
        params: req,
        method: 'get'
      }).then(res => {
        if(res.data.success===true) {
          this.$message({
            type: 'success',
            message: '申请成功'
          })
        }else if (res.data.success===false) {
          this.$message.error('申请失败')
        }
        // this.getData()
        // this.nowShopworkId = req.workshopId
        console.log('修改：',res)
      }).catch(err => {
        this.$message.error('服务器异常')

      })

    },


    // submitForm(formName) {
    //   this.$refs[formName].validate((valid) => {
    //     if (valid) {
    //
    //       var o = JSON.parse(JSON.stringify(this.theLines));
    //       o.lineNum = o.lineNum+1
    //       this.lineInfo.lines.push(o)
    //       // this.workshopInfo.lines.push(this.theLines)
    //       // this.workshopInfo.lines.stationNum = this.workshopInfo.lines.stationNum + 1
    //
    //
    //
    //       console.log(this.lineInfo)
    //       // 向数据库提交这么一个产线信息
    //       let req = {
    //
    //       }
    //       this.resetForm('theLines')
    //
    //       this.addDialog = false;
    //
    //       this.addStationDialogs.push({
    //         flag:false
    //       })
    //
    //     } else {
    //
    //       return false;
    //     }
    //   });
    // },
    // addStation(line,index, line_id, line_name, line_routing) {
    //   this.addStationDialogs[index].flag = false;
    //   let i = 0
    //   console.log('add==', line);
    //   for (; i < this.lineInfo.lines.length; i++) {
    //     if (this.lineInfo.lines[i].name === line.name) {
    //       break
    //     }
    //   }
    //   console.log(i,line.name)
    //   let indx = stationOrder -1
    //   this.workshopInfo.lines[i].stations.splice( indx, 0, {
    //     stationName: stationName,
    //     stationOrder: stationOrder,
    //     equipment: equipment,
    //     equipmentId: equipmentId,
    //
    //   })
    //   this.count = this.count + 1;
    //   this.addStationDialog = false;
    //
    //   this.stationOrder = '';
    //   this.stationName = '';
    //   this.equipmentId = '';
    //   this.equipment = '';
    // },


//提交批量增加产线
    submitForm(formName) {
      this.$router.push("/work/apply/lineInfo")
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let req = this.theLines
          req.theWorkshopId =this.nowShopworkId
          //req.user = this.$store.getters.userinfo.id
          // req.user = 1
          console.log(req)
          my_request({
            url: 'line/createAll',
            method: 'post',
            data: req
          }).then(res => {
            if (res.data.success===true){
              this.addDialog = false;
              this.$message({
                type: 'success',
                message: '创建成功'
              })

            }else {
              this.$message.error('创建失败')
            }
            //还得给他跳转到主页去
          }).catch(err => {
            this.$message.error('服务器异常')

          })
          console.log(this.theLines)
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    // submitForm(formName) {
    //   this.$refs[formName].validate((valid) => {
    //     if (valid) {
    //
    //       this.addLine(this.nowShopworkId, this.newLineName.newLineId, this.newLineName.newLineName,this.newLineName.newLineRouting)
    //     } else {
    //       this.$message.error('提交失败')
    //       return false;
    //     }
    //   });
    // },

    updateFormAddSta(formName, id, lineId, stationId,stationOrder, index, index1) {
      this.$refs[formName][0].validate((valid) => {
        if (valid) {
          this.editStation(id, lineId, stationId,stationOrder, index, index1)
          //this.$refs[formName][index].resetFields();


        } else {
          this.$message.error('提交失败')
          return false;
        }

      });
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
  overflow: auto;
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