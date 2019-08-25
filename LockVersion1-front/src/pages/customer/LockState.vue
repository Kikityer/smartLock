<template>
    <section>
      <!--信息表上部工具条-->
      <!--制做一个工具栏，类型为封装好的工具栏，底部的内边框为0 作用为居中-->
      <el-col :span="24" class="toolbar"  style="padding-bottom: 0;">
        <!--form表单，用于收集、校验和提交数据。设置 inline 属性可以让表单域变为行内的表单域。model为表单数据对象-->
        <el-form :inline="true" :model="filters"  @submit.native.prevent>
          <el-form-item>
            <el-autocomplete  v-model="filters.buildingname" :fetch-suggestions="querySearchAsync" @select="controllselect"  placeholder="请选择楼宇" size="medium" @keyup.enter.native="search1" style="width: 165px">
              <el-button  slot="append" icon="el-icon-search"  size="small" v-on:click="search1"></el-button>
            </el-autocomplete>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.dormnumber"   placeholder="寝室号查询" size="medium" @keyup.enter.native="search2" style="width: 165px">
              <el-button  slot="append" icon="el-icon-search" size="small" v-on:click="search2"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.lockid"   placeholder="锁ID查询" size="medium" @keyup.enter.native="search3"  style="width: 165px">
              <el-button  slot="append" icon="el-icon-search" size="small" v-on:click="search3"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="success" icon="el-icon-refresh"  v-on:click="reagain"  round>刷新</el-button>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item" effect="light" content="导出需换电池的寝室信息" placement="top-end">
              <el-button size="small" type="success" icon="el-icon-download"  v-on:click="exportToExcel1"  round>导出1</el-button>
            </el-tooltip>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item" effect="light" content="导出当前页信息" placement="top-start">
              <el-button size="small" type="success" icon="el-icon-download"  v-on:click="exportToExcel2"  round>导出2</el-button>
            </el-tooltip>
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
        <el-table-column prop="dormNumber" label="寝室号" min-width="120"  align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.dormNumber }}</span>
          </template>
        </el-table-column>n-
        <el-table-column prop="lockId" label="锁ID" min-width="180"  align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.lockId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="battery" label="剩余电量" min-width="100" sortable align="center">
          <template slot-scope="scope">
            <template v-if="80 <= scope.row.battery && scope.row.battery <= 100">
              <i class="iconfont" style="font-size: 2.1em">&#xe61b;</i>
            </template>
            <template v-else-if="60 <= scope.row.battery && scope.row.battery < 80">
              <i class="iconfont" style="font-size: 2.1em">&#xe625;</i>
            </template>
            <template v-else-if="20 <= scope.row.battery && scope.row.battery< 60">
              <i class="iconfont" style="font-size: 1.9em">&#xe631;</i>
            </template>
            <template v-else-if=" 10 <= scope.row.battery && scope.row.battery< 20 ">
              <i class="iconfont" style="color: red; font-size: 2.1em">&#xe600;</i>
            </template>
            <template v-else-if=" 0 < scope.row.battery && scope.row.battery< 10 ">
              <i class="iconfont " style="color: red; font-size: 2em">&#xe65a;</i>
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="signal" label="信号强度" min-width="100" sortable align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.signal === '3强'">
              <i class="iconfont icon-WIFIxinhao-ji" style="font-size: 2em"></i>
            </template>
            <template v-else-if="scope.row.signal === '2中'">
              <i class="iconfont icon-WIFIxinhao-ji1" style="font-size: 2em"></i>
            </template>
            <template v-else-if="scope.row.signal === '1弱'">
              <i class="iconfont icon-WIFIxinhao-ji2" style="font-size: 2em"></i>
            </template>
            <template v-else-if="scope.row.signal === '0无'">
              <i class="iconfont icon-WIFIxinhao-wu" style="color: red; font-size: 2em"></i>
            </template>
            <!--<span>{{ scope.row.signal }}</span>-->
          </template>
        </el-table-column>
        <el-table-column prop="currentTime" label="具体时间" min-width="150"  align="center">
          <template slot-scope="scope">
            <!--<i class="el-icon-location"></i>-->
            <span>{{ scope.row.currentTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="100" align="center">
          <!--slot-scope="scope"父组件上的作用域插槽，由子组件提供数据scope.$index, scope.row-->
          <template slot-scope="scope">
            <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)" round>删除</el-button>
          </template>
        </el-table-column>
        <el-table-column label="远程开锁" min-width="100" align="center">
          <!--slot-scope="scope"父组件上的作用域插槽，由子组件提供数据scope.$index, scope.row-->
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="openlock(scope.$index, scope.row)" round>开锁</el-button>
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
        name: "LockInfo",
      data(){
          return{
            filters: {
              buildingname: '',
              dormnumber: '',
              lockid: '',
              battery: '',
              signal: ''
            },
            brr: [],//输入楼宇信息查询之后的结果放入其中
            users: [], //后台传入的总数据
            pageTable: [],//要展示的总页数
            total: 0, //总的信息条数
            currentPage: 1,  //当前页数
            pagesize: 20,   //默认每页显示信息条数
            listLoading: false,
            sels: [],//列表选中列
            lowBattery:[], //低电量的寝室信息数组
            outSame:[], //低电量中去重之后的数组
            searchResult:[], //搜索之后结果集
          }
      },
      /*初始化页面完成后之后开始获得校园用户信息*/
      mounted() {
        this.getLockState();
      },
      methods:{
        /*实现刷新功能*/
        reagain(){
          this.getLockState();
          this.brr = [];
        },


        /*实现导出需要更换电池的寝室的Excel表格*/
        exportToExcel1(){
          require.ensure([], () => {
            const { export_json_to_excel } = require('../../excel/Export2Excel');
            const tHeader = ['楼宇名称', '寝室号', '剩余电量','日期'];
            //对应表格输出的title
            const filterVal = ['buildingName','dormNumber', 'battery','currentTime'];

            //当将所有的信息拿到之后，循环判断电池余量低于10以下的用户，将其放在数组容器lowBattery中。返回给生成给Excel的程序。
            for (let i = 0; i<this.users.length; i++){
              if (this.users[i].battery < 10) {
                  this.lowBattery.push(this.users[i]);
              }
            }
            let hash = [];
            for(let i = 0; i < this.lowBattery.length; i++){
              if (hash.indexOf(this.lowBattery[i].lockId) === -1){
                   hash.push(this.lowBattery[i].lockId);
                   this.outSame.push(this.lowBattery[i])
              }
            }
            const list = this.outSame;
            const data = this.formatJson(filterVal, list);
            export_json_to_excel(tHeader, data, '需换电池寝室名单'); //对应下载文件的名字
            this.lowBattery = [];
            this.outSame = [];
          })
        },
        formatJson(filterVal, jsonData) {
          return jsonData.map(v => filterVal.map(j => v[j]))
        },

        /*实现将当前页的表格信息导出Excel表*/
        exportToExcel2(){
          require.ensure([], () => {
            const { export_json_to_excel } = require('../../excel/Export2Excel');
            const tHeader = ['楼宇名称', '寝室号', '锁ID', '剩余电量', '信号','当前时间'];
            //对应表格输出的title
            const filterVal = ['buildingName','lockId', 'dormNumber', 'battery', 'signal','currentTime'];
            // 对应表格输出的数据
            const list = this.pageTable[this.currentPage-1];
            const data = this.formatJson2(filterVal, list);
            export_json_to_excel(tHeader, data, '门锁状态表'); //对应下载文件的名字
          })
        },
        formatJson2(filterVal, jsonData) {
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

        //在页面初始化完成之后只获取24小时内的状态信息
        getLockState() {
          let _this = this;
          _this.currentPage = 1;
          this.listLoading = true;//页面初始化时，true时会显示加载图标
          this.$ajax({
            method: 'get',
            url: 'http://localhost:8082/lockState/getLockState',
          }).then(function (res) {
            console.log(res);
            for (let i=0; i < res.data.length; i++){
              let time =  res.data[i].currentTime;
              let d = new Date(time);
              let times = d.getFullYear() + '-' + ((d.getMonth() + 1)<10?'0'+(d.getMonth() + 1):(d.getMonth() + 1))
                + '-' + (d.getDate()<10?'0'+d.getDate():d.getDate()) + ' ' + (d.getHours()<10?'0'+d.getHours():d.getHours())
                + ':' + (d.getMinutes()<10?'0'+d.getMinutes():d.getMinutes()) + ':' + (d.getSeconds()<10?'0'+d.getSeconds():d.getSeconds());
              res.data[i].currentTime = times;
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

        /*查询功能，前台数据查询,可以按照学校名称模糊查询*/
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
                  _this.brr.push(_this.users[i])
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
              /*if (_this.brr[0].buildingName.indexOf(_this.filters.buildingname) === -1) {
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
        search2(){    //按照寝室号进行模糊查找
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
        search3(){
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

        /*单行删除操作*/
        handleDel: function (index, row) {
          this.$confirm('确认删除该门锁状态信息吗?', '提示', {
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
              url: 'http://localhost:8082/lockState/deleteOne',
              params: {id: para.id},
            }).then((res) => {  //通过api.js里面定义的axios路径访问后台的数据，将选中的数据在数据库中删除
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getLockState();  //删除之后重新请求获得用户信息
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
          this.$confirm('确认删除所有选中的门锁状态信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            // let para = { ids: ids };
            console.log(ids);
            this.$ajax({
              method: 'get',
              url: 'http://localhost:8082/lockState/deleteMany',
              params: {ids},
            }).then((res) => {
              this.listLoading = false;
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getLockState();  //批量删除之后重新请求用户信息
              // location.reload()
            });
          }).catch(function (error) {
            console.log(error);
          });
        },


        /*实现立即开锁功能*/
        openlock:function (index, row) {
          this.$confirm('是否确认立即开锁？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const loading = this.$loading({
              lock: true,
              // fullscreen:false,
              body:true,
              text: '正在开锁中...',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.8)'
            });
            let para = { lockId: row.lockId };
            console.log(row);
            console.log(para);
            this.$ajax({
              method: 'post',
              url: 'http://localhost:8082/lockState/openImmediately',
              params: {lockId: para.lockId},
            }).then((res) => {  //通过api.js里面定义的axios路径访问后台的数据，将选中的数据在数据库中删除
              console.log(res);
              loading.close();
              if (res.data === "success"){
                this.$message({
                  message: '开锁成功!',
                  center: true,
                  type: 'success'
                });
              }
              else if(res.data === "Connection timed out: connect" || res.data==="Connection refused: connect"){
                this.$message({
                  message: '连接服务器异常，请重试！',
                  center: true,
                  type: 'error'
                });
              }
              else {
                this.$message({
                  message: '服务器无响应，请重试！',
                  center: true,
                  type: 'error'
                });
              }
            });
          }).catch(function (error) {
            console.log(error);
          })
        }
      }
    }
</script>

<style scoped>
  icon {

    width: 1em; height: 1em;

    vertical-align: -0.15em;

    fill: currentColor;

    overflow: hidden;

  }
</style>
