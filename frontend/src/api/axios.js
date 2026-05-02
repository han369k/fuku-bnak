import axios from 'axios'
import router from '@/router'

export const BASE_URL = 'http://localhost:8080'

// 建立一個 axios 實例，統一設定後端 API 的基礎 URL
const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // ← 帶上 Session Cookie，Spring Security 才認得
})

// 回應攔截器：遇到 401 自動跳轉登入頁
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Session 過期 → 清掉前端登入狀態
      localStorage.removeItem('auth_user')
      // 避免在登入頁面重複跳轉
      if (router.currentRoute.value.name !== 'admin-login') {
        router.push({ name: 'admin-login' })
      }
    }
    return Promise.reject(error)
  },
)

export default api
