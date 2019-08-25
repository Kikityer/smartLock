<template>
    <section>
      <!--信息表上部工具条-->
      <!--制做一个工具栏，类型为封装好的工具栏，底部的内边框为0 作用为居中-->
      <el-col :span="24" class="toolbar"  style="padding-bottom: 0;">
        <!--form表单，用于收集、校验和提交数据。设置 inline 属性可以让表单域变为行内的表单域。model为表单数据对象-->
        <el-form :inline="true" :model="filters"  @submit.native.prevent>
          <el-form-item>
            <el-autocomplete v-model="filters.name" :fetch-suggestions="querySearchAsync" @select="controllselect"  prefix-icon="el-icon-search" placeholder="请选择账号名查询" size="medium" @keyup.enter.native="search"></el-autocomplete>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" v-on:click="search" size="small" round>查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-edit-outline"  v-on:click="handleAdd" size="small" round>新增</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="success" icon="el-icon-refresh"  v-on:click="getUsers"  round>刷新</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <!--表格信息展示-->
      <!--data绑定要显示的信息。selection-change列表改变事件，勾选选择框时触发 -->
      <el-table :data="pageTable[currentPage-1]" :highlight-current-row="true" v-loading="listLoading" @selection-change="selsChange" style="width: 100%;" height="500" border :header-cell-style="{background:'#f1f2f7'}">
        <el-table-column type="selection" width="55" align="center"></el-table-column> <!--多选框列-->
        <el-table-column type="index" width="130" label="编号" align="center"></el-table-column> <!--索引编号列-->
        <el-table-column prop="username" label="账号" width="200" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="correlation" label="关联单位" width="230" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.correlation }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="condition" label="状态" width="200" align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.condition === '已启用'">
              <i class="el-icon-success"></i>
            </template>
            <template v-else-if="scope.row.condition === '未启用'">
            <i class="el-icon-error"></i>
          </template>
            <span>{{ scope.row.condition }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="identity" label="身份" width="200" align="center">
          <template slot-scope="scope">
            <i class="fa fa-id-badge"></i>
            <span>{{ scope.row.identity }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <!--slot-scope="scope"父组件上的作用域插槽，由子组件提供数据scope.$index, scope.row-->
          <template slot-scope="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)" round>修改</el-button>
            <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)" round>删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--信息表下部工具条-->
      <el-col :span="24" class="toolbar">
        <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0" size="medium" round>批量删除</el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 15, 20]"
          :page-size="pagesize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="float:right">
        </el-pagination>
      </el-col>

      <!--新增界面-->
      <el-dialog title="新增管理员信息" :visible.sync="addFormVisible"  width="500px" :show-close="false">
        <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
          <el-form-item label="账号" prop="name">
            <el-input v-model="addForm.name" auto-complete="off" clearable placeholder="建议取校名全称小写首字母"></el-input>
          </el-form-item>
          <el-form-item label="账号关联" prop="school" style="position: relative; top: 10px;">
            <el-input v-model="addForm.school" auto-complete="off" clearable placeholder="请填写本账号关联的学校"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="pass" style="position: relative; top: 20px;">
            <el-input :type="pass1" v-model="addForm.pass" auto-complete="off"  clearable>
              <template slot="append"><i :class="see1" style="cursor: pointer;" @click='show1'></i></template>
            </el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkpass" style="position: relative; top: 30px;">
            <el-input :type="pass2" v-model="addForm.checkpass" auto-complete="off"  clearable>
              <template slot="append"><i :class="see2" style="cursor: pointer;" @click='show2'></i></template>
            </el-input>
          </el-form-item>
          <el-form-item label="状态"  prop="condi" style="position: relative; top: 40px;">
            <el-select v-model="addForm.condi" clearable placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="身份" prop="identi" style="position: relative; top: 50px;">
            <el-select v-model="addForm.identi" clearable placeholder="请选择">
              <el-option
                v-for="item in options1"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-tooltip   placement="top-start" effect="light">
              <div slot="content">company代表公司端管理员身份<br/>customer代表客户端管理员身份</div>
              <i style="position: relative; left: 20px; color:#409EFF; cursor: pointer" >温馨提示</i>
            </el-tooltip>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer"  style="position: relative; top: 10px; padding-bottom: 10px">
          <el-button @click="resetForm('addForm')" @click.native="addFormVisible = false" size="small" round>取消</el-button>
          <el-button type="primary" @click.native="addSubmit" :loading="addLoading" size="small" round>提交</el-button>
        </div>
      </el-dialog>

      <!--编辑界面-->
      <!--编辑对话框，不可见，点击模态框对话框不消失-->
      <el-dialog title="修改账号信息" :visible.sync="editFormVisible"  width="500px" :show-close="false">
        <!--model: 表单数据对象  label-width：表单域标签的宽度  rules：表单验证的规则 -->
        <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
          <el-form-item label="账号名称" prop="username">
            <el-input v-model="editForm.username" auto-complete="off"  :disabled="true" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份" prop="identity">
            <el-input v-model="editForm.identity" auto-complete="off"  :disabled="true" clearable></el-input>
          </el-form-item>
          <el-form-item label="关联单位" prop="correlation" style="position: relative; top: 10px;">
            <el-input v-model="editForm.correlation" auto-complete="off" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="condition" style="position: relative; top: 20px;">
            <el-select v-model="editForm.condition"  placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" >
          <el-button @click="resetForm('editForm')" @click.native="editFormVisible = false" size="small" round>取消</el-button>
          <el-button type="primary" @click.native="editSubmit" :loading="editLoading" size="small" round>保存</el-button>
        </div>
      </el-dialog>
    </section>
</template>

<script>
    export default {
        name: "AccountManage",
      data(){
        let validatePass = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入密码'));
          }
          else {
            if (this.addForm.checkpass !== '') {
              this.$refs.addForm.validateField('checkpass');
            }
            callback();
          }
        };
        let validatePass2 = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请再次输入密码'));
          } else if (value !== this.addForm.pass) {
            callback(new Error('两次输入密码不一致!'));
          } else {
            callback();
          }
        };
          return{
            pass1: 'password',
            see1: 'fa fa-eye-slash',
            pass2: 'password',
            see2: 'fa fa-eye-slash',

            options: [{
              value: '已启用',
              label: '已启用'
            }, {
              value: '未启用',
              label: '未启用'
            },],
            options1: [{
              value: 'company',
              label: 'company'
            }, {
              value: 'customer',
              label: 'customer'
            },],


            filters: {
              name: ''
            },
            users: [], //后台传入的总数据
            pageTable: [],//要展示的总页数
            total: 0, //总的信息条数
            currentPage: 1,  //当前页数
            pagesize: 10,   //默认每页显示信息条数
            listLoading: false,
            sels: [],//列表选中列
            searchResult:[], //搜索之后结果集


            addFormVisible: false,//新增界面是否显示
            addLoading: false,
            //新增界面数据
            addForm: {
              name: '',
              school:'',
              pass: '',
              checkpass: '',
              condi: '',
              identi: '',
            },
            addFormRules: {
              name: [
                { required: true, message: '请输入新的管理员账号', trigger: 'change' }
              ],
              school: [
                { required: true, message: '请输入本账号的关联信息', trigger: 'change' }
              ],
              pass: [
                { validator: validatePass, trigger: 'change' },
                { min: 6, max: 10, message: '长度在 6 到 10 个字符', trigger: 'change' },
                { required: true, message: '请输入密码', trigger: 'change' }
              ],
              checkpass: [
                { validator: validatePass2, trigger: 'change' },
                { required: true, message: '请再次输入密码', trigger: 'change' }
              ],
              condi: [
                { required: true, message: '请输入新的管理员账号', trigger: 'change' }
              ],
              identi: [
                { required: true, message: '请输入新的管理员账号', trigger: 'change' }
              ]
            },


            editFormVisible: false,//编辑界面是否显示
            editLoading: false,
            //编辑界面数据
            editForm: {
              id: 0,
              username: '',
              correlation: '',
              condition: '',
              identity: '',
            },
            editFormRules: {
              correlation: [
                { required: true, message: '请输入校区名称', trigger: 'change' }
              ],
              condition: [
                { required: true, message: '请输入校区名称', trigger: 'change' }
              ]
            },
          }
      },
      /*初始化页面完成后之后开始获得校园用户信息*/
      mounted() {
        this.getUsers();
      },

      methods:{
        //在页面初始化完成之后获取校园用户信息
        getUsers() {
          let _this = this;
          _this.currentPage = 1;
          this.listLoading = true;//页面初始化时，true时会显示加载图标
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/manage/getManagers',
          }).then(function (res) {
            console.log(res);
            _this.users = res.data;
            _this.searchResult = res.data;
            _this.cutPage(_this.users);
            _this.listLoading = false;
          }).catch(function (error) {
            console.log(error);
          })
        },


        //用于搜索楼宇信息的后台数据上传
        querySearchAsync(queryString, callback) {
          let returnAccount= [];
          let timeout = '';
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/manage/searchSuggestion',
          }).then(function (res) {
            console.log(res.data);
            returnAccount = res.data;

            clearTimeout(timeout);
            timeout = setTimeout(() => {
              callback(returnAccount);
            }, 300 * Math.random());

          }).catch(function (error) {
            console.log(error);
          });

        },
        controllselect(item){
          console.log(item);
        },


        /*每页显示条数选择框绑定函数*/
        handleSizeChange: function (size) {
          let _this = this;
          _this.pagesize = size;
          _this.cutPage(_this.searchResult);  //在每次选择每页条数之后刷新一次请求列表参数得以渲染新的列表
        },
        /*选择当前页数绑定函数*/
        handleCurrentChange: function(currentPage){
          this.currentPage = currentPage;
        },

        /*分页*/
        cutPage(people){
          let pagedata = people;
          this.total = pagedata.length;  //拿到所有数据条数
          this.pageTable = this.sliceArray(pagedata, this.pagesize);  //分页函数
        },
        sliceArray(array, size) {
          let result = [];
          for (let x = 0; x < Math.ceil(array.length / size); x++) {
            let start = x * size;
            let end = start + size;
            result.push(array.slice(start, end));
          }
          return result;
        },


        /*新增管理员密码设置，可以点击后面图标实现密码隐藏显现切换功能*/
        show1(){
          this.pass1 = this.pass1 ==='password'? 'text':'password';
          this.see1 = this.see1 ==='fa fa-eye-slash'? 'fa fa-eye':'fa fa-eye-slash';
        },
        show2(){
          this.pass2 = this.pass2 ==='password'? 'text':'password';
          this.see2 = this.see2 ==='fa fa-eye-slash'? 'fa fa-eye':'fa fa-eye-slash';
        },
        resetForm(formName){
          this.$refs[formName].resetFields();
        },



        /*查询功能，前台数据查询,可以按照学校名称模糊查询*/
        search(){
          let _this = this;
          let arr = [];
          _this.currentPage = 1;
          _this.searchResult = [];
          if (_this.filters.name === "") {
            _this.$message({
              showClose: true,
              message: '您输入账号名为空 !',
              type: 'warning',
              center: true
            });
          }else {
            for (let i = 0; i <_this.users.length; i++) {
              if (_this.users[i].username.indexOf(_this.filters.name)!==-1) {   //indexOf实现输入数据的模糊查询
                arr.push(_this.users[i]) ;
                console.log(arr);
              }
            }
            _this.searchResult = arr;
            _this.cutPage(arr);
            _this.filters.name = '';
          }
        },

        /*新增及提交操作*/
        //显示新增界面
        handleAdd: function () {
          this.addFormVisible = true;
          this.addForm = {
            name: '',
            school:'',
            pass: '',
            checkpass: '',
            condi: '',
            identi: '',
          };
        },
        //新增提交操作
        addSubmit: function () {
          let _this = this;
          this.$refs.addForm.validate((valid) => {
            if (valid) {
              this.$confirm('确认提交吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: "success"
              }).then(() => {
                this.addLoading = true;
                let para = Object.assign({}, this.addForm);
                // para.date = (!para.date || para.date ==='') ? '' : util.formatDate.format(new Date(para.date), 'yyyy-MM-dd');
                console.log(para.condi);
                this.$ajax({
                  method: 'post',
                  url: 'http://localhost:8082/manage/subNewManagers',
                  params: {name: para.name, school: para.school, pass: para.pass, condi: para.condi, identi: para.identi },
                }).then((res) => {
                  console.log(res);
                  if(res.data === "success"){
                    _this.addLoading = false;
                    _this.$message({
                      message: '提交成功',
                      type: 'success',
                      center: true
                    });
                    _this.$refs["addForm"].resetFields(); //重置表单
                    _this.addFormVisible = false; //提交成功之后界面关闭
                    _this.getUsers();  //信息提交之后重新请求获得用户信息
                  }
                }).catch(function (error) {
                  console.log(error);
                })
              });
            }
          });
        },



        /*编辑及提交操作*/
        //显示编辑界面，并将已有数据进行显示
        handleEdit: function (index, row) {
          this.editFormVisible = true;
          console.log(index, row);
          this.editForm = Object.assign({}, row);
        },
        //编辑后提交操作
        editSubmit: function () {
          this.$refs.editForm.validate((valid) => {   //本句验证的是rules中的规则 如果合法则弹出确认框
            if (valid) {
              this.$confirm('确认提交吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: "success"
              }).then(() => {
                this.editLoading = true;
                let para = Object.assign({}, this.editForm);
                this.$ajax({
                  method: 'post',
                  url: 'http://localhost:8082/manage/managerOne',
                  params: {id: para.id, username: para.username, correlation: para.correlation, condition: para.condition},
                }).then((res) => {
                  this.editLoading = false;
                  this.$message({
                    message: '提交成功',
                    type: 'success'
                  });
                  this.$refs['editForm'].resetFields();
                  this.editFormVisible = false;
                  this.getUsers();  //信息更改之后重新请求获得用户信息
                });
              });
            }
          });
        },



        /*单行删除操作*/
        handleDel: function (index, row) {
          this.$confirm('确认删除该管理员信息么?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            let para = { id: row.id };
            console.log(row);
            console.log(para);
            this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/manage/deleteOne',
              params: {id: para.id},
            }).then((res) => {  //通过api.js里面定义的axios路径访问后台的数据，将选中的数据在数据库中删除
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success',
                center: true
              });
              this.getUsers();  //删除之后重新请求获得用户信息
            });
          }).catch(function (error) {
            console.log(error);
          })
        },
        //列表改变事件，勾选选择框时触发
        selsChange: function (sels) {
          this.sels = sels;
        },



        //批量删除操作
        batchRemove: function () {
          let ids = this.sels.map(item => item.id).toString();
          this.$confirm('确认删除选中记录吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            // let para = { ids: ids };
            console.log(ids);
            this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/manage/deleteMany',
              params: {ids},
            }).then((res) => {
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success',
                center: true
              });
              this.getUsers();  //批量删除之后重新请求用户信息
            });
          }).catch(function (error) {
            console.log(error);
          })
        },
      }

    }
</script>

<style scoped>

</style>
