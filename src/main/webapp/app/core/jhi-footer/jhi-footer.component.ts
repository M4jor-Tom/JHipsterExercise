import { Component, Vue } from 'vue-property-decorator';

@Component
export default class JhiFooter extends Vue {
  constructor() {
    super();
    //changeImg();
  }
}

function changeImg(): void {
  alert('Test modif live...');
  var num = Math.floor(Math.random() * 16 + 1); 
  const randomimage = "/content/images/product" + num +".png";
  console.log(num);
  console.log("JE SUIS DANS CHANGIMG");
  (document.getElementById('FooterImage1') as HTMLImageElement).src = randomimage;
  (document.getElementById('FooterImage2') as HTMLImageElement).src = '/content/images/pub/bg2.png';
  //(document.getElementById("#FooterImage1") as HTMLImageElement).src = "/content/images/pub/bg1.png";
  //(document.getElementById('FooterImage1') as HTMLImageElement).src = "/content/images/pub/bg1.png";
  //(document.getElementById('FooterImage2') as HTMLImageElement).src = "/content/images/pub/bg2.png";
  //(document.getElementById('FooterImage1') as HTMLImageElement).src = "src/main/webapp/content/images/bg3.png";
  //(document.getElementById('FooterImage2') as HTMLImageElement).src = "src/main/webapp/content/images/bg4.png";
  //(document.getElementById('FooterImage1') as HTMLImageElement).src = 'src/main/webapp/content/images/bg5.png';
  //(document.getElementById('FooterImage2') as HTMLImageElement).src = 'src/main/webapp/content/images/bg6.png';
}
