<template>
  <div>
    <el-table :data="orders" style="width: 100%" stripe>
      <el-table-column prop="orderNumber" label="订单编号" width="200"></el-table-column>
      <el-table-column prop="product.productName" label="产品名称" width="180"></el-table-column>
      <el-table-column prop="supplier.companyName" label="供应商" width="180"></el-table-column>
      <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
      <el-table-column prop="unitPrice" label="单价(元)" width="100"></el-table-column>
      <el-table-column prop="orderAmount" label="总价(元)" width="120"></el-table-column>
      <el-table-column label="订单状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.orderStatus)">
            {{ getStatusText(scope.row.orderStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template #default="scope">
          <!-- 待付款 -->
          <template v-if="scope.row.orderStatus === 0">
            <el-button type="primary" size="small" @click="handlePayment(scope.row.orderId)">支付</el-button>
            <el-button type="danger" size="small" @click="cancelOrder(scope.row.orderId)">取消订单</el-button>
          </template>
          <!-- 待收货 -->
          <template v-if="scope.row.orderStatus === 3">
            <el-button type="success" size="small" @click="confirmReceipt(scope.row.orderId)">确认收货</el-button>
          </template>
          <!-- 已完成 -->
          <template v-if="scope.row.orderStatus === 4">
            <el-button size="small" @click="showEvaluationDialog(scope.row)">评价</el-button>
            <el-button type="danger" size="small" @click="deleteOrder(scope.row.orderId)">删除订单</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 评价对话框 -->
    <el-dialog title="订单评价" v-model="evaluationDialogVisible" width="450px" @close="resetEvaluationForm">
      <el-form :model="evaluationForm" label-width="80px">
        <el-form-item label="星级">
          <el-rate v-model="evaluationForm.score" :max="5" show-text :texts="['极差', '失望', '一般', '满意', '惊喜']"></el-rate>
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evaluationForm.content" type="textarea" rows="4" placeholder="请输入您的评价"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="evaluationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluation">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue';
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus';

const { proxy } = getCurrentInstance();

const props = defineProps({
  orders: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['reload']);

const evaluationDialogVisible = ref(false);
const evaluationForm = ref({
  orderId: null,
  fromUserId: null,
  toUserId: null,
  score: 5,
  content: ''
});

const getStatusText = (status) => {
  const statusMap = { 0: '待付款', 1: '已付款', 2: '待发货', 3: '已发货', 4: '已完成' };
  return statusMap[status] || '未知状态';
};

const getStatusTagType = (status) => {
  const tagMap = { 0: 'warning', 1: 'info', 2: 'info', 3: 'primary', 4: 'success' };
  return tagMap[status] || 'info';
};

const handlePayment = async (orderId) => {
  const loading = ElLoading.service({
    lock: true,
    text: '正在处理支付...',
    background: 'rgba(0, 0, 0, 0.7)',
  });
  
  // 模拟3秒支付过程
  setTimeout(async () => {
    try {
      // 支付成功后，状态变为2（待发货）
      await proxy.$http.post(`/order/updateStatus`, { orderId, orderStatus: 2 });
      loading.close();
      ElMessage.success('支付成功，等待卖家发货');
      emit('reload');
    } catch (error) {
      loading.close();
      ElMessage.error('支付处理失败');
      console.error(error);
    }
  }, 3000);
};

const cancelOrder = async (orderId) => {
  await ElMessageBox.confirm('确定要取消这个订单吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  });
  try {
    // 假设后端提供了取消订单接口，状态变为-1或直接删除
    // 此处使用删除接口作为示例
    await proxy.$http.delete(`/order/${orderId}`);
    ElMessage.success('订单已取消');
    emit('reload');
  } catch (error) {
    ElMessage.error('取消订单失败');
  }
};

const confirmReceipt = async (orderId) => {
    await ElMessageBox.confirm('请确认您已收到商品', '确认收货', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
    });
    try {
        await proxy.$http.post(`/order/updateStatus`, { orderId: orderId, orderStatus: 4 });
        ElMessage.success('操作成功，订单已完成');
        emit('reload');
    } catch(error) {
        ElMessage.error('确认收货失败');
        console.error(error);
    }
};

const showEvaluationDialog = (order) => {
  resetEvaluationForm();
  evaluationForm.value.orderId = order.orderId;
  evaluationForm.value.fromUserId = parseInt(localStorage.getItem('userId'));
  evaluationForm.value.toUserId = order.supplierId;
  evaluationDialogVisible.value = true;
};

const submitEvaluation = async () => {
  if (evaluationForm.value.score === 0) {
      ElMessage.warning('请选择星级');
      return;
  }
  try {
    await proxy.$http.post('/evaluation/add', evaluationForm.value);
    ElMessage.success('评价成功！');
    evaluationDialogVisible.value = false;
    emit('reload');
  } catch (error) {
    ElMessage.error('评价提交失败');
    console.error(error);
  }
};

const deleteOrder = async (orderId) => {
  await ElMessageBox.confirm('确定要永久删除这个订单吗?', '危险操作', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'error',
  });
  try {
    await proxy.$http.delete(`/order/${orderId}`);
    ElMessage.success('订单已删除');
    emit('reload');
  } catch (error) {
    ElMessage.error('删除订单失败');
  }
};

const resetEvaluationForm = () => {
  evaluationForm.value.orderId = null;
  evaluationForm.value.fromUserId = null;
  evaluationForm.value.toUserId = null;
  evaluationForm.value.score = 5;
  evaluationForm.value.content = '';
};
</script>

<style scoped>
.el-button+.el-button {
  margin-left: 8px;
}
</style> 