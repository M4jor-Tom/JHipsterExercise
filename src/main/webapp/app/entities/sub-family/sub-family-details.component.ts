import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISubFamily } from '@/shared/model/sub-family.model';
import SubFamilyService from './sub-family.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component
export default class SubFamilyDetails extends Vue {
  @Inject('subFamilyService') private subFamilyService: () => SubFamilyService;
  @Inject('alertService') private alertService: () => AlertService;

  public subFamily: ISubFamily = {};
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.subFamilyId) {
        vm.retrieveSubFamily(to.params.subFamilyId);
      }
    });
  }

  public retrieveSubFamily(subFamilyId) {
    this.subFamilyService()
      .find(subFamilyId)
      .then(res => {
        this.subFamily = res;
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
