<template>
  <div class="wholePage">
    <nav class="navbar">
      <div>
        <img src="../assets/NavBar/logo.png" alt="Logo" class="navbar-logo">
      </div>
      <!-- <div class="menu-icon" @click="toggleMenu">
        <img src="../assets/NavBar/menuIcon.png" alt="Menu" class="menu-back">
      </div> -->
      <div class="title" @click="gotoMain">寰球视窗GlobalWindow</div>
      <!-- <div class="Nav" v-show="isMenuOpen || isDesktop"> -->
      <div class="right">
        <div class="Nav" v-show="isMenuOpen || isDesktop">
          <ul>
            <li id="Main">
              <router-link to="/main" active-class="active">
              <!-- <n-tag checkable :color="{ color: '##007BFF'}"> -->
                首页
                <!-- <template #icon>
                  <n-icon :component="Earth" />
                </template>
              </n-tag>  -->
              </router-link>
            </li>
            <li id="HPNowadays"><router-link to="/nav/nowadaysHotSpots" active-class="active">当期热点</router-link></li>
            <li id="HP"><router-link to="/nav/recentEvents" active-class="active">热点专题</router-link></li>
            <li id="HeadLine"><router-link to="/nav/importantHeadlines" active-class="active">重要头条</router-link></li>
            <li id="About"><router-link to="/nav/about" active-class="active">关于我们</router-link></li>
          </ul>
        </div>
        <div class="search-button">
          <img src="../assets/NavBar/searchIcon.png" alt="搜索" class="search-back" @click="goToSearch">
        </div>
      </div>
    </nav>
    <router-view></router-view>
  </div>
</template>


<script>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Earth, StatsChartSharp, Language } from '@vicons/ionicons5'


export default {
  name: 'NavBar',
  setup() {
    const router = useRouter();
    const isMenuOpen = ref(false);
    const toggleMenu = () => {
      isMenuOpen.value = !isMenuOpen.value;
    };
    const goToSearch = () => {
      router.push('/nav/searcher');
    };
    const gotoMain = () => {
      router.push('/main');
    };
    const isDesktop = computed(() => window.innerWidth > 768);
    window.addEventListener('resize', () => {
      isMenuOpen.value = window.innerWidth > 768;
    });
    return { Earth, StatsChartSharp, Language, goToSearch, toggleMenu, isMenuOpen, isDesktop, gotoMain};
  }
}
</script>


<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.wholePage {
  flex-direction: column;
  height: 70px; 
  width: 100%;
}

.menu-icon {
  display: none; /* 默认隐藏，小屏幕时显示 现在暂不使用*/
  cursor: pointer;
  padding: 10px;
}

@media (max-width: 768px) {
  .menu-icon {
    display: block; /* 小屏幕时显示菜单图标 */
  }

  .Nav {
    display: none; /* 默认不显示，通过Vue控制显示 */
    flex-direction: column; /* 垂直排列导航项 */
    align-items: flex-start; /* 左对齐导航项 */
  }

  .title, .navbar-logo {
    display: none;
  }
}

.title{
  font-size: 1.5em;
  font-weight: bold;
  color: #ff6f3c;
  justify-content: left;
  margin-left: 1rem;
  margin-right: 20rem;
}

.title:hover {
  cursor: pointer;
}

.Nav {
  display: flex;
  justify-content: right;
  flex-direction: column;
  align-items: flex-end;
}

.navbar {
  display: flex;
  position: relative;
  align-items: center;
  width: 100%;
  height: 70px;
  top: 0;
  left: 0;
  background-color: #f8f9fa; 
  color: black; 
  box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
  z-index: 1000;
  justify-content: space-between; 
}

.navbar-logo {
  margin-left: 50px;
  height: 70px;
}

.navbar ul {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 18px;
}

.navbar a {
  text-decoration: none;
  padding: 0.8em 1.2em;
  display: block;
  transition: background-color 0.3s ease, color 0.3s ease;
  color: #f07b3f;
}

.navbar a:hover,
.navbar a.active {
  background-color: #007BFF;
  color: #fff;
}

.right {
  display: flex;
  align-items: center;
  justify-content: flex-end; /* 确保内容靠右显示 */
  flex-grow: 1; /* 让它占据多余的空间 */
}

.search-button {
  width: 2.3rem;
  height: 2.3rem;
  margin-left: 20px;
  background-color: #346ca8;
  position: relative;
  border-radius: 100%;
  border: none;
  cursor: pointer;
  margin-right: 50px;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #0056b3;
}

.search-back {
  width: 1.5rem;
  height: 1.5rem;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

</style>
