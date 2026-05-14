<template>
  <div class="loan-status-page">

    <!-- ── Page Header ── -->
    <div class="page-header">
      <div class="page-header-inner">
        <div class="page-title-block">
          <span class="page-icon">📋</span>
          <div>
            <h1 class="page-title">我的貸款申請</h1>
            <p class="page-subtitle">查看所有已提交的貸款申請狀態</p>
          </div>
        </div>
        <button class="btn-apply" @click="$router.push({ name: 'user-loan-apply' })">
          + 新增申請
        </button>
      </div>
    </div>

    <div class="page-body">

      <!-- ── Loading ── -->
      <div v-if="loading" class="state-block">
        <div class="skeleton-list">
          <div v-for="i in 3" :key="i" class="skel-card">
            <div class="sk sk-tag"></div>
            <div class="sk sk-line"></div>
            <div class="sk sk-short"></div>
          </div>
        </div>
      </div>

      <!-- ── Error ── -->
      <div v-else-if="error" class="state-block state-error">
        <span class="state-icon">⚠️</span>
        <div class="state-text">{{ error }}</div>
        <button class="btn-retry" @click="load">重新載入</button>
      </div>

      <!-- ── Empty ── -->
      <div v-else-if="applications.length === 0" class="state-block state-empty">
        <span class="state-icon">📂</span>
        <div class="state-text">您目前尚未有任何貸款申請紀錄</div>
        <button class="btn-apply-sm" @click="$router.push({ name: 'user-loan-apply' })">
          立即申請貸款
        </button>
      </div>

      <!-- ── List ── -->
      <div v-else class="app-list">
        <div
          v-for="app in applications"
          :key="app.applicationId"
          class="app-card"
        >
          <!-- 卡片頂部：ID + 狀態標籤 -->
          <div class="card-top">
            <span class="app-id">{{ app.applicationId }}</span>
            <span class="status-badge" :class="statusClass(app.applicationStatus)">
              {{ statusLabel(app.applicationStatus) }}
            </span>
          </div>

          <!-- 核心資訊列 -->
          <div class="card-info-grid">
            <div class="info-item">
              <span class="info-label">貸款類型</span>
              <span class="info-val">{{ loanTypeLabel(app.applyType) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">申請金額</span>
              <span class="info-val accent">$ {{ formatAmount(app.applyAmount) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">申請期數</span>
              <span class="info-val">{{ app.applyPeriod }} 個月</span>
            </div>
            <div class="info-item">
              <span class="info-label">年利率</span>
              <span class="info-val">{{ app.rate ? (app.rate * 100).toFixed(2) + ' %' : '—' }}</span>
            </div>
          </div>

          <!-- ── 聯繫狀態區塊 ── -->
          <div
            class="contact-banner"
            :class="contactBannerClass(app.latestContactStatus)"
            v-if="app.latestContactStatus"
          >
            <span class="contact-banner-icon">{{ contactIcon(app.latestContactStatus) }}</span>
            <div class="contact-banner-body">
              <span class="contact-banner-label">{{ contactLabel(app.latestContactStatus) }}</span>
              <span class="contact-banner-desc">{{ contactDesc(app.latestContactStatus) }}</span>
            </div>
            <span class="contact-banner-time" v-if="app.latestContactTime">
              {{ formatTime(app.latestContactTime) }}
            </span>
          </div>

          <!-- 卡片底部：申請時間 -->
          <div class="card-footer">
            <span class="footer-item">
              🕐 申請時間：{{ formatTime(app.createTime) }}
            </span>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/axios'

const applications = ref([])
const loading = ref(false)
const error = ref('')

// ── 貸款類型 ──
const LOAN_TYPE_MAP = {
  PERSONAL: '個人信貸',
  CAR:      '汽車貸款',
  MOTOR:    '機車貸款',
  STUDENT:  '學貸',
  BUSINESS: '創業貸款',
  HOUSE:    '房屋貸款',
  LAND:     '土地貸款',
}

// ── 申請狀態 ──
const STATUS_MAP = {
  PENDING_CONTACT: { label: '新申請（待聯繫）', cls: 'st-pending'   },
  IN_CONTACT:      { label: '聯繫中',           cls: 'st-contact'   },
  PENDING_REVIEW:  { label: '審核中',            cls: 'st-review'    },
  APPROVED:        { label: '核准撥款',          cls: 'st-approved'  },
  REJECTED:        { label: '申請拒絕',          cls: 'st-rejected'  },
  CANCELLED:       { label: '已取消',            cls: 'st-cancelled' },
  DISBURSED:       { label: '已撥款',            cls: 'st-disbursed' },
  CLOSED:          { label: '已結案',            cls: 'st-closed'    },
}

// ── 聯繫狀態：標籤 / 圖示 / 說明文字 / 顏色 ──
const CONTACT_MAP = {
  NOT_CONTACTED: {
    label:  '尚未聯繫',
    icon:   '🕐',
    desc:   '行員將盡快與您聯繫，請保持電話暢通。',
    banner: 'banner-gray',
  },
  ATTEMPTED: {
    label:  '嘗試聯繫中',
    icon:   '📲',
    desc:   '行員已嘗試聯繫您，請確認是否有未接來電或訊息。',
    banner: 'banner-blue',
  },
  REACHED: {
    label:  '已成功聯繫',
    icon:   '✅',
    desc:   '行員已與您取得聯繫，申請正在進行中。',
    banner: 'banner-green',
  },
  CONFIRMED: {
    label:  '資料已確認',
    icon:   '📄',
    desc:   '行員已確認您的申請資料，即將進入審核流程。',
    banner: 'banner-gold',
  },
  DECLINED: {
    label:  '已放棄申請',
    icon:   '🚫',
    desc:   '您已放棄此次申請，如有需要請重新提交。',
    banner: 'banner-red',
  },
}

function loanTypeLabel(type)   { return LOAN_TYPE_MAP[type] || type }
function statusLabel(st)       { return STATUS_MAP[st]?.label || st }
function statusClass(st)       { return STATUS_MAP[st]?.cls   || '' }
function contactLabel(ct)      { return CONTACT_MAP[ct]?.label  || ct }
function contactIcon(ct)       { return CONTACT_MAP[ct]?.icon   || '📋' }
function contactDesc(ct)       { return CONTACT_MAP[ct]?.desc   || '' }
function contactBannerClass(ct){ return CONTACT_MAP[ct]?.banner || 'banner-gray' }

function formatAmount(n) {
  return n != null ? Number(n).toLocaleString('zh-TW') : '—'
}

function formatTime(t) {
  if (!t) return '—'
  return new Date(t).toLocaleString('zh-TW', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit',
  })
}

// ── API ──
async function load() {
  loading.value = true
  error.value = ''
  try {
    const token = localStorage.getItem('customer_token')
    const res = await api.get('/api/loan-applications/my', {
      headers: { Authorization: `Bearer ${token}` },
    })
    applications.value = res.data.data || []
  } catch (e) {
    error.value = e.response?.data?.message || '載入失敗，請稍後再試'
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
/* ── Variables ── */
.loan-status-page {
  --accent:    #A65A4D;
  --primary:   #5C6B5F;
  --pk:        #3F4A42;
  --bg:        #F5F1EA;
  --surface:   #FDFAF6;
  --surface-2: #EAE4DA;
  --border:    #D6CEC3;
  --ink:       #2B2B2B;
  --muted:     #A89A8E;
  --muted-2:   #6E6259;

  min-height: 100vh;
  background: var(--bg);
  font-family: 'Noto Sans TC', sans-serif;
  color: var(--ink);
}

/* ── Header ── */
.page-header {
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  padding: 28px 0;
}
.page-header-inner {
  max-width: 860px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}
.page-title-block {
  display: flex;
  align-items: center;
  gap: 14px;
}
.page-icon  { font-size: 28px; line-height: 1; }
.page-title { font-size: 22px; font-weight: 700; color: var(--ink); margin: 0 0 3px; }
.page-subtitle { font-size: 13px; color: var(--muted-2); margin: 0; }

.btn-apply {
  padding: 10px 22px;
  background: var(--primary);
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
  white-space: nowrap;
}
.btn-apply:hover { background: var(--pk); }

/* ── Body ── */
.page-body {
  max-width: 860px;
  margin: 0 auto;
  padding: 32px 24px 64px;
}

/* ── States ── */
.state-block {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  padding: 64px 0;
  text-align: center;
}
.state-icon  { font-size: 40px; }
.state-text  { font-size: 15px; color: var(--muted-2); }
.btn-retry, .btn-apply-sm {
  margin-top: 4px;
  padding: 9px 22px;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-retry    { background: var(--surface-2); color: var(--muted-2); }
.btn-apply-sm { background: var(--primary); color: #fff; }
.btn-apply-sm:hover { background: var(--pk); }

/* ── Skeleton ── */
.skeleton-list { width: 100%; display: flex; flex-direction: column; gap: 16px; }
.skel-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 22px 24px;
  display: flex; flex-direction: column; gap: 12px;
}
.sk {
  background: var(--surface-2);
  border-radius: 6px;
  animation: shimmer 1.2s ease-in-out infinite alternate;
}
.sk-tag   { height: 22px; width: 80px;  border-radius: 20px; }
.sk-line  { height: 14px; width: 100%; }
.sk-short { height: 12px; width: 55%; }
@keyframes shimmer { from { opacity: 0.7; } to { opacity: 0.35; } }

/* ── App Card ── */
.app-list { display: flex; flex-direction: column; gap: 16px; }

.app-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  transition: box-shadow 0.15s;
}
.app-card:hover { box-shadow: 0 4px 20px rgba(92, 107, 95, 0.10); }

/* Top row */
.card-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}
.app-id {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 13px;
  color: var(--muted-2);
  letter-spacing: 0.04em;
}

/* Status badge */
.status-badge {
  display: inline-block;
  padding: 3px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.02em;
}
.st-pending   { background: rgba(166,140,0,0.10);   color: #7a6000; }
.st-contact   { background: rgba(55,100,180,0.10);   color: #2a5aad; }
.st-review    { background: rgba(106,80,160,0.10);   color: #5a3aad; }
.st-approved  { background: rgba(40,150,80,0.12);    color: #1a7a40; }
.st-rejected  { background: rgba(166,90,77,0.12);    color: #A65A4D; }
.st-cancelled { background: rgba(130,130,130,0.10);  color: #6e6e6e; }
.st-disbursed { background: rgba(30,160,180,0.12);   color: #107080; }
.st-closed    { background: rgba(80,80,80,0.08);     color: #555;    }

/* Info grid */
.card-info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px 16px;
}
@media (max-width: 640px) {
  .card-info-grid { grid-template-columns: 1fr 1fr; }
}
.info-item {
  background: var(--surface-2);
  border-radius: 8px;
  padding: 9px 12px;
  display: flex;
  flex-direction: column;
  gap: 3px;
}
.info-label {
  font-size: 10px;
  color: var(--muted-2);
  font-family: 'IBM Plex Mono', monospace;
  letter-spacing: 0.05em;
}
.info-val { font-size: 13px; font-weight: 600; color: var(--ink); }
.info-val.accent { color: var(--primary); font-family: 'IBM Plex Mono', monospace; }

/* ── Contact Banner ── */
.contact-banner {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 10px;
  border: 1px solid;
}
.contact-banner-icon { font-size: 20px; flex-shrink: 0; }
.contact-banner-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
}
.contact-banner-label {
  font-size: 13px;
  font-weight: 700;
  line-height: 1.3;
}
.contact-banner-desc {
  font-size: 12px;
  line-height: 1.4;
  opacity: 0.8;
}
.contact-banner-time {
  font-size: 11px;
  font-family: 'IBM Plex Mono', monospace;
  white-space: nowrap;
  opacity: 0.65;
  flex-shrink: 0;
}

/* Banner variants */
.banner-gray  { background: rgba(130,130,130,0.06); border-color: rgba(130,130,130,0.20); color: #555; }
.banner-blue  { background: rgba(55,100,180,0.07);  border-color: rgba(55,100,180,0.22);  color: #2a5aad; }
.banner-green { background: rgba(40,150,80,0.07);   border-color: rgba(40,150,80,0.22);   color: #1a7a40; }
.banner-gold  { background: rgba(140,115,60,0.08);  border-color: rgba(140,115,60,0.22);  color: #7a6000; }
.banner-red   { background: rgba(166,90,77,0.07);   border-color: rgba(166,90,77,0.22);   color: #A65A4D; }

/* Footer */
.card-footer {
  display: flex;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid var(--border);
}
.footer-item { font-size: 12px; color: var(--muted); }
</style>
