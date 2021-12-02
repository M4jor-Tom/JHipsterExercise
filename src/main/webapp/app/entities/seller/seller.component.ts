import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISeller } from '@/shared/model/seller.model';

import SellerService from './seller.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Seller extends Vue {
  @Inject('sellerService') private sellerService: () => SellerService;
  @Inject('alertService') private alertService: () => AlertService;

  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  private removeId: number = null;

  public sellers: ISeller[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllSellers();
  }

  public clear(): void {
    this.retrieveAllSellers();
  }

  public retrieveAllSellers(): void {
    this.isFetching = true;
    this.sellerService()
      .retrieve()
      .then(
        res => {
          this.sellers = res.data;
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

  public prepareRemove(instance: ISeller): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeSeller(): void {
    this.sellerService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.seller.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllSellers();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
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

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
