<template>
  <div id="PermissionManagement" ref="PermissionManagement">

    <div id="TableHeader">
      <el-button @click="clearFilter" size="small">清除所有筛选</el-button>
      <el-button @click="handleCreate" size="small">+新建权限</el-button>
      <el-button @click="handleDelete()" size="small" :type="this.multipleSelection.length > 0 ? 'danger':''">删除权限
      </el-button>

      <!--      充当白板-->
      <div style="margin: auto;"></div>

      <div id="FilterBar">

        <div class="filter-bar-item">
          <span style="margin-right: 5px;">权限名</span>
          <el-input v-model="filter_permission_name" placeholder="查询权限名" size="small"></el-input>
        </div>

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
          width="300"
          column-key="permission_name"
          prop="permission_name"
          sortable="custom"
          label="权限名">
        <template slot-scope="scope">
          <span>{{ scope.row.permission_name }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="access_name"
          min-width="500"
          column-key="access_name"
          label="拥有权限">
        <template slot-scope="scope">
          <span>{{ accessParse(scope.row.accessList) }}</span>
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


    <PermissionEditPanel
        :current-obj="currentObj"
        :dialog-visible="dialogVisible"
        :is-id-editable="isIdEditable"
        :mode="mode"
        :current-list="currentList"
        :access-list="accessList"
        @close="closeDialog"
    >
    </PermissionEditPanel>
  </div>
</template>

<script>
import {my_request, time_formatter} from "@/views/systemManagement/utils";
import PermissionEditPanel from "@/components/systemManagement/PermissionEditPanel";


export default {
  name: "PermissionManagement",
  components: {PermissionEditPanel},
  data() {
    return {
      tableData: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,
      mode:'update',

      permissionList:[],
      accessList:[],


      //editpanel
      currentObj: {},
      currentList:[],
      dialogVisible: false,
      isIdEditable: true,

      //selection
      multipleSelection: [],

      //filter
      filter_permission_name: "",

      //sort
      sort: "permission_name",
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
        "filter_permission_name": this.filter_permission_name,
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;
      return my_request(this, {
        url: 'data/permissionManagement/permissionList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);
        let {total, permissionList,accessList} = res.data;
        this.permissionList = permissionList;
        this.tableData = permissionList;
        this.accessList = accessList;
        // for (let i = 0; i < permissionList.length; i++) {
        //   this.permissionList.push({text: permissionList[i], value: permissionList[i]});
        // }
        this.totalNum = total;
      }).finally(() => {
        this.loading = false;
      });
    },
    formatter(time) {
      return time_formatter(time);
    },
    accessParse(accessList) {

      return accessList.join("， ");
    },

    //filter
    clearFilter() {
      this.filter_permission_name = "";
      this.sort = "permission_name";
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
        this.filter_permission_name = args.role_name
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
        this.$message.info("未选择任何部门");
        return;
      }
      let count = user.length;
      this.$confirm(`确定删除选择的${count}个权限？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // request
        this.loading = true;
        my_request(this, {
          url: 'data/permissionManagement/permissionDelete',
          method: 'post',
          data: {
            "permissionList":user.map((x) => {return x.permission_name}),
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
      this.currentList = row.accessList;
      this.isIdEditable = false;
      this.dialogVisible = true;
      this.mode = 'update';
    },
    closeDialog() {
      this.currentObj = {};
      this.currentList = [];
      this.dialogVisible = false;
      this.getData();
    },
    handleCreate() {
      this.currentObj = {};
      this.currentList = [];
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
#PermissionManagement {
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
