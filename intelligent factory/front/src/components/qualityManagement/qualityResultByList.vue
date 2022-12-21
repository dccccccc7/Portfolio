<template>
  <div>
    <el-button style="margin-top: 3%;margin-left: 65%" type="primary" @click="returnLastPage" size="middle">返回</el-button>
  <div style="margin-top: 5%;margin-left: 15%">
    <!--    <search-bar @onSearch="searchResult" ref="searchBar" class="searchBar"></search-bar>-->
    <el-row class="el-row1">
      <el-tooltip effect="transparent" placement="bottom"
                  v-for="item in qualityForm.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                  :key="item.checkOrder">

          <el-card class="card" style="width: 700px;height: 300px" bodyStyle="padding:10px;" shadow="hover">
            <div id="pdfDom">
            <div style="margin-top: 3%;margin-left: 3%">
              <div class="clearfix" style="margin-top: 3%;margin-left: 3%" slot="header" >
                <span style="float: left">质检时间：{{ item.checkTime }}</span>
                <span>
                <i class="el-icon-download" @click="getPdf('#pdfDom')"></i>
              </span>
<!--                <span>-->
<!--                <i class="el-icon-delete" @click="deleteBook(item.name)"></i>-->
<!--              </span>-->
<!--                <span style="float:right;margin-right: 10px">-->
<!--                <i class="el-icon-edit" @click="editBook(item)"></i>-->
<!--              </span>-->

              </div>
              <div class="text item" style="margin-top: 5%;margin-left: 3%" >
                <el-divider></el-divider>
                <table>
                  <tr>
                    <th>订单数量</th>
                    <th>样品数量</th>
                    <th>不合格数量</th>
                    <th>不合格率</th>
                    <th>车间</th>
                    <th>生产线</th>
                    <th>工序</th>
                    <th>负责人员</th>
                  </tr>
                  <tr>
                    <td>{{item.orderNum}}</td>
                    <td>{{item.checkNum}}</td>
                    <td>{{ item.defectNum }}</td>
                    <td>{{ item.defectRatio * 100 }}%</td>
                    <td>{{ item.workshop }}</td>
                    <td>{{ item.productionLine }}</td>
                    <td>{{ item.procedure }}</td>
                    <td>{{ item.staff }}</td>
                  </tr>
                </table>
                <p>瑕疵类别：{{ item.defectType.replaceAll(","," ") }}</p>
                <p>瑕疵代号：{{ item.defectCode.replaceAll(","," ") }}</p>
              </div>
              <!--            <el-divider></el-divider>-->
              <!--            <i class="el-icon-edit" @click="editBook(item)"></i>-->
              <!--            <i class="el-icon-delete" @click="deleteBook(item.name)"></i>-->
            </div>
            </div>
          </el-card>

      </el-tooltip>
      <!--      <edit-form @onSubmit="" ref="edit"></edit-form>-->
    </el-row>
    <el-row>
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="qualityForm.length"
          :hide-on-single-page="true">
      </el-pagination>
    </el-row>
  </div>
  </div>
</template>

<script>
import EditForm from './EditForm'
import SearchBar from "./SearchBar";
import {request} from "@/network/request";
import qualityForm from "@/components/qualityManagement/qualityForm";

export default {
  name: 'qualityResultsByList',
  components: {EditForm, SearchBar},
  data() {
    return {
      list_id: '',//质检单号
      qualityForm: [],
      currentPage: 1,
      pagesize: 4,
      htmlTitle: '质检报告',
      company_id:'',

    }
  },


  mounted: function () {
    this.company_id = this.$store.getters.userinfo.company_id;
    this.list_id = this.$route.query.list_id;
    this.loadBefore()
  },
  methods: {


    loadBefore() {
      let req = {
        list_id: this.list_id,
        //加公司id
        company_id:this.$store.getters.userinfo.company_id,
      }

      var _this = this
      request({
        url: '/quality/queryResultByList',
        params: req,
        method: 'get',
      }).then(res => {
        let data = res.data;
        this.qualityForm = data;
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
      console.log(this.currentPage)
    },
    handleChange(val) {
      console.log(val);
    },
    searchResult() {
      //加公司id
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      var _this = this
      var url = '/quality/search?keywords=' + _this.$refs.searchBar.keywords
      request({
        url: url,
        methods: 'get',
        params: req,
      }).then(res => {
        let data = res.data;
        _this.qualityForm = data;
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    },
    deleteBook(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //加公司id
            this.$http
                .post('/quality/delete', {id: id,company_id:this.$store.getters.userinfo.company_id}).then(resp => {
              if (resp && resp.status === 200) {
                this.loadBefore()
              }
            })
          }
      ).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
      // alert(id)
    },
    returnLastPage () {
      this.$router.push({
        path:'/qualityManagement/qualityTaskListComponent',

      })
    },

    editBook(item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.qualityForm = {
        id: item.id,
        date: item.date,
        product: item.product,
        workshop: item.workshop,
        productionLine: item.productionLine,
        total: item.total,
        unqualifiedNum: item.unqualifiedNum,
        problemProcess: item.problemProcess,
        processPersonnel: item.processPersonnel,
        description: item.description,
        defect: {
          id: item.defect.id.toString(),
          name: item.defect.name
        }
      }
    },
  }
}
</script>
<style scoped>
.el-row1{
  width: auto;
  height: auto;
  display:flex;
  flex-wrap: wrap;
  align-items: flex-start;
}
th{
  padding-right: 15px;
  padding-bottom: 10px;
  color: rgb(192,192,192);
  font-size: 14px;
}
td{
  position: center;
  font-size: 18px;
}
/*.el-row1:after{*/
/*  content: '';*/
/*  flex-grow: 99999;*/
/*}*/
.card{
  width: 45%;
  max-height: max-content;
  float: left;
  margin-left: 2%;
  margin-right: 2%;
  margin-bottom: 10px;
}

img {
  width: 115px;
  height: 172px;
  /*margin: 0 auto;*/
}
.title {
  font-size: 14px;
  text-align: left;
}
.el-icon-delete,.el-icon-edit,.el-icon-download{
  cursor: pointer;
  font-size: 20px;
  float: right;
}

a {
  text-decoration: none;
}

a:link, a:visited, a:focus {
  color: #3377aa;
}

.text {
  font-size: 14px;
  text-align: left;
}

.item {
  margin-bottom: 0px;
}

.searchBar {
  margin-top: 0%;
  margin-bottom: 3%;
  left: 30%
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>

