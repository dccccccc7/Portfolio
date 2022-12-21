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

        <el-form-item class="item" label="部门名" label-width="100px" prop="department_name">
          <el-input v-model="currentObj.department_name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="负责人id" label-width="100px" prop="manager_id">
          <el-input v-model.number="currentObj.manager_id" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="负责人姓名" label-width="100px" prop="manager_name">
          <el-input v-model="currentObj.manager_name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="角色" label-width="100px" prop="role_name">
          <el-select  multiple v-model="currentObj.role_name" style="width: 100%">
            <el-option
                v-for="item in options"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item class="item" label="描述" label-width="100px"prop="role_describe">
          <el-input style="width: 400px" v-model="currentObj.department_describe" autocomplete="off"></el-input>
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
import {mapGetters} from 'vuex'

export default {
  name: "DepartmentEditPanel",
  data() {
    return {
      loading: false,
      options: [],
      rules: {
        manager_name: [
          {required: true, message: '请输入负责人姓名', trigger: 'blur'},
        ],
        manager_id: [
          {required: true, message: '请输入负责人id', trigger: 'blur'},
          {type: "number", message: '负责人id必须是数字', trigger: 'blur'},
        ],
        department_name: [
          {required: true, message: '请输入部门名', trigger: 'blur'},
        ],
        role_name: [
          {required: true, message: '请输入角色', trigger: 'blur'},
        ],
      }
    }
  },
  computed: {
    ...mapGetters([
      'userinfo',
    ])
  },
  props: {
    dialogVisible: false,
    currentObj: {},
    isIdEditable: false,
    mode: String,
  },
  mounted() {
    this.loadOptions()
  },
  methods: {
    loadOptions() {

      my_request(this, {
        url: 'data/roleManagement/getRoles',
        method: 'get',
        params: {company_id: this.userinfo.company_id},
      }).then(res => {
        this.options = res.data.roles
      })
    },
    dialogConfirm() {

      let i;
      const type = this.currentObj.role_name;
      let role_name = "";
      for(i = 0; i< type.length; i++)
        role_name += type[i] + ",";

      let data = this.currentObj;
      data.request = this.mode;
      data.department_describe = data.department_describe ? data.department_describe : "";
      data.company_id = this.userinfo.company_id;
      data.role_name = role_name;

      this.$refs["elementForm"].validate((valid) => {
        if (valid) {
          my_request(this, {
            url: 'data/departmentManagement/departmentUpdate',
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
