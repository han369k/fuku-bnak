<script setup>
import { ref, onMounted } from 'vue'
import { getMyCards,activateCard } from '@/api/userCard'
import { BASE_URL } from '@/api/axios'

const cards = ref([])

const fetchCards = async () => {
  try {
    const response = await getMyCards()
    console.log(response)
    cards.value = response
  } catch (error) {
    console.error('Failed to fetch cards:', error)
  }
}

const getImageUrl = (url) => {
  if (!url) return ''
  return url.startsWith('http')
    ? url
    : `${BASE_URL}/${url}`
}
const handleActivateCard = async (cardId) => {
  try {
    await activateCard(cardId)

    alert('開卡成功!')

    fetchCards()
  } catch (error) {
    console.error('Failed to activate card:', error)
  }
}

onMounted(() => {
  fetchCards()
})
</script>

<template>
  <div class="page">
    <h1 class="page-title">My Cards</h1>

    <div class="cards-grid">
      <div
        class="card-wrapper"
        v-for="card in cards"
        :key="card.cardId"
      >
        <!-- 卡片上方資訊列 -->
        <div class="card-header">
          <!-- 狀態 -->
          <span
            class="status-badge"
            :class="card.status?.toLowerCase()"
          >
            {{ card.status }}
          </span>

          <!-- 開卡按鈕 -->
          <button
            v-if="card.status === 'INACTIVE'"
            class="activate-btn"
            @click="handleActivateCard(card.cardId)"
          >
            開卡
          </button>
        </div>

        <!-- 信用卡 -->
        <div class="credit-card">
          <!-- 卡片背景 -->
          <img
            class="card-bg"
            :src="getImageUrl(card.cardType?.cardImageUrl)"
            alt=""
          />

          <!-- 黑色遮罩 -->
          <div class="card-overlay"></div>

          <!-- 卡片內容 -->
          <div class="card-content">
            <div class="card-brand">
              {{ card.cardType?.brand }}
            </div>

            <div class="card-type">
              {{ card.cardType?.cardTypeName }}
            </div>

            <div class="card-number">
              {{ card.cardNumber }}
            </div>

            <div class="card-footer">
              <div>
                <span class="label">VALID THRU</span>
                <div>{{ card.expiryDate }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page {
  padding: 24px;
}

.page-title {
  font-size: 48px;
  margin-bottom: 32px;
  color: #1f2a44;
}

.cards-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
}

.credit-card {
  position: relative;
  width: 380px;
  height: 220px;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0,0,0,0.18);
}

.card-bg {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.28);
}

.card-content {
  position: absolute;
  inset: 0;
  padding: 24px;
  display: flex;
  flex-direction: column;
  color: white;
}

.card-brand {
  font-size: 14px;
  opacity: 0.9;
}

.card-type {
  font-size: 24px;
  font-weight: bold;
  margin-top: 4px;
}

.card-number {
  margin-top: auto;
  font-size: 28px;
  letter-spacing: 3px;
  font-weight: 600;
}

.card-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.label {
  font-size: 11px;
  opacity: 0.7;
}
.activate-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 12px;
  background: #22c55e;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
}

.activate-btn:hover {
  background: #16a34a;
}

.status-badge {
  margin-top: 4px;
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  width: fit-content;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

/* ACTIVE */
.status-badge.active {
  background: #dcfce7;
  color: #166534;
  border: 1px solid #22c55e;
}

/* INACTIVE */
.status-badge.inactive {
  background: #fef3c7;
  color: #92400e;
  border: 1px solid #f59e0b;
}

/* BLOCKED */
.status-badge.blocked {
  background: #fee2e2;
  color: #991b1b;
  border: 1px solid #ef4444;
}

.card-wrapper {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card-header {
  width: 380px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>