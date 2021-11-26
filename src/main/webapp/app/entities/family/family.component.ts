import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFamily } from '@/shared/model/family.model';

import FamilyService from './family.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Family extends Vue {
  @Inject('familyService') private familyService: () => FamilyService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public families: IFamily[] = [];

  public isFetching = false;
  public propOrder = 'id';
  public reverse = false;

  public mounted(): void {
    this.retrieveAllFamilys();
  }

  public clear(): void {
    this.retrieveAllFamilys();
  }

  public retrieveAllFamilys(): void {
    this.isFetching = true;
    this.familyService()
      .retrieve({
        sort: this.sort(),
      })
      .then(
        res => {
          this.families = res.data;
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

  public prepareRemove(instance: IFamily): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFamily(): void {
    this.familyService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('jHipsterExerciseApp.family.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFamilys();
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
    this.retrieveAllFamilys();
  }

  public sort(): any {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }
}
