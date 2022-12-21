<template>
  <div style="display: flex;justify-content: center;align-items: center">
    <el-input
        @keyup.enter.native="searchClick"
        placeholder=""
        prefix-icon="el-icon-search"
        size="small"
        clearable
        style="width: 50%;margin-right: 10px"
        v-model="keywords">
    </el-input>
    <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data() {
    return {
      keywords: '',
      qualityForm: [],
      cardLoading: []
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

