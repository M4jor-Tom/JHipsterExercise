import { Authority } from '@/shared/security/authority';

const JhiCartComponent = () => import('@/cart/cart.vue');


export default [
  {
    path: '/cart',
    name: 'JhiCartComponent',
    component: JhiCartComponent,
  }

];
