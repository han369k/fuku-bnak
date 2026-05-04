<template>
  <div class="container">
    <h2>風險管理 - 黑名單系統</h2>

    <!-- 功能列 -->
    <div
      style="
        margin-bottom: 16px;
        display: flex;
        align-items: center;
        justify-content: space-between;
      "
    >
      <!-- 左側：操作按鈕 -->
      <div>
        <a-button type="primary" @click="openModal('create')"> 新增黑名單 </a-button>
      </div>

      <!-- 右側：狀態篩選器 -->
      <div style="display: flex; align-items: center">
        <span style="margin-right: 8px; white-space: nowrap">顯示狀態：</span>
        <a-radio-group v-model:value="filterStatus" @change="handleFilterChange">
          <a-radio-button :value="true">已啟用</a-radio-button>
          <a-radio-button :value="false">已停用</a-radio-button>
          <a-radio-button :value="null">全部</a-radio-button>
        </a-radio-group>
      </div>
    </div>

    <!-- 數據表格 -->
    <a-table
      :columns="columns"
      :data-source="dataSource"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
      row-key="id"
    >
      <!-- 狀態欄位插槽 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-switch
            v-model:checked="record.status"
            @change="(checked) => handleStatusChange(record, checked)"
          />
        </template>

        <!-- 操作欄位 -->
        <template v-if="column.key === 'action'">
          <a @click="openModal('edit', record)">編輯</a>
        </template>
      </template>
    </a-table>

    <!-- 新增/編輯 彈窗 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalType === 'create' ? '新增黑名單' : '編輯黑名單'"
      @ok="handleModalOk"
    >
      <a-form :model="formData" layout="vertical">
        <a-form-item label="類型">
          <a-select v-model:value="formData.listType" :disabled="modalType === 'edit'">
            <a-select-option value="ID_CARD">身份證字號</a-select-option>
            <a-select-option value="ACCOUNT_NO">帳戶</a-select-option>
            <a-select-option value="EMAIL">電子郵件</a-select-option>
            <a-select-option value="PHONE">電話</a-select-option>
            <!--<a-select-option value="IP">IP 位址</a-select-option>-->
          </a-select>
        </a-form-item>
        <a-form-item label="數值">
          <a-input v-model:value="formData.listValue" :disabled="modalType === 'edit'" />
        </a-form-item>
        <a-form-item label="備註原因"> <a-textarea v-model:value="formData.reason" /> </a-form-item>
        <a-form-item label="解封時間 (不選則為永久)">
          <a-date-picker
            v-model:value="formData.expireAt"
            show-time
            placeholder="選擇解封日期時間"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 100%"
          />
          <div style="font-size: 12px; color: #999; margin-top: 4px">
            超過此時間後，系統將自動不再攔截該筆資料。
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { message } from 'ant-design-vue'
import api from '@/api/axios'

// --- 資料定義 ---
const loading = ref(false)
const dataSource = ref([])
const modalVisible = ref(false)
const modalType = ref('create') // 'create' or 'edit'
const filterStatus = ref(true)

const formData = reactive({
  listType: 'ID_CARD',
  listValue: '',
  reason: '',
  status: true,
})
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
})
const typeLabelMap = {
  ID_CARD: '身份證字號',
  ACCOUNT_NO: '帳戶',
  EMAIL: '電子郵件',
  PHONE: '電話',
}

const columns = [
  {
    title: '類型',
    dataIndex: 'listType',
    key: 'listType',
    // 透過 render 函數進行中文轉換
    customRender: ({ text }) => {
      return typeLabelMap[text] || text // 如果找不到對應，就顯示原始值
    },
  },
  { title: '數值', dataIndex: 'listValue', key: 'listValue' },
  { title: '原因', dataIndex: 'reason', key: 'reason' },
  { title: '啟用狀態', dataIndex: 'status', key: 'status' },
  { title: '資料來源', dataIndex: 'source', key: 'source' },
  { title: '建立時間', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '解封時間', dataIndex: 'expireAt', key: 'expireAt' },
  { title: '更新時間', dataIndex: 'updatedAt', key: 'updatedAt' },
  { title: '操作', key: 'action' },
]

// --- 邏輯處理 ---

// 獲取列表資料
const fetchList = async () => {
  loading.value = true
  try {
    const res = await api.get('/api/blacklist', {
      params: {
        page: pagination.current - 1, // Spring Pageable 從 0 開始
        size: pagination.pageSize,
        activated: filterStatus.value,
        sort: 'createdAt,desc',
      },
    })
    dataSource.value = res.data.data.content
    pagination.total = res.data.data.totalElements
  } catch {
    message.error('讀取失敗')
  } finally {
    loading.value = false
  }
}

// 切換狀態
const handleStatusChange = async (record, checked) => {
  try {
    // 對應後端 @PutMapping("/{type}/{value}/status")
    await api.put(`/api/blacklist/${record.listType}/${record.listValue}/status`, null, {
      params: { status: checked },
    })
    message.success('狀態更新成功')
  } catch {
    record.status = !checked // 失敗時還原開關狀態
    message.error('狀態更新失敗')
  }
}

// 打開彈窗
const openModal = (type, record = null) => {
  modalType.value = type
  if (type === 'edit' && record) {
    Object.assign(formData, record)
  } else {
    Object.assign(formData, { listType: 'ID_CARD', listValue: '', reason: '', status: true })
  }
  modalVisible.value = true
}

// 提交彈窗表單
const handleModalOk = async () => {
  try {
    if (modalType.value === 'create') {
      // 對應後端 @PostMapping("/create")
      await api.post('/api/blacklist/create', formData)
      message.success('新增成功')
    } else {
      // 對應後端 @PostMapping("/{type}/{value}/update")
      await api.post(`/api/blacklist/${formData.listType}/${formData.listValue}/update`, formData)
      message.success('更新成功')
    }
    modalVisible.value = false
    fetchList()
  } catch {
    message.error('操作失敗')
  }
}

// 處理表格換頁
const handleTableChange = (pag) => {
  pagination.current = pag.current
  fetchList()
}

// 當切換篩選條件時，回到第一頁並重新讀取
const handleFilterChange = () => {
  pagination.current = 1
  fetchList()
}

onMounted(() => {
  fetchList()
})
</script>
