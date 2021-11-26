import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFamily } from '@/shared/model/family.model';
import FamilyService from './family.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component
export default class FamilyDetails extends Vue {
  @Inject('familyService') private familyService: () => FamilyService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  public family: IFamily = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.familyId) {
        vm.retrieveFamily(to.params.familyId);
      }
    });
  }

  public retrieveFamily(familyId) {
    this.familyService()
      .find(familyId)
      .then(res => {
        this.family = res;
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
}
