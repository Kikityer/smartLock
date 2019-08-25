<template>
   <section>
     <!--信息表上部工具条-->
     <!--制做一个工具栏，类型为封装好的工具栏，底部的内边框为0 作用为居中-->
     <el-col :span="24" class="toolbar"  style="padding-bottom: 0;">
       <!--form表单，用于收集、校验和提交数据。设置 inline 属性可以让表单域变为行内的表单域。model为表单数据对象-->
       <el-form :inline="true" :model="filters"  @submit.native.prevent>
         <el-form-item>
           <el-autocomplete  v-model="filters.buildingname" :fetch-suggestions="querySearchAsync" @select="controllselect"  placeholder="请选择楼宇" size="small" @keyup.enter.native="search1" style="width: 160px">
             <el-button  slot="append" icon="el-icon-search"  size="mini" v-on:click="search1"></el-button>
           </el-autocomplete>
         </el-form-item>
         <el-form-item>
           <el-input v-model="filters.dormnumber"   placeholder="寝室号查询" size="small" @keyup.enter.native="search2" style="width: 160px">
             <el-button  slot="append" icon="el-icon-search" size="mini" v-on:click="search2"></el-button>
           </el-input>
         </el-form-item>
         <el-form-item>
           <el-input v-model="filters.studentname"   placeholder="学生姓名查询" size="small" @keyup.enter.native="search3"  style="width: 160px">
             <el-button  slot="append" icon="el-icon-search" size="mini" v-on:click="search3"></el-button>
           </el-input>
         </el-form-item>
         <el-form-item>
           <el-date-picker v-model="filters.value1" type="date" placeholder="请选择日期" size="small" style="width: 128px"></el-date-picker>
           <el-button  icon="el-icon-search" size="small" v-on:click="search4" style="position: relative;left: -4.5px; background: #f1f2f7;"></el-button>
         </el-form-item>
         <el-form-item>
           <el-button size="mini" type="success" icon="el-icon-refresh"  v-on:click="reagain"  round>刷新</el-button>
         </el-form-item>
         <el-form-item>
           <el-tooltip class="item" effect="light" content="导出当前页信息" placement="top">
             <el-button size="mini" type="success" icon="el-icon-download"  v-on:click="exportToExcel"  round>导出</el-button>
           </el-tooltip>
         </el-form-item>
         <el-form-item>
           <el-select v-model="value" placeholder="请选择历史记录区间" size="small" style="width: 180px;" @keyup.enter.native="search5">
             <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
           </el-select>
           <el-button  icon="el-icon-search" size="small" v-on:click="search5" style="position: relative;left: -4px; top: 1px; height:30px"  type="primary"></el-button>
         </el-form-item>
       </el-form>
     </el-col>

     <!--表格信息展示-->
     <!--data绑定要显示的信息。selection-change列表改变事件，勾选选择框时触发 -->
     <el-table :data="pageTable[currentPage-1]" :highlight-current-row="true" v-loading="listLoading" @selection-change="selsChange" border  height="500" style="width: 100%;" :header-cell-style="{background:'#f1f2f7'}" >
       <el-table-column type="selection" min-width="55" align="center" ></el-table-column > <!--多选框列-->
       <el-table-column type="index" width="70" label="编号" align="center"></el-table-column> <!--索引编号列-->
       <el-table-column prop="buildingName" label="楼宇名称" min-width="120"  align="center">
         <template slot-scope="scope">
           <span>{{ scope.row.buildingName }}</span>
         </template>
       </el-table-column>
       <el-table-column prop="dormNumber" label="寝室号" min-width="120" sortable align="center">
         <template slot-scope="scope">
           <span>{{ scope.row.dormNumber }}</span>
         </template>
       </el-table-column>n-
       <el-table-column prop="studentName" label="学生姓名" min-width="150"  align="center">
         <template slot-scope="scope">
           <span>{{ scope.row.studentName }}</span>
         </template>
       </el-table-column>
       <el-table-column prop="studentNumber" label="学号" min-width="150" sortable align="center">
         <template slot-scope="scope">
           <span>{{ scope.row.studentNumber }}</span>
         </template>
       </el-table-column>
       <el-table-column prop="unlockMeasure" label="开锁方式" min-width="120"  align="center">
         <template slot-scope="scope">
           <span>{{ scope.row.unlockMeasure }}</span>
         </template>
       </el-table-column>
       <el-table-column prop="unlockTime" label="开锁时间" min-width="200"  align="center">
         <template slot-scope="scope">
           <!--<i class="el-icon-location"></i>-->
           <span>{{ scope.row.unlockTime }}</span>
         </template>
       </el-table-column>
       <el-table-column label="操作" min-width="120" align="center">
         <!--slot-scope="scope"父组件上的作用域插槽，由子组件提供数据scope.$index, scope.row-->
         <template slot-scope="scope">
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

   </section>
</template>

<script>
    export default {
        name: "LockOpen",
      data(){
          return{
            value: '七天内开锁记录',

            options: [{
              value: '七天内开锁记录',
              label: '七天内开锁记录'
            },{
              value: '半个月内开锁记录',
              label: '半个月内开锁记录'
            },{
              value: '一个月内开锁记录',
              label: '一个月内开锁记录'
            },{
              value: '三个月内开锁记录',
              label: '三个月内开锁记录'
            },{
              value: '六个月内开锁记录',
              label: '六个月内开锁记录'
            }],
              filters: {
              buildingname: '',
              dormnumber: '',
              studentname: '',
              value1: '',
            },
            brr: [],//输入楼宇信息查询之后的结果放入其中
            users: [], //后台传入的总数据
            pageTable: [],//要展示的总页数
            total: 0, //总的信息条数
            currentPage: 1,  //当前页数
            pagesize: 20,   //默认每页显示信息条数
            listLoading: false,
            sels: [],//列表选中列
            searchResult:[], //搜索之后结果集
          }
      },
      /*初始化页面完成后之后开始获得校园用户信息*/
      mounted() {
        this.getLockRecord();
      },
      methods:{
        /*实现刷新功能*/
        reagain(){
          this.getLockRecord();
          this.brr = [];
        },

        /*实现将当前页的表格信息导出Excel表*/
        exportToExcel(){
          require.ensure([], () => {
            const { export_json_to_excel } = require('../../excel/Export2Excel');
            const tHeader = ['楼宇名称', '寝室号', '学生姓名', '学号', '开锁方式','开锁时间'];
            //对应表格输出的title
            const filterVal = ['buildingName','dormNumber', 'studentName', 'studentNumber','unlockMeasure', 'unlockTime'];
            // 对应表格输出的数据
            const list = this.pageTable[this.currentPage-1];
            const data = this.formatJson(filterVal, list);
            export_json_to_excel(tHeader, data, '开锁记录Excel表'); //对应下载文件的名字
          })
        },
        formatJson(filterVal, jsonData) {
          return jsonData.map(v => filterVal.map(j => v[j]))
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
        getLockRecord:function() {
          let _this = this;
          _this.value ='七天内开锁记录';
          _this.currentPage = 1;
          this.listLoading = true;//页面初始化时，true时会显示加载图标
          let value = this.value;
          console.log(value);
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/unlockRecord/getRecord',
            params:{mark: value},
          }).then(function (res) {
            console.log(res.data);
           for (let i=0; i < res.data.length; i++){
             let time =  res.data[i].unlockTime;
             let d = new Date(time);
             let times = d.getFullYear() + '-' + ((d.getMonth() + 1)<10?'0'+(d.getMonth() + 1):(d.getMonth() + 1))
               + '-' + (d.getDate()<10?'0'+d.getDate():d.getDate()) + ' ' + (d.getHours()<10?'0'+d.getHours():d.getHours())
               + ':' + (d.getMinutes()<10?'0'+d.getMinutes():d.getMinutes()) + ':' + (d.getSeconds()<10?'0'+d.getSeconds():d.getSeconds());
             res.data[i].unlockTime = times;
           }
            _this.users = res.data;
            _this.searchResult = res.data;
            _this.cutPage(_this.users);
            _this.listLoading = false;
          }).catch(function (error) {
            console.log(error);
          })
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

        /*查询功能，前台数据查询,可以按照楼宇名称模糊查询*/
        search1(){
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
            if (!_this.brr.length) {
              _this.searchResult = [];
              for (let i = 0; i <_this.users.length; i++) {
                if (_this.users[i].buildingName.indexOf(_this.filters.buildingname)!==-1){   //indexOf实现输入数据的模糊查询
                  this.brr.push(_this.users[i])
                }
              }
              _this.searchResult = _this.brr;
              _this.cutPage(_this.brr);
              _this.filters.buildingname = '';
            }else {
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
             /* if (_this.brr[0].buildingName.indexOf(_this.filters.buildingname) === -1) {
                _this.brr = [];
                for (let i = 0; i < _this.users.length; i++) {
                  if (_this.users[i].buildingName.indexOf(_this.filters.buildingname) !== -1) {   //indexOf实现输入数据的模糊查询
                    _this.brr.push(_this.users[i])
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
        search2(){
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
            if (!_this.brr.length) {
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
            else {
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
        //学生姓名查询方法。直接去后台中的所有数据中查询指定的姓名。
        search3(){
          let _this = this;
          _this.currentPage = 1;
          let arr = [];
          if (_this.filters.studentname === "") {
            _this.$message({
              showClose: true,
              message: '您输入学生姓名为空 !',
              type: 'warning',
              center: true
            });
          }else {
            _this.searchResult = [];
            _this.listLoading = true;//页面初始化时，true时会显示加载图标
            _this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/unlockRecord/getRecord',
              params:{mark: '六个月内开锁记录'},
            }).then(function (res) {
              console.log(res.data);
              for (let i=0; i < res.data.length; i++){
                let time =  res.data[i].unlockTime;
                let d = new Date(time);
                let times = d.getFullYear() + '-' + ((d.getMonth() + 1)<10?'0'+(d.getMonth() + 1):(d.getMonth() + 1))
                  + '-' + (d.getDate()<10?'0'+d.getDate():d.getDate()) + ' ' + (d.getHours()<10?'0'+d.getHours():d.getHours())
                  + ':' + (d.getMinutes()<10?'0'+d.getMinutes():d.getMinutes()) + ':' + (d.getSeconds()<10?'0'+d.getSeconds():d.getSeconds());
                res.data[i].unlockTime = times;
              }
              _this.users = res.data;
              for (let i = 0; i <_this.users.length; i++) {
                if (_this.users[i].studentName.indexOf(_this.filters.studentname)!==-1) {   //indexOf实现输入数据的模糊查询
                  arr.push(_this.users[i]) ;
                  console.log(arr);
                }
              }
              _this.searchResult = arr;
              _this.cutPage(arr);
              _this.listLoading = false;
              _this.filters.studentname = '';
            }).catch(function (error) {
              console.log(error);
            });

          }
        },
        //日期查询功能。直接去后台数据库中的所有数据中查找指定的日期。
        search4(){
          let _this = this;
          _this.currentPage = 1;
          let arr = [];
          if (_this.filters.value1 === "") {
            _this.$message({
              showClose: true,
              message: '您输入的日期为空 !',
              type: 'warning',
              center: true
            });
          }else {
            _this.searchResult = [];
            _this.listLoading = true;//页面初始化时，true时会显示加载图标
            _this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/unlockRecord/getRecord',
              params:{mark: '六个月内开锁记录'},
            }).then(function (res) {
              console.log(res.data);
              for (let i=0; i < res.data.length; i++){
                let time =  res.data[i].unlockTime;
                let d = new Date(time); //后台传进的date类型中国标准时间做一个格式转换变成String类型
                let times = d.getFullYear() + '-' + ((d.getMonth() + 1)<10?'0'+(d.getMonth() + 1):(d.getMonth() + 1))
                  + '-' + (d.getDate()<10?'0'+d.getDate():d.getDate()) + ' ' + (d.getHours()<10?'0'+d.getHours():d.getHours())
                  + ':' + (d.getMinutes()<10?'0'+d.getMinutes():d.getMinutes()) + ':' + (d.getSeconds()<10?'0'+d.getSeconds():d.getSeconds());
                res.data[i].unlockTime = times;
              }
              let p = new Date(_this.filters.value1);//前台页面日期选择表单传进的date类型中国标准时间做一个格式转换变成String类型
              let date = p.getFullYear() + '-' + (p.getMonth() + 1) + '-' + p.getDate();
              _this.users = res.data;
              for (let i = 0; i <_this.users.length; i++) {
                if (_this.users[i].unlockTime.indexOf(date)!==-1) {   //indexOf实现输入数据的模糊查询_this.users[i].unlockTime === date
                  arr.push(_this.users[i]) ;
                  console.log(arr);
                }
              }
              _this.searchResult = arr;
              _this.cutPage(arr);
              _this.listLoading = false;
              _this.filters.value1 = '';
            }).catch(function (error) {
              console.log(error);
            });
          }
        },
        //选择开锁记录的查询区间范围函数
        search5:function(){
          let _this = this;
          _this.searchResult = [];
          _this.users = [];
          _this.listLoading = true;//页面初始化时，true时会显示加载图标
          let value = this.value;
          console.log(value);
          _this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/unlockRecord/getRecord',
            params:{mark: value},
          }).then(function (res) {
            console.log(res.data);
            for (let i=0; i < res.data.length; i++){
              let time =  res.data[i].unlockTime;
              let d = new Date(time);
              let times = d.getFullYear() + '-' + ((d.getMonth() + 1)<10?'0'+(d.getMonth() + 1):(d.getMonth() + 1))
                + '-' + (d.getDate()<10?'0'+d.getDate():d.getDate()) + ' ' + (d.getHours()<10?'0'+d.getHours():d.getHours())
                + ':' + (d.getMinutes()<10?'0'+d.getMinutes():d.getMinutes()) + ':' + (d.getSeconds()<10?'0'+d.getSeconds():d.getSeconds());
              res.data[i].unlockTime = times;
              _this.users = res.data;
            }
            _this.searchResult = _this.users;
            _this.cutPage(_this.users);
            _this.listLoading = false;
          }).catch(function (error) {
            console.log(error);
          })
        },


        /*单行删除操作*/
        handleDel: function (index, row) {
          this.$confirm('确认删除该开锁记录信息吗?', '提示', {
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
              url: 'http://localhost:8082/unlockRecord/deleteOne',
              params: {id: para.id},
            }).then((res) => {  //通过api.js里面定义的axios路径访问后台的数据，将选中的数据在数据库中删除
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getLockRecord();  //删除之后重新请求获得用户信息
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
          this.$confirm('确认删除所有选中的开锁记录信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            // let para = { ids: ids };
            console.log(ids);
            this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/unlockRecord/deleteMany',
              params: {ids},
            }).then((res) => {
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getLockRecord();  //批量删除之后重新请求用户信息
              // location.reload()
            });
          }).catch(function (error) {
            console.log(error);
          });
        },


      }
    }
</script>

<style scoped>

</style>
