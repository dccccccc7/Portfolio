<template>
  <div style="display: flex;justify-content: center;align-items: center">
    <el-form :inline="true">
      <el-form-item>
        <el-input
            :visible.sync="dialogFormVisible"
            @keyup.enter.native="searchClick"
            placeholder=""
            prefix-icon="el-icon-search"
            size="small"
            clearable
            style="width: 100%;margin-right: 10px"
            v-model="keywords">
        </el-input>
      </el-form-item>
      <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
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

