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

        <el-form-item class="item" label="角色名" label-width="100px" prop="role_name">
          <el-input v-model="currentObj.role_name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="角色id" label-width="100px" prop="role_id">
          <el-input v-model="currentObj.role_id" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="描述" label-width="100px" prop="role_describe">
          <el-input  v-model="currentObj.role_describe" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="权限组" label-width="100px" prop="permission">
          <el-select v-model="currentObj.permission">
            <el-option
                v-for="item in permissionList.map((value) => {return value.value})"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
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
  name: "RoleEditPanel",
  data() {
    return {
      loading: false,
      rules: {
        role_name: [
          {required: true, message: '请输入角色名', trigger: 'blur'},
        ],
        role_id: [
          {required: true, message: '请输入角色id', trigger: 'blur'},
        ],
        permission: [
          { required: true, message: '请选择权限', trigger: 'blur'},
        ],
      }
    }
  },
  props: {
    dialogVisible: false,
    currentObj: {},
    isIdEditable: false,
    permissionList: Array,
    mode:String,
  },
  methods: {
    dialogConfirm() {
      let data = this.currentObj;
      data.request = this.mode;
      data.role_describe = data.role_describe?data.role_describe:"";
      this.$refs["elementForm"].validate((valid) => {
        if (valid) {
          my_request(this, {
            url: 'data/roleManagement/roleUpdate',
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
  },

}
</script>

<style scoped>

.item {
  width: 300px;
}
</style>
