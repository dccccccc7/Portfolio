<template>
  <el-form id="edit-base" label-width="60px">
    <el-card>
      <el-form-item label="产品id">
        <el-input v-model="procedure.procedure_id" :disabled="true"></el-input>
      </el-form-item>

      <el-form-item label="状态" :prop="procedure.status"
                    :rules="{required: true, message: '请填写工序状态', trigger: 'blur'}">
        <el-input placeholder="请输入工序状态" v-model="procedure.status" maxlength="5" show-word-limit></el-input>
      </el-form-item>

      <el-form-item label="成本" :rules="costRules">
        <el-input-number v-model="procedure.cost" placeholder="请输入成本"
                         :precision="2" :controls="false"></el-input-number>
      </el-form-item>

      <el-form-item label="描述">
        <el-input type="textarea" placeholder="请输入工序描述" v-model="procedure.comments" maxlength="45" show-word-limit></el-input>
      </el-form-item>
    </el-card>

    <el-form-item>
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" @click="commit">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "edit",
  props: ['procedure_id', 'detailDrawer', 'changeCommit'],
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
    // 页面初始化获取数据
    getData() {
      request({
        ///process/add
        url: '/process/getProcedureInfoByID',
        method: 'get',
        params: {
          procedure_id: this.procedure_id,
          company_id: this.$store.getters.userinfo.company_id
        }
      }).then(res => {
        this.procedure = res.data
      }).catch(err => {
        console.log(err)
      });
    },

    // 提交修改
    commit() {
      this.$confirm("是否提交本次修改？", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: '/process/updateProcedureByID',
          method: 'post',
          data: {
            procedure_id: this.procedure.procedure_id,
            status: this.procedure.status,
            comments: this.procedure.comments,
            cost: this.procedure.cost,
            operator_id: this.$store.getters.userinfo.id,
            company_id: this.$store.getters.userinfo.company_id
          }
        }).then(res => {
          console.log(res.data)
          if (res.data != "更新失败") {
            this.changeCommit()
            this.detailDrawer.closeDrawer()
            this.$message({
              type: 'success',
              message: '修改成功'
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
    },

    // 取消修改
    cancel() {
      this.detailDrawer.closeDrawer()
    }
  },
  created() {
    this.getData()
  }
}
</script>

<style scoped>
#edit-base {
  overflow-y: auto;
  position: absolute;
  width: 100%;
  height: 85%;
}
#edit-product {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#edit-process {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#edit-process .el-card {
  margin-bottom: 5px;
}
#edit-material {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#edit-material .el-card {
  margin-bottom: 5px;
}
</style>