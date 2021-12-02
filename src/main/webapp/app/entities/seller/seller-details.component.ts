import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISeller } from '@/shared/model/seller.model';
import SellerService from './seller.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class SellerDetails extends Vue {
  @Inject('sellerService') private sellerService: () => SellerService;
  @Inject('alertService') private alertService: () => AlertService;

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

  public previousState() {
    this.$router.go(-1);
  }
}
