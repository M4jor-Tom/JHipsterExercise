<template>
  <div >

    <div class="img_banniere">
        <img src="/content/images/home/Noel_banniere.png" />
    </div>
    <div> 
      <p class="customfont" style="text-align: center; font-size:100px;"> Un Noel pour tous Zarma</p>      
    </div>

    <div class="separate-3_nav"> 

      <div class="category"  v-on:click="goProduct('/list_produit?param1=enfant')">
          <img class="img_category" src="/content/images/home/Enfant.png"/>
          <p class="centered customfont text_category" > Enfant </p>
      </div>
      <div class="category"  v-on:click="goProduct('/list_produit')">
          <img class="img_category" src="/content/images/home/Adulte.png"/>
          <p class="centered customfont text_category" > Adult </p>
      </div>

      <div class="category"  v-on:click="goProduct('/list_produit')">
          <img class="img_category" src="/content/images/home/BonVivant.png"/>          
          <p class="centered customfont text_category" > Bon vivant </p>
      </div>
         
    </div>
     


  </div>
</template> 

<script lang="ts" src="./home.component.ts"></script>

<style>

.category{
  background:black;
  position: relative;
  text-align: center;
  border-radius:40px;
}

.text_category {
font-size:50px;
color:white;
}

.img_category{
  width:100%;
  height:100%;
  filter: brightness(50%);
  border-radius:40px;

}

.img_banniere{
  width: 100%;
  height: 100%;
  text-align: center;
  display:block;

}

.separate-3_nav{

  display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    grid-gap : 10px;

  
}

/* Centered text */
.centered {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}


@font-face {
    font-family: "My Custom Font";
    src: url(./AlexBrush.ttf) format("truetype");
}
p.customfont { 
    font-family: "My Custom Font", Verdana, Tahoma;
}

</style>
