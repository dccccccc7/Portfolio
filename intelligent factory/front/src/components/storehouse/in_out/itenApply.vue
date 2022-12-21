<template>
<div style="margin-left: 50px;margin-top: 40px;width: 40%">
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="设备申请" name="first">
      <el-form :model="equipmentInfo" ref="equipmentInfo" label-width="100px" class="demo-ruleForm">
        <el-form-item
            label="车间编号"
            prop="workshop_id"
            :rules="[
                      { required: true, message: '车间编号不能为空'},
                    ]">
          <el-select
              style="float: left;width:100%"
              v-model.trim="equipmentInfo.workshop_id"
              placeholder="请选择车间编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              @change="selectChange"
          >
            <el-option
                v-for="(item,index) in options_workshop"
                :key="index"
                :label="item.id"
                :value="item.id" />
          </el-select>

        </el-form-item>
        <el-form-item
            label="设备编号"
            prop="equip_id"
            :rules="[
                      { required: true, message: '设备名称不可为空'},
                    ]">
          <el-input style="width:48%" placeholder="请输入设备名称"  v-model.trim="equipmentInfo.equip_name"
                    @change="selectEquipChange" clearable autocomplete="off"></el-input>
          <el-select
              style="width:48%;margin-left: 4%"
              v-model.number="equipmentInfo.equip_id"
              placeholder="请选择设备编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
          >
            <el-option
                v-for="(item,index) in options"
                :key="index"
                :label="item.equipment_id"
                :value="item.equipment_id" />
          </el-select>        </el-form-item>
        <el-form-item
            label="申请数量"
            prop="quantity"
            :rules="[
                      { required: true, message: '申请数量不能为空'},
                      { pattern: /^[0-9]*$/, message: '只能输入正整数'},
                    ]">
          <el-input  v-model.trim="equipmentInfo.quantity" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
            label="出库/入库"
            prop="in_out"
            :rules="[
                      { required: true, message: '出入库不能为空'},
                    ]">
          <el-select style="float: left;width:100%" v-model.trim="equipmentInfo.in_out" @change="changeStatus_e" placeholder="请选择出入库类型" >
            <el-option label="出库" value="出库"></el-option>
            <el-option label="入库" value="入库"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
            v-if="show_e"
            label="货区编号"
            prop="storage_id"
            :rules="[
                      { required: true, message: '货区编号不能为空'},
                    ]">
          <el-select
              style="float: left;width:100%"
              v-model.trim="equipmentInfo.storage_id"
              placeholder="请选择货区编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              @change="sectorEquipChange"
          >
            <el-option
                v-for="(item,index) in options_sector"
                :key="index"
                :label="item.id"
                :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item
            v-if="show_e"
            label="货架编号"
            prop="shelf_id"
            :rules="[
                      { required: true, message: '货架编号不能为空'},
                    ]">
          <el-select
              style="float: left;width:100%"
              v-model.trim="equipmentInfo.shelf_id"
              placeholder="请选择货架编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              @change="selectChange"
          >
            <el-option
                v-for="(item,index) in options_shelf"
                :key="index"
                :label="item.id"
                :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitApplyEquip('equipmentInfo')">提交</el-button>
          <el-button @click="resetForm('equipmentInfo')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="物料申请" name="second">
      <el-form :model="materialInfo" ref="materialInfo" label-width="100px" class="demo-ruleForm">
        <el-form-item
            label="车间编号"
            prop="workshop_id"
            :rules="[
                      { required: true, message: '车间编号不能为空'},
                    ]">
          <el-select
              style="float: left;width:100%"
              v-model.trim="materialInfo.workshop_id"
              placeholder="请选择车间编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              @change="selectChange"
          >
            <el-option
                v-for="(item,index) in options_workshop"
                :key="index"
                :label="item.id"
                :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item
            label="物料编号"
            prop="material_id"
            :rules="[
                      { required: true, message: '物料名称不可为空'},
                    ]">
          <el-input style="width:48%" placeholder="请输入物料名称" v-model.trim="materialInfo.material_name"
                    @change="selectMaterialChange" clearable autocomplete="off"></el-input>
          <el-select
              style="width:48%;margin-left: 4%"
              v-model.number="materialInfo.material_id"
              placeholder="请选择物料编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              >
            <el-option
                v-for="(item,index) in options"
                :key="index"
                :label="item.material_id"
                :value="item.material_id" />
          </el-select>
        </el-form-item>
        <el-form-item
            label="申请数量"
            prop="quantity"
            :rules="[
                      { required: true, message: '申请数量不能为空'},
                      { pattern: /^[0-9]*$/, message: '只能输入正整数'},
                    ]">
          <el-input  v-model.trim="materialInfo.quantity" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
            label="出库/入库"
            prop="in_out"
            :rules="[
                      { required: true, message: '出入库不能为空'},
                    ]">
          <el-select style="float: left;width:100%" v-model.trim="materialInfo.in_out" @change="changeStatus_m" placeholder="请选择出入库类型" >
            <el-option label="出库" value="出库" ></el-option>
            <el-option label="入库" value="入库" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
            v-if="show"
            label="货区编号"
            prop="storage_id"
            :rules="[
                      { required: true, message: '货区编号不能为空'},
                    ]">
          <el-select
              style="float: left;width:100%"
              v-model.trim="materialInfo.storage_id"
              placeholder="请选择货区编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              @change="sectorMaterialChange"
          >
            <el-option
                v-for="(item,index) in options_sector"
                :key="index"
                :label="item.id"
                :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item
            v-if="show"
            label="货架编号"
            prop="shelf_id"
            :rules="[
                      { required: true, message: '货架编号不能为空'},
                    ]">
          <el-select
              style="float: left;width:100%"
              v-model.trim="materialInfo.shelf_id"
              placeholder="请选择货架编号"
              clearable
              filterable
              @blur="selectBlur"
              @clear="selectClear"
              @change="selectChange"
          >
            <el-option
                v-for="(item,index) in options_shelf"
                :key="index"
                :label="item.id"
                :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitApplyMaterial('materialInfo')">提交</el-button>
          <el-button @click="resetForm('materialInfo')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";

export default {
  name: "itenApply",
  data() {
    return {
      show: false,
      show_e: false,
      userMsg:'',

      activeName: 'second',
      equipmentInfo:{
        workshop_id:'',
        equip_id:'',
        equip_name:'',
        quantity:'',
        in_out:'',
        storage_id:'',
        shelf_id:''
      },
      materialInfo:{
        workshop_id: '',
        material_id:'',
        material_name:'',
        quantity: '',
        in_out:'',
        storage_id:'',
        shelf_id:''
      },
      options: [
      ],
      options_workshop: [
      ],
      options_sector: [
      ],
      options_shelf: [
      ],
    };
  },
  created() {
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)
    this.getWorkshop()
    this.getSecctor()
  },
  methods: {
    //获取车间编号和货架编号
    getWorkshop(){
      let req = {
        company_id:this.userMsg.company_id,
      }
      my_request({
        url:'workshop/all_workshop',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        //改data
        this.options_workshop = res.data
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })
    },
    getSecctor(){
      let req = {
        company_id:this.userMsg.company_id,
      }
      my_request({
        url:'storage/all_storage',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        this.options_sector = res.data
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })
    },
    //入库输入框显示
    changeStatus_m() {
      if (this.materialInfo.in_out==='入库') {
        this.show = true;
      }
      if (this.materialInfo.in_out==='出库') {
        this.show = false;
      }
    },
    changeStatus_e() {
      if (this.equipmentInfo.in_out==='入库') {
        this.show_e = true;
      }
      if (this.equipmentInfo.in_out==='出库') {
        this.show_e = false;
      }
    },

    selectBlur(e) {
      if (e.target.value !== '') {
        this.value = e.target.value + '(其他)';
        this.$forceUpdate()   // 强制更新
      }
    },
    selectClear() {
      this.value = ''
      this.$forceUpdate()
    },
    selectChange(val) {
      this.value = val
      this.$forceUpdate()
    },
    //根据库区选货架
    sectorEquipChange(){
      let req = {
        storage_id:this.equipmentInfo.storage_id,
        company_id:this.userMsg.company_id
      }
      console.log('req:',req)
      my_request({
        url:'shelf/shelfById',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        //改data
        this.options_shelf = res.data
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })
    },
    sectorMaterialChange(){
      let req = {
        storage_id:this.materialInfo.storage_id,
        company_id:this.userMsg.company_id
      }
      console.log('req:',req)
      my_request({
        url:'shelf/shelfById',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        //改data!!!!
        this.options_shelf = res.data
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })
    },

    selectEquipChange() {
      let req = {
        name:this.equipmentInfo.equip_name,
        company_id:this.userMsg.company_id
      }
      console.log('req:',req)

      my_request({
        url:'process/getEquipmentByName',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        this.options = res.data.equipments
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })
    },
    selectMaterialChange() {
      let req = {
        name:this.materialInfo.material_name,
        company_id:this.userMsg.company_id
      }
      console.log('req:',req)

      my_request({
        url:'process/getMaterialByName',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        this.options = res.data.materials
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },
    submitApplyEquip(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let req = {
            workshop_id: this.equipmentInfo.workshop_id,
            id: this.equipmentInfo.equip_id,
            quantity: this.equipmentInfo.quantity,
            in_out: this.equipmentInfo.in_out,
            type: 'equipment',
            user: this.$store.getters.userinfo.id,
            //改名字
            storage_id:this.equipmentInfo.storage_id,
            shelf_id:this.equipmentInfo.shelf_id,
            company_id:this.userMsg.company_id
            // user: 1
          }
          console.log('申请设备的req：',req)
          my_request({
            url:'goods/apply',
            method:'get',
            params:req
          }).then(res=>{
            console.log('申请设备的res：',res)

            if (res.data.success===true){
              this.$message({
                type:'success',
                message:'申请成功'
              })
              this.$refs[formName].resetFields();
            }else {
              this.$message.error('申请失败')
            }
          }).catch(err=>{
            this.$message.error('服务器异常')
          })

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    submitApplyMaterial(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let req = {
            workshop_id: this.materialInfo.workshop_id,
            id: this.materialInfo.material_id,
            quantity: this.materialInfo.quantity,
            in_out: this.materialInfo.in_out,
            type:'material',
            user: this.$store.getters.userinfo.id,
            //改名字
            storage_id:this.materialInfo.storage_id,
            shelf_id:this.materialInfo.shelf_id,
            company_id:this.userMsg.company_id
            // user: 1
          }
          console.log('申请材料的req：',req)

          my_request({
            url:'goods/apply',
            method:'get',
            params:req
          }).then(res=>{
            console.log('申请材料的res：',res)

            if (res.data.success===true){
              this.$message({
                type:'success',
                message:'申请成功'
              })
              this.$refs[formName].resetFields();
            }else {
              this.$message.error('申请失败')
            }
          }).catch(err=>{
            this.$message.error('服务器异常')
          })

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }


}
</script>

<style scoped>

</style>