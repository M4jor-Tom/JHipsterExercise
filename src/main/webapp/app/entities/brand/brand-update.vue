<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.brand.home.createOrEditLabel"
          data-cy="BrandCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.brand.home.createOrEditLabel')"
        >
          Create or edit a Brand
        </h2>
        <div>
          <div class="form-group" v-if="brand.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="brand.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.brand.name')" for="brand-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="brand-name"
              data-cy="name"
              :class="{ valid: !$v.brand.name.$invalid, invalid: $v.brand.name.$invalid }"
              v-model="$v.brand.name.$model"
              required
            />
            <div v-if="$v.brand.name.$anyDirty && $v.brand.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.brand.name.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.brand.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./brand-update.component.ts"></script>
