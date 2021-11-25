/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import BrandDetailComponent from '@/entities/brand/brand-details.vue';
import BrandClass from '@/entities/brand/brand-details.component';
import BrandService from '@/entities/brand/brand.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Brand Management Detail Component', () => {
    let wrapper: Wrapper<BrandClass>;
    let comp: BrandClass;
    let brandServiceStub: SinonStubbedInstance<BrandService>;

    beforeEach(() => {
      brandServiceStub = sinon.createStubInstance<BrandService>(BrandService);

      wrapper = shallowMount<BrandClass>(BrandDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { brandService: () => brandServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundBrand = { id: 123 };
        brandServiceStub.find.resolves(foundBrand);

        // WHEN
        comp.retrieveBrand(123);
        await comp.$nextTick();

        // THEN
        expect(comp.brand).toBe(foundBrand);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundBrand = { id: 123 };
        brandServiceStub.find.resolves(foundBrand);

        // WHEN
        comp.beforeRouteEnter({ params: { brandId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.brand).toBe(foundBrand);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
