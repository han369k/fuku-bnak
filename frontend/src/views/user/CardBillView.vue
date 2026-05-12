<script setup>
import { ref, onMounted } from 'vue'
import dayjs from 'dayjs'
import { getBills} from '@/api/userCardBill';

const loading = ref(false)
const bills = ref([])
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
})

const columns = [
  {
    title: '帳單編號',
    dataIndex: 'billId',
    key: 'billId',
  },
  {
    title: '帳單月份',
    dataIndex: 'billingMonth',
    key: 'billingMonth',
  },
  {
    title: '帳單金額',
    dataIndex: 'totalAmount',
    key: 'totalAmount',
  },
  {
    title: '最低應繳金額',
    dataIndex: 'minimumPayment',
    key: 'minimumPayment',
  },
  {
    title: '已繳金額',
    dataIndex: 'paidAmount',
    key: 'paidAmount',
  },
  {
    title: '繳費截止日',
    dataIndex: 'dueDate',
    key: 'dueDate',
  },
]
const fetchBills = async () => {
  loading.value = true
  try {
    const res = await getBills(pagination.value.current, pagination.value.pageSize)
    bills.value = res.data.records
    pagination.value.total = res.data.total
    console.log(res);
    
  } catch (error) {
    console.error('Failed to fetch bills:', error)
  } finally {
    loading.value = false
  }
}


</script>
<template>
    <h2>我的帳單管理</h2>
</template>