<template>
  <div class="purchaser-main-page">
    <el-card class="welcome-card" shadow="never">
      <h2>您好，{{ userInfo.contactPerson || '采购商' }}！欢迎来到新能源交易平台。</h2>
      <p>在这里，您可以轻松浏览和采购所需的产品，管理您的订单，并与供应商进行有效沟通。</p>
    </el-card>

    <el-row :gutter="20" class="stats-cards">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card" @click="goTo('/purchaser/orders?tab=pendingPayment')">
          <div class="stat-icon-wrapper" style="background-color: #fdf6ec;">
             <el-icon :size="32" color="#E6A23C"><Wallet /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ orderStats.pendingPayment }}</div>
            <div class="stat-label">待付款订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card" @click="goTo('/purchaser/orders?tab=toReceive')">
           <div class="stat-icon-wrapper" style="background-color: #ecf5ff;">
             <el-icon :size="32" color="#409EFC"><Van /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ orderStats.toReceive }}</div>
            <div class="stat-label">待收货订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card" @click="goTo('/purchaser/orders?tab=completed')">
           <div class="stat-icon-wrapper" style="background-color: #f0f9eb;">
            <el-icon :size="32" color="#67C23A"><CircleCheckFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ orderStats.completed }}</div>
            <div class="stat-label">已完成订单</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="quick-nav-card" shadow="never">
       <template #header>
        <div class="card-header">
          <span>快速导航</span>
        </div>
      </template>
      <el-button type="primary" size="large" @click="goTo('/purchaser/products')">前往采购商城</el-button>
      <el-button size="large" @click="goTo('/purchaser/orders')">查看我的订单</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Wallet, Van, CircleCheckFilled } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance();
const router = useRouter();

const userInfo = ref({});
const orders = ref([]);

const orderStats = computed(() => {
  return {
    pendingPayment: orders.value.filter(o => o.orderStatus === 0).length,
    toReceive: orders.value.filter(o => o.orderStatus === 3).length,
    completed: orders.value.filter(o => o.orderStatus === 4).length,
  };
});

onMounted(() => {
  const userStr = localStorage.getItem('purchaser_user');
  if (userStr) {
    userInfo.value = JSON.parse(userStr);
  }
  fetchOrders();
});

const fetchOrders = async () => {
  const userId = localStorage.getItem('purchaser_userId');
  if (!userId) {
    return;
  }
  try {
    const { data } = await proxy.$http.get(`/order/user/${userId}`);
    orders.value = data || [];
  } catch (error) {
    ElMessage.error('获取订单数据失败');
    console.error(error);
  }
};

const goTo = (path) => {
  router.push(path);
};
</script>

<style scoped>
.purchaser-main-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.welcome-card {
  border-left: 5px solid #409EFF;
  background-color: #ecf5ff;
}
.welcome-card h2 {
  font-size: 22px;
  color: #333;
  margin-top: 0;
}
.welcome-card p {
  font-size: 16px;
  color: #666;
}
.stats-cards .stat-card {
  cursor: pointer;
  transition: all 0.3s ease;
}
.stats-cards .stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 20px;
}
.stat-icon-wrapper {
  padding: 15px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.stat-info {
  display: flex;
  flex-direction: column;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-label {
  font-size: 16px;
  color: #909399;
}
.quick-nav-card .el-button {
  margin-right: 10px;
}
</style>
