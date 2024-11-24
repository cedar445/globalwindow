import { createRouter, createWebHistory } from 'vue-router'

import AboutVue from '../views/About.vue'
import ImportantHeadlinesVue from '../views/ImportantHeadlines.vue'
import LoadingVue from '../views/Loading.vue'
import MainPageVue from '../views/MainPage.vue'
import NowadaysHotSpotsVue from '../views/NowadaysHotSpots.vue'
import RecentEventsVue from '../views/RecentEvents.vue'
import HotSpotsDeatilsVue from '../views/HotSpotsDetails.vue'
import SearcherVue from '../views/Searcher.vue'
import SearchPage from '../views/SearchPage.vue'
import EventDetailsVue from '../views/EventDetails.vue'
import NavBar from '../components/NavBar.vue'
import Carousel from '../components/Carousel.vue'
import NewsDetails from '../views/NewsDetails.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Loading',
      component: LoadingVue,
    },
    {
      path: '/main',
      name: 'MainPage',
      component: MainPageVue,
    },
    {
      path: '/nav',
      name: 'Nav',
      component: NavBar,
      redirect: '/nav/main',
      meta: { requiresAuth: true },
      children: [
        {
          path: 'about',
          component: AboutVue,
          meta: { allowScroll: false }
        },
        {
          path: 'importantHeadlines',
          component: ImportantHeadlinesVue,
          meta: { allowScroll: false }
        },
        {
          path: 'nowadaysHotSpots',
          name: 'Nowadays',
          component: NowadaysHotSpotsVue,
          meta: { allowScroll: true }
        },
        {
          path: 'recentEvents',
          component: RecentEventsVue,
          meta: { allowScroll: false }
        },
        {
          path: 'hotSpotsdetails/:keyword',
          component: HotSpotsDeatilsVue,
          name:'keywordDetails',
          props: route=>({
            keyword: route.params.keyword
          })
        },
        {
          path: 'eventdetails/:eventId',
          name: 'eventDetails',
          component: EventDetailsVue,
          props: route => ({
            eventId: route.params.eventId
          })
        },
        {
          path: 'newsdetails/:newsId',
          name: 'newsDetails',
          component: NewsDetails,
          props: route => ({
            newsId: route.params.newsId
          })
        },
        {
          path: 'searcher',
          component: SearcherVue,
          meta: { allowScroll: false }
        },
        {
          path:'searchPage',
          name: 'SearchPage',
          component: SearchPage,
          props: true,
          meta: { allowScroll: true }
        },
        {
          path: 'carousel',
          component: Carousel
        }
      ]  
    },
  ],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  }
})

export default router