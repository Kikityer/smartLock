<template>
   <section class="productsIntro">
     <el-row>
       <el-col :span="12">
         <div class="block">
           <el-carousel height="280px" style="position: relative; left: 6%" >
             <el-carousel-item v-for="item in pic1" :key="item">
               <img :src='item'/>
             </el-carousel-item>
           </el-carousel>
         </div>
         <span style="position: relative; left: 46%">实物展示</span>
       </el-col>
       <el-col :span="12">
         <div class="block">
           <el-carousel  height="280px" style="position: relative; left: 5%">
             <el-carousel-item v-for="item in 4" :key="item">
               <h3>{{ item }}</h3>
             </el-carousel-item>
           </el-carousel>
         </div>
         <span style="position: relative; left: 45%">场景模拟</span>
       </el-col>
     </el-row>

     <div style="background-color: aliceblue">
       <h1 style="text-align: center; font-size: 23px; padding: 10px 0" >智能锁产品介绍</h1>
       <p>本产品主要是面向高校定制化寝室门禁锁的</p>
     </div>

   </section>
</template>

<script>
    export default {
        name: "Introduce",
      data(){
          return{
            pic1:[require("../../assets/lock.jpg"),
                  require("../../assets/lock1.jpg"),]
          }
      },
      methods:{

      }
    }
</script>

<style scoped>
  .block{
    width: 90%;
    margin: 20px 0 10px 0;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;}
  img{
    width: 100%;
    background-size: contain;
  }
</style>
