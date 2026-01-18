<template>
  <div class="purchaser-product-container">
    <el-tabs v-model="activeTab" class="product-tabs">
      <!-- 产品分类 Tab -->
      <el-tab-pane label="产品分类" name="categories">
        <el-row :gutter="20" class="category-list">
          <el-col :span="8" v-for="category in productCategories" :key="category.categoryId" style="margin-bottom: 20px;">
            <el-card shadow="hover" class="category-card">
              <template #header>
                <div class="card-header">
                  <span>{{ category.categoryName }}</span>
                </div>
              </template>
              <div class="category-content">
                <el-image :src="`${backendUrl}${category.imageUrl}`" fit="cover" class="category-image" />
                <p class="category-description">{{ category.categoryDescription }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- 搜索产品 Tab -->
      <el-tab-pane label="搜索产品" name="products">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="产品名称">
            <el-input v-model="searchForm.productName" placeholder="输入产品名称关键词" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品类别">
            <el-select v-model="searchForm.productCategoryId" placeholder="请选择类别" clearable>
              <el-option v-for="cat in productCategories" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="价格区间">
            <el-input-number v-model="searchForm.minPrice" :min="0" controls-position="right" placeholder="最低价" style="width: 120px;"></el-input-number>
            <span style="margin: 0 10px;">-</span>
            <el-input-number v-model="searchForm.maxPrice" :min="0" controls-position="right" placeholder="最高价" style="width: 120px;"></el-input-number>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchProducts">搜索</el-button>
            <el-button @click="clearSearch">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="searchResults" style="width: 100%" border stripe>
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
          <el-table-column prop="supplierName" label="供应商" width="120"></el-table-column>
          <el-table-column prop="companyName" label="供应商公司" width="180"></el-table-column>
          <el-table-column prop="price" label="价格(元)" width="120"></el-table-column>
          <el-table-column prop="stockQuantity" label="库存" width="120"></el-table-column>
          <el-table-column label="操作" fixed="right" width="120">
            <template #default="scope">
              <el-button  @click="viewProductDetails(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    
    <!-- 产品详情对话框 -->
    <el-dialog v-model="productDetailVisible" title="产品详情" width="600px">
        <div v-if="selectedProduct" class="product-details">
            <h3>{{ selectedProduct.productName }}</h3>
            <p><strong>供应商:</strong> {{ selectedProduct.companyName }}</p>
            <p><strong>联系人:</strong> {{ selectedProduct.supplierName }}</p>
            <p><strong>联系电话:</strong> {{ selectedProduct.contactPhone }}</p>
            <p><strong>邮箱:</strong> {{ selectedProduct.email }}</p>
            <p><strong>技术参数:</strong> {{ selectedProduct.technicalParameters }}</p>
            <p><strong>产品描述:</strong> {{ selectedProduct.productDescription }}</p>
        </div>
        <template #footer>
            <el-button type="primary" @click="productDetailVisible = false">关闭</el-button>
        </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
import { ElMessage } from 'element-plus';

const { proxy } = getCurrentInstance();
const backendUrl = ref('http://localhost:8080'); // 后端地址，用于加载图片

const activeTab = ref('categories');
const productCategories = ref([]);
const searchResults = ref([]);

const searchForm = reactive({
  productName: '',
  productCategoryId: null,
  minPrice: null,
  maxPrice: null
});

const productDetailVisible = ref(false);
const selectedProduct = ref(null);

onMounted(() => {
  fetchProductCategories();
  fetchAllProducts();
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

const fetchAllProducts = async () => {
  try {
    const { data } = await proxy.$http.get('/product/all');
    if (data) {
      searchResults.value = data;
    } else {
      searchResults.value = [];
    }
  } catch (error) {
    ElMessage.error('获取产品列表失败');
    console.error(error);
  }
};

const searchProducts = async () => {
  try {
    const searchPayload = Object.fromEntries(
        Object.entries(searchForm).filter(([, value]) => value !== null && value !== '')
    );
    const { data } = await proxy.$http.post('/product/search', searchPayload);
    searchResults.value = data || [];
    ElMessage.success('搜索完成');
  } catch (error) {
    ElMessage.error('搜索产品失败');
    console.error(error);
  }
};

const clearSearch = () => {
    searchForm.productName = '';
    searchForm.productCategoryId = null;
    searchForm.minPrice = null;
    searchForm.maxPrice = null;
    fetchAllProducts();
};

const viewProductDetails = (product) => {
    selectedProduct.value = product;
    productDetailVisible.value = true;
};

</script>

<style scoped>
.purchaser-product-container {
  padding: 20px;
}
.category-list {
  padding-top: 20px;
}
.category-card {
  cursor: pointer;
}
.category-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}
.category-description {
  margin-top: 15px;
  color: #666;
  font-size: 14px;
  min-height: 40px;
}
.search-form {
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 8px;
    margin-bottom: 20px;
}
.product-details p {
    margin: 10px 0;
    font-size: 16px;
}
</style> 