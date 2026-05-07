<template>
  <a-layout class="admin-layout">
    <a-layout-sider
      v-model:collapsed="collapsed"
      collapsible
      :width="260"
      theme="light"
      style="position: fixed; left: 0; top: 0; bottom: 0; z-index: 100"
    >
      <div class="logo-container">
        <BankFilled class="logo-icon" />
        <span v-if="!collapsed" class="logo-text">爪哇銀行</span>
      </div>

      <a-menu theme="light" mode="inline" :selected-keys="selectedKeys">
        <a-menu-item key="admin-home" @click="$router.push('/admin')">
          <HomeOutlined />
          <span>首頁</span>
        </a-menu-item>
        
        <!-- 業務操作：資安部 (DPT005) 隱藏 -->
        <template v-if="authStore.user?.deptId !== 'DPT005'">
          <a-menu-item key="admin-accounts" @click="$router.push('/admin/accounts')">
            <BankOutlined />
            <span>帳戶管理</span>
          </a-menu-item>
          <a-menu-item key="admin-transfers" @click="$router.push('/admin/transfers')">
            <SwapOutlined />
            <span>交易操作</span>
          </a-menu-item>
          <a-menu-item key="admin-trans-logs" @click="$router.push('/admin/trans-logs')">
            <SwapOutlined />
            <span>交易紀錄</span>
          </a-menu-item>
          <a-menu-item key="admin-loan-apply" @click="$router.push('/admin/loan-apply')">
            <FileTextOutlined />
            <span>貸款申請(測試)</span>
          </a-menu-item>
          <a-menu-item key="admin-loan-applications" @click="$router.push('/admin/loan-applications')">
            <FileTextOutlined />
            <span>貸款申請管理</span>
          </a-menu-item>
        </template>

        <!-- 系統權限與管理：資深資安人員或特定管理員可見 -->
        <a-menu-item key="admin-employees" @click="$router.push('/admin/employees')">
          <TeamOutlined />
          <span>員工管理</span>
        </a-menu-item>
        
        <a-menu-item key="admin-customers" @click="$router.push('/admin/customers')">
          <UserOutlined />
          <span>客戶管理</span>
        </a-menu-item>

        <template v-if="authStore.user?.deptId !== 'DPT005'">
          <a-menu-item key="admin-risk-events" @click="$router.push('/admin/risk-events')">
            <LineChartOutlined />
            <span>風險事件</span>
          </a-menu-item>
          <a-menu-item key="admin-blacklist" @click="$router.push('/admin/blacklist')">
            <FileTextOutlined />
            <span>黑名單</span>
          </a-menu-item>
          <a-menu-item key="admin-card-types" @click="$router.push('/admin/card-types')">
            <FileTextOutlined />
            <span>卡別管理</span>
          </a-menu-item>
          <a-menu-item key="admin-card-applications" @click="$router.push('/admin/card-applications')">
            <FileTextOutlined />
            <span>信用卡審核</span>
          </a-menu-item>
          <a-menu-item key="admin-cards" @click="$router.push('/admin/cards')">
            <FileTextOutlined />
            <span>卡片管理</span>
          </a-menu-item>
        </template>

        <!-- 系統稽核：僅資安部 (DPT005) 可見 -->
        <a-menu-item v-if="authStore.user?.deptId === 'DPT005'" key="admin-logs" @click="$router.push('/admin/logs')">
          <SettingOutlined />
          <span>系統設定</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <a-layout :style="{ marginLeft: collapsed ? '80px' : '260px', transition: 'margin-left 0.2s' }">
      <a-layout-header class="custom-header">
        <div class="header-search"></div>
        <!-- 使用者資訊 & 登出 -->
        <div v-if="authStore.isLoggedIn" class="header-right">
          <a-tag class="custom-role-tag">
            {{ authStore.user?.roleCode || 'ROLE' }}
          </a-tag>
          <span class="user-name">{{ authStore.user?.empName || '員工姓名' }}</span>
          <a-button shape="round" class="logout-btn" @click="handleLogout">
            <LogoutOutlined /> 登出
          </a-button>
        </div>
      </a-layout-header>
      <a-layout-content class="admin-content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  HomeOutlined,
  BankOutlined,
  SwapOutlined,
  FileTextOutlined,
  TeamOutlined,
  UserOutlined,
  LogoutOutlined,
  AuditOutlined,
  LineChartOutlined,
  SettingOutlined,
  BankFilled
} from '@ant-design/icons-vue'
import { logout } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const collapsed = ref(false)
const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const selectedKeys = computed(() => [route.name])

async function handleLogout() {
  try {
    await logout()
  } catch {
    // 即使後端登出失敗，前端也要清除狀態
  }
  authStore.clearUser()
  message.success('已登出')
  router.push({ name: 'admin-login' })
}
</script>

<style scoped>
/* 側邊欄整體容器 */
:deep(.ant-layout-sider) {
  background-color: #e9ecef !important; /* 淺灰色背景 */
  padding: 20px 15px;
}

/* 隱藏選單預設的右邊框 */
:deep(.ant-menu) {
  background: transparent !important;
  border-inline-end: none !important;
}

/* 每一項選單的基礎樣式 */
:deep(.ant-menu-item) {
  height: 48px !important;
  line-height: 48px !important;
  margin-bottom: 8px !important;
  color: #4a4a4a !important; /* 未選中時的文字顏色 */
  font-weight: 500;
  border-radius: 24px !important; /* hover 效果圓角 */
  transition: all 0.3s ease;
}

/* 選中狀態：墨綠色膠囊樣式 */
:deep(.ant-menu-item-selected) {
  background-color: #5C6B5F !important; /* 墨綠色背景 */
  color: #ffffff !important;           /* 白色文字 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 確保選中時圖示也是白色的 */
:deep(.ant-menu-item-selected .anticon) {
  color: #ffffff !important;
}

/* 圖示與文字的間距調整 */
:deep(.ant-menu-title-content) {
  margin-inline-start: 12px !important;
  font-size: 15px;
}

/* LOGO 區塊美化 */
.logo-container {
  padding: 0 16px 32px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  font-size: 24px;
  color: #5C6B5F; /* 改成我們的墨綠色 */
}

.logo-text {
  font-weight: 700;
  font-size: 18px;
  letter-spacing: 0.5px;
  color: #4A574D; /* 用稍微深一點的墨綠色作為標題字，比純黑更好看 */
}

/* =========================================
   頂部導覽列加大設計
========================================= */
.admin-layout {
  min-height: 100vh;
}

.custom-header {
  height: 80px !important; 
  line-height: 80px !important;
  padding: 0 32px !important;
  background: transparent !important;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 徹底移除藍色 Hover，統一改為墨綠色 */
.custom-header a:hover,
.custom-header .anticon:hover {
  color: #5C6B5F !important;
}

.custom-header *:focus {
  outline: none !important;
  box-shadow: none !important;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px; /* 元素之間的距離拉開 */
  /* 強制將邊框改為實線，消除虛線 */
  border-style: solid !important;
  border-color: transparent !important;
}

/* 墨綠色質感標籤 */
.custom-role-tag {
  background-color: rgba(92, 107, 95, 0.1) !important;
  color: #5C6B5F !important;
  border: 1px solid rgba(92, 107, 95, 0.3) !important;
  font-size: 14px !important;
  font-weight: 600;
  padding: 4px 12px !important;
  border-radius: 6px !important;
  margin: 0;
}

/* 員工姓名變大 */
.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

/* 登出按鈕微調變大 */
.logout-btn {
  height: 38px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
  border-color: #d9d9d9;
  color: #333;
}
.logout-btn:hover {
  border-color: #5C6B5F;
  color: #5C6B5F;
}

.admin-content {
  padding: 0 32px 32px;
  overflow-y: auto;
}
</style>
