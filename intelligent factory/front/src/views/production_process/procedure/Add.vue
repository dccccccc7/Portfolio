<template>
  <div id="procedure-add-base">
    <el-form :model="procedure" ref="procedureForm" label-position="right" label-width="80px">

      <el-form-item label="名称" prop="name"
                    :rules="{required: true, message: '请填写名称', trigger: 'blur'}">
        <el-input v-model="procedure.name" placeholder="请输入名称" maxlength="32" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="cost" prop="cost"
                    :rules="costRules">
        <el-input-number v-model="procedure.cost" placeholder="请输入成本"
                         :precision="2" :controls="false"
                         style="float: left"></el-input-number>
      </el-form-item>

      <el-form-item label="描述" prop="comments">
        <el-input type="textarea" placeholder="请输入产品描述" v-model="procedure.comments" maxlength="45" show-word-limit></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="resetForm('procedureForm')">重置</el-button>
        <el-button @click="commit" type="primary">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "Add",
  data() {
    let checkCost = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请填写成本'))
      } else {
        if (value <= 0) {
          callback(new Error('成本必须大于0'));
        } else {
          callback();
        }
      }
    };
    return {
      procedure: {},
      costRules: [
        {validator: checkCost, trigger: 'blur'}
      ]
    }
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    commit() {
      this.$refs['procedureForm'].validate((valid) => {
        if (valid) {
          this.$confirm("是否提交？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            request({
              url: '/process/addProcedure',
              method: 'post',
              data: {
                name: this.procedure.name,
                comments: this.procedure.comments,
                cost: this.procedure.cost,
                operator_id: this.$store.getters.userinfo.id,
                company_id: this.$store.getters.userinfo.company_id
              }
            }).then(res => {
              if (res.data != "更新失败") {
                this.resetForm("procedureForm")
                this.$message({
                  type: 'success',
                  message: '添加成功'
                })
              } else {
                this.$message({
                  type: 'error',
                  message: res.data
                })
              }
            }).catch(err => {
              this.$message({
                type: 'error',
                message: '网络错误，修改失败'
              })
            });
          })
        } else {
          return false;
        }
      });
    }
  }
}
</script>

<style scoped>
#procedure-add-base {
  margin-top: 50px;
  margin-left: 5%;
  margin-right: 40%;
}
</style>
