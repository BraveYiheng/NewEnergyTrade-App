<template>
  <div class="supplier-order">
    <h1>我的订单</h1>
    <el-tabs v-model="orderActiveTab">
      <el-tab-pane label="全部" name="all">
        <SupplierOrderTable :orders="orders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="待发货" name="pendingDispatch">
        <SupplierOrderTable :orders="pendingDispatchOrders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="已发货" name="dispatched">
        <SupplierOrderTable :orders="dispatchedOrders" @reload="fetchOrders" />
      </el-tab-pane>
      <el-tab-pane label="已完成" name="completed">
        <SupplierOrderTable :orders="completedOrders" @reload="fetchOrders" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { ElMessage } from 'element-plus';
import SupplierOrderTable from './components/SupplierOrderTable.vue';

const { proxy } = getCurrentInstance();

const orderActiveTab = ref('all');
const orders = ref([]);

// 根据订单状态动态筛选
const pendingDispatchOrders = computed(() => orders.value.filter(o => o.orderStatus === 2));
const dispatchedOrders = computed(() => orders.value.filter(o => o.orderStatus === 3));
const completedOrders = computed(() => orders.value.filter(o => o.orderStatus === 4));

onMounted(() => {
  fetchOrders();
});

const fetchOrders = async () => {
  const userId = localStorage.getItem('supplier_userId');
  if (!userId) {
    ElMessage.warning('用户未登录，无法获取订单');
    return;
  }
  try {
    const { data } = await proxy.$http.get(`/order/user/${userId}`);
    // 筛选出当前用户是供应商的订单
    orders.value = (data || []).filter(order => order.supplierId === parseInt(userId));
  } catch (error) {
    ElMessage.error('获取订单列表失败');
    console.error(error);
  }
};
</script>

<style scoped>
h1 {
  font-size: 24px;
  margin-bottom: 20px;
}
</style> 