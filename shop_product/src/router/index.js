import Vue from 'vue'
import Router from 'vue-router'
import Test from "../components/Test";
import Home from "../components/common/Home";
import Type from "../components/views/Type";
import Brand from "../components/views/Brand";
import Attr from "../components/views/Attr";
import AddProduct from "../components/views/AddProduct";
import Product from "../components/views/Product";

Vue.use(Router)

export default new Router({
  routes: [
      {
        path: '/',
        redirect:'/test'
      },
      {
        path: '/',
        component: Home,
        children: [
          {
            path: 'test',
            component: Test,
            meta: {title: '系统首页'}
          },
          {
            path: 'product',
            component: Product,
            meta: {title: '商品列表'}
          },
          {
            path: 'addProduct',
            component: AddProduct,
            meta: {title: '新增商品'}
          },
          {
            path: 'type',
            component: Type,
            meta: {title: '分类管理'}
          },
          {
            path: 'brand',
            component: Brand,
            meta: {title: '品牌管理'}
          },
          {
            path: 'attr',
            component: Attr,
            meta: {title: '属性管理'}
          },
        ]
      }
  ]
})
