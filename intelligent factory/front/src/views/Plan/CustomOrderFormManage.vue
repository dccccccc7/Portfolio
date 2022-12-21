<template>
    <div id="ProductPlanManagement" ref="CustomOrderFormManage">
        <!--
        <meta  http-equiv="Cache-Control" content="no-cache,no-store,must-revlidate">
        <meta  http-equiv="Expires" content="O">
        <meta http-equiv="Pragma" content="no-cache">-->
        <div class="table_container">
            <el-form :model="searchForm"  ref="searchForm">
                <el-row :gutter="24" style="margin-top: 10px">
                    <!--<el-col :span="12">
                        <div >
                            <el-date-picker
                                v-model="searchForm.created_time_start"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                align="right"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                >
                            </el-date-picker>
                        </div>
                    </el-col>-->
                    <el-col :span="6">
                        <div >
                            <el-date-picker
                                v-model="searchForm.created_time_start"
                                type="datetime"
                                placeholder="选择开始时间"
                                align="right"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                >
                            </el-date-picker>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 20px">
                            <el-date-picker
                                v-model="searchForm.created_time_end"
                                type="datetime"
                                placeholder="选择终止时间"
                                align="right"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                >
                            </el-date-picker>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 140%">
                            <el-button type="primary" round  @click="find">按创建时间查询</el-button>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="24" style="margin-top: 10px">
                    <el-col :span="6">
                        <div>
                            <el-date-picker
                            v-model="searchForm.expected_time_start"
                            type="date"
                            placeholder="选择开始日期"
                            align="right"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            >
                        </el-date-picker>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 20px">
                            <el-date-picker
                                v-model="searchForm.expected_time_end"
                                type="date"
                                placeholder="选择终止日期"
                                align="right"
                                format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd"
                                >
                            </el-date-picker>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 130%">
                            <el-button type="primary" round  @click="find">按预期交付时间查询</el-button>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="24" style="margin-top: 10px">
                    <el-col :span="6">
                        <div style="margin-right: 10%" >
                            <el-autocomplete
                                class="inline-input"
                                v-model="searchForm.no"
                                :fetch-suggestions="querySearch"
                                placeholder="按需求单号查询"
                                :trigger-on-focus="true"
                            ></el-autocomplete>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 50%">
                            <el-button type="primary" round  @click="find">查询</el-button>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div  style="margin-left: 10%">
                            <el-autocomplete
                                class="inline-input"
                                v-model="searchForm.company"
                                :fetch-suggestions="querySearch2"
                                placeholder="按公司款号查询"
                                :trigger-on-focus="true"
                            ></el-autocomplete>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 30%">
                            <el-button type="primary" round  @click="find">查询</el-button>
                        </div>
                    </el-col>
                </el-row>


                <el-row style="margin-top: 30px;margin-left: 30%">
                    <el-col :span="6">
                        <div >
                            <el-button type="primary" round  @click="handleCreate">申请客户订单</el-button>
                            <!--<router-view></router-view>-->
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="margin-left: 45%">
                            <el-button type="primary" round  @click="initData">查询所有单据</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-form>

            <el-table
                stripe
                highlight-current-row
                style="quantityth: 100% ;margin-top: 20px"
                :key="tablekey"
                :data="tableData"
                :default-sort = "{prop: 'no', order: 'descending'}">
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item label="产品品牌">
                                <span>{{ props.row.brand }}</span>
                            </el-form-item>
                            <el-form-item label="产品颜色">
                                <span>{{ props.row.color }}</span>
                            </el-form-item>
                            <!-- <el-form-item label="产品季节">
                                <span>{{ props.row.season }}</span> -->
                            </el-form-item>
                            <el-form-item label="公司款号">
                                <span>{{ props.row.company }}</span>
                            </el-form-item>
                            <el-form-item label="产品编号">
                                <span>{{ props.row.product_id }}</span>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column
                    sortable
                    property="no"
                    label="订单号"
                >
                </el-table-column>
                <el-table-column
                  property="type"
                  label="产品名称"
                >
                </el-table-column>
                <el-table-column
                  property="quantity"
                  label="预期数量"
                  >
                </el-table-column>
                <el-table-column
                  property="expected_date"
                  label="交付时间"
                  >
                </el-table-column>
                <el-table-column
                    property="created_time"
                    label="创建时间"
                >
                </el-table-column>
                <el-table-column
                    label="操作"
                >
                    <template slot-scope="props">
                      <el-col :span="12"><div >
                          <el-button plain round  @click="handleEdit(props.$index, props.row)">修改</el-button>
                        </div>
                      </el-col>
                      <el-col :span="6">
                      </el-col>
                      <el-col :span="12"><div >
                        <el-button plain round  @click="distribute(props.row)">分配</el-button>
                      </div>
                      </el-col>
                    </template>


                </el-table-column>
            </el-table>
            <div class="Pagination" style="text-align: left;margin-top: 10px;">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-size="limit"
                    layout="total, prev, pager, next, jumper"
                    :total="count">
                </el-pagination>
            </div>
        </div>
        <CustomOrderFormEditPanel :currentObj_prop="currentObj_prop"
                                  :mode="mode"
                                  :companyList="autocompleteDataList2"
                                  :productInfoList="productInfo"
                                  :dialog-visible="dialogVisible"
                                  :isCompanyIdEditable="isCompanyIdEditable"
                                  @close="closeDialog"
                                  ></CustomOrderFormEditPanel>
    </div>
</template>

<script>
    import request from "../../network/Plan/CommondForm.js";//请求数据api
    import CustomOrderFormEditPanel from "@/components/Plan/CustomOrderFormEditPanel";//弹出框
    //import {getCustomOrderForm} from "../../network/Plan/getData"//自己写的请求数据api，有跨域问题，弃用
    export default {
        data(){
            return {
                tableData: [//表格的数据
                ],searchForm:
                    {//存放搜索框的数据
                        no: '',
                        company: '',
                        created_time_start: '',
                        created_time_end: '',
                        expected_time_start: '',
                        expected_time_end: '',
                    },
                productInfo:[],//当前行的产品信息（用于修改功能的自动填充）

                /*存储自动填充的数据列表*/
                autocompleteDataList1:[],//订单号
                autocompleteDataList2:[],//公司款号

                currentRow: null,//当前选中的行
                offset: 0,//当前页第一个数据下标
                limit: 20,//每页显示的数据数目
                count: 0,//一共多少条数据
                currentPage: 1,//当前页数
                tablekey:0,//用于刷新数据，可能无用

                //控制新建/修改窗口的参数
                currentObj_prop: {},//当前行数据
                dialogVisible: false,//是否可见
                isCompanyIdEditable: true,//company是否可编辑标志
                mode:'update',//模式 更新：update |  新建：insert
            }
        },
    	  components: {
            CustomOrderFormEditPanel
    	  },
        created(){
            this.initData();
        },
        methods: {
          async find() {
              //多条件搜索
              this.offset=0;
              this.currentPage=1;
              try{
                  request({
                      url: "/customOrderForm/searchCustomOrderForm",
                      params: {
                        no: !this.searchForm.no ? null : this.searchForm.no.trim(),
                        company: !this.searchForm.company ? null : this.searchForm.company.trim(),
                        created_time_start: !this.searchForm.created_time_start ? null : this.searchForm.created_time_start.trim(),
                        created_time_end: !this.searchForm.created_time_end ? null : this.searchForm.created_time_end.trim(),
                        expected_time_start: !this.searchForm.expected_time_start ? null : this.searchForm.expected_time_start.trim(),
                        expected_time_end: !this.searchForm.expected_time_end ? null : this.searchForm.expected_time_end.trim(),
                        limit:this.limit,
                        offset:this.offset,
                        company_id:this.$store.getters.userinfo.company_id

                      },
                  })
                      .then((res) => {
                          console.log(res);
                          let CustomOrderForm = res.data;
                          this.tableData = CustomOrderForm;
                          this.count = CustomOrderForm.length;
                          this.$message({
                            type: 'success',
                            message: '获取数据成功'
                          });
                      })
                      .catch((err) => {
                          console.log(err);
                      });
              }catch(err){
                  this.$message({
                      type: 'error',
                      message: '获取数据失败'
                  });
                  console.log(err);
              }
          },
          async initData(){
            console.log("初始化数据")
            //查询全体数据
            try{
              //查询全部产品数据
              request({
                url: "/process/getProductsByCompany",
                params: {
                  pageSize:this.limit,
                  pageOffset:this.offset+1,
                  company_id:this.$store.getters.userinfo.company_id
                },
              }).then((res) => {
                console.log("全部产品信息");
                console.log(res);
                var count_p=res.data.count;
                var products=res.data.products;
                /*更新自动填充的数据*/
                this.autocompleteDataList2=[];
                var array2=[];//临时存放数据
                for(var i=0;i<count_p;i++){
                  array2.push(products[i].company)
                }
                array2=Array.from(new Set(array2)).sort()//去重&排序
                for(var i=0;i<array2.length;i++){//整合成[{'value':'data'}]形式
                  const autoCompleteData={};
                  autoCompleteData.value=array2[i]
                  this.autocompleteDataList2.push(autoCompleteData);
                }
                console.log(this.autocompleteDataList2)
              }).catch((err) => {
                console.log(err);
              });
              //获取全部需求单
              request({
              url: "/customOrderForm/allCustomOrderForm",
              params: {
                  limit:this.limit,
                  offset:this.offset,
                  company_id:this.$store.getters.userinfo.company_id
                  },
              }).then((res) => {
                  console.log(res);
                  let CustomOrderForm = res.data;
                  this.tableData = CustomOrderForm;
                  this.count = CustomOrderForm.length;
                  this.autocompleteDataList1=[];
                  /*更新自动填充的数据*/
                  var array1=[];//临时存放数据
                  for(var i=0;i<this.count;i++){
                      array1.push(this.tableData[i].no)
                  }
                  array1=Array.from(new Set(array1)).sort()//去重&排序

                  for(var i=0;i<array1.length;i++){//整合成[{'value':'data'}]形式
                      const autoCompleteData={};
                      autoCompleteData.value=array1[i]
                      this.autocompleteDataList1.push(autoCompleteData);
                  }

                  this.$message({
                      type: 'success',
                      message: '获取数据成功'
                  });
              }).catch((err) => {
                  console.log(err);
                  });
              }catch(err){
                  this.$message({
                      type: 'error',
                      message: '获取数据失败'
                      });
              }
          },
          handleSizeChange(val) {
              console.log(`每页 ${val} 条`);
          },
          handleCurrentChange(val) {
              this.currentPage = val;
              this.offset = (val - 1)*this.limit;
              this.initData()
          },
          async showAll() {
              this.initData();
          },
          async querySearch(queryString, cb) {
              var autoCompleteDataList = this.autocompleteDataList1;
              let results = queryString ? autoCompleteDataList.filter(this.createFilter(queryString)) : autoCompleteDataList;

              clearTimeout(this.timeout);
              this.timeout = setTimeout(() => {
                  cb(results);
              }, 1000 * Math.random());

          },
          async querySearch2(queryString, cb) {
              var autoCompleteDataList = this.autocompleteDataList2;
              let results = queryString ? autoCompleteDataList.filter(this.createFilter(queryString)) : autoCompleteDataList;

              clearTimeout(this.timeout);
              this.timeout = setTimeout(() => {
                  cb(results);
              }, 1000 * Math.random());

          },
          createFilter(queryString) {
              return (autoCompleteData) => {
                  return (autoCompleteData.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
              };
          },
          initSetTimeout(today) {
              setInterval(() => {
                  this.getdata(today);
              }, 10000);
          },
          transfer() {
            //this.$router.push('/AddInventorySetting')
          },
          filterHandler(value, row, column) {
            const property = column['property'];
            return row[property] === value;
          },
          //弹出框
          handleEdit(index, row) {
            this.mode = 'update';
            this.currentObj_prop = row;
            this.isCompanyIdEditable = false;
            this.dialogVisible = true;
          },
          closeDialog() {
            this.currentObj_prop = {};
            this.dialogVisible = false;
          },
          handleCreate() {
            this.mode = 'insert'
            this.currentObj_prop = {};
            this.isCompanyIdEditable = true;
            this.dialogVisible = true;
            let date = new Date();
            this.currentObj_prop.no =
                "17" +
              date.getFullYear() +
              (date.getMonth() + 1).toString().padStart(2, "0") +
              date.getDate().toString().padStart(2, "0") +
              Date.now().toString().slice(-8);
          },
          distribute(item) {
            //跳转到分配页面
            //重构数据结构
            var product={
              brand:item.brand,
              style:item.type,
              color:item.color,
              season:item.season,
              id:item.product_id
            }
            var dataTmp={
              id:item.id,
              no:item.no,
              product:product,
              quantity:item.quantity,
            }
            this.$router.push({
              path:'/distribution',
              query:dataTmp
            })
          },
        },
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
