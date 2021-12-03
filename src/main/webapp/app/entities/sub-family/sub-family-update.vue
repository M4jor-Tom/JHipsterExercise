<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.subFamily.home.createOrEditLabel"
          data-cy="SubFamilyCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.subFamily.home.createOrEditLabel')"
        >
          Create or edit a SubFamily
        </h2>
        <div>
          <div class="form-group" v-if="subFamily.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="subFamily.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.subFamily.name')" for="sub-family-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="sub-family-name"
              data-cy="name"
              :class="{ valid: !$v.subFamily.name.$invalid, invalid: $v.subFamily.name.$invalid }"
              v-model="$v.subFamily.name.$model"
              required
            />
            <div v-if="$v.subFamily.name.$anyDirty && $v.subFamily.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.subFamily.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.subFamily.family')" for="sub-family-family">Family</label>
            <select class="form-control" id="sub-family-family" data-cy="family" name="family" v-model="subFamily.family" required>
              <option v-if="!subFamily.family" v-bind:value="null" selected></option>
              <option
                v-bind:value="subFamily.family && familyOption.id === subFamily.family.id ? subFamily.family : familyOption"
                v-for="familyOption in families"
                :key="familyOption.id"
              >
                {{ familyOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.subFamily.family.$anyDirty && $v.subFamily.family.$invalid">
            <small class="form-text text-danger" v-if="!$v.subFamily.family.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
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
            :disabled="$v.subFamily.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./sub-family-update.component.ts"></script>
