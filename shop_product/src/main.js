// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import qs from 'qs'
import axios from 'axios'
import ElementUI from 'element-ui';

import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题

//进行axios的全局配置   允许转发cookie
axios.defaults.withCredentials = true

Vue.prototype.$qs = qs
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.use(ElementUI, {
  size: 'small'
});

//设置axios 的请求拦截器
axios.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么，例如加入token
  //获取浏览器中的token
  //debugger
  let userInfo=window.sessionStorage.getItem("user"); //可能有没有值
  if(userInfo!=null){
    //将请求中加入token参数
    //将字符串转为json对象
    let token=JSON.parse(userInfo).token;
    //设置token为每次请求都有的参数
    config.params={
      "token":token
    };
  }
  console.log("发送请求了")
  return config;
});

//每次请求后的处理  如果是非法请求  跳转登陆页面
axios.interceptors.response.use(config=>{
  console.log("请求后");
  if (config.data.code==6666){
    router.push("/login");
  }
  return config;
})

//配置路由守卫
router.beforeEach((to, from, next) => {
  //  to  去  from  来自   next 允许通过
  if(to.fullPath!="/login"){//需要认证
    //判断是否登陆
    let  user=window.sessionStorage.getItem("user");
    if(user!=null){
      next();
    }else{
      next({path:"/login"});
    }
  }else{
    next();
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
