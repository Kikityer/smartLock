<template>
  <section class="chart-container">
    <el-row style="padding: 20px 10px 0 10px">
      <el-col :span="12">
        <div id="chartColumn" style="width:100%; height:330px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="chartBar" style="width:100%; height:330px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="lockColumn" style="width:100%; height:330px;"></div>
      </el-col>
    </el-row>
  </section>
</template>

<script>
  import echarts from 'echarts'

  export default {
    name: "ChartInfo",
    data() {
      return {
        chartColumn: null,
        chartBar: null,
        xdata: [],
        ydata:[],
        xYearData:[],
        yYearData:[],
        xLockNum:[],
        yLockNum:[],
      }
    },
    methods: {
      getAllDate() {
        let _this = this;
        let data1 = []; //省份的key值
        let data2 = []; //省份的value值
        let data3 = []; //年份的key值
        let data4 = []; //年份的value值
        let data5 = []; //锁数量的key值
        let data6 = []; //锁数量的value值
        this.$ajax({
          method: 'get',
          async: false,
          dataType: 'json',
          url: 'http://localhost:8082/cilentInfo/getEchartData',
        }).then(res => {
          console.log(res);

          for (let key in res.data) {
            let reg = /^[0-9]+.?[0-9]*$/;
            if (!reg.test(key)){
                if(key.indexOf("大学")!==-1 || key.indexOf("学院")!==-1 || key.indexOf("中学")!==-1 || key.indexOf("学校")!==-1){
                  data5.push(key)
                  data6.push(res.data[key])
                }else {
                  data1.push(key);
                  data2.push(res.data[key]);
                }
            }else {
              data3.push(key);
              data4.push(res.data[key])
            }

          }
          // console.log(data1);
          // console.log(data2);
           _this.xdata = data1;
           _this.ydata = data2;
           _this.xYearData = data3;
           _this.yYearData = data4;
           _this.xLockNum = data5;
           _this.yLockNum = data6;

          // console.log(_this);
          console.log(_this.xdata);
          console.log(_this.ydata);
          console.log(_this.xYearData);
          console.log(_this.yYearData);
          this.chartColumn.setOption({
            title: {
              text: '产品销售地区分布',
              subtext: '省份/直辖市'
            },
            tooltip: {},
            legend: {
              data:['地址分布']
            },
            xAxis:{
              data: _this.xdata
            },
            yAxis: {},
            series: [{
              name: '本地区销售数量',
              type: 'bar',
              data: _this.ydata
            }]
          });
          this.chartBar.setOption({
            title: {
              text: '产品销售日期分布',
              subtext: '年份'
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis:{
              type: 'category',
              data: _this.xYearData
            },
            yAxis: { type: 'value'},
            series: [{
              name: '本年份销售数量',
              type: 'line',
              data: _this.yYearData
            }]
          });
          this.chartColumn1.setOption({
            title: {
              text: '各高校销售总量',
              subtext: '单位/套数'
            },
            tooltip: {},
            legend: {
              data:['地址分布']
            },
            grid:{y2:120},
            xAxis:{
              data: _this.xLockNum,
              axisLabel:{
                interval:0,
                rotate:40
              }
            },
            yAxis: {},
            series: [{
              name: '本校销售总量',
              type: 'bar',
              data: _this.yLockNum
            }]
          });
        });

      },

      drawColumnChart() {
        this.chartColumn = echarts.init(document.getElementById('chartColumn'));
       /* this.chartColumn.setOption({
          title: {
            text: '客户地址分布',
            subtext: '省份/直辖市'
          },
          tooltip: {},
          xAxis: {
            data: this.xdata
          },
          yAxis: {},
          series: [{
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10]

          }]
        });*/
      },
      drawBarChart() {
        this.chartBar = echarts.init(document.getElementById('chartBar'));
        /*this.chartBar.setOption({
          title: {
            text: 'Bar Chart',
            subtext: '数据来自网络'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: ['2011年', '2012年']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
          },
          yAxis: {
            type: 'category',
            data: ['巴西', '印尼', '美国', '印度', '中国', '世界人口(万)']

          },
          series: [
            {
              name: '2011年',
              type: 'bar',
              data: [18203, 23489, 29034, 104970, 131744, 630230]
            },
            {
              name: '2012年',
              type: 'bar',
              data: [19325, 23438, 31000, 121594, 134141, 681807]
            }
          ]
        });*/
      },

      drawColumnChart1() {
        this.chartColumn1 = echarts.init(document.getElementById('lockColumn'));
      },
      drawCharts() {
        this.drawColumnChart();
        this.drawBarChart();
        this.drawColumnChart1();
      },
    },
    mounted: function () {
      this.drawCharts();
      this.getAllDate();
    },
    updated: function () {
      this.drawCharts()
    }
  }
</script>

<style scoped>
  .chart-container {
    width: 100%;
    float: left;
  }

  el-col {
    /*padding: 30px 20px;*/
  }
</style>
