import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISeller } from '@/shared/model/seller.model';
import SellerService from './seller.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component
export default class SellerDetails extends Vue {
  @Inject('sellerService') private sellerService: () => SellerService;
  @Inject('alertService') private alertService: () => AlertService;

  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  public seller: ISeller = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.sellerId) {
        vm.retrieveSeller(to.params.sellerId);
      }
    });
  }

  public retrieveSeller(sellerId) {
    this.sellerService()
      .find(sellerId)
      .then(res => {
        this.seller = res;
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

  public previousState() {
    this.$router.go(-1);
  }
}
