<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="model">
        <h2 class="jh-entity-heading" data-cy="modelDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.model.detail.title')">Model</span> {{ model.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.model.name')">Name</span>
          </dt>
          <dd>
            <span>{{ model.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.model.brand')">Brand</span>
          </dt>
          <dd>
            <div v-if="model.brand">
              <router-link :to="{ name: 'BrandView', params: { brandId: model.brand.id } }">{{ model.brand.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.model.family')">Family</span>
          </dt>
          <dd>
            <div v-if="model.family">
              <router-link :to="{ name: 'FamilyView', params: { familyId: model.family.id } }">{{ model.family.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="model.id" :to="{ name: 'ModelEdit', params: { modelId: model.id } }" custom v-slot="{ navigate }">
          <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./model-details.component.ts"></script>
