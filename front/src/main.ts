import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'  
import ElementPlus from 'element-plus'
//import 'element-plus/dist/index.css'

router.beforeEach((to, from, next) => {
    const waifuElement = document.getElementById('waifu');
    if (waifuElement) {
        if (to.path === '/main') {
            waifuElement.style.display = 'none';
        }
    }
    next();
    
    // 检查是否从 '/main' 路由跳转到其他路由
    if (from.path === '/main' && to.path !== '/main') {
        window.location.assign(to.fullPath); // 使用 window.location.assign 以保留历史记录
    } else {
        next(); // 继续路由跳转
    }
});




const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.mount('#app')


