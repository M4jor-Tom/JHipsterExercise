<template>
  <div class="paypal row">
    <div class="col-md-9">
      <h1 class="display-4" v-text="$t('paypal.title')">Welcome, Java Hipster!</h1>
      <p class="lead" v-text="$t('paypal.subtitle')">This is your paypalpage</p>

      <div>
        <div class="alert alert-success" v-if="authenticated">
          <span v-if="username" v-text="$t('paypal.logged.message', { username: username })"
            >You are logged in as user "{{ username }}"</span
          >
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          <span v-text="$t('global.messages.info.authenticated.prefix')">If you want to </span>
          <a class="alert-link" v-on:click="openLogin()" v-text="$t('global.messages.info.authenticated.link')">sign in</a
          ><span v-html="$t('global.messages.info.authenticated.suffix')"
            >, you can try the default accounts:<br />- Administrator (login="admin" and password="admin") <br />- User (login="user" and
            password="user").</span
          >
        </div>
        <div class="alert alert-warning" v-if="!authenticated">
          <span v-text="$t('global.messages.info.register.noaccount')">You don't have an account yet?</span>&nbsp;
          <router-link class="alert-link" to="/register" v-text="$t('global.messages.info.register.link')"
            >Register a new account</router-link
          >
        </div>
      </div>

      <p v-text="$t('paypal.question')">If you have any question on JHipster:</p>

      <ul>
        <li>
          <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer" v-text="$t('paypal.link.paypalpage')"
            >JHipster paypalpage</a
          >
        </li>
        <li>
          <a
            href="http://stackoverflow.com/tags/jhipster/info"
            target="_blank"
            rel="noopener noreferrer"
            v-text="$t('paypal.link.stackoverflow')"
            >JHipster on Stack Overflow</a
          >
        </li>
        <li>
          <a
            href="https://github.com/jhipster/generator-jhipster/issues?state=open"
            target="_blank"
            rel="noopener noreferrer"
            v-text="$t('paypal.link.bugtracker')"
            >JHipster bug tracker</a
          >
        </li>
        <li>
          <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer" v-text="$t('paypal.link.chat')"
            >JHipster public chat room</a
          >
        </li>
        <li>
          <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer" v-text="$t('paypal.link.follow')"
            >follow @jhipster on Twitter</a
          >
        </li>
      </ul>

      <p>
        <span v-text="$t('paypal.like')">If you like JHipster, don't forget to give us a star on</span>
        <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer" v-text="$t('paypal.github')"
          >GitHub</a
        >!
      </p>
      <div id="paypal-button-container"></div>
    </div>
  </div>
</template>

<script src="https://www.paypal.com/sdk/js?client-id=AZyVvavpkhKOdesqvT8jWNnYsgsZ81UiBvTND4swJEs9oT4AjXU-biqVsTC657gHpxWFcdzX43BFsem1"></script>
<script src="./paypal.js"></script>
<script lang="ts" src="./paypal.component.ts"></script>
