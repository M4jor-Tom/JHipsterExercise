import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFamily } from '@/shared/model/family.model';

import FamilyService from './family.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Family extends Vue {
  @Inject('familyService') private familyService: () => FamilyService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  public families: IFamily[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFamilys();
  }

  public clear(): void {
    this.retrieveAllFamilys();
  }

  public retrieveAllFamilys(): void {
    this.isFetching = true;
    this.familyService()
      .retrieve()
      .then(
        res => {
          this.families = res.data;
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

  public prepareRemove(instance: IFamily): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFamily(): void {
    this.familyService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.family.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFamilys();
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
