
<!--
表单：用于增改车间
-->

<template>
  <div>
<!--    <i class="el-icon-circle-plus-outline" @click="dialogFormVisible = true"></i>-->
<!--    <svg style="margin-right: 20%" t="1656742372582" class="icon" @click="dialogFormVisible = true" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3066" width="175" height="175"><path d="M187.264 888.746667l9.173333-22.186667c8.448 3.498667 17.749333 5.461333 27.562667 5.461333h36.010667V896H224c-13.013333 0-25.429333-2.56-36.736-7.253333z m576.725333 7.253333v-24.021333h36.010667c9.813333 0 19.114667-1.92 27.562667-5.418667l9.173333 22.186667c-11.306667 4.693333-23.722667 7.253333-36.736 7.253333h-36.010667zM896 260.010667h-24.021333V224c0-9.813333-1.92-19.114667-5.418667-27.562667l22.186667-9.173333c4.693333 11.306667 7.253333 23.722667 7.253333 36.736v36.010667zM260.010667 128H224c-13.013333 0-25.429333 2.56-36.736 7.253333l9.173333 22.186667a71.68 71.68 0 0 1 27.562667-5.418667h36.010667V128zM128 763.989333h24.021333v36.010667c0 9.813333 1.92 19.114667 5.418667 27.562667l-22.186667 9.173333A95.701333 95.701333 0 0 1 128 800v-36.010667z m0-71.978666h24.021333V619.946667H128v72.021333z m0-144h24.021333v-72.021334H128v72.021334z m0-144h24.021333V331.946667H128V404.053333z m0-144h24.021333V224c0-9.813333 1.92-19.114667 5.418667-27.562667l-22.186667-9.173333c-4.693333 11.306667-7.253333 23.722667-7.253333 36.736v36.010667zM331.989333 128v24.021333H404.053333V128H331.946667z m144 0v24.021333h72.021334V128h-72.021334z m144 0v24.021333h72.021334V128H619.946667z m144 0v24.021333h36.010667c9.813333 0 19.114667 1.92 27.562667 5.418667l9.173333-22.186667A95.701333 95.701333 0 0 0 800 128h-36.010667zM896 331.989333h-24.021333V404.053333H896V331.946667z m0 144h-24.021333v72.021334H896v-72.021334z m0 144h-24.021333v72.021334H896V619.946667z m0 144h-24.021333v36.010667c0 9.813333-1.92 19.114667-5.418667 27.562667l22.186667 9.173333c4.693333-11.306667 7.253333-23.722667 7.253333-36.736v-36.010667zM692.010667 896v-24.021333H619.946667V896h72.021333z m-144 0v-24.021333h-72.021334V896h72.021334z m-144 0v-24.021333H331.946667V896H404.053333z" fill="#515151" p-id="3067"></path><path d="M392.021333 512c0-13.226667 10.709333-24.021333 23.978667-24.021333h192a23.978667 23.978667 0 1 1 0 48.042666h-192A23.978667 23.978667 0 0 1 391.978667 512z" fill="#515151" p-id="3068"></path><path d="M512 392.021333c13.226667 0 24.021333 10.709333 24.021333 23.978667v192a23.978667 23.978667 0 1 1-48.042666 0v-192c0-13.226667 10.752-24.021333 24.021333-24.021333z" fill="#515151" p-id="3069"></path></svg>-->
    <el-dialog
        title="添加/修改车间"
        :visible.sync="dialogFormVisible"
        @close="clear">
      <el-form :model="workshopForm" :rules="rules" ref="dataForm" label-width="100px" size="large">
        <el-form-item label="车间编号" prop="id">
          <el-input v-model="workshopForm.id" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="车间名称" prop="name">
          <el-input v-model="workshopForm.name" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="工厂名称" prop="factory_name">
          <el-input v-model="workshopForm.factory_name" style="width: 100%;"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onCancel()">取 消</el-button>
        <el-button type="primary" @click="onSubmit()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {request} from "@/network/request";

export default {
  name: 'workshopForm',
  data() {
    return {
      dialogFormVisible: false,

      workshopForm: {
        id: '',//瑕疵分类
        name: '',//瑕疵代号
        factory_name: '',//瑕疵名称
      },

      value: '',
      formLabelWidth: '120px',
      rules: {
        id: [
          {required: true, message: '请输入车间编号', trigger: 'blur' },
        ],
        name: [
          {required: true, message: '请输入车间名称', trigger: 'blur' },
        ],
        factory_name: [
          {required: true, message: '请输入工厂名称', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    clear() {
      this.resetDataForm()
    },
    validateForm() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          request({
            url: 'workshop/update',
            method:'post',
            data:{
              id: this.workshopForm.id,
              name: this.workshopForm.name,
              factory_name: this.workshopForm.factory_name,
            }
          }).then(resp => {
            if (resp && resp.status === 200) {
              this.dialogFormVisible = false
              this.$emit('onSubmit')
            }
          })
        } else {
          this.$message.error('网络出错，请稍后再试');
          return false;
        }
      });
    },
    resetDataForm() {
      this.$refs['dataForm'].clearValidate();
      this.$refs['dataForm'].resetFields();
    },
    onSubmit() {
      this.validateForm();
    },
    onCancel(){
      this.dialogFormVisible = false;
      this.resetDataForm();
    },
  }
}
</script>

<style scoped>
.el-icon-circle-plus-outline {
  margin: 50px 0 0 20px;
  font-size: 100px;
  float: left;
  cursor: pointer;
}
</style>


