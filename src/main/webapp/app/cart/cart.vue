<template>
  <div>
   
    <div class="container-fluid">
      <div class="row">
          <aside class="col-lg-9">
              <div class="card">
                  <div class="table-responsive">
                      <table class="table table-borderless table-shopping-cart">
                          <thead class="text-muted">
                              <tr class="small text-uppercase">
                                  <th scope="col" v-text="$t('product')">Product</th>
                                  <th scope="col" width="120" v-text="$t('quantity')">Quantity</th>
                                  <th scope="col" width="120" v-text="$t('price')">Price</th>
                                  <th scope="col" class="text-right d-none d-md-block" width="200"></th>
                              </tr>
                          </thead>
                          <tbody v-if="products">  
                              <tr  v-for="product in products" :key="product.id" :id="product.modelName">
                                  <td>
                                      <figure class="itemside align-items-center">
                                          <div class="aside" v-show="product.modelName">
                                              <img  :src="'/content/images/product/' + product.id + '.png'" class="img-sm">
                                          </div>
                                          <figcaption class="info"> <a href="#" class="title text-dark" data-abc="true">{{ product.brandNameWithModelName }}</a>
                                                   <!--<p class="small text-muted">SIZE:M <br> Brand: Cantabil</p>-->
                                          </figcaption>
                                      </figure>
                                  </td>
                                  <td> <select class="form-control">
                                          <option>1</option>
                                          <option>2</option>
                                          <option>3</option>
                                      </select> </td>
                                  <td>
                                      <div class="price-wrap price"> ${{ product.price}}</div> 
                                     <!-- <div class="price-wrap"> <var class="price">$9</var> <small class="text-muted"> $6 each</small> </div>-->
                                  </td>
                             </tr>
                          </tbody>
                      </table>
                  </div>
              </div>
          </aside>
          <aside class="col-lg-3">
              <div class="card mb-3">
                  <div class="card-body">
                      <form>
                          <div class="form-group"> <label v-text="$t('enterpromocode')">Have coupon?</label>
                              <div class="input-group"> <input type="text" class="form-control coupon" name="" v-bind:placeholder="$t('promocode')"> <span class="input-group-append"> <button class="btn btn-primary btn-apply coupon" v-text="$t('apply')">Apply</button> </span> </div>
                          </div>
                      </form>
                  </div>
              </div>
              <div class="card">
                  <div class="card-body">
                      <dl class="dlist-align">
                          <dt v-text="$t('totalprice')">Total price:</dt>
                          <dd class="text-right ml-3">$69.97</dd>
                      </dl>
                      <dl class="dlist-align">
                          <dt v-text="$t('discount')">Discount:</dt>
                          <dd class="text-right text-danger ml-3">- $10.00</dd>
                      </dl>
                      <dl class="dlist-align">
                          <dt v-text="$t('total')">Total:</dt>
                          <dd class="text-right text-dark b ml-3"><strong>$59.97</strong></dd>
                      </dl>
                      <hr> <a href="paypal" class="btn btn-out btn-primary btn-square btn-main" data-abc="true" v-text="$t('makepurch')"> Make Purchase </a> <a href="#" class="btn btn-out btn-success btn-square btn-main mt-2" data-abc="true" v-text="$t('continuepurch')">Continue Shopping</a>
                  </div>
              </div>
          </aside>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cart.component.ts"></script>


<!-- CSS -->
<style scoped>
body {
    background-color: #eeeeee;
    font-family: 'Open Sans', serif;
    font-size: 14px
}

.container-fluid {
    margin-top: 70px
}

.card-body {
    -ms-flex: 1 1 auto;
    flex: 1 1 auto;
    padding: 1.40rem
}

.img-sm {
    width: 130px;
    height: 90px
}

.itemside .info {
    padding-left: 15px;
    padding-right: 7px
}

.table-shopping-cart .price-wrap {
    line-height: 1.2
}

.table-shopping-cart .price {
    font-weight: bold;
    margin-right: 5px;
    display: block
}

.text-muted {
    color: #969696 !important
}

a {
    text-decoration: none !important
}

.card {
    position: relative;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-direction: column;
    flex-direction: column;
    min-width: 0;
    word-wrap: break-word;
    background-color: #fff;
    background-clip: border-box;
    border: 1px solid rgba(0, 0, 0, .125);
    border-radius: 0px
}

.itemside {
    position: relative;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    width: 100%
}

.dlist-align {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex
}

[class*="dlist-"] {
    margin-bottom: 5px
}

.coupon {
    border-radius: 1px
}

.price {
    font-weight: 600;
    color: #212529
}

.btn.btn-out {
    outline: 1px solid #fff;
    outline-offset: -5px
}

.btn-main {
    border-radius: 2px;
    text-transform: capitalize;
    font-size: 15px;
    padding: 10px 19px;
    cursor: pointer;
    color: #fff;
    width: 100%
}

.btn-light {
    color: #ffffff;
    background-color: #F44336;
    border-color: #f8f9fa;
    font-size: 12px
}

.btn-light:hover {
    color: #ffffff;
    background-color: #F44336;
    border-color: #F44336
}

.btn-apply {
    font-size: 11px
}

</style>

