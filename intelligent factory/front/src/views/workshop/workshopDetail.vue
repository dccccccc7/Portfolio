<template>
  <div id="mainDiv">
    <div id="header">
      <div class="headerRight">
        <el-button @click="updateOrSubmit" type="primary" plain>&nbsp;&nbsp;&nbsp;{{ isDisabled ? '修改' : '申请' }}&nbsp;&nbsp;&nbsp;</el-button>
        <i v-if="workshop.status" style="color: #ff7300">&nbsp;&nbsp;&nbsp;&nbsp;正在审核ing....</i>
        <el-dialog
            title="提示"
            :visible.sync="showDialog"
            width="30%"
            :before-close="handleClose">
          <span>是否确认提交修改申请？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="canSubmit">取 消</el-button>
            <el-button type="primary" @click="conSubmit">确 定</el-button>
          </span>
        </el-dialog>

      </div>
    </div>
    <div class="subMain">
      <div class="item">
        <span>车间编号:</span>
        <div class="inputBox">
          <el-input
              :disabled="isDisabled"
              :placeholder="workshop.id"
              v-model="theId"
              clearable>
          </el-input>
        </div>

      </div>
      <div class="item">
        <span>车间名称:</span>
        <div class="inputBox">
          <el-input
              :disabled="isDisabled"
              :placeholder="workshop.name"
              v-model="theName"
              clearable>
          </el-input>
        </div>

      </div>
      <div class="item">
        <span>工厂名称:</span>
        <div class="inputBox">
          <el-input
              :disabled="isDisabled"
              placeholder="workshop.factory"
              v-model="theFactory"
              clearable>
          </el-input>
        </div>
      </div>
    </div>
    <div class="subMain">
      <div class="buttonDiv">
        <i style="font-size: large;color: lightskyblue">下属产线</i>
        <el-button icon="el-icon-plus" @click="dialogFormVisible = true" disabled size="medium">增加产线</el-button>
        <i style="font-size: small;color: gray">在通过审核后可对产线进行操作</i>
                <el-dialog width="25%" title="产线信息" :visible.sync="dialogFormVisible">
                    <el-form >
  <!--                    就给一个产线名称  后面的内容  在进入编辑页面进行编辑            -->
                      <el-form-item label="产线名称 :" :label-width="formLabelWidth">
                        <el-input  v-model="lineName" aria-rowindex="20%" autocomplete="off"></el-input>
                      </el-form-item>
                    </el-form>

                    <div slot="footer" class="dialog-footer">
                      <el-button @click="cancelClick">取 消</el-button>
                      <el-button type="primary" @click="confirmClick">确 定</el-button>
                    </div>

                </el-dialog>


      </div>

      <div style="width: 1500px">
        <el-table
            v-loading="loading"
            id="theSubTable"
            :data="workshop.productionLine"
            highlight-current-row
            stripe
            @sort-change="sortchange"
            style="width: 100%">
          <el-table-column
              label="产线编号"
              prop="lineId"
              sortable>
          </el-table-column>
          <el-table-column
              label="产线名称"
              prop="lineName"
              width="180">
          </el-table-column>
          <el-table-column
              label="设备数量"
              sortable
              prop="lineEqu">
          </el-table-column>
          <el-table-column
              label="工位数量"
              prop="stateNum"
              sortable
              width="200px">
          </el-table-column>
          <el-table-column
              label="创建日期"
              prop="createDate"
              sortable
              width="200px">
          </el-table-column>
          <el-table-column
              label="修改日期"
              prop="updateDate"
              sortable
              width="200px">
          </el-table-column>
          <el-table-column
              fixed="right"
              label="操作"
              width="200">
            <template slot-scope="scope">
<!--              <router-link :to="{-->
<!--              path: '/work/workshop/line/lineDetail',-->
<!--              query: {-->
<!--              id: scope.row.lineId,-->
<!--              name: scope.row.lineName-->
<!--              }}">-->
                <el-button style="padding: 8px" @click="editLine(scope.row.lineId,scope.row.lineName)" disabled type="text">查看</el-button>
<!--              </router-link>-->
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>

  </div>
</template>

<script>
import {request} from "@/network/request";

export default {

  data() {
    return {
      showDialog: false,
      isDisabled: true,
      theId: '',
      theName: '',
      theFactory:'',

      workshop: '',
      dialogFormVisible: false,
      lineName: '',
      formLabelWidth: '80px',
      loading : false,

      nowWorkshopId: this.$route.query.workshopId,
      nowWorkshopName: this.$route.query.workshopName
    };


  },
  name: "workshopDetail",


  created() {
    // ---------------------------  要根据这个 查询对应仓库的信息
    this.getData()
  },
  activated() {
    // ---------------------------  要根据这个 查询对应仓库的信息
    this.getData()
  },
  methods: {
    getData() {

      let req = {
        company_id:this.$store.getters.userinfo.company_id,
        workshopId: this.nowWorkshopId,
        workshopName: this.nowWorkshopName
      }
      console.log('this',req)
      request({
        url: 'workshop/workshopDetail',
        params: req,
        methods: 'get'
      }).then(res => {
        this.workshop = res.data.workshopDetail;
        // console.log(res.data.workshopDetail.productionLine);

      }).catch(err => {
        this.$message.error('错了哦，这是一条错误消息');
      })
      // console.log(this.workshop);

    },
    //------按钮改变的-----------------------------------
    updateOrSubmit() {
      if (this.isDisabled) {
        this.isDisabled = !this.isDisabled;
      } else {
        this.showDialog = true
      }
    },

    handleClose(done) {
      //这里有问题 不会用 dialog  等等看看
      this.$confirm('确认关闭？')
          .then(res => {

            this.isDisabled = !this.isDisabled
            done();
          })
          .catch(err => {
          });
    },

    //这里 实现点击编辑 跳转到产线的详情页面：
    editLine(item){
      // console.log(item);
    //  进行路由跳转了。。
      this.$router.push({
        path : '/work/workshop/line/lineDetail',
        query: {
          lineId: item.id,
          lineName: item.name
        }
      })
    },

    //新建产线的两个函数
    confirmClick(){
      this.dialogFormVisible = false;
      // console.log(this.lineName);
    //  -------------------  发起请求 创建 名为  this.lineName 的产线

    //  ----------------
      this.lineName=''   //在变为空，预备下一次的新建产线

    },
    cancelClick() {
      this.dialogFormVisible = false;
    },

    //提交修改的两个函数：                ---------------------------------------------------------就这里一个提交申请的e
    conSubmit(){
      this.showDialog = false
      this.isDisabled = !this.isDisabled

      let req = {
        company_id:this.$store.getters.userinfo.company_id,
        workshopid:this.workshop.id,
        newWorkshopId : this.theId,
        workshopName : this.theName,
        factory : this.theFactory
      }
      request({
        url: 'workshop/workshopDetail/update',
        params: req,
        methods: 'get'
      }).then(res=>{
        this.$message({
          message: '修改成功',
          type: "success"
        });
        this.nowShopworkId = this.workshop.id
        this.getData()
      }).catch(err=>{
        this.$message.error('修改失败')
      })
    //  将新的数据workshop  post到 后台进行修改
    },
    canSubmit(){
      this.showDialog = false
      this.isDisabled = !this.isDisabled

    },

    //查看


    sortchange(){
      this.loading = true
      setTimeout(()=>{
        this.loading=false
      },1000)
    },
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

</style>