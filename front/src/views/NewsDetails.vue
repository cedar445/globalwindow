<template>
  <div class="dashboard">

    <div class="card">

      <div class="left-content">
        <n-card :bordered="false">
          <template #header>
            <h2>💫{{ newsTitle }}</h2>
          </template>
            <n-button ghost round type="error">热度: {{ popularity }}</n-button><br>
            <n-button ghost round type="info" style="margin-top:24px ">
              来源:
            <a v-if="newsLink" :href="newsLink" target="_blank" class="source">
              {{ newsLink }}
            </a>
            </n-button><br>
            
            <n-button ghost round type="warning" style="margin-top:24px ">
              关键词:
              <span v-for="keyword in keywords" :key="keyword.keyword"
                  @click="goToKeywordDetails(keyword.keyword)">
              {{ keyword.keyword }} ({{ keyword.frequency }})
              </span>
            </n-button>
        </n-card>
      </div>

      <div class="right-comments">
        <n-card :bordered="false">
          <template #header>
            <h2>💬热门评论</h2>
          </template>
          <div v-if="comments.length" class="comments-list">
            <div class="comment-item" v-for="comment in comments" :key="comment.commentId">
              <n-card hoverable :bordered="false" class="news-card">
                <template #header>
                  <span>{{ comment.comment }}</span>
                </template>
                <template #header-extra>
                  <n-button ghost type="info" round size="small">From-{{ comment.source }}</n-button>
                </template>
              </n-card>
            </div>
          </div>
          <div v-else>
            <n-empty description="可能是没有人评论吧">
              <template #extra>
                <n-button size="small" ghost type="success" @click="goToNowadaysHotSpots()">
                  看看别的
                </n-button>
              </template>
            </n-empty>
          </div>
        </n-card>
      </div>

    </div>

    <div class="charts-container">
      <n-card class="chart-left" :bordered="false">
        <template #header>
          <h2>🪐新闻态度分布</h2>
        </template>
        <div ref="pieChartContainer" class="chart-placeholder"></div>
      </n-card>
      <n-card class="chart-right" :bordered="false">
        <template #header>
          <h2>🌟新闻情感分布</h2>
        </template>
        <div ref="barChartContainer" class="chart-placeholder"></div>
      </n-card>
    </div>

    <div class="news-container">
      <n-card>
        <template #header>
          <h2>相关新闻</h2>
        </template>
        <div v-if="news.length">
            <div v-for="item in news" :key="item.newsId" >
              <n-card class="news-card" hoverable :bordered="false" style="position: relative; margin-bottom: 20px;">
                <template #header>
                <div v-html="item.news" ></div>
                </template>
                <n-button  ghost round type="error" style="margin-right: 20px;" >
                  热度指数: {{ item.popularity }}
                </n-button>
                <n-button type="info" round @click="goToNewsDetails(item.newsId)">
                  查看详情
                </n-button>
              </n-card>
            </div>
        </div>
        <div v-else>
          <n-empty description="竟然没有相关的新闻吗……">
            <template #extra>
              <n-button size="small" ghost type="success" @click="goToNowadaysHotSpots()">
                看看别的
              </n-button>
            </template>
          </n-empty>
        </div>
     </n-card>
    </div>

  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import newsService from '../api/newsService';
import { useRoute, useRouter } from 'vue-router';
import * as echarts from 'echarts';

export default {
  name: 'NewsDetails',
  props: {
    newsId: {
      type: Number,
      required: true
    }
  },
  setup(props) {
    const route = useRoute();
    const router = useRouter();
    const newsTitle = ref('');
    const popularity = ref(0);
    const newsLink = ref('');
    const newsSources = ref([]);
    const keywords = ref([]);
    const comments = ref([]);
    const news = ref([]);
    const pieChartContainer = ref(null);
    const barChartContainer = ref(null);
    let pieChartInstance = null;
    let barChartInstance = null;

    const fetchNewsData = async (newsId) => {
      try {
        const response = await newsService.getNewsById(newsId);
        newsTitle.value = response.news;
        if (newsTitle.value.length > 40) {
          newsTitle.value = newsTitle.value.substring(0, 40) + '...';
        }
        popularity.value = response.popularity;
        newsLink.value = response.link;
      } catch (error) {
        console.error('获取新闻详情时出错:', error);
      }
    };

    const fetchNewsSources = async (newsId) => {
      try {
        const response = await newsService.getNewsSource(newsId);
        newsSources.value = response;
      } catch (error) {
        console.error('获取新闻来源时出错:', error);
      }
    };

    const fetchKeywords = async (newsId) => {
      try {
        const response = await newsService.getNewsKeywords(newsId, 5);
        keywords.value = response;
      } catch (error) {
        console.error('获取相关关键词时出错:', error);
      }
    };

    function goToNowadaysHotSpots() {
      router.push('/nav/nowadaysHotSpots')
    }

    const fetchComments = async (newsId) => {
      try {
        const response = await newsService.getNewsComments(newsId, 3);
        comments.value = response;
      } catch (error) {
        console.error('获取热门评论时出错:', error);
      }
    };

    const getRelatedNews = async (newsId, num) => {
      try {
        console.log(`Fetching related news for newsId: ${newsId}, num: ${num}`);
        const response = await newsService.getNewsRelatedNews(newsId, num);
        news.value = response;
        for (let i = 0; i < 5; i++) {
          if (news.value[i].news.length > 100) {
            news.value[i].news = news.value[i].news.substring(0, 100) + '...';
          }
        }        
      } catch (error) {
        console.error(`Error fetching related news for newsId: ${newsId}, num: ${num}`, error);
        news.value = []; // 确保在出错时新闻数组为空，不会影响页面显示
      }
    };

    const fetchNewsAttitude = async (newsId) => {
      try {
        const response = await newsService.getNewsAttitude(newsId);
        const attitudeData = response.map(item => ({ name: item.attitude || '无态度', value: item.number }));
        initPieChart(attitudeData);
      } catch (error) {
        console.error('获取新闻态度数据时出错:', error);
      }
    };

    const fetchNewsEmotion = async (newsId) => {
      try {
        const response = await newsService.getNewsEmotion(newsId);
        const emotionData = response.map(item => ({ name: item.emotion, value: item.number }));
        initBarChart(emotionData);
      } catch (error) {
        console.error('获取新闻情感数据时出错:', error);
      }
    };

    const randomColor = () => {
      const letters = '0123456789ABCDEF';
      let color = '#';
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    };

    const initPieChart = (data) => {
      if (pieChartContainer.value) {
        pieChartInstance = echarts.init(pieChartContainer.value);
        const options = {
          series: [
            {
              name: '新闻态度',
              type: 'pie',
              radius: '50%',
              data: data.map(item => ({ ...item, itemStyle: { color: randomColor() } })),
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        pieChartInstance.setOption(options);
      }
    };

    const initBarChart = (data) => {
      if (barChartContainer.value) {
        barChartInstance = echarts.init(barChartContainer.value);
        const options = {
          xAxis: {
            type: 'category',
            data: data.map(item => item.name)
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '新闻情感',
              type: 'bar',
              data: data.map(item => ({ value: item.value, itemStyle: { color: randomColor() } })),
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        barChartInstance.setOption(options);
      }
    };

    const fetchAllData = async (newsId) => {
      try {
        console.log(`Fetching all data for newsId: ${newsId}`);
        await fetchNewsData(newsId);
        await fetchNewsSources(newsId);
        await fetchComments(newsId);
        await getRelatedNews(newsId, 5);
        await fetchNewsAttitude(newsId);
        await fetchNewsEmotion(newsId);
        await fetchKeywords(newsId);
      } catch (error) {
        console.error('加载所有数据时出错:', error);
      }
    };

    const goToKeywordDetails = (keyword) => {
      router.push({
        name: 'keywordDetails',
        params: { keyword }
      });
    };

    const goToNewsDetails = (newsId) => {
      router.push({
        name: 'newsDetails',
        params: { newsId }
      });
    };

    onMounted(() => {
      fetchAllData(props.newsId);
    });

    watch(() => props.newsId, (newNewsId) => {
      // 重置状态，防止残留
      console.log(`Watching props.newsId change: newNewsId = ${newNewsId}`);
      newsTitle.value = '';
      popularity.value = 0;
      newsLink.value = '';
      newsSources.value = [];
      keywords.value = [];
      comments.value = [];
      news.value = [];
      fetchAllData(newNewsId);
    });

    return {
      newsTitle,
      popularity,
      newsLink,
      newsSources,
      keywords,
      comments,
      news,
      goToKeywordDetails,
      goToNowadaysHotSpots,
      goToNewsDetails,
      pieChartContainer,
      barChartContainer
    };
  }
};
</script>


<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-image: linear-gradient(45deg, rgba(255, 154, 158, 0.5) 0%, rgba(250, 208, 196, 0.5) 99%, rgba(250, 208, 196, 0.5) 100%);
}

.card {
  flex-direction: row;
  padding: 20px;
  display: flex;
}

.left-content {
  width: 63%;
  display: flex;
  flex-direction: column;
  margin-right: 20px;
}

.right-comments {
  width: 30%;
  padding-left: 20px;
}

.charts-container {
  display:flex;
  padding: 20px;
  justify-content: space-between;
}

.chart-left, .chart-right {
  width: 48%;
  border: none;
}

.news-container, .charts-container {
  margin:30px;
  --n-border-color: transparent;
}

.left-content, .right-comments, .charts-container, .news-container {
  flex: 1;
  margin-bottom: 20px;
  border-radius: 30px;
  border: none;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 10px rgba(0,0,0,0.05);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.7), rgba(235, 235, 255, 0.7)) ; /* 现代感渐变背景 */
  transition: backdrop-filter 0.3s ease-in-out, background-color 0.3s ease-in-out, transform 0.3s ease-in-out; /* 精细过渡效果 */
}

.left-content:hover, .right-comments:hover, .charts-container:hover, .news-container:hover {
  backdrop-filter: blur(20px);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.8), rgba(235, 235, 255, 0.8)); /* 加深渐变效果 */
  transform: scale(1.03);
}

.news-card {
  position: relative;
}

.news-card::before{
  content: '';
  position: absolute;
  left: 0;
  top: 20%;
  bottom: 0;
  height: 50%;
  width: 2px; 
  background-color: rgb(246, 175, 89); 
}

.chart-placeholder {
  width: 100%;
  height: 400px;
}
</style>


