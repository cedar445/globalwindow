
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // 替换为你的后端 API 基础 URL
  timeout: 10000, // 设置请求超时时间
  headers: {
    'Content-Type': 'application/json',
    // 在需要时添加其他默认头部信息
  },
});

export default axiosInstance;


