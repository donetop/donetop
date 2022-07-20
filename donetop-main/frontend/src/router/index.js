import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "@/views/home/HomeView.vue";
import LoginView from "@/views/login/LoginView";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    alias: ["/home"],
    name: "HomeView",
    component: HomeView,
  },
  {
    path: "/login",
    name: "LoginView",
    component: LoginView,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
