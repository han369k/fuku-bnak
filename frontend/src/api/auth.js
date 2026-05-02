import api from './axios'

// === 統一管理 Auth 的 API 設定 ===

// 員工登入
export function login(data) {
  return api.post('/api/auth/login', data)
}

// 確認登入狀態（給路由守衛用）
export function checkAuth() {
  return api.get('/api/auth/me')
}

// 登出
export function logout() {
  return api.post('/api/auth/logout')
}

// 查詢員工（支援 keyword 模糊搜尋）
// GET /api/auth/employees          → 全部
// GET /api/auth/employees?keyword=林 → 姓名含「林」的
export function getEmployees(keyword) {
  return api.get('/api/auth/employees', {
    params: keyword ? { keyword } : {},
  })
}

// 新增員工
export function createEmployee(data) {
  return api.post('/api/auth/employees', data)
}

// 修改員工
export function updateEmployee(empId, data) {
  return api.put(`/api/auth/employees/${empId}`, data)
}

// 停用員工（軟刪除）
export function suspendEmployee(empId) {
  return api.delete(`/api/auth/employees/${empId}/suspend`)
}

// 一鍵帶入測試資料
export function seedEmployees() {
  return api.post('/api/auth/employees/seed')
}
