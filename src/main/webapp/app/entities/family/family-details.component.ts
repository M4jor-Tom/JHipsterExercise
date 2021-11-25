import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFamily } from '@/shared/model/family.model';
import FamilyService from './family.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FamilyDetails extends Vue {
  @Inject('familyService') private familyService: () => FamilyService;
  @Inject('alertService') private alertService: () => AlertService;

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
}
