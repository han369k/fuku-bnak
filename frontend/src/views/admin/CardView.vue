<script setup>
import { ref, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { getCards, createCard, updateCard, deleteCard } from '@/api/card'
import api from '@/api/axios'

const cards = ref([])
const loading = ref(false)

const modalVisible = ref(false)
const form = ref({
  annualFee: 0,
})
const isEdit = ref(false)

const currentId = ref(null)

//取得資料
const fetchData = async () => {
  loading.value = true
  try {
    const response = await getCards()
    cards.value = response.data.data
    console.log(response.data)
  } catch (error) {
    console.log(error)
  } finally {
    loading.value = false
  }
}
//編輯資料
const handleEdit = (record) => {
  isEdit.value = true
  currentId.value = record.cardId
  form.value.annualFee = record.annualFee
  modalVisible.value = true
}
//新增資料
const handleCreate = () => {
  isEdit.value = false
  form.value.annualFee = 0
  modalVisible.value = true
}
//刪除資料
const handleDelete = async (record) => {
  Modal.confirm({
    title: '確定刪除嗎?',
    onOk: async () => {
      try {
        await deleteCard(record.cardId)
        await fetchData()
        message.success('刪除成功')
      } catch (error) {
        console.log(error)
      }
    },
  })
}

const columns = [
  { title: 'ID', dataIndex: 'cardId' },
  { title: '卡號', dataIndex: 'cardNumber' },
  { title: '卡片名稱', key: 'cardTypeName' },
  { title: '卡片圖片', key: 'image' },
  { title: '額度', dataIndex: 'creditLimit' },
  { title: '已用額度', dataIndex: 'currentBalance' },
  { title: '狀態', dataIndex: 'status' },
  { title: '操作', key: 'action' },
]
onMounted(() => {
  fetchData()
})
</script>
<template>
  <a-button type="primary" @click="handleCreate">新增卡片</a-button>
  <a-table :columns="columns" :data-source="cards" :loading="loading" row-key="cardId">
    <template #bodyCell="{ column, record }">
      <!-- 卡片名稱 -->
      <template v-if="column.key === 'cardTypeName'">
        {{ record.cardType?.cardTypeName }}
      </template>

      <!-- 卡片圖片 -->
      <template v-else-if="column.key === 'image'">
        <img :src="`${api.defaults.baseURL}/${record.cardType?.cardImageUrl}`" style="width: 80px" />
      </template>

      <!-- 操作按鈕處理 -->

      <template v-if="column.key === 'action'">
        <a-button @click="handleEdit(record)">編輯</a-button>
        <a-button danger @click="handleDelete(record)">刪除</a-button>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="modalVisible" title="卡片">
    <a-form layout="vertical">
      <a-form-item label="年費">
        <a-input-number v-model:value="form.annualFee" style="width: 100%" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
