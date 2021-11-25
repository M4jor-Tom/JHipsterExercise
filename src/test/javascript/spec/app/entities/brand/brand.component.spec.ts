/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import BrandComponent from '@/entities/brand/brand.vue';
import BrandClass from '@/entities/brand/brand.component';
import BrandService from '@/entities/brand/brand.service';
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
  describe('Brand Management Component', () => {
    let wrapper: Wrapper<BrandClass>;
    let comp: BrandClass;
    let brandServiceStub: SinonStubbedInstance<BrandService>;

    beforeEach(() => {
      brandServiceStub = sinon.createStubInstance<BrandService>(BrandService);
      brandServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<BrandClass>(BrandComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          brandService: () => brandServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      brandServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllBrands();
      await comp.$nextTick();

      // THEN
      expect(brandServiceStub.retrieve.called).toBeTruthy();
      expect(comp.brands[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      brandServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeBrand();
      await comp.$nextTick();

      // THEN
      expect(brandServiceStub.delete.called).toBeTruthy();
      expect(brandServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
