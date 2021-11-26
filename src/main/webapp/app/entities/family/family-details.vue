<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="family">
        <h2 class="jh-entity-heading" data-cy="familyDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.family.detail.title')">Family</span> {{ family.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.family.name')">Name</span>
          </dt>
          <dd>
            <span>{{ family.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="family.id" :to="{ name: 'FamilyEdit', params: { familyId: family.id } }" custom v-slot="{ navigate }">
          <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./family-details.component.ts"></script>
