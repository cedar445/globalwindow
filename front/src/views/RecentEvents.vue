<template>
  <n-layout style="padding-left: 5%; padding-right: 5%; height: 100vh " embedded>
    <n-layout has-sider style="height: 100%">
      <n-layout-sider 
        bordered
        width="15vw" 
        style="min-width: 200px; max-width: 300px">
        <n-menu
          v-model:value="selectedCategory"
          @update:value="handleSelect"
          vertical
          :options="menuOptions"
          style="height: 100%; overflow: auto; font-size: 20px">
        </n-menu>
      </n-layout-sider>
      <n-layout-content :native-scrollbar="false">
        <div v-for="event in processedEvents" :key="event.newsId">
          <n-card embedded hoverable style="margin-bottom: 16px; max-width: 95%; margin-right: auto; margin-left: auto">
            <template #header>
              <div>
                <span class="rank-circle">{{ event.rank }}</span>
                {{ event.title }}
              </div>
            </template>
            <div>
              <p v-html="event.cleanedNews"></p>
              <n-button style="width:auto; padding-left: 15px; padding-right: 15px;" strong secondary circle type="success" @click="goToNewsDetails(event.newsId)">查看详情</n-button>
              <n-button style="margin-left: 2%; width: auto; padding-left: 15px; padding-right: 15px;" type="error" circle ghost class="event-popularity">热度指数: {{ event.popularity }}</n-button>
            </div>
          </n-card>
        </div>
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import router from '../router';
import { NIcon } from 'naive-ui'
import newsService from '../api/newsService'; // 导入配置好的 newsService 实例
import mainIcon from '../components/icons/mainIcon.vue';
import policyIcon from '../components/icons/policyIcon.vue';
import economyIcon from '../components/icons/economyIcon.vue';
import playIcon from '../components/icons/playIcon.vue';
import scienceIcon from '../components/icons/scienceIcon.vue';
import whuIcon from '../components/icons/whuIcon.vue';
import otherIcon from '../components/icons/otherIcon.vue';

export default {
  name: 'HotSpot',
  setup() {
    const selectedCategory = ref('综合');
    const events = ref([]);
    const categories = ['综合', '社会新闻', '经济', '娱乐', '科技', '武大'];

    function renderIcon (icon) {
      return () => h(NIcon, null, { default: () => h(icon) })
    }

    // 正则表达式来匹配第一个被"#"包围的词
    function findFirstHashTag(htmlString) {
      const hashtagRegex = /#([^#]+)#/;
      const match = htmlString.match(hashtagRegex);
      return match ? match[1] : null;
    }

    // 去除所有被"#"包围的数据
    function removeHashTags(htmlString) {
      return htmlString.replace(/#[^#]+#/g, '').trim();
    }

    const processedEvents = computed(() => {
      return events.value.map((event, index) => ({
        ...event,
        rank: index + 1, // 保持排名为单独的属性
        title: findFirstHashTag(event.news), // 仅标题文本
        cleanedNews: removeHashTags(event.news)
      }));
    });

    const menuOptions = computed(() => {
      return categories.map((category, index) => ({
        label: category,
        key: category,
        icon: (() => {
          const icons = [
            renderIcon(mainIcon),
            renderIcon(policyIcon),
            renderIcon(economyIcon),
            renderIcon(playIcon),
            renderIcon(scienceIcon),
            renderIcon(whuIcon),
            renderIcon(otherIcon)
          ];
          return icons[index];
        })()
      }));
    });

    const filteredEvents = computed(() => {
      return events.value.filter(event => event.category === selectedCategory.value);
    });

    const handleSelect = async (value) => {
      selectedCategory.value = value;
      await fetchEventsByType(selectedCategory.value);
    };

    const fetchEventsByType = async (category) => {
      try {
        let response;
        if (category === '综合') {
          response = await newsService.getNewsByPopularity(5);
        } else {
          response = await newsService.getNewsByType(5, category);
        }
        events.value = response.map(item => ({ ...item, category }));
      } catch (error) {
        console.error('获取事件数据时出错:', error);
        events.value = [];
      }
    };

    const goToNewsDetails = (newsId) => {
      router.push({
        name: 'newsDetails',
        params: { newsId }
      });
    };

    onMounted(() => {
      fetchEventsByType(selectedCategory.value);
    });

    return { processedEvents, selectedCategory, events, filteredEvents, handleSelect, goToNewsDetails, categories, menuOptions };
  }
};
</script>

<style scoped>

.n-layout {
  height: 100vh; /* 使布局填满视窗高度 */
}

.rank-circle {
  background-color: #ff691e;
  color: white;
  border-radius: 100%;
  padding: 5px 12px;
  font-size: 1.2em;
}

.n-layout-content {
  overflow-y: auto;
}
</style>
