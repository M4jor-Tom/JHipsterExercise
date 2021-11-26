<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="order">
        <h2 class="jh-entity-heading" data-cy="orderDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.order.detail.title')">Order</span> {{ order.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.order.sum')">Sum</span>
          </dt>
          <dd>
            <span>{{ order.sum }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.order.deliveyAdress')">Delivey Adress</span>
          </dt>
          <dd>
            <span>{{ order.deliveyAdress }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.order.deliveryDateTime')">Delivery Date Time</span>
          </dt>
          <dd>
            <span v-if="order.deliveryDateTime">{{ $d(Date.parse(order.deliveryDateTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.order.billingMethod')">Billing Method</span>
          </dt>
          <dd>
            <span v-text="$t('jHipsterExerciseApp.BillingMethod.' + order.billingMethod)">{{ order.billingMethod }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.order.products')">Products</span>
          </dt>
          <dd>
            <span v-for="(products, i) in order.products" :key="products.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ProductView', params: { productId: products.id } }">{{ products.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.order.client')">Client</span>
          </dt>
          <dd>
            <div v-if="order.client">
              <router-link :to="{ name: 'ClientView', params: { clientId: order.client.id } }">{{ order.client.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="order.id" :to="{ name: 'OrderEdit', params: { orderId: order.id } }" custom v-slot="{ navigate }">
          <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./order-details.component.ts"></script>
