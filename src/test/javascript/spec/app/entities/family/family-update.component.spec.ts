/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import FamilyUpdateComponent from '@/entities/family/family-update.vue';
import FamilyClass from '@/entities/family/family-update.component';
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
  describe('Family Management Update Component', () => {
    let wrapper: Wrapper<FamilyClass>;
    let comp: FamilyClass;
    let familyServiceStub: SinonStubbedInstance<FamilyService>;

    beforeEach(() => {
      familyServiceStub = sinon.createStubInstance<FamilyService>(FamilyService);

      wrapper = shallowMount<FamilyClass>(FamilyUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          familyService: () => familyServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.family = entity;
        familyServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(familyServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.family = entity;
        familyServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(familyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFamily = { id: 123 };
        familyServiceStub.find.resolves(foundFamily);
        familyServiceStub.retrieve.resolves([foundFamily]);

        // WHEN
        comp.beforeRouteEnter({ params: { familyId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.family).toBe(foundFamily);
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
