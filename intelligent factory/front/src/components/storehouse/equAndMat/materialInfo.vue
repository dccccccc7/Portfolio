<template>
  <div style=" ;width: 90%;height:80%;margin: 10px auto 0;">
    <div style="width: 100%;height: 40px;margin: 10px 0;margin-bottom: 20px ;display: flex;justify-content: space-between">
      <div style="width: 30%;height: 40px; ;display: flex;justify-content: flex-start">
        <div style="width: 80%;height: 40px;display: flex;justify-content: space-between">
          <el-form :model="input" ref="input" label-width="100px" class="demo-ruleForm">
            <el-form-item
                label="物料名称"
                prop="input"
                :rules="[
                        // { required: true, message: '物料id不能为空'},
                      ]">
              <el-input v-model="input.input" autocomplete="off" @input="sel"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div style="width: 30%;height: 40px; "></div>
      <div style="width: 30%;height: 40px; ">
        <el-button @click="addMaterial = true"  >增加</el-button>

      </div>
    </div>
    <template >
      <el-table
          stripe
          v-loading="loading"
          :data="showData"
          style="width: 100%">
        <el-table-column
            prop="material_id"
            label="物料编号">
        </el-table-column>
        <el-table-column
            prop="name"
            label="名称">
        </el-table-column>
<!--        <el-table-column-->
<!--            prop="color"-->
<!--            label="颜色">-->
<!--        </el-table-column>-->
        <el-table-column
            label="属性"
        >
          <template v-slot="scope">
            <el-popover trigger="hover" placement="top">
              <div v-for="(val,key) in  JSON.parse(scope.row.size)">
                <p>{{key}}:{{val}}</p>
              </div>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">查看属性</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
            prop="status"
            label="状态">
        </el-table-column>
        <el-table-column
            prop="comments"
            label="描述">
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
            <el-button @click="edit(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="deleteThis(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </template>
    <div class="block" style="padding: 10px;margin-top: 10px">
      <el-pagination
          @prev-click="preclick"
          @next-click="nextclick"
          @current-change="curChange"
          :hide-on-single-page=true
          :total="total"
          background
          layout="total,prev, pager, next, jumper"
          :page-size="page.pages">
      </el-pagination>
    </div>

    <el-dialog
        title="添加物料"
        :visible.sync="addMaterial"
        width="30%"
        :before-close="handleClose">

      <el-form :model="incrementM" ref="incrementM" label-width="100px" class="demo-ruleForm">
        <el-form-item
            label="物料模板"
            prop="size"
            :rules="[
                        { required: true, message: '物料模板不能为空'},
                      ]">
          <el-select  v-model="materialID" placeholder="请选择" style="width: 100%" @change="choosePanel">
            <el-option
                v-for="item in  shitVue"
                :key="item.id"
                :value="item.id"
                :label="item.name+item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <div v-for="(val,key) in incrementM.size" :key="key">
          <el-form-item :label="key">
            <el-input v-model="incrementM.size[key]"></el-input>
          </el-form-item>
        </div>
        <el-form-item
            label="物料名称"
            prop="name"
            :rules="[
                        { required: true, message: '物料名称不能为空'},
                      ]">
          <el-input v-model.trim="incrementM.name" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item-->
<!--            label="物料颜色"-->
<!--            prop="color"-->
<!--            :rules="[-->
<!--                        { required: true, message: '物料颜色不能为空'},-->
<!--                      ]">-->
<!--          <el-input  v-model.trim="incrementM.color" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item
            label="物料描述"
            prop="comments"
            :rules="[
                        { required: true, message: '物料描述不能为空'},
                      ]">
          <el-input  v-model.trim="incrementM.comments" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="incrementThis('incrementM')">提交</el-button>
          <el-button @click="resetForm('incrementM')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
        title="提示"
        :visible.sync="updateM"
        width="30%"
        :before-close="handleClose">

      <el-form :model="updateInfo" ref="updateInfo" label-width="100px" class="demo-ruleForm">
        <el-form-item
            label="状态"
            prop="status"
            :rules="[
                        { required: true, message: '物料状态不能为空'},
                      ]">
          <el-select v-model.trim="updateInfo.status" autocomplete="off" placeholder="请选择状态参数"  style="width: 100%">
            <el-option
                v-for="item in optionStatus"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
            label="描述"
            prop="comments"
            :rules="[
                        { required: true, message: '物料描述不能为空'},
                      ]">
          <el-input v-model.trim="updateInfo.comments" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="updateThis('updateInfo')">提交</el-button>
          <el-button @click="resetForm('updateInfo')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";
export default {
  name: "materialInfo",
  data(){
    return {
      userMsg:'',
      showData:[],
      domains:[],
      materialID:'',
      loading:false,
      updateM:false,
      addMaterial:false,
      addPanel:false,
      shitVue:[{}],
      optionStatus:[{
        value:"正常",
        label:"正常"
      },
        {value:"禁用",
          label:"禁用"

        }],
      incrementM:{
        name:'默认',
        size:{},
        color:'',
        comments:'无',
        operator_id:'',
      },
      input:{
        input:'',
      },
      updateInfo:{
        material_id:'',
        operator_id:'',
        status:'',
        comments:'',
        company_id:'',
      },
      materials:[
        {
          material_id:'2',
          name:1,
          size:'',
          status:'iasd',
          comments:'s',

        }
      ],
      matJs:[
        {
          material_id:'2',
          name:'1',
          size:{},
          status:'iasd',
          comments:'s',

        }
      ],
      total: 1,
      page:{
        pages: 10,
        current: 1,
      },

    }
  },
  created() {
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)
    this.getData()
    this.getTemplate()

  },
  mounted(){

  },
  methods:{
    sel(){

      this.showData=this.materials.filter((item)=>{

        return item.name.includes(this.input.input)
      })
    },
    choosePanel(panel_id){
      let shit = this.shitVue

        this.addPanel=true
        for(let i in shit){
          if(panel_id==shit[i].id){
            let obj={}
            shit[i].attribute.forEach(item=>{
              obj[item]=''
            })
           this.incrementM.size=obj

            console.log(this.incrementM.size)
            console.log(Object.keys(this.incrementM.size))
          }
        }
    },

    preclick(current){
      this.page.current=current
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },
    nextclick(current){
      this.page.current=current
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)

    },
    curChange(current){
      this.page.current=current
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },
    getTemplate(){
      my_request({
      url:'/template/material/all?company_id='+this.userMsg.company_id,
      method:'get',

      }).then(res=>{
    this.shitVue=res.data.materials
        console.log(this.shitVue)
        console.log(res.data.materials)
  })
    },
    getData(){
      this.loading = true
      let req = {
        pageOffset: this.page.current,
        pageSize: this.page.pages,
        company_id:this.userMsg.company_id
      }
      my_request({
        url: '/process/getMaterials',
        method: 'get',
        params: req,
      }).then(res=>{
        console.log('返回的：',res)
        this.materials = res.data.materials
        this.showData=res.data.materials
        this.total = res.data.count
        console.log(this.materials)
      }).catch(err=>{
        console.log('ssss')
      })
      this.loading = false
      console.log(req)
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    //
    incrementThis(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          let req = {
            name: this.incrementM.name,
            size:JSON.stringify(this.incrementM.size),
            // color: this.incrementM.color,
            comments: this.incrementM.comments,
            company_id:this.userMsg.company_id,
            operator_id: 1,
          }
          console.log(typeof(this.incrementM.size))
          console.log('增加信息参数：',req)
          my_request({
            url:'/process/addMaterial',
            method: 'post',
            data: req,
          }).then(res =>{
            if (res.data==='添加成功'){
              this.addMaterial = false
              this.getData()
              this.$message({
                type:'success',
                message:'添加成功'
              })
            }else {
              this.$message.error('添加失败')
            }
            this.loading=false
            console.log('增加信结果：',res)
          }).catch(err=>{

          })

        } else {
          console.log('error submit!!');
          return false;
        }
      });
      this.incrementM.name="默认"
      this.incrementM.comments="无"
      this.incrementM.operator_id=1
      this.incrementM.size={}
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          if(this.input.input===''){
            this.getData()
          }else {
            this.loading = true
            let req = {
              material_id: this.input.input
            }
            console.log('查询参数：', req)
            my_request({
              url: '/process/getMaterialByName',
              params: req,
              method: 'get'
            }).then(res => {
              console.log('查询的结果：', res)
              console.log('---', res.data)
              let s = []
              s.push(res.data)
              if (res.data!==null){
                this.materials = s
                this.total = 1
              }else {
                this.$message.error('未查询到该物品')
              }
              this.loading=false
            }).catch(err => {
              console.log('?')
            })
          }
        } else {
          console.log('!')
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    handleClick(row){
      this.$router.push({
        path:'/storehouse/equAndMatMain/materialInfoDetail',
        query:{
          info: row
        }
      })
    },
    deleteThis(row){
      this.loading = true
      let req = {
        material_id : row.material_id,
        company_id:row.company_id,
        operator_id : 1,
      }
      my_request({
        url:'/process/deleteMaterialByID',
        method:'post',
        data:req
      }).then(res=>{
        if (res.data==='删除成功'){
          this.$message({
            type:'success',
            message:'删除成功'
          })
        }else {
          this.$message.error('删除失败')
        }
        this.getData()
      }).catch(err=>{

      })
    },

    edit(row){
      this.updateM = true
      this.updateInfo.material_id = row.material_id
      this.updateInfo.company_id=row.company_id
      this.operator_id = 1
    },
    updateThis(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let req= this.updateInfo
          this.loading=true
          my_request({
            url:'/process/updateMaterialByID',
            data:req,
            method:'post'
          }).then(res=>{

            console.log('修改--',res);
            this.updateM = false
            this.getData()
          }).catch(err=>{

          })

        } else {
          console.log('!')
        }
      });

    }
  }
}
</script>

<style scoped>
td {
  padding:3px !important;
  /*border: 1px solid blue;*/
  height: 6px;
  overflow: hidden;
}
</style>