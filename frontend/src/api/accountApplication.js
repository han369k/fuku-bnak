import api from './axios'

/**
 * 客戶端：提交開戶申請（multipart/form-data）
 * @param {FormData} formData - 包含文字欄位 + 三張證件圖片
 */
export function submitAccountApplication(formData) {
  return api.post('/api/customer/account-applications', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }).then(res => res.data.data)
}

/**
 * 客戶端：查詢我的開戶申請
 */
export function getMyAccountApplications() {
  return api.get('/api/customer/account-applications')
    .then(res => res.data.data)
}
