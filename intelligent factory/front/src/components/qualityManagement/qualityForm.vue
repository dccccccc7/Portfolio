<template>
  <div>
    <el-form id="qualityForm" ref="form" :model="qualityForm" label-width="100px" size="large">
      <el-form-item label="瑕疵类别">
        <el-select v-model="qualityForm.name" filterable clearable placeholder="请选择" style="width: 100%;">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="质检时间">
        <el-col :span="10">
          <el-date-picker type="date" placeholder="选择日期" v-model="qualityForm.date1"
                          style="width: 100%;"></el-date-picker>
        </el-col>
        <el-col class="line" :span="4">----</el-col>
        <el-col :span="10">
          <el-time-picker placeholder="选择时间" v-model="qualityForm.date2" style="width: 100%;"></el-time-picker>
        </el-col>
      </el-form-item>

      <el-form-item label="质检地点">
        <el-col :span="10">
          <el-input v-model="qualityForm.workshop" clearable style="width: 100%;"></el-input>
        </el-col>
        <el-col class="line" :span="4">生产线</el-col>
        <el-col :span="10">
          <el-input v-model="qualityForm.productionLine" clearable style="width: 100%;"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="样品数量">
        <el-col :span="10">
          <el-input v-model="qualityForm.total" clearable style="width: 100%;"></el-input>
        </el-col>
        <el-col class="line" :span="4">不合格数量</el-col>
        <el-col :span="10">
          <el-input v-model="qualityForm.unqualifiedNum" clearable style="width: 100%;"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="问题工序">
        <el-input v-model="qualityForm.problemProcess" clearable style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="执行工序人员">
        <el-input v-model="qualityForm.processPersonnel" clearable style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="具体瑕疵描述">
        <el-input v-model="qualityForm.description" clearable style="width: 100%;"></el-input>
      </el-form-item>

      <el-form-item size="large">
        <el-button type="primary" @click="onSubmit">提交</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>

    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%">
      <span>提交成功</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>

export default {
  data() {
    return {
      qualityForm: {
        id: '',
        name: '',
        date1: '',
        date2: '',
        workshop: '',
        productionLine: '',
        total: '',
        unqualifiedNum: '',
        problemProcess: '',
        processPersonnel: '',
        description: '',
        category: {
          id: '',
          name: ''
        }
      },
      dialogVisible: false,
      options: [{
        value: '1',
        label: '瑕疵1'
      }, {
        value: '2',
        label: '瑕疵2'
      }, {
        value: '3',
        label: '瑕疵3'
      }, {
        value: '4',
        label: '瑕疵4'
      }, {
        value: '5',
        label: '瑕疵5'
      }],
      value: ''
    };
  },
  methods: {
    onSubmit() {
      this.$http
          .post('data/quality', {
            name: this.qualityForm.name,
            date1: this.qualityForm.date1,
            date2: this.qualityForm.date2,
            workshop: this.qualityForm.workshop,
            productionLine: this.qualityForm.productionLine,
            total: this.qualityForm.total,
            unqualifiedNum: this.qualityForm.unqualifiedNum,
            problemProcess: this.qualityForm.problemProcess,
            processPersonnel: this.qualityForm.processPersonnel,
            description: this.qualityForm.description,
          }).then(resp => {
        console.log(this.qualityForm)
        if (resp && resp.status === 200) {
          //this.$emit('onSubmit')
          this.clear()
          this.dialogVisible = true
        }
      })
    },
    clear() {
      this.qualityForm = {
        name: '',
        date1: '',
        date2: '',
        workshop: '',
        productionLine: '',
        total: '',
        unqualifiedNum: '',
        problemProcess: '',
        processPersonnel: '',
        description: '',
      }
    },
  }
};
</script>

<style>


</style>
