import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBrand } from '@/shared/model/brand.model';
import BrandService from './brand.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component
export default class BrandDetails extends Vue {
  @Inject('brandService') private brandService: () => BrandService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;
  private hasSellerAuthorityValue = false;
  private hasAdminAuthorityValue = false;

  public brand: IBrand = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.brandId) {
        vm.retrieveBrand(to.params.brandId);
      }
    });
  }

  public retrieveBrand(brandId) {
    this.brandService()
      .find(brandId)
      .then(res => {
        this.brand = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
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
  
  public hasSellerAuthority(authority: string): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authority)
      .then(value => {
        this.hasSellerAuthorityValue = value;
      });
    return this.hasSellerAuthorityValue;
  }
  
  public hasAdminAuthority(authority: string): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authority)
      .then(value => {
        this.hasAdminAuthorityValue = value;
      });
    return this.hasAdminAuthorityValue;
  }
}
