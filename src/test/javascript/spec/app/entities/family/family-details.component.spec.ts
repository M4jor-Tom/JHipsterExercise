/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FamilyDetailComponent from '@/entities/family/family-details.vue';
import FamilyClass from '@/entities/family/family-details.component';
import FamilyService from '@/entities/family/family.service';
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
  describe('Family Management Detail Component', () => {
    let wrapper: Wrapper<FamilyClass>;
    let comp: FamilyClass;
    let familyServiceStub: SinonStubbedInstance<FamilyService>;

    beforeEach(() => {
      familyServiceStub = sinon.createStubInstance<FamilyService>(FamilyService);

      wrapper = shallowMount<FamilyClass>(FamilyDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { familyService: () => familyServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFamily = { id: 123 };
        familyServiceStub.find.resolves(foundFamily);

        // WHEN
        comp.retrieveFamily(123);
        await comp.$nextTick();

        // THEN
        expect(comp.family).toBe(foundFamily);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFamily = { id: 123 };
        familyServiceStub.find.resolves(foundFamily);

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
