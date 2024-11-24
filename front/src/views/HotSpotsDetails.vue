<template>
  <div class="keyword-dashboard">

    <div class="card">

      <div class="left-content">
        <n-card :bordered="false">
          <template #header>
            <h2> {{ keyword }}</h2>
          </template>
            <n-button ghost round type="error">
              频率: {{ frequency }}
              <template #icon>
                <n-icon :component="Fire" />
              </template>
            </n-button><br>
            <n-button ghost round type="info" style="margin-top:24px ">
              <span>来源:</span>
              <span v-for="(source, index) in keywordSources" :key="index" class="source">
                {{ source }}
              </span>
              <template #icon>
                <n-icon :component="SourceSharp" />
              </template>
            </n-button>
        </n-card>
      </div>

    </div>

    <div class="trend-chart">
      <n-card>
        <template #header>
          <h2>关键词趋势</h2>
        </template>
        <div ref="chartContainer" class="chart-placeholder"></div>
      </n-card>
    </div>

    <div class="news-container">
      <n-card>
        <template #header>
          <h2>相关新闻</h2>
        </template>
        <div v-if="news.length" class="news-list">
          <div class="news-item" v-for="item in news" :key="item.newsId" @click="goToNewsDetails(item.newsId)">
            <n-card class="news-card" hoverable :bordered="false" style="position: relative; margin-bottom: 20px;">
              <template #header>
                <div v-html="item.news" ></div>
              </template>
              <n-button  ghost round type="error" style="margin-right: 20px;" >
                热度指数: {{ item.popularity }}
              </n-button>
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
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import keywordService from '../api/keywordService';
import * as echarts from 'echarts';
import { useRouter, useRoute } from 'vue-router';
import { Fire } from '@vicons/fa';
import { SourceSharp, AbcFilled } from '@vicons/material';

export default {
  name: 'KeywordDetails',
  props: {
    keyword: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const router = useRouter();
    const route = useRoute();
    const news = ref([]);
    const keywordSources = ref([]);
    const frequency = ref(0);
    const chartContainer = ref(null);
    const ChartInstance = ref(null);

    const fetchKeywordFre = async (keyword) => {
      try {
        const response = await keywordService.getKeywordsFre(keyword);
        frequency.value = response;
      } catch (error) {
        console.error('获取关键词频率时出错:', error);
        frequency.value = 0; // 错误时设置为默认值
      }
    };

    const fetchKeywordNews = async (keyword, num) => {
      try {
        const response = await keywordService.getKeywordNews(keyword, num);
        news.value = response;
        for (let i = 0; i < 5; i++) {
          if (news.value[i].news.length > 100) {
            news.value[i].news = news.value[i].news.substring(0, 100) + '...';
          }
        }   
      } catch (error) {
        console.error('获取相关新闻时出错:', error);
      }
    };

    const fetchKeywordSources = async (keyword) => {
      try {
        const response = await keywordService.getKeywordSource(keyword);
        keywordSources.value = response;
      } catch (error) {
        console.error('获取关键词来源时出错:', error);
      }
    };

    const fetchEchartsData = async (keyword) => {
      try {
        const response = await keywordService.getKeywordEchartsData(keyword);
        const data = response.map(item => ({
          time: item.time,
          value: item.popularity
        }));
        renderChart(data);
      } catch (error) {
        console.error('获取Echarts数据时出错:', error);
      }
    };

    const renderChart = (data) => {
      if (chartContainer.value && data.length > 0) {
        const option = {
          xAxis: {
            type: 'category',
            data: data.map(item => item.time)
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: data.map(item => item.value),
              type: 'line',
              smooth: true
            }
          ]
        };
        ChartInstance.value = echarts.init(chartContainer.value);
        ChartInstance.value.setOption(option);
      }
    };

    function goToNowadaysHotSpots() {
      router.push('/nav/nowadaysHotSpots')
    }

    const fetchAllData = async (keyword) => {
      try {
        await fetchKeywordSources(keyword);
        await fetchKeywordNews(keyword, 5);
        await fetchEchartsData(keyword);
        await fetchKeywordFre(keyword); 
      } catch (error) {
        console.error('获取数据时出错:', error);
      }
    };

    const goToNewsDetails = (newsId) => {
      router.push({
        name: 'newsDetails',
        params: { newsId }
      });
    };

    onMounted(() => {
      fetchAllData(props.keyword);
    });

    watch(() => route.params.keyword, (newKeyword) => {
      news.value = [];
      keywordSources.value = [];
      frequency.value = 10;
      fetchAllData(newKeyword);
    });

    return {
      Fire,
      SourceSharp,
      news,
      frequency,
      keywordSources,
      goToNewsDetails,
      goToNowadaysHotSpots, 
      chartContainer,
      ChartInstance
    };
  }
};
</script>



<style scoped>

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

.keyword-dashboard {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-image: linear-gradient(45deg, rgba(255, 154, 158, 0.5) 0%, rgba(250, 208, 196, 0.5) 99%, rgba(250, 208, 196, 0.5) 100%);
}

.card {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 20px;
}

.left-content {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.right-comments {
  width: 33.3%;
  padding-left: 20px;
}


.trend-chart, .news-container {
  margin: 20px;
  padding: 20px;
}

.left-content, .right-comments, .trend-chart, .news-container {
  flex: 1;
  margin-bottom: 20px;
  border-radius: 30px;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 10px rgba(0,0,0,0.05);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.7), rgba(235, 235, 255, 0.7)) ; /* 现代感渐变背景 */
  transition: backdrop-filter 0.3s ease-in-out, background-color 0.3s ease-in-out, transform 0.3s ease-in-out; /* 精细过渡效果 */
}

.comments-list {
  margin-top: 10px;
}

.comment-item {
  margin-bottom: 10px;
  padding-bottom: 10px;
}

.comment-item p {
  margin: 0;
}

.left-content:hover, .right-comments:hover, .trend-chart:hover, .news-container:hover {
  backdrop-filter: blur(20px);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.8), rgba(235, 235, 255, 0.8)); /* 加深渐变效果 */
  transform: scale(1.03);
}

.chart-placeholder {
  width: 100%;
  height: 400px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}


.keyword {
  display: inline-block;
  margin-right: 10px;
  padding: 5px 10px;
  background: #f0f0f0;
  border-radius: 5px;
  font-size: 14px;
}

.news-container h2,
.trend-chart h2 {
  margin-bottom: 10px;
}

.news-list .news-item {
  margin-bottom: 10px;
}

.news-item a {
  text-decoration: none;
  color: #333;
}

.news-item .popularity {
  display: block;
  font-size: 12px;
  color: #888;
}

.no-news {
  color: #888;
}
</style>
