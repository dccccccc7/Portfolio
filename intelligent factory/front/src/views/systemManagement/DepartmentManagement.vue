<template>
  <div id="DepartmentManagement" ref="DepartmentManagement">
    <div id="TableHeader">
      <el-button @click="clearFilter" size="small">清除所有筛选</el-button>
      <el-button @click="handleCreate" size="small">+新建部门</el-button>
      <el-button @click="handleDelete()" size="small" :type="this.multipleSelection.length > 0 ? 'danger':''">删除部门
      </el-button>

      <!--      充当白板-->
      <div style="margin: auto;"></div>

      <div id="FilterBar">

        <div class="filter-bar-item">
          <span style="margin-right: 5px;">部门名</span>
          <el-input v-model="filter_department_name" placeholder="查询部门名" size="small"></el-input>
        </div>

        <el-button size="small" @click="getData">查询</el-button>

      </div>


    </div>
    <el-table
        v-loading="loading"
        class="elementTable"
        stripe
        fit
        :height="670"
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
          width="100"
          column-key="department_name"
          prop="department_name"
          sortable="custom"
          label="部门名">
        <template slot-scope="scope">
          <span>{{ scope.row.department_name }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="manager_id"
          width="130"
          sortable="custom"
          column-key="manager_id"
          label="负责人id">
        <template slot-scope="scope">
          <span>{{ scope.row.manager_id }}</span>
        </template>
      </el-table-column>

      <el-table-column
          width="130"
          column-key="manager_name"
          prop="manager_name"
          sortable="custom"
          label="负责人姓名">
        <template slot-scope="scope">
          <span>{{ scope.row.manager_name }}</span>
        </template>
      </el-table-column>

      <el-table-column
          width="130"
          column-key="departmentCount"
          prop="departmentCount"
          label="部门人数">
        <template slot-scope="scope">
          <span>{{ departmentCount[scope.row.department_name] }}</span>
        </template>
      </el-table-column>

      <el-table-column
          width="130"
          column-key="departmentRole"
          prop="departmentRole"
          label="部门角色">
        <template slot-scope="scope">
          <span>{{ roleParse(scope.row.roleList) }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="department_describe"
          min-width="100"
          column-key="department_describe"
          label="描述">
        <template slot-scope="scope">
          <span>{{ scope.row.department_describe }}</span>
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


    <DepartmentEditPanel
        :current-obj="currentObj"
        :dialog-visible="dialogVisible"
        :is-id-editable="isIdEditable"
        :mode="mode"
        :role-list="roleList"
        @close="closeDialog"
    >
    </DepartmentEditPanel>
  </div>
</template>

<script>
import {my_request, time_formatter} from "@/views/systemManagement/utils";

import DepartmentEditPanel from "@/components/systemManagement/DepartmentEditPanel";
import {request} from "@/network/request";
import DepartmentChangeManagement from "@/views/systemManagement/DepartmentChangeManagement";

export default {
  name: "DepartmentManagement",
  components: {DepartmentChangeManagement, DepartmentEditPanel},
  data() {
    return {
      tableData: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,
      mode: 'update',
      departmentCount: {},
      roleList:{},
      statusCount: 0,

      currentTime: new Date(),


      //editpanel
      currentObj: {},
      dialogVisible: false,
      isIdEditable: true,

      //selection
      multipleSelection: [],

      //filter
      filter_department_name: "",

      //sort
      sort: "department_name",
      sortMethod: "asc",
    };
  },
  created() {
    this.getData();
  },

  methods: {
    getCount() {
      request({
        url: 'data/applyManagement/toBeAddress',
        method: 'post',
      }).then(res => {
        this.statusCount = res.data;
      });
    },
    getData() {
      this.loading = true;
      let params = {
        "filter_role_name": this.filter_department_name,
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;
      this.getCount();
      return my_request(this, {
        url: 'data/departmentManagement/departmentList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);
        let {total, departmentList, departmentCount,roleList} = res.data;
        this.tableData = departmentList;
        this.roleList = roleList;
        let dict = {};
        for (let i = 0; i < departmentCount.length; i++) {
          dict[departmentCount[i].department] = departmentCount[i]['count'];
        }
        this.departmentCount = dict;

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
    roleParse(roleList) {

      return roleList.join("， ");
    },

    //filter
    clearFilter() {
      this.filter_department_name = "";
      this.sort = "department_name";
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
        this.filter_department_name = args.role_name
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
      for (let i = 0; i < user.length; i++) {
        let name = user[i].department_name;
        if (this.departmentCount[name] > 0) {
          this.$message.info("不能删除仍有人员的部门");
          return;
        }

      }
      let count = user.length;
      this.$confirm(`确定删除选择的${count}个部门？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // request
        this.loading = true;
        my_request(this, {
          url: 'data/departmentManagement/departmentDelete',
          method: 'post',
          data: {
            departmentList:this.multipleSelection,
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
    this.getData();
    this.currentTime = new Date();
    this.$nextTick(() => {
      this.$refs.elementTable.doLayout(); //解决表格错位asd
    });
  }
}
</script>

<style scoped>
#DepartmentManagement {
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
  margin-bottom: 20px;
  display: flex;
  /*justify-content: space-between;*/
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
/deep/ .el-tabs__item {
  height: 50px;
}

/deep/ .el-tabs__active-bar {
  height: 50px;
}
</style>
