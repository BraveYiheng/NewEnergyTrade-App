<template>
  <div class="center-container">
    <!-- 消息提示条 -->
    <div :class="['message-bar', { show: messageBar.show }]">{{ messageBar.text }}</div>

    <!-- 登录表单 -->
    <transition name="fade">
      <div class="form-card" v-show="isLoginView">
        <h1>用户登录</h1>
        <el-form :model="loginForm" @submit.prevent="handleLogin">
          <el-form-item label="邮箱" :error="loginEmailError">
            <el-input type="email" v-model="loginForm.email" @blur="validateLoginEmail" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="密码" :error="loginPasswordError">
            <el-input :type="showLoginPassword ? 'text' : 'password'" v-model="loginForm.password" @blur="validateLoginPassword" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" native-type="submit" :loading="loginLoading" style="width: 100%;">
              {{ loginLoading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
          <p class="switch-link" @click="toggleView">还没有账号？去注册</p>
        </el-form>
      </div>
    </transition>

    <!-- 注册表单 -->
    <transition name="fade">
      <div class="form-card" v-show="!isLoginView">
        <h1>用户注册</h1>
        <el-form :model="registerForm" @submit.prevent="handleRegister">
          <el-form-item label="企业名称" :error="companyNameError">
            <el-input v-model="registerForm.companyName" @blur="validateCompanyName" placeholder="请输入企业名称" />
          </el-form-item>
          <el-form-item label="联系人" :error="contactPersonError">
            <el-input v-model="registerForm.contactPerson" @blur="validateContactPerson" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="联系方式" :error="contactNumberError">
            <el-input v-model="registerForm.contactNumber" @blur="validateContactNumber" placeholder="请输入联系方式" />
          </el-form-item>
          <el-form-item label="邮箱" :error="registerEmailError">
            <el-input type="email" v-model="registerForm.email" @blur="checkEmailExist" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="密码" :error="registerPasswordError">
            <el-input :type="showRegisterPassword ? 'text' : 'password'" v-model="registerForm.password" @blur="validateRegisterPassword" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item label="角色" :error="roleError">
            <el-select v-model="registerForm.role" @change="validateRole" placeholder="请选择角色" style="width:100%;">
              <el-option label="供应商" value="0" />
              <el-option label="采购商" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" native-type="submit" :loading="registerLoading" :disabled="registerButtonDisabled" style="width: 100%;">
              {{ registerLoading ? '注册中...' : '注册' }}
            </el-button>
          </el-form-item>
          <p class="switch-link" @click="toggleView">已有账号？去登录</p>
        </el-form>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive, computed, getCurrentInstance } from 'vue';
import { useRouter } from 'vue-router';

const { proxy } = getCurrentInstance();
const router = useRouter();

// 视图切换
const isLoginView = ref(true);
const toggleView = () => {
  isLoginView.value = !isLoginView.value;
};

// 消息提示
const messageBar = reactive({
  show: false,
  text: ''
});
const showMessage = (text) => {
  messageBar.text = text;
  messageBar.show = true;
  setTimeout(() => {
    messageBar.show = false;
  }, 3000);
};

// --- 登录逻辑 ---
const loginForm = reactive({ email: '', password: '' });
const showLoginPassword = ref(false);
const loginEmailError = ref('');
const loginPasswordError = ref('');
const loginLoading = ref(false);

const validateEmailFormat = (email) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);

const validateLoginEmail = () => {
  if (!loginForm.email) {
    loginEmailError.value = '邮箱不能为空';
  } else if (!validateEmailFormat(loginForm.email)) {
    loginEmailError.value = '邮箱格式不正确';
  } else {
    loginEmailError.value = '';
  }
};

const validateLoginPassword = () => {
  if (!loginForm.password) {
    loginPasswordError.value = '密码不能为空';
  } else {
    loginPasswordError.value = '';
  }
};

const handleLogin = async () => {
  validateLoginEmail();
  validateLoginPassword();
  if (loginEmailError.value || loginPasswordError.value) {
    return;
  }
  loginLoading.value = true;
  try {
    const response = await proxy.$http.post('/user/login', loginForm);
    console.log(response)
    if (response.data.success) {
      const user = response.data.user;
      showMessage('登录成功！');
      // 按角色分别存储
      if (user.role == 0) { // 供应商
        localStorage.setItem('supplier_userId', user.userId);
        localStorage.setItem('supplier_user', JSON.stringify(user));
        router.push('/supplier');
      } else if (user.role == 1) { // 采购商
        localStorage.setItem('purchaser_userId', user.userId);
        localStorage.setItem('purchaser_user', JSON.stringify(user));
        router.push('/purchaser');
      }
    } else {
      showMessage(response.data.message || '登录失败');
    }
  } catch (error) {
    showMessage(error.response?.data?.message || '登录失败，请检查邮箱或密码');
  } finally {
    loginLoading.value = false;
  }
};


// --- 注册逻辑 ---
const registerForm = reactive({
  companyName: '',
  contactPerson: '',
  contactNumber: '',
  email: '',
  password: '',
  role: '0'
});
const showRegisterPassword = ref(false);
const companyNameError = ref('');
const contactPersonError = ref('');
const contactNumberError = ref('');
const registerEmailError = ref('');
const registerPasswordError = ref('');
const roleError = ref('');
const registerLoading = ref(false);

const validateCompanyName = () => { companyNameError.value = registerForm.companyName ? '' : '企业名称不能为空'; };
const validateContactPerson = () => { contactPersonError.value = registerForm.contactPerson ? '' : '联系人不能为空'; };
const validateContactNumber = () => { contactNumberError.value = registerForm.contactNumber ? '' : '联系方式不能为空'; };
const validateRegisterPassword = () => { registerPasswordError.value = registerForm.password.length >= 6 ? '' : '密码长度不能少于6位'; };
const validateRole = () => { roleError.value = registerForm.role ? '' : '必须选择一个角色'; };

const checkEmailExist = async () => {
  if (!registerForm.email) {
    registerEmailError.value = '邮箱不能为空';
  } else if (!validateEmailFormat(registerForm.email)) {
    registerEmailError.value = '邮箱格式不正确';
  } else {
    try {
      const response = await proxy.$http.get(`/user/checkEmail?email=${registerForm.email}`);
      if (response.data) {
        registerEmailError.value = '该邮箱已被注册';
      } else {
        registerEmailError.value = '';
      }
    } catch (error) {
      console.error(error);
      registerEmailError.value = '验证邮箱时发生错误';
    }
  }
};

const registerButtonDisabled = computed(() => {
  return !registerForm.companyName || !registerForm.contactPerson || !registerForm.contactNumber || !registerForm.email || !registerForm.password || !registerForm.role || registerEmailError.value;
});

const handleRegister = async () => {
  // 触发所有验证
  validateCompanyName();
  validateContactPerson();
  validateContactNumber();
  await checkEmailExist();
  validateRegisterPassword();
  validateRole();

  if (companyNameError.value || contactPersonError.value || contactNumberError.value || registerEmailError.value || registerPasswordError.value || roleError.value) {
    return;
  }
  
  registerLoading.value = true;
  try {
    const response = await proxy.$http.post('/user/register', registerForm);
    if (response.data) {
      showMessage('注册成功！将切换到登录页。');
      setTimeout(() => {
        toggleView();
      }, 2000);
    } else {
      showMessage('注册失败，请稍后重试');
    }
  } catch (error) {
    showMessage(error.response?.data?.message || '注册失败，请稍后重试');
  } finally {
    registerLoading.value = false;
  }
};
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

.center-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}

.form-card {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 6px 32px 0 rgba(0, 0, 0, 0.08);
  padding: 48px 40px 36px 40px;
  width: 410px;
  max-width: 98vw;
  position: relative;
  transition: box-shadow 0.3s;
  box-sizing: border-box;
}

.form-card h1 {
  text-align: center;
  margin-bottom: 32px;
  color: #409eff;
  font-size: 2.3rem;
  font-weight: 800;
  letter-spacing: 2px;
}

.switch-link {
  color: #409eff;
  cursor: pointer;
  text-align: center;
  margin-top: 18px;
  display: block;
  font-size: 16px;
  transition: color 0.2s;
}

.switch-link:hover {
  color: #66b1ff;
  text-decoration: underline;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-form-item :deep(.el-form-item__label) {
  font-weight: 600;
  font-size: 1.08rem;
  color: #333;
  line-height: 1.5;
  margin-bottom: 8px;
}

.el-input :deep(input) {
  height: 44px;
  border-radius: 12px;
}

.el-button {
  border-radius: 22px;
  font-size: 1.15rem;
  padding: 20px 0;
  font-weight: 700;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.4s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.message-bar {
  position: fixed;
  top: -60px; /* Start hidden above */
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  background: #409eff;
  color: #fff;
  text-align: center;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: top 0.5s ease-in-out;
  display: block;
}

.message-bar.show {
  top: 20px; /* Slide in to view */
}

@media (max-width: 600px) {
  .form-card {
    padding: 24px 5vw;
    width: 90vw;
  }
  .form-card h1 {
    font-size: 1.8rem;
  }
}
</style> 