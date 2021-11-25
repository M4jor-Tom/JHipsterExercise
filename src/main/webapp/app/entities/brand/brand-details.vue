<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="brand">
        <h2 class="jh-entity-heading" data-cy="brandDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.brand.detail.title')">Brand</span> {{ brand.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.brand.name')">Name</span>
          </dt>
          <dd>
            <span>{{ brand.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="brand.id" :to="{ name: 'BrandEdit', params: { brandId: brand.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./brand-details.component.ts"></script>
