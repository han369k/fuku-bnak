<template>
  <div class="page-container">
    <a-page-header title="客戶信用評分詳情" @back="router.back()">
      <template #extra>
        <a-tag :color="riskColor(credit?.riskLevel)">
          {{ riskLabel(credit?.riskLevel) }}
        </a-tag>
      </template>
    </a-page-header>

    <a-alert
      message="資料敏感提醒"
      description="此頁面顯示客戶完整信用評分，查看行為已記錄。請勿截圖或外流。"
      type="error"
      show-icon
      style="margin-bottom: 16px"
    />

    <a-spin :spinning="loading">
      <a-descriptions
        title="基本資訊"
        bordered
        :column="2"
        size="small"
        style="margin-bottom: 20px"
      >
        <a-descriptions-item label="客戶 ID">{{ credit?.customerId }}</a-descriptions-item>
        <a-descriptions-item label="客戶姓名">{{ credit?.customerName }}</a-descriptions-item>
        <a-descriptions-item label="風險等級">
          <a-tag :color="riskColor(credit?.riskLevel)">
            {{ riskLabel(credit?.riskLevel) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="最後更新">
          {{ credit?.lastUpdatedAt }}
        </a-descriptions-item>
      </a-descriptions>

      <a-descriptions title="評分詳情" bordered :column="2" size="small">
        <a-descriptions-item label="綜合評分">
          <span style="font-size: 20px; font-weight: bold; color: #1890ff">
            {{ credit?.finalScore }}
          </span>
          分
        </a-descriptions-item>
        <a-descriptions-item label="聯徵分數">
          {{ credit?.externalScore ?? '—' }}
        </a-descriptions-item>
        <a-descriptions-item label="職業">
          {{ occupationLabel(credit?.occupation) }}
        </a-descriptions-item>
        <a-descriptions-item label="年收入">
          {{
            credit?.annualIncome ? '$ ' + Number(credit.annualIncome).toLocaleString('zh-TW') : '—'
          }}
        </a-descriptions-item>
        <a-descriptions-item label="他行負債">
          {{
            credit?.otherBankDebt
              ? '$ ' + Number(credit.otherBankDebt).toLocaleString('zh-TW')
              : '—'
          }}
        </a-descriptions-item>
        <a-descriptions-item label="有無不動產">
          {{ credit?.hasRealEstate ? '是' : '否' }}
        </a-descriptions-item>
        <a-descriptions-item label="PEP 人士">
          <a-tag :color="credit?.isPep ? 'red' : 'default'">
            {{ credit?.isPep ? '是' : '否' }}
          </a-tag>
        </a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const BASE_URL = '/api/risk/creditscore'

const credit = ref(null)
const loading = ref(false)

async function fetchDetail() {
  loading.value = true
  try {
    const res = await axios.get(`${BASE_URL}/${route.params.customerId}`, {
      withCredentials: true,
    })
    credit.value = res.data.data
  } catch (e) {
    message.error('載入失敗：' + (e.response?.data?.message || e.message))
  } finally {
    loading.value = false
  }
}

function riskLabel(r) {
  return { HIGH: '高風險', MEDIUM: '中風險', LOW: '低風險' }[r] || r
}

function riskColor(r) {
  return { HIGH: 'red', MEDIUM: 'orange', LOW: 'green' }[r] || 'default'
}

function occupationLabel(o) {
  return (
    {
      LEGISLATOR_MANAGER: '民意代表／高階主管',
      PROFESSIONAL: '專業人員',
      TECHNICIAN: '技術員及助理專業人員',
      CLERICAL: '事務支援人員',
      SERVICE_SALES: '服務及銷售工作人員',
      AGRICULTURAL: '農林漁牧業生產人員',
      CRAFT_WORKER: '技藝有關工作人員',
      MACHINE_OPERATOR: '機械設備操作及組裝人員',
      ELEMENTARY: '基層技術工及勞力工',
      MILITARY: '軍人',
      NONE: '無',
      OTHER: '其他',
    }[o] || o
  )
}

onMounted(fetchDetail)
</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
