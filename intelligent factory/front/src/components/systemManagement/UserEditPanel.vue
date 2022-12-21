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

        <el-form-item class="item" label="id" label-width="100px" prop="id">
          <el-input v-model.number="currentObj.id" autocomplete="off" disabled>0</el-input>
        </el-form-item>
        <el-form-item class="item" label="用户名" label-width="100px" prop="name">
          <el-input v-model="currentObj.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item class="item" label="性别" label-width="100px" prop="sex">
          <el-select v-model="currentObj.sex" placeholder="请选择">
            <el-option label="男" :value=1></el-option>
            <el-option label="女" :value=0></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="item" label="年龄" label-width="100px" prop="age">
          <el-input v-model.number="currentObj.age" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item class="item" label="账号" label-width="100px" prop="user_name">
          <el-input v-model="currentObj.user_name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item class="item" label="密码" label-width="100px" prop="pwd">
          <el-input v-model="currentObj.pwd" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item class="item" label="部门" label-width="100px" prop="department">
          <el-select v-model="currentObj.department">
            <el-option
                v-for="item in departmentList.map((value) => {return value.value})"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item class="item" label="职位" label-width="100px" prop="role">
          <el-select v-model="currentObj.role">
            <el-option
                multiple
                v-for="item in role_options"
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
import {mapGetters} from 'vuex'

export default {
  name: "EditPanel",
  data() {
    return {
      loading: false,
      department_options: [],
      role_options: [],
      originDepartment: "",
      rules: {
        // id: [
        //   {required: true, message: '请输入id', trigger: 'blur'},
        //   {type: "number", message: 'id必须为数字', trigger: 'blur'},
        // ],
        name: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {max: 4, message: '用户名过长', trigger: 'blur'},
        ],
        user_name: [
          {required: true, message: '请输入账号', trigger: 'blur'},
        ],
        pwd: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
        department: [
          {required: true, message: '请选择部门', trigger: 'blur'},
        ],
        role: [
          {required: true, message: '请选择职位', trigger: 'blur'},
        ],
        age: [
          {required: true, message: '请填写年龄', trigger: 'blur'},
          {type: "number", min: 0, max: 120, message: '年龄必须是合法整数', trigger: 'blur'},
        ],
        sex: [
          {required: true, message: '请选择性别', trigger: 'blur'},
        ],
      }
    }
  },
  props: {
    dialogVisible: false,
    currentObj: {},
    isIdEditable: false,
    departmentList: Array,
    mode: String,
  },
  computed: {
    ...mapGetters([
      'userinfo',
    ])
  },
  methods: {
    loadRoleOptions() {
      my_request(this, {
        url: '/data/roleManagement/getRolesByDepartment',
        method: 'get',
        params: {department_name: this.currentObj.department,company_id: this.userinfo.company_id}
      }).then(res => {
        this.role_options = res.data.roles
      })
    },
    dialogConfirm() {
      let data = this.currentObj;
      data.request = this.mode;
      data.isChangeDepartment = this.currentObj.department === this.originDepartment ? 0 : 1;

      this.$refs["elementForm"].validate((valid) => {
        if (valid) {
          my_request(this, {
            url: 'data/systemManagement/userUpdate',
            method: 'post',
            data: data,
          }).then(res => {
            this.$message.success("操作成功！");
          }).catch(reason => {
            this.$message.info("已提出部门变更申请！");
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
  watch: {
    currentObj: {
      handler(value) {
        if (value.department) {
          this.$message.info(value.department);
          this.originDepartment = value.department;
        }
      }
    },
    'currentObj.department': {
      handler(value) {
        if (value) {
          this.loadRoleOptions()
        }
      }
    }
  }

}
</script>

<style scoped>

.item {
  width: 300px;
}
</style>
