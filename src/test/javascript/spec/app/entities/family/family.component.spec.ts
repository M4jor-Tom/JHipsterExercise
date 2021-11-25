/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import FamilyComponent from '@/entities/family/family.vue';
import FamilyClass from '@/entities/family/family.component';
import FamilyService from '@/entities/family/family.service';
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
  describe('Family Management Component', () => {
    let wrapper: Wrapper<FamilyClass>;
    let comp: FamilyClass;
    let familyServiceStub: SinonStubbedInstance<FamilyService>;

    beforeEach(() => {
      familyServiceStub = sinon.createStubInstance<FamilyService>(FamilyService);
      familyServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FamilyClass>(FamilyComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          familyService: () => familyServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      familyServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFamilys();
      await comp.$nextTick();

      // THEN
      expect(familyServiceStub.retrieve.called).toBeTruthy();
      expect(comp.families[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      familyServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeFamily();
      await comp.$nextTick();

      // THEN
      expect(familyServiceStub.delete.called).toBeTruthy();
      expect(familyServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
