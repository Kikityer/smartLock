import Vue from 'vue'
import Router from 'vue-router'
import admin from '..'
import homepage from '../pages/company/homepage'
import tableInfo from '../pages/company/TableInfo'
import chartInfo from '../pages/company/ChartInfo'
import accountChange from '../pages/company/AccountChange'
import introduce from '../pages/company/Introduce'
import accountManage from '../pages/company/AccountManage'


import homepage1 from '../pages/customer/homepage1'
import buildingInfo from '../pages/customer/buildingInfo'
import lockState from '../pages/customer/LockState'
import studentsInfo from '../pages/customer/studentsInfo'
import lockOpen from '../pages/customer/LockOpen'
import introduce1 from '../pages/customer/Introduce1'
import accountChange1 from '../pages/customer/AccountChange1'


Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/login',
      name: '',
      component: admin,
      hidden: true
    },
    {
      path: '/homepage',
      name: '客户信息',
      iconCls: 'fa fa-users',
      component: homepage,
      children:[
        { path: 'tableInfo', component: tableInfo, name: '信息展示表' },
        { path: 'chartInfo', component: chartInfo, name: '信息展示图'},
      ]
    },
    {
      path: '/homepage',
      name: '产品信息',
      iconCls: 'fa fa-info-circle',
      component: homepage,
      children:[
        { path: 'introduce', component: introduce, name: '产品介绍'},
      ]
    },
    {
      path: '/homepage',
      name: '用户管理',
      iconCls: 'fa fa-wrench',
      component: homepage,
      children:[
        { path: 'accountManage', component: accountManage, name: '账号管理'},
        { path: 'accountChange', component: accountChange, name: '账号修改'},
      ]
    },







    {
      path: '/homepage1',
      name: '楼宇信息',
      iconCls: 'fa fa-institution (alias)',
      component: homepage1,
      hidden: true,
      // leaf: true,//只有一个节点
      sell:true,
      children:[
        { path: 'buildingInfo', component: buildingInfo, name: '楼宇信息查询' },
      ]
    },
    {
      path: '/homepage1',
      name: '学生信息',
      iconCls: 'fa fa-mortar-board (alias)',
      component: homepage1,
      hidden: true,
      sell:true,
      children:[
        { path: 'studentsInfo', component: studentsInfo, name: '学生信息查询' }

      ]
    },
    {
      path: '/homepage1',
      name: '门锁信息',
      iconCls: 'fa fa-unlock',
      component: homepage1,
      hidden: true,
      sell:true,
      children:[
        { path: 'lockState', component: lockState, name: '门锁状态查询' },
        { path: 'lockOpen', component: lockOpen, name: '开锁记录查询' }

      ]
    },
    {
      path: '/homepage1',
      name: '产品信息',
      iconCls: 'fa fa-info-circle',
      component: homepage1,
      hidden: true,
      sell:true,
      children:[
        { path: 'introduce1', component: introduce1, name: '产品介绍' }

      ]
    },
    {
      path: '/homepage1',
      name: '用户管理',
      iconCls: 'fa fa-wrench',
      component: homepage1,
      hidden: true,
      sell:true,
      children:[
        { path: 'accountChange1', component: accountChange1, name: '账号修改' }

      ]
    },

  ]
});
