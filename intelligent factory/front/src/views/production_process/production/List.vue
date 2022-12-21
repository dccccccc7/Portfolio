<template>
  <div>

    <div id="list-base">
      <search-bar @onSearch="searchResult" ref="searchBar" class="searchBar"></search-bar>

      <div id="list-main" >
        <el-table :data="products" border>
          <el-table-column prop="product_id" label="id" min-width="3" align="center" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="brand" label="品牌" min-width="1" align="center"></el-table-column>
<!--          <el-table-column prop="season" label="季节" width="100" align="center"></el-table-column>-->
          <el-table-column prop="type" label="产品类别" min-width="1" align="center"></el-table-column>
          <el-table-column prop="customer_id" label="客户款号" min-width="1" align="center"></el-table-column>
          <el-table-column prop="company" label="公司款号" min-width="1" align="center"></el-table-column>
          <el-table-column prop="color" label="颜色" min-width="1" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" min-width="1" align="center"></el-table-column>
          <el-table-column prop="comments" label="产品描述" min-width="2" align="center" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" id="tool" min-width="2" align="center">
            <template v-slot="scope">
              <el-button type="primary" circle size="mini" icon="el-icon-more" @click="showDetail(scope.row)"></el-button>
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
      </div>

      <el-drawer title="详情" :visible.sync="isShowDetail" size="40%" :destroy-on-close="true">
        <detail-page :product_id="product_id"></detail-page>
      </el-drawer>

      <el-drawer ref="detailDrawer" title="编辑" :visible.sync="isShowEdit" size="40%" :destroy-on-close="true" :before-close="handleClose">
        <edit-page :product_id="product_id" :detailDrawer="$refs.detailDrawer" :changeCommit="changeCommit"></edit-page>
      </el-drawer>

      <div id="list-bottom"></div>
    </div>
  </div>
</template>

<script>
import {request} from "@/network/request";
import detailPage from "./Detail"
import editPage from "./Edit"
import SearchBar from "@/components/productManagement/SearchBar";

export default {
  name: "list",
  data() {
    return {
      currentPage: 1,
      size: 5,
      isCommit: false,
      count: 0,
      products: [],
      isShowDetail: false,
      product_id: '',
      isShowEdit: false
    }
  },
  methods: {
    handleSizeChange(val) {
      this.size = val
      this.getProduct(this.currentPage, val)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getProduct(val, this.size)
    },
    getProduct(currentPage, size) {
      request({
        url: '/process/getProductsByCompany',
        method: 'get',
        params: {
          pageOffset: currentPage,
          pageSize: size,
          company_id: this.$store.getters.userinfo.company_id
        }
      }).then(res => {
        this.count = res.data.count
        this.products = res.data.products
      }).catch(err => {
        console.log(err)
      });
    },
    del(index, row) {
      this.$confirm("是否删除该产品？", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: '/process/deleteProductByID',
          method: 'post',
          data: {
            operator_id: 'CM',
            product_id: row.product_id
          }
        }).then(res => {
          console.log(res.data)
          if (res.data === "删除成功") {
            this.getProduct(this.currentPage, this.size)
            this.$message({
              type: 'success',
              message: '删除成功'
            })
          } else {
            this.$message({
              type: 'error',
              message: '删除失败'
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
    showDetail(row) {
      this.product_id = row.product_id
      this.isShowDetail = true
    },
    showEdit(row) {
      this.product_id = row.product_id
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
        this.getProduct(this.currentPage, this.size)
        done()
      }
      this.isCommit = false
    },
    changeCommit() {
      this.isCommit = true
    },
    //getproducts
    searchResult() {
      if(this.$refs.searchBar.keywords.length===0){

        this.getProduct(this.currentPage, this.size)

      }else{
      request({
      url: '/process/getProductsByCompanyAndType',
      methods: 'get',
      params: {
        pageOffset: 1,
        pageSize: this.size,
        company_id: this.$store.getters.userinfo.company_id,
        type:this.$refs.searchBar.keywords
      }
    }).then(res => {
      this.count = res.data.products.length;
      this.products=res.data.products;
      console.log(this.count)
    }).catch(err => {
      this.$message.error('网络出错，请稍后再试');
    });}
    },},
  components: {
    detailPage,
    editPage,
    SearchBar,
  },
  created() {
    this.getProduct(this.currentPage, this.size)
  },
  activated() {
    this.getProduct(this.currentPage, this.size)
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

#list-main{
  width:80vw;
}
.searchBar {
  margin-top: 0;
  margin-bottom: 0;
  margin-left: 55%
}
</style>