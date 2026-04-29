<script setup>
import { ref, onMounted } from 'vue'
import {message,Modal} from 'ant-design-vue'
import {getApplications, deleteApplication,updateApplicationStatus} from '@/api/axios'


const applications = ref([])
const loading = ref(false)

//分頁機
const pagination=ref({
  current:1,
  pageSize:10,
  total:0
})

// 表格欄位
const columns = [
  { title: 'ID', dataIndex: 'applicationId', key: 'applicationId' },
  { title: '使用者', dataIndex: 'userId', key: 'userId' },
  { title: '卡別', dataIndex: 'cardTypeName', key: 'cardTypeName' },
  { title: '狀態', dataIndex: 'status', key: 'status' },
  { title: '操作', key: 'action' }
]
//取得資料
const fetchData = async () => {
  loading.value = true
  try {
    const response = await getApplications({
      current: pagination.value.current-1,
      pageSize: pagination.value.pageSize
    })
    applications.value = response.data.data
    pagination.value.total = response.data.total
  } catch (error) {
    console.log(error)
  }finally{
    loading.value = false
  }
}
//刪除資料
const handleDelete = async (record) => {
  Modal.confirm({
    title: '確定刪除嗎?',
    onOk: async () => {
      try {
        await deleteApplication(record.applicationId)
        await fetchData()
        message.success('刪除成功')
        fetchData()
      } catch (error) {
        console.log(error)
      }
    }
  })
  
}
//更新資料
const handleUpdate = async (id,status) => {
  try {
    await updateApplicationStatus(id,status)
    await fetchData()
  } catch (error) {
    console.log(error)
  }
}
//分頁
const handlePageChange = (page) => {
  pagination.value.current = page.current;
  fetchData();
}



onMounted(() => {
  fetchData()
})


</script>
<template>
  <div>
    <h2>信用卡申請管理</h2>
    <a-table
      :columns="columns"
      :data="applications"
      :loading="loading"
      :pagination="pagination"
      @page-change="handlePageChange"
    >
      <template #applicationId>
        {{ record.applicationId }}
      </template> 
      

    >
    </a-table>
  </div>
</template>