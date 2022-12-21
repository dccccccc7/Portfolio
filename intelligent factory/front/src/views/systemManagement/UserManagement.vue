<template>
  <div id="DepartmentManagement" ref="UserManagement">

    <div id="TableHeader">
      <el-button @click="clearFilter" size="small">清除所有筛选</el-button>
      <el-button @click="handleCreate" size="small">+新建用户</el-button>
      <el-button @click="handleDelete()" size="small" :type="this.multipleSelection.length > 0 ? 'danger':''">删除用户
      </el-button>

<!--      充当白板-->
      <div style="margin: auto;"></div>

      <div id="FilterBar">
        <div class="filter-bar-item">
          <span style="margin-right: 5px;">id</span>
          <el-input v-model="filter_id" type="number" placeholder="查询id" size="small"></el-input>
        </div>
        <div class="filter-bar-item">
          <span style="margin-right: 5px;">用户名</span>
          <el-input v-model="filter_name" placeholder="查询用户名" size="small"></el-input>
        </div>
        <div class="filter-bar-item">
          <span style="margin-right: 5px;">账号</span>
          <el-input v-model="filter_user_name" placeholder="查询账号" size="small"> ></el-input>
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
          prop="id"
          width="100"
          sortable="custom"
          column-key="id"
          label="id">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column
          min-width="45"
          column-key="name"
          prop="name"
          label="用户名">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="sex"
          min-width="30"
          column-key="sex"
          sortable="custom"
          label="性别">
        <template slot-scope="scope">
          <span>{{ scope.row.sex === 0 ? "女" : "男" }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="age"
          column-key="age"
          sortable="custom"
          min-width="35"
          label="年龄">
        <template slot-scope="scope">
          <span>{{ scope.row.age }}</span>
        </template>
      </el-table-column>

      <el-table-column
          column-key="user_name"
          sortable="custom"
          prop="user_name"
          label="账号">
        <template slot-scope="scope">
          <span>{{ scope.row.user_name }}</span>
        </template>
      </el-table-column>


      <el-table-column
          prop="department"
          column-key="department"
          sortable="custom"
          :filters="this.departmentList"
          label="部门">
        <template slot-scope="scope">
          <span>{{ scope.row.department }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="role"
          column-key="role"
          sortable="custom"
          :filters="this.roleList"
          label="职位">
        <template slot-scope="scope">
          <span>{{ scope.row.role }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="created_time"
          column-key="created_time"
          sortable="custom"
          label="创建日期">
        <template slot-scope="scope">
          <span>{{ formatter(scope.row.created_time) }}</span>
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
    <UserEditPanel :current-obj="currentObj"
                   :mode="mode"
                   :role-list="roleList"
                   :department-list="departmentList"
                   :dialog-visible="dialogVisible"
                   :is-id-editable="isIdEditable"
                   @close="closeDialog"></UserEditPanel>

  </div>
</template>

<script>
import {my_request, time_formatter} from "@/views/systemManagement/utils";
import UserEditPanel from "@/components/systemManagement/UserEditPanel";

export default {
  name: "UserManagement",
  components: {UserEditPanel},
  data() {
    return {
      tableData: [],
      roleList: [],
      departmentList: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,


      //editpanel
      currentObj: {},
      dialogVisible: false,
      isIdEditable: true,
      mode:'update',

      //selection
      multipleSelection: [],

      //filter
      filter_id: "",
      filter_department: [],
      filter_role: [],
      filter_name: "",
      filter_user_name: "",

      //sort
      sort: "id",
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
        "filter_name": this.filter_name,
        "filter_id": this.filter_id ? Number(this.filter_id) : 0,
        "filter_department": this.filter_department,
        "filter_role": this.filter_role,
        "filter_user_name": this.filter_user_name,
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;

      return my_request(this, {
        url: 'data/systemManagement/userList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);

        let {total, userList, roleList, departmentList} = res.data;
        this.tableData = userList;
        this.roleList = [];
        this.departmentList = [];
        for (let i = 0; i < roleList.length; i++) {
          this.roleList.push({text: roleList[i], value: roleList[i]});
        }
        for (let i = 0; i < departmentList.length; i++) {
          this.departmentList.push({text: departmentList[i], value: departmentList[i]});
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
      this.filter_name = "";
      this.filter_id = "";
      this.filter_role = [];
      this.filter_department = [];
      this.filter_user_name = "";
      this.sort = "id";
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
      if (args.role) {
        this.filter_role = args.role
      }
      if (args.department) {
        this.filter_department = args.department
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
      this.getData();
    },


    // button
    handleDelete() {
      let user = this.multipleSelection;
      if (user.length === 0) {
        this.$message.info("未选择任何用户");
        return;
      }
      let count = user.length;
      this.$confirm(`确定删除选择的${count}个用户？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // request
        this.loading = true;
        let waitTime = 120 * this.multipleSelection.length;
        if (this.multipleSelection.length >= 25) {
          this.$message({
            type: 'info',
            message: `执行时间约为${waitTime / 1000}秒，请等待！`
          });
        }
        my_request(this, {
          url: 'data/systemManagement/userDelete',
          method: 'post',
          timeout:waitTime > 2500 ? waitTime : 2500,
          data: {
            userList:this.multipleSelection,
          },
        }).then(res => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.multipleSelection = [];
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
      this.mode = 'update'
      this.currentObj = row;
      this.isIdEditable = false;
      this.dialogVisible = true;
    },
    closeDialog() {
      this.currentObj = {};
      this.dialogVisible = false;
      this.getData();
    },
    handleCreate() {
      this.mode = 'insert'
      this.currentObj = {};
      this.isIdEditable = true;
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
