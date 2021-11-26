<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="connection">
        <h2 class="jh-entity-heading" data-cy="connectionDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.connection.detail.title')">Connection</span> {{ connection.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.connection.username')">Username</span>
          </dt>
          <dd>
            <span>{{ connection.username }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.connection.password')">Password</span>
          </dt>
          <dd>
            <span>{{ connection.password }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="connection.id"
          :to="{ name: 'ConnectionEdit', params: { connectionId: connection.id } }"
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

<script lang="ts" src="./connection-details.component.ts"></script>
