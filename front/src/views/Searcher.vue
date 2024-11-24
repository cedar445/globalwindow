<template>
  <div class="MainText">GlobalWindow</div>
  <div class="searchbar">
    <img src="../assets/NavBar/searchIcon.png" class="mg">
    <input class="input" type="text" id="search" v-model="keyword"
           @focus="clearKeyword" @blur="resetKeyword"
           @keypress.enter="SearchPage" />
    <button class="search-button" @click="SearchPage" >
      搜索
    </button>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';


export default {
  setup() {
    const keyword = ref('请输入想查询的关键词');
    const router = useRouter();

    const clearKeyword = () => {
      if (keyword.value === '请输入想查询的关键词') {
        keyword.value = '';
      }
    };

    const resetKeyword = () => {
      if (keyword.value.trim() === '') {
        keyword.value = '请输入想查询的关键词';
      }
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

    return { keyword, clearKeyword, resetKeyword, SearchPage };
  }
}
</script>

<style scoped>
#search { 
  flex-grow: 1;
  height: 40px;
  border: none;    /*取消默认的边框以设置自定义边框*/
  outline: none;   /*取消浏览器默认的蓝光边框以设置自定义的输入框*/
  font-size: 20px;        
  color: rgb(112, 112, 112);
  margin-left: 10px;
  text-align: left;
}

.MainText {
  font-weight: bold;
  font-size: 10vh;
  text-align: center;
  margin-top: 7%;
  font-family:'Times New Roman', Times, serif;
  background-image: linear-gradient(to right, #ff8177 0%, #ff867a 0%, #ff8c7f 21%, #f99185 52%, #cf556c 78%, #b12a5b 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.searchbar {        
  border: #f0f0f0 solid 1px;
  border-radius: 2em;
  width: 700px; 
  height: 50px;
  box-shadow: 0px 0px 5px rgb(212, 212, 212);
  margin: 0 auto;
  margin-top: 4%;
  display: flex;  /* 使搜索栏的内容水平对齐 */
  align-items: center;  /* 垂直居中 */
}

.input {
  background-color: #f0f0f0;
  flex: 1;  /* 使输入框占据剩余空间 */
}

.searchbar:hover {
  border: rgb(0, 151, 255) solid 1px;
  box-shadow: 0px 0px 5px rgb(0, 151, 255);
}

.mcp {
  height: 35px;
}

.mg {
  height: 30px;
  margin-left: 15px;
}
#search, .mcp, .mg {
  vertical-align: middle;
}

.search-button {
  background: none;
  border: none;
  cursor: pointer;
  outline: none;
  font-size: 20px;  /* 增大按钮字体大小 */
  color: orange;  /* 将按钮文字颜色设置为橘色 */
  margin-right: 15px;
  padding: 0 15px;  /* 添加内边距使按钮更容易点击 */
}
</style>
