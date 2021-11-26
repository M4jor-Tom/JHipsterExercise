/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import SubFamilyUpdateComponent from '@/entities/sub-family/sub-family-update.vue';
import SubFamilyClass from '@/entities/sub-family/sub-family-update.component';
import SubFamilyService from '@/entities/sub-family/sub-family.service';

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
  describe('SubFamily Management Update Component', () => {
    let wrapper: Wrapper<SubFamilyClass>;
    let comp: SubFamilyClass;
    let subFamilyServiceStub: SinonStubbedInstance<SubFamilyService>;

    beforeEach(() => {
      subFamilyServiceStub = sinon.createStubInstance<SubFamilyService>(SubFamilyService);

      wrapper = shallowMount<SubFamilyClass>(SubFamilyUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          subFamilyService: () => subFamilyServiceStub,
          alertService: () => new AlertService(),

          familyService: () => new FamilyService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.subFamily = entity;
        subFamilyServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subFamilyServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.subFamily = entity;
        subFamilyServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subFamilyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSubFamily = { id: 123 };
        subFamilyServiceStub.find.resolves(foundSubFamily);
        subFamilyServiceStub.retrieve.resolves([foundSubFamily]);

        // WHEN
        comp.beforeRouteEnter({ params: { subFamilyId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.subFamily).toBe(foundSubFamily);
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
