import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, decimal } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import SubFamilyService from '@/entities/sub-family/sub-family.service';
import { ISubFamily } from '@/shared/model/sub-family.model';

import BrandService from '@/entities/brand/brand.service';
import { IBrand } from '@/shared/model/brand.model';

import TagService from '@/entities/tag/tag.service';
import { ITag } from '@/shared/model/tag.model';

import OrderService from '@/entities/order/order.service';
import { IOrder } from '@/shared/model/order.model';

import { IProduct, Product } from '@/shared/model/product.model';
import ProductService from './product.service';
import { Color } from '@/shared/model/enumerations/color.model';

const validations: any = {
  product: {
    description: {},
    photoId: {},
    stock: {
      required,
      numeric,
    },
    price: {
      required,
      decimal,
    },
    modelName: {
      required,
    },
    color: {},
  },
};

@Component({
  validations,
})
export default class ProductUpdate extends Vue {
  @Inject('productService') private productService: () => ProductService;
  @Inject('alertService') private alertService: () => AlertService;

  public product: IProduct = new Product();

  @Inject('subFamilyService') private subFamilyService: () => SubFamilyService;

  public subFamilies: ISubFamily[] = [];

  @Inject('brandService') private brandService: () => BrandService;

  public brands: IBrand[] = [];

  @Inject('tagService') private tagService: () => TagService;

  public tags: ITag[] = [];

  @Inject('orderService') private orderService: () => OrderService;

  public orders: IOrder[] = [];
  public colorValues: string[] = Object.keys(Color);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productId) {
        vm.retrieveProduct(to.params.productId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.product.tags = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.product.id) {
      this.productService()
        .update(this.product)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.product.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.productService()
        .create(this.product)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jHipsterExerciseApp.product.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveProduct(productId): void {
    this.productService()
      .find(productId)
      .then(res => {
        this.product = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.subFamilyService()
      .retrieve()
      .then(res => {
        this.subFamilies = res.data;
      });
    this.brandService()
      .retrieve()
      .then(res => {
        this.brands = res.data;
      });
    this.tagService()
      .retrieve()
      .then(res => {
        this.tags = res.data;
      });
    this.orderService()
      .retrieve()
      .then(res => {
        this.orders = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
