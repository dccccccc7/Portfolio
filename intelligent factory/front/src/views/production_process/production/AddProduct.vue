<template>
  <div id="base">
    <el-form :model="product" ref="productForm" :rules="rules" label-position="right" label-width="80px">

      <el-form-item label="品牌" prop="brand">
        <el-input placeholder="请输入品牌" v-model="product.brand" maxlength="32" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="产品类别" prop="type">
        <el-input placeholder="请输入产品类别" v-model="product.type" maxlength="32" show-word-limit></el-input>
      </el-form-item>
<!--      <el-form-item filterable label="品牌" prop="brand">-->
<!--        <el-select v-model="product.brand" placeholder="请选择品牌" filterable>-->
<!--          <el-option v-for="item in brands" :label="item" :value="item"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="季节" prop="season">-->
<!--        <el-select v-model="product.season" placeholder="请选择季节" filterable>-->
<!--          <el-option v-for="item in seasons" :label="item" :value="item"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->

<!--      <el-form-item filterable label="产品类别" prop="color">-->
<!--        <el-select v-model="product.type" placeholder="请选择类别" filterable>-->
<!--          <el-option v-for="item in types" :label="item" :value="item"></el-option>-->
<!--        </el-select>-->
<!--      getProcedures-->
<!--      </el-form-item>-->

      <el-form-item label="客户款号" prop="customer_id">
        <el-input placeholder="请输入客户款号" v-model="product.customer_id" maxlength="32" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="公司款号" prop="company">
        <el-input placeholder="请输入公司款号" v-model="product.company" maxlength="32" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="颜色" prop="color">
        <el-input placeholder="请输入颜色" v-model="product.color" maxlength="32" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="描述" prop="comments">
        <el-input type="textarea" placeholder="请输入产品描述" v-model="product.comments" maxlength="45" show-word-limit></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="resetForm('productForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "AddProduct",
  data() {
    return {

      product: {
        product_id:"",
        brand: "",
        // season: "",
        type: "",
        customer_id: "",
        company: "",
        color: "",
        comments: "",
        company_id:this.$store.getters.userinfo.company_id,
      },
      rules: {
        brand: [
          {required: true, message: "请选择品牌方", trigger: 'blur'}
        ],
        // season: [
        //   {required: true, message: "请选择季节", trigger: 'blur'}
        // ],
        type: [
          {required: true, message: "请选择类别", trigger: 'blur'}
        ],
        customer_id: [
          {required: true, message: "请填写客户款号", trigger: 'blur'},
        ],
        company: [
          {required: true, message: "请填写公司款号", trigger: 'blur'},
        ],
        color: [
          {required: true, message: "请填写颜色", trigger: 'blur'},
        ]
      },
      brands: [],
      // seasons: [],
      types: []
    }
  },
  methods: {
    submitForm() {
      let result = false
      this.$refs['productForm'].validate((valid) => {
        if (valid) {
          this.$emit('message', this.product)
          result = true
        } else {
          return false;
        }
      });
      return result
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // getProductId(){
    //   request({
    //     url: '/process/getProductIdsByDate',
    //     method: 'get',
    //     params:{
    //       date:String.valueOf().date
    //     }
    //   }).then(res => {
    //     this.product_id = res.data
    //   }).catch(err => {
    //     console.log(err)
    //   });
    // },
    //getProductIdsByDate
    getBrands() {
      request({
        url: '/getBrands',
        method: 'get',
      }).then(res => {
        this.brands = res.data
      }).catch(err => {
        console.log(err)
      });
    },
    // getSeasons() {
    //   request({
    //     url: '/getSeasons',
    //     method: 'get',
    //   }).then(res => {
    //     this.seasons = res.data
    //   }).catch(err => {
    //     console.log(err)
    //   });
    // },
    getTypes() {
      request({
        url: '/getTypes',
        method: 'get',
      }).then(res => {
        this.types = res.data
      }).catch(err => {
        console.log(err)
      });
    },
  },
  created() {
    this.getBrands()
    // this.getSeasons()
    this.getTypes()
    // this.getProductId()
  }
}
</script>

<style scoped>

</style>