<template>
<div>
  <el-table

      v-loading="loading"
      id="theMainTable"
      :data="tableData"
      highlight-current-row
      stripe
      @sort-change="sortchange"
      style="width: 100%">
    <el-table-column
        label="车间编号"
        prop="workshopId"
        sortable
        width="180">
    </el-table-column>
    <el-table-column
        label="车间名称"
        prop="workshopName"
        width="180">
    </el-table-column>
    <el-table-column
        label="工厂名称"
        prop="factory">
    </el-table-column>
    <el-table-column
        label="创建日期"
        prop="createDate"
        sortable
        width="200px">
    </el-table-column>
    <el-table-column
        label="修改日期"
        prop="updateDate"
        sortable
        width="200px">
    </el-table-column>
    <el-table-column
        fixed="right"
        label="操作"
        width="200">
      <template slot-scope="scope">
        <el-button style="padding: 8px" type="text" @click="handleClick({type:!showLine,id:scope.row.workshopId,name:scope.row.workshopName})">查看</el-button>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
export default {
  name: "TableWorkshop"
}
</script>

<style scoped>

</style>