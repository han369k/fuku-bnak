<script setup>
import { ref, onMounted } from 'vue'
import { getTransactions, createTransaction, getMerchantNames } from '@/api/userCardTxn'
import { getMyCards } from '@/api/userCard'
import dayjs from 'dayjs'

const transactions = ref([])
const loading = ref(false)
const cards = ref([])
const merchants = ref([])

const columns = [
  {
    key: 'txnDate',
    label: 'Transaction Date',
  },
  {
    key: 'merchantName',
    label: 'Merchant',
  },
  {
    key: 'cardNumber',
    label: 'Card Number',
  },
  {
    key: 'txnAmount',
    label: 'Amount',
  },
  {
    key: 'cashbackAmount',
    label: 'Cashback',
  },
  {
    key: 'txnType',
    label: 'Type',
  },
  {
    key: 'refunded',
    label: 'Status',
  },
]

const form = ref({
  cardId: '',
  merchantId: '',
  txnAmount: '',
  txnType: 'PAYMENT',
  description: '',
})
const currentPage = ref(0)
const pageSize = ref(10)
const totalPages = ref(0)
const totalElements = ref(0)

const fetchCards = async () => {
  try {
    const response = await getMyCards()
    cards.value = response
  } catch (error) {
    console.error('Failed to fetch cards:', error)
  }
}

const fetchTransactions = async () => {
  try {
    loading.value = true
    const response = await getTransactions(currentPage.value, pageSize.value)
    transactions.value = response.content
    totalPages.value = response.totalPages
    totalElements.value = response.totalElements
    console.log(response.content)
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const fetchMerchantNames = async () => {
  try {
    const response = await getMerchantNames()
    merchants.value = response
    console.log(response)
  } catch (error) {
    console.error('Failed to fetch merchant names:', error)
  }
}

const handleCreateTransaction = async () => {
  try {
    if (!form.value.cardId || !form.value.merchantId || !form.value.txnAmount) {
      alert('Please fill all required fields')
      return
    }

    await createTransaction(form.value)

    alert('Transaction created successfully')

    // reset form
    form.value = {
      cardId: '',
      merchantId: '',
      txnAmount: '',
      txnType: 'PAYMENT',
      description: '',
    }

    // refresh table
    await fetchTransactions()
  } catch (err) {
    console.error(err)
    alert(err.response?.data?.message ||'Create transaction failed')
  }
}

const nextPage = async () => {
  currentPage.value++
  await fetchTransactions()
}

const prevPage = async () => {
  currentPage.value--
  await fetchTransactions()
}

onMounted(async () => {
  await fetchCards()
  fetchTransactions()
  fetchMerchantNames()
})
</script>
<template>
  <div class="min-h-screen bg-stone-100 p-8">
    <div class="max-w-7xl mx-auto">
      <!-- title -->
      <div class="mb-8">
        <h1 class="text-4xl font-bold text-stone-700 tracking-wide">Card Transactions</h1>

        <p class="text-stone-500 mt-2">View and manage your transaction history</p>
      </div>

      <!-- create form -->
      <div class="bg-white border border-stone-200 rounded-3xl shadow-sm p-8 mb-8">
        <h2 class="text-2xl font-semibold text-stone-700 mb-6">Create Transaction</h2>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-5">
          <!-- card id -->
          <div>
            <label class="block text-stone-600 mb-2"> Card Number </label>

            <select
              v-model="form.cardId"
              class="w-full border border-stone-300 rounded-2xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-stone-400"
            >
              <option disabled value="">Please select card</option>

              <option v-for="card in cards" :key="card.cardId" :value="card.cardId">
                {{ card.cardTypeName }}
                - {{ card.cardNumber }}
              </option>
            </select>
          </div>

          <!-- merchant id -->
          <div>
            <label class="block text-stone-600 mb-2"> Merchant </label>

            <select
              v-model="form.merchantId"
              class="w-full border border-stone-300 rounded-2xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-stone-400"
            >
              <option disabled value="">Please select merchant</option>

              <option
                v-for="merchant in merchants"
                :key="merchant.merchantId"
                :value="merchant.merchantId"
              >
                {{ merchant.merchantName }}
              </option>
            </select>
          </div>

          <!-- amount -->
          <div>
            <label class="block text-stone-600 mb-2"> Amount </label>

            <input
              v-model="form.txnAmount"
              type="number"
              placeholder="Enter amount"
              class="w-full border border-stone-300 rounded-2xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-stone-400"
            />
          </div>

          <!-- type -->
          <div>
            <label class="block text-stone-600 mb-2"> Transaction Type </label>

            <select
              v-model="form.txnType"
              class="w-full border border-stone-300 rounded-2xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-stone-400"
            >
              <option value="PAYMENT">PAYMENT</option>

              <option value="REFUND">REFUND</option>
            </select>
          </div>

          <!-- description -->
          <div class="md:col-span-2">
            <label class="block text-stone-600 mb-2"> Description </label>

            <textarea
              v-model="form.description"
              rows="3"
              placeholder="Enter description"
              class="w-full border border-stone-300 rounded-2xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-stone-400"
            />
          </div>
        </div>

        <!-- button -->
        <div class="mt-8">
          <button
            @click="handleCreateTransaction"
            class="bg-stone-700 hover:bg-stone-800 text-white px-8 py-3 rounded-2xl transition duration-200"
          >
            Create Transaction
          </button>
        </div>
      </div>

      <!-- transaction table -->
      <div class="bg-white border border-stone-200 rounded-3xl shadow-sm p-8">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-2xl font-semibold text-stone-700">Transaction History</h2>

          <span class="text-stone-500"> Total {{ totalElements }} </span>
        </div>

        <!-- table -->
        <div class="overflow-x-auto relative">
          <!-- loading overlay -->
          <div
            v-if="loading"
            class="absolute inset-0 bg-white/60 backdrop-blur-[1px] flex items-center justify-center z-10 rounded-2xl"
          >
            <div class="flex items-center gap-3">
              <!-- spinner -->
              <div
                class="w-6 h-6 border-2 border-stone-400 border-t-transparent rounded-full animate-spin"
              />

              <span class="text-stone-600 font-medium"> Loading... </span>
            </div>
          </div>

          <!-- table -->

          <table class="w-full min-w-[1000px]">
            <!-- header -->
            <thead>
              <tr class="border-b border-stone-200 text-stone-500">
                <th v-for="column in columns" :key="column.key" class="text-left py-4 font-medium">
                  {{ column.label }}
                </th>
              </tr>
            </thead>

            <!-- body -->
            <tbody>
              <tr
                v-for="txn in transactions"
                :key="txn.txnId"
                class="border-b border-stone-100 hover:bg-stone-50 transition"
              >
                <!-- date -->
                <td class="py-5 text-stone-600 whitespace-nowrap">
                  {{ dayjs(txn.txnDate).format('YYYY/MM/DD HH:mm') }}
                </td>

                <!-- merchant -->
                <td class="py-5 text-stone-700">
                  {{ txn.merchantName }}
                </td>

                <!-- card -->
                <td class="py-5 text-stone-700 tracking-widest whitespace-nowrap">
                  {{ txn.cardNumber }}
                </td>

                <!-- amount -->
                <td class="py-5 font-medium text-stone-700 whitespace-nowrap">
                  NT$ {{ txn.txnAmount }}
                </td>

                <!-- cashback -->
                <td class="py-5 text-emerald-600 font-medium whitespace-nowrap">
                  +{{ txn.cashbackAmount || 0 }}
                </td>

                <!-- type -->
                <td class="py-5">
                  <span
                    v-if="txn.txnType === 'PAYMENT'"
                    class="bg-stone-200 text-stone-700 px-4 py-1 rounded-full text-sm"
                  >
                    PAYMENT
                  </span>

                  <span v-else class="bg-red-100 text-red-700 px-4 py-1 rounded-full text-sm">
                    REFUND
                  </span>
                </td>

                <!-- refunded -->
                <td class="py-5">
                  <span
                    v-if="txn.refunded"
                    class="bg-red-100 text-red-700 px-4 py-1 rounded-full text-sm"
                  >
                    Refunded
                  </span>

                  <span
                    v-else
                    class="bg-emerald-100 text-emerald-700 px-4 py-1 rounded-full text-sm"
                  >
                    Active
                  </span>
                </td>
              </tr>

              <!-- empty -->
              <tr v-if="transactions.length === 0">
                <td colspan="7" class="text-center py-12 text-stone-400">No transactions found</td>
              </tr>
            </tbody>
          </table>
          <!-- pagination -->
          <div class="flex justify-center items-center gap-4 mt-8">
            <!-- previous -->
            <button
              :disabled="currentPage === 0"
              @click="prevPage"
              class="px-4 py-2 border border-stone-300 rounded-xl disabled:opacity-50"
            >
              Previous
            </button>

            <!-- page info -->
            <span class="text-stone-600"> Page {{ currentPage + 1 }} / {{ totalPages }} </span>

            <!-- next -->
            <button
              :disabled="currentPage + 1 >= totalPages"
              @click="nextPage"
              class="px-4 py-2 border border-stone-300 rounded-xl disabled:opacity-50"
            >
              Next
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
