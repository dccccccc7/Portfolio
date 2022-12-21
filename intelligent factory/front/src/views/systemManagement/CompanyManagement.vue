<template>
  <div id="CompanyManagement" ref="CompanyManagement">

    <div style="margin-left: 3%;margin-top: 3%" id="TableHeader">
      <el-button @click="clearFilter" size="small">清除所有过滤器</el-button>
      <el-button @click="handleCreate" size="small">添加公司</el-button>
      <el-button @click="handleDelete()" size="small" :type="this.multipleSelection.length > 0 ? 'danger':''">删除公司
      </el-button>

      <!--      充当白板-->
      <div style="margin: auto;"></div>

      <div style="margin-right: 10%" id="FilterBar">
        <!--        <div class="filter-bar-item">-->
        <!--          <span style="margin-right: 5px;">角色名</span>-->
        <!--          <el-input v-model="filter_id" type="number" placeholder="查询id" size="small"></el-input>-->
        <!--        </div>-->
        <div class="filter-bar-item">
          <span style="margin-right: 5px;width: 40px;">搜索</span>

          <!-- 22.7.7 修改了model👇-->

          <el-input v-model="filter_company_name" placeholder="请输入公司名称" size="small"></el-input>
        </div>
        <!--        <div class="filter-bar-item">-->
        <!--          <span style="margin-right: 5px;">角色_id</span>-->
        <!--          <el-input v-model="filter_user_name" placeholder="查询账号" size="small"> ></el-input>-->
        <!--        </div>-->
        <el-button size="small" @click="getData">搜索</el-button>

      </div>


    </div>
    <div style="margin-left: 3%">
    <el-table
        v-loading="loading"
        class="elementTable"
        stripe
        fit
        :height="710"
        @sort-change="sortChange"
        @filter-change="filterChange"
        @selection-change="selectionChange"
        ref="elementTable"
        :default-sort="{prop: 'id', order: 'ascending'}"
        :data="tableData">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          width="60"
          type="index">
      </el-table-column>


<!--      22.7.7修改了column-key和prop字段👇-->
      <el-table-column
          min-width="45"
          column-key="company_name"
          prop="company_name"
          sortable="custom"
          label="公司名">

<!--        22.7.7修改了span里的内容↓-->
        <template slot-scope="scope">
          <span>{{ scope.row.company_name }}</span>
        </template>
      </el-table-column>

      <!--      22.7.7修改了column-key和prop字段👇-->
      <el-table-column
          prop="company_id"
          min-width="45"
          column-key="company_id"
          sortable="custom"
          label="公司id">
        <!--        22.7.7修改了span里的内容↓-->

        <template slot-scope="scope">
          <span>{{ scope.row.company_id }}</span>
        </template>
      </el-table-column>

<!--      22.7.8添加了mail字段-->
      <el-table-column
          prop="mail"
          min-width="45"
          column-key="mail"
          sortable="custom"
          label="注册邮箱">
        <template slot-scope="scope">
          <span>{{ scope.row.mail }}</span>
        </template>
      </el-table-column>


      <!--      22.7.7修改了column-key和prop字段👇 但实际上company没有描述字段,所以展示的是company_name-->

      <el-table-column
          prop="company_describe"
          min-width="100"
          column-key="company_describe"
          label="公司状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '正常'" type="info">{{ scope.row.status }}</el-tag>
          <el-tag v-if="scope.row.status === '正常运行'" type="success">{{ scope.row.status }}</el-tag>
          <el-tag v-if="scope.row.status === '审核不通过'" type="danger">{{ scope.row.status }}</el-tag>
          <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
<!--          <span>{{ scope.row.status }}</span>-->
        </template>
      </el-table-column>

<!--      <el-table-column-->
<!--          prop="permission"-->
<!--          column-key="permission"-->
<!--          sortable="custom"-->
<!--          label="权限组">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.permission }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column
          fixed="right"
          label="操作"
          width="180">

        <template slot-scope="scope">

          <el-button
              v-if="scope.row.status != '审核不通过'"
              @click.native.prevent="handleEdit(scope.$index, scope.row)"
              type="text"
              size="small">
            编辑
          </el-button>
          <el-button v-if="scope.row.status == '待审核'"
              @click.native.prevent="handleCommit(scope.$index, scope.row)"
              type="text"
              size="small">
            同意
          </el-button>
          <el-button v-if="scope.row.status == '待审核'"
                     @click.native.prevent="handleRefuse(scope.$index, scope.row)"
                     type="text"
                     size="small">
            拒绝
          </el-button>
        </template>
      </el-table-column>

    </el-table>
    </div>
    <el-pagination
        id="TablePagination"
        background
        layout="total,prev, pager, next,jumper,sizes"
        :total="totalNum"
        :page-size="pageSize"
        :page-sizes="[15,30,50,100,99999]"
        @current-change="pageChange"
        @size-change="sizeChange"
        :current-page.sync="pageNum"
    >
    </el-pagination>
    <CompanyEditPanel
        :current-obj="currentObj"
        :permission-list="permissionList"
        :dialog-visible="dialogVisible"
        :is-id-editable="isIdEditable"
        :mode="mode"
        @close="closeDialog"
    ></CompanyEditPanel>

  </div>
</template>

<script>
import {my_request, time_formatter} from "@/views/systemManagement/utils";
import CompanyEditPanel from "@/components/systemManagement/CompanyEditPanel";

export default {
  name: "CompanyManagement",

  components: {CompanyEditPanel},
  data() {
    return {
      tableData: [],
      permissionList: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,
      mode: 'update',


      //editpanel
      currentObj: {},
      dialogVisible: false,
      isIdEditable: true,

      //selection
      multipleSelection: [],


      //filter
      filter_company_name: "",

      //sort
      sort: "company_name",
      sortMethod: "asc",
    };
  },
  created() {
    this.getData();
  },

  methods: {
    getData() {
      this.loading = true;
      let params = {
        // 同改
        "filter_company_name": this.filter_company_name,
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;
      return my_request(this, {
        url: 'data/companyManagement/companyList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);
        // 这里的permissionList应该是给角色管理内部的权限调整，后续估计应该要删
        let {total, companyList} = res.data;
        this.tableData = companyList;
        this.totalNum = total;
      }).finally(() => {
        this.loading = false;
      });
    },
    formatter(time) {
      return time_formatter(time);
    },


    // 同改
    //filter
    clearFilter() {
      this.filter_company_name = "";
      this.sort = "company_name";
      this.sortMethod = 'asc'
      this.$refs.elementTable.clearFilter();
      this.getData();
    },
    // filterMethod(value, row, column) {
    //   const property = column['property'];
    //   return row[property] === value;
    // },

    //sort/filter change
    sortChange(args) {
      let {column, prop, order} = args;
      this.sort = prop;
      this.sortMethod = order === "descending" ? 'desc' : 'asc';
      console.log(column, prop, order);
      this.getData();

    },
    filterChange(args) {

      if (args.role_name) {
        this.filter_company_name = args.company_name
      }
      this.getData();
    },
    pageChange(num) {
      this.pageNum = num;
      this.getData();

    },
    sizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1;
      this.getData();
    },
    selectionChange(selectList) {
      this.multipleSelection = selectList;
    },


    // button
    handleDelete() {
      let user = this.multipleSelection;
      if (user.length === 0) {
        this.$message.info("未选择任何公司");
        return;
      }
      let count = user.length;
      this.$confirm(`确定删除选择的${count}个公司？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // request
        this.loading = true;
        my_request(this, {
          url: 'data/companyManagement/companyDelete',
          method: 'post',
          data: {
            companyList: this.multipleSelection,
          },
        }).then(res => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.multipleSelection = []
          this.getData();


        }).finally(() => {
          this.loading = false;
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //editpanel
    handleEdit(index, row) {
      this.currentObj = row;
      this.isIdEditable = false;
      this.dialogVisible = true;
      this.mode = 'update';
    },
    closeDialog() {
      this.currentObj = {};
      this.dialogVisible = false;
      this.getData();
    },
    handleCreate() {
      this.currentObj = {};
      this.isIdEditable = true;
      this.mode = 'insert';
      this.dialogVisible = true;
    },
    handleCommit(index, row) {
      this.currentObj = row;
      this.mode = 'commit';
      let data = this.currentObj;
      data.request = this.mode;
          my_request(this, {
            url: '/data/companyManagement/companyUpdate',
            method: 'post',
            data: data,
          }).then(res => {
              this.$message.success({
                dangerouslyUseHTMLString: true,
                message: "申请批准成功! 新注册公司的超级管理员同步生成，默认id为：公司id+10000，密码为100000",
                duration: 5000
              });
          }).finally(() => {
            this.$emit('close');
          });
      this.getData();

    },
    handleRefuse(index, row) {
      this.currentObj = row;
      this.mode = 'refuse';
      let data = this.currentObj;
      data.request = this.mode;
      my_request(this, {
        url: '/data/companyManagement/companyUpdate',
        method: 'post',
        data: data,
      }).then(res => {
        this.$message.success({
          dangerouslyUseHTMLString: true,
          message: "操作成功！请及时通知申请人核实相关信息！",
          duration: 5000
        });
      }).finally(() => {
        this.$emit('close');
      });
      this.getData();

    },

  },
  activated() {
    this.$nextTick(() => {
      this.$refs.elementTable.doLayout(); //解决表格错位asd
    });
    this.getData();
  }
}
</script>

<style scoped>
#RoleManagement {
  /*width: 800px;*/
  width: inherit;
  padding: 20px 120px;
}

/deep/ .elementTable td {
  padding: 3px 0;
}

/deep/ .elementTable th {
  padding: 3px 0;
}

#TableHeader {
  display: flex;
  margin-bottom: 20px; /*justify-content: space-between;*/
}

#TablePagination {
  margin-top: 20px;
}

#FilterBar {
  display: flex;
}

#FilterBar .filter-bar-item {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

#FilterBar .filter-bar-item span {
  width: 50px;
  padding-right: 5px;
  text-align: right;
  font-size: 0.8em;

}

/* element样式重置 start */
/* 去掉input尾部上下小箭头 start */
/deep/ input::-webkit-outer-spin-button,
/deep/ input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}

/deep/ input[type='number'] {
  -moz-appearance: textfield !important;
}

/* 去掉input尾部上下小箭头 end */

</style>
