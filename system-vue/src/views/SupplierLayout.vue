<template>
  <div class="supplier-layout">
    <el-container>
      <!-- 顶部 Header -->
      <el-header class="header">
        <div class="header-content">
          <span class="main-title">新能源交易平台</span>
          <span class="sub-title">（供应商）</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :style="{ backgroundColor: userColor }" style="margin-right: 8px;">{{ userInitial }}</el-avatar>
              {{ userInfo.contactPerson || '供应商' }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="info">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <!-- 左侧边栏 -->
        <el-aside width="220px" class="sidebar">
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            background-color="transparent"
            text-color="#333"
            active-text-color="#409EFF"
            :router="true"
          >
            <el-menu-item index="/supplier/mainPage">
              <el-icon><House /></el-icon>
              <span>主页</span>
            </el-menu-item>
            <el-menu-item index="/supplier/info">
              <el-icon><User /></el-icon>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item index="/supplier/products">
              <el-icon><Goods /></el-icon>
              <span>浏览产品</span>
            </el-menu-item>
            <el-menu-item index="/supplier/my-products">
              <el-icon><Shop /></el-icon>
              <span>我的产品</span>
            </el-menu-item>
            <el-menu-item index="/supplier/orders">
              <el-icon><List /></el-icon>
              <span>我的订单</span>
            </el-menu-item>
            <el-menu-item index="/supplier/evaluations">
              <el-icon><Star /></el-icon>
              <span>交易评价</span>
            </el-menu-item>
            <el-menu-item index="/supplier/feedback">
              <el-icon><Comment /></el-icon>
              <span>意见反馈</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <!-- 右侧内容区 -->
        <el-main class="content">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ArrowDown, House, User, Goods, Shop, List, Star, Comment } from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();
const userInfo = ref({});
const colors = ['#67C23A', '#409EFF', '#E6A23C', '#F56C6C', '#909399'];

const activeMenu = computed(() => route.path);

const userInitial = computed(() => {
  return userInfo.value.contactPerson ? userInfo.value.contactPerson.charAt(0).toUpperCase() : '供';
});

const userColor = computed(() => {
  if (!userInfo.value.contactPerson) return colors[0];
  const hash = userInfo.value.contactPerson.split('').reduce((acc, char) => char.charCodeAt(0) + ((acc << 5) - acc), 0);
  const index = Math.abs(hash % colors.length);
  return colors[index];
});

onMounted(() => {
  const userStr = localStorage.getItem('supplier_user');
  if (userStr) {
    userInfo.value = JSON.parse(userStr);
  }
});

const handleMenuSelect = (index) => {
  router.push(index);
};

const logout = () => {
  localStorage.removeItem('supplier_userId');
  localStorage.removeItem('supplier_user');
  router.push('/login');
};

const handleCommand = (command) => {
  if (command === 'logout') {
    logout();
  } else if (command === 'info') {
    router.push('/supplier/info');
  }
};
</script>

<style scoped>
.supplier-layout, .el-container {
  height: 100vh;
  width: 100%;
}

.header {
  background-color: #74b310;
  color: white;
  text-align: center;
  line-height: 60px;
  font-size: 28px;
  font-weight: bold;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 0px 0 0px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-grow: 1;
  padding-left: 150px;
}

.header-right {
  min-width: 120px;
  text-align: right;
  height: 100%;
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #fff;
  display: flex;
  align-items: center;
  font-size: 16px;
}

.main-title {
  font-size: 32px;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
}

.sub-title {
  font-size: 20px;
  color: #fff;
  margin-left: 10px;
  font-weight: normal;
}

.sidebar {
  position: relative;
  background-color: #f7faff;
  border-right: 1px solid #e6e6e6;
  height: calc(100vh - 60px);
}

.el-menu {
  border-right: none;
}

.el-menu-item {
  font-size: 16px;
  transition: all 0.2s ease-in-out;
  border-left: 4px solid transparent;
}
.el-menu-item:hover {
  background-color: #eff5ff !important;
}
.el-menu-item.is-active {
  background-color: #eaf5ff !important;
  border-left-color: #409eff;
  font-weight: 600;
}

.content {
  padding: 20px;
  background-color: #f5f7fa;
}
</style> 