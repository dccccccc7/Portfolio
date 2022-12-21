
<!--
弹出的表单，用于修改模版
-->

<template>
  <div>

<!--    <el-button size="small" type="primary" icon="el-icon-circle-plus"  @click="dialogFormVisible = true">增加</el-button>-->

    <el-dialog
        title="修改模版"
        :visible.sync="dialogFormVisible"
        @close="clear">
<!--      111111111ref-->
      <el-form :model="modelform" :rules="rules" ref="modelForm" label-width="100px" size="large">
        <el-form-item label="模版名" prop="name">
          <el-input v-model="modelform.name" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="模版号" prop="material_id">
          <el-input v-model="modelform.material_id" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="模版属性" prop="attribute">
          <el-input v-model="modelform.attribute" style="width: 100%;"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onCancel()">取 消</el-button>
        <el-button type="primary" @click="addMaterialTemplate()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {request} from "@/network/request";

export default {
  name: 'modelForm',
  data() {
    return {
      dialogFormVisible: false,
      modelform: {
        name: '',//模版名称
        material_id: '',//模版号
        attribute: '',//模版名称
        company_id:'',
      },
      options: [{
        value: '物料模版',
        label: '物料模版'
      }, {
        value: '设备模版',
        label: '设备模版'
      }, {
        value: '产品模版',
        label: '产品模版'
      },
      ],
      value: '',
      formLabelWidth: '120px',
      rules: {

        name: [
          {required: true, message: '请选择模版分类', trigger: 'change' },
        ],
        material_id: [
          {required: true, message: '请输入模版代号', trigger: 'blur' },
        ],
        attribute: [
          {required: true, message: '请输入模版名称', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    clear() {
      this.resetDataForm()
    },

    addMaterialTemplate() {
      //

          request({
            url: '/template/material/addMaterialTemplate',
            method:'post',

            data:{
              modelform:{
                name: this.modelform.name,
                material_id: this.modelform.material_id,
                attribute: this.modelform.attribute,
                company_id: this.modelform.company_id,
              }

            },
          }).then(resp => {
            if (resp && resp.status === 200) {
              this.dialogFormVisible = false
              this.addMaterialTemplate(modelform)
            }

          })
      ;
    },
    resetDataForm() {
      this.$refs['dataForm'].clearValidate();
      this.$refs['dataForm'].resetFields();
    },
    onSubmit() {
      this.addMaterialTemplate(modelform);
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


