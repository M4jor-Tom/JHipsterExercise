/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ModelUpdateComponent from '@/entities/model/model-update.vue';
import ModelClass from '@/entities/model/model-update.component';
import ModelService from '@/entities/model/model.service';

import BrandService from '@/entities/brand/brand.service';

import FamilyService from '@/entities/family/family.service';
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
  describe('Model Management Update Component', () => {
    let wrapper: Wrapper<ModelClass>;
    let comp: ModelClass;
    let modelServiceStub: SinonStubbedInstance<ModelService>;

    beforeEach(() => {
      modelServiceStub = sinon.createStubInstance<ModelService>(ModelService);

      wrapper = shallowMount<ModelClass>(ModelUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          modelService: () => modelServiceStub,
          alertService: () => new AlertService(),

          brandService: () => new BrandService(),

          familyService: () => new FamilyService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.model = entity;
        modelServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(modelServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.model = entity;
        modelServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(modelServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundModel = { id: 123 };
        modelServiceStub.find.resolves(foundModel);
        modelServiceStub.retrieve.resolves([foundModel]);

        // WHEN
        comp.beforeRouteEnter({ params: { modelId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.model).toBe(foundModel);
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
