
<template>
  <div>
    <search-bar @onSearch="searchResult" ref="searchBar" class="searchBar" ></search-bar>

    <el-row class="el-row1">
      <el-tooltip effect="transparent" placement="bottom"
                  v-for="(item,index) in modeldata.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                  :key="index">
        <el-card class="card" bodyStyle="padding:10px" shadow="hover">
          <div class="info">
            <div class="clearfix" slot="header" >
              <span style="float: left">模版号：{{ item.id}}</span>
              <span>
                <i class="el-icon-delete" @click="deleteModel(item)"></i>
              </span>
              <span style="float: right;margin-right: 10px">
                <i class="el-icon-edit" @click="editModel(item)"></i>
              </span>
            </div>
            <div class="text item">
              <p>模版名称：{{ item.name}}</p>
              <div v-for="(att,index) in item.attribute">
                <p>属性{{index+1}}: {{ att }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-tooltip>
      <model-form @onSubmit="loadModels()" ref="edit"></model-form>
    </el-row>
    <el-row>
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="modeldata.length"
          :hide-on-single-page="true">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
import modelForm from "@/components/modelManagement/modelForm";
import SearchBar from "@/components/modelManagement/SearchBar";
import modelSideMenu from "@/components/modelManagement/modelSideMenu"
import {request} from "@/network/request";

export default {
  name: 'modelAll',
  components: {modelForm, SearchBar,modelSideMenu},
  data() {
    return {
      classification:'material',
      modeldata: [],
      currentPage: 1,
      pagesize: 4,
      name:'',
    }
  },
  mounted: function () {
    this.loadModels()
  },
  methods: {
    loadModels() {

      request({
        url: '/template/material/all',
        method: 'get',
        params: {
          company_id: this.$store.getters.userinfo.company_id,
        }
      }).then(res => {
        console.log(res.data.materials)
        this.modeldata = res.data.materials

      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
      console.log(this.currentPage)
    },
    handleChange(val) {
      console.log(val);
    },

    searchResult() {
      // let classification = this.$refs.searchBar.keytype
      let classification=this.classification
      let myurl = ""
      if (classification==="material"){
        myurl="template/material/getMaterialTemplateByName"
      }
      if (classification==="equipment"){
        myurl="template/equipment/getEquipmentTemplateByName"
      }
      // if (classification==="product"){
      //   myurl="/template/product/getProductTemplateByName"
      // }
      console.log("search"+classification)
      // console.log(myurl)

      request({
        url: myurl,
        methods: 'get',
        params: {
          name: this.$refs.searchBar.keywords,
          company_id: this.$store.getters.userinfo.company_id,
        }
      }).then(res => {
        let data = res.data;
        if (classification==='material'){
          this.modeldata = data.materials;
        }
        if (classification==='equipment'){
          this.modeldata = data.equipments;
        }
        // this.loadModels()
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    },
    deleteModel(item) {
      let url=""
      if (item.type==="material"){
        url="template/material/deleteMaterialTemplate"
      }
      if (item.type==="equipment"){
        url="template/equipment/deleteEquipmentTemplate"
      }

      console.log("delete"+item.type)
      // console.log(url)
      this.$confirm('此操作将永久删除该模版, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(resp => {
            request({
              url:url,
              method:'post',
              data:{
                name:item.name,
                id:item.id,
                attribute:item.attribute,
                company_id: this.$store.getters.userinfo.company_id,
        },
            }).then(resp => {
              if (resp && resp.status === 200) {
                this.loadModels()
              }
            }).catch(err => {
              this.$message.error('网络出错，请稍后再试');
            });
          }
      ).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
      // alert(id)
    },
    editModel(item) {
      this.$refs.edit.dialogFormVisible = true
      let myattributes=[]
      for(let i =0;i<item.attribute.length;i++)
      { myattributes.push({id: i,content:item.attribute[i]}) }
      let mytype = ""
      if(item.type==="material") {mytype="物料模版"}
      if(item.type==="equipment"){mytype="设备模版"}
      // console.log(myattributes)
      this.$refs.edit.modelEditForm = {
        templateName: item.name,
        templateId: item.id,
        attributes: myattributes,
        classification: mytype,
      }
      this.$router.push({name:'modelManagementComponent'})

    },
  }
}
</script>
<style scoped>
.el-row1{
  width: auto;
  height: auto;
  display:flex;
  flex-wrap: wrap;
  align-items: flex-start;
}

.card{
  width: 20%;
  max-height: max-content;
  float: left;
  margin-right: 15px;
  margin-bottom: 20px;
}

img {
  width: 115px;
  height: 172px;
  /*margin: 0 auto;*/
}
.title {
  font-size: 14px;
  text-align: left;
}

.info {
  /*display: flex;*/
}

.el-icon-delete,.el-icon-edit{
  cursor: pointer;
  font-size: 20px;
  float: right;
}

a {
  text-decoration: none;
}

a:link, a:visited, a:focus {
  color: #3377aa;
}

.text {
  font-size: 14px;
  text-align: left;
}

.item {
  margin-bottom: 18px;
}

.searchBar {
  margin-top: 2%;
  margin-bottom: 3%;
  left: 30%
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>

