import { Component, Vue, Inject } from 'vue-property-decorator';

import { IConnection } from '@/shared/model/connection.model';
import ConnectionService from './connection.service';
import AlertService from '@/shared/alert/alert.service';

import AccountService from '@/account/account.service';
import LoginService from '@/account/login.service';

@Component
export default class ConnectionDetails extends Vue {
  @Inject('connectionService') private connectionService: () => ConnectionService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('loginService')
  private loginService: () => LoginService;
  private hasAnyAuthorityValue = false;

  public connection: IConnection = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.connectionId) {
        vm.retrieveConnection(to.params.connectionId);
      }
    });
  }

  public retrieveConnection(connectionId) {
    this.connectionService()
      .find(connectionId)
      .then(res => {
        this.connection = res;
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
