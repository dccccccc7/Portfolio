<template>
  <div style="margin-top: 3%" id="DepartmentManagement" ref="UserManagement">

    <div id="TableHeader">

      <el-button @click="clearFilter" size="small">清除所有过滤器</el-button>
      <el-button @click="viewAllResult" type="success" size="small">所有质检结果</el-button>
      <!--      充当白板-->
      <div style="margin: auto;"></div>

      <div id="FilterBar">


<!--        <div class="filter-bar-item">-->
<!--          <el-form-item filterable label="车间" prop="filter_workshopID">-->
<!--            <el-select v-model="filter_workshopID" placeholder="查询车间" filterable>-->
<!--              <el-option v-for="item in workshop_id_terms" :label="item" :value="item"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </div>-->

        <div class="filter-bar-item">
          <span style="margin-right: 5px;">车间</span>
          <el-select v-model="filter_workshopID" @change="getLines(filter_workshopID)" placeholder="查询车间" filterable>
          <el-option v-for="item in workshop_id_terms" :label="item" :value="item"></el-option>
          </el-select>
        </div>


        <div class="filter-bar-item">

          <span style="margin-right: 5px;">产线</span>
          <el-select v-model="filter_lineID" placeholder="查询产线" filterable>
            <el-option v-for="item in corLines" :label="item" :value="item"></el-option>
          </el-select>

        </div>



        <div class="filter-bar-item">
          <span style="margin-right: 5px;">状态</span>
          <el-select v-model="filter_status"  placeholder="查询状态"
                     style="width: 100%">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>

        </div>

        <el-button size="small" type="primary" @click="getData">查询</el-button>

      </div>


    </div>
    <el-table
        style="margin-top: 3%"
        v-loading="loading"
        class="elementTable"
        stripe
        fit
        :height="710"
        @sort-change="sortChange"
        @selection-change="selectionChange"
        ref="elementTable"
        :default-sort="{prop: 'workshop_id', order: 'ascending'}"
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
          prop="workshopID"
          width="100"
          column-key="workshopID"
          label="车间">
        <template slot-scope="scope">
          <span>{{ scope.row.workshop_id }}</span>
        </template>
      </el-table-column>

      <el-table-column
          column-key="lineID"
          sortable="custom"
          prop="lineID"
          label="产线">
        <template slot-scope="scope">
          <span>{{ scope.row.line_id }}</span>
        </template>
      </el-table-column>


      <el-table-column
          prop="name"
          column-key="name"
          sortable="custom"
          label="产品名称">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="num"
          column-key="num"
          sortable="custom"
          label="产品数量">
        <template slot-scope="scope">
          <span>{{ scope.row.num }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="status"
          column-key="status"
          sortable="custom"
          label="产品状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">未质检</el-tag>
          <el-tag v-else type="success">已质检</el-tag>
        </template>
      </el-table-column>



      <el-table-column
          fixed="right"
          label="操作"
          width="120">

        <template slot-scope="scope">

          <el-button v-if="scope.row.status === '0'"
                     @click.native.prevent="handleEdit0(scope.$index, scope.row)"
                     type="text"
                     size="small">
            录入质检结果
          </el-button>
          <el-button v-else
                     @click.native.prevent="handleEdit1(scope.$index, scope.row)"
                     type="text"
                     size="small">
            查看质检结果
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

  </div>
</template>

<script>
// import {my_request, time_formatter} from "@/views/systemManagement/utils";
import {my_request} from "@/views/systemManagement/utils";
import {request} from "@/network/request";
//import {my_request} from "@/network/requests/workshop_request";
import UserEditPanel from "@/components/systemManagement/UserEditPanel";

export default {
  name: "qualityTaskListComponent",
  //components: {UserEditPanel},
  data() {
    return {
      workshop_id_terms: [],
      line_id_terms:[],

      //对应的产线
      corLines:[],

      tableData: [],
      //List: [],
      //departmentList: [],
      pageSize: 30,
      pageNum: 1,
      totalNum: 50,
      loading: false,
      options: [{
        value: '1',
        label: '已检'
      }, {
        value: '0',
        label: '未检'
      },
      ],


      //editpanel
      // currentObj: {},
      // dialogVisible: false,
      // isIdEditable: true,
      // mode:'update',

      //selection
      multipleSelection: [],

      //filter
      filter_workshopID: "",
      filter_lineID: "",
      filter_status: '',


      //sort
      sort: "workshop_id",
      sortMethod: "asc",
      company_id:'',
    };
  },
  created() {
    this.getData();
    this.getWorkshopIdTerms();
    this.getLineIdTerms();
    this.company_id = this.$store.getters.userinfo.company_id;
  },

  methods: {

    //查询车间对应产线(记得改URL)
    getLines(workshopId) {
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
        corWorkShopId: workshopId
      };
      request({
        url: '/qualityList/getAllLinesByWorkshop',
        method: 'get',
        params: req,
      }).then(res => {
        this.corLines = res.data
      }).catch(err => {
        console.log(err)
      });
    },



    getWorkshopIdTerms() {
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      request({
        url: '/qualityList/getAllWorkshop',
        method: 'get',
        params: req,
      }).then(res => {
        this.workshop_id_terms = res.data
      }).catch(err => {
        console.log(err)
      });
    },

    getLineIdTerms() {
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      request({
        url: '/qualityList/getAllLine',
        method: 'get',
        params: req,
      }).then(res => {
        this.line_id_terms = res.data
      }).catch(err => {
        console.log(err)
      });
    },





    getData() {
      this.loading = true;
      let params = {
        "filter_workshopID": this.filter_workshopID,
        "filter_lineID": this.filter_lineID,
        "filter_status": this.filter_status,
      };
      params.pageSize = this.pageSize;
      params.pageNum = this.pageNum;
      params.sort = this.sort;
      params.sortMethod = this.sortMethod;
      params.company_id =this.$store.getters.userinfo.company_id;


    //   my_request({
    //     url: '/qualitylist/qualityList',
    //     params: params,
    //     methods: 'get'
    //   }).then(res => {
    //
    //     if (res.data.code === 200) {
    //       this.$message({
    //         type: 'success',
    //         message: '查询成功'
    //       })
    //       console.log('查询结果', res)
    //
    //
    //       let s = []
    //       s.push(res.data.result)
    //       //s.push(res.data)
    //       //this.tableData = s;
    //       this.tableData = s;
    //       this.total = res.data.total;
    //       this.loading = false
    //     } else {
    //       this.$message.error(res.data.code)
    //       //this.$message.error('暂未查询到此记录')
    //     }
    //
    //   }).catch(err => {
    //     this.$message.error('服务器异常')
    //   });
    // },

      return my_request(this,{
        url: 'qualityList/queryQualityList',
        method: 'post',
        data: params,
      }).then(res => {
        console.log(res.data);
        let {total, qualityList} = res.data;
        this.tableData = qualityList;
        this.totalNum = total;
      }).finally(() => {
        this.loading = false;
      });
    },

    //filter
    clearFilter() {
      this.filter_workshopID = "";
      this.filter_lineID = "";
      this.filter_status = "";
      this.sort = "workshop_id";
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

    viewAllResult () {
      this.$router.push('/components/qualityManagement/quality/allQualityResults')
    },

    handleEdit0 (index,row) {
      this.$router.push({
        path:'/components/qualityManagement/quality/EditForm',
        query:{
          list_id:row.list_id,
          line_id:row.line_id,
          workshop_id:row.workshop_id,
          num:row.num
        }
      })
    },
    handleEdit1 (index,row) {
      this.$router.push({
        path:'/components/qualityManagement/quality/qualityResults',
        query:{
          list_id:row.list_id,
        }
      })

    },
    // filterChange(args) {
    //   if (args.role) {
    //     this.filter_role = args.role
    //   }
    //   if (args.department) {
    //     this.filter_department = args.department
    //   }
    //   this.getData();
    // },
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


    //editpanel
    // handleEdit(index, row) {
    //   this.mode = 'update'
    //   this.currentObj = row;
    //   this.isIdEditable = false;
    //   this.dialogVisible = true;
    // },
    // closeDialog() {
    //   this.currentObj = {};
    //   this.dialogVisible = false;
    //   this.getData();
    // },

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
