import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import BrandService from '@/entities/brand/brand.service';
import { IBrand } from '@/shared/model/brand.model';

import FamilyService from '@/entities/family/family.service';
import { IFamily } from '@/shared/model/family.model';

import { IModel, Model } from '@/shared/model/model.model';
import ModelService from './model.service';

const validations: any = {
  model: {
    name: {},
  },
};

@Component({
  validations,
})
export default class ModelUpdate extends Vue {
  @Inject('modelService') private modelService: () => ModelService;
  @Inject('alertService') private alertService: () => AlertService;

  public model: IModel = new Model();

  @Inject('brandService') private brandService: () => BrandService;

  public brands: IBrand[] = [];

  @Inject('familyService') private familyService: () => FamilyService;

  public families: IFamily[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.modelId) {
        vm.retrieveModel(to.params.modelId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.model.id) {
      this.modelService()
        .update(this.model)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.model.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.modelService()
        .create(this.model)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.model.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveModel(modelId): void {
    this.modelService()
      .find(modelId)
      .then(res => {
        this.model = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.brandService()
      .retrieve()
      .then(res => {
        this.brands = res.data;
      });
    this.familyService()
      .retrieve()
      .then(res => {
        this.families = res.data;
      });
  }
}
