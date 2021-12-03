import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISubFamily } from '@/shared/model/sub-family.model';

import SubFamilyService from './sub-family.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class SubFamily extends Vue {
  @Inject('subFamilyService') private subFamilyService: () => SubFamilyService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  public subFamilies: ISubFamily[] = [];

  public isFetching = false;
  public propOrder = 'id';
  public reverse = false;
  public filtered = '';

  public mounted(): void {
    this.retrieveAllSubFamilys();
  }

  public clear(): void {
    this.retrieveAllSubFamilys();
  }

  public retrieveAllSubFamilys(): void {
    this.isFetching = true;
    this.subFamilyService()
      .retrieve({
        sort: this.sort(),
      })
      .then(
        res => {
          this.subFamilies = res.data;
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

  public prepareRemove(instance: ISubFamily): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeSubFamily(): void {
    this.subFamilyService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.subFamily.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllSubFamilys();
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

  public changeOrder(propOrder: string): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public transition(): void {
    this.retrieveAllSubFamilys();
  }

  public sort(): any {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }
}
