<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.model.home.createOrEditLabel"
          data-cy="ModelCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.model.home.createOrEditLabel')"
        >
          Create or edit a Model
        </h2>
        <div>
          <div class="form-group" v-if="model.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="model.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.model.name')" for="model-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="model-name"
              data-cy="name"
              :class="{ valid: !$v.model.name.$invalid, invalid: $v.model.name.$invalid }"
              v-model="$v.model.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.model.brand')" for="model-brand">Brand</label>
            <select class="form-control" id="model-brand" data-cy="brand" name="brand" v-model="model.brand">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="model.brand && brandOption.id === model.brand.id ? model.brand : brandOption"
                v-for="brandOption in brands"
                :key="brandOption.id"
              >
                {{ brandOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.model.family')" for="model-family">Family</label>
            <select class="form-control" id="model-family" data-cy="family" name="family" v-model="model.family">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="model.family && familyOption.id === model.family.id ? model.family : familyOption"
                v-for="familyOption in families"
                :key="familyOption.id"
              >
                {{ familyOption.id }}
              </option>
            </select>
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
            :disabled="$v.model.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./model-update.component.ts"></script>
