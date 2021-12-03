import { Component,Inject, Vue } from 'vue-property-decorator';
import ContactService from './Contact.component.service';

@Component
export default class JhiContactComponent extends Vue {

  @Inject('productService') private ContactService: () => ContactService;

  public error = '';
  public success = '';
  public products: any[] = [];
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;
  public isLoading = false;
  public removeId: number = null;



  public mounted(): void {
    this.loadAll();
  }

  public setActive(product, isActivated): void {


    product.activated = isActivated;
    this.ContactService()
      .update(product)
      .then(() => {
        this.error = null;
        this.success = 'OK';
        this.loadAll();
      })
      .catch(() => {
        this.success = null;
        this.error = 'ERROR';
        product.activated = false;
      });
  }

  public loadAll(): void {
    this.isLoading = true;

    console.log(this.$route.query.param1);

    this.ContactService()
      .retrieve({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.products = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.isLoading = false;
      });
  }


    public sort(): any {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }
}