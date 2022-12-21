<template>
  <el-form id="edit-base" label-width="70px" :model="edit" ref="editForm">
    <div id="edit-product">
      <h2>产品信息</h2>
      <el-card>
        <el-form-item label="状态" prop="product.status"
                      :rules="{required: true, message: '请填写产品状态', trigger: 'blur'}">
          <el-input placeholder="请输入产品状态" v-model="edit.product.status" maxlength="5" show-word-limit></el-input>
        </el-form-item>

        <el-form-item label="产品id">
          <el-input v-model="edit.product.product_id" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="品牌">
          <el-input v-model="edit.product.brand" :disabled="true"></el-input>
        </el-form-item>

<!--        <el-form-item label="季节">-->
<!--          <el-input v-model="edit.product.season" :disabled="true"></el-input>-->
<!--        </el-form-item>-->

        <el-form-item label="产品类别">
          <el-input v-model="edit.product.type" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="客户款号">
          <el-input v-model="edit.product.customer_id" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="公司款号">
          <el-input v-model="edit.product.company" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="颜色" prop="color">
          <el-input v-model="edit.product.color" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="描述">
          <el-input type="textarea" placeholder="请输入产品描述" v-model="edit.product.comments" maxlength="45" show-word-limit></el-input>
        </el-form-item>
      </el-card>
    </div>

    <div id="edit-process">
      <h2>工艺流程</h2>
      <el-card>
        <el-form-item label="状态" prop="process.status"
                      :rules="{required: true, message: '请填写工艺状态', trigger: 'blur'}">
          <el-input placeholder="请输入工艺状态" v-model="edit.process.status" maxlength="5" show-word-limit></el-input>
        </el-form-item>

        <el-form-item label="描述" :prop="edit.process.comments"
                      :rules="{required: true, message: '请填写工艺描述', trigger: 'blur'}">
          <el-input type="textarea" placeholder="请输入工艺描述" v-model="edit.process.comments" maxlength="45" show-word-limit></el-input>
        </el-form-item>

        <draggable v-model="edit.process.data" forceFallback="true" animation="1000">
          <transition-group>
            <el-form-item v-for="(item, index) in edit.process.data"
                          :label="'步骤'+(index+1)"
                          :key="item.procedure_id"
                          :prop="'process.data.' + index + '.name'"
                          :rules="{required: true, message: '请选择步骤', trigger: 'change'}">
              <el-select v-model="edit.process.data[index].name"
                         placeholder="请选择工序"
                         filterable>
                <el-option v-for="item in process_options"
                           :label="item"
                           :value="item"></el-option>
              </el-select>
              <el-button size="mini"
                         style="color: red; margin-left: 20px;"
                         @click="delProcess(index)">删除</el-button>
            </el-form-item>
          </transition-group>
        </draggable>
        <el-form-item>
          <el-button @click="addProcess">添加</el-button>
        </el-form-item>
      </el-card>
    </div>

    <div id="edit-material">
      <h2>物料清单</h2>
      <el-card>
        <div v-for="(item, index) in edit.material" style="display: flex; flex-direction: row;">
          <el-form-item :label="'物料'+(index+1)"
                        :prop="'material.' + index + '.size'"
                        :rules="{required: true, message: '请选择物料', trigger: 'change'}">
            <el-cascader placeholder="请选择物料"
                         v-model="original_material[index]"
                         :options="material_options"
                         @change="setMaterial(index, $event)"
                         clearable>
            </el-cascader>
          </el-form-item>

          <el-form-item label-width="0px" :prop="'material.' + index + '.count'"
                        :rules="numRules">
            <el-input-number style="width: 25%; margin-left: 10px;"
                             v-model="item.count"
                             :precision="0" :controls="false"></el-input-number>
            <el-button size="mini"
                       style="color: red; margin-left: 20px;"
                       @click="delMaterial(index)">删除</el-button>
          </el-form-item>

        </div>

        <el-form-item>
          <el-button @click="addMaterial">添加</el-button>
        </el-form-item>
      </el-card>

      <el-form-item>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="commit">提交</el-button>
      </el-form-item>
    </div>
  </el-form>
</template>

<script>
import Draggable from "vuedraggable"
import {request} from "@/network/request";

export default {
  name: "edit",
  props: ['product_id', 'detailDrawer', 'changeCommit'],
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
      num: 0,
      edit: {
        product: {},
        process: {},
        material: [],
      },
      original_material: [],
      process_options: [],
      material_options: [],
      numRules: [
        {validator: checkNum, trigger: 'blur'}
      ]
    }
  },
  methods: {
    // 页面初始化获取数据
    getData() {
      request({
        url: '/process/getInfosByID',
        method: 'get',
        params: {
          product_id: this.product_id
        }
      }).then(res => {
        this.num = res.data.process.data.length
        this.edit.product = res.data.product
        this.edit.process = res.data.process
        this.edit.material = res.data.material
        this.setOriginalMaterial(res.data.material)
      }).catch(err => {
        console.log(err)
      });
    },

    // 获取工序下拉列表的值
    getProcess() {
      request({
        url: '/getProceduresByCompany',
        method: 'get',
        params:{
          company_id:this.$store.getters.userinfo.company_id,
        }
      }).then(res => {
        this.process_options = res.data
      }).catch(err => {
        console.log(err)
      });
    },

    // 获取物料下拉列表的值
    getMaterial() {
      request({
        url: '/getOptionMaterial',
        method: 'get',
      }).then(res => {
        this.material_options = res.data
      }).catch(err => {
        console.log(err)
      });
    },

    // 添加工序getinfosbyid
    addProcess() {
      this.num++
      this.edit.process.data.push({procedure_id: this.num, name: ''})
    },

    // 删除工序
    delProcess(index) {
      this.edit.process.data.splice(index, 1)
    },

    // 当material数据发生修改时调用
    setMaterial(index, event) {
      this.edit.material[index].name = event[0]
      this.edit.material[index].size = event[1]
      // this.edit.material[index].color = event[2]
    },

    // 删除某一个物料
    delMaterial(index) {
      this.edit.material.splice(index, 1)
      this.original_material.splice(index, 1)
    },

    // 添加物料
    addMaterial() {
      this.edit.material.push({name: '', size: ''})
    },

    setOriginalMaterial(data) {
      for (let item of data) {
        let a = []
        a.push(item.name)
        a.push(item.size)
        // a.push(item.color)
        this.original_material.push(a)
      }
    },

    // 提交修改
    commit() {
      this.$confirm("是否提交本次修改？", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs['editForm'].validate((valid) => {
          if (this.edit.process.data.length < 1) {
            this.$message({
              type: 'error',
              message: "步骤不能为空"
            })
            return false
          } else if (this.edit.material.length < 1) {
            this.$message({
              type: 'error',
              message: "物料不能为空"
            })
            return false
          } else if (valid) {
            request({
              url: '/process/update2',
              method: 'post',
              data: {
                operator_id: this.$store.getters.userinfo.id,
                product: this.edit.product,
                process: this.edit.process,
                material: this.edit.material
              }
            }).then(res => {
              if (res.data == "更新成功") {
                this.changeCommit()
                this.detailDrawer.closeDrawer()
                this.$message({
                  type: 'success',
                  message: '更新成功'
                })
              } else {
                this.$message({
                  type: 'error',
                  message: res.data
                })
              }
            }).catch(err => {
              this.$message({
                type: 'error',
                message: '网络错误，修改失败'
              })
            });
          } else {
            return false;
          }
        });
      })
    },

    // 取消修改
    cancel() {
      this.detailDrawer.closeDrawer()
    }
  },
  components: {
    Draggable
  },
  created() {
    this.getData()
    this.getProcess()
    this.getMaterial()
  }
}
</script>

<style scoped>
#edit-base {
  overflow-y: auto;
  position: absolute;
  width: 100%;
  height: 85%;
}
#edit-product {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#edit-process {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#edit-process .el-card {
  margin-bottom: 5px;
}
#edit-material {
  text-align: left;
  margin-left: 20px;
  margin-right: 20px;
}
#edit-material .el-card {
  margin-bottom: 5px;
}
</style>