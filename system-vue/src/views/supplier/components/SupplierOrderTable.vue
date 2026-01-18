<template>
  <div>
    <el-table :data="orders" style="width: 100%" stripe>
      <el-table-column prop="orderNumber" label="订单编号" width="200"></el-table-column>
      <el-table-column prop="product.productName" label="产品名称" width="180"></el-table-column>
      <el-table-column prop="purchaser.companyName" label="采购商" width="180"></el-table-column>
      <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
      <el-table-column prop="orderAmount" label="总价(元)" width="120"></el-table-column>
      <el-table-column label="订单状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.orderStatus)">
            {{ getStatusText(scope.row.orderStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="150">
        <template #default="scope">
          <!-- 待发货 -->
          <template v-if="scope.row.orderStatus === 2">
            <el-button type="primary" size="small" @click="dispatchOrder(scope.row.orderId)">发货</el-button>
          </template>
          <!-- 已发货 -->
          <template v-if="scope.row.orderStatus === 3">
            <el-button type="info" size="small" @click="trackOrder(scope.row.orderId)">物流跟踪</el-button>
          </template>
          <!-- 已完成 -->
          <template v-if="scope.row.orderStatus === 4">
            <el-button size="small" @click="showEvaluationDialog(scope.row)">评价</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 评价对话框 -->
    <el-dialog title="订单评价" v-model="evaluationDialogVisible" width="450px" @close="resetEvaluationForm">
      <el-form :model="evaluationForm" label-width="80px">
        <el-form-item label="星级">
          <el-rate v-model="evaluationForm.starRating" :max="5" show-text :texts="['极差', '失望', '一般', '满意', '惊喜']"></el-rate>
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evaluationForm.comment" type="textarea" rows="4" placeholder="请输入您的评价"></el-input>
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
import { ElMessage, ElMessageBox } from 'element-plus';

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
  starRating: 0,
  comment: ''
});

const getStatusText = (status) => {
  const statusMap = { 0: '待付款', 1: '已付款', 2: '待发货', 3: '已发货', 4: '已完成' };
  return statusMap[status] || '未知状态';
};

const getStatusTagType = (status) => {
  const tagMap = { 0: 'warning', 1: 'info', 2: 'warning', 3: 'primary', 4: 'success' };
  return tagMap[status] || 'info';
};

const dispatchOrder = async (orderId) => {
  await ElMessageBox.confirm('确定要发货吗?', '发货确认', {
    confirmButtonText: '确定发货',
    cancelButtonText: '取消',
    type: 'warning',
  });
  try {
    await proxy.$http.post(`/order/updateStatus`, { orderId, orderStatus: 3 });
    ElMessage.success('发货成功');
    emit('reload');
  } catch (error) {
    ElMessage.error('发货失败');
    console.error(error);
  }
};

const trackOrder = (orderId) => {
    ElMessage.info(`物流跟踪功能待开发，订单ID: ${orderId}`);
};

const showEvaluationDialog = (order) => {
  resetEvaluationForm();
  evaluationForm.value.orderId = order.orderId;
  evaluationForm.value.fromUserId = parseInt(localStorage.getItem('userId'));
  evaluationForm.value.toUserId = order.purchaserId; // 供应商评价采购商
  evaluationDialogVisible.value = true;
};

const submitEvaluation = async () => {
  if (evaluationForm.value.starRating === 0) {
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

const resetEvaluationForm = () => {
  evaluationForm.value.orderId = null;
  evaluationForm.value.fromUserId = null;
  evaluationForm.value.toUserId = null;
  evaluationForm.value.starRating = 0;
  evaluationForm.value.comment = '';
};
</script>
<style scoped>
.el-button+.el-button {
  margin-left: 8px;
}
</style> 