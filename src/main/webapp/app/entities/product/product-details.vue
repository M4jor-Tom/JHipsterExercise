<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="product">
        <h2 class="jh-entity-heading" data-cy="productDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.product.detail.title')">Product</span> {{ product.id }}
        </h2>
        <dl class="row jh-entity-details">
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
            <span v-text="$t('jHipsterExerciseApp.product.price')">Price</span>
          </dt>
          <dd>
            <span>{{ product.price }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.modelName')">Model Name</span>
          </dt>
          <dd>
            <span>{{ product.modelName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.color')">Color</span>
          </dt>
          <dd>
            <span v-text="$t('jHipsterExerciseApp.Color.' + product.color)">{{ product.color }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.subFamily')">Sub Family</span>
          </dt>
          <dd>
            <div v-if="product.subFamily">
              <router-link :to="{ name: 'SubFamilyView', params: { subFamilyId: product.subFamily.id } }">{{
                product.subFamily.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.brand')">Brand</span>
          </dt>
          <dd>
            <div v-if="product.brand">
              <router-link :to="{ name: 'BrandView', params: { brandId: product.brand.id } }">{{ product.brand.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.seller')">Seller</span>
          </dt>
          <dd>
            <div v-if="product.seller">
              <router-link :to="{ name: 'SellerView', params: { sellerId: product.seller.id } }">{{ product.seller.email }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.product.tags')">Tags</span>
          </dt>
          <dd>
            <span v-for="(tags, i) in product.tags" :key="tags.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TagView', params: { tagId: tags.id } }">{{ tags.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="product.id" :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
          <button v-if="(hasAdminAuthority('ROLE_ADMIN') || hasSellerAuthority('ROLE_SELLER')) && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product-details.component.ts"></script>
