new Vue({
    el: "#app",
    data() {
        return {
            // 用户管理
            userId: 0,
            currentSection: 'product',
            userInfo: null,
            showEditBasicInfoDialog: false,
            showEditPasswordDialog: false,
            editBasicInfoForm: {
                userId: 0,
                contactPerson: '',
                companyName: '',
                contactNumber: '',
                email: ''
            },
            passwordForm: {
                userId: 0,
                oldPassword: '',
                password: '',
                confirmPassword: ''
            },
            rules: {
                contactPerson: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
                ],
                companyName: [
                    { required: true, message: '请输入企业名称', trigger: 'blur' },
                    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
                ],
                contactNumber: [
                    { required: true, message: '请输入联系方式', trigger: 'blur' },
                    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
                ]
            },
            passwordRules: {
                oldPassword: [
                    { required: true, message: '请输入原密码', trigger: 'blur' },
                    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请再次输入新密码', trigger: 'blur' },
                    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
                ]
            },
            //商城
            activeTab: 'categoryDisplay',             //当前展示
            productCategories: ['全部', '光伏板', '风力叶片', '储能电池'],
            currentCategory: '',
            productDetailsDialogVisible: false,       // 控制类别详情对话框显示与否
            currentProductDetails: null,              // 用于存储当前点击产品的详情信息
            searchForm: {          //选择类别
                keyword: '',        //关键字
                productCategory: '',       //类别
                minPrice: null,
                maxPrice: null
            },
            searchResults: [],
            // 个人订单
            orderActiveTab: 'allOrders',
            orders: [],
            unpaidOrders: [],           //未支付
            toDeliveredOrders: [],    // 存放待发货订单数据
            deliveredOrders: [],    // 存放已发货订单数据
            completedOrders: [],    // 存放已完成订单数据
            evaluationDialogVisible: false,  // 评价对话框是否显示
            evaluationForm: {
                evaluationId: 0,
                orderId: 0,
                fromUserId: 0,
                toUserId: 0,
                starRating: 0,
                comment: "",
            },
            //交易评价
            evaluationActiveTab: "myEvaluation",
            myEvaluations: [], // 存放我写的评价数据
            otherEvaluations: [], // 存放其他用户对我的评价数据
            //用户反馈
            feedbackForm: {
                content: ''
            },
            feedbackRecords: [],
            loading: false, // 添加loading状态
            carouselItems: [
                { img: '../images/solarPanel.jpg', title: '高效太阳能板' },
                { img: '../images/windBlade.jpg', title: '风力发电机叶片' },
                { img: '../images/battery.jpg', title: '储能电池新品' }
            ],
            recommendedProducts: [
                { productId: 1, productName: '高效太阳能板', price: 299.99, stockQuantity: 50, img: '../images/solarPanel.jpg' },
                { productId: 2, productName: '风力发电机叶片', price: 499.00, stockQuantity: 30, img: '../images/windBlade.jpg' },
                { productId: 3, productName: '储能电池', price: 199.00, stockQuantity: 80, img: '../images/battery.jpg' },
                { productId: 4, productName: '智能逆变器', price: 399.00, stockQuantity: 20, img: '../images/solarPanel.jpg' }
            ]
        }
    },
    mounted() {
        // 在组件挂载完成后，将 userInfo 的值赋给 editBasicInfoForm
        //个人信息
        this.initSetInfo();
        //产品管理
        this.searchAllProducts();//加载搜索所有产品
        this.fetchOrders();//加载我的订单
        this.initEvaluations();//初始化评价
        this.fetchFeedbackRecords();//反馈初始化
    },
    methods: {
        //个人信息
        initSetInfo() {
            const urlParams = new URLSearchParams(window.location.search);
            this.userId = parseInt(urlParams.get('id'), 10);
            axios.get(`/user/${this.userId}`).then((response) => {
                this.userInfo = response.data;
                this.editBasicInfoForm.userId = this.userInfo.userId;
                this.editBasicInfoForm.contactPerson = this.userInfo.contactPerson;
                this.editBasicInfoForm.companyName = this.userInfo.companyName;
                this.editBasicInfoForm.contactNumber = this.userInfo.contactNumber;
                this.editBasicInfoForm.email = this.userInfo.email;
                this.passwordForm.userId = this.userInfo.userId;
            })
                .catch((error) => {
                    console.error('用户信息获取出错：', error);
                });
        },
        showSection(section) {
            this.currentSection = section;
        },
        saveEditBasicInfo() {
            this.$refs.editBasicInfoForm.validate(valid => {
                if (valid) {
                    const loading = this.$loading({
                        lock: true,
                        text: '保存中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    axios.put('/user/updateBasicInfo', this.editBasicInfoForm)
                        .then(response => {
                            if (response.status === 200) {
                                this.$message.success('保存成功');
                                this.showEditBasicInfoDialog = false;
                                this.getUserInfo(); // 刷新用户信息
                            } else {
                                this.$message.error('保存失败');
                            }
                        })
                        .catch(error => {
                            console.error('保存失败:', error);
                            this.$message.error('保存失败');
                        })
                        .finally(() => {
                            loading.close();
                        });
                }
            });
        },
        updatePassword() {
            this.$refs.passwordForm.validate(valid => {
                if (valid) {
                    if (this.passwordForm.password !== this.passwordForm.confirmPassword) {
                        this.$message.error('两次输入的密码不一致');
                        return;
                    }
                    const loading = this.$loading({
                        lock: true,
                        text: '修改中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    axios.put('/user/updatePassword', {
                        userId: this.userId,
                        oldPassword: this.passwordForm.oldPassword,
                        password: this.passwordForm.password
                    })
                        .then(response => {
                            if (response.status === 200) {
                                this.$message.success('密码修改成功');
                                this.showEditPasswordDialog = false;
                                this.passwordForm = {
                                    oldPassword: '',
                                    password: '',
                                    confirmPassword: ''
                                };
                            } else {
                                this.$message.error('密码修改失败');
                            }
                        })
                        .catch(error => {
                            console.error('密码修改失败:', error);
                            this.$message.error('密码修改失败');
                        })
                        .finally(() => {
                            loading.close();
                        });
                }
            });
        },
        //产品管理
        getCategoryImage(category) {
            const imageMap = {
                '光伏板': '../images/solarPanel.jpg',
                '风力叶片': '../images/windBlade.jpg',
                '储能电池': '../images/battery.jpg'
            };
            return imageMap[category] || '';
        },//产品类别
        showProductDetailsDialog(category) {
            this.currentCategory = category;
            const details = {
                '光伏板': {
                    description: '高效光伏板，采用先进的太阳能电池技术，具有高转换效率、长寿命、抗恶劣天气等特点。适用于各类太阳能发电系统，是清洁能源利用的理想选择。'
                },
                '风力叶片': {
                    description: '高性能风力发电叶片，采用航空级复合材料制造，具有优异的空气动力学性能和结构强度。适用于各种风力发电设备，能够高效捕获风能。'
                },
                '储能电池': {
                    description: '新一代储能电池系统，采用先进的电池管理技术，具有高能量密度、长循环寿命、安全可靠等特点。适用于新能源发电配套储能、工业储能等场景。'
                }
            };
            this.currentProductDetails = details[category];
            this.productDetailsDialogVisible = true;
        },
        goToSearch() {
            this.activeTab = 'searchFunction';
            this.searchForm.productCategory = this.currentCategory;
            this.productDetailsDialogVisible = false;
            this.$nextTick(() => {
                this.searchProducts();
            });
        },
        searchAllProducts() {
            axios.get('/product/all')
                .then((response) => {
                    this.searchResults = response.data;
                })
                .catch((error) => {
                    console.error('搜索产品出错：', error);
                });
        },
        searchProducts() {
            const loading = this.$loading({
                lock: true,
                text: '搜索中...',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            // 构建查询参数
            const params = {
                productName: this.searchForm.keyword,
                productCategory: this.searchForm.productCategory,
                minPrice: this.searchForm.minPrice,
                maxPrice: this.searchForm.maxPrice
            };
            axios.post('/product/search', params)
                .then(response => {
                    this.searchResults = response.data;
                })
                .catch(error => {
                    console.error('搜索失败:', error);
                    this.$message.error('搜索失败');
                })
                .finally(() => {
                    loading.close();
                });
        },
        resetSearch() {
            this.$refs.searchForm.resetFields();
            this.searchForm = {
                keyword: '',
                productCategory: '',
                minPrice: null,
                maxPrice: null
            };
            this.searchProducts();
        },
        placeOrder(scope) {
            const product = scope.row;
            if (!product.selectedQuantity || product.selectedQuantity <= 0) {
                this.$message.warning('请选择购买数量');
                return;
            }
            if (product.selectedQuantity > product.stockQuantity) {
                this.$message.warning('购买数量不能超过库存数量');
                return;
            }
            this.$confirm(`确认购买 ${product.productName} ${product.selectedQuantity} 个吗？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'info'
            }).then(() => {
                const loading = this.$loading({
                    lock: true,
                    text: '提交订单中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                const userId = this.userId;
                if (!userId) {
                    this.$message.error('用户ID不能为空');
                    loading.close();
                    return;
                }
                const orderData = {
                    purchaserId: userId,
                    productId: product.productId,
                    supplierId: product.supplierId,
                    quantity: product.selectedQuantity,
                    unitPrice: product.price,
                    orderAmount: product.selectedQuantity * product.price,
                    orderStatus: 0, // 初始状态为待付款
                };
                axios.post('/order/add', orderData)
                    .then(response => {
                        if (response.data) {
                            this.$message.success('下单成功');
                            this.searchProducts(); // 刷新商品列表
                        } else {
                            this.$message.error('下单失败');
                        }
                    })
                    .catch(error => {
                        console.error('下单失败:', error);
                        this.$message.error('下单失败');
                    })
                    .finally(() => {
                        loading.close();
                    });
            }).catch(() => {});
        },
        //我的订单
        fetchOrders() {
            const userId = this.userId;
            this.unpaidOrders = [];
            this.toDeliveredOrders = [];
            this.deliveredOrders = [];
            this.completedOrders = [];
            axios.get(`/order/user/${userId}`).then((response) => {
                this.orders = response.data;
                this.orders.forEach(order => {
                    switch (order.orderStatus) {
                        case 0:
                            this.unpaidOrders.push(order);
                            break;
                        case 1:
                            this.toDeliveredOrders.push(order);
                            break;
                        case 2:
                            this.toDeliveredOrders.push(order);
                            break;
                        case 3:
                            this.deliveredOrders.push(order);
                            break;
                        case 4:
                            this.completedOrders.push(order);
                            break;
                    }
                });
            }
            ).catch((error) => {
                console.error(error);
                alert('获取订单出现错误，请稍后再试');
            });
        },
        getStatusText(statusCode) {
            switch (statusCode) {
                case 0:
                    return "待付款";
                case 1:
                    return "待发货";
                case 2:
                    return "待发货";
                case 3:
                    return "已发货";
                case 4:
                    return "已完成";
                default:
                    return "未知状态";
            }
        },
        deleteOrder(orderId) {
            this.$confirm('是否确定删除该订单?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete(`/order/${orderId}`)
                    .then(() => {
                        this.fetchOrders();
                    })
                    .catch(error => {
                        console.error('删除订单失败', error);
                    });
            }).catch(() => {
                // 用户取消删除操作
            });
        },
        trackOrder(order) {
            // 处理跟踪订单逻辑，比如跳转到物流页面等
        },
        goPay(order) { },
        showEvaluationDialog(order) {
            this.evaluationForm.orderId = order.orderId;
            this.evaluationForm.fromUserId = this.userId
            this.evaluationForm.toUserId = order.supplierId;
            this.evaluationDialogVisible = true;
        },
        submitEvaluation() {
            const evaluationData = this.evaluationForm;
            axios.post('/evaluation/submit', evaluationData).then(response => {
                this.fetchOrders();
                this.evaluationDialogVisible = false;
                this.resetEvaluationForm();
            }).catch(error => {
                console.error('提交评价失败', error);
            });
        },
        resetEvaluationForm() {
            this.evaluationForm = {
                starRating: 0,
                comment: '',
                orderId: null,
                toUserId: null,
            };
        },
        formatDate(row, column, cellValue, index) {
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
        },
        fetchMyEvaluations() {
            const id = this.userId;
            axios.get(`/evaluation/fromUser/${id}`).then(response => {
                this.myEvaluations = response.data;
            }).catch(error => {
                console.error('获取我写的评价数据失败', error);
            });
        },
        fetchOtherEvaluations() {
            const id = this.userId;
            axios.get(`/evaluation/toUser/${id}`).then(response => {
                this.otherEvaluations = response.data;
            }).catch(error => {
                console.error('获取其他用户对我的评价数据失败', error);
            });
        },
        initEvaluations() {
            this.fetchMyEvaluations();
            this.fetchOtherEvaluations();
        },
        deleteEvaluation(evaluationId) {
            console.log('删除评价ID:', evaluationId);
            if (!evaluationId) {
                this.$message.error('评价ID无效，无法删除');
                return;
            }
            this.$confirm('是否确定删除该评论?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete(`/evaluation/${evaluationId}`)
                    .then(res => {
                        if (res.data === true) {
                            this.$message.success('删除成功');
                            this.initEvaluations();
                        } else {
                            this.$message.error('删除失败，评价不存在或已被删除');
                        }
                    })
                    .catch(error => {
                        console.error('删除评论失败', error);
                        this.$message.error('删除失败，服务器异常');
                    });
            }).catch(() => {
                // 用户取消删除操作
            });
        },
        submitFeedback() {
            if (!this.feedbackForm.content.trim()) {
                this.$message({
                    message: '反馈内容不能为空，请填写后再提交',
                    type: 'warning'
                });
                return;
            }
            const feedback = {
                content: this.feedbackForm.content,
                userId: this.userId
            };
            axios.post('/feedback/add', feedback).then(response => {
                if (response.data) {
                    this.feedbackForm.content = '';
                    this.fetchFeedbackRecords();
                    this.$message({
                        message: '反馈提交成功',
                        type: 'success'
                    });
                } else {
                    this.$message.error('反馈提交失败，请稍后再试');
                }
            }).catch(error => {
                console.error('反馈提交出错', error);
                this.$message.error('反馈提交出现系统错误，请联系管理员');
            });
        },
        fetchFeedbackRecords() {
            this.loading = true;
            axios.get(`/feedback/user/${this.userId}`)
                .then(response => {
                    this.feedbackRecords = response.data;
                })
                .catch(error => {
                    console.error('获取反馈记录出错', error);
                    this.$message.error('获取反馈记录出现问题，请稍后再试');
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        deleteFeedback(feedbackId) {
            this.$confirm('是否确定删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.delete(`/feedback/${feedbackId}`)
                    .then(() => {
                        this.fetchFeedbackRecords();
                    })
                    .catch(error => {
                        console.error('删除失败', error);
                    });
            }).catch(() => {
                // 用户取消删除操作
            });
        },
        getUserInfo() {
            const userId = this.userId;
            if (!userId) {
                this.$message.error('用户ID不能为空');
                return;
            }
            axios.get(`/user/${userId}`)
                .then(response => {
                    this.userInfo = response.data;
                    Object.assign(this.editBasicInfoForm, this.userInfo);
                })
                .catch(error => {
                    console.error('获取用户信息失败:', error);
                    this.$message.error('获取用户信息失败');
                });
        },
        getUrlParam(name) {
            const queryString = window.location.search;
            const urlParams = new URLSearchParams(queryString);
            return urlParams.get(name);
        },
        getStatusType(status) {
            switch(status) {
                case '待处理':
                    return 'warning';
                case '处理中':
                    return 'primary';
                case '已完成':
                    return 'success';
                default:
                    return 'info';
            }
        },
        formatDate(timestamp) {
            if (!timestamp) return '';
            const date = new Date(timestamp);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        },
    }
}); 