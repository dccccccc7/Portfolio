<template>
  <div id="modelEditForm">
    <el-dialog
        title="修改模板"
        :visible.sync="dialogFormVisible"
        @close="clear">
    <el-form :model="modelEditForm" ref="model-edit" label-position="right" label-width="80px" style="margin-left:12%">
<!--      //可以修改的模版分类模板号:-->
<!--      <el-form-item label="模版分类" prop="classification"-->
<!--                    :rules="{required: true, message: '请选择分类', trigger: 'blur'}">-->
<!--        <el-select v-model="modelEditForm.classification" filterable placeholder="请选择"-->
<!--                   prop="classification" style="width: 100%">-->
<!--          <el-option-->
<!--              v-for="item in options"-->
<!--              :key="item.value"-->
<!--              :label="item.label"-->
<!--              :value="item.value">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="模版号" prop="templateId"-->
<!--                    :rules="{required: true, message: '请输入模板号', trigger: 'change'}">-->
<!--        <el-input v-model="modelEditForm.templateId" style="width: 100%;"></el-input>-->
<!--      </el-form-item>-->

<!--      //不可修改的模板号模板分类：-->

      <el-form-item label="模板号"  >
        <el-col :span="15">
        <div style="float:left;margin-left:10%"> {{modelEditForm.templateId}}</div>
        </el-col>
      </el-form-item>
      <el-form-item label="模板分类" >
        <el-col :span="15">
        <div style="float:left;margin-left:10%">{{modelEditForm.classification}}</div>
        </el-col>
      </el-form-item>
<!--      **********************-->
      <el-form-item label="模版名称" prop="templateName"
                    :rules="{required: true, message: '请输入名称', trigger: 'blur'}">
        <el-col :span="15">
        <el-input v-model="modelEditForm.templateName" style="margin-left:10%;width:90%;"></el-input>
        </el-col>
      </el-form-item>

      <el-form-item v-for="(item, index) in modelEditForm.attributes"
                    :label="'属性'+(index+1)"
                    :key="item.id"
                    :prop="'attributes.' + index + '.content'"
                    :rules="{required: true, message: '请输入属性', trigger: 'blur'}">
        <el-col :span="15">
          <el-input v-model="modelEditForm.attributes[index].content"
                    style="width: 66%;margin-left:10%;"
                    placeholder="请输入属性"
                    filterable>
          </el-input>

          <el-button size="mini"
                     style="color: red; margin-left: 5%;width:19%"
                     @click="del(index)">删除</el-button>
        </el-col>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="onCancel()">取 消</el-button>
      <el-button @click="add">添 加</el-button>
      <el-button type="primary" @click="onSubmit()">确 定</el-button>
    </div>
    </el-dialog>
  </div>
</template>

<script>
import Draggable from "vuedraggable"
import {request} from "@/network/request";

export default {
  name: "modelForm",
  data() {
    return {
      num: 1,
      dialogVisible: false,
      dialogFormVisible:false,
      modelEditForm: {
        classification: '',//模版分类
        templateId: '',//模版号
        templateName: '',//模版名称
        attributes: [],
      },

      options: [
        {
          value: '物料模版',
          label: '物料模版'
        },
        {
          value: '设备模版',
          label: '设备模版'
        },
        // {
        //   value: 'product',
        //   label: '产品模版'
        // },
      ]
    }
  },

  methods: {
    add() {
      this.num++
      if(this.attributes === undefined) {
        this.attributes = []
      }

      this.modelEditForm.attributes.push({id: this.num, content: ''})
    },
    del(index) {
      this.modelEditForm.attributes.splice(index, 1)
    },

    onSubmit() {
      this.validateForm()
    },

    onCancel(){
      this.dialogFormVisible = false;
      this.resetDataForm();
    },

    clear() {
      this.resetDataForm()
    },

    resetDataForm() {
      this.$refs['model-edit'].clearValidate();
      this.$refs['model-edit'].resetFields();
    },

    validateForm() {
      let attributedata = []
      for(let i = 0; i < this.modelEditForm.attributes.length; i++)
      {
        attributedata.push(this.modelEditForm.attributes[i].content)
      }
      console.log(attributedata)
      let myurl=""
      if (this.modelEditForm.classification==="物料模版"){
        myurl="template/material/updateMaterialTemplate"
      }
      if (this.modelEditForm.classification==="设备模版"){
        myurl="template/equipment/updateEquipmentTemplate"
      }
      // if (this.modelEditForm.classification==="product"){
      //   myurl="/template/product/updateProductTemplate"
      // }
      console.log(myurl)
      this.$refs['model-edit'].validate((valid) => {
        if (valid) {
          this.$confirm("是否提交？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            request({
              url: myurl,
              method: 'post',
              data: {
                id:Number(this.modelEditForm.templateId),//模板号
                name:this.modelEditForm.templateName,//模板名
                attribute:attributedata,//属性
                company_id:this.$store.getters.userinfo.company_id,//公司
              }
            }).then(res => {
              if (res && res.status === 200) {
                this.dialogFormVisible = false
                this.$message({
                  type: 'success',
                  message: '添加成功'
                })
                this.$emit('onSubmit')
              }
            })
          })
        } else {
          this.$message({
            type: 'error',
            message: '网络出错',
          })
          return false
        }
      });
    },
  },

  components: {
    Draggable
  },

}
</script>

<style scoped>
#modelEditForm {
  margin-top: 50px;
  margin-left: 5%;
  margin-right: 40%;
}
</style>