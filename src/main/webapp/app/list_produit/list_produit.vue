<template>
  <div class="home row">
  
    <div class="separate-2_nav">

      <div>

         <!-- Filtre -->
        <br>
        <br>
        <a> Search :</a>
        <br>
        <input type="text" id="myInput"  placeholder="Search.." onkeyup="myFunction">
        <br>
        <br>
        <div class="dropdown categ">
          <button class="dropbtn" style="font-size:20px"> Categorie :</button>
          <div class="dropdown-content">
            <a :href="'http://localhost:8080/list_produit?cat=' + family.name" 
            v-for="family in families" :key="family.id" :id="family.name" >
             {{family.name}} </a>

           <!-- <a href="http://localhost:8080/list_produit?cat=enfant"> Enfant </a>
            <a href="http://localhost:8080/list_produit?cat=adulte"> Adulte </a>
            <a href="http://localhost:8080/list_produit?cat=alcool"> Alcool </a>-->
          </div>
        </div> 

        

      </div>

      <div class="container" v-if="products">
        <div :class="'card ' + product.subFamily.name"  v-for="product in products" :key="product.id" :id="product.modelName" >
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
            <div id="example">
              <button @click="AddCart(product)">Add to cart</button>
            </div>
          </div>

        </div>
      </div>

    </div>


  </div>


  
</template>


<script>
export default {
  methods: {
    myFunction: function(event) {
      // `event` is the native DOM event
      console.log("ok");
      var input, filter, ul, li, a, i, txtValue;
      input = document.getElementById("myInput");
      filter = input.value.toUpperCase();
      li = ul.getElementsByClassName("card");
      for (i = 0; i < li.length; i++) {
          a = li[i].getElementsById()[0];
          txtValue = a.textContent || a.innerText;
          if (txtValue.toUpperCase().indexOf(filter) > -1) {
              li[i].style.display = "";
          } else {
              li[i].style.display = "none";
          }
      }
    }
  }
};
</script>



<script lang="ts" src="./list_produit.component.ts"></script>
<style src="./list_product.css"></style>


<style>


.separate-2_nav{

  display: grid;
  grid-template-columns: 1fr 6fr;
  grid-gap : 10px;

}

.dropbtn {
    background-color: rgba(95, 244, 255, 0.473);
    color: black;
    padding: 5px;
    font-size: 12px;
}
.dropdown .categ{
    display: inline-block;
    position: relative;
    width: 150px;

}
.dropdown-content {
    position: absolute;
    background-color: lightgrey;
    min-width: 200px;
    display: none;
    z-index: 1;
}
.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
}
.dropdown-content a:hover {
    background-color: rgba(53, 206, 226, 0.473);
}
.dropdown:hover .dropdown-content {
    display: block;
}
.dropdown:hover .dropbtn {
    background-color: grey;
}




</style>

