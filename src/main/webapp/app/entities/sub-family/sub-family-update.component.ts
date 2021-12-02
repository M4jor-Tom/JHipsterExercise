import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FamilyService from '@/entities/family/family.service';
import { IFamily } from '@/shared/model/family.model';

import { ISubFamily, SubFamily } from '@/shared/model/sub-family.model';
import SubFamilyService from './sub-family.service';

const validations: any = {
  subFamily: {
    name: {
      required,
    },
    family: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class SubFamilyUpdate extends Vue {
  @Inject('subFamilyService') private subFamilyService: () => SubFamilyService;
  @Inject('alertService') private alertService: () => AlertService;

  public subFamily: ISubFamily = new SubFamily();

  @Inject('familyService') private familyService: () => FamilyService;

  public families: IFamily[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.subFamilyId) {
        vm.retrieveSubFamily(to.params.subFamilyId);
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
    if (this.subFamily.id) {
      this.subFamilyService()
        .update(this.subFamily)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.subFamily.updated', { param: param.id });
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
      this.subFamilyService()
        .create(this.subFamily)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.subFamily.created', { param: param.id });
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

  public retrieveSubFamily(subFamilyId): void {
    this.subFamilyService()
      .find(subFamilyId)
      .then(res => {
        this.subFamily = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.familyService()
      .retrieve()
      .then(res => {
        this.families = res.data;
      });
  }
}
