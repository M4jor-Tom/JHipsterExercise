import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IModel } from '@/shared/model/model.model';

import ModelService from './model.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Model extends Vue {
  @Inject('modelService') private modelService: () => ModelService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public models: IModel[] = [];

  public isFetching = false;

  public propOrder = 'id';
  public reverse = false;

  public mounted(): void {
    this.retrieveAllModels();
  }

  public clear(): void {
    this.retrieveAllModels();
  }

  public retrieveAllModels(): void {
    this.isFetching = true;
    this.modelService()
      .retrieve({
        sort: this.sort(),
      })
      .then(
        res => {
          this.models = res.data;
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

  public prepareRemove(instance: IModel): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeModel(): void {
    this.modelService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.model.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllModels();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public changeOrder(propOrder: string): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public transition(): void {
    this.retrieveAllModels();
  }

  public sort(): any {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }
}
