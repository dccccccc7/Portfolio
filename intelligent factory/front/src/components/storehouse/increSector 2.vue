<template>
  <div class="mainAdd">
    <el-form :model="storage" ref="storage" label-width="100px" class="demo-dynamic">
      <el-form-item
          prop="storage_id"
          label="库区    :  "
          :rules="[
                        { required: true, message: '请输入库区编号', trigger: 'blur' },
                        // {  message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                      ]">
        <el-input class="upupup" v-model.trim="storage.storage_id" placeholder="请输入库区编号"></el-input>
      </el-form-item>
      <el-form-item
          v-for="(domain, index) in storage.shelfs"
          :label="'货架' + index+'    :'"
          :prop="'shelfs.' + index + '.shelf_id'"
          :rules="{
                          required: true, message: '货架编号不可为空', trigger: 'blur'
                        }">
        <el-input v-model.trim="domain.shelf_id" placeholder="请输入货架编号"></el-input>
        <el-button @click.prevent="removeDomain(domain)">删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('storage')">创建</el-button>
        <el-button @click="addDomain">增加货架</el-button>
        <el-button @click="resetForm('storage')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";
import shelfManage from "@/components/storehouse/shelfManage";

export default {
  name: "increSector",
  data(){
    return{
      storage: {
        shelfs: [],
        user: '',
        storage_id: ''
      },

    }
  },
  methods:{
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let req = this.storage
          req.user = this.$store.getters.userinfo.id
          // req.user = 1
          console.log(req)
          my_request({
            url: 'storage/create',
            method: 'post',
            data: req
          }).then(res => {
            if (res.data.success===true){
              this.$message({
                type: 'success',
                message: '创建成功'
              })
            }else {
              this.$message.error('创建失败')
            }
            //还得给他跳转到主页去
          }).catch(err => {
            this.$message.error('服务器异常')

          })
          console.log(this.storage)
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    removeDomain(item) {
      var index = this.storage.shelfs.indexOf(item)
      if (index !== -1) {
        this.storage.shelfs.splice(index, 1)
      }
    },
    addDomain() {
      this.storage.shelfs.push({
        shelf_id: '',
      });
    },

  }
}
</script>

<style scoped>
.el-select .el-input {
  width: 230px;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.mainAdd .el-input .el-input__inner {
  width: 30%;
}

.mainAdd {
  width: 40%;
  height: auto;
  margin-top: 20px;
}

.mainAdd .el-input {
  width: 80%;
  margin-right: 20px;
}

.mainAdd .upupup {
  margin-right: 90px;
}

#theBaseMainBox {
  display: flex;
  /*background-color: gray;*/
  height: 600px;
  flex-direction: column;
  width: 1500px;
  margin: 80px;

}

#theBaseHeader {
  /*background-color: aliceblue;*/
  height: 15%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  /*align-items: center;*/
}

#theBaseMain {
  /*background-color: lightgreen;*/
  height: 80%;

  width: 100%;

}
</style>