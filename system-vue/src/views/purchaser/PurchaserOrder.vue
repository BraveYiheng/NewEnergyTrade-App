<template>
  <div class="purchaser-order">
    <h1>我的订单</h1>
    <el-tabs v-model="orderActiveTab" @tab-click="handleTabClick">
      <el-tab-pane label="全部订单" name="all">
        <OrderTable :orders="orders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="待付款" name="pendingPayment">
        <OrderTable :orders="pendingPaymentOrders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="待发货" name="pendingDispatch">
         <OrderTable :orders="pendingDispatchOrders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="待收货" name="toReceive">
        <OrderTable :orders="toReceiveOrders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="已完成" name="completed">
        <OrderTable :orders="completedOrders" @reload="fetchOrders" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { ElMessage } from 'element-plus';
import OrderTable from './components/OrderTable.vue'; // 引入抽离的表格组件

const { proxy } = getCurrentInstance();
const orderActiveTab = ref('all');
const orders = ref([]);

// 使用计算属性动态筛选订单
const pendingPaymentOrders = computed(() => orders.value.filter(o => o.orderStatus === 0));
const pendingDispatchOrders = computed(() => orders.value.filter(o => [1, 2].includes(o.orderStatus)));
const toReceiveOrders = computed(() => orders.value.filter(o => o.orderStatus === 3));
const completedOrders = computed(() => orders.value.filter(o => o.orderStatus === 4));

onMounted(() => {
  fetchOrders();
});

const fetchOrders = async () => {
  const userId = localStorage.getItem('purchaser_userId');
  if (!userId) {
      ElMessage.warning('请先登录');
      return;
  }
  try {
    const { data } = await proxy.$http.get(`/order/user/${userId}`);
    orders.value = data || [];
  } catch (error) {
    ElMessage.error('获取订单列表失败');
    console.error(error);
  }
};

const handleTabClick = () => {
  // 刷新
    fetchOrders();
  
};
</script>

<style scoped>
h1 {
  font-size: 24px;
  margin-bottom: 20px;
}
</style> 