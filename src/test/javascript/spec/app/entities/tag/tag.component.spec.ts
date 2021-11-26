/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import TagComponent from '@/entities/tag/tag.vue';
import TagClass from '@/entities/tag/tag.component';
import TagService from '@/entities/tag/tag.service';
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
  describe('Tag Management Component', () => {
    let wrapper: Wrapper<TagClass>;
    let comp: TagClass;
    let tagServiceStub: SinonStubbedInstance<TagService>;

    beforeEach(() => {
      tagServiceStub = sinon.createStubInstance<TagService>(TagService);
      tagServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TagClass>(TagComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          tagService: () => tagServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      tagServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTags();
      await comp.$nextTick();

      // THEN
      expect(tagServiceStub.retrieve.called).toBeTruthy();
      expect(comp.tags[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      tagServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeTag();
      await comp.$nextTick();

      // THEN
      expect(tagServiceStub.delete.called).toBeTruthy();
      expect(tagServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
