<template>
  <div class="about" @wheel="handleScroll">
    <h1>关于本项目</h1>
    <div><img src="../assets/NavBar/logo.png"></div>
    <div class = "MainText">
      <p>本项目是武汉大学2022级大二实训项目</p>
      <p>寰球视野GlobalWindow</p>
      <p>本项目旨在为用户提供互联网大数据情报监测分析</p>
      <p>我们的项目目标是让每一个用户都能</p>
      <p class = "SpecialText">从这里，看见世界</p>
    </div>
    <h2>开发团队</h2>
    <ul>
      <li v-for="member in teamMembers" :key="member.id">
        <div class="member-info">
          <div class="avatar-and-name">
            <img :src=member.avatar alt="头像" class="avatar">
            <span class="name">{{ member.name }}</span>
          </div>
          <div class="details">
            <p class="position">职位: {{ member.position }}</p>
            <p class="experience">经验: {{ member.experience }}</p>
          </div>
        </div>
      </li>
    </ul>
  </div>
  <div class="line-box">
      <span
        class="line-item"
        v-for="(item, index) in lineList"
        :key="index"
        :style="{
          '--c1': item.c1, //颜色1
          '--c2': item.c2, //颜色2
          '--t': item.t,   //top
          '--l': item.l,   //left
          '--d': item.d,   //
          '--r': item.r,
          '--duration': item.duration, //持续时间
          '--delay': item.delay,       //延迟时间
        }"
      ></span>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref} from 'vue';

const lineList = ref(
  Array.from({ length: 30 }).map((_, index) => ({
    c1: index === 9 ? '#DA70D6' : Math.random() > 0.5 ? '#69E4F6' : '#FED258',
    c2: index === 9 ? '#FF69B4' : Math.random() > 0.5 ? '#69e4f600' : 'rgba(254,210,88,0)',
    t: `${Math.random() * 100}vh`,
    l: `${Math.random() * 100}vw`,
    d: Math.floor(Math.random() * 6),
    r: Math.random() * 360,
    duration: `${Math.random() * 3 + 2}s`, // 随机时间2-5s
    delay: `${Math.random() * 5}s`, // 随机延迟0-5s
  }))
);

export default defineComponent({
  name: 'About',
  data() {
    innerHeight = window.innerHeight;

    onMounted(() => {
      window.addEventListener('resize', () => {
        innerHeight = window.innerHeight;
      });
    });

    return {
      teamMembers: [
        { id: 1, name: '张孝谦', position: '后端开发工程师 & 数据库工程师 & 项目经理', experience: '600天', avatar: '/teamMember/zxq.jpg'},
        { id: 2, name: '肖磊', position: '自然语言处理算法工程师 & 大语言模型算法工程师 & 前端UI设计优化工程师', experience: '500天', avatar: '/teamMember/xl.jpg'},
        { id: 3, name: '徐润', position: '大数据研究院 & 数据挖掘工程师 & 数据清洗专家', experience: '550天', avatar: '/teamMember/xr.jpg'},
        { id: 4, name: '陈胤含', position: '前端研发工程师 & 用户界面设计工程师 & 数据接口研究员', experience: '560天', avatar: '/teamMember/cyh.jpg'},
      ], lineList
    };
  },
  methods: {
    handleScroll(event: WheelEvent){
      const scrollDistance = event.deltaY > 0 ? innerHeight-70 : -innerHeight + 70;
      window.scrollBy({
        top: scrollDistance,
        behavior: 'smooth'
      })
      event.preventDefault();
    }
  }
});
</script>

<style scoped>
html {
  scroll-behavior: smooth;
  overflow: hidden;
}

.about {
  margin: auto;
  padding: 20px;
  text-align: center;
  animation: bg-pan-left 10s both;
  background: radial-gradient(circle, rgba(13,128,51,1) 0%, rgba(31,71,91,1) 100%);
  overflow: hidden;
}

@keyframes bg-pan-left {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}


h1, h2 {
  color: #333;
  margin-bottom: 47.5vh;
  font-size: 150px;
  background-image: linear-gradient(to right, #fa709a 0%, #fee140 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  position: relative;
  z-index: 1000;
}

.MainText {
  font-size: 20px;
  color: #ffffff;
  margin-bottom: 300px;
}

.SpecialText {
  font-style: oblique;
  font: bold;
  background: linear-gradient(93.23deg, #F778BA 2.35%, #79C0FF 76.99%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  z-index: 2;
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  position: relative;
  z-index: 2;
}

li {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background: linear-gradient(93.23deg, #cf94b2 2.35%, #12ad72 76.99%);
  position: relative;
  z-index: 2;
}

.member-info {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 15px;
}

.avatar-and-name {
  flex: 1;
  text-align: center;
}

.avatar {
  width: 70px;
  height: 70px;
  border-radius: 35px; /* 圆形头像 */
  margin-bottom: 10px; /* 名字和头像之间的距离 */
}

.name {
  display: block;
  color: #333;
  font-size: 20px;
}

.details {
  flex: 2;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  padding-left: 20px; /* 与头像部分的间隔 */
}

.position, .experience {
  margin: 5px 0;
  font-size: 18px;
}

.line-box {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  pointer-events: none; /* 确保流星不会阻挡其他元素的交互 */
  z-index: 1;
}

.line-item {
  width: 2px;
  height: 80px;
  background: linear-gradient(0deg, var(--c1) 0%, var(--c2) 100%);
  position: absolute;
  top: var(--t);
  left: var(--l);
  transform: rotate(45deg);
  opacity: 0;
  animation: shank var(--duration) linear infinite; /* 使用随机持续时间 */
  animation-delay: var(--delay); /* 使用随机延迟 */
}

.line-item::after {
  content: '';
  position: absolute;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--c1);
  filter: blur(1.8px);
  box-shadow: 0px -1px -1px 5px var(--c2);
  bottom: -4px;
  left: 50%;
  transform: translate(-50%);
}

@keyframes shank {
  0% {
    transform: rotate(45deg) translateY(-200px) scale(0.5); /* 调整初始位置 */
    opacity: 0;
  }
  70% {
    opacity: 1;
    transform: rotate(45deg) translateY(200px) scale(1.1); /* 调整中间位置 */
  }
  100% {
    transform: rotate(45deg) translateY(400px) scale(0.5); /* 调整结束位置 */
    opacity: 0;
  }
}
</style>
