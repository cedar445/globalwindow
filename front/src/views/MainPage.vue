<template>
    <div class="home-container" :style="mainStyle" @mousemove="handleMouseMove">
      <div class="background" :style="backgroundStyle"></div>
        <div class="content">
          <h1>寰球视野</h1>
          <p>迅速发现全球热点事件。从这里，看见世界。<br>From Here : Know Everything</p>
          <button class="explore-button" @click="goToNowadaysHotSpot">查看今日热榜</button>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

export default {
  setup() {
      const mouseX = ref(0);
      const mouseY = ref(0);
      const data = {
        fullWidth: window.innerWidth,
        fullHeight: window.innerHeight,
      };
      const backgroundStyle = ref({
        transform: '',
      });

      const mainStyle = ref({
        width: `${window.innerWidth}px`,
        height: `${window.innerHeight}px`,
      });

      // 处理鼠标移动事件
      function handleMouseMove(event: MouseEvent) {
        mouseX.value = (event.clientX / window.innerWidth) * 360 - 180;
        mouseY.value = (event.clientY / window.innerHeight) * 180 - 90;
        backgroundStyle.value.transform = `translate(${mouseX.value / 15}px, ${mouseY.value / 15}px)`;
      }

      // 处理窗口大小变化事件
      function handleResize() {
        data.fullWidth = window.innerWidth;
        data.fullHeight = window.innerHeight;
        mainStyle.value.width = `${data.fullWidth}px`;
        mainStyle.value.height = `${data.fullHeight}px`;
      }

      onMounted(() => {
        document.addEventListener('mousemove', handleMouseMove);
        window.addEventListener('resize', handleResize);
      });

      onUnmounted(() => {
        document.removeEventListener('mousemove', handleMouseMove);
        window.removeEventListener('resize', handleResize);
      });

      return {
        backgroundStyle, handleMouseMove, mainStyle
      };
  },
  methods: {
    goToNowadaysHotSpot() {
      this.$router.push('/nav/nowadaysHotSpots');
    }
  }
}
</script>

<style scoped>

body, html {
  overflow: hidden;
}

.home-container {
  top: 0;
  left: 0;
  overflow: hidden;
  z-index: -1;
  position: fixed;
}

.background {
  position: absolute;
  top: -3%;
  left: -3%;
  width: 105%;
  height: 105%;
  background: url('../assets/HomePage/back2.jpg') center/cover;
  background-position: center center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  background-size: cover;
  z-index: -1;
  transition: transform 0.1s;
}

.content {
  position: absolute;
  top: 30%;
  left: 10%;
  text-align: left;
  color: white;
  max-width: 600px;
}

h1 {
  font-size: 4rem;
  margin-bottom: 100px;
  background: linear-gradient(90deg, rgba(216,53,218,1) 0%, rgba(0,151,255,1) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  -webkit-text-stroke: #ffffff;
  background-clip: text;
  color: transparent;
}

p {
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 60px;
}

.explore-button {
  margin-top: 30px;
  padding-left: 40px;
  padding-right: 40px;
  padding-top: 30px;
  padding-bottom: 30px;
  font-size: 1rem;
  color: #0e77d3;
  background-color: #ffffff;
  border: none;
  border-radius: 40px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.explore-button:hover {
  background: linear-gradient(90deg, rgba(216,53,218,1) 0%, rgba(0,151,255,1) 100%);
  color: white;
}
</style>
