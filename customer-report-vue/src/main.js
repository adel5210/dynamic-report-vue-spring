import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import VeeValidate from "vee-validate";
import {
  BootstrapVue,
  IconsPlugin,
  LayoutPlugin,
  ModalPlugin,
  CardPlugin,
  VBScrollspyPlugin,
  DropdownPlugin,
  TablePlugin,
  FormPlugin,
  EmbedPlugin
} from "bootstrap-vue";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "bootstrap/dist/css/bootstrap.css";

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(LayoutPlugin);
Vue.use(ModalPlugin);
Vue.use(CardPlugin);
Vue.use(VBScrollspyPlugin);
Vue.use(DropdownPlugin);
Vue.use(TablePlugin);
Vue.use(FormPlugin);
Vue.use(EmbedPlugin);

Vue.use(VeeValidate);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
