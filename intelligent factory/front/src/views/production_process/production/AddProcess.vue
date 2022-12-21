<template>
  <div>
    <el-form :model="process" ref="processForm" label-position="right" label-width="80px">
      <el-form-item label="工艺描述" prop="comments"
                    :rules="{required: true, message: '请填写工艺描述', trigger: 'blur'}">
        <el-input type="textarea" placeholder="请输入工艺描述" v-model="process.comments" maxlength="45" show-word-limit></el-input>
      </el-form-item>
      <draggable v-model="process.data" chosenClass="chosen" forceFallback="true" animation="1000">
        <transition-group>
          <el-form-item v-for="(item, index) in process.data"
                        :label="'步骤'+(index+1)"
                        :key="item.id"
                        :prop="'data.' + index + '.content'"
                        :rules="{required: true, message: '请选择步骤', trigger: 'change'}">
            <el-select v-model="process.data[index].content"
                       placeholder="请选择工序"
                       filterable>
              <el-option v-for="item in options"
                         :label="item"
                         :value="item"></el-option>
            </el-select>
            <el-button size="mini"
                       style="color: red; margin-left: 20px;"
                       @click="del(index)">删除</el-button>
          </el-form-item>
        </transition-group>
      </draggable>
      <el-form-item>
        <el-button @click="add">添加</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import Draggable from "vuedraggable"
import {request} from "@/network/request";

export default {
  name: "AddProcess",
  data() {
    return {
      num: 1,
      process: {
        data: [
          {id: 1, content: ''}
        ],
        comments: ''
      },
      options: []
    }
  },
  methods: {
    add() {
      this.num++
      this.process.data.push({id: this.num, content: ''})
    },
    del(index) {
      this.process.data.splice(index, 1)
    },
    submitForm() {
      let result = false
      this.$refs['processForm'].validate((valid) => {
        if (this.process.data.length < 1) {
          this.$message({
            type: 'error',
            message: "步骤不能为空"
          })
          return false
        } else if (valid) {
          this.$emit('message', this.process)
          result = true
        } else {
          return false;
        }
      });
      return result
    },
    getProcess() {
      request({
        url: '/getProceduresByCompany',
        method: 'get',
        params:{
          company_id:this.$store.getters.userinfo.company_id,
        }
      }).then(res => {
        this.options = res.data
      }).catch(err => {
        console.log(err)
      });
    }
  },
  components: {
    Draggable
  },
  created() {
    this.getProcess()
  }
}
</script>

<style scoped>
.chosen {
  border: solid 2px #3089dc !important;
}
.movable-item {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}
</style>