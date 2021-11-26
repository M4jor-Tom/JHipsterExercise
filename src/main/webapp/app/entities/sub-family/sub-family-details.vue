<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subFamily">
        <h2 class="jh-entity-heading" data-cy="subFamilyDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.subFamily.detail.title')">SubFamily</span> {{ subFamily.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.subFamily.name')">Name</span>
          </dt>
          <dd>
            <span>{{ subFamily.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.subFamily.family')">Family</span>
          </dt>
          <dd>
            <div v-if="subFamily.family">
              <router-link :to="{ name: 'FamilyView', params: { familyId: subFamily.family.id } }">{{ subFamily.family.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="subFamily.id"
          :to="{ name: 'SubFamilyEdit', params: { subFamilyId: subFamily.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./sub-family-details.component.ts"></script>
