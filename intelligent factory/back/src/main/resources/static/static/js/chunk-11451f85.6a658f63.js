(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-11451f85"],{1332:function(t,e,s){},"1bab":function(t,e,s){"use strict";s.d(e,"a",(function(){return r}));s("d3b7");var i=s("bc3a"),a=s.n(i);function r(t){return new Promise((function(e,s){var i=a.a.create({timeout:3e3});i(t).then((function(t){e(t)})).catch((function(t){s(t)}))}))}},"69ac":function(t,e,s){"use strict";var i=s("1332"),a=s.n(i);a.a},c95d:function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("el-container",{attrs:{id:"base"}},[s("el-header",{staticStyle:{"margin-top":"50px"}},[s("el-steps",{attrs:{active:t.active,"finish-status":"success"}},[s("el-step",{attrs:{title:"添加产品"}}),s("el-step",{attrs:{title:"分配工艺路线"}}),s("el-step",{attrs:{title:"填写物料清单"}}),s("el-step",{attrs:{title:"提交"}})],1)],1),s("el-main",{key:t.key,staticStyle:{margin:"50px 70px 20px 20px",padding:"0px 20px 20px 20px"}},[s("transition",{attrs:{mode:"out-in",name:"fade-transform"}},[s("keep-alive",[s("router-view",{ref:"add",staticStyle:{height:"400px"},attrs:{product:t.product,process:t.process,material:t.material}})],1)],1)],1),s("el-footer",{attrs:{id:"bottom"}},[s("el-button",{staticStyle:{"margin-top":"12px"},attrs:{disabled:t.disabled},on:{click:t.next}},[t._v(t._s(t.next_text))]),s("el-button",{directives:[{name:"show",rawName:"v-show",value:t.previous_show,expression:"previous_show"}],staticStyle:{"margin-top":"12px"},on:{click:t.previous}},[t._v("上一步")])],1)],1)},a=[],r=s("1bab"),o={name:"Add",data:function(){return{active:0,next_text:"下一步",disabled:!1,previous_show:!1,product:{},process:[],material:[],path:["product","process","material","ensure"],key:0}},methods:{next:function(){if(0==this.active){var t=this.$refs.add;t.submitForm()&&(this.active++,this.addProduct(),this.previous_show=!0,this.$router.push(this.path[this.active]))}else if(1==this.active){var e=this.$refs.add;e.submitForm()&&(this.active++,this.addProcess(),this.$router.push(this.path[this.active]))}else if(2==this.active){var s=this.$refs.add;s.submitForm()&&(this.active++,this.next_text="完成",this.addMaterial(),this.$router.push(this.path[this.active]))}else this.addCommit()},previous:function(){1==this.active?(this.active--,this.previous_show=!1,this.$router.push(this.path[this.active])):3==this.active?(this.active--,this.next_text="下一步",this.$router.push(this.path[this.active])):(this.active--,this.$router.push(this.path[this.active]))},addProduct:function(){var t=this.$refs.add.product;this.product.brand=t.brand,this.product.season=t.season,this.product.type=t.type,this.product.customer_id=t.customer_id,this.product.company_id=t.company_id,this.product.color=t.color,this.product.comments=t.comments},addProcess:function(){var t=this.$refs.add.process;this.process=t},addMaterial:function(){var t=this.$refs.add.material;this.material=t.data},init:function(){this.active=0,this.previous_show=!1,this.disabled=!1,this.next_text="下一步",this.key++,this.$router.push(this.path[0])},addCommit:function(){var t=this;Object(r["a"])({url:"/process/add",method:"post",data:{operator_id:this.$store.getters.userinfo.id,product:this.product,process:this.process,material:this.material}}).then((function(e){"添加成功"==e.data?(t.$message({type:"success",message:"添加成功"}),t.disabled=!0,t.active++,setTimeout((function(){t.init()}),1e3)):t.$message({type:"error",message:e.data})})).catch((function(e){t.$message({type:"error",message:"网络错误，添加失败"})}))}},created:function(){this.$router.push(this.path[0])},activated:function(){this.init()}},c=o,h=(s("69ac"),s("2877")),n=Object(h["a"])(c,i,a,!1,null,"f8d793b8",null);e["default"]=n.exports}}]);
//# sourceMappingURL=chunk-11451f85.6a658f63.js.map