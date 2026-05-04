<template>
  <div class="login-wrapper">
    <div class="login-card">
      <!-- Logo 區 -->
      <div class="login-header">
        <div class="logo-icon">
          <BankOutlined style="font-size: 36px; color: #1677ff" />
        </div>
        <h1 class="login-title">爪哇銀行</h1>
        <p class="login-subtitle">管理端登入系統</p>
      </div>

      <!-- 登入表單 -->
      <a-form
        :model="form"
        layout="vertical"
        @finish="handleLogin"
        autocomplete="off"
      >
        <a-form-item
          label="電子信箱"
          name="email"
          :rules="[{ required: true, message: '請輸入電子信箱' }, { type: 'email', message: '信箱格式不正確' }]"
        >
          <a-input
            v-model:value="form.email"
            size="large"
            placeholder="請輸入員工信箱"
            @press-enter="handleLogin"
          >
            <template #prefix>
              <MailOutlined style="color: rgba(0, 0, 0, 0.25)" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item
          label="密碼"
          name="password"
          :rules="[{ required: true, message: '請輸入密碼' }]"
        >
          <a-input-password
            v-model:value="form.password"
            size="large"
            placeholder="請輸入密碼"
            @press-enter="handleLogin"
          >
            <template #prefix>
              <LockOutlined style="color: rgba(0, 0, 0, 0.25)" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            block
            :loading="loading"
          >
            登入
          </a-button>
        </a-form-item>
      </a-form>

      <!-- 快速登入 -->
      <div class="quick-login">
        <a-divider style="margin: 8px 0 16px">
          <span style="color: #999; font-size: 12px">快速填入測試帳號</span>
        </a-divider>
        <div class="quick-btn-grid">
          <a-button
            v-for="acc in testAccounts"
            :key="acc.email"
            size="small"
            @click="fillAccount(acc)"
            class="quick-btn"
          >
            <span class="quick-role">{{ acc.role }}</span>
            <span class="quick-name">{{ acc.name }}</span>
          </a-button>
        </div>
        <p style="color: #bbb; font-size: 11px; text-align: center; margin-top: 10px">
          密碼皆為 123456
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { BankOutlined, MailOutlined, LockOutlined } from '@ant-design/icons-vue'
import { login } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)

const form = reactive({
  email: '',
  password: '',
})

// 測試帳號（對應 auth_role 表）
const testAccounts = [
  { name: '鄭文華', role: 'CISO',  email: 'wenhua.cheng@javabank.com' },
  { name: '郭建國', role: 'ISSA',  email: 'chienkuo.kuo@javabank.com' },
  { name: '林家豪', role: 'CFSO',  email: 'chiahao.lin@javabank.com' },
  { name: '蔡欣妤', role: 'CFDM',  email: 'xinyu.tsai@javabank.com' },
  { name: '陳建志', role: 'CSVO',  email: 'chienchi.chen@javabank.com' },
  { name: '吳承翰', role: 'JCRO',  email: 'chenghan.wu@javabank.com' },
  { name: '王俊傑', role: 'CRO',   email: 'chunchie.wang@javabank.com' },
  { name: '許志豪', role: 'COO',   email: 'chihhao.hsu@javabank.com' },
]

function fillAccount(acc) {
  form.email = acc.email
  form.password = '123456'
}

async function handleLogin() {
  if (!form.email || !form.password) return

  loading.value = true
  try {
    const res = await login({
      email: form.email,
      password: form.password,
    })

    // res.data = ApiResponse { success, data, message }
    const userData = res.data.data
    authStore.setUser(userData)

    message.success(`歡迎回來，${userData.empName}！`)
    router.push({ name: 'admin-home' })
  } catch (err) {
    message.error(err.response?.data?.message || '登入失敗，請檢查帳號密碼')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrapper {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 40px 32px 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-icon {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e6f4ff;
  border-radius: 50%;
}

.login-title {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #1a1a2e;
  letter-spacing: 2px;
}

.login-subtitle {
  margin: 4px 0 0;
  font-size: 14px;
  color: #8c8c8c;
}

.quick-login {
  margin-top: 8px;
}

.quick-btn-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}

.quick-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  text-align: left;
  padding: 4px 10px;
  height: auto;
}

.quick-role {
  font-size: 11px;
  color: #1677ff;
  font-weight: 600;
  min-width: 36px;
}

.quick-name {
  font-size: 12px;
  color: #555;
}
</style>