<template>
  <div>
    <el-form :model="material" ref="materialForm" label-position="right">
      <div v-for="(item, index) in material.data" style="display: flex; flex-direction: row;">
        <el-form-item :label="'物料'+(index+1)"
                      :prop="'data.' + index + '.size'"
                      :rules="{required: true, message: '请选择物料', trigger: 'blur'}" label-width="80px">
          <el-cascader placeholder="请选择物料"
                       :options="options"
                       @change="addMaterial(index, $event)"
                       clearable>
<!--            :props="optionProps"-->
          </el-cascader>
        </el-form-item>
        <el-form-item :prop="'data.' + index + '.count'"
                      :rules="numRules">
          <el-input-number style="width: 25%; margin-left: 10px;" v-model="item.count" :controls="false" :precision="0"></el-input-number>
          <el-button size="mini"
                     style="color: red; margin-left: 20px;"
                     @click="del(index)">删除</el-button>
        </el-form-item>
      </div>

      <el-form-item>
        <el-button @click="add">添加</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {request} from "@/network/request";

export default {
  name: "AddMaterial",
  data() {
    let checkNum = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请填写数量'))
      } else {
        if (value <= 0) {
          callback(new Error('数量必须大于0'));
        } else {
          callback();
        }
      }
    };
    return {
      material: {
        data: [
          {name: '', size: '',color:''}
        ]
      },
      options: [],
      // optionProps:{
      //   value: 'material_id',
      //   label: 'name',
      //   children: 'attribute',
      //   checkStrictly: true
      // },
      numRules: [
        {validator: checkNum, trigger: 'blur'}
      ],
    }
  },
  methods: {
    add() {
      this.material.data.push({name: '', size: '',color:''})
    },
    del(index) {
      this.material.data.splice(index, 1)
    },
    addMaterial(index, event) {
      this.material.data[index].name = event[0]
      this.material.data[index].size = event[1]
/*      this.material.data[index].color = event[2]*/
    },
    submitForm() {
      let result = false
      this.$refs['materialForm'].validate((valid) => {
        if (this.material.data.length < 1) {
          this.$message({
            type: 'error',
            message: "物料不能为空"
          })
          return false
        }
        if (valid) {
          this.$emit('message', this.material.data)
          result = true
        } else {
          return false;
        }
      });
      return result
    },
    getMaterial() {
      request({
        url: '/getOptionMaterial',
        method: 'get',
      }).then(res => {
        this.options = res.data
      }).catch(err => {
        console.log(err)
      });
    }
  },
  created() {
    this.getMaterial()
  }
}
</script>

<style scoped>

</style>