<template>
  <div style="display: table">
    <el-divider style="background-color: black">全部课程</el-divider>
    <all-course-table ref="allCourse" @OverallLoad="load"></all-course-table>
    <el-divider style="background-color: black">已选课程</el-divider>
    <my-course-table ref="myCourse" @OverallLoad="load"></my-course-table>
  </div>



</template>

<script>
import allCourseTable from "@/components/allCourseTable";
import myCourseTable from "@/components/myCourseTable";

export default {
  name: "Choose",
  components:{
    allCourseTable,
    myCourseTable
  },
  data(){
    return{
    }
  },
  created() {
  },
  methods:{
    load(){
      this.$refs.allCourse.load()
      this.$refs.myCourse.load()
    }
  }
}
</script>

<style scoped>

</style>