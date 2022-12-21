<!--显示所有瑕疵-->
<template>
  <div>
    <search-bar style="margin-right: 15%" @onSearch="searchResult" ref="searchBar" class="searchBar"></search-bar>
    <el-row class="el-row1">
      <el-tooltip effect="transparent" placement="bottom"
                  v-for="item in defectForm.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                  :key="item.defectCode">
        <el-card style="width: 250px;height: 200px" class="card" bodyStyle="padding:10px" shadow="hover">
          <div class="info">
            <div class="clearfix" slot="header" >
              <span style="float: left">瑕疵代号：{{ item.defectCode}}</span>
              <span>
                <i class="el-icon-delete" @click="deleteDefect(item.defectCode)" />
<!--                <el-button style="margin-top:3%;margin-left: 20%" type="danger" size="mini" icon="el-icon-delete" circle @click="deleteDefect(item.defectCode)"></el-button>-->
              </span>
              <span style="float: right;margin-right: 10px">
                <i class="el-icon-edit" @click="editDefect(item)" />
<!--                 <el-button style="margin-top:26%;margin-left: 25%" type="primary" size="mini" icon="el-icon-edit" circle @click="editDefect(item)"></el-button>-->
              </span>
            </div>
            <div class="text item">
              <el-divider></el-divider>
              <p>类别：{{ item.classification}}</p>
              <p>描述：{{ item.defectType}}</p>
            </div>
<!--            <el-divider></el-divider>-->
<!--            <i class="el-icon-edit" @click="editDefect(item)"></i>-->
<!--            <i class="el-icon-delete" @click="deleteDefect(item.defectCode)"></i>-->
          </div>
        </el-card>
      </el-tooltip>

      <defect-form @onSubmit="loadDefects()" ref="edit"></defect-form>
    </el-row>
    <el-row>
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="defectForm.length"
          :hide-on-single-page="true">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
import defectForm from "@/components/qualityManagement/defect/defectForm";
import SearchBar from "@/components/qualityManagement/SearchBar";
import {request} from "@/network/request";

export default {
  name: 'defectAll',
  components: {defectForm, SearchBar},
  data() {
    return {
      defectForm: [],
      currentPage: 1,
      pagesize: 7,
      company_id:'',
    }
  },
  mounted: function () {
    this.loadDefects();
    this.company_id = this.$store.getters.userinfo.company_id;
  },
  methods: {
    loadDefects() {
      //公司id
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      const _this = this;
      request({
        url: '/defect/all',
        method: 'get',
        params: req,
      }).then(res => {
        const data = res.data;
        _this.defectForm = data;
        console.log(_this.defectForm);
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
      //添加公司id
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      const _this = this;
      const url = '/defect/search?keywords=' + _this.$refs.searchBar.keywords;
      request({
        url: url,
        methods: 'get',
        params: req,
      }).then(res => {
        let data = res.data;
        _this.defectForm = data;
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    },
    deleteDefect(defectCode) {
      this.$confirm('此操作将永久删除该瑕疵, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(resp => {
        request({
            url:'/defect/delete',
            method: 'post',
          //添加公司id
            data: {defectCode:defectCode,company_id:this.$store.getters.userinfo.company_id,},
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.loadDefects()
          }
        }).catch(err => {
          this.$message.error('网络出错，请稍后再试');
        });
      }
      ).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
      // alert(id)
    },
    editDefect(item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.defectForm = {
        classification: item.classification,
        defectCode: item.defectCode,
        defectType: item.defectType,
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
/*.el-row1:after{*/
/*  content: '';*/
/*  flex-grow: 99999;*/
/*}*/
.card{
  width: 20%;
  max-height: max-content;
  float: left;
  margin-right: 15px;
  margin-bottom: 20px;
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

.info {
  /*display: flex;*/
}

.el-icon-delete,.el-icon-edit{
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
  margin-bottom: 18px;
}

.searchBar {
  margin-top: 2%;
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

