import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IFamily, Family } from '@/shared/model/family.model';
import FamilyService from './family.service';

const validations: any = {
  family: {
    name: {},
  },
};

@Component({
  validations,
})
export default class FamilyUpdate extends Vue {
  @Inject('familyService') private familyService: () => FamilyService;
  @Inject('alertService') private alertService: () => AlertService;

  public family: IFamily = new Family();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.familyId) {
        vm.retrieveFamily(to.params.familyId);
      }
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
    if (this.family.id) {
      this.familyService()
        .update(this.family)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.family.updated', { param: param.id });
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
      this.familyService()
        .create(this.family)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.family.created', { param: param.id });
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

  public retrieveFamily(familyId): void {
    this.familyService()
      .find(familyId)
      .then(res => {
        this.family = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
