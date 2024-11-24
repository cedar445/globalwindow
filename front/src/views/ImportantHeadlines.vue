<template>
  <div class="background">
    <div class="the-nation-header">THE CHINA.</div>
    <div id="top" class="the-nation-header-top">THE CHINA.</div>
    <n-layout embedded content-style="margin-top: 0px; padding-left: 17%; padding-right: 17%; padding-top: 3%; padding-bottom: 5%;">
      <n-flex vertical size="large">
        <n-carousel interval="3000" :show-arrow="false" autoplay dot-type="line">
          <n-carousel v-if="images.length === 0">
            <div class="placeholder"></div>
          </n-carousel>
          <n-carousel v-else v-for="image in images" :key="image.id">
            <img :src="image.url" alt="Carousel Image" style="width: 100%; height: 70vh;">
          </n-carousel>
        </n-carousel>
        <n-flex vertical size="medium" class="news-container">
          <n-tag type="error" style="font-size: x-large; text-align: center; align-content: center; justify-content: space-evenly; padding: 20px;" round :bordered="false">
            重要新闻
          </n-tag>
          <n-card
            v-for="news in newsList"
            :key="news.id"
            style="margin-bottom: 10px; cursor:pointer"
            hoverable
            @click="goTOlink(news.url)"
          >
            <template #header>
              <div class="news-title">
                <div class="vertical-line"></div>
                <strong>{{ news.title }}</strong>
                <div class="title-line"></div>
              </div>
            </template>
          </n-card>
        </n-flex>
      </n-flex>
    </n-layout>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import newsService from '../api/newsService';

export default {
  name: 'ImportantHeadlines',
  setup() {
    const images = ref([]);
    const newsList = ref([]);
    const lastScroll = ref(0);

    const handleScroll = () => {
      const currentScroll = window.scrollY;
      const header = document.querySelector('.the-nation-header');
      const top = document.getElementById('top');
      if (currentScroll > lastScroll.value && currentScroll > 100) {
        // 向下滚动
        header.style.top = '-200px';
        top.classList.add('active');
      } else {
        // 向上滚动或处于页面顶部
        header.style.top = '0px';
        top.classList.remove('active');
      }
      lastScroll.value = currentScroll;
    };
    const fetchNews = async () => {
      try {
        const response = await newsService.getNewsByX(10);
        console.log(response); // 调试输出 API 响应
        // 直接使用返回的数组数据
        if (Array.isArray(response)) {
          newsList.value = response.map(item => ({
            id: item.newsId,
            title: item.news,
            url: item.link
          }));
        } else {
          console.error('Invalid response format', response);
        }
      } catch (error) {
        console.log(error);
      }
    };

    const fetchImages = async () => {
      try {
        const response = await newsService.getNewsImages(3);
        if (Array.isArray(response)) {
          images.value = response.map((imageUrl, index) => ({
            id: `image-${index}`, // 自动生成的简单 id
            url: imageUrl
          }));
        } else {
          console.error('Invalid response format', response);
        }
      } catch (error) {
        console.log(error);
      }
    };

    const goTOlink = (url) => {
      window.open(url, '_blank');
    };

    onMounted(() => {
      fetchNews();
      fetchImages();
      window.addEventListener('scroll', handleScroll);
    });

    return {
      goTOlink,
      newsList,
      images,
      handleScroll,
    };
  }
};
</script>

<style scoped>

img {
  object-fit: cover;
}

.the-nation-header {
  padding-top: 80px;
  padding-left: 2%;
  background-color: red;
  color: white;
  font-size: 10rem;
  font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
  font-weight: bold;
  text-align: left;
  width: 100%;
  position: relative;
  top: 0;
  left: 0;
  transition: top 0.3s ease-in-out;
  z-index: 100;
}

.the-nation-header-top, .the-nation-header-top.active {
  background-color: red;
  text-align: center;
  color: white;
  font-size: 1.5rem;
  font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
  display: block;
  z-index: 100;
  top: 0;
  left: 0;
  width: 100%;
  position: fixed;
}

.the-nation-header-top {
  opacity: 0;
  visibility: hidden;
  transition: visibility 0s linear 0.5s, opacity 0.5s linear;
}

.the-nation-header-top.active {
  opacity: 1;
  visibility: visible;
  transition: opacity 0.5s linear;
}

.background :deep(.n-layout) {
  background-color: #f0f2f5;
}

.placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70vh;
  font-size: 24px;
  color: #ccc;
}

.news-container {
  margin-top: 20px;
  height: 70vh;
  overflow-y: auto;
}

.vertical-line {
  position: absolute;
  left: -1%;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: red;
  transform: scaleY(1);
}

.news-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-left: 10px; /* 调整标题的左内边距 */
  position: relative;
}

.title-line {
  position: absolute;
  left: 1%;
  bottom: 0;
  height: 2px; 
  width: 0;
  background-color: red;
  transition: width 0.3s ease-in-out;
}

.news-title:hover .title-line {
  width: 90%;
}
</style>
