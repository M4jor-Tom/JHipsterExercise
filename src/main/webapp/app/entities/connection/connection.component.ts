import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IConnection } from '@/shared/model/connection.model';

import ConnectionService from './connection.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Connection extends Vue {
  @Inject('connectionService') private connectionService: () => ConnectionService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public connections: IConnection[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllConnections();
  }

  public clear(): void {
    this.retrieveAllConnections();
  }

  public retrieveAllConnections(): void {
    this.isFetching = true;
    this.connectionService()
      .retrieve()
      .then(
        res => {
          this.connections = res.data;
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

  public prepareRemove(instance: IConnection): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeConnection(): void {
    this.connectionService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.connection.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllConnections();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}