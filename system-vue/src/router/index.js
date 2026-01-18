// 引入函数 createMemoryHistory 制路由跳转方式  createRouter 创建路由器

/* 路由器：
   包含多个路由配置路由目的：
   让组件和指定的路径绑定在一起当访问路径的时候，就会加载该组件 */

import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import SupplierLayout from '../views/SupplierLayout.vue'
import SupplierMainPage from '../views/supplier/SupplierMainPage.vue'
import SupplierInfo from '../views/supplier/SupplierInfo.vue'
import ProductBrowsePage from '../views/supplier/Products.vue'
import MyProducts from '../views/supplier/MyProducts.vue'
import SupplierOrder from '../views/supplier/SupplierOrder.vue'
import SupplierEvaluation from '../views/supplier/SupplierEvaluation.vue'
import SupplierFeedback from '../views/supplier/SupplierFeedback.vue'
import PurchaserLayout from '../views/PurchaserLayout.vue'
import PurchaserInfo from '../views/purchaser/PurchaserInfo.vue'
import PurchaserProduct from '../views/purchaser/PurchaserProduct.vue'
import PurchaserOrder from '../views/purchaser/PurchaserOrder.vue'
import PurchaserEvaluation from '../views/purchaser/PurchaserEvaluation.vue'
import PurchaserFeedback from '../views/purchaser/PurchaserFeedback.vue'
import PurchaserMainPage from '../views/purchaser/PurchaserMainPage.vue'


// 配置路由规则
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/supplier',
    name: 'Supplier',
    component: SupplierLayout,
    redirect: '/supplier/mainPage',
    children: [
      {
        path: 'mainPage',
        name: 'SupplierMainPage',
        component: SupplierMainPage
      },
      {
        path: 'info',
        name: 'SupplierInfo',
        component: SupplierInfo
      },
      {
        path: 'products',
        name: 'Products',
        component: ProductBrowsePage
      },
      {
        path: 'my-products',
        name: 'MyProducts',
        component: MyProducts
      },
      {
        path: 'orders',
        name: 'SupplierOrder',
        component: SupplierOrder
      },
      {
        path: 'evaluations',
        name: 'SupplierEvaluation',
        component: SupplierEvaluation
      },
      {
        path: 'feedback',
        name: 'SupplierFeedback',
        component: SupplierFeedback
      },
     
     
    ]
  },
  {
    path: '/purchaser',
    name: 'Purchaser',
    component: PurchaserLayout,
    redirect: '/purchaser/mainPage',
    children: [
      {
        path: 'mainPage',
        name: 'PurchaserMainPage',
        component: PurchaserMainPage
      },
      {
        path: 'info',
        name: 'PurchaserInfo',
        component: PurchaserInfo
      },
      {
        path: 'products',
        name: 'PurchaserProducts',
        component: PurchaserProduct
      },
      {
        path: 'orders',
        name: 'PurchaserOrder',
        component: PurchaserOrder
      },
      {
        path: 'evaluations',
        name: 'PurchaserEvaluation',
        component: PurchaserEvaluation
      },
      {
        path: 'feedback',
        name: 'PurchaserFeedback',
        component: PurchaserFeedback
      },
      // 这里后续会添加采购商子页面
    ]
  }
]

// 创建路由器
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 导出路由器
export default router;
