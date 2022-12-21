<template>
   <el-form id="apply" label-width="100px" style="margin-right: 10%;margin-left: 10%;">
    <div style="margin: 10px 0px 10px 0px;">当前需求单号:{{ no }}</div>

    <div style="display: flex; flex-direction: row;">
      <el-form-item label="公司款号">
        <el-select v-model="selectCompany" @change="getStyle">
          <el-option v-for="item in productCompany" :key="item" :value="item"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="产品信息">
        <el-select v-model="selectStyle">
          <el-option v-for="item in productStyle" :key="item" :value="item"></el-option>
        </el-select>
      </el-form-item>

     
    </div>

    <div style="display: flex; flex-direction: row;">
      <el-form-item label="需求数量">
        <el-input type="number" min="1" v-model="quantity" />
      </el-form-item>

      <el-form-item label="交付时间" style="margin-left: 12px;">
        <el-date-picker type="date" :min="limit" v-model="dateStr" value-format="yyyy-MM-dd"/>
      </el-form-item>

      <router-link to>
        <el-button @click="permit" type="primary" style="margin-left: 150px;">提交</el-button>
      </router-link>
    </div>
  </el-form>
</template>

<script>
import getProduct from "../../../network/Plan/product.js";
import { user } from "../../../network/Plan/variable.js";
import request from "../../../network/Plan/CommondForm";

export default {
  name: "Apply",
  data() {
    return {
      productCompany: [],
      selectCompany: "",
      productStyle: [],
      selectStyle: "",
      
      quantity: 0,
      dueDate: {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        day: new Date().getDate(),
      },
      no: this.$route.params.formNo,
    };
  },
  watch:{
    '$route.params'(newval, oldval) {
      this.no = newval.formNo
    }
  },
  computed: {
    limit() {
      return (
        new Date().getFullYear() +
        "-" +
        (new Date().getMonth() + 1).toString().padStart(2, "0") +
        "-" +
        new Date().getDate().toString().padStart(2, "0")
      );
    },
    dateStr: {
      get() {
        return (
          this.dueDate.year +
          "-" +
          this.dueDate.month.toString().padStart(2, "0") +
          "-" +
          this.dueDate.day.toString().padStart(2, "0")
        );
      },
      set(val) {
        let re = val.split("-");
        this.dueDate.year = re[0];
        this.dueDate.month = Number(re[1]);
        this.dueDate.day = Number(re[2]);
      },
    },
  },
  created() {
    getProduct({
      url: "/getCompanyIDs",
    })
      .then((res) => {
        this.productCompany = res.data;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    getStyle() {
      getProduct({
        url: "/getProductByCompanyID",
        params: {
          company: this.selectCompany,
        },
      })
        .then((res) => {
          this.productStyle = res.data;
          
        })
        .catch((err) => {
          console.log(err);
        });
    },
    permit() {
      let result = this.selectStyle.split(",")
      let info = {
        brand:result[0],
        //season:result[1],
        style:result[2],
        color:result[3]
      }
          
      let form =
        "\n公司款号:"+
        this.selectCompany+
        "\n品牌:" +
        info.brand +
        // "\n季节:"+
        // info.season+
        "\n样式：" +
        info.style +
        "\n颜色" +
        info.color +
        "\n" +
        this.quantity +
        "\n" +
        this.dateStr;

      if (
        this.selectCompany == "" ||
        this.selectStyle == "" ||
        this.quantity < 1
      ) {
        alert("数据未填完整");
        return;
      }

      if (!confirm("你提交的内容是：\n" + form)) {
        return;
      }

      request({
        url: "/demandForm/saveDemandForm",
        params: {
          "product.company":this.selectCompany,
          "product.brand": info.brand,
          //"product.season":info.season,
          "product.style": info.style,
          "product.color": info.color,
          quantity: this.quantity,
          expectedDate: this.dateStr,
          waiting: true,
          status: "1",
          deleted: "0",
          createdBy: user,
          no: this.no,
          company_id: this.$store.getters.userinfo.company_id
          
        },
      })
        .then((res) => {
          console.log("wwww");
          console.log(res);
          this.$router.push("/commondform")
          this.$router.back(-1)
          this.$router.go(0)
        })
        .catch((err) => {
          console.log(err);
        });

      // this.$router.go(-1)
      // location.replace("http://localhost:8080/#/commondform")
      // this.$router.push("/commondform")
      // this.$router.back(-1)
      // this.$router.go(0)
      // this.$parent.step();
    },
  },
};
</script>

<style>
</style>
