<template>
  <section>
    <!--信息表上部工具条-->
    <!--制做一个工具栏，类型为封装好的工具栏，底部的内边框为0 作用为居中-->
    <el-col :span="24" class="toolbar"  style="padding-bottom: 0;">
      <!--form表单，用于收集、校验和提交数据。设置 inline 属性可以让表单域变为行内的表单域。model为表单数据对象-->
      <el-form :inline="true" :model="filters" @submit.native.prevent>
        <el-form-item>
          <el-autocomplete v-model="filters.name"  prefix-icon="el-icon-search" size="medium" :fetch-suggestions="querySearchAsync" @select="controllselect" placeholder="请选择楼宇名称查询" @keyup.enter.native="search">
          </el-autocomplete>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="search" size="small" round>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-edit-outline"  v-on:click="handleAdd" size="small" round>新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="success" icon="el-icon-refresh"  v-on:click="getBuildingInfo"  round>刷新</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--表格信息展示-->
    <!--data绑定要显示的信息。selection-change列表改变事件，勾选选择框时触发 -->
    <el-table :data="pageTable[currentPage-1]" :highlight-current-row="true" v-loading="listLoading" @selection-change="selsChange"  border  height="500" style="width: 100%;" :header-cell-style="{background:'#f1f2f7'}" >
      <el-table-column type="selection" min-width="55" align="center" ></el-table-column> <!--多选框列-->
      <el-table-column type="index" width="100" label="编号" align="center"></el-table-column> <!--索引编号列-->
      <el-table-column prop="buildingName" label="楼宇名称" min-width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.buildingName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="peopleLive" label="住户人群" min-width="200" align="center">
        <template slot-scope="scope">
          <!--<template v-if="scope.row.peopleLive === '男'">
            <i class="fa fa-mars"></i>
          </template>
          <template v-else-if="scope.row.peopleLive === '女'">
            <i class="fa fa-venus"></i>
          </template>
          <template v-else-if="scope.row.peopleLive === '男&女' || scope.row.peopleLive === '女&男'">
            <i class="fa fa-venus-mars"></i>
          </template>-->
          <span>{{ scope.row.peopleLive }}</span>
        </template>
      </el-table-column>n-
      <el-table-column prop="managerName" label="宿管姓名" min-width="200" align="center">
        <template slot-scope="scope">
          <!--<i class="fa fa-vcard (alias)"></i>-->
          <span>{{ scope.row.managerName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="managerPhone" label="宿管电话" min-width="200" align="center">
        <template slot-scope="scope">
          <i class="el-icon-phone-outline"></i>
          <span>{{ scope.row.managerPhone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
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
        :page-sizes="[10, 15, 20, 25]"
        :page-size="pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="float:right">
      </el-pagination>
    </el-col>


    <!--修改界面-->
    <!--修改界面对话框，不可见，点击模态框对话框不消失-->
    <el-dialog title="修改楼宇信息" :visible.sync="editFormVisible"  width="500px" :show-close="false">
      <!--model: 表单数据对象  label-width：表单域标签的宽度  rules：表单验证的规则 -->
      <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm" >
        <el-form-item label="楼宇名称" prop="buildingName">
          <el-input v-model="editForm.buildingName" auto-complete="off" clearable ></el-input>
        </el-form-item>
        <el-form-item label="住户人群" prop="peopleLive"  style="position: relative; top: 10px;">
          <!--<el-input v-model="editForm.peopleLive" auto-complete="off" clearable ></el-input>-->
          <el-select v-model="editForm.peopleLive" >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="宿管姓名" prop="managerName" style="position: relative; top: 20px;">
          <el-input v-model="editForm.managerName" auto-complete="off" clearable></el-input>
        </el-form-item>
        <el-form-item label="宿管电话" prop="managerPhone" style="position: relative; top: 30px;">
          <el-input v-model="editForm.managerPhone" auto-complete="off" clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" >
        <el-button @click="resetForm('editForm')" @click.native="editFormVisible = false" size="small" round>取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading" size="small" round>保存</el-button>
      </div>
    </el-dialog>

    <!--新增界面-->
    <el-dialog title="新增楼宇信息" :visible.sync="addFormVisible"  width="500px" :show-close="false">
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
        <el-form-item label="楼宇名称" prop="buildingName">
          <el-input v-model="addForm.buildingName" auto-complete="off"   clearable></el-input>
        </el-form-item>
        <el-form-item label="住户人群"  style="position: relative; top: 10px;">
          <el-select v-model="addForm.peopleLive" clearable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="宿管姓名" prop="managerName" style="position: relative; top: 20px;">
          <el-input v-model="addForm.managerName" auto-complete="off"  clearable></el-input>
        </el-form-item>
        <el-form-item label="宿管电话" prop="managerPhone" style="position: relative; top: 30px;">
          <el-input v-model="addForm.managerPhone" auto-complete="off"  clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('addForm')" @click.native="addFormVisible = false" size="small" round>取消</el-button>
        <el-button type="primary" @click.native="addSubmit" :loading="addLoading" size="small" round>提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
    export default {
        name: "buildingInfo",
      data(){
          return{
            options:[{
              value: '男&女',
              label: '男&女'
            }, {
              value: '男',
              label: '男'
            }, {
              value: '女',
              label: '女'
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


            editFormVisible: false,//编辑界面是否显示
            editLoading: false,
            //编辑界面数据
            editForm: {
              id: 0,
              buildingName: '',
              peopleLive: '',
              managerName: '',
              managerPhone: ''
            },
            editFormRules: {
              buildingName: [
                { required: true, message: '请输入楼宇名称', trigger: 'change' }
              ],
              peopleLive: [
                { required: true, message: '请输入入住人群', trigger: 'blur' }
              ],
              managerName: [
                { required: true, message: '请输入管理员姓名', trigger: 'blur' }
              ],
              managerPhone: [
                { required: true, message: '请输入管理员电话', trigger: 'blur' }
              ]
            },


            addFormVisible: false,//新增界面是否显示
            addLoading: false,
            //新增界面数据
            addForm: {
              buildingName: '',
              peopleLive: '',
              managerName: '',
              managerPhone: ''
            },
            addFormRules: {
              buildingName: [
                { required: true, message: '请输入楼宇名称', trigger: 'change' }
              ],
              managerName: [
                { required: true, message: '请输入宿管姓名', trigger: 'change' }
              ],
              managerPhone: [
                { required: true, message: '请输入宿管电话', trigger: 'change' }
              ]
            },
          }
      },
      /*created(){
          let bu = this;
          document.onkeydown = function (e) {
            let key = window.event.keyCode;
            if(key === 13){
              bu.search();
            }
          }
      },*/
      /*初始化页面完成后之后开始获得校园用户信息*/
      mounted() {
        this.getBuildingInfo();
      },
      methods:{

        //用于搜索楼宇信息的后台数据上传
        querySearchAsync(queryString, callback) {
          let returnbuilding = [];
          let timeout = '';
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/studentInfo/returnBuildingInfo',
          }).then(function (res) {
            console.log(res.data);
            returnbuilding = res.data;

            clearTimeout(timeout);
            timeout = setTimeout(() => {
              callback(returnbuilding);
            }, 300 * Math.random());

          }).catch(function (error) {
            console.log(error);
          });

        },
        controllselect(item){
          console.log(item);
        },

        resetForm(formName){
          this.$refs[formName].resetFields();
        },

        //在页面初始化完成之后获取校园用户信息
        getBuildingInfo() {
          let _this = this;
          _this.currentPage = 1;
          this.listLoading = true;//页面初始化时，true时会显示加载图标
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/buildingInfo/getBuildingInfo',
          }).then(function (res) {
            console.log(res.data);
            _this.users = res.data;
            _this.searchResult = res.data;
            _this.cutPage(_this.users);
            // _this.pageUsers  = _this.users.slice((_this.currentPage-1)*_this.pagesize,_this.currentPage*_this.pagesize); //将后台传入的总数据进行切割
            _this.listLoading = false;
          }).catch(function (error) {
            console.log(error);
          })
        },


        /*每页显示条数选择框绑定函数*/
        handleSizeChange: function (size) {
          let _this = this;
          _this.pagesize = size;
          this.cutPage(_this.searchResult);  //在每次选择每页条数之后刷新一次请求列表参数得以渲染新的列表
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



        /*查询功能，前台数据查询,可以按照学校名称模糊查询*/
        search(){
          let arr = [];
          let _this = this;
          _this.currentPage = 1;
          if (_this.filters.name === "") {
            _this.$message({
              showClose: true,
              message: '您输入的楼宇名称为空 !',
              type: 'warning',
              center: true
            });
          }else {
            _this.searchResult = [];
            for (let i = 0; i <_this.users.length; i++) {
              if (_this.users[i].buildingName.indexOf(_this.filters.name)!==-1) {   //indexOf实现输入数据的模糊查询
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
            buildingName: '',
            peopleLive: '',
            managerName: '',
            managerPhone: '',
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
                console.log(para);
                this.$ajax({
                  method: 'get',
                  url: 'http://localhost:8082/buildingInfo/createBuildingInfo',
                  params: {buildingName: para.buildingName, peopleLive: para.peopleLive, managerName: para.managerName, managerPhone: para.managerPhone},
                }).then((res) => {
                  console.log(res);
                  if(res.data === "success"){
                    _this.addLoading = false;
                    _this.$message({
                      message: '提交成功',
                      type: 'success'
                    });
                    _this.$refs["addForm"].resetFields(); //重置表单
                    _this.addFormVisible = false; //提交成功之后界面关闭
                    _this.getBuildingInfo();  //信息提交之后重新请求获得用户信息
                  }
                }).catch(function (error) {
                  console.log(error);
                })
              });
            }
          });
        },

        /*修改及提交操作*/
        //显示修改界面，并将已有数据进行显示
        handleEdit: function (index, row) {
          this.editFormVisible = true;
          console.log(index, row);
          this.editForm = Object.assign({}, row);
        },
        //修改后提交操作
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
                  method: 'get',
                  url: 'http://localhost:8082/buildingInfo/submitBuildingInfo',
                  params: {id: para.id,buildingName: para.buildingName, peopleLive: para.peopleLive, managerName: para.managerName, managerPhone: para.managerPhone},
                }).then((res) => {
                  this.editLoading = false;
                  this.$message({
                    message: '提交成功',
                    type: 'success'
                  });
                  this.$refs['editForm'].resetFields();
                  this.editFormVisible = false;
                  this.getBuildingInfo();  //信息更改之后重新请求获得用户信息
                });
              });
            }
          });
        },

        /*单行删除操作*/
        handleDel: function (index, row) {
          this.$confirm('确认删除该条楼宇信息吗?', '提示', {
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
              url: 'http://localhost:8082/buildingInfo/deleteOne',
              params: {id: para.id},
            }).then((res) => {  //通过api.js里面定义的axios路径访问后台的数据，将选中的数据在数据库中删除
              this.listLoading = false;
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
              this.getBuildingInfo();  //删除之后重新请求获得用户信息
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
          this.$confirm('确认删除所有选中的楼宇信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            // let para = { ids: ids };
            console.log(ids);
            this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/buildingInfo/deleteMany',
              params: {ids},
            }).then((res) => {
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getBuildingInfo();  //批量删除之后重新请求用户信息
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
