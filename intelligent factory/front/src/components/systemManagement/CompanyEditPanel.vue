<template>
  <div id="editpanel">
    <el-dialog title="编辑信息"
               width="40%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :show-close="false"
               :visible.sync="dialogVisible">
      <el-form :model="currentObj"
               :rules="rules"
               ref="elementForm"
      >

        <el-form-item class="item" label="公司名称" label-width="80px" prop="company_name">
          <el-input v-model="currentObj.company_name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="注册邮箱" label-width="80px" prop="mail">
          <el-input v-model="currentObj.mail" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="公司id" label-width="80px" prop="company_id">
          <el-input v-model="currentObj.company_id" autocomplete="off" placeholder="系统将自动生成" disabled></el-input>
        </el-form-item>

<!--        <el-form-item class="item" label="描述" label-width="100px" prop="role_describe">-->
<!--          <el-input style="width: 500px" v-model="currentObj.role_describe" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item class="item" label="权限组" label-width="100px" prop="permission">-->
<!--          <el-select v-model="currentObj.permission">-->
<!--            <el-option-->
<!--                v-for="item in permissionList.map((value) => {return value.value})"-->
<!--                :key="item"-->
<!--                :label="item"-->
<!--                :value="item">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCancel">取 消</el-button>
        <el-button type="primary" @click="dialogConfirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import {my_request} from "@/views/systemManagement/utils";

export default {
  name: "CompanyEditPanel",
  data() {
    return {
      loading: false,
      rules: {
        company_name: [
          {required: true, message: '请输入公司名称', trigger: 'blur'},
        ],
        mail: [
          { required: true, message: '请输入注册邮箱', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change'],
          },
        ],
      }
    }
  },
  props: {
    dialogVisible: false,
    currentObj: {},
    isIdEditable: false,
    mode:String,
  },
  methods: {
    dialogConfirm() {
      let data = this.currentObj;
      data.request = this.mode;
      this.$refs["elementForm"].validate((valid) => {
        if (valid) {
          my_request(this, {
            url: '/data/companyManagement/companyUpdate',
            method: 'post',
            data: data,
          }).then(res => {
            if (res.data.modified) {
              this.$message.success({
                dangerouslyUseHTMLString: true,
                message: "申请批准成功! 新注册公司的超级管理员同步生成，默认id为：公司id+10000，密码为100000",
                duration: 5000
              });
            }else {
              this.$message.success("修改成功！");
            }
          }).finally(() => {
            this.$emit('close');
          });
        } else {
          this.$message.error('错误提交!');
          return false;
        }
      });

    },
    dialogCancel() {
      this.$emit('close');
    },
  },

}
</script>

<style scoped>

.item {
  width: 300px;
}
</style>
