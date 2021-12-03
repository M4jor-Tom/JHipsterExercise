<template>
  <div >

    <div class="img_banniere">
        <img src="/content/images/home/Noel_banniere.jpg" />
    </div>
    <div> 
      <p class="customfont" v-text="$t('welcomeMessage')" style="text-align: center; font-size:100px;"> Un Noel pour tous</p>      
    </div>

    <div class="separate-3_nav"> 

      <div class="category"  v-on:click="goProduct('/list_produit?cat=High-Tech')">
          <img class="img_category" src="/content/images/home/HighTech.png"/>
          <p v-text="$t('highTech')" class="centered customfont text_category" > High Tech </p>
      </div>
      <div class="category"  v-on:click="goProduct('/list_produit?cat=Vetements')">
          <img class="img_category" src="/content/images/home/Vetement.png"/>
          <p v-text="$t('vetement')" class="centered customfont text_category" > Vetement </p>
      </div>

      <div class="category"  v-on:click="goProduct('/list_produit?cat=Transport')">
          <img class="img_category" src="/content/images/home/Transport.png"/>          
          <p v-text="$t('transport')" class="centered customfont text_category" > Transport </p>
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
