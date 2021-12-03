import { Component,Inject, Vue } from 'vue-property-decorator';
import { ids } from 'webpack';
import ListProduitService from './List_Produit.component.service';
import SubFamilyService from './subfamily.service';
import FamilyService from './family.service';
import OrderService from './order.service';
import ClientService from './order.service';
import { OrderState } from '@/shared/model/enumerations/order-state.model';


@Component
export default class JhiListProduitComponent extends Vue {

  @Inject('productService') private ListProduitService: () => ListProduitService;
  @Inject('subFamilyService') private SubFamilyService: () =>  SubFamilyService;
  @Inject('familyService') private FamilyService: () =>  FamilyService;
  @Inject('orderService') private OrderService: () =>  OrderService;
  @Inject('clientService') private ClientService: () =>  ClientService;


  public error = '';
  public success = '';
  public products: any[] = [];
  public subfamilies: any[] = [];
  public families: any[] = [];
  public orders: any[] = [];
  public clients: any[] = [];
  public propOrder = 'id';
  public reverse = false;
  public isLoading = false;

  



  public mounted(): void {
    this.loadAllSubFamilies(); 
    this.loadAllFamilies(); 
    this.loadAllClients(); 
    this.loadAllOrders();   
    this.loadAll();
     
  }

  public setActive(product, isActivated): void {

    product.activated = isActivated;
    this.ListProduitService()
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


    this.ListProduitService()
      .retrieve({
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.products = res.data;
        if(this.$route.query.cat != null)
        {
          for(var i = 0;i < this.products.length;i++)
          {            
            if(!(this.subfamilies[this.products[i].subFamily.id -1].family.name === this.$route.query.cat))
            {
              this.products.splice(i,1);
              i -= 1;
            }  
          } 
        }
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

  public loadAllSubFamilies(): void {
    this.isLoading = true;
    this.SubFamilyService()
      .retrieve({
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.subfamilies = res.data; 
        console.log(this.subfamilies); 
      })
      .catch(() => {
        this.isLoading = false;
      });
  }
  
  public loadAllFamilies(): void {
    this.isLoading = true;
    this.FamilyService()
      .retrieve({
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.families = res.data;       
      })
      .catch(() => {
        this.isLoading = false;
      });
  }

  public loadAllOrders(): void {
    this.isLoading = true;
    this.OrderService()
      .retrieve({
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.orders = res.data;       
      })
      .catch(() => {
        this.isLoading = false;
      });
  }

  public loadAllClients(): void {
    this.isLoading = true;
    this.ClientService()
      .retrieve({
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.clients = res.data;       
      })
      .catch(() => {
        this.isLoading = false;
      });
  }

  public AddCart(productChoice): void {

    
    var tempindex = 0;
    this.orders.forEach((element,index) => {

      if((element.client.id == this.$store.getters.account.id) && (element.orderState == OrderState.PROCESSING))
      {
        
          tempindex = 1;
          element.products.push(productChoice)
          console.log("exist");
      }
    });
    if(tempindex == 0)
    { 
      var orderTemp = this.orders[0];
      orderTemp.id = null;
      orderTemp.orderState = OrderState.PROCESSING;
      orderTemp.client = this.clients[this.$store.getters.account.id - 1];
      this.OrderService()      
      .create(orderTemp)
      .then(response => { 
        console.log(response)
      })
      .catch(error => {
          console.log(error.response)
      });
      console.log("ok");
    }


  }

 

}