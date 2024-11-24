<template>
  <div class="dashboard">

    <div class="first-card">
      <div class="left-content">
        <n-card :bordered="false">
          <template #header>
            <h2>💫{{ eventName }}</h2>
          </template>
            <n-button ghost round type="error">
              热度: {{ popularity }}
              <template #icon>
                <n-icon :component="Fire" />
              </template>
            </n-button><br>
            <n-button ghost round type="info" style="margin-top:24px ">
              <span>来源:</span>
              <span v-for="(source, index) in eventSources" :key="index" class="source">
                {{ source }}
              </span>
              <template #icon>
                <n-icon :component="SourceSharp" />
              </template>
            </n-button>
           <template #footer>
            <n-button ghost round type="warning">
              <span>关键词:</span>
              <span v-for="keyword in keywords" :key="keyword.keyword"
                    @click="goToKeywordDetails(keyword.keyword)">
                {{ keyword.keyword }} ({{ keyword.frequency }})
              </span>
              <template #icon>
                <n-icon :component="AbcFilled" />
              </template>
            </n-button>
          </template> 
        </n-card>
      </div>
      <div class="right-comments">
        <n-card :bordered="false">
          <template #header>
            <h2>💬热门评论</h2>
          </template>
          <div v-if="comments.length">
            <div class="comment-item" v-for="comment in comments" :key="comment.commentId">
              <n-card hoverable :bordered="false" class="news-card">
                <template #header>
                  {{ comment.comment }}
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
      <div class="chart">
        <n-card :bordered="false">
          <template #header>
            <h2>🔥热度曲线</h2>
          </template>
          <div ref="trendChartContainer" class="TrendChart-placeholder"></div>
        </n-card>
      </div>
      <div class="chart" style="margin-left: 20px">
        <n-card :bordered="false">
          <template #header>
            <h2>🌟事件轴</h2>
          </template>
          <div ref="timelineChartContainer" class="TimelineChart-placeholder"></div>
        </n-card>
      </div>
    </div>

    <div class="news-container" style="margin: 20px">
      <n-card :bordered="false">
        <template #header>
          <h2>相关新闻</h2>
        </template>
        <div v-if="news.length">
          <div v-for="item in news" :key="item.newsId">
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
          <n-empty description="你什么也找不到">
            <template #extra>
              <n-button size="small" ghost type="success" @click="goToNowadaysHotSpots()" >
                看看别的
              </n-button>
            </template>
          </n-empty>
        </div>

      </n-card>
    </div>
  </div>

</template>


<script lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import eventService from '../api/eventService';
import { news } from '../api/newsService'; // 导入 news 类型
import * as echarts from 'echarts';
import { Fire } from '@vicons/fa';
import { SourceSharp, AbcFilled } from '@vicons/material';

export default {
  name: 'EventDetails',
  props: {
    eventId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const router = useRouter();
    const eventName = ref('');
    const popularity = ref(0);
    const newsList = ref<news[]>([]); // 使用 news 类型注解并重命名变量
    const eventSources = ref<string[]>([]);
    const keywords = ref<{ keyword: string; frequency: number }[]>([]);
    const comments = ref<{ commentId: number; comment: string; source: string }[]>([]);
    const trendChartContainer = ref(null);
    const timelineChartContainer = ref(null);
    let trendChartInstance: echarts.ECharts | null = null;
    let timelineChartInstance: echarts.ECharts | null = null;

    const initTrendChart = () => {
      const options = {
        xAxis: {
          type: 'time',
          boundaryGap: false,
       
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [],
          type: 'line',
          smooth: true
        }]
      };
      if (trendChartContainer.value) {
        trendChartInstance = echarts.init(trendChartContainer.value);
        trendChartInstance.setOption(options);
      }
    };

    const initTimelineChart = () => {
      const options = {
        xAxis: {
          type: 'time',
          boundaryGap: false,
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 1,
          show: false
        },
        series: [{
          type: 'scatter',
          data: [],
          label: {
            show: true,
            position: 'top'
          }
        }]
      };
      if (timelineChartContainer.value) {
        timelineChartInstance = echarts.init(timelineChartContainer.value);
        timelineChartInstance.setOption(options);
      }
    };

    const fetchEventData = async (eventId: string) => {
      try {
        console.log(`Fetching event data for eventId: ${eventId}`);
        const response = await eventService.getEventById(eventId);
        eventName.value = response.event;
        popularity.value = response.popularity;
        console.log('Event data:', response);
      } catch (error) {
        console.error('获取事件详情时出错:', error);
      }
    };

    const fetchTrendChartData = async (eventId: string) => {
      try {
        console.log(`Fetching trend chart data for eventId: ${eventId}`);
        const response = await eventService.getEventEchartsData(eventId);
        const data = response.map((item: { time: string, popularity: number }) => ({
          time: new Date(item.time).getTime(), // 确保时间被正确转换为时间戳
          value: item.popularity // 使用正确的字段
        }));
        if (trendChartInstance) {
          trendChartInstance.setOption({
            series: [{
              data: data.map((item: { time: any; value: any; }) => [item.time, item.value])
            }]
          });
        }
      } catch (error) {
        console.error('获取事件热度曲线数据时出错:', error);
      }
    };

    const fetchTimelineData = async (eventId: string) => {
      try {
        console.log(`Fetching event timeline data for eventId: ${eventId}`);
        const response = await eventService.getEventTimeline(eventId);
        const data = response.map((item: { time: string, event: string }) => ({
          time: new Date(item.time).getTime(),
          event: item.event
        }));
        if (timelineChartInstance) {
          timelineChartInstance.setOption({
            series: [{
              data: data.map((item: { time: any; event: any; }) => ({
                value: [item.time, 0],
                label: {
                  show: true,
                  formatter: item.event,
                  position: 'top'
                }
              }))
            }]
          });
        }
      } catch (error) {
        console.error('获取事件轴数据时出错:', error);
      }
    };

    const getRelatedNews = async (eventId: string, num: number) => {
      try {
        console.log(`Fetching related news for eventId: ${eventId}`);
        const response = await eventService.getRelatedNews(eventId, num);
        newsList.value = response; // 直接将返回值赋给 newsList
        for (let i = 0; i < 5; i++) {
          if (newsList.value[i].news.length > 100) {
            newsList.value[i].news = newsList.value[i].news.substring(0, 100) + '...';
          }
        }        
      } catch (error) {
        console.error('获取相关新闻时出错:', error);
        throw error;
      }
    };

    const fetchEventSources = async (eventId: string) => {
      try {
        console.log(`Fetching event sources for eventId: ${eventId}`);
        const response = await eventService.getEventSource(eventId);
        eventSources.value = response;
        console.log('Event sources:', eventSources.value);
      } catch (error) {
        console.error('获取事件来源时出错:', error);
      }
    };

    const fetchKeywords = async (eventId: string) => {
  try {
    console.log(`Fetching keywords for eventId: ${eventId}`);
    const response = await eventService.getRelatedKeywords(eventId, 5);

    // 过滤掉空值
    const filteredKeywords = response.filter(item => item !== null && item !== undefined);

    keywords.value = filteredKeywords;
    console.log('Keywords:', keywords.value);
  } catch (error) {
    console.error('获取相关关键词时出错:', error);
  }
};


    const fetchComments = async (eventId: string) => {
      try {
        const response = await eventService.getEventComment(eventId, 3);
        comments.value = response;
      } catch (error) {
        console.error('获取热门评论时出错:', error);
      }
    };

    const fetchAllData = async (eventId: string) => {
      try {
        await fetchEventData(eventId);
        await Promise.all([
          fetchEventSources(eventId),
          fetchTrendChartData(eventId),
          fetchTimelineData(eventId),
          fetchComments(eventId),
          getRelatedNews(eventId, 5),
          fetchKeywords(eventId),
        ]);
      } catch (error) {
        console.error('获取数据时出错:', error);
      }
    };

    const goToNewsDetails = (newsId: number) => {
      router.push({
        name: 'newsDetails',
        params: { newsId }
      });
    };

    const goToKeywordDetails = (keyword: string) => {
      router.push({
        name: 'keywordDetails',
        params: { keyword }
      });
    };

    function goToNowadaysHotSpots() {
      router.push('/nav/nowadaysHotSpots')
    }

    onMounted(() => {
      initTrendChart();
      initTimelineChart();
      fetchAllData(props.eventId);
      
    });

    watch(() => props.eventId, (newEventId) => {
      fetchAllData(newEventId);
    });

    onUnmounted(() => {
      if (trendChartInstance) {
        trendChartInstance.dispose();
      }
      if (timelineChartInstance) {
        timelineChartInstance.dispose();
      }
    });

    return {
      Fire,
      SourceSharp,
      AbcFilled,
      //以上是icon
      eventName,
      popularity,
      news: newsList,
      eventSources,
      goToNowadaysHotSpots,
      keywords,
      comments,
      goToNewsDetails,
      goToKeywordDetails,
      trendChartContainer,
      timelineChartContainer,
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

.first-card {
  flex-direction: row;
  padding: 20px;
  display: flex;
}

.left-content {
  width: 66.6%;
  display: flex;
  flex-direction: column;
}

.right-comments {
  width: 33.3%;
  margin-left: 20px;
}

.left-content, .right-comments, .chart, .news-container {
  flex: 1;
  margin-bottom: 20px;
  border-radius: 30px;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 10px rgba(0,0,0,0.05);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.7), rgba(235, 235, 255, 0.7)) ; /* 现代感渐变背景 */
  transition: backdrop-filter 0.3s ease-in-out, background-color 0.3s ease-in-out, transform 0.3s ease-in-out; /* 精细过渡效果 */
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

.charts-container {
  display: flex;
  padding: 20px;
  border: none;
}

.TrendChart-placeholder,
.TimelineChart-placeholder {
  width: 100%;
  height: 400px;
  border: 1px solid #eee; /* 示例边框样式 */
}

.news-container h2 {
  margin-bottom: 10px;
}

.left-content:hover, .right-comments:hover, .chart:hover, .news-container:hover {
  backdrop-filter: blur(20px);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.8), rgba(235, 235, 255, 0.8)); /* 加深渐变效果 */
  transform: scale(1.03);
}
</style>



