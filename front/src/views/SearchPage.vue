<template>
  <div class="search-page">
    <div class="searchbar">
      <img src="../assets/NavBar/searchIcon.png" class="mg">
      <input class="input" type="text" id="search" v-model="keyword"
               placeholder="搜索..."
               @focus="clearKeyword"
               @blur="resetKeyword"
               @keypress.enter="SearchPage"
               prefix="magnify"/>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-if="error" class="error">
      <n-result size="huge" status="404" title="404 资源不存在" description="生活总归带点荒谬">
        <template #footer>
          <n-button size="large" type="info" style="width:auto; padding-left: 15px; padding-right: 15px;" circle @click="resetError">找点乐子吧</n-button>
        </template>
      </n-result>
    </div>
    <div v-if="!loading && !error" class="results">
      <n-card v-for="newsItem in news"
              :key="`news-${newsItem.newsId}`"
              @click="goToNewsDetails(newsItem.newsId)"
              size = "huge"
              :bordered="false"
              style="cursor: pointer"
              hoverable>
        <template #header>
          <div class="resultTitle">{{ newsItem.news }}</div>
        </template>
        <template #default>
          <!--具体内容 -->
        </template>
        <template #footer>
          <n-tag class="footer" type="success" size = "large" round>news</n-tag>
          <n-tag type="error" size = "large" round>(热度: {{ newsItem.popularity }})</n-tag>
        </template>
      </n-card>

      <n-card v-for="event in events"
              :key="`event-${event.eventId}`"
              size = "huge"
              :bordered="false"
              style="cursor: pointer"
              @click="goToEventDetails(event.eventId)"
              hoverable>
        <template #header>
          <div class="resultTitle">{{ event.event }}</div>
        </template>
        <template #default>
        <!-- 一些具体内容 -->
        </template>
        <template #footer>
          <n-tag class="footer" type="info" size = "large" round>event</n-tag>
          <n-tag type="error" size = "large" round>(流行度: {{ event.popularity }})</n-tag>
        </template>
      </n-card>

      <n-card v-for="keyword in keywords"
              :key="`keyword-${keyword.keyword}`"
              size = "huge"
              :bordered="false"
              style="cursor: pointer"
              @click="goToKeywordDetails(keyword.keyword)"
              hoverable>
        <template #header>
          <div class="resultTitle">{{ keyword.keyword }}</div>
        </template>
        <template #default>
          <!-- 具体内容 -->
        </template>
        <template #footer>
          <n-tag class="footer" type="warning" size = "large" round>keyword</n-tag>
          <n-tag type="error" size = "large" round>(频率: {{ keyword.frequency }})</n-tag>
        </template>
      </n-card>
    </div>
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
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import keywordService, { keyword as Keyword } from '../api/keywordService';
import eventService, { event as Event } from '../api/eventService';
import newsService, { news as News } from '../api/newsService';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const keyword = ref(route.query.keyword as string || '请输入想查询的关键词');
    const keywords = ref<Keyword[]>([]);
    const events = ref<Event[]>([]);
    const news = ref<News[]>([]);
    const loading = ref(true);
    const error = ref<string | null>(null);

    const clearKeyword = () => {
      if (keyword.value === '请输入想查询的关键词') {
        keyword.value = '';
      }
    };

    const lineList = ref(
      Array.from({ length: 10 }).map((_, index) => ({
        c1: index === 9 ? '#DA70D6' : Math.random() > 0.5 ? '#69E4F6' : '#FED258',
        c2: index === 9 ? '#FF69B4' : Math.random() > 0.5 ? '#69e4f600' : 'rgba(254,210,88,0)',
        t: `${Math.random()*15}vh`,
        l: `${(Math.random() > 0.5) ? (Math.random()*15) : 100-(Math.random() * 15)}vw`,
        d: Math.floor(Math.random() * 6),
        r: Math.random() * 360,
        duration: `${Math.random() * 3 + 8}s`,
        delay: `${Math.random() * 5}s`, // 随机延迟0-5s
      }))
    );

    const resetKeyword = () => {
      if (keyword.value.trim() === '') {
        keyword.value = '请输入想查询的关键词';
      }
    };

    const search = async () => {
      loading.value = true;
      error.value = null;
      try {
        const [keywordResults, eventResults, newsResults] = await Promise.all([
          keywordService.searchKeywords(keyword.value, 10),
          eventService.searchEvent(keyword.value, 10),
          newsService.searchNews(keyword.value, 10)
        ]);

        // 检查当前路由是否仍然是搜索页面
        if (route.name === 'SearchPage' || route.name === 'SearchResult') {
          keywords.value = keywordResults;
          events.value = eventResults;
          news.value = newsResults;

          // 如果所有结果都为空，则视为搜索失败
          if (keywordResults.length === 0 && eventResults.length === 0 && newsResults.length === 0) {
            throw new Error('No results found');
          }
        }
      } catch (err) {
        error.value = '搜索失败，请稍后重试';
        console.error(err);
      } finally {
        loading.value = false;
      }
    };


    const resetError = () => {
      window.location.href = 'https://ys.mihoyo.com/main/';
    };

    const SearchPage = async () => {
      console.log('Searching for:', keyword.value);
      
      // 将搜索关键词传递给路由跳转到搜索结果页面
      router.push({
        name: 'SearchPage',
        query: {
          keyword: keyword.value
        }
      });
    };

    const goToKeywordDetails = (keyword: string) => {
      router.push({
        name: 'keywordDetails',
        params: { keyword}
      });
    };

    const goToEventDetails = (eventId: string) => {
      router.push({
        name: 'eventDetails',
        params: { eventId}
      });
    };

    const goToNewsDetails = (newsId: number) => {
      router.push({
        name: 'newsDetails',
        params: { newsId}
      });
    };

    onMounted(() => {
      const queryKeyword = route.query.keyword as string;
      if (queryKeyword) {
        search();
      }
    });

    watch(() => route.query.keyword, (newKeyword) => {
      if (newKeyword) {
        keyword.value = newKeyword as string;
        search();
      }
    });

    return { resetError,lineList, keyword, keywords, events, news, loading, error, clearKeyword, resetKeyword, search, SearchPage, goToKeywordDetails, goToEventDetails, goToNewsDetails };
  }
};
</script>

<style scoped>
.search-page {
  padding: 20px;
  background-color: #f4f5f7;
}

.searchbar {
  border: 1px solid #e6e6e6;
  border-radius: 2em;
  width: 80%;
  max-width: 700px; 
  height: 50px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); 
  margin: 2% auto; /* 自动边距 */
  display: flex;
  align-items: center;
  transition: box-shadow 0.3s ease; /* 平滑的阴影过渡效果 */
}

.input {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  padding-left: 20px;
  font-size: 16px;
  color: #333;
  background-color: transparent; /* 透明背景 */
}

.searchbar:hover, .searchbar:focus-within {
  border-color: #0056b3; /* 焦点和悬停时边框颜色 */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.15);
}

.mg {
  width: 24px;
  margin-left: 15px;
}

.search-button {
  background: url('../assets/NavBar/searchIcon.png') no-repeat center;
  background-size: 100%;
  position: relative;
  border: none;
  color: white;
  border-radius: 2em;
  padding: 20px 20px;
  margin-right: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #a6c8ed;
}

.results {
  width: 80%;
  max-width: 1000px;
  margin: 20px auto;
  background: white;
  border-radius: 16px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  z-index: 5;
}

.data {
  font-size: 0.7em;
  border-radius: 100rem;
  padding: 10px 10px;
  color: #ffffff;
  background-color: #f07b3f;
}

.label {
  margin-left: 10px;
  padding: 4px 8px;
  border-radius: 16px;
  font-size: 12px;
  color: white;
}

.resultTitle {
  font-size: 1.5em;
  margin-bottom: 20px;
  color: #1e62f7;
}

.line-box {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  pointer-events: none; /* 确保流星不会阻挡其他元素的交互 */
  z-index: 0;
}

.line-item {
  width: 2px;
  height: 200px;
  background: linear-gradient(0deg, var(--c1) 0%, var(--c2) 100%);
  position: absolute;
  top: var(--t);
  left: var(--l);
  transform: rotate(45deg);
  opacity: 0;
  animation: shank var(--duration) linear infinite; /* 使用随机持续时间 */
  animation-delay: var(--delay); /* 使用随机延迟 */
  z-index: 0;
}

.line-item::after {
  content: '';
  position: absolute;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--c1);
  filter: blur(3px);
  box-shadow: 0px -1px -1px 5px var(--c2);
  bottom: -4px;
  left: 50%;
  transform: translate(-50%);
}

@keyframes shank {
  0% {
    transform:translateY(-200px) scale(0.5); /* 调整初始位置 */
    opacity: 0;
  }
  70% {
    opacity: 1;
    transform:translateY(200px) scale(1.1); /* 调整中间位置 */
  }
  100% {
    transform:translateY(400px) scale(0.5); /* 调整结束位置 */
    opacity: 0;
  }
}  

n-card{
  cursor: pointer;
}

.footer{
  margin-right: 10px;
}
</style>




