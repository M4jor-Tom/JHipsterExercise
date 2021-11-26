import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IBrand } from '@/shared/model/brand.model';

import BrandService from './brand.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Brand extends Vue {
  @Inject('brandService') private brandService: () => BrandService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  public brands: IBrand[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllBrands();
  }

  public clear(): void {
    this.retrieveAllBrands();
  }

  public retrieveAllBrands(): void {
    this.isFetching = true;
    this.brandService()
      .retrieve()
      .then(
        res => {
          this.brands = res.data;
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

  public prepareRemove(instance: IBrand): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeBrand(): void {
    this.brandService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.brand.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllBrands();
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
