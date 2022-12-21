<template>
  <div style="display: flex;justify-content: center;align-items: center">
    <el-form :inline="true">
<!--      <el-form-item-->
<!--                  :visible.sync="dialogFormVisible"-->
<!--                  @keyup.enter.native="searchClick"-->
<!--                  style="width: 40%;margin-right: 10px"-->
<!--                  :rules="{required: true, message: '请选择分类', trigger: 'blur'}">-->
<!--          <el-select-->
<!--              v-model="keytype" filterable placeholder="请选择" >-->
<!--&lt;!&ndash;              style="width: 100%;">&ndash;&gt;-->
<!--            <el-option-->
<!--                v-for="item in options"-->
<!--                :key="item.value"-->
<!--                :label="item.label"-->
<!--                :value="item.value">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-input
            :visible.sync="dialogFormVisible"
            @keyup.enter.native="searchClick"
            prefix-icon="el-icon-search"
            size="big"
            placeholder="请输入搜索内容"
            clearable
            style="width: 100%;margin-right: 10px"
            v-model="keywords">
        </el-input>
      </el-form-item>
      <el-button size="big" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
    </el-form>


  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data() {
    return {
      dialogFormVisible: false,
      keywords: '',
      modelForm: [],
      cardLoading: [],
      keytype:'',

      options: [
        {
          value: 'material',
          label: '物料模版'
        },
        {
          value: 'equipment',
          label: '设备模版'
        },
        // {
        //   value: 'ProductTemplate',
        //   label: '产品模版'
        // },
      ]
    }
  },

  methods: {
    searchClick() {
      this.$emit('onSearch')
    }

  }
}
</script>

<style scoped>

</style>

