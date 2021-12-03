import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Client = () => import('@/entities/client/client.vue');
// prettier-ignore
const ClientUpdate = () => import('@/entities/client/client-update.vue');
// prettier-ignore
const ClientDetails = () => import('@/entities/client/client-details.vue');
// prettier-ignore
const Order = () => import('@/entities/order/order.vue');
// prettier-ignore
const OrderUpdate = () => import('@/entities/order/order-update.vue');
// prettier-ignore
const OrderDetails = () => import('@/entities/order/order-details.vue');
// prettier-ignore
const Connection = () => import('@/entities/connection/connection.vue');
// prettier-ignore
const ConnectionUpdate = () => import('@/entities/connection/connection-update.vue');
// prettier-ignore
const ConnectionDetails = () => import('@/entities/connection/connection-details.vue');
// prettier-ignore
const Product = () => import('@/entities/product/product.vue');
// prettier-ignore
const ProductUpdate = () => import('@/entities/product/product-update.vue');
// prettier-ignore
const ProductDetails = () => import('@/entities/product/product-details.vue');
// prettier-ignore
const Tag = () => import('@/entities/tag/tag.vue');
// prettier-ignore
const TagUpdate = () => import('@/entities/tag/tag-update.vue');
// prettier-ignore
const TagDetails = () => import('@/entities/tag/tag-details.vue');
// prettier-ignore
const Brand = () => import('@/entities/brand/brand.vue');
// prettier-ignore
const BrandUpdate = () => import('@/entities/brand/brand-update.vue');
// prettier-ignore
const BrandDetails = () => import('@/entities/brand/brand-details.vue');
// prettier-ignore
const Family = () => import('@/entities/family/family.vue');
// prettier-ignore
const FamilyUpdate = () => import('@/entities/family/family-update.vue');
// prettier-ignore
const FamilyDetails = () => import('@/entities/family/family-details.vue');
// prettier-ignore
const SubFamily = () => import('@/entities/sub-family/sub-family.vue');
// prettier-ignore
const SubFamilyUpdate = () => import('@/entities/sub-family/sub-family-update.vue');
// prettier-ignore
const SubFamilyDetails = () => import('@/entities/sub-family/sub-family-details.vue');
// prettier-ignore
const Seller = () => import('@/entities/seller/seller.vue');
// prettier-ignore
const SellerUpdate = () => import('@/entities/seller/seller-update.vue');
// prettier-ignore
const SellerDetails = () => import('@/entities/seller/seller-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/client',
    name: 'Client',
    component: Client,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/client/new',
    name: 'ClientCreate',
    component: ClientUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/client/:clientId/edit',
    name: 'ClientEdit',
    component: ClientUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/client/:clientId/view',
    name: 'ClientView',
    component: ClientDetails,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/order',
    name: 'Order',
    component: Order,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER, Authority.USER] },
  },
  {
    path: '/order/new',
    name: 'OrderCreate',
    component: OrderUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/order/:orderId/edit',
    name: 'OrderEdit',
    component: OrderUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/order/:orderId/view',
    name: 'OrderView',
    component: OrderDetails,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER, Authority.USER] },
  },
  {
    path: '/connection',
    name: 'Connection',
    component: Connection,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/connection/new',
    name: 'ConnectionCreate',
    component: ConnectionUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/connection/:connectionId/edit',
    name: 'ConnectionEdit',
    component: ConnectionUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/connection/:connectionId/view',
    name: 'ConnectionView',
    component: ConnectionDetails,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/product',
    name: 'Product',
    component: Product,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER, Authority.USER] },
  },
  {
    path: '/product/new',
    name: 'ProductCreate',
    component: ProductUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/product/:productId/edit',
    name: 'ProductEdit',
    component: ProductUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/product/:productId/view',
    name: 'ProductView',
    component: ProductDetails,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER, Authority.USER] },
  },
  {
    path: '/tag',
    name: 'Tag',
    component: Tag,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/tag/new',
    name: 'TagCreate',
    component: TagUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/tag/:tagId/edit',
    name: 'TagEdit',
    component: TagUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/tag/:tagId/view',
    name: 'TagView',
    component: TagDetails,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/brand',
    name: 'Brand',
    component: Brand,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/brand/new',
    name: 'BrandCreate',
    component: BrandUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/brand/:brandId/edit',
    name: 'BrandEdit',
    component: BrandUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/brand/:brandId/view',
    name: 'BrandView',
    component: BrandDetails,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/family',
    name: 'Family',
    component: Family,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/family/new',
    name: 'FamilyCreate',
    component: FamilyUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/family/:familyId/edit',
    name: 'FamilyEdit',
    component: FamilyUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/family/:familyId/view',
    name: 'FamilyView',
    component: FamilyDetails,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/sub-family',
    name: 'SubFamily',
    component: SubFamily,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/sub-family/new',
    name: 'SubFamilyCreate',
    component: SubFamilyUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/sub-family/:subFamilyId/edit',
    name: 'SubFamilyEdit',
    component: SubFamilyUpdate,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/sub-family/:subFamilyId/view',
    name: 'SubFamilyView',
    component: SubFamilyDetails,
    meta: { authorities: [Authority.ADMIN, Authority.SELLER] },
  },
  {
    path: '/seller',
    name: 'Seller',
    component: Seller,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/seller/new',
    name: 'SellerCreate',
    component: SellerUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/seller/:sellerId/edit',
    name: 'SellerEdit',
    component: SellerUpdate,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/seller/:sellerId/view',
    name: 'SellerView',
    component: SellerDetails,
    meta: { authorities: [Authority.ADMIN] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
