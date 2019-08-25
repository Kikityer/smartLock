<template>
   <div>
      <el-row class="container">
        <!--顶部位置，占满24列，高60px-->
        <el-col :span="24" class="header">
          <!--前十列用于收缩和显示系统名称-->
          <el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
            {{collapsed?'':sysName}}
          </el-col>
          <!--中间十列，在其前面放了一个小图标，点击会向前收缩-->
          <el-col :span="10">
            <div class="tools" @click.prevent="collapse">
              <i class="el-icon-menu"></i>
            </div>
          </el-col>
          <!--顶部剩下部分，实现管理员图标，鼠标悬浮在图标上显示下拉菜单-->
          <el-col :span="4" class="userinfo">
            <el-dropdown trigger="hover">
              <span class="el-dropdown-link userinfo-inner"><img src="../../assets/admin.png" style="width: 45px;height: 45px;border-radius: 20px;margin: 10px 0 0 10px;float: right;"/>欢迎您，{{sysUserName}}！</span>
              <el-dropdown-menu  slot="dropdown">
                <el-dropdown-item  @click.native="introduce">关于产品</el-dropdown-item>
                <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-col>

        <!--除高为60px的顶部之外的其他部分，也分为24列-->
        <el-col :span="24" class="main">
          <aside :class="collapsed? 'menu-collapsed':'menu-expanded'">
            <!--导航菜单未折叠之前-->
            <!--定义真个menu的样式和事件  :default-active="$route.path"按照路由的路径默认激活。unique-opened每次只打开一个子菜单。-->
            <el-menu background-color="#f1f2f7" :default-active="$route.path"  class="el-menu-vertical-demo"  @open="handleOpen" @close="handleClose"  @select="handleselect" unique-opened router v-show="!collapsed">
              <!--一级菜单的名字为路由过来组件的路径名-->
              <template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
                  <el-submenu :index="index+''">
                      <template slot="title" ><i :class="item.iconCls"></i>  {{item.name}}</template>
                      <!--二级菜单的名字为子路由的名字-->
                      <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path" v-if="!child.hidden">{{child.name}}</el-menu-item>
                  </el-submenu>
              </template>
            </el-menu>
            <!--导航菜单折叠后-->
            <!--当点击工具图标时，v-show被激活，菜单实现向左折叠-->
            <ul class="el-menu  collapsed" v-show="collapsed" ref="menuCollapsed">
              <li v-for="(item,index) in $router.options.routes" v-if="!item.hidden" class="el-submenu item">
                <!--如果子菜单不是单一节点-->
                <template v-if="!item.leaf">
                  <div class="el-submenu__title" style="padding-left: 20px;" @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)"><i :class="item.iconCls"></i></div>
                  <ul class="el-menu submenu" :class="'submenu-hook-'+index" @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)">
                    <li v-for="child in item.children" v-if="!child.hidden" :key="child.path" class="el-menu-item" style="padding-left: 20px;" :class="$route.path===child.path?'is-active':''" @click="$router.push(child.path)">{{child.name}}</li>
                  </ul>
                </template>
                <!--如果子菜单是单一节点-->
                <template v-else>
                  <ul class="el-submenu">
                    <div class=" el-submenu__title el-menu-item" style="padding-left: 20px;height: 56px;line-height: 56px;padding: 0 20px;" :class="$route.path===item.children[0].path?'is-active':''" @click="$router.push(item.children[0].path)"><i :class="item.iconCls"></i></div>
                  </ul>
                </template>
              </li>
            </ul>
          </aside>
          <!--右边的整个内容区-->
          <section class="content-container">
            <div class="grid-content bg-purple-light">
              <el-col :span="24" class="breadcrumb-container" style="border-radius: 8px; border: #008888 solid 1px; padding: 8px 5px">
                <!--在内容显示区域的左上角显示选中的菜单的名字-->
                <strong class="title"> <i class="fa fa-hand-o-right"></i>  {{$route.name}}</strong>
                <!--面包屑在在内容显示区域的右上角显示当前页路径-->
                <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb-inner">
                    <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
                      <i class="fa fa-tag"></i> {{ item.name }}
                    </el-breadcrumb-item>
                </el-breadcrumb>
              </el-col>
              <!--内容展示区，将路由到的组件挂载到本区域显示-->
              <el-col :span="24" class="content-wrapper">
                <transition name="fade" mode="out-in">
                   <router-view></router-view>
                </transition>
              </el-col>
            </div>
          </section>
        </el-col>
      </el-row>
   </div>
</template>

<script>
  export default {
      name: "homepage",
      data() {
        return {
          sysName:'智能门锁管理平台',
          collapsed:false,
          sysUserName: '',
          introduceVisible:false
          }
        },

      methods: {
        //折叠导航栏
        collapse: function () {
          this.collapsed = !this.collapsed;
        },
        //退出登录
        logout: function () {
          let _this = this;
          this.$confirm('确认退出吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            sessionStorage.removeItem('username');
            _this.$router.push('/login');
          }).catch(() => {

          });
        },

        handleselect: function(a, b) {
        },

        handleOpen(key, keyPath) {
          console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
          console.log(key, keyPath);
        },

        showMenu(i,status){
          this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-'+i)[0].style.display=status?'block':'none';
        },
        introduce(){
          this.$router.push('/homepage/introduce');
        },
      },
    mounted() {
      let user = sessionStorage.getItem('user');
      if (user) {
        user = JSON.parse(user);
        this.sysUserName = user;
      }
    }
  }

</script>

<style scoped lang="scss">
  .container {
    position: absolute;
    top: 0px;
    bottom: 0px;
    left: 0;
    width: 100%;
  }
  .header {
    height: 60px;
    line-height: 60px;
    /*background: #1b6d85;*/
    background: #008888;

    color:#fff;}
  .logo {
    height:60px;
    font-size: 22px;
    padding-left:20px;
    padding-right:20px;
    border-color: grey;
    border-right: 1px solid;
  }
  .tools{
    padding: 0px 23px;
    width:14px;
    height: 60px;
    line-height: 60px;
    cursor: pointer;  /*引导性的光标，本句指代手指光标*/
  }
  .userinfo {
    text-align: right;
    height: 60px;
    padding-right: 30px;
    float: right;}
  .userinfo-inner {
    cursor: pointer;
    color:#fff;}
  .logo-width{
    width:230px;
  }
  .logo-collapse-width{
    width:60px
  }
  .main {
    display: flex;/*将对象作为弹性伸缩盒显示*/
    position: absolute;
    top: 60px;
    bottom: 0px;
    overflow: hidden;}/*当内容溢出元素框时，内容会被修剪，并且其余内容是不可见的。*/

  .menu-collapsed{
    flex:0 0 60px;
    width: 60px;
  }
  .menu-expanded{
    flex:0 0 230px;
    width: 230px;}
  aside{
    background: #f1f2f7;
    flex:0 0 230px;
    width: 230px;}
  .el-menu{
    height: 100%;
  }
  .el-menu-vertical-demo{
    width: 230px;
   }
  .collapsed{
    width:60px;}
  .item{
    position: relative;
  }
  .submenu{
    position:absolute;
    top:0px;
    left:60px;
    z-index:99999;
    height:auto;
    display:none;
  }
  .content-container {
    flex:1;
    overflow-y: scroll;
    padding: 10px;}
  .title {
    font-weight: bold;
    width: 200px;
    float: left;
    color: #475669;
  }
  .breadcrumb-inner {
    float: right;
    font-weight: bold;
  }
  .content-wrapper {
    background-color: #fff;
    box-sizing: border-box;
  }

</style>
