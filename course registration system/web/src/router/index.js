import { createRouter, createWebHistory } from 'vue-router'
import Layout from "@/layout/Layout";
import request from "@/utils/request";

function getUserToken(){
  try{
    return JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
  }catch (e){
    sessionStorage.setItem("userToken","-1")
    router.push("/login")
  }

}

const routes = [
  {
    path: '/',
    name: 'Layout',
    component:Layout,
    redirect:'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    children:[
        //学生版块
      {
        path:'choose',
        name:'Choose',
        component:()=>import("@/views/Choose"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="student"){
            next("/login")
          }else{
            request.get("/course/getCHOOSING").then(res=>{
              if(!res.data.flag){
                next("/unChoosing")
              }
              else{
                next()
              }
            })
          }
        },
        meta:{
          title:'学生选课'
        }
      },
      {
        path:'timeTable',
        name:'TimeTable',
        component:()=>import("@/views/TimeTable"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="student"){
            next("/login")
          }else{
            next()
          }
        },
        meta: {
          title:'课表'
        }
      },
      {
        path:'historyCourse',
        name:'HistoryCourse',
        component:()=>import("@/views/HistoryCourse"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="student"){
            next("/login")
          }else{next()}
        },
        meta:{
          title:'历史课程与成绩'
        }
      },
        //教师版块
      {
        path:'teacherCourse',
        name:'TeacherCourse',
        component:()=>import("@/views/teacherCourse"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="teacher"){
            next("/login")
          }else{
            next()
          }
        },
        meta:{
          title:'课程列表'
        }
      },
      {
        path:'resultSet',
        name:'ResultSet',
        component:()=>import("@/views/resultSet"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="teacher"){
            next("/login")
          }else{
            next()
          }
        },
        meta:{
          title:'成绩评定'
        }
      },
      {
        path:'roster',
        name:'Roster',
        component:()=>import("@/views/roster"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="teacher"){
            next("/login")
          }else{
            next()
          }
        },
        meta:{
          title:'名册'
        }
      },
      {
        path:'courseList',
        name:'CourseList',
        component:()=>import("@/views/courseList"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="teacher"){
            next("/login")
          }else{
            next()
          }
        },
        meta:{
          title: '设立课程'
        }
      },
      {
        path: 'courseSet',
        name: 'CourseSet',
        component:()=>import("@/views/courseSet"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="teacher"){
            next("/login")
          }else{
            next()
          }
        },
        meta:{
          title: '编辑课程'
        }
      },
        //ROOT版块
      {
        path:'configuration',
        name:'Configuration',
        component:()=>import("@/views/Configuration"),
        beforeEnter: (to,from,next)=>{
          let userInfo = getUserToken()
          if(userInfo.role!="root"){
            next("/login")
          }else{
            next()
          }
        },
        meta:{
          title: '选课系统配置'
        }
      },
        //全角色版块
      {
        path:'userInfo',
        name:'UserInfo',
        component:()=>import("@/views/UserInfo"),
        meta:{
          title:'个人信息'
        }
      },
      {
        path:'unChoosing',
        name:'UnChoosing',
        component:()=>import("@/components/UnChoosing"),
        meta:{
          title:'不在选课时间'
        }
      }
    ]
  },
  {
    path:'/login',
    name:'Login',
    component: ()=>import("@/views/Login"),
    meta:{
      title:'登录系统'
    }
  },
  {
    path:"/register",
    name:"Register",
    component: ()=>import("@/views/Register"),
    meta:{
      title:'注册'
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router