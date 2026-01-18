<template>
  <div class="evaluation-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>交易评价管理</span>
          <el-text class="mx-1">管理您发表的评价，查看收到的评价</el-text>
        </div>
      </template>
      <el-tabs v-model="evaluationActiveTab">
        <el-tab-pane label="我写的评价" name="myEvaluation">
          <el-table :data="myEvaluations" style="width: 100%" stripe>
            <el-table-column prop="orderNumber" label="订单编号" width="200"></el-table-column>
            <el-table-column label="星级" width="180">
              <template #default="scope">
                <el-rate v-model="scope.row.starRating" disabled show-score text-color="#ff9900" score-template="{value} 星" />
              </template>
            </el-table-column>
            <el-table-column prop="toUserCompanyName" label="评价的公司" show-overflow-tooltip></el-table-column>
            <el-table-column prop="comment" label="评价内容" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deleteEvaluation(scope.row.evaluationId)" :icon="Delete">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <el-empty description="您还没有写过任何评价" />
            </template>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="我收到的评价" name="otherEvaluation">
          <el-table :data="otherEvaluations" style="width: 100%" stripe>
            <el-table-column prop="orderNumber" label="订单编号" width="200"></el-table-column>
            <el-table-column label="星级" width="180">
              <template #default="scope">
                <el-rate v-model="scope.row.starRating" disabled show-score text-color="#ff9900" score-template="{value} 星" />
              </template>
            </el-table-column>
            <el-table-column prop="fromUserCompanyName" label="评价方公司" show-overflow-tooltip></el-table-column>
            <el-table-column prop="comment" label="评价内容" show-overflow-tooltip></el-table-column>
            <el-table-column prop="createTime" label="评价时间" :formatter="formatDate" width="180"></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deleteEvaluation(scope.row.evaluationId)" :icon="Delete">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <el-empty description="您还没有收到任何评价" />
            </template>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance();

const evaluationActiveTab = ref('myEvaluation');
const myEvaluations = ref([]);
const otherEvaluations = ref([]);

onMounted(() => {
  fetchMyEvaluations();
  fetchOtherEvaluations();
});

const fetchMyEvaluations = async () => {
  const userId = localStorage.getItem('supplier_userId');
  try {
    const { data } = await proxy.$http.get(`/evaluation/fromUser/${userId}`);
    myEvaluations.value = data;
  } catch (error) {
    ElMessage.error('获取我写的评价数据失败');
  }
};

const fetchOtherEvaluations = async () => {
  const userId = localStorage.getItem('supplier_userId');
  try {
    const { data } = await proxy.$http.get(`/evaluation/toUser/${userId}`);
    otherEvaluations.value = data;
  } catch (error) {
    ElMessage.error('获取其他用户对我的评价数据失败');
  }
};

const formatDate = (row, column, cellValue) => {
  if (cellValue) {
    const date = new Date(cellValue);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }
  return '';
};

const deleteEvaluation = (evaluationId) => {
  ElMessageBox.confirm('是否确定删除该评论?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await proxy.$http.delete(`/evaluation/${evaluationId}`);
      ElMessage.success('删除成功');
      fetchMyEvaluations();
      fetchOtherEvaluations();
    } catch (error) {
      ElMessage.error('删除评论失败');
    }
  }).catch(() => {});
};
</script>

<style scoped>
.evaluation-container {
  padding: 20px;
  background-color: #f5f7fa;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-header span {
  font-size: 18px;
  font-weight: 600;
}
.el-rate {
  position: relative;
  top: 4px; /* Vertically align with text in other cells */
}
</style> 