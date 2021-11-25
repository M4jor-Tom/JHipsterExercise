/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import BrandUpdateComponent from '@/entities/brand/brand-update.vue';
import BrandClass from '@/entities/brand/brand-update.component';
import BrandService from '@/entities/brand/brand.service';

import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Brand Management Update Component', () => {
    let wrapper: Wrapper<BrandClass>;
    let comp: BrandClass;
    let brandServiceStub: SinonStubbedInstance<BrandService>;

    beforeEach(() => {
      brandServiceStub = sinon.createStubInstance<BrandService>(BrandService);

      wrapper = shallowMount<BrandClass>(BrandUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          brandService: () => brandServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.brand = entity;
        brandServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(brandServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.brand = entity;
        brandServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(brandServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundBrand = { id: 123 };
        brandServiceStub.find.resolves(foundBrand);
        brandServiceStub.retrieve.resolves([foundBrand]);

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
