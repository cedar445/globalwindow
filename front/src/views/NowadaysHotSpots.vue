<template>
  <div class="common-layout">
    <div class="main-content">
      <section class="hot-spot-content">
        <div class="hot-spot-left">
          <div class="events">
            <n-tag style="font-size: large;" round :bordered="false" type="info" size="large">
              事件热度排行榜
              <template #icon>
                <n-icon :component="Earth" />
              </template>
            </n-tag>
            <div v-for="event in events" :key="event.eventId">
              <n-card :bordered="false" >
                <template #header>
                  <span class="rank-circle">{{ event.rank }}</span>
                  <span class="event-name"
                        @mouseover="handleMouseOver(event.eventId)"
                        @mouseleave="handleMouseLeave"
                        @click="goToEventDetails(event.eventId)">
                    {{ event.event }}
                  </span>
                </template>
                <template #header-extra>
                  <n-button type="info" style="border-radius: 15px; margin-right: 10px;" @click="goToEventDetails(event.eventId)">查看详情</n-button>
                  <n-button type="error" ghost style="border-radius: 15px;">热度指数:  {{ event.popularity }}</n-button>
                </template>
              </n-card>
            </div>
          </div>
          <div class="word-cloud">
            <n-tag style="font-size: large; padding-left: 23px" round :bordered="false" type="info" size="large">
              词云图
              <template #icon>
                <n-icon :component="Language" />
              </template>
            </n-tag>
            <div id="wordCloud" style="width: 100%; height: 300px;"></div>
          </div>
        </div>
        <div class="hot-spot-right">
          <div class="stats">
            <n-tag style="font-size: large;" round :bordered="false" type="info" size="large">
              事件时间线
              <template #icon>
                <n-icon :component="StatsChartSharp" />
              </template>
            </n-tag>
            <div ref="echartsContainer" style="width: 100%; height: 400px;"></div>
          </div>
          <div class="comments">
            <n-tag style="font-size: large;" round :bordered="false" type="info" size="large">
              事件热门评论
              <template #icon>
                <n-icon :component="ChatbubbleEllipses" />
              </template>
            </n-tag>
            <ul class="comments-list">
              <li v-for="comment in comments" :key="comment.id">
                <n-card size="small" :bordered="false" hoverable>
                  <template #header>
                    <span> 📖{{ comment.source }}用户：</span>
                  </template>
                  <template #header-extra>
                    {{ new Date(comment.time).toLocaleString() }}
                  </template>
                  <div>{{ comment.content }}</div>
                </n-card>
              </li>
            </ul>
          </div>
        </div>
      </section>
      <section class="bottom-section">
        <Carousel v-if="types.length > 0" :types="types" :platforms="platforms" />
      </section>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';
import eventService from '../api/eventService';
import keywordService from '../api/keywordService';
import * as echarts from 'echarts';
import 'echarts-wordcloud';
import { useRouter } from 'vue-router';
import Carousel from '../components/Carousel.vue';

import component from 'element-plus/es/components/tree-select/src/tree-select-option.mjs';
import { Planet, Earth, StatsChartSharp, Language, ChatbubbleEllipses } from '@vicons/ionicons5'


export default {
  name: 'NowadaysHotSpots',
  components: {
    Carousel,
  },
  setup() {
    const events = ref([]);
    const comments = ref([]);
    const echartsContainer = ref(null);
    const echartsInstance = ref(null);
    const router = useRouter();
    const types = ref(["微博", "小红书", "今日头条", "抖音", "知乎",  "百度"]);
    let hoverTimeout = null;

    const handleMouseOver = (eventId) => {
      hoverTimeout = setTimeout(() => {
        handleEventHover(eventId);
      }, 500);
    };

    const handleMouseLeave = () => {
      if (hoverTimeout) {
        clearTimeout(hoverTimeout);
        hoverTimeout = null;
      }
    };


    const initECharts = () => {
      const options = {
        xAxis: {
          type: 'time',
          boundaryGap: false,
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'line',
          data: [],
          smooth: true,
        }]
      };
      if (echartsContainer.value) {
        echartsInstance.value = echarts.init(echartsContainer.value);
        echartsInstance.value.setOption(options);
      }
    };

    const initWordCloud = async () => {
      const wordCloudDom = document.getElementById('wordCloud');
      const wordCloudChart = echarts.init(wordCloudDom);
      try {
        const keywords = await keywordService.getHotKeywords(20);
        const data = keywords.map(keyword => ({
          name: keyword.keyword,
          value: keyword.frequency
        }));
        const option = {
          series: [{
            type: 'wordCloud',
            shape: 'circle',
            keepAspect: false,
            left: 'center',
            top: 'center',
            width: '100%',
            height: '90%',
            right: null,
            bottom: null,
            sizeRange: [12, 60],
            rotationRange: [-90, 90],
            rotationStep: 45,
            gridSize: 8,
            drawOutOfBound: false,
            layoutAnimation: true,
            textStyle: {
              fontFamily: 'sans-serif',
              fontWeight: 'bold',
              color: function () {
                return 'rgb(' + [
                  Math.round(Math.random() * 160),
                  Math.round(Math.random() * 160),
                  Math.round(Math.random() * 160)
                ].join(',') + ')';
              }
            },
            emphasis: {
              textStyle: {
                textShadowBlur: 3,
                textShadowColor: '#333'
              }
            },
            data
          }]
        };

        wordCloudChart.on('click', function (params) {
          const { name } = params.data;
          router.push({
            name: 'keywordDetails',
            params: { keyword: name }
          });
        });
        wordCloudChart.setOption(option);

        window.addEventListener("resize", () => {
          wordCloudChart.resize();
        });
      } catch (error) {
        console.error('Failed to initialize word cloud:', error);
      }
    };

    const fetchEvents = async () => {
      try {
        const eventResponse = await eventService.getEvent(5);
        events.value = eventResponse.map((event, index) => ({
          ...event,
          rank: index + 1  // 自增rank，从1开始
        }));

        // 如果有事件，则处理第一个事件的悬停
        if (events.value.length > 0) {
          handleEventHover(events.value[0].eventId);
        }
      } catch (error) {
        console.error('获取事件排行榜数据时出错:', error);
        initECharts(); 
      }
    };


    const goToEventDetails = (eventId) => {
      router.push({
        name: 'eventDetails',
        params: { eventId }
      });
    };

    const goToLink = (link) => {
      window.open(link, '_blank');
    };

    const fetchEventEchartsData = async (eventId) => {
      try {
        const response = await eventService.getEventEchartsData(eventId);
        const data = response.map(item => ({
          time: new Date(item.time).getTime(), // 将时间转换为时间戳
          value: item.popularity // 使用popularity字段
        }));
        if (echartsInstance.value) {
          echartsInstance.value.setOption({
            series: [{
              data: data.map(item => [item.time, item.value])
            }]
          });
        }
      } catch (error) {
        console.error('获取事件时间线数据时出错:', error);
      }
    };

    const fetchEventComments = async (eventId) => {
      try {
        const response = await eventService.getEventComment(eventId, 3);
        comments.value = response.map(comment => ({
          id: comment.commentId,
          content: comment.comment,
          time: new Date().toISOString(), // 如果没有时间字段，可以用当前时间
          source: comment.source
        }));
      } catch (error) {
        console.error('获取事件评论时出错:', error);
      }
    };

    const handleEventHover = (eventId) => {
      fetchEventEchartsData(eventId);
      fetchEventComments(eventId);
    };

    onMounted(() => {
      fetchEvents();
      initECharts();
      initWordCloud();
    });

    onUnmounted(() => {
      if (echartsInstance.value) {
        echartsInstance.value.dispose();
      }
    });

    return {
      Planet, 
      ChatbubbleEllipses, 
      Language, 
      StatsChartSharp, 
      handleMouseOver,
      handleMouseLeave, 
      Earth, //从上到这里是Icon
      events, comments, types, echartsContainer, echartsInstance, goToLink, fetchEvents, goToEventDetails, handleEventHover };
  }
};
</script>

<style scoped>
.rank-circle {
  background-color: #ff691e;
  margin-right: 3px;
  color: white;
  border-radius: 100%;
  padding: 5px 12px;
  font-size: 1em;
}

.main-content {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-image: linear-gradient(45deg, rgba(255, 154, 158, 0.5) 0%, rgba(250, 208, 196, 0.5) 99%, rgba(250, 208, 196, 0.5) 100%);
}

.hot-spot-content {
  display: flex;
  justify-content: space-between;
}

.hot-spot-left, .hot-spot-right {
  flex: 1;
  margin: 10px;
  display: flex;
  flex-direction: column;
}

.event-name {
  position: relative;
  cursor: pointer;
  transition: color 0.3s ease;
}

.event-name::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -2px; 
  width: 0;
  height: 2px;
  background-color: rgb(62, 90, 227);
  transition: width 0.3s ease;
}

.event-name:hover::after {
  width: 100%;
}

.events, .hot-spot-left, .word-cloud, .hot-spot-right, .stats, .hot-spot-right, .comments {
  flex: 1;
  margin-bottom: 20px;
  padding: 20px; 
  border-radius: 30px;
}

.word-cloud, .comments, .events, .stats {
  border-top: 1px solid #ccc;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 10px rgba(0,0,0,0.05);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.7), rgba(235, 235, 255, 0.7)) ; /* 现代感渐变背景 */
  transition: backdrop-filter 0.3s ease-in-out, background-color 0.3s ease-in-out, transform 0.3s ease-in-out; /* 精细过渡效果 */
}

.word-cloud:hover, .comments:hover, .events:hover, .stats:hover {
  backdrop-filter: blur(20px);
  background: linear-gradient(to right, rgba(255, 255, 255, 0.8), rgba(235, 235, 255, 0.8)); /* 加深渐变效果 */
  transform: scale(1.03);
}


.hot-spot-left {
  width: 60vw;
}

.hot-spot-right {
  max-width: 40vw;
}

.hot-spot-left, .hot-spot-right {
  background: transparent;
}

.hot-spot-left h2, .hot-spot-right h2 {
  margin-bottom: 10px;
  font-size: 20px;
}

.comments-list {
  list-style: none;
  padding: 0;
}

.comments-list li {
  margin-bottom: 10px;
}

.stat-item {
  margin-bottom: 10px;
}

.bottom-section {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

</style>
