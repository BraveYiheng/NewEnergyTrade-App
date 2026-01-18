// 屏蔽 ResizeObserver loop completed with undelivered notifications 报错
const ignoreResizeObserverError = () => {
    const errorHandler = (e) => {
      if (e.message && e.message.includes('ResizeObserver loop')) {
        e.stopImmediatePropagation();
      }
    };
    window.addEventListener('error', errorHandler, true);
  };
  ignoreResizeObserverError();
// 导入vue
import { createApp } from "vue";

// 导入element-plus
import ElementPlus from "element-plus"; // 组件的引入
import "element-plus/dist/index.css"; //样式引入

import "@/assets/global.css"; // 全局样式

// 导入路由器
import router from "./router";

// 导入axios
import axios from "axios";

// 导入App组件
import App from "./App.vue";

// 创建app实例
const app = createApp(App);

// 为axios配置请求根路径
 axios.defaults.baseURL = 'http://localhost:8080'

// 挂在到全局属性中方便调用
app.config.globalProperties.$http = axios; // $http自定义属性   相当于 axios

// 使用element-plus
app.use(ElementPlus);

// 使用路由器
app.use(router);

// app实例挂载到id为app的元素上
app.mount("#app");
