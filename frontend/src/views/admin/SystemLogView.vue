<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2>系統日誌</h2>
      <div style="display: flex; gap: 8px">
        <a-button type="primary" @click="handleExportCsv">匯出 CSV</a-button>
        <a-button type="primary" @click="handleExportPdf">匯出 PDF</a-button>
      </div>
    </div>

    <a-table
      :columns="columns"
      :data-source="logs"
      :loading="loading"
      row-key="id"
      bordered
    >
      <template #bodyCell="{ column, text }">
        <template v-if="column.key === 'actionTime'">
          {{ formatTime(text) }}
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getActionLogs, exportLogsCsv, exportLogsPdf } from '@/api/auth'

const logs = ref([])
const loading = ref(false)

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: '員工編號', dataIndex: 'empId', key: 'empId', width: 100 },
  { title: '姓名', dataIndex: 'empName', key: 'empName', width: 100 },
  { title: '動作', dataIndex: 'action', key: 'action', width: 120 },
  { title: '目標', dataIndex: 'target', key: 'target', width: 100 },
  { title: '詳情', dataIndex: 'details', key: 'details' },
  { title: '時間', dataIndex: 'actionTime', key: 'actionTime', width: 170 },
  { title: 'IP', dataIndex: 'ipAddress', key: 'ipAddress', width: 130 },
]

function formatTime(value) {
  if (!value) return '-'
  return value.replace('T', ' ').substring(0, 19)
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getActionLogs()
    logs.value = res.data.data
  } catch (err) {
    message.error('載入日誌失敗')
  } finally {
    loading.value = false
  }
}

async function handleExportCsv() {
  try {
    const res = await exportLogsCsv()
    downloadFile(res.data, 'system_logs.csv')
  } catch (err) {
    message.error('匯出 CSV 失敗')
  }
}

async function handleExportPdf() {
  try {
    const res = await exportLogsPdf()
    downloadFile(res.data, 'system_logs.pdf')
  } catch (err) {
    message.error('匯出 PDF 失敗')
  }
}

function downloadFile(data, filename) {
  const url = window.URL.createObjectURL(new Blob([data]))
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', filename)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

onMounted(() => {
  fetchData()
})
</script>
