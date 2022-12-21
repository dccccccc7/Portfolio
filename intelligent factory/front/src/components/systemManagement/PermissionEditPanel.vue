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

        <el-form-item class="item" label="权限名" label-width="100px" prop="permission_name">
          <el-input v-model="currentObj.permission_name" autocomplete="off"></el-input>
        </el-form-item>


        <el-form-item label="拥有权限" label-width="100px" prop="checkboxGroup" >
          <div id="TopCheckBoxContainer">
            <el-checkbox  :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选
            </el-checkbox>
            <el-checkbox-group v-model="checkboxGroup" id="CheckBoxGroup" size="small" @change="handleCheckedCitiesChange">
              <el-checkbox class="box-item" v-for="access in accessList" :label="access" :key="access">{{ access }}
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>


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
  name: "PermissionEditPanel",
  data() {
    return {
      loading: false,
      checkAll:false,
      checkboxGroup: [],
      rules: {
        permission_name: [
          {required: true, message: '请输入权限名称', trigger: 'blur'},
        ],

      }
    }
  },
  props: {
    dialogVisible: false,
    currentObj: {},
    isIdEditable: false,
    mode: String,
    accessList: Array,
    currentList:Array,
  },
  methods: {
    dialogConfirm() {
      let data = this.currentObj;
      data.accessList = this.checkboxGroup;
      data.request = this.mode;
      this.$refs["elementForm"].validate((valid) => {
        if (valid) {
          if (this.checkboxGroup.length === 0) {
            this.$message.error("请至少选择一种权限！");
            return;
          }
          my_request(this, {
            url: 'data/permissionManagement/permissionUpdate',
            method: 'post',
            data: data,
          }).then(res => {
            this.$message.success("操作成功！");
          }).finally(() => {
            this.$emit('close');
          });
        } else {
          this.$message.error('错误提交');
          return false;
        }
      });

    },
    dialogCancel() {
      this.$emit('close');
    },
    handleCheckAllChange(val) {
      this.checkboxGroup = val ? this.accessList : [];
      // this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.accessList.length ;
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.accessList.length;
    }
  },
  watch:{
    currentList(value) {
      this.checkboxGroup = value;
    }
  },
  computed:{
    isIndeterminate() {
      console.log(1);
      return this.checkboxGroup.length > 0 && this.checkboxGroup.length < this.accessList.length;
    }

  }
}
</script>

<style scoped>

.item {
  width: 300px;
}
#TopCheckBoxContainer {
  /*border: 1px purple solid;*/
  display: flex;
}
#CheckBoxGroup {
  display: flex;
  height: 300px;
  width: fit-content;
  /*border: 1px solid red;*/
  flex-wrap: wrap;
  justify-content: flex-start;
  flex-direction: column;
  align-items: flex-start;
  padding-left: 10px;
}

.box-item {
  /*border: 1px solid rgba(0, 162, 255, 0.64);*/
  width: auto;
}
</style>
