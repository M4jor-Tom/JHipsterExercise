import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITag } from '@/shared/model/tag.model';
import TagService from './tag.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component
export default class TagDetails extends Vue {
  @Inject('tagService') private tagService: () => TagService;
  @Inject('alertService') private alertService: () => AlertService;

  public tag: ITag = {};
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;
  private hasSellerAuthorityValue = false;
  private hasAdminAuthorityValue = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tagId) {
        vm.retrieveTag(to.params.tagId);
      }
    });
  }

  public retrieveTag(tagId) {
    this.tagService()
      .find(tagId)
      .then(res => {
        this.tag = res;
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
