/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ConnectionDetailComponent from '@/entities/connection/connection-details.vue';
import ConnectionClass from '@/entities/connection/connection-details.component';
import ConnectionService from '@/entities/connection/connection.service';
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
  describe('Connection Management Detail Component', () => {
    let wrapper: Wrapper<ConnectionClass>;
    let comp: ConnectionClass;
    let connectionServiceStub: SinonStubbedInstance<ConnectionService>;

    beforeEach(() => {
      connectionServiceStub = sinon.createStubInstance<ConnectionService>(ConnectionService);

      wrapper = shallowMount<ConnectionClass>(ConnectionDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { connectionService: () => connectionServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundConnection = { id: 123 };
        connectionServiceStub.find.resolves(foundConnection);

        // WHEN
        comp.retrieveConnection(123);
        await comp.$nextTick();

        // THEN
        expect(comp.connection).toBe(foundConnection);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundConnection = { id: 123 };
        connectionServiceStub.find.resolves(foundConnection);

        // WHEN
        comp.beforeRouteEnter({ params: { connectionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.connection).toBe(foundConnection);
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
