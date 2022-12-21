<template>
  <div id="RoleManagement" ref="RoleManagement">

    <div id="TableHeader">
      <el-button @click="clearFilter" size="small">清除所有筛选</el-button>
      <el-button @click="handleCreate" size="small">+新建角色</el-button>
      <el-button @click="handleDelete()" size="small" :type="this.multipleSelection.length > 0 ? 'danger':''">删除角色
      </el-button>

      <!--      充当白板-->
      <div style="margin: auto;"></div>

      <div id="FilterBar">
        <!--        <div class="filter-bar-item">-->
        <!--          <span style="margin-right: 5px;">角色名</span>-->
        <!--          <el-input v-model="filter_id" type="number" placeholder="查询id" size="small"></el-input>-->
        <!--        </div>-->
        <div class="filter-bar-item">
          <span style="margin-right: 5px;">角色名</span>
          <el-input v-model="filter_role_name" placeholder="查询角色名" size="small"></el-input>
        </div>
        <!--        <div class="filter-bar-item">-->
        <!--          <span style="margin-right: 5px;">角色_id</span>-->
        <!--          <el-input v-model="filter_user_name" placeholder="查询账号" size="small"> ></el-input>-->
        <!--        </div>-->
        <el-button size="small" @click="getData">查询</el-button>

      </div>


    </div>
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


      <el-table-column
          min-width="45"
          column-key="role_name"
          prop="role_name"
          sortable="custom"
          label="角色名">
        <template slot-scope="scope">
          <span>{{ scope.row.role_name }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="role_id"
          min-width="45"
          column-key="role_id"
          sortable="custom"
          label="角色id">
        <template slot-scope="scope">
          <span>{{ scope.row.role_id }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="role_describe"
          min-width="100"
          column-key="role_describe"
          label="描述">
        <template slot-scope="scope">
          <span>{{ scope.row.role_describe }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="permission"
          column-key="permission"
          sortable="custom"
          label="权限组">
        <template slot-scope="scope">
          <span>{{ scope.row.permission }}</span>
        </template>
      </el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="120">

        <template slot-scope="scope">

          <el-button
              @click.native.prevent="handleEdit(scope.$index, scope.row)"
              type="text"
              size="small">
            修改
          </el-button>
        </template>
      </el-table-column>

    </el-table>
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
    <RoleEditPanel
        :current-obj="currentObj"
        :permission-list="permissionList"
        :dialog-visible="dialogVisible"
        :is-id-editable="isIdEditable"
        :mode="mode"
        @close="closeDialog"
    ></RoleEditPanel>

  </div>
</template>

<script>
import {my_request, time_formatter} from "@/views/systemManagement/utils";
import RoleEditPanel from "@/components/systemManagement/RoleEditPanel";

export default {
  name: "RoleManagement",
  components: {RoleEditPanel},
  data() {
    return {
      tableData: [],
      permissionList: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,
      mode:'update',


      //editpanel
      currentObj: {},
      dialogVisible: false,
      isIdEditable: true,

      //selection
      multipleSelection: [],

      //filter
      filter_role_name: "",

      //sort
      sort: "role_name",
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
        "filter_role_name": this.filter_role_name,
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;
      return my_request(this, {
        url: 'data/roleManagement/roleList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);

        let {total, roleList, permissionList} = res.data;
        this.tableData = roleList;
        this.permissionList = [];
        for (let i = 0; i < permissionList.length; i++) {
          this.permissionList.push({text: permissionList[i], value: permissionList[i]});
        }
        this.totalNum = total;
      }).finally(() => {
        this.loading = false;
      });
    },
    formatter(time) {
      return time_formatter(time);
    },

    //filter
    clearFilter() {
      this.filter_role_name = "";
      this.sort = "role_name";
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
        this.filter_role_name = args.role_name
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
        this.$message.info("未选择任何角色");
        return;
      }
      let count = user.length;
      this.$confirm(`确定删除选择的${count}个角色？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // request
        this.loading = true;
        my_request(this, {
          url: 'data/roleManagement/roleDelete',
          method: 'post',
          data: {
            roleList:this.multipleSelection,
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
  margin-bottom:20px;  /*justify-content: space-between;*/
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
