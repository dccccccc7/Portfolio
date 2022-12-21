<template>
  <div style="margin-left: 90px;margin-top: 25px" id="theBaseMainBox">
    <div style="height: 20px"></div>

    <div id="theBaseHeader">
      <div>
        <el-button style="margin-left: 55%" type="primary" @click="clearFilter" >清除所有过滤器</el-button>

      </div>

      <!--实现查询的输入框和按钮    start-->
      <div id="theLeftBase">
        <el-form :inline="true" :model="formInline" class="demo-form-inline" ref="formInline" :rules="rules">

          <el-form-item  label="选择信息">
            <el-select v-model="formInline.category" placeholder="选择信息" @change="selectChange">
              <el-option label="车间" value="workshop"></el-option>
              <el-option label="产线" value="line"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="查询信息" prop="info">
            <el-input v-model.trim="formInline.info" :placeholder="this.showLine===false?'车间编号/名称':'车间编号/名称+产线编号/名称'"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="margin-left: 10%" type="primary" @click="search(formInline)">查询</el-button>

          </el-form-item>
        </el-form>


      </div>
      <div id="theRightBase">
        <el-button :plain="true" icon="el-icon-plus" style="margin-right: 150%" @click="createItem">新建车间</el-button>
      </div>
    </div>

    <div style="height: 20px"></div>

    <div id="theBaseMain">
      <!--      表格主题       -->

      <div v-if="!showLine">
        <search-bar @onSearch="search" ref="searchBar" class="searchBar"></search-bar>
        <el-row>

        <el-tooltip effect="transparent" placement="bottom"

                    v-for="(item,index) in workshopForm"
                    >

        <el-card
            style="width: 300px;height: 200px"
            class="card"
            bodyStyle="padding:10px"
            shadow="hover">
          <div class="info">
            <div class="clearfix" slot="header" >

              <span style="float: left">车间编号：{{item.id}}</span>

              <span style="margin-left: 70px;margin-top: 2px">
                <i class="el-icon-delete" @click="delWrokshop(item.id)" />
<!--                <el-button style="margin-left: 20%" type="danger" size="mini" icon="el-icon-delete" circle @click="delWrokshop(item.id)"></el-button>-->
              </span>

<!--              <el-button-->
<!--                  v-loading.fullscreen.lock="fullscreenLoading"-->
<!--                  @click="editWorkShopName[index].flag = true"-->
<!--                  style="float: right; padding: 3px 3px;margin-left: 6px"-->
<!--                  icon="el-icon-edit"-->
<!--                  circle-->
<!--              ></el-button>-->

              <span
                  v-loading.fullscreen.lock="fullscreenLoading"
                  style="float: right;
                  margin-right: 15px"
              ><i class="el-icon-edit" @click="editWorkShopName[index].flag = true" />
                <!--                 <el-button style="margin-left:30%"  type="primary" size="mini" icon="el-icon-edit" circle @click="editworkshop(item)"></el-button>-->
              </span>

              <el-dialog
                  title="添加/修改车间"
                  :visible.sync="editWorkShopName[index].flag"
                  width="30%"
                  :before-close="handleClose">

                <el-form :model="newWorkshopForm" ref="newWorkshopForm" label-width="100px" class="demo-ruleForm">
                  <el-form-item
                      label="车间编号"
                      prop="id"
                      :rules="{ required: true, message: '车间编号不能为空'}">
                    <el-input type="age" v-model="newWorkshopForm.id" :placeholder="item.id" style="width: 100%;"></el-input>
                  </el-form-item>
                  <el-form-item
                      label="车间名称"
                      prop="name"
                      :rules="{ required: true, message: '车间名称不能为空'}">
                    <el-input type="age" v-model="newWorkshopForm.name" :placeholder="item.name" style="width: 100%;"></el-input>
                  </el-form-item>
                  <el-form-item
                      label="工厂名称"
                      prop="factory_name"
                      :rules="{ required: true, message: '工厂名称不能为空'}">
                    <el-input type="age" v-model="newWorkshopForm.factory_name" :placeholder="item.factory_name"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="submitToUpdate(item.id)">修改</el-button>
                  </el-form-item>
                </el-form>

              </el-dialog>

<!--              <span style="float: right;margin-right: 15px">-->
<!--                <i class="el-icon-edit" @click="editworkshop(item)" />-->
<!--&lt;!&ndash;                 <el-button style="margin-left:30%"  type="primary" size="mini" icon="el-icon-edit" circle @click="editworkshop(item)"></el-button>&ndash;&gt;-->
<!--              </span>-->

                <!--            加参数：        -->
                <router-link :to="{
              path: '/work/workshop/workshopDetail',
              query: {
              workshopId: item.id,
              workshopName: item.name,
              factory_name:item.factory_name
              }
            }">
                  <!--            <el-button style="padding: 8px" type="text" @click="handleClick({type:!showLine,id:scope.row.workshopId,name:scope.row.workshopName})">查看</el-button>-->
                  <span style="float: right;margin-right: 15px">
<!--                    <el-button type="warning" style="margin-left: 35%" size="mini" icon="el-icon-message" circle></el-button>-->
                      <i class="el-icon-document"></i>
                  </span>
                </router-link>


<!--              <span style="float: right;margin-right: 15px">-->
<!--                <i class="el-icon-document" @click="editDefect(item)"></i>-->
<!--              </span>-->
            </div>
            <div class="text item">
              <el-divider></el-divider>
              <p>名称：{{ item.name}}</p>
              <p>工厂：{{ item.factory_name}}</p>
            </div>
          </div>
        </el-card>
        </el-tooltip>
          <workshopForm @onSubmit="loadWorkshops()" ref="edit"></workshopForm>
        </el-row>
      </div>

<!--      展示产线-->
      <div v-else>
        <el-table
            v-loading="loading"
            id="theSubTable"
            :data="tableLineData"
            highlight-current-row
            stripe
            @sort-change="sortchange"
            style="width: 100%">
          <el-table-column
              label="车间编号"
              prop="workshop_id">
          </el-table-column>
          <el-table-column
              label="产线编号"
              prop="id">
          </el-table-column>
          <el-table-column
              label="产线名称"
              prop="name"
              width="180">
          </el-table-column>
          <el-table-column
              label="设备数量"
              prop="equipNum">
          </el-table-column>
          <el-table-column
              label="工位数量"
              prop="stationNum"
              width="200px">
          </el-table-column>
          <el-table-column
              label="创建日期"
              prop="created_time"
              width="200px">
          </el-table-column>

<!--          <el-table-column-->
<!--              fixed="right"-->
<!--              label="操作"-->
<!--              width="200">-->
<!--            <template slot-scope="scope">-->
<!--              &lt;!&ndash;              <el-button style="padding: 8px" type="text" @click="handleClick({type:!showLine,id:scope.row.workshopId,name:scope.row.workshopName})">查看</el-button>&ndash;&gt;-->
<!--              &lt;!&ndash;            加参数：        &ndash;&gt;-->
<!--              <router-link :to="{-->
<!--              path: '/work/workshop/workshopDetail',-->
<!--              query: {-->
<!--              workshopId: scope.row.workshop_id,-->
<!--              workshopNmae:'s'-->
<!--              }}">-->
<!--                <el-button style="padding: 8px" type="text">查看</el-button>-->
<!--              </router-link>-->
<!--            </template>-->
<!--          </el-table-column>-->
        </el-table>
      </div>





      <div class="block" style="padding: 10px;margin-right: 125px">
        <el-pagination
            @size-change="current-change"
            @prev-click="preclick"
            @next-click="nextclick"
            @current-change="curChange"
            :hide-on-single-page=true
            :total="total"
            background
            layout="total,prev, pager, next, jumper"
            :page-size="page.pagesize">
        </el-pagination>
      </div>


    </div>
  </div>
</template>

<script>
import {request} from "@/network/request";
import {my_request} from "@/network/requests/workshop_request";
import workshopForm from "@/components/baseShop/workshopForm";
export default {
  name: "baseview",
  components: {workshopForm},
  data() {

    return {

      newWorkshopForm: {
        id: '',
        name: '',
        factory_name: '',
      },

      editWorkShopName: [],
      fullscreenLoading: false,
      dialogFormVisible: false,
      workshopForm: [],
      total:1,
      formInline: {
        category: 'workshop',
        info: ''
      },
      value: 'workshop',

      page: {
        pagesize: 6,
        curpage: 1,
      },
      // sortForm: '车间编号',
      tableData: [],
      tableLineData: [],
      workshopItem: '',
      //workshopData:[],


      loading: false,
      //是否展示当前页面， 作为一个表示
      showLine: false,

      path: '',

      rules: {

        info: [
          {required: true, message: ' ', trigger: 'blur'},
          {min: 1, message: '信息不可为空', trigger: 'blur'}
        ],
      }
    }

  },
  created() {
    this.getData()
  },
  activated() {
    this.$router.push(this.path);
    this.getData()
  },
  beforeRouteLeave(to, from, next) {
    this.path = this.$route.path;
    next();
  },
  methods: {

    clearFilter() {
      this.getData();
    },

    //时间格式转换
    formatDate(value) {
      var year = value.substr(0, 4)
      var month = value.substr(5, 2)
      var day = value.substr(8, 2)
      var hour = value.substr(11, 2)
      var min = value.substr(14, 2)
      var second = value.substr(17,2)
      return year + "-" + month + "-" + day + " " + hour + ":" + min + ":"+second
    },

    //修改车间
    submitToUpdate(id) {
      this.$router.push("/work/apply/workshopInfo")
      let req = {
        workshopId: id,
        company_id:this.$store.getters.userinfo.company_id,
        newWorkshopId: this.newWorkshopForm.id,
        newWorkshopName: this.newWorkshopForm.name,
        newFactory: this.newWorkshopForm.factory_name
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
        }else {
          this.$message.error('申请失败！车间编号已存在或正在修改中')
        }
        // this.getData()
        // this.nowShopworkId = req.workshopId
        console.log('修改：',res)
      }).catch(err => {
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

    loadWorkshops() {
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      const _this = this;
      request({
        url: '/mainInfo',
        method: 'get',
        params: req,
      }).then(res => {
        const data = res.data;
        _this.workshopForm = data;
        console.log(_this.workshopForm);
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    },


    editworkshop(item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.workshopForm = {
        id: item.id,
        name: item.name,
        factory_name: item.factory_name,
      }
    },


    //删除车间
    delWrokshop(id) {
      let req = {
        workshopId: id,
        company_id:this.$store.getters.userinfo.company_id,
      }
      request({
        url: 'workshop/deleteAll',
        params: req,
        method: 'post'
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
    //  翻页  ----------------------------
    preclick(curpage) {
      this.page.curpage = curpage
      this.loading = true
      this.getData()
      this.loading = false

    },
    nextclick(curpage) {
      this.page.curpage = curpage
      this.loading = true
      this.getData()
      this.loading = false

    },
    curChange(curpage) {
      this.page.curpage = curpage
      this.loading = true
      setTimeout(() => {
        this.getData()
        this.loading = false
      }, 1000)
    },

    //页表上方的排序
    sortchange() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },

    //选择当前页面展示信息分类
    selectChange() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
        this.showLine = !this.showLine
        this.getData()
      }, 1000)
    },

    //搜索方法
    search(formInline) {
      //---------------------------------------开始查寻数据。
      this.$refs.formInline.validate((valid) => {
        if (valid) {
          // 如果通过验证 则开始进行搜索------------
          this.getData({type: 'search', data: this.formInline.info})
        } else {
          return false;
        }
      });
    },

    //已经磨的用了
    handleCommand(command) {
      this.sortForm = command
      this.$message('click on item ' + command);
    },

    //查看每一行数据的 已经ok 了吧。  访问数据在另一个网页进行访问
    handleClick(obj) {
      if (obj.type) {
        this.$router.push({
          path: '/work/workshop/workshopDetail',
          query: {
            workshopId: obj.id,
            workshopName: obj.name
          }
        })
      } else {
        this.$router.push({
          path: '/work/workshop/line/lineDetail',
          query: {
            lineId: obj.id,
            lineName: obj.name
          }
        })
      }

    },

    //新建项目的 网页
    createItem() {
      this.$router.push('/test/baseTest/createPro')
    },


    getData(obj = {type: 'def'}) {

      //先看是哪个类型的 车间 还是 产线：
      if (!this.showLine) {
        //根据 obj的类型 选择不同的查询内容：
        if (obj.type === 'search') {
          //  -----------查询车间内容数据返回。  查询的内容在 obj.data 里面
          this.loading = true
          let req = {
            info: obj.data,
            company_id:this.$store.getters.userinfo.company_id,

          }
          my_request({
            url: 'workshop/searchWorkshop',
            params: req,
            methods: 'get'
          }).then(res => {

            if (res.data.success === true){
              this.$message({
                type:'success',
                message:'查询成功'
              })
              console.log('查询结果',res)
              let s = []
              s.push(res.data.result)
              //s.push(res.data)
              //this.tableData = s;
              this.workshopForm = s;
              this.total = res.data.total;
              this.loading = false
            }else {
              this.$message.error('暂未查询到此记录')
            }

          }).catch(err => {
            this.$message.error('服务器异常')
          })

          this.formInline.info = ''
          this.loading = false
        } else {
          ('2');

          let req = {
            currentPage: this.page.curpage,
            pagesize: this.page.pagesize,
            company_id:this.$store.getters.userinfo.company_id,
          }
          this.loading = true
          my_request({
            //设置一下让他请求 从 ( curpage - 1 ) *  pageSize 开始  一共 pageSize个数据 -------------------------------
            url: 'workshop/workshopItem',
            params: req,
            methods: 'get'
          }).then(res => {
            console.log('凡回家过',res)
            if (res.data.message==='操作成功！'){
              //this.tableData = res.data.result.records
              this.workshopForm = res.data.result.records
              //this.tableData = res.data

              this.total = res.data.result.total
            }else {
              this.$message.error(res.data.message)
            }

          }).catch(err => {
            this.$message.error('服务器异常')
          })

          this.loading = false
        }
      } else {
        if (obj.type === 'search') {
          if (obj.data.indexOf('+') !== -1 && obj.data.split('+').length === 2 && obj.data.split('+')[0] !== '' && obj.data.split('+')[1] !== '') {
            this.loading = true
            //  -----------查询产线内容数据返回。  查询的内容在 obj.data 里面
            let s = obj.data.split("+")
            let req = {
              Id: s[0],//车间
              info:s[1],//产线
              company_id:this.$store.getters.userinfo.company_id,
            }
            this.loading = true
            console.log('搜索',req)
            my_request({
              url: 'line/searchLine',
              params: req,
              methods: 'get'
            }).then(res => {
              console.log('产线查询:',res)
              if(res.data.success === true) {
                let s = []
                s.push(res.data.result)
                this.tableLineData = s
                this.total = res.data.total;

                for (let item of this.tableLineData) {
                  item.created_time = this.formatDate(item.created_time)
                }

                this.$message({
                  type:'success',
                  message:'查询成功'
                })
              }else{
                this.$message.error('查无此记录')
              }

            }).catch(err => {
              this.$message.error('服务器异常')
            })
            this.loading = false

            this.formInline.info = ''
          }else {
            this.$message({
              type:'info',
              message:'请输入正确格式'
            })
          }
        } else {
          let req = {
            currentPage: this.page.curpage,
            pagesize: this.page.pagesize,
            company_id:this.$store.getters.userinfo.company_id,
          };
          this.loading = true

          my_request({
            //设置一下让他请求 从 curpage *  ( pageSize - 1 ) 开始  一共 pageSize个数据 -------------------------------
            url: 'line/searchLineItem',
            params: req,
            methods: 'get'
          }).then(res => {
            console.log('产线信息',res)
            this.tableLineData = res.data.result.records
            this.total = res.data.result.total

            for (let item of this.tableLineData) {
              item.created_time = this.formatDate(item.created_time)
            }

          }).catch(err => {
            this.$message.error('服务器异常')
          })
          this.loading = false
        }

      }

      for (let i = 0; i < 10; i++) {
        this.editWorkShopName.push({flag: false})
      }


    },
  }
}
</script>

<style scoped>

#theBaseMainBox {
  display: flex;
  /*background-color: gray;*/
  height: 100%;
  flex-direction: column;
  width: auto;
}

#theBaseHeader {
  /*background-color: aliceblue;*/
  height: 5%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  /*align-items: center;*/
}

#theBaseMain {
  /*background-color: beige;*/
  height: 100%;

  width: inherit;
  padding: 0 80px;

}

#theLeftButton {
  display: flex;
  align-items: flex-end;
}

.theLeftHeaderInput {
  display: flex;
  height: 40px;
  width: 300px;
  /*justify-content: space-between;*/
}

#theLeftBase {
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: auto
}

#theRightBase {
  width: 20%;
  height: 40px;
  display: flex;
  justify-content: space-around;
  align-items: center;
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

.card{
  width: 20%;
  max-height: max-content;
  float: left;
  margin-right: 15px;
  margin-bottom: 20px;
}


.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
  /*size: ;*/
}
/deep/  td {
  padding:3px !important;
  /*border: 1px solid blue;*/
  height: 6px;
  overflow: hidden;
}

</style>
