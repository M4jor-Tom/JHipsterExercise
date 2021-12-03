import Vue from 'vue';
import Component from 'vue-class-component';
Component.registerHooks([
  'beforeRouteEnter',
  'beforeRouteLeave',
  'beforeRouteUpdate', // for vue-router 2.2+
]);
import Router from 'vue-router';

const Home = () => import('@/core/home/home.vue');
const Paypal = () => import('@/core/paypal/paypal.vue');
const LoginServlet = () => import('@/core/loginServlet/loginServlet.vue')
const Adyen = () => import('@/core/adyen/adyen.vue')
const Error = () => import('@/core/error/error.vue');
import account from '@/router/account';
import admin from '@/router/admin';
import entities from '@/router/entities';
import pages from '@/router/pages';
import cart from './cart';
import list_produit from './list_produit';
import contact from './contact';

Vue.use(Router);

// prettier-ignore
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/paypal',
      name: 'Paypal',
      component: Paypal
    },
    {
      path: '/adyen',
      name: 'adyen',
      component: Adyen
    },
    {
      path: '/loginServlet',
      name: 'loginServlet',
      component: LoginServlet
    },

    {
      path: '/forbidden',
      name: 'Forbidden',
      component: Error,
      meta: { error403: true }
    },
    {
      path: '/not-found',
      name: 'NotFound',
      component: Error,
      meta: { error404: true }
    },
    ...account,
    ...admin,
    ...cart,
    ...list_produit,
    ...contact,
    ...entities,
    ...pages
  ]
});
