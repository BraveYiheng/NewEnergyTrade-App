<template>
  <div class="supplier-main-page">
    <el-card class="welcome-card" shadow="never">
      <h2>您好，{{ userInfo.contactPerson || '供应商' }}！欢迎回到您的管理中心。</h2>
      <p>在这里，您可以高效管理您的产品、处理订单，并与采购商进行互动。</p>
    </el-card>

    <el-row :gutter="20" class="stats-cards">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card" @click="goTo('/supplier/my-products')">
          <div class="stat-icon-wrapper" style="background-color: #f0f9eb;">
             <el-icon :size="32" color="#67C23A"><Goods /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ productCount }}</div>
            <div class="stat-label">上架产品总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card" @click="goTo('/supplier/orders?tab=pendingDispatch')">
           <div class="stat-icon-wrapper" style="background-color: #fdf6ec;">
             <el-icon :size="32" color="#E6A23C"><Box /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ pendingDispatchCount }}</div>
            <div class="stat-label">待发货订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card" @click="goTo('/supplier/evaluations')">
           <div class="stat-icon-wrapper" style="background-color: #ecf5ff;">
            <el-icon :size="32" color="#409EFC"><ChatDotRound /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ evaluationCount }}</div>
            <div class="stat-label">收到的评价</div>
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
      <el-button type="primary" size="large" @click="goTo('/supplier/my-products')">管理我的产品</el-button>
      <el-button size="large" @click="goTo('/supplier/orders')">查看所有订单</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Goods, Box, ChatDotRound } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance();
const router = useRouter();

const userInfo = ref({});
const productCount = ref(0);
const orders = ref([]);
const evaluationCount = ref(0);

const pendingDispatchCount = computed(() => {
  return orders.value.filter(o => o.orderStatus === 2).length;
});

onMounted(() => {
  const userStr = localStorage.getItem('supplier_user');
  if (userStr) {
    userInfo.value = JSON.parse(userStr);
  }
  fetchAllData();
});

const fetchAllData = async () => {
  const userId = localStorage.getItem('supplier_userId');
  if (!userId) return;

  try {
    const [productsRes, ordersRes, evaluationsRes] = await Promise.all([
      proxy.$http.get(`/product/user/${userId}`),
      proxy.$http.get(`/order/user/${userId}`),
      proxy.$http.get(`/evaluation/toUser/${userId}`)
    ]);
    
    productCount.value = productsRes.data?.length || 0;
    orders.value = (ordersRes.data || []).filter(order => order.supplierId === parseInt(userId));
    evaluationCount.value = evaluationsRes.data?.length || 0;

  } catch (error) {
    ElMessage.error('获取主页数据失败');
    console.error(error);
  }
};

const goTo = (path) => {
  router.push(path);
};
</script>

<style scoped>
.supplier-main-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.welcome-card {
  border-left: 5px solid #67C23A;
  background-color: #f0f9eb;
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
