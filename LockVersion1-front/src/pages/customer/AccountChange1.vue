<template>
  <div class="bg shadow">
    <!-- <el-dialog title="编辑校区信息" v-model="editFormVisible" :close-on-click-modal="false">-->
    <!--model: 绑定表单中需要提交给后台的一个对象  label-width：表单域标签的宽度  rules：表单验证的规则  ref：表单绑定的对象-->
    <p style="text-align: center; font-size: x-large; padding: 40px 0 0 0">当前账号修改</p>
    <el-tooltip   placement="top-end" effect="light">
      <div slot="content">密码一旦修改将无法追回<br/>请您牢记修改之后的密码！</div>
      <i style="position: relative; left: 100px; top: -10px; color:#409EFF; cursor: pointer">温馨提示</i>
    </el-tooltip>
    <el-form :model="editForm" label-width="100px" status-icon :rules="editFormRules" ref="editForm">
      <el-form-item label="旧用户名" prop="oldname" class="formstyle">
        <el-input v-model="editForm.oldname" auto-complete="off" clearable>
          <template slot="prepend"><i class="fa fa-user"></i></template>
        </el-input>
      </el-form-item>
      <el-form-item label="旧密码" prop="oldpass" class="formstyle">
        <el-input :type="pass" v-model="editForm.oldpass" auto-complete="off" clearable>
          <template slot="prepend"><i class="fa fa-unlock-alt"></i></template>
          <template slot="append" ><i :class="see" style="cursor: pointer;" @click='show'></i></template>
        </el-input>
      </el-form-item>
      <el-form-item label="新用户名" prop="newname" class="formstyle">
        <el-input v-model="editForm.newname" auto-complete="off" placeholder="新用户名可与旧用户名相同" clearable>
          <template slot="prepend"><i class="fa fa-user"></i></template>
        </el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newpass" class="formstyle">
        <el-input :type="pass1" v-model="editForm.newpass" auto-complete="off" clearable>
          <template slot="prepend"><i class="fa fa-lock"></i></template>
          <template slot="append"><i :class="see1" style="cursor: pointer;" @click='show1'></i></template>
        </el-input>

      </el-form-item>
      <el-form-item label="确认密码" prop="checkpass" class="formstyle">
        <el-input :type="pass2" v-model="editForm.checkpass" auto-complete="off" clearable>
          <template slot="prepend"><i class="fa fa-lock"></i></template>
          <template slot="append"><i :class="see2" style="cursor: pointer;" @click='show2'></i></template>
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer" style="padding: 0 100px 40px 100px; float: right">
      <el-button @click="resetForm('editForm')" round>重置</el-button>
      <el-button type="primary" @click.native="editSubmit" :loading="editLoading" round>提交</el-button>
    </div>
    <!--   </el-dialog>-->
  </div>
</template>

<script>
    export default {
        name: "AccountChange",
      data(){

        /*validatePass validatePass2 validatePass3是表单的前端验证*/
        let validatePass = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入密码'));
          }
          else {
            if (value === this.editForm.oldpass) {
              callback(new Error('与旧密码重复'));
            }
            else if (this.editForm.checkpass !== '') {
              this.$refs.editForm.validateField('checkpass');
            }
            callback();
          }
        };

        let validatePass2 = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请再次输入密码'));
          } else if (value !== this.editForm.newpass) {
            callback(new Error('两次输入密码不一致!'));
          } else {
            callback();
          }
        };

        let validatePass3 = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入用户名'));
          } else if (value !== this.user) {
            callback(new Error('请输入当前登陆账号用户名'));
          } else {
            callback();
          }
        };
          return{
            user: '',
            editLoading: false,
            editForm: {
              oldname: '',
              oldpass: '',
              newname: '',
              newpass: '',
              checkpass: '',
            },
            pass: 'password',
            see: 'fa fa-eye-slash',
            pass1: 'password',
            see1: 'fa fa-eye-slash',
            pass2: 'password',
            see2: 'fa fa-eye-slash',
            editFormRules: {
              oldname: [
                {validator: validatePass3, trigger: 'change' },
                { required: true, message: '请输入用户名', trigger: 'change'  }
              ],
              oldpass: [
                { required: true, message: '请输入密码', trigger: 'change'  }
              ],
              newname: [
                { required: true, message: '请输入用户名', trigger: 'change'  }
              ],
              newpass: [
                { validator: validatePass, trigger: 'change' },
                { min: 6, max: 10, message: '长度在 6 到 10 个字符', trigger: 'change' }
              ],
              checkpass: [
                { validator: validatePass2, trigger: 'change' }
              ]
            },
          }
      },
      methods:{
        resetForm(formName){
          this.$refs[formName].resetFields();
        },
        show(){
          this.pass = this.pass ==='password'? 'text':'password';
          this.see = this.see ==='fa fa-eye-slash'? 'fa fa-eye':'fa fa-eye-slash';
        },
        show1(){
          this.pass1 = this.pass1 ==='password'? 'text':'password';
          this.see1 = this.see1 ==='fa fa-eye-slash'? 'fa fa-eye':'fa fa-eye-slash';
        },
        show2(){
          this.pass2 = this.pass2 ==='password'? 'text':'password';
          this.see2 = this.see2 ==='fa fa-eye-slash'? 'fa fa-eye':'fa fa-eye-slash';
        },



        //更改当前账号提交按钮
        editSubmit: function () {
          let _this = this;
          this.$refs.editForm.validate((valid) => {
            if (valid) {
              this.$confirm('确认提交吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: "success"
              }).then(() => {
                this.editLoading = true;
                let para = Object.assign({}, this.editForm);
                // para.date = (!para.date || para.date ==='') ? '' : util.formatDate.format(new Date(para.date), 'yyyy-MM-dd');
                console.log(para);
                this.$ajax({
                  method: 'post',
                  url: 'http://localhost:8082/manage/changeManager',
                  params: {oldname: para.oldname, oldpass: para.oldpass, newname: para.newname, newpass: para.newpass,},
                }).then((res) => {
                  console.log(res);
                  if (res.data === "旧密输入有误"){
                    _this.editLoading = false;
                    _this.$message({
                      message: '旧密码输入有误，请检查！',
                      type: 'warning',
                      center: true
                    });
                  }
                  if (res.data === "success"){
                    _this.editLoading = false;
                    _this.$message({
                      message: '密码修改成功,请重新登陆',
                      type: 'success',
                      center: true
                    });
                    _this.$refs["editForm"].resetFields(); //重置表单
                    _this.$router.push({path:'/login'});
                  }
                }).catch(function (error) {
                  console.log(error);
                })
              });
            }
          });
        },
      },
      mounted() {
        let user = sessionStorage.getItem('user');
        if (user) {
          user = JSON.parse(user);
          this.user = user;
        }
      }
    }
</script>

<style scoped>
  .formstyle{
    margin: 25px 0;
    padding: 0 80px 0 0;
  }
  .bg{
    margin: 0 260px 0 200px;
    padding: 0 30px 80px 30px;
    background: -webkit-linear-gradient(-135deg,transparent 2.5em ,#f1f2f7 0);
    position: relative;
    border-radius: .5em;
  }
  .bg::before{
    content: '';
    position: absolute;
    top: 0; right: 0;
    background: -webkit-linear-gradient(to left bottom,transparent 50%,rgba(0,0,0,.2) 0,rgba(0,0,0,.4)) 100% 0 no-repeat;
    width: 3.5em;
    height: 3.5em;
    transform: translateY(0em) rotate(0deg) ;
    transform-origin: bottom right;
    border-bottom-left-radius: inherit;
    box-shadow: -.2em .2em  .3em -.1em  rgba(0,0,0,.15);
  }
  .shadow{
    box-shadow: lightblue -8px 8px 20px 0px ;
  }

</style>
