import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IModel } from '@/shared/model/model.model';

import ModelService from './model.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Model extends Vue {
  @Inject('modelService') private modelService: () => ModelService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;
  
  public models: IModel[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllModels();
  }

  public clear(): void {
    this.retrieveAllModels();
  }

  public retrieveAllModels(): void {
    this.isFetching = true;
    this.modelService()
      .retrieve()
      .then(
        res => {
          this.models = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IModel): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeModel(): void {
    this.modelService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.model.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllModels();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
  
  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        this.hasAnyAuthorityValue = value;
      });
    return this.hasAnyAuthorityValue;
  }
}
