<template>
    <div id="editpanel">
        <el-dialog :title=this.title
                   width="70%"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false"
                   :visible.sync="dialogVisible"
                   @open="initData"
                   >
        <el-form :model="currentObj_prop"
                 :rules="rules"
                 ref="elementForm">

        <el-form-item class="item" label="需求单号" label-width=30% prop="no" >
          <el-input v-model.number="currentObj.no" autocomplete="off" :disabled=true></el-input>
        </el-form-item>
        <el-form-item class="item" label="公司款号" label-width=30% prop="company">
          <el-select v-model="currentObj.company"
                     :disabled="!isCompanyIdEditable"
                     @change ="find">
            <el-option
                v-for="item in companyList.map((value) => {return value.value})"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <el-row :gutter="24" style="margin-top: 10px"
                v-for="(domain, index) in dynamicValidateForm.domains"
                :key="domain.key"
                >
          <el-col :span="11">
            <el-form-item class="item" label="产品信息" label-width=30%
                          >
              <el-select v-model="domain.productInfo"
                         :disabled="currentObj.company==null"
                         @blur="productInfoChange"
                         >
                <el-option
                    v-for="item in productInfoList.map((value) => {return value.value})"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="11" >
            <el-form-item class="item" label="需求数量" label-width=30%
                          >
              <el-input-number :min="1" v-model="domain.quantity"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button
                @click.prevent="removeDomain(domain)"
                icon="el-icon-delete"
                size="mini"
                style="margin-top: 4px"
                ></el-button>
          </el-col>
        </el-row>


        <el-row :gutter="24" style="margin-top: 10px">
          <el-col :span="11">
            <el-form-item class="item" label="产品信息" label-width=30% prop="productInfo">
              <el-select v-model="currentObj.productInfo"
                         :disabled="currentObj.company==null"
                         :placeholder="this.mode=='insert'?'请选择':this.currentObj_old.productInfo"
                         @blur="productInfoChange">
                <el-option
                    v-for="item in productInfoList.map((value) => {return value.value})"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="11" >
            <el-form-item class="item" label="需求数量" label-width=30% prop="quantity">
              <el-input-number :min="1" v-model="currentObj.quantity"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button
                @click.prevent="addDomain"
                icon="el-icon-plus"
                size="mini"
                style="margin-top: 4px"
                :disabled="this.mode!='insert'"></el-button>
          </el-col>
        </el-row>




        <el-row :gutter="24" style="margin-top: 10px">
          <el-col :span="11">
            <el-form-item class="item" label="创建时间" label-width=30%>
              <el-date-picker
                 :disabled="true"
                  v-model="currentObj.created_time"
                  type="datetime"
                  placeholder="提交后自动生成"
                  align="right"
                  format="yyyy-MM-dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="11" >
            <el-form-item class="item" label="预期时间" label-width=35% prop="expected_date">
              <el-date-picker
                  v-model="currentObj.expected_date"
                  type="date"
                  placeholder="预期交付时间"
                  align="right"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>


        </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button v-if="this.mode=='update'" @click="deleteForm">删 除</el-button>
            <el-button @click="dialogCancel">取 消</el-button>
            <el-button type="primary" @click="permit">提 交</el-button>
          </div>
      </el-dialog>
    </div>
</template>

<script>
import {my_request} from "@/views/systemManagement/utils";
import request from "@/network/Plan/CommondForm";
import {user} from "@/network/Plan/variable";

export default {
  name: "EditPanel",
  data() {
    return {
      dynamicValidateForm: {
        domains: [],
      },//动态表单
      title:'新建需求单',
      productInfoMap:new Map(),//将产品信息映射成product_id
      tableData:[],
      productInfoList:[],//产品信息自动填充列表
      currentObj_old:{},//旧的表单的数据
      currentObj:{},//保存表单数据
      isCompanyIdEditable:true,//company是否可编辑
      rules: {
        no: [
          {required: true, message: '需求单号为必填', trigger: 'blur'},
        ],
        company: [
          {required: true, message: '公司款号为必填', trigger: 'blur'},
        ],
        productInfo: [
          {required: true, message: '产品信息为必填', trigger: 'blur'},
        ],
        quantity: [
          {required: true, message: '需求数量为必填', trigger: 'blur'},
          {type: "number", message: '需求数量为数字', trigger: 'blur'},
        ],
        expected_date: [
          {required: true, message: '预期日期为必填', trigger: 'blur'},
        ],
      }
    }
  },
  props: {
    dialogVisible: false,
    currentObj_prop: {},
    //isCompanyIdEditable_prop: true,
    companyList: Array,
    mode: String,
  },
  created(){
  },
  methods: {
    initData() {
      //this.isCompanyIdEditable=this.isCompanyIdEditable_prop;
      if (this.mode=='update'){
        this.isCompanyIdEditable=false;
        this.title="修改需求单";
        this.currentObj_old=JSON.parse(JSON.stringify(this.currentObj_prop));//复制对象数据
        this.currentObj=this.currentObj_prop;
        //this.currentObj=JSON.parse(JSON.stringify(this.currentObj_prop));//直接复制会出bug,只能绑定到一起了
        this.dynamicValidateForm={domains: [],};
        this.currentObj_old.productInfo=this.currentObj_prop.brand+'-'+this.currentObj_prop.season+'-'+
            this.currentObj_prop.type+'-'+this.currentObj_prop.color;
        this.find();
      }else if(this.mode=='insert'){
        this.isCompanyIdEditable=true;
        this.title="新建需求单"
        this.currentObj=this.currentObj_prop;
        this.currentObj.quantity=1;
        this.dynamicValidateForm={domains: [],}
      }

    },
    productInfoChange(){
      console.log("productInfoChange");
      this.isCompanyIdEditable=false;
    },

    async find() {
      //清空产品信息框
      //this.currentObj.productInfo='';
      for(index in this.dynamicValidateForm.domains) {
        var domain = this.dynamicValidateForm.domains[index];
        //domain.productInfo='';
      }
      //多条件搜索
      if (this.currentObj.company!=null)
        try{
          request({


            // url: "/customOrderForm/searchCustomOrderForm",
            // params: {
            //   no: null,
            //   company: !this.currentObj.company ? null : this.currentObj.company.trim(),
            //   created_time_start: null,
            //   created_time_end: null,
            //   expected_time_start: null,
            //   expected_time_end: null,
            //   limit:2142,
            //   offset:0
            // },

                url: "/process/getProductsByCompanyAndCompany",
                params: {
                  company: !this.currentObj.company ? null : this.currentObj.company.trim(),
                  pageSize: 2142,
                  pageOffset: 1,
                  company_id:this.$store.getters.userinfo.company_id
                },



          }
          
          )
            .then((res) => {
              //console.log(res);
              // let CustomOrderForm = res.data;
              let CustomOrderForm = res.data.products;
              this.tableData = CustomOrderForm;

              this.productInfoList=[];//清空产品信息列表
              this.productInfoMap=new Map();//清空映射

              /*更新自动填充的数据*/
              var array1=[];//临时存放数据，用于排序
              for(var i=0;i<this.tableData.length;i++){
                var row=this.tableData[i];
                // var product_info_tmp=row.brand+'-'+row.season+'-'+row.type+'-'+row.color;//构造产品信息
                var product_info_tmp=row.brand+'-'+row.type+'-'+row.color;//构造产品信息
                array1.push(product_info_tmp);
                this.productInfoMap.set(product_info_tmp,row.product_id);//创建映射
              }
              array1=Array.from(new Set(array1)).sort()//去重&排序
              for(var i=0;i<array1.length;i++){//整合成[{'value':'data'}]形式
                const autoCompleteData={};
                autoCompleteData.value=array1[i]
                this.productInfoList.push(autoCompleteData);
              }
              //console.log(this.productInfoList)
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
    dialogCancel() {
      this.$emit('close');
    },
    permit() {
      try{
        this.$refs["elementForm"].validate((valid) => {
          //检查输入有效性
          if (!valid) {
            this.$message.error('错误提交');
            return false;
          }

          let result = this.currentObj.productInfo.split("-")
          let info = {
            brand: result[0],
            //season: result[1],
            style: result[1],
            color: result[2]
          }
          let form =
              "\n公司款号：" +
              this.currentObj.company +
              "\n\n品牌：" +
              info.brand +
              // "\n季节：" +
              // info.season +
              "\n样式：" +
              info.style +
              "\n颜色：" +
              info.color +
              "\n数量："+
              this.currentObj.quantity ;
          if (
              this.currentObj.company == "" ||
              this.currentObj.productInfo == "" ||
              this.currentObj.quantity < 1
          ) {
            alert("数据未填完整");
            return;
          }
          if (this.mode=='insert'){
            console.log("提交了insert");
            for(index in this.dynamicValidateForm.domains){
              var domain=this.dynamicValidateForm.domains[index];
              if(domain.productInfo==null||domain.productInfo==''){
                alert("数据未填完整");
                return;
              }
            }
            for(var index in this.dynamicValidateForm.domains){
              var domain=this.dynamicValidateForm.domains[index];
              let result = domain.productInfo.split("-")
              let info = {
                brand: result[0],
                //season: result[1],
                style: result[1],
                color: result[2]
              }
              form+="\n\n品牌：" +
                  info.brand +
                  // "\n季节：" +
                  // info.season +
                  "\n样式：" +
                  info.style +
                  "\n颜色：" +
                  info.color +
                  "\n数量："+
                  domain.quantity ;
            }
            form+="\n预期时间：" + this.currentObj.expected_date
            if (!confirm("你提交的内容是：\n" + form)) {
              return;
            }

            request({
              url: "/demandForm/saveDemandForm",
              params: {
                "product.company": this.currentObj.company,
                "product.brand": info.brand,
                //"product.season": info.season,
                "product.style": info.style,
                "product.color": info.color,
                quantity: this.currentObj.quantity,
                expectedDate: this.currentObj.expected_date,
                waiting: true,
                status: "1",
                deleted: "0",
                createdBy: user,
                no: this.currentObj.no,
                company_id:this.$store.getters.userinfo.company_id
              },
            }).then((res) => {
              this.$message.success("请求成功");
              this.dialogVisible=false;
            }).catch((err) => {
              this.$message.error('请求发生错误');
              console.log(err);
            });
            for(var index in this.dynamicValidateForm.domains){
              var domain=this.dynamicValidateForm.domains[index];
              let result = domain.productInfo.split("-")
              let info = {
                brand: result[0],
                //season: result[1],
                style: result[1],
                color: result[2]
              }
              request({
                url: "/demandForm/saveDemandForm",
                params: {
                  "product.company": this.currentObj.company,
                  "product.brand": info.brand,
                  //"product.season": info.season,
                  "product.style": info.style,
                  "product.color": info.color,
                  quantity: domain.quantity,
                  expectedDate: this.currentObj.expected_date,
                  waiting: true,
                  status: "1",
                  deleted: "0",
                  createdBy: user,
                  no: this.currentObj.no,
                  company_id:this.$store.getters.userinfo.company_id
                },
              }).then((res) => {
                this.$message.success("请求成功");
                this.dialogVisible=false;
              }).catch((err) => {
                this.$message.error('请求发生错误');
                console.log(err);
              });
            }
          }//insert结尾
          else if(this.mode=='update'){
            console.log("提交了update");
            form+= "\n预期时间：" + this.currentObj.expected_date;
            let result_old = this.currentObj_old.productInfo.split("-")
            let info_old = {
              brand: result_old[0],
              //season: result_old[1],
              style: result_old[1],
              color: result_old[2]
            }
            let form_old =
                "\n公司款号：" +
                this.currentObj_old.company +
                "\n品牌：" +
                info_old.brand +
                // "\n季节：" +
                // info_old.season +
                "\n样式：" +
                info_old.style +
                "\n颜色：" +
                info_old.color +
                "\n数量：" +
                this.currentObj_old.quantity +
                "\n预期时间：" +
                this.currentObj_old.expected_date;
            console.log("确认");
            if (!confirm("你提交的内容是：" + form+ "\n\n原本的内容是："+ form_old)) {
              return;
            }
            request({
              url: "/customOrderForm/updateCustomOrderForm",
              params: {
                id:this.currentObj.id,
                expected_date: this.currentObj.expected_date,
                quantity:this.currentObj.quantity,
                product_id:this.currentObj.product_id
              },
            }).then((res) => {
              this.$message.success("请求成功");
              this.dialogVisible=false;
            }).catch((err) => {
              this.$message.error('请求发生错误');
              console.log(err);
            });
          }
        });//valid结尾

      }catch(err)
        {//try结束
          this.$message({
            type: 'error',
            message: '发生错误'
          });
          console.log(err);

      }
    //permit()结尾
    },
    removeDomain(item) {
      var index = this.dynamicValidateForm.domains.indexOf(item)
      if (index !== -1) {
        this.dynamicValidateForm.domains.splice(index, 1)
      }
    },
    addDomain() {
      //this.rules.push({productInfo+this.dynamicValidateForm.domains.length: })
      this.dynamicValidateForm.domains.push({
        productInfo: '',
        quantity:''
      });
    },
    deleteForm(){
      try{
        let result = this.currentObj_old.productInfo.split("-")
        let info = {
          brand: result[0],
         // season: result[1],
          style: result[2],
          color: result[3]
        }
        let form =
            "\n需求单号：" +
            this.currentObj.id +
            "\n公司款号：" +
            this.currentObj.company +
            "\n品牌：" +
            info.brand +
            // "\n季节：" +
            // info.season +
            "\n样式：" +
            info.style +
            "\n颜色：" +
            info.color +
            "\n数量："+
            this.currentObj.quantity+
            "\n预期时间：" +
            this.currentObj.expected_date+
            "\n创建时间：" +
            this.currentObj.created_time;
        if (!confirm("确认要删除以下条目吗：\n" + form)) {
          return;
        }
        request({
          url: "/customOrderForm/deleteCustomOrderForm",
          params: {
            id:this.currentObj.id
          },
        }).then((res) => {
          this.$message.success("请求成功");
          this.dialogVisible=false;
        }).catch((err) => {
          this.$message.error('请求发生错误');
          console.log(err);
        });
      }catch (e){
        console.log(e);
      }
    },
  },
  watch: {
  }

}
</script>

<style scoped>

.item {
  width: 400px;
}
</style>
