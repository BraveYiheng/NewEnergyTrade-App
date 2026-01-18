<template>
  <div class="purchaser-info">
    <h1>个人信息管理</h1>
    <el-card class="box-card">
      <el-descriptions title="基本信息" :column="1" border>
        <el-descriptions-item label="联系人">{{ userInfo.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ userInfo.companyName }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ userInfo.contactNumber }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
      </el-descriptions>
      <div class="card-footer">
        <el-button type="primary" @click="openEditInfoDialog">修改基本信息</el-button>
        <el-button type="warning" @click="openEditPasswordDialog">修改密码</el-button>
      </div>
    </el-card>

    <!-- 修改基本信息对话框 -->
    <el-dialog v-model="editInfoDialogVisible" title="修改基本信息" width="500px">
      <el-form :model="editInfoForm" label-width="120px">
        <el-form-item label="联系人">
          <el-input v-model="editInfoForm.contactPerson" />
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="editInfoForm.companyName" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="editInfoForm.contactNumber" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editInfoForm.email" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editInfoDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBasicInfo">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="editPasswordDialogVisible" title="修改密码" width="500px">
      <el-form :model="passwordForm" label-width="120px" :rules="passwordRules" ref="passwordFormRef">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input type="password" v-model="passwordForm.oldPassword" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input type="password" v-model="passwordForm.password" show-password />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input type="password" v-model="passwordForm.confirmPassword" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editPasswordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updatePassword">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

const { proxy } = getCurrentInstance();
const router = useRouter();

const userInfo = ref({});
const editInfoDialogVisible = ref(false);
const editInfoForm = reactive({
  userId: null,
  contactPerson: '',
  companyName: '',
  contactNumber: '',
  email: ''
});

const editPasswordDialogVisible = ref(false);
const passwordFormRef = ref(null);
const passwordForm = reactive({
  userId: null,
  oldPassword: '',
  password: '',
  confirmPassword: ''
});

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入新密码'));
  } else if (passwordForm.confirmPassword !== '') {
    passwordFormRef.value.validateField('confirmPassword');
  }
  callback();
};

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'));
  } else if (value !== passwordForm.password) {
    callback(new Error("两次输入的密码不一致!"));
  } else {
    callback();
  }
};

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  password: [{ validator: validatePass, trigger: 'blur' }],
  confirmPassword: [{ validator: validatePass2, trigger: 'blur' }],
};

onMounted(() => {
  fetchUserInfo();
});

const fetchUserInfo = async () => {
  try {
    const userId = localStorage.getItem('purchaser_userId');
    if (!userId) {
      ElMessage.error('无法获取用户信息，请重新登录');
      router.push('/login');
      return;
    }
    const { data } = await proxy.$http.get(`/user/${userId}`);
    userInfo.value = data;
    editInfoForm.userId = data.userId;
    passwordForm.userId = data.userId;
  } catch (error) {
    ElMessage.error('获取用户信息失败');
    console.error(error);
  }
};

const openEditInfoDialog = () => {
  Object.assign(editInfoForm, userInfo.value);
  editInfoDialogVisible.value = true;
};

const saveBasicInfo = async () => {
  try {
    await proxy.$http.put('/user/updateBasicInfo', editInfoForm);
    ElMessage.success('信息更新成功');
    editInfoDialogVisible.value = false;
    await fetchUserInfo();
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '信息更新失败');
    console.error(error);
  }
};

const openEditPasswordDialog = () => {
  passwordFormRef.value?.resetFields();
  Object.assign(passwordForm, { oldPassword: '', password: '', confirmPassword: '' });
  editPasswordDialogVisible.value = true;
};

const updatePassword = async () => {
  if (!passwordFormRef.value) return;
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await proxy.$http.put('/user/updatePassword', passwordForm);
        ElMessage.success('密码修改成功，请重新登录');
        editPasswordDialogVisible.value = false;
        localStorage.removeItem('purchaser_userId');
        localStorage.removeItem('purchaser_user');
        router.push('/login');
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '密码修改失败');
        console.error(error);
      }
    }
  });
};
</script>

<style scoped>
h1 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}
.box-card {
  padding: 20px;
}
.card-footer {
  margin-top: 20px;
  text-align: left;
}
</style> 