import { Authority } from '@/shared/security/authority';

const JhiListProduitComponent = () => import('@/list_produit/list_produit.vue');

export default [
  {
    path: '/list_produit',
    name: 'JhiListProduitComponent',
    component: JhiListProduitComponent,
  }

];
