<template>
  <div class="feedback-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never" class="form-card">
          <template #header>
            <div class="card-header">
              <span>提交意见或建议</span>
            </div>
          </template>
          <el-form :model="feedbackForm" label-position="top" ref="feedbackFormRef">
            <el-form-item label="反馈内容" prop="content" :rules="[{ required: true, message: '反馈内容不能为空', trigger: 'blur' }]">
              <el-input 
                v-model="feedbackForm.content" 
                type="textarea" 
                :rows="8" 
                placeholder="我们期待听到您的声音，请在此处填写您的宝贵意见或建议。"
                show-word-limit
                maxlength="500"
              />
            </el-form-item>
            <el-form-item>
              <div class="form-actions">
                <el-button type="primary" @click="submitFeedback" :icon="Position">提交反馈</el-button>
                <el-button @click="resetForm">重置</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-card shadow="never" class="records-card">
          <template #header>
            <div class="card-header">
              <span>我的反馈记录</span>
            </div>
          </template>
          <el-table :data="feedbackRecords" style="width: 100%" stripe>
            <el-table-column type="index" label="序号" width="80"></el-table-column>
            <el-table-column prop="content" label="反馈内容" show-overflow-tooltip></el-table-column>
            <el-table-column prop="status" label="处理状态" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status || '待处理' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deleteFeedback(scope.row.feedbackId)" :icon="Delete">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <el-empty description="您还没有提交过任何反馈" />
            </template>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Position } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance();
const feedbackFormRef = ref(null);

const feedbackForm = reactive({
  content: ''
});
const feedbackRecords = ref([]);

onMounted(() => {
  fetchFeedbackRecords();
});

const submitFeedback = async () => {
  if (!feedbackFormRef.value) return;
  await feedbackFormRef.value.validate(async (valid) => {
    if (valid) {
      const userId = localStorage.getItem('purchaser_userId');
      const feedback = {
        content: feedbackForm.content,
        userId
      };
      try {
        const { data } = await proxy.$http.post('/feedback/add', feedback);
        if (data) {
          resetForm();
          fetchFeedbackRecords();
          ElMessage.success('反馈提交成功，感谢您的支持！');
        } else {
          ElMessage.error('反馈提交失败，请稍后再试');
        }
      } catch (error) {
        ElMessage.error('反馈提交出现系统错误，请联系管理员');
      }
    }
  });
};

const fetchFeedbackRecords = async () => {
  const userId = localStorage.getItem('purchaser_userId');
  try {
    const { data } = await proxy.$http.get(`/feedback/user/${userId}`);
    feedbackRecords.value = data;
  } catch (error) {
    ElMessage.error('获取反馈记录出现问题，请稍后再试');
  }
};

const deleteFeedback = (feedbackId) => {
  ElMessageBox.confirm('确定要删除这条反馈记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await proxy.$http.delete(`/feedback/${feedbackId}`);
      fetchFeedbackRecords();
      ElMessage.success('删除成功');
    } catch (error) {
      ElMessage.error('删除失败');
    }
  }).catch(() => {});
};

const resetForm = () => {
  if (feedbackFormRef.value) {
    feedbackFormRef.value.resetFields();
  }
};

const getStatusType = (status) => {
  switch(status) {
    case '已处理': return 'success';
    case '处理中': return 'primary';
    case '待处理': return 'warning';
    default: return 'info';
  }
};
</script>

<style scoped>
.feedback-container {
  padding: 20px;
  background-color: #f5f7fa;
  height: calc(100vh - 100px); /* Adjust based on overall layout */
  box-sizing: border-box;
}
.form-card, .records-card {
  height: 100%;
}
.card-header {
  font-size: 18px;
  font-weight: 600;
}
.form-actions {
  width: 100%;
  text-align: right;
}
</style> 