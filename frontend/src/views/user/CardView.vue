<script setup>
import { ref, onMounted } from 'vue'
import { getMyCards,activateCard } from '@/api/userCard'
import { BASE_URL } from '@/api/axios'

const cards = ref([])

const fetchCards = async () => {
  try {
    const response = await getMyCards()
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
        class="credit-card"
        v-for="card in cards"
        :key="card.cardId"
      >
        <!-- 卡片圖片 -->
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

            <div>
              <span class="label">STATUS</span>
              <div>{{ card.status }}</div>
            </div>
          </div>
          <div v-if="card.status === 'INACTIVE'">
            <button @click="handleActivateCard(card.cardId)">
              Activate Card
            </button>
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
  justify-content: space-between;
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

.status.active {
  color: #4ade80;
}

.status.inactive {
  color: #facc15;
}

.status.blocked {
  color: #f87171;
}
</style>