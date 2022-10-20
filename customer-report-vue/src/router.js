import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
    // mode: "history",
    routes: [
        {
            path: "/",
            alias: "/login",
            name: "login",
            component: () => import("./components/Login")
        },
        {
            path: "/home",
            name: "home",
            component: () => import("./components/Home")
        }
    ]
});