<template>
  <div class="my-products-container">
    <el-tabs v-model="activeTab" class="product-tabs">
      <!-- 我的产品 Tab -->
      <el-tab-pane label="我的产品" name="my-products">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" @click="openAddDialog">添加新产品</el-button>
        </div>
        
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="产品名称">
            <el-input v-model="searchForm.productName" placeholder="输入产品名称关键词" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品类别">
            <el-select v-model="searchForm.productCategoryId" placeholder="请选择类别" clearable>
              <el-option v-for="cat in productCategories" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="clearSearch">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="myProducts" style="width: 100%" border stripe>
          <el-table-column label="产品图片" width="120">
            <template #default="scope">
              <el-image 
                style="width: 100px; height: 100px; border-radius: 4px;"
                :src="`${backendUrl}${scope.row.imagePath}`" 
                fit="cover"
                :preview-src-list="[`${backendUrl}${scope.row.imagePath}`]"
                preview-teleported
                >
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="productName" label="产品名称" width="180"></el-table-column>
          <el-table-column prop="categoryName" label="产品类别" width="120"></el-table-column>
          <el-table-column prop="price" label="价格(元)" width="120"></el-table-column>
          <el-table-column prop="stockQuantity" label="库存" width="120"></el-table-column>
          <el-table-column label="操作" fixed="right" width="150">
            <template #default="scope">
              <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="deleteProduct(scope.row.productId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加/编辑产品对话框 -->
    <el-dialog v-model="productDialogVisible" :title="dialogTitle" width="50%">
      <el-form :model="productForm" label-width="120px" ref="productFormRef">
        <el-form-item label="产品名称" prop="productName" :rules="[{ required: true, message: '请输入产品名称', trigger: 'blur' }]">
          <el-input v-model="productForm.productName"></el-input>
        </el-form-item>
        <el-form-item label="产品类别" prop="productCategoryId" :rules="[{ required: true, message: '请选择产品类别', trigger: 'change' }]">
          <el-select v-model="productForm.productCategoryId" placeholder="请选择类别">
            <el-option v-for="cat in productCategories" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="技术参数" prop="technicalParameters">
          <el-input v-model="productForm.technicalParameters"></el-input>
        </el-form-item>
        <el-form-item label="产品描述" prop="productDescription">
          <el-input type="textarea" v-model="productForm.productDescription"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price" :rules="[{ required: true, message: '请输入价格', trigger: 'blur' }]">
          <el-input-number v-model="productForm.price" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="库存数量" prop="stockQuantity" :rules="[{ required: true, message: '请输入库存', trigger: 'blur' }]">
          <el-input-number v-model="productForm.stockQuantity" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="产品图片" prop="imagePath" :rules="[{ required: true, message: '请上传产品图片', trigger: 'blur' }]">
            <el-upload
                class="product-uploader"
                action="http://localhost:8080/file/upload"
                :show-file-list="false"
                :on-success="handleImageSuccess"
                :before-upload="beforeImageUpload"
              >
                <img v-if="productForm.imagePath" :src="`${backendUrl}${productForm.imagePath}`" class="product-image" />
                <el-icon v-else class="el-icon-plus uploader-icon"><Plus /></el-icon>
              </el-upload>
              <el-text class="el-upload__tip">
                只能上传jpg/png文件，且不超过2MB
              </el-text>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance();
const backendUrl = ref('http://localhost:8080'); // 后端地址

const activeTab = ref('my-products');
const productCategories = ref([]);
const myProducts = ref([]);
const searchForm = reactive({
  productName: '',
  productCategoryId: null,
});

const productDialogVisible = ref(false);
const isEditMode = ref(false);
const dialogTitle = computed(() => isEditMode.value ? '编辑产品' : '添加产品');
const productFormRef = ref(null);

const productForm = reactive({
  productId: null,
  productName: '',
  productCategoryId: null,
  technicalParameters: '',
  productDescription: '',
  price: 0,
  stockQuantity: 0,
  imagePath: '',
  supplierId: parseInt(localStorage.getItem('supplier_userId')),
  imageFile: null
});

const imagePreview = ref('');

onMounted(() => {
  fetchProductCategories();
  fetchMyProducts();
});

const fetchProductCategories = async () => {
  try {
    const { data } = await proxy.$http.get('/productCategory/all');
    if (data) {
      productCategories.value = data;
    }
  } catch (error) {
    ElMessage.error('获取产品分类失败');
    console.error(error);
  }
};

const fetchMyProducts = async () => {
  try {
    const userId = localStorage.getItem('supplier_userId');
    if (!userId) {
      ElMessage.error('无法获取供应商ID，请重新登录。');
      return;
    }
    const { data } = await proxy.$http.get(`/product/user/${userId}`);
    myProducts.value = data || [];
  } catch (error) {
    ElMessage.error('获取我的产品列表失败');
    console.error(error);
    myProducts.value = []; // 失败时清空列表
  }
};

const handleSearch = async () => {
  try {
    const userId = localStorage.getItem('supplier_userId');
    if (!userId) {
      ElMessage.error('无法获取供应商ID，请重新登录。');
      return;
    }
    
    // 构造搜索载荷，只包含有值的字段
    const searchPayload = {
      supplierId: parseInt(userId)
    };
    if (searchForm.productName) {
      searchPayload.productName = searchForm.productName;
    }
    if (searchForm.productCategoryId) {
      searchPayload.productCategoryId = searchForm.productCategoryId;
    }

    const { data } = await proxy.$http.post('/product/search', searchPayload);
    myProducts.value = data || [];
    if (!data || data.length === 0) {
        ElMessage.info('未找到符合条件的产品');
    }
  } catch (error) {
    ElMessage.error('搜索产品失败');
    console.error(error);
    myProducts.value = [];
  }
};

const clearSearch = () => {
  searchForm.productName = '';
  searchForm.productCategoryId = null;
  fetchMyProducts(); // 重置后重新加载所有产品
};

const resetProductForm = () => {
  if (productFormRef.value) {
    productFormRef.value.resetFields();
  }
  Object.assign(productForm, {
    productId: null,
    productName: '',
    productCategoryId: null,
    technicalParameters: '',
    productDescription: '',
    price: 0,
    stockQuantity: 0,
    imagePath: '',
    supplierId: parseInt(localStorage.getItem('supplier_userId')),
    imageFile: null
  });
  imagePreview.value = '';
};

const openAddDialog = () => {
  isEditMode.value = false;
  resetProductForm();
  productDialogVisible.value = true;
};

const openEditDialog = (product) => {
  isEditMode.value = true;
  // 直接从表格数据填充表单
  Object.assign(productForm, product);
  productDialogVisible.value = true;
};

const handleImageChange = (file) => {
  const rawFile = file.raw;
  if (!beforeImageUpload(rawFile)) return false;
  const reader = new FileReader();
  reader.onload = (e) => {
    imagePreview.value = e.target.result;
  };
  reader.readAsDataURL(rawFile);
  productForm.imageFile = rawFile;
};

const saveProduct = async () => {
  if (!productFormRef.value) return;
  await productFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let imagePath = productForm.imagePath;
        if (productForm.imageFile) {
          const formData = new FormData();
          formData.append('file', productForm.imageFile);
          const { data } = await proxy.$http.post('/file/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          });
          imagePath = data; // /uploads/xxx
        }
        const payload = { ...productForm, imagePath };
        delete payload.imageFile;
        if (isEditMode.value) {
          await proxy.$http.put('/product/update', payload);
          ElMessage.success('产品更新成功');
        } else {
          await proxy.$http.post('/product/add', payload);
          ElMessage.success('产品添加成功');
        }
        productDialogVisible.value = false;
        await fetchMyProducts();
      } catch (error) {
        ElMessage.error('操作失败，请检查输入或联系管理员');
        console.error(error);
      }
    } else {
      ElMessage.error('请填写所有必填项');
      return false;
    }
  });
};

const deleteProduct = (productId) => {
  ElMessageBox.confirm('确定要删除这个产品吗？此操作不可逆。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await proxy.$http.delete(`/product/${productId}`);
      ElMessage.success('删除成功');
      await fetchMyProducts(); // Refresh the list
    } catch (error) {
      ElMessage.error('删除失败');
      console.error(error);
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};

const handleImageSuccess = (response, uploadFile) => {
  // 后端返回的是图片的相对路径，保存到表单中
  productForm.imagePath = response;
  ElMessage.success('图片上传成功');
};

const beforeImageUpload = (rawFile) => {
  const isJpgOrPng = rawFile.type === 'image/jpeg' || rawFile.type === 'image/png';
  if (!isJpgOrPng) {
    ElMessage.error('图片只能是 JPG/PNG 格式!');
    return false;
  }
  const isLt2M = rawFile.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
};
</script>

<style scoped>
.my-products-container {
  padding: 20px;
}
.search-form {
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 8px;
    margin-bottom: 20px;
}
.product-uploader .product-image {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.product-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.product-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.el-icon.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style> 