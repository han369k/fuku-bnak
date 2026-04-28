import axios from 'axios'

// 建立一個 axios 實例，統一設定後端 API 的基礎 URL
const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // 跨域請求攜帶 Cookie，Session 才能正常運作
})

export default api