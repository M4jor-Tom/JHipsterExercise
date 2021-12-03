<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="seller">
        <h2 class="jh-entity-heading" data-cy="sellerDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.seller.detail.title')">Seller</span> {{ seller.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.seller.socialReason')">Social Reason</span>
          </dt>
          <dd>
            <span>{{ seller.socialReason }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.seller.address')">Address</span>
          </dt>
          <dd>
            <span>{{ seller.address }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.seller.siretNumber')">Siret Number</span>
          </dt>
          <dd>
            <span>{{ seller.siretNumber }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.seller.phone')">Phone</span>
          </dt>
          <dd>
            <span>{{ seller.phone }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.seller.email')">Email</span>
          </dt>
          <dd>
            <span>{{ seller.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.seller.connection')">Connection</span>
          </dt>
          <dd>
            <div v-if="seller.connection">
              <router-link :to="{ name: 'ConnectionView', params: { connectionId: seller.connection.id } }">{{
                seller.connection.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="seller.id" :to="{ name: 'SellerEdit', params: { sellerId: seller.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./seller-details.component.ts"></script>
