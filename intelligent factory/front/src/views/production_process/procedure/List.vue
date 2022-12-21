<template>
  <div id="list-base">
     <search-bar @onSearch="searchResult" ref="searchBar" class="searchBar"></search-bar>

    <div id="list-main">
      <el-table :data="procedures" border>
        <el-table-column prop="procedure_id" label="id" min-width="3"  align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="name" label="名称" min-width="2"   align="center"></el-table-column>
        <el-table-column prop="cost" label="成本" min-width="2"   align="center"></el-table-column>
        <el-table-column prop="status" label="状态" min-width="2"  align="center"></el-table-column>
        <el-table-column prop="comments" label="产品描述" min-width="2"  align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="操作" id="tool" min-width="2"  align="center">
          <template slot-scope="scope">
            <el-button type="primary" circle size="mini" icon="el-icon-edit" @click="showEdit(scope.row)"></el-button>
            <el-button type="danger" circle size="mini" icon="el-icon-delete" @click="del(scope.$index, scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="list-page">
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

      <el-drawer ref="detailDrawer" title="编辑" :visible.sync="isShowEdit" size="40%" :destroy-on-close="true" :before-close="handleClose">
        <edit-page :procedure_id="procedure_id" :detailDrawer="$refs.detailDrawer" :changeCommit="changeCommit"></edit-page>
      </el-drawer>
    </div>
  </div>
</template>

<script>
import {request} from "@/network/request";
import editPage from "./Edit"
import SearchBar from "@/components/procedureManagement/SearchBar";

export default {
  name: "List",
  data() {
    return {
      currentPage: 1,
      size: 5,
      isCommit: false,
      count: 0,
      procedures: [],
      procedure_id: '',
      isShowEdit: false
    }
  },
  methods: {
    handleSizeChange(val) {
      this.size = val
      this.getProcedure(this.currentPage, val , this.$store.getters.userinfo.company_id)
    },

    handleCurrentChange(val) {
      this.currentPage = val
      this.getProcedure(val, this.size , this.$store.getters.userinfo.company_id)
    },

    getProcedure(currentPage, size , company_id) {
      request({
        url: '/process/getProceduresInfo',
        method: 'get',
        params: {
          pageOffset: currentPage,
          pageSize: size,
          //后续修改
          company_id: company_id
        }
      }).then(res => {
        this.count = res.data.count
        this.procedures = res.data.procedures
      }).catch(err => {
        console.log(err)
      });
    },

    del(index, row) {
      this.$confirm("是否删除该工序？", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: '/process/deleteProcedureByID',
          method: 'post',
          data: {
            procedure_id: row.procedure_id,
            operator_id: 'CM',
            company_id:this.$store.getters.userinfo.company_id
          }
        }).then(res => {
          if (res.data == "删除成功") {
            this.getProcedure(this.currentPage, this.size , this.$store.getters.userinfo.company_id) //company-id
            this.$message({
              type: 'success',
              message: '删除成功'
            })
          } else {
            this.$message({
              type: 'error',
              message: res.data
            })
          }
        }).catch(err => {
          this.$message({
            type: 'error',
            message: '网络错误，删除失败'
          })
        });
      })
    },

    showEdit(row) {
      this.procedure_id = row.procedure_id
      this.isShowEdit = true
    },

    handleClose(done) {
      if (!this.isCommit) {
        this.$confirm("关闭后会丢失本次修改，是否关闭？").then(() => {
          done()
        }).catch(() => {

        })
      }
      else {
        this.getProcedure(this.currentPage, this.size ,this.$store.getters.userinfo.company_id)
        done()
      }
      this.isCommit = false
    },
//搜索
      searchResult() {
      const _this = this;
      const url = '/process/getProcedureInfoByName?name=' + _this.$refs.searchBar.keywords + '&company_id='+ this.$store.getters.userinfo.company_id;
      if(_this.$refs.searchBar.keywords.length==0){

        this.getProcedure(this.currentPage, this.size ,this.$store.getters.userinfo.company_id)

      }else{
      request({
        url: url,
        methods: 'get'
      }).then(
        res => {
          this.count=1
        this.procedures = res.data
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });}
    },

    changeCommit() {
      this.isCommit = true
    }

  },
  components: {
    editPage,
    SearchBar,
  },
  created() {
    this.getProcedure(this.currentPage, this.size ,this.$store.getters.userinfo.company_id)
  },
  activated() {
    this.getProcedure(this.currentPage, this.size , this.$store.getters.userinfo.company_id)
  }
}
</script>

<style scoped>
#list-base {
  padding: 50px 50px 50px 50px;
}
#tool {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
#list-page {
  margin-top: 20px;
}
.searchBar {
  margin-top: 2%;
  margin-bottom: 0%;
  left: 30%
}

#list-main{
  width:71vw;
}
</style>