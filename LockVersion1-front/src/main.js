// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import 'font-awesome/css/font-awesome.css'
import './assets/iconfont/iconfont.css'

import { Button ,MessageBox, Row, Col, Dropdown , DropdownMenu,DropdownItem,  Menu , Submenu,  Icon, MenuItem,Loading,Notification,Message,
  MenuItemGroup, Form, FormItem,Input,Table, TableColumn,Pagination,  Dialog,Breadcrumb, BreadcrumbItem, Tooltip,Carousel, CarouselItem,Popover,
  Checkbox, DatePicker,  Select, Option, Autocomplete, Upload,Progress} from 'element-ui';

Vue.prototype.$ajax = axios;
Vue.use(Loading.directive);
Vue.prototype.$loading = Loading.service;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;
Vue.use(Button);
Vue.use(Row);
Vue.use(Col);
Vue.use(Dropdown);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(Icon);
Vue.use(MenuItem);
Vue.use(MenuItemGroup);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Pagination);
Vue.use(Dialog);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Tooltip);
Vue.use(Carousel);
Vue.use(CarouselItem);
Vue.use(Popover);
Vue.use(Checkbox);
Vue.use(DatePicker);
Vue.use(Select);
Vue.use(Option);
Vue.use(Autocomplete);
Vue.use(Upload);
Vue.use(Progress);


//当导航的路径为/login的时候 1：从主页退入到登录页面，此时sessionStorage删除登陆成功的信息 2：主动进入登陆页面的路径时，sessionStorage没有信息
//当导航的路径不是登陆页面时同时检测到sessionStorage里面有登陆成功的信息，则表示已经成功登陆了，并可以任意导航。
//当导航的路径不是登陆页面时同时检测到sessionStorage里面没有登陆信息，表示还未登陆，并直接导航到登陆界面。
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    sessionStorage.removeItem('user');
  }
  let user = JSON.parse(sessionStorage.getItem('user'));
  if (!user && to.path !== '/login') {
    next({ path: '/login' })
  } else {
    next()
  }
});

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
});
