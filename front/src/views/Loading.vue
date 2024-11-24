<template>
  <div class="app" :style="mainStyle" @mousemove="handleMouseMove">
    <div class="starry-sky" :style="backgroundStyle">
      <div class="stars"></div>
    </div>
    <div>
      <earthScene class="earth"/>
    </div>
    <div class="loading" v-if="isLoading">
      <div class="loading-text" :style="progressStyle">Loading...</div>
    </div>
    <div class="title" v-else>寰球视窗</div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';
import earthScene from '../components/earthScene.vue';

const isLoading = ref(true);
const mouseX = ref(0);
const mouseY = ref(0);
const progress = ref(0);
const router = useRouter();

const data = {
  fullWidth: window.innerWidth,
  fullHeight: window.innerHeight,
};

onBeforeMount(() => {
  window.addEventListener('resize', handleResize);
  document.addEventListener('mousemove', handleMouseMove);
});

onMounted(() => {
  const interval = setInterval(() => {
    progress.value++;
    if (progress.value <= 100) { // 限制进度条最大值为100
      progressStyle.value.width = `${progress.value++}%`;
    }
    if (progress.value == 90) {
      isLoading.value = false;
    }
    if (progress.value >= 160) { 
      clearInterval(interval);
      router.push('/main');
    }
  }, 50); // 5秒内完成加载 (5000ms / 100)

  setTimeout(() => {
    if (progress.value < 100) {
      progressStyle.value.width = `100%`;
      progress.value = 100;
      isLoading.value = false;
      router.push('/main');
    }
  }, 8000);
});

const mainStyle = ref({
  width: `${data.fullWidth}px`,
  height: `${data.fullHeight}px`,
});

const backgroundStyle = ref({
  transform: `translate(${mouseX.value / 20}px, ${mouseY.value / 20}px)`,
});

const progressStyle = ref({
  width: `0%`,
});

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

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  document.removeEventListener('mousemove', handleMouseMove);
});

function handleResize() {
  data.fullWidth = window.innerWidth;
  data.fullHeight = window.innerHeight;
  mainStyle.value.width = `${data.fullWidth}px`;
  mainStyle.value.height = `${data.fullHeight}px`;
}

function handleMouseMove(event: MouseEvent) {
  mouseX.value = (event.clientX / window.innerWidth) * 360 - 180;
  mouseY.value = (event.clientY / window.innerHeight) * 180 - 90;
  //earthStyle.value.transform = `translate(${-(mouseX.value / 10)}px, ${-(mouseY.value / 10)}px)`;
  backgroundStyle.value.transform = `translate(${mouseX.value / 10}px, ${mouseY.value / 10}px)`;
}

</script>

<style scoped>
.app {
  top: 0;
  left: 0;
  position: fixed;
  overflow: hidden;
  z-index: -1;
}

.starry-sky {
  position: absolute;
  top: -3%;
  left: -3%;
  width: 105%;
  height: 105%;
  background: url('../assets/LoadingPage/starry2.jpg') center/cover;
  background-position: center center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  background-size: cover;
  z-index: -1;
  transition: transform 0.1s;
}

.stars {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.stars:after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  box-shadow: 0 0 2px #fff, 0 0 4px #fff, 0 0 8px #fff;
  animation: star-flicker 2s infinite alternate;
}

@keyframes star-flicker {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.earth {
  position: absolute;
  top: 38%;
  left: 30%;
  transform-origin: center center;
  transition: transform 0.1s;
  z-index: 999;
  overflow: visible;
}

.loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: white;
}

.loading-text {
  transition: width 0.2s;
  font-size: 30px;
  border-radius: 20px;
  animation: FadeOut 6s infinite;
  background: rgb(216,53,218);
  background: linear-gradient(90deg, rgba(216,53,218,1) 0%, rgba(67,9,121,1) 39%, rgba(0,151,255,1) 100%);
}

@keyframes FadeOut {
  70% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.title {
  font-size: 5rem;
  font-weight: bold;
  background: linear-gradient(90deg, rgba(216,53,218,1) 0%, rgba(0,151,255,1) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: transparent;
  color: #ffffff;
  position: absolute;
  top: 42%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: FadeIn 2s;
}

@keyframes FadeIn {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}

.line-box {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  pointer-events: none; /* 确保流星不会阻挡其他元素的交互 */
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

