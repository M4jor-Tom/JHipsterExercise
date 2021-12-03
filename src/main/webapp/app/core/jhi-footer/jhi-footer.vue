<template>
  <footer>
    <div class="footer-content">
      <h3>JHipster Exercise</h3>
      <p v-text="$t('footer')">We sell everything you need: cars, clothes, phones...</p>
      <ul class="socials">
        <li>
          <a href="#"><i class="fab fa-facebook-f fa-fw"></i></a>
        </li>
        <li>
          <a href="#"><i class="fab fa-twitter fa-fw"></i></a>
        </li>
        <li>
          <a href="#"><i class="fab fa-instagram fa-fw"></i></a>
        </li>
        <li>
          <a href="#"><i class="fab fa-youtube fa-fw"></i></a>
        </li>
        <li>
          <a href="#"><i class="fab fa-linkedin fa-fw"></i></a>
        </li>
      </ul>
    </div>
    <div class="footer-bottom">
      <p>copyright &copy; <a href="#">JHipster Exercise</a></p>
      <div class="footer-menu">
        <ul class="f-menu">
          <li><a href="/">Home</a></li>
          <li><a href="/cart">Cart</a></li>
          <li><a href="/contact">Contact</a></li>
        </ul>
      </div>
    </div>
  </footer>
</template>

<script lang="ts" src="./jhi-footer.component.ts"></script>

<style media="screen">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html {
  height: 100%;
}
body {
  background: #fcfcfc;
  font-family: sans-serif;
  position: relative;
  min-height: 100%;
  padding-bottom: 260px;
}
footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 260px;
  left: 0;
  right: 0;
  background: #111;
  padding-top: 40px;
  color: #fff;
}
.footer-content {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  text-align: center;
}
.footer-content h3 {
  font-size: 2.1rem;
  font-weight: 500;
  text-transform: capitalize;
  line-height: 3rem;
}
.footer-content p {
  max-width: 500px;
  margin: 10px auto;
  line-height: 28px;
  font-size: 14px;
  color: #cacdd2;
}
.socials {
  list-style: none;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 1rem 0 3rem 0;
}
.socials li {
  margin: 0 10px;
}
.socials a {
  text-decoration: none;
  color: #fff;
  border: 1.1px solid white;
  padding: 5px;

  border-radius: 50%;
}
.socials a i {
  font-size: 1.1rem;
  width: 20px;

  transition: color 0.4s ease;
}
.socials a:hover i {
  color: aqua;
}

.footer-bottom {
  background: #000;
  padding: 20px;
  padding-bottom: 40px;
  text-align: center;
}
.footer-bottom p {
  float: left;
  font-size: 14px;
  word-spacing: 2px;
  text-transform: capitalize;
}
.footer-bottom p a {
  color: #44bae8;
  font-size: 16px;
  text-decoration: none;
}
.footer-bottom span {
  text-transform: uppercase;
  opacity: 0.4;
  font-weight: 200;
}
.footer-menu {
  float: right;
}
.footer-menu ul {
  display: flex;
}
.footer-menu ul li {
  padding-right: 10px;
  display: block;
}
.footer-menu ul li a {
  color: #cfd2d6;
  text-decoration: none;
}
.footer-menu ul li a:hover {
  color: #27bcda;
}

@media (max-width: 500px) {
  .footer-menu ul {
    display: flex;
    margin-bottom: 20px;
  }
}
</style>
