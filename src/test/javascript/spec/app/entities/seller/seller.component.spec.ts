/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import SellerComponent from '@/entities/seller/seller.vue';
import SellerClass from '@/entities/seller/seller.component';
import SellerService from '@/entities/seller/seller.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Seller Management Component', () => {
    let wrapper: Wrapper<SellerClass>;
    let comp: SellerClass;
    let sellerServiceStub: SinonStubbedInstance<SellerService>;

    beforeEach(() => {
      sellerServiceStub = sinon.createStubInstance<SellerService>(SellerService);
      sellerServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SellerClass>(SellerComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          sellerService: () => sellerServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      sellerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSellers();
      await comp.$nextTick();

      // THEN
      expect(sellerServiceStub.retrieve.called).toBeTruthy();
      expect(comp.sellers[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      sellerServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeSeller();
      await comp.$nextTick();

      // THEN
      expect(sellerServiceStub.delete.called).toBeTruthy();
      expect(sellerServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
