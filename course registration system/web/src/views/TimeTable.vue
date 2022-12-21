<template>
  <div style="display: table">
    <el-divider style="background-color: black">{{ this.tableTitle }}</el-divider>
    <TimeTableCore ref="choosingTimeTable" @OverallLoad="load"></TimeTableCore>
    <el-divider style="background-color: black">{{ this.listTitle }}</el-divider>
    <my-course-table ref="myCourse" @OverallLoad="load"></my-course-table>
  </div>
</template>

<script>
import myCourseTable from "@/components/myCourseTable";
import TimeTableCore from "@/components/TimeTableCore";
import request from "@/utils/request";

export default {
  name: "TimeTable",
  components:{
    myCourseTable,
    TimeTableCore,
  },
  created() {
    this.load()
  },
  data(){
    return{
      CHOOSING:false,
      tableTitle:"",
      listTitle:""
    }
  },
  methods:{
    load(){
      request.get("/course/getCHOOSING").then(res=>{
        this.CHOOSING=res.data.flag
        if(this.CHOOSING){
          this.tableTitle = "预期课表"
          this.listTitle = "已选课程"
        }
        else{
          this.tableTitle = "课表"
          this.listTitle = "全部课程"
        }
        this.$refs.myCourse.load()
        this.$refs.choosingTimeTable.load()
      })
    }
  }
}
</script>

<style scoped>

</style>