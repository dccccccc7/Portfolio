<template>
<div>
  <div class="mainAdd" style="align-items: flex-start;width: 37.5%">
    <el-input :placeholder="select==='库区'?'库区编号':'库区编号+货架编号'" v-model.trim="searchInput" class="input-with-select">
      <el-select v-model="select" slot="prepend" style="width: 80px" placeholder="请选择">
        <el-option label="库区" value="库区"></el-option>
        <el-option label="货架" value="编号"></el-option>
      </el-select>
    </el-input>
    <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
  </div>
  <router-view></router-view>
</div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";
import tableSector from "@/components/storehouse/tableSector";
export default {
  name: "shelfManage",

  data() {
    return {
      //  search
      select: '库区',
      showSearchSectorInfo: false,
      showSearchShelfInfo: false,
      searchInput: '',

    }
  },
  methods: {
    //  search=============
    // search(){
    //   if (this.searchInput.indexOf(' ')===-1){
    //     console.log('库区')
    //   }else {
    //     console.log('货架')
    //   }
    //   let str = this.searchInput.split('+')
    //   console.log(str,'str')
    // }
    search() {
      // if(this.select==='库区'){
      console.log(this.searchInput)
      this.$router.push({
        path: '/storehouse/storageMain/ShelfInfo',
        query: {
          sectorId: this.searchInput
        }
      })


      //   }else {
      //     console.log('货架')
      //
      //     if (this.searchInput.indexOf('+')!==-1 && this.searchInput.split('+').length===2&&this.searchInput.split('+')[0]!==''&&this.searchInput.split('+')[1]!==''){
      //       let info = this.searchInput.split('+')
      //       console.log(info.length,'kenegsads')
      //         this.$router.push({
      //           path: '/storehouse/storageMain/ShelfInfo',
      //           query: {
      //             sectorId: info[0],
      //             shelfId: info[1]
      //           }
      //         })
      //     }else {
      //      this.$message({
      //        type:'info',
      //        message:"请输入'库区编号+货架编号'"
      //      })
      //     }
      //
      //   }
      //
      //
      //   if (this.searchInput.indexOf('+')!==-1){
      //     console.log(this.searchInput)
      //     let info = this.searchInput.split('+')
      //     console.log(info.length,'kenegsads')
      //     if(info[0]===''||info[1]===''||info.length>2){
      //       console.log('err')
      //     }else {
      //       this.$router.push({
      //         path: '/storehouse/storageMain/ShelfInfo',
      //         query: {
      //           sectorId: info[0],
      //           shelfId: info[1]
      //         }
      //       })
      //     }
      //   }else {
      //     console.log(this.searchInput)
      //     this.$router.push({
      //       path: '/storehouse/storageMain/SectorInfo',
      //       query: {
      //         sectorId: this.searchInput
      //       }
      //     })
      //
      //   }
      // },

      //table delete

    }
  }
}
</script>

<style scoped>
.el-select .el-input {
  width: 230px;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.mainAdd .el-input .el-input__inner {
  width: 30%;
}

.mainAdd {
  width: 40%;
  height: auto;
  margin-top: 20px;
}

.mainAdd .el-input {
  width: 80%;
  margin-right: 20px;
}

.mainAdd .upupup {
  margin-right: 90px;
}

#theBaseMainBox {
  display: flex;
  /*background-color: gray;*/
  height: 600px;
  flex-direction: column;
  width: 1500px;
  margin: 80px;

}

#theBaseHeader {
  /*background-color: aliceblue;*/
  height: 15%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  /*align-items: center;*/
}

#theBaseMain {
  /*background-color: lightgreen;*/
  height: 80%;

  width: 100%;

}
</style>