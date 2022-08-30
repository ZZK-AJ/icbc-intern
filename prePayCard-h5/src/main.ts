import { createApp } from 'vue';
import './style.css';
import App from './App.vue';
import router from './router';
import store from './store';
import { ConfigProvider } from 'vant';

//Vant 中有个别组件是以函数的形式提供的，包括 Toast，Dialog，Notify 和 ImagePreview 组件。在使用函数组件时，unplugin-vue-components 无法自动引入对应的样式，因此需要手动引入样式。
import { Toast, Dialog } from "vant";
import 'vant/es/toast/style';
import 'vant/es/dialog/style';


import 'amfe-flexible'
const app = createApp(App);

app.use(ConfigProvider);
app.use(Toast);
app.use(Dialog);
app.use(store);
app.use(router);
app.mount('#app')
