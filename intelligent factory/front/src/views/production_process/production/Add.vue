<template>
  <el-container id="base">
    <!-- 步骤条 -->
    <el-header style="margin-top: 50px">
      <el-steps :active="active" finish-status="success" simple>
        <el-step title="添加产品" icon="el-icon-circle-plus"></el-step>
        <el-step title="分配工艺路线" icon="el-icon-menu"></el-step>
        <el-step title="填写物料清单" icon="el-icon-edit-outline"></el-step>
        <el-step title="提交" icon="el-icon-thumb"></el-step>
      </el-steps>
    </el-header>

    <el-main style="margin: 50px 70px 20px 20px;padding: 0px 20px 20px 20px;" :key="key">
      <transition mode="out-in" name="fade-transform">
        <keep-alive>
          <!-- 添加产品 -->
          <router-view ref="add" style="height: 400px"
                       :product="product" :process="process" :material="material"></router-view>
        </keep-alive>
      </transition>
    </el-main>

    <el-footer id="bottom">
      <el-button style="margin-top: 12px;" @click="next" :disabled="disabled">{{next_text}}</el-button>
      <el-button v-show="previous_show" style="margin-top: 12px;" @click="previous">上一步</el-button>
    </el-footer>
  </el-container>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "Add",
  data() {
    return {
      active: 0,
      next_text: "下一步",
      disabled: false,
      previous_show: false,
      product: {},
      process: [],
      material: [],
      path: ['product', 'process', 'material', 'ensure'],
      key: 0
    }
  },
  methods: {
    next() {
      // 在添加产品页面点击下一步
      // 需要先对输入进行验证，通过才可以进入下一步
      // 设置路径为process，并显示上一步按钮
      if (this.active == 0) {
        let child = this.$refs.add
        if (child.submitForm()) {
          this.active++
          this.addProduct()
          this.previous_show = true
          this.$router.push(this.path[this.active])
        }
      }
      // 在添加工艺路线页面点击下一步
      else if (this.active == 1) {
        let child = this.$refs.add
        if (child.submitForm()) {
          this.active++
          this.addProcess()
          this.$router.push(this.path[this.active])
        }
      }
      // 在物料清单页面点击下一步
      else if (this.active == 2) {
        let child = this.$refs.add
        if (child.submitForm()) {
          this.active++
          this.next_text = "完成"
          this.addMaterial()
          this.$router.push(this.path[this.active])
        }
      }
      else {
        this.addCommit()
      }
    },
    previous() {
      // 在添加产品页点击上一步
      // 隐藏上一步按钮
      if (this.active == 1) {
        this.active--
        this.previous_show = false
        this.$router.push(this.path[this.active])
      }
      // 在提交页面点击上一步
      // 修改完成按钮为下一步
      else if (this.active === 3) {
        this.active--
        this.next_text = '下一步'
        this.$router.push(this.path[this.active])
      }
      else {
        this.active--
        this.$router.push(this.path[this.active])
      }
    },
    addProduct() {
      let product = this.$refs.add.product
      this.product.brand = product.brand
      this.product.season = ''
      this.product.type = product.type
      this.product.customer_id = product.customer_id
      this.product.company = product.company
      this.product.color = product.color
      this.product.comments = product.comments
      this.product.company_id=this.$store.getters.userinfo.company_id
    },
    addProcess() {
      let process = this.$refs.add.process
      this.process = process
    },
    addMaterial() {
      let material = this.$refs.add.material
      this.material = material.data
    },
    init() {
      this.active = 0
      this.previous_show = false
      this.disabled = false
      this.next_text = "下一步"
      this.key++
      this.$router.push(this.path[0])
    },
    addCommit() {

      request({
        url: '/process/add3',
        method: 'post',
        data: {
          operator_id: this.$store.getters.userinfo.id,
          product: this.product,
          process: this.process,
          material: this.material
        }
      }).then(res => {
        // console.log(1)
        if (res.data === "添加成功") {
          this.$message({
            type: 'success',
            message: '添加成功'
          })
          this.disabled = true
          this.active++
          setTimeout(() => {
            this.init()
          }, 1000)
        } else {
          this.$message({
            type: 'error',
            message: res.data
          })
        }
      }).catch(err => {
        this.$message({
          type: 'error',
          message: '网络错误，添加失败'
        })
      });
    }
  },
  created() {
    this.$router.push(this.path[0])
  },
  activated() {
    this.init()
  }
}
</script>

<style scoped>
#base {
  text-align: left;
  padding: 0px 5% 0px 50px;
  margin-right: 25%;
}

#bottom {
  display: flex;
  justify-content: space-between;
  flex-direction: row-reverse;
}
</style>