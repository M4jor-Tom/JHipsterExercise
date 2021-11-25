/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ConnectionComponent from '@/entities/connection/connection.vue';
import ConnectionClass from '@/entities/connection/connection.component';
import ConnectionService from '@/entities/connection/connection.service';
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
  describe('Connection Management Component', () => {
    let wrapper: Wrapper<ConnectionClass>;
    let comp: ConnectionClass;
    let connectionServiceStub: SinonStubbedInstance<ConnectionService>;

    beforeEach(() => {
      connectionServiceStub = sinon.createStubInstance<ConnectionService>(ConnectionService);
      connectionServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ConnectionClass>(ConnectionComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          connectionService: () => connectionServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      connectionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllConnections();
      await comp.$nextTick();

      // THEN
      expect(connectionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.connections[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      connectionServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeConnection();
      await comp.$nextTick();

      // THEN
      expect(connectionServiceStub.delete.called).toBeTruthy();
      expect(connectionServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
