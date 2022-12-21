<template>
  <div id="DepartmentChangeManagement" ref="DepartmentChangeManagement">

    <div id="TableHeader">
      <el-button @click="clearFilter" size="small">清除所有筛选</el-button>
      <el-button @click="batchHandle()" size="small" :type="this.multipleSelection.length > 0 ? 'danger':''">批量审核
      </el-button>

      <!--      充当白板-->
      <div style="margin: auto;"></div>

      <div id="FilterBar">
        <!--        <div class="filter-bar-item">-->
        <!--          <span style="margin-right: 5px;">角色名</span>-->
        <!--          <el-input v-model="filter_id" type="number" placeholder="查询id" size="small"></el-input>-->
        <!--        </div>-->
        <div class="filter-bar-item">
          <span style="margin-right: 5px;">申请人</span>
          <el-input v-model.number="filter_transfer_id" placeholder="查询申请人id" size="small"></el-input>
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
        :height="670"
        @sort-change="sortChange"
        @filter-change="filterChange"
        @selection-change="selectionChange"
        ref="elementTable2"
        :default-sort="{prop: 'id', order: 'ascending'}"
        :data="tableData">
      <el-table-column
          type="selection"
          :selectable="checkSelectable"
          width="55">
      </el-table-column>
      <el-table-column
          width="60"
          type="index">
      </el-table-column>


      <el-table-column
          min-width="60"
          column-key="transfer_id"
          prop="transfer_id"
          sortable="custom"
          label="申请人">
        <template slot-scope="scope">
          <span>{{ scope.row.transfer_id }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="from_department"
          min-width="60"
          column-key="from_department"
          label="原部门">
        <template slot-scope="scope">
          <span>{{ scope.row.from_department }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="to_department"
          min-width="60"
          column-key="to_department"
          label="新部门">
        <template slot-scope="scope">
          <span>{{ scope.row.to_department }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="status"
          width="100"
          column-key="status"
          sortable="custom"
          label="状态">
        <template slot-scope="scope">
          <el-tag
              :type="tagColor(scope.row.status)"
              disable-transitions>{{ scope.row.status }}
          </el-tag>
        </template>

      </el-table-column>


      <el-table-column
          prop="created_by"
          column-key="created_by"
          sortable="custom"
          label="操作人">
        <template slot-scope="scope">
          <span>{{ scope.row.created_by }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="created_time"
          column-key="created_time"
          sortable="custom"
          label="操作时间">
        <template slot-scope="scope">
          <span>{{ formatter(scope.row.created_time) }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="handled_by"
          column-key="handled_by"
          sortable="custom"
          label="处理人">
        <template slot-scope="scope">
          <span>{{ scope.row.handled_by }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="handled_time"
          column-key="handled_time"
          sortable="custom"
          label="处理时间">
        <template slot-scope="scope">
          <span>{{ formatter(scope.row.handled_time) }}</span>
        </template>
      </el-table-column>


      <el-table-column
          fixed="right"
          label="操作"
          width="160">
        <template slot-scope="scope">
          <div style="display: flex">
            <el-button
                :disabled="scope.row.status !== '待审核'"
                @click.native.prevent="handleAgree(scope.$index, scope.row)"
                type="success"
                size="mini">
              同意
            </el-button>
            <el-button
                :disabled="scope.row.status !== '待审核'"
                @click.native.prevent="handleDecline(scope.$index, scope.row)"
                type="danger"
                size="mini">
              拒绝
            </el-button>
          </div>
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

  </div>
</template>

<script>
import {my_request, time_formatter} from "@/views/systemManagement/utils";


export default {
  name: "DepartmentChangeManagement",
  components: {},
  data() {
    return {
      tableData: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,
      mode: 'update',


      //selection
      multipleSelection: [],

      //filter
      filter_transfer_id: "",
      //sort
      sort: "created_time",
      sortMethod: "asc",
    };
  },
  created() {
    this.getData();
  },
  props: {
    tableDoLayout: Date,
  },

  methods: {
    getData() {
      this.$emit("getCount")
      this.loading = true;
      let params = {
        "filter_transfer_id": this.filter_transfer_id ? this.filter_transfer_id : 0,
        "filter_created_by": "",
        "status": [],
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;
      return my_request(this, {
        url: 'data/applyManagement/applyList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);

        let {total, applyList} = res.data;
        this.tableData = applyList;

        this.totalNum = total;
      }).finally(() => {
        this.loading = false;
      });
    },
    formatter(time) {
      return time_formatter(time);
    },
    checkSelectable(row, index) {
      return row.status === '待审核';
    },

    //filter
    clearFilter() {
      this.filter_transfer_id = 0;
      this.sort = "created_time";
      this.sortMethod = 'asc'
      this.$refs.elementTable2.clearFilter();
      this.getData();
    },


    //sort/filter change
    sortChange(args) {
      let {column, prop, order} = args;
      this.sort = prop;
      this.sortMethod = order === "descending" ? 'desc' : 'asc';
      console.log(column, prop, order);
      this.getData();

    },
    filterChange(args) {

      // if (args.role_name) {
      //   this.filter_role_name = args.role_name
      // }
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
    batchHandle() {
      let user = this.multipleSelection;
      if (user.length === 0) {
        this.$message.info("未选择任何申请");
        return;
      }
      let count = user.length;
      this.$confirm(`确定审核选择的${count}个申请？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let isAccept = 0;
        this.$confirm('是否同意申请？', '提示', {
          confirmButtonText: '同意',
          cancelButtonText: '拒绝',
          type: 'warning'
        }).then(() => isAccept = 1)
            .catch(() => isAccept = 0)
            .finally(() => {
              this.loading = true;
              let data = []
              for (let i = 0; i < user.length; i++) {
                data.push({
                  handled_id: 123456789,
                  apply_id: user[i].log_id,
                  isAccept: isAccept,
                  user: this.$store.getters.userinfo
                })
              }
              my_request(this, {
                url: 'data/applyManagement/applyAddress',
                method: 'post',
                data:data,
              }).catch(res => {
                this.$message({
                  type: 'success',
                  message: '操作成功!'
                });
                this.multipleSelection = []
                this.getData();
              })
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
    handleAgree(index, row) {
      this.currentObj = row;
      this.applyRequest(row.log_id, 1)
    },
    handleDecline(index, row) {
      this.currentObj = row;
      this.applyRequest(row.log_id, 0)

    },
    applyRequest(log_id, isAccept) {
      return my_request(this, {

        url: 'data/applyManagement/applySingleAddress',
        method: 'post',
        data: {
          handled_id: 123456789,
          apply_id: log_id,
          isAccept: isAccept,
        },
      }).then(res => {

      }).catch(reason => {
        this.$message.success("操作成功")
        this.getData();
      });

    }

  },
  computed: {
    tagColor() {
      return function (text) {
        if (text === '同意') {
          return 'success';
        } else if (text === '拒绝') {
          return 'danger';
        }
        return 'primary'
      };
    }
  },
  watch: {
    tableDoLayout(value) {
      this.getData();
      this.$refs.elementTable2.doLayout();
    }
  },
  activated() {
    this.getData();
    this.$refs.elementTable2.doLayout();
  }
}
</script>

<style scoped>
#DepartmentChangeManagement {
  /*width: 800px;*/
  width: inherit;
  padding: 20px 120px 20px 40px;
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
