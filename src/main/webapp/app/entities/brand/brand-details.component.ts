import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBrand } from '@/shared/model/brand.model';
import BrandService from './brand.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class BrandDetails extends Vue {
  @Inject('brandService') private brandService: () => BrandService;
  @Inject('alertService') private alertService: () => AlertService;

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
}
