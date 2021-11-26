/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import SubFamilyComponent from '@/entities/sub-family/sub-family.vue';
import SubFamilyClass from '@/entities/sub-family/sub-family.component';
import SubFamilyService from '@/entities/sub-family/sub-family.service';
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
  describe('SubFamily Management Component', () => {
    let wrapper: Wrapper<SubFamilyClass>;
    let comp: SubFamilyClass;
    let subFamilyServiceStub: SinonStubbedInstance<SubFamilyService>;

    beforeEach(() => {
      subFamilyServiceStub = sinon.createStubInstance<SubFamilyService>(SubFamilyService);
      subFamilyServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SubFamilyClass>(SubFamilyComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          subFamilyService: () => subFamilyServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      subFamilyServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSubFamilys();
      await comp.$nextTick();

      // THEN
      expect(subFamilyServiceStub.retrieve.called).toBeTruthy();
      expect(comp.subFamilies[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      subFamilyServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeSubFamily();
      await comp.$nextTick();

      // THEN
      expect(subFamilyServiceStub.delete.called).toBeTruthy();
      expect(subFamilyServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
