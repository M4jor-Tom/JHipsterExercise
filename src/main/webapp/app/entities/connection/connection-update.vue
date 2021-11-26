<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.connection.home.createOrEditLabel"
          data-cy="ConnectionCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.connection.home.createOrEditLabel')"
        >
          Create or edit a Connection
        </h2>
        <div>
          <div class="form-group" v-if="connection.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="connection.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.connection.username')" for="connection-username"
              >Username</label
            >
            <input
              type="text"
              class="form-control"
              name="username"
              id="connection-username"
              data-cy="username"
              :class="{ valid: !$v.connection.username.$invalid, invalid: $v.connection.username.$invalid }"
              v-model="$v.connection.username.$model"
              required
            />
            <div v-if="$v.connection.username.$anyDirty && $v.connection.username.$invalid">
              <small class="form-text text-danger" v-if="!$v.connection.username.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.connection.password')" for="connection-password"
              >Password</label
            >
            <input
              type="text"
              class="form-control"
              name="password"
              id="connection-password"
              data-cy="password"
              :class="{ valid: !$v.connection.password.$invalid, invalid: $v.connection.password.$invalid }"
              v-model="$v.connection.password.$model"
              required
            />
            <div v-if="$v.connection.password.$anyDirty && $v.connection.password.$invalid">
              <small class="form-text text-danger" v-if="!$v.connection.password.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.connection.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./connection-update.component.ts"></script>
