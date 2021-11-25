/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import SubFamilyDetailComponent from '@/entities/sub-family/sub-family-details.vue';
import SubFamilyClass from '@/entities/sub-family/sub-family-details.component';
import SubFamilyService from '@/entities/sub-family/sub-family.service';
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
  describe('SubFamily Management Detail Component', () => {
    let wrapper: Wrapper<SubFamilyClass>;
    let comp: SubFamilyClass;
    let subFamilyServiceStub: SinonStubbedInstance<SubFamilyService>;

    beforeEach(() => {
      subFamilyServiceStub = sinon.createStubInstance<SubFamilyService>(SubFamilyService);

      wrapper = shallowMount<SubFamilyClass>(SubFamilyDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { subFamilyService: () => subFamilyServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSubFamily = { id: 123 };
        subFamilyServiceStub.find.resolves(foundSubFamily);

        // WHEN
        comp.retrieveSubFamily(123);
        await comp.$nextTick();

        // THEN
        expect(comp.subFamily).toBe(foundSubFamily);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSubFamily = { id: 123 };
        subFamilyServiceStub.find.resolves(foundSubFamily);

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
