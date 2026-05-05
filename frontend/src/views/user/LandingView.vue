<template>
  <div class="landing-wrapper">
    <!-- 動態背景 -->
    <div class="bg-animation">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
      <div class="wave wave-3"></div>
    </div>

    <!-- 主內容 -->
    <div class="landing-content">
      <div class="brand-section">
        <div class="logo-circle">
          <BankOutlined style="font-size: 48px; color: #1677ff" />
        </div>
        <h1 class="brand-title">爪哇銀行</h1>
        <p class="brand-subtitle">Java Easy Bank</p>
        <p class="brand-desc">值得信賴的數位金融夥伴，為您提供安全、便捷的網路銀行服務</p>
      </div>

      <div class="cta-section">
        <a-button
          type="primary"
          size="large"
          class="cta-btn cta-join"
          @click="$router.push('/register')"
        >
          <UserAddOutlined />
          立即加入
        </a-button>
        <a-button
          size="large"
          class="cta-btn cta-login"
          @click="$router.push('/login')"
        >
          <LoginOutlined />
          會員登入
        </a-button>
      </div>

      <!-- 特色亮點 -->
      <div class="features">
        <div class="feature-item">
          <SafetyCertificateOutlined class="feature-icon" />
          <span>銀行級加密</span>
        </div>
        <div class="feature-item">
          <ThunderboltOutlined class="feature-icon" />
          <span>即時轉帳</span>
        </div>
        <div class="feature-item">
          <MobileOutlined class="feature-icon" />
          <span>隨時隨地</span>
        </div>
      </div>

      <!-- 管理員入口 -->
      <div class="admin-entry">
        <router-link to="/admin/login" class="admin-link">
          <SettingOutlined /> 管理員入口
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  BankOutlined,
  UserAddOutlined,
  LoginOutlined,
  SafetyCertificateOutlined,
  ThunderboltOutlined,
  MobileOutlined,
  SettingOutlined,
} from '@ant-design/icons-vue'
</script>

<style scoped>
.landing-wrapper {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%);
  position: relative;
  overflow: hidden;
}

/* 波紋動畫背景 */
.bg-animation {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.wave {
  position: absolute;
  bottom: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  border-radius: 45%;
  opacity: 0.06;
  animation: wave-rotate 20s linear infinite;
}

.wave-1 { background: #667eea; animation-duration: 18s; }
.wave-2 { background: #764ba2; animation-duration: 22s; animation-delay: -5s; }
.wave-3 { background: #1677ff; animation-duration: 26s; animation-delay: -10s; }

@keyframes wave-rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.landing-content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 40px 32px;
  max-width: 500px;
}

.logo-circle {
  width: 96px;
  height: 96px;
  margin: 0 auto 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(12px);
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.15);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.brand-title {
  font-size: 42px;
  font-weight: 700;
  color: #fff;
  margin: 0;
  letter-spacing: 6px;
}

.brand-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.5);
  margin: 4px 0 0;
  letter-spacing: 4px;
  text-transform: uppercase;
}

.brand-desc {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.65);
  margin: 20px 0 0;
  line-height: 1.6;
}

.cta-section {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-top: 36px;
}

.cta-btn {
  min-width: 160px;
  height: 48px;
  font-size: 16px;
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
}

.cta-join {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.4);
}

.cta-join:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 28px rgba(102, 126, 234, 0.6);
}

.cta-login {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #fff;
  backdrop-filter: blur(8px);
}

.cta-login:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
  color: #fff;
  transform: translateY(-2px);
}

.features {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 48px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.55);
  font-size: 13px;
}

.feature-icon {
  font-size: 24px;
  color: rgba(255, 255, 255, 0.4);
}

.admin-entry {
  margin-top: 48px;
}

.admin-link {
  color: rgba(255, 255, 255, 0.3);
  font-size: 12px;
  text-decoration: none;
  transition: color 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.admin-link:hover {
  color: rgba(255, 255, 255, 0.6);
}
</style>
