<template>
    <section>
      <!--信息表上部工具条-->
      <!--制做一个工具栏，类型为封装好的工具栏，底部的内边框为0 作用为居中-->
      <el-col :span="24" class="toolbar"  style="padding-bottom: 0;">
        <!--form表单，用于收集、校验和提交数据。设置 inline 属性可以让表单域变为行内的表单域。model为表单数据对象-->
        <el-form :inline="true" :model="filters"  @submit.native.prevent>
          <el-form-item>
            <el-autocomplete  v-model="filters.buildingname" :fetch-suggestions="querySearchAsync" @select="controllselect"  placeholder="请选择楼宇" size="small" @keyup.enter.native="search1" style="width: 145px">
              <el-button  slot="append" icon="el-icon-search"  size="mini" v-on:click="search1"></el-button>
            </el-autocomplete>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.dormnumber"   placeholder="寝室号查询" size="small" @keyup.enter.native="search2" style="width: 145px">
              <el-button  slot="append" icon="el-icon-search" size="mini" v-on:click="search2"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.lockid"   placeholder="锁ID查询" size="small" @keyup.enter.native="search3"  style="width: 140px">
              <el-button  slot="append" icon="el-icon-search" size="mini" v-on:click="search3"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.studentname"   placeholder="学生姓名查询" size="small" @keyup.enter.native="search4"  style="width: 155px">
              <el-button  slot="append" icon="el-icon-search" size="mini" v-on:click="search4"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.studentnumber"   placeholder="学号查询" size="small" @keyup.enter.native="search5"  style="width: 130px">
              <el-button  slot="append" icon="el-icon-search" size="mini" v-on:click="search5"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
              <el-button size="mini" type="primary" icon="el-icon-edit-outline"  v-on:click="handleAdd"  round>新增</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="mini" type="success" icon="el-icon-refresh"  v-on:click="reagain"  round>刷新</el-button>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item" effect="light" content="导出当前页信息" placement="top-end">
              <el-button size="mini" type="success" icon="el-icon-download"  v-on:click="exportToExcel1"  round>导出</el-button>
            </el-tooltip>
          </el-form-item>
          <el-form-item>
            <!--<el-dropdown placement="bottom-start"  @command="importViemShow" trigger="click">
              <el-button size="mini" type="success" icon="el-icon-upload2"  round >导入</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item  command="a">导入所有学生信息</el-dropdown-item>
                <el-dropdown-item  command="b">导入毕业生名单</el-dropdown-item>
                <el-dropdown-item  command="c">导入新生名单</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>-->
            <el-tooltip class="item" effect="light" content="上传所有学生信息" placement="top-start">
             <el-button size="mini" type="success" icon="el-icon-upload"  round  v-on:click="importViemShow" >导入</el-button>
            </el-tooltip>
          </el-form-item>
        </el-form>
      </el-col>

      <!--表格信息展示-->
      <!--data绑定要显示的信息。selection-change列表改变事件，勾选选择框时触发 -->
      <el-table :data="pageTable[currentPage-1]" :highlight-current-row="true" v-loading="listLoading" @selection-change="selsChange" border  height="500" style="width: 100%;" :header-cell-style="{background:'#f1f2f7'}" >
        <el-table-column type="selection" min-width="55" align="center" ></el-table-column > <!--多选框列-->
        <el-table-column type="index" width="60" label="编号" align="center"></el-table-column> <!--索引编号列-->
        <el-table-column prop="buildingName" label="楼宇名称" min-width="110"  align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.buildingName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dormNumber" label="寝室号" min-width="90"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-date"></i>-->
            <span>{{ scope.row.dormNumber }}</span>
          </template>
        </el-table-column>n-
        <el-table-column prop="lockId" label="锁ID" min-width="90"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.lockId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userNumber" label="学生编号" min-width="50"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.userNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentName" label="学生姓名" min-width="110"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.studentName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentNumber" label="学号" min-width="120" sortable align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.studentNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentCardId" label="学生卡ID" min-width="130"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.studentCardId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="identificationCardId" label="身份证ID" min-width="160"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.identificationCardId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="180" align="center">
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
          :page-sizes="[10,20, 50, 100]"
          :page-size="pagesize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="float:right">
        </el-pagination>
      </el-col>

      <!--编辑界面-->
      <!--编辑对话框，不可见，点击模态框对话框不消失-->
      <el-dialog title="修改学生信息" :visible.sync="editFormVisible"  width="500px" :show-close="false">
        <!--model: 表单数据对象  label-width：表单域标签的宽度  rules：表单验证的规则 -->
        <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
          <el-form-item label="楼宇名称" prop="buildingName">
            <!--<el-input v-model="editForm.buildingName" auto-complete="off" clearable></el-input>-->
            <el-autocomplete v-model="editForm.buildingName" :fetch-suggestions="querySearchAsync" @select="controllselect" auto-complete="off" placeholder="请选择楼宇" :disabled="true" ></el-autocomplete>
          </el-form-item>
          <el-form-item label="寝室号"  prop="dormNumber" style="position: relative; top: 6px;">
            <el-input v-model="editForm.dormNumber" auto-complete="off" clearable :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="锁ID"  prop="lockId" style="position: relative; top: 12px;">
            <el-input v-model="editForm.lockId" auto-complete="off" clearable :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="学生编号"  prop="userNumber" style="position: relative; top: 18px;">
            <el-input v-model="editForm.userNumber" auto-complete="off" clearable :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="学生姓名"  prop="studentName" style="position: relative; top: 24px;">
            <el-input v-model="editForm.studentName" auto-complete="off" clearable :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="学号"  prop="studentNumber" style="position: relative; top: 30px;">
            <el-input v-model="editForm.studentNumber" auto-complete="off" clearable :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="学生卡ID"  prop="studentCardId" style="position: relative; top: 36px;">
            <el-input v-model="editForm.studentCardId" auto-complete="off" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证ID"  prop="identificationCardId" style="position: relative; top: 42px;">
            <el-input v-model="editForm.identificationCardId" auto-complete="off" clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="position: relative; top: 5px;">
          <el-button @click="resetForm('editForm')" @click.native="editFormVisible = false" size="medium" round>取消</el-button>
          <el-button type="primary" @click.native="editSubmit" :loading="editLoading" size="medium" round>保存</el-button>
        </div>
      </el-dialog>

      <!--新增界面-->
      <el-dialog title="新增学生信息" :visible.sync="addFormVisible" width="500px" :show-close="false">
        <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
          <el-form-item label="楼宇名称" prop="buildingName">
            <el-autocomplete v-model="addForm.buildingName" :fetch-suggestions="querySearchAsync" @select="controllselect" auto-complete="off" placeholder="请选择楼宇" ></el-autocomplete>
          </el-form-item>
          <el-form-item label="寝室号" prop="dormNumber" style="position: relative; top: 6px;">
            <el-input v-model="addForm.dormNumber" auto-complete="off"  clearable></el-input>
          </el-form-item>
          <el-form-item label="锁ID" prop="lockId" style="position: relative; top: 12px;">
            <el-input v-model="addForm.lockId" auto-complete="off"  clearable></el-input>
          </el-form-item>
          <el-form-item label="学生编号" prop="userNumber" style="position: relative; top: 18px;">
            <el-input v-model="addForm.userNumber" auto-complete="off" placeholder="格式为：01、02"  clearable></el-input>
          </el-form-item>
          <el-form-item label="学生姓名" prop="studentName" style="position: relative; top: 24px;">
            <el-input v-model="addForm.studentName" auto-complete="off"  clearable></el-input>
          </el-form-item>
          <el-form-item label="学名" prop="studentNumber" style="position: relative; top: 30px;">
            <el-input v-model="addForm.studentNumber" auto-complete="off"  clearable></el-input>
          </el-form-item>
          <el-form-item label="学生卡ID"  prop="studentCardId" style="position: relative; top: 36px;">
            <el-input v-model="addForm.studentCardId" auto-complete="off" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证ID"  prop="identificationCardId" style="position: relative; top: 42px;">
            <el-input v-model="addForm.identificationCardId" auto-complete="off" clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="position: relative; top: 5px;">
          <el-button @click="resetForm('addForm')"  @click.native="addFormVisible = false" size="medium" round>取消</el-button>
          <el-button type="primary" @click.native="addSubmit" :loading="addLoading" size="medium" round>提交</el-button>
        </div>
      </el-dialog>

      <!--excel表格导入到后台数据库模板-->
      <el-dialog title="导入学生信息表" :visible.sync="excelImportShow" :close-on-click-modal="false" width="410px" :show-close="false">
        <el-tooltip class="item" effect="light" placement="right-end">
          <div slot="content">上传的Excel数据格式<br/>请严格按照此模板填写</div>
         <el-button size="small" type="success" round  style="position: absolute; left: 150px; top: 84px" v-on:click="exportToExcel2" icon="el-icon-download">下载模板</el-button>
        </el-tooltip>
        <el-upload
          class="upload-demo"
          ref="upload"
          :limit="1"
          :before-upload="beforeAvatarUpload"
          action="http://localhost:8082/studentInfo/getExcel"
          :on-success="onSuccess"
          :on-progress="onProgress"
          :before-remove="beforeRemove"
          :on-remove="handleRemove"
          :file-list="fileList"
          :on-exceed="onexceed"
          :auto-upload="false">
        <el-button size="small" type="primary" round  icon="el-icon-document">选取文件</el-button>
        <div slot="tip" class="el-upload__tip" >只能上传excel文件</div>
        <el-progress :text-inside="true" :stroke-width="14" v-if="fileFlag === true" type="line" status="success" :percentage="fileUploadPercent" style="width: 150px"></el-progress>
        </el-upload>
        <!--<el-button size="small" type="success" round  style="position: absolute; left: 150px; top: 84px" v-on:click="exportToExcel2" icon="el-icon-download">下载模板</el-button>-->
        <div slot="footer" class="dialog-footer">
            <el-button @click.native="excelImportShow = false" round size="medium">取消</el-button>
            <el-button type="primary" @click.native="submitUpload()" :loading="addLoading" round size="medium">上传</el-button>
        </div>
      </el-dialog>
    </section>
</template>

<script>
  export default {
        name: "studentsInfo",
      data(){
          return{
            filters: {
              buildingname: '',
              dormnumber: '',
              lockid: '',
              studentname: '',
              studentnumber: ''
            },
            brr: [],//输入楼宇信息查询之后的结果放入其中
            users: [], //后台传入的总数据
            pageTable: [],//要展示的总页数
            total: 0, //总的信息条数
            currentPage: 1,  //当前页数
            pagesize: 20,   //默认每页显示信息条数
            listLoading: false,
            sels: [],//列表选中列
            graduate:[],
            fileList:[],
            fileFlag: false,
            fileUploadPercent: 0,
            searchResult:[], //搜索之后结果集



            editFormVisible: false,//编辑界面是否显示
            editLoading: false,
            //编辑界面数据
            editForm: {
              id: 0,
              buildingName: '',
              dormNumber: '',
              lockId: '',
              userNumber:'',
              studentName: '',
              studentNumber: '',
              studentCardId:'',
              identificationCardId:'',
            },
            editFormRules: {
              studentCardId: [
                { required: true, message: '请输入校园卡ID', trigger: 'change' }
              ],
              identificationCardId: [
                { required: true, message: '请输入身份证ID', trigger: 'change' }
              ],

            },

            addFormVisible: false,//新增界面是否显示
            addLoading: false,
            //新增界面数据
            addForm: {
              buildingName: '',
              dormNumber: '',
              lockId: '',
              userNumber:'',
              studentName: '',
              studentNumber: '',
              studentCardId:'',
              identificationCardId:'',
            },
            addFormRules: {
              dormNumber: [
                { required: true, message: '请输入寝室号', trigger: 'change' }
              ],
              lockId: [
                { required: true, message: '请输入锁ID', trigger: 'change' }
              ],
              userNumber: [
                { required: true, message: '请输入学生编号', trigger: 'change' }
              ],
              studentName: [
                { required: true, message: '请输入学生姓名', trigger: 'change' }
              ],
              studentNumber: [
                { required: true, message: '请输入学号', trigger: 'change' }
              ],
              studentCardId: [
                { required: true, message: '请输入校园卡ID', trigger: 'change' }
              ],
              identificationCardId: [
                { required: true, message: '请输入身份证ID', trigger: 'change' }
              ],
            },

            excelImportShow: false,

          }
      },
      /*初始化页面完成后之后开始获得校园用户信息*/
      mounted() {
        this.getStudentInfo();
      },
      methods:{
        /*实现刷新功能*/
        reagain(){
          this.getStudentInfo();
          this.brr = [];
          console.log(this.searchResult)
        },

        resetForm(formName){
          this.$refs[formName].resetFields();
        },

        /*实现将当前页的表格信息导出Excel表*/
        exportToExcel1(){
          require.ensure([], () => {
            const { export_json_to_excel } = require('../../excel/Export2Excel');
            const tHeader = [ '楼宇名称','寝室号', '锁ID', '学生编号', '学生姓名','学号','学生卡ID','身份证ID'];
            //对应表格输出的title
            const filterVal = ['buildingName', 'dormNumber','lockId', 'userNumber', 'studentName', 'studentNumber','studentCardId', 'identificationCardId'];
            // 对应表格输出的数据
            const list = this.pageTable[this.currentPage-1];
            const data = this.formatJson(filterVal, list);
            export_json_to_excel(tHeader, data, '学生信息Excel表'); //对应下载文件的名字
          })
        },
        formatJson(filterVal, jsonData) {
          return jsonData.map(v => filterVal.map(j => v[j]))
        },

        //导出学生信息模板表
        exportToExcel2(){
          require.ensure([], () => {
            const { export_json_to_excel } = require('../../excel/Export2Excel');
            const tHeader = [ '楼宇名称','寝室号', '锁ID', '学生编号','学生姓名', '学号','学生卡ID','身份证ID'];
            const data = ['', '','', '', ''];
            // 对应表格输出的数据
            export_json_to_excel(tHeader,data, '学生信息模板表'); //对应下载文件的名字
          })
        },



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



        //在页面初始化完成之后获取校园用户信息
        getStudentInfo() {
          let _this = this;
          _this.currentPage = 1;
          this.listLoading = true;//页面初始化时，true时会显示加载图标
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/studentInfo/getAllInfo',
          }).then(function (res) {
            _this.users = res.data;
            _this.searchResult = res.data;
            _this.cutPage(_this.users);
            _this.listLoading = false;
          }).catch(function (error) {
            console.log(error);
          })
        },



        /*改变每页的显示条数*/
        handleSizeChange: function (size) {
          let _this = this;
          _this.pagesize = size;
          _this.cutPage(_this.searchResult);  //在每次选择每页条数之后刷新一次请求列表参数得以渲染新的列表
        },
        /*选择浏览第几页*/
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




        search1(){    //楼宇信息查询
          let _this = this;
          _this.currentPage = 1;
          if (_this.filters.buildingname === "") {
            _this.$message({
              showClose: true,
              message: '您输入的楼宇名称为空 !',
              type: 'warning',
              center: true
            });
          }else {
            if (!_this.brr.length) {  //brr是全局变量  当它为空时
              _this.searchResult = [];
              for (let i = 0; i <_this.users.length; i++) {
                if (_this.users[i].buildingName.indexOf(_this.filters.buildingname)!==-1){   //indexOf实现输入数据的模糊查询
                  _this.brr.push(_this.users[i])
                }
              }
              _this.searchResult = _this.brr;
              _this.cutPage(_this.brr);
              _this.filters.buildingname = '';
            }else {                 //brr是全局变量  当它不为空时
              _this.brr = [];
              _this.searchResult = [];
              for (let i = 0; i < _this.users.length; i++) {
                if (_this.users[i].buildingName.indexOf(_this.filters.buildingname) !== -1) {   //indexOf实现输入数据的模糊查询
                  _this.brr.push(_this.users[i])
                }
              }
              _this.searchResult = _this.brr;
              _this.cutPage(_this.brr);
              _this.filters.buildingname = '';
               /* if (this.brr[0].buildingName.indexOf(this.filters.buildingname) === -1) {
                  this.brr = [];
                  for (let i = 0; i < this.users.length; i++) {
                    if (this.users[i].buildingName.indexOf(this.filters.buildingname) !== -1) {   //indexOf实现输入数据的模糊查询
                      this.brr.push(this.users[i])
                    }
                  }
                  let finddata = this.brr;
                  this.pageTable = this.sliceArray(finddata, this.pagesize);
                  this.filters.buildingname = '';
                }else {
                  console.log('ok');
                }*/
              }
          }
        },
        search2(){   //按照寝室号进行模糊查询
          let _this = this;
          _this.currentPage = 1;
          let arr = [];
          if (_this.filters.dormnumber === "") {
            _this.$message({
              showClose: true,
              message: '您输入的寝室号为空 !',
              type: 'warning',
              center: true
            });
          }else {
            if (!_this.brr.length) {   //只输入寝室号查询
              _this.searchResult = [];
              console.log(_this.brr);
              for (let i = 0; i <_this.users.length; i++) {
                if (_this.users[i].dormNumber.indexOf(_this.filters.dormnumber)!==-1) {   //indexOf实现输入数据的模糊查询
                  arr.push(_this.users[i]) ;
                  console.log(arr);
                }
              }
              _this.searchResult = arr;
              _this.cutPage(arr);
              _this.filters.dormnumber = '';
            }
            else {                 //在楼宇查询的结果中再进行寝室号查询
              console.log(_this.brr);
              _this.searchResult = [];
              for (let i = 0; i <_this.brr.length; i++) {
                if (_this.brr[i].dormNumber.indexOf(_this.filters.dormnumber)!==-1) {   //indexOf实现输入数据的模糊查询
                  arr.push(_this.brr[i]) ;
                  console.log(arr);
                }
              }
              _this.searchResult = arr;
              _this.cutPage(arr);
              _this.filters.dormnumber = '';
            }
          }
        },
        search3(){    //按照锁id进行模糊查询
          let _this = this;
          _this.currentPage = 1;
          let arr = [];
          if (_this.filters.lockid === "") {
            _this.$message({
              showClose: true,
              message: '您输入的锁ID为空 !',
              type: 'warning',
              center: true
            });
          }else {
            _this.searchResult = [];
            for (let i = 0; i <_this.users.length; i++) {
              if (_this.users[i].lockId.indexOf(_this.filters.lockid)!==-1) {   //indexOf实现输入数据的模糊查询
                arr.push(_this.users[i]) ;
                console.log(arr);
              }
            }
            _this.searchResult = arr;
            _this.cutPage(arr);
            _this.filters.lockid = '';
          }
        },
        search4(){   //按照学生姓名进行模糊查询
          let _this = this;
          let arr = [];
          _this.currentPage = 1;
          if (_this.filters.studentname === "") {
            _this.$message({
              showClose: true,
              message: '您输入的学生姓名为空 !',
              type: 'warning',
              center: true
            });
          }else {
            _this.searchResult = [];
            for (let i = 0; i <_this.users.length; i++) {
              if (_this.users[i].studentName.indexOf(_this.filters.studentname)!==-1) {   //indexOf实现输入数据的模糊查询
                arr.push(_this.users[i]) ;
                console.log(arr);
              }
            }
            _this.searchResult = arr;
            _this.cutPage(arr);
            _this.filters.studentname = '';
          }
        },
        search5(){   //按照学号进行模糊查询
          let _this = this;
          let arr = [];
          _this.currentPage = 1;
          if (_this.filters.studentnumber === "") {
            _this.$message({
              showClose: true,
              message: '您输入的学号为空 !',
              type: 'warning',
              center: true
            });
          }else {
            _this.searchResult = [];
            for (let i = 0; i <_this.users.length; i++) {
              if (_this.users[i].studentNumber.indexOf(_this.filters.studentnumber)!==-1) {   //indexOf实现输入数据的模糊查询
                arr.push(_this.users[i]) ;
                console.log(arr);
              }
            }
            _this.searchResult = arr;
            _this.cutPage(arr);
            _this.filters.studentnumber = '';
          }
        },


        /*新增及提交操作*/
        //显示新增界面
        handleAdd: function () {
          this.addFormVisible = true;
          this.addForm = {
            buildingName: '',
            dormNumber: '',
            lockId: '',
            studentName: '',
            studentNumber: '',
            studentCardId:'',
            identificationCardId:'',
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
                // console.log(para);
                this.$ajax({
                  method: 'post',
                  url: 'http://localhost:8082/studentInfo/newStudentInfo',
                  params: {buildingName: para.buildingName, dormNumber: para.dormNumber, lockId: para.lockId, userNumber: para.userNumber, studentName: para.studentName,
                           studentNumber: para.studentNumber, studentCardId: para.studentCardId, identificationCardId:para.identificationCardId},
                }).then((res) => {
                  console.log(res);
                  if(res.data === 'success'){
                    _this.addLoading = false;
                    _this.$message({
                      message: '提交成功',
                      type: 'success'
                    });
                    _this.$refs["addForm"].resetFields(); //重置表单
                    _this.addFormVisible = false; //提交成功之后界面关闭
                    _this.getStudentInfo();  //信息提交之后重新请求获得用户信息
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
                  method: 'get',
                  url: 'http://localhost:8082/studentInfo/editStudentInfo',
                  params: {id: para.id, studentCardId: para.studentCardId, identificationCardId: para.identificationCardId},
                }).then((res) => {
                  this.editLoading = false;
                  this.$message({
                    message: '提交成功',
                    type: 'success'
                  });
                  this.$refs['editForm'].resetFields();
                  this.editFormVisible = false;
                  this.getStudentInfo();  //信息更改之后重新请求获得用户信息
                });
              });
            }
          });
        },


        /*单行删除操作*/
        handleDel: function (index, row) {
          this.$confirm('确认删除该学生信息吗?', '提示', {
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
              url: 'http://localhost:8082/studentInfo/deleteOne',
              params: {id: para.id},
            }).then((res) => {  //通过api.js里面定义的axios路径访问后台的数据，将选中的数据在数据库中删除
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getStudentInfo();  //删除之后重新请求获得用户信息
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
          this.$confirm('确认删除所有选中的学生信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            // let para = { ids: ids };
            console.log(ids);
            this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/studentInfo/deleteMany',
              params: {ids},
            }).then((res) => {
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getStudentInfo();  //批量删除之后重新请求用户信息
              // location.reload()
            });
          }).catch(function (error) {
            console.log(error);
          });
        },



        /*实现将excel表格导入到数据库*/
        importViemShow:function(){         //打开导入模态框
          this.excelImportShow = true;
        },
        beforeAvatarUpload(file){    //提交前进行文件类型的判断
          const extension = file.name.split('.')[1] === 'xls';
          const extension2 = file.name.split('.')[1] === 'xlsx';
          if (!extension && !extension2){
            this.$message({
              message: '文件格式错误，只允许上传Excel文件！',
              type: 'warning',
              center: true
            });
            console.log(file,'文件');
            return false;  //提交失败
          }
        },
        submitUpload() {         //提交
          this.$refs.upload.submit();
        },
        onSuccess(response){   //上传后台成功，但无法确定是否写入数据库中是否成功
          console.log(response);
          if(response === "success"){
            this.$message({
              message: '文件上传成功！',
              type: 'success',
              center: true
            });
            this.fileUploadPercent = 100;
            clearInterval(this.time);
            let _this = this;
            setTimeout(function(){
              _this.fileFlag = false;
              _this.fileUploadPercent = 0;
            },2000)
          }else {
            this.$message({
              message: '文件内容不完整，请检查修改后重试！',
              type: 'error',
              center: true,
            });
            clearInterval(this.time);
            let _this = this;
            setTimeout(function(){
              _this.fileFlag = false;
            },2000)
          }

        },
        onProgress(){
          let _this = this;
          _this.fileFlag = true;
          clearInterval(this.time);
          this.time = setInterval(function(){
            if(_this.fileUploadPercent<90){
              _this.fileUploadPercent += 5;//进程条
            }else{
            }
          },1000)
        },

        onexceed(){     //每次限制上传一个文件，当超过数目时提示。
          this.$message({
            message: '每次只能上传一个文件，如需继续上传请删除已有记录!',
            type: 'warning',
            center: true
          });
        },

        beforeRemove(file) {
          const extension = file.name.split('.')[1] === 'xls';
          const extension2 = file.name.split('.')[1] === 'xlsx';
          if (extension || extension2){
          return this.$confirm(`确定移除 ${ file.name }吗？`);}
        },

        handleRemove(file,fileList) {
          console.log(file, fileList);
        },
       /* handlePost(file){
          const extension = file.name.split('.')[1] === 'xls';
          const extension2 = file.name.split('.')[1] === 'xlsx';
          if (!extension && !extension2){
            this.$message.warning('上传文档只能是xls、xlsx格式！');
            console.log(file,'文件');
            return false
          }
            let fd = new FormData();
            fd.append('file', file);
          console.log(fd);
            this.$ajax({
              method: 'post',
              url: '',
              params: {file: fd},
            }).then((res) => {
              console.log(res.data);
            });
        },*/

      }
  }

</script>

<style scoped>

</style>
