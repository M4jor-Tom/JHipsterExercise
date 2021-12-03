<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="client">
        <h2 class="jh-entity-heading" data-cy="clientDetailsHeading">
          <span v-text="$t('jHipsterExerciseApp.client.detail.title')">Client</span> {{ client.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.addedDateTime')">Added Date Time</span>
          </dt>
          <dd>
            <span v-if="client.addedDateTime">{{ $d(Date.parse(client.addedDateTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.lastName')">Last Name</span>
          </dt>
          <dd>
            <span>{{ client.lastName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.firstName')">First Name</span>
          </dt>
          <dd>
            <span>{{ client.firstName }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.email')">Email</span>
          </dt>
          <dd>
            <span>{{ client.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.phone')">Phone</span>
          </dt>
          <dd>
            <span>{{ client.phone }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.adress')">Adress</span>
          </dt>
          <dd>
            <span>{{ client.adress }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.country')">Country</span>
          </dt>
          <dd>
            <span>{{ client.country }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.postalCode')">Postal Code</span>
          </dt>
          <dd>
            <span>{{ client.postalCode }}</span>
          </dd>
          <dt>
            <span v-text="$t('jHipsterExerciseApp.client.connection')">Connection</span>
          </dt>
          <dd>
            <div v-if="client.connection">
              <router-link :to="{ name: 'ConnectionView', params: { connectionId: client.connection.id } }">{{
                client.connection.username
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="client.id" :to="{ name: 'ClientEdit', params: { clientId: client.id } }" custom v-slot="{ navigate }">
          <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./client-details.component.ts"></script>
