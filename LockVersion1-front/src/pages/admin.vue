<template >
    <div class="bgcolor">
        <div class="textbg center-in-center shadow"  >
            <div  style="font-size: 2em; padding: 18px 0; text-align: center">
              管理员登陆
            </div>
            <div style=" position: relative; left: 23px; ">
              <el-form :model="userinfo" :rules="editFormRules" ref="userinfo">
                  <el-form-item prop="username" >
                    <img src="../assets/user-male-icon.png" style="width: 35px; height: 35px; position: absolute; top: 5px">
                    <el-input type="text" v-model="userinfo.username" style="width: 320px; position: relative;left: 55px" placeholder="请输入账号" @keyup.enter.native="submitInfo"></el-input>
                  </el-form-item>

                  <el-form-item prop="password">
                    <img src="../assets/password.png" style="width: 35px; height: 35px; position: relative; top: 15px">
                    <el-input :type="this.registration_data.psd" v-model="userinfo.password" style="width: 320px; position: relative;left: 15px;" placeholder="请输入密码" @keyup.enter.native="submitInfo"></el-input>
                  </el-form-item>

                  <el-checkbox  v-model="checked"  class="remember">一周内自动登陆</el-checkbox>
                  <el-tooltip class="item" effect="light"  placement="right-end">
                    <div slot="content">如果您忘记了登陆密码<br/>请联系后勤部门重新获取</div>
                    <span class="help" style="color:#409EFF;">帮助</span>
                  </el-tooltip>
                  <el-form-item style="position:relative; top: 35px; left: 50px; ">
                    <el-button type="primary" v-on:click="submitInfo" :loading="logining" style="width: 327px; height: 35px; font-size: 18px; line-height: 60%;">登 陆</el-button>
                  </el-form-item>
              </el-form>
            </div>
        </div>
      <p class="center-in-center1">*推荐使用Microsoft Edge浏览器或Chrome内核浏览器访问本站*</p>
    </div>
</template>

<script>
    export default {
        name: "admin",
      data(){
          return{
            userinfo:{
                username: '',//用户名
                password: '',//密码
            },
            registration_data:{
              psd:"password",
              // src:require("../assets/eye_closed.png")
            },
            checked: false,
            listLoading: false,
            logining: false,

            editFormRules: {
              username: [
                {required: false, trigger: 'blur'}
              ],
              password: [
                {required: false, trigger: 'blur'},
                { min: 6, max: 10, message: '长度在 6 到 10 个字符', trigger: 'blur' }
              ],
            }
          }
      },
      //页面初始化的时候从cookie中读取用户名和密码的信息
      mounted() {
        this.getCookie();
      },

      methods:{
        //点击眼睛按钮切换密码的可见性




        //点击登陆按钮提交校验信息
        submitInfo:function() {
                //保存的账号
                let name = this.userinfo.username;
                //保存的密码
                let pass = this.userinfo.password;
                if (name === '' || name == null) {
                  this.$message({
                    showClose: true,
                    message: '请正确输入用户名和密码 !',
                    center: true
                  });
                  return
                }
                else if (pass === '' || pass == null) {
                  this.$message({
                    showClose: true,
                    message: '请正确输入密码 !',
                    center: true
                  });
                      return
                }
                //当点击登陆按钮的时候，判断复选框是否被勾选，如果被勾选则将用户名和密码传入cookie中
                /*else {
                  // console.log("清空Cookie");
                  //清空Cookie
                  this.clearCookie();
                }*/
               let _this = this;
              //请求后台接口
              this.logining = true;
              this.$ajax({
                method: 'post',
                url: 'http://localhost:8082/manage/login',
                params: {username:name, password: pass},
              }).then(res => {
                // console.log(res);
                //登陆验证跳转之公司登陆账号，跳转到公司主页面
                if (res.data[0] === name && res.data[1]==="company") {
                  if (this.checked === true && document.cookie.length===0) {
                    //传入账号名，密码
                    this.setCookie(res.data[0], res.data[2], 7);
                  }
                  if(this.checked === false && document.cookie.length!==0){
                    this.checked = true;
                    this.setCookie(res.data[0], res.data[2], 7);
                  }
                  _this.$message({
                    showClose: false,
                    message: '登陆成功 !',
                    type: 'success',
                    center: true,
                    duration: 1100,
                  });
                  this.logining = false;
                  // console.log(_this);
                  let user = res.data[0];
                  // console.log(user);
                  sessionStorage.setItem('user',JSON.stringify(user));
                  setTimeout(() =>{         //登陆成功时，延迟1.25秒之后路由到公司端主页面。
                    _this.$router.push({path:'/homepage/tableInfo'});
                  },1250);
                }
                //登陆验证跳转之客户登陆账号，跳转到客户主页面
                else if(res.data[0] === name && res.data[1]==="customer"){
                  if (this.checked === true && document.cookie.length===0) {
                    //传入账号名，密码
                    this.setCookie(res.data[0], res.data[2], 7);
                  }
                  if(this.checked === false && document.cookie.length!==0){
                    this.checked = true;
                    this.setCookie(res.data[0], res.data[2], 7);
                  }
                  _this.$message({
                    showClose: false,
                    message: '登陆成功 !',
                    type: 'success',
                    center: true,
                    duration: 1100,
                  });
                  this.logining = false;
                  // console.log(_this);
                  let user = res.data[0];
                  // console.log(user);
                  sessionStorage.setItem('user',JSON.stringify(user));
                  setTimeout(() =>{         //登陆成功时，延迟1.25秒之后路由到客户端（学校）主页面。
                    _this.$router.push({path:'/homepage1/LockState'});
                  },1250);
                }
                else{
                  _this.$message({
                    showClose: false,
                    message: '用户名或密码错误,请重新输入 !',
                    type: 'error',
                    center: true
                  });
                  this.logining = false;
                  _this.userinfo.username = '';
                  _this.userinfo.password = '';
                  // _this.$router.push({path:'/login'});
              /*    setTimeout(() =>{         //当用户名或者密码输入错误的时候，延迟一秒执行刷新整个页面。
                    location.reload()
                  },1000);
*/
                }
              }).catch(function (error) {
                console.log(error);
              })
        },



        //设置cookie
        setCookie(c_name,c_pwd,exdays){
          let exdate=new Date();//获取时间
          exdate.setTime(exdate.getTime() + 24*60*60*1000*exdays);//保存的天数
          //字符串拼接cookie
          window.document.cookie="userName"+ "=" +c_name+'bjnnm'+";path=/;expires="+exdate.toGMTString();  //bjnnm拼接在用户名上在cookie中用于简单加密
          window.document.cookie="userPwd"+"="+c_pwd+'562ad'+";path=/;expires="+exdate.toGMTString(); //562ad拼接在密码上在cookie中用于简单加密
        },

        //在页面初始化的时候读取cookie中的信息
        getCookie:function () {
          if (document.cookie.length>0) {
            let arr=document.cookie.split('; ');//这里显示的格式需要切割一下自己可输出看下
            // console.log(arr);
            for(let i=0;i<arr.length;i++){
              let arr2=arr[i].split('=');//再次切割
              // console.log(arr2);
              //判断查找相对应的值
              if(arr2[0]==='userName'){
                let uname  = arr2[1].substring(0,arr2[1].length-5); //去除拼接在用户名上的简单加密字符串
                // console.log(uname);
                this.userinfo.username = uname;//保存到保存数据的地方
              }else if(arr2[0]==='userPwd'){
                let psd  = arr2[1].substring(0,arr2[1].length-5);  //去除拼接在密码上的简单加密字符串
                // console.log(psd);
                this.userinfo.password = psd;
              }
            }
          }
        },
        //清除cookie
        clearCookie: function() {
          this.setCookie("", "", -1); //修改用户名和密码的值都为空，天数为-1天
        }

      }
    }
</script>

<style scoped>

.bgcolor {
  /*background: #1b6d85;*/
  background: url("../assets/bgpic3.png") no-repeat ;
  background-size: cover;
  position: fixed;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
}
.textbg{
  width: 463px;
  height: 330px;
  border-radius: 18px;
  background-color: white;
  border: black;
}


/*登陆面板在屏幕中间位置的样式*/
.center-in-center{
  position: absolute;
  top: 42%;
  left: 50%;
  -webkit-transform:translate(-50%, -50%) ;
  -moz-transform:translate(-50%, -50%) ;
  -ms-transform:translate(-50%, -50%) ;
  transform:translate(-50%, -50%) ;
}
.center-in-center1{
  color: white;
  position: absolute;
  top: 67%;
  left: 50%;
  -webkit-transform:translate(-50%, -50%) ;
  -moz-transform:translate(-50%, -50%) ;
  -ms-transform:translate(-50%, -50%) ;
  transform:translate(-50%, -50%) ;
}
.shadow{
    box-shadow: lightblue 0 0 20px 8px ;
  }
.remember{
  position: relative;
  top: 15px;
  left: 54px;
}
.help{
  position: relative;
  top: 15px;
  left: 220px;
  cursor: pointer;
}

</style>
