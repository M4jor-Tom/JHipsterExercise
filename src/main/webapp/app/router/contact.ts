import { Authority } from '@/shared/security/authority';

const JhiContactComponent = () => import('@/contact/contact.vue');

export default [
  {
    path: '/contact',
    name: 'JhiContactComponent',
    component: JhiContactComponent,
  }

];
