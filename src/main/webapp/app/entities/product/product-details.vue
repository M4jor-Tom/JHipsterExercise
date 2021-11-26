<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="product">
        <h2 class="jh-entity-heading" data-cy="productDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.product.detail.title')">Product</span> {{ product.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.name')">Name</span>
          </dt>
          <dd>
            <span>{{ product.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.description')">Description</span>
          </dt>
          <dd>
            <span>{{ product.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.photoId')">Photo Id</span>
          </dt>
          <dd>
            <span>{{ product.photoId }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.stock')">Stock</span>
          </dt>
          <dd>
            <span>{{ product.stock }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.tag')">Tag</span>
          </dt>
          <dd>
            <span>{{ product.tag }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.tariff')">Tariff</span>
          </dt>
          <dd>
            <span>{{ product.tariff }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.color')">Color</span>
          </dt>
          <dd>
            <span v-text="$t('jHipsterExerciseApp.Color.' + product.color)">{{ product.color }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.model')">Model</span>
          </dt>
          <dd>
            <div v-if="product.model">
              <router-link :to="{ name: 'ModelView', params: { modelId: product.model.id } }">{{ product.model.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="product.id" :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
          <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product-details.component.ts"></script>
