<template>
  <div class="home row">
  
    <div class="separate-2_nav">

      <div>

         <!-- Filtre -->

      </div>

      <div class="container" v-if="products">
        <div class="card" v-for="product in products" :key="product.id" :id="product.modelName" >
          <p class="price">${{product.price}} USD</p>

          <i class="info fas fa-info-circle"></i>
          <p class="text">{{product.modelName}}.</p>

          <div class="imgBx">
            <img :src="'/content/images/product/' + product.id + '.png'" />
          </div>
          <div class="contentBx">
            <h2>{{product.modelName}}</h2>
            <div class="size">
                <h3>Size :</h3>
                <span>6</span>
                <span>7</span>
                <span>8</span>
                <span>9</span>
                <span>10</span>
            </div>
            <div class="color">
                <h3>Color:</h3>
                <span></span>
                <span></span>
                <span></span>
            </div>
            <button>Add to Cart</button>
          </div>

        </div>
      </div>

    </div>
    
  </div>
</template>

<script lang="ts" src="./list_produit.component.ts"></script>
<style src="./list_product.css"></style>


<style>


.separate-2_nav{

  display: grid;
  grid-template-columns: 1fr 5fr;
  grid-gap : 10px;

}




</style>

