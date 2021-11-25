<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.product.home.createOrEditLabel"
          data-cy="ProductCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.product.home.createOrEditLabel')"
        >
          Create or edit a Product
        </h2>
        <div>
          <div class="form-group" v-if="product.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="product.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.name')" for="product-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="product-name"
              data-cy="name"
              :class="{ valid: !$v.product.name.$invalid, invalid: $v.product.name.$invalid }"
              v-model="$v.product.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.description')" for="product-description"
              >Description</label
            >
            <input
              type="text"
              class="form-control"
              name="description"
              id="product-description"
              data-cy="description"
              :class="{ valid: !$v.product.description.$invalid, invalid: $v.product.description.$invalid }"
              v-model="$v.product.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.photoId')" for="product-photoId">Photo Id</label>
            <input
              type="number"
              class="form-control"
              name="photoId"
              id="product-photoId"
              data-cy="photoId"
              :class="{ valid: !$v.product.photoId.$invalid, invalid: $v.product.photoId.$invalid }"
              v-model.number="$v.product.photoId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.stock')" for="product-stock">Stock</label>
            <input
              type="number"
              class="form-control"
              name="stock"
              id="product-stock"
              data-cy="stock"
              :class="{ valid: !$v.product.stock.$invalid, invalid: $v.product.stock.$invalid }"
              v-model.number="$v.product.stock.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.tag')" for="product-tag">Tag</label>
            <input
              type="text"
              class="form-control"
              name="tag"
              id="product-tag"
              data-cy="tag"
              :class="{ valid: !$v.product.tag.$invalid, invalid: $v.product.tag.$invalid }"
              v-model="$v.product.tag.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.tariff')" for="product-tariff">Tariff</label>
            <input
              type="number"
              class="form-control"
              name="tariff"
              id="product-tariff"
              data-cy="tariff"
              :class="{ valid: !$v.product.tariff.$invalid, invalid: $v.product.tariff.$invalid }"
              v-model.number="$v.product.tariff.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.color')" for="product-color">Color</label>
            <select
              class="form-control"
              name="color"
              :class="{ valid: !$v.product.color.$invalid, invalid: $v.product.color.$invalid }"
              v-model="$v.product.color.$model"
              id="product-color"
              data-cy="color"
            >
              <option
                v-for="color in colorValues"
                :key="color"
                v-bind:value="color"
                v-bind:label="$t('jHipsterExerciseApp.Color.' + color)"
              >
                {{ color }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.model')" for="product-model">Model</label>
            <select class="form-control" id="product-model" data-cy="model" name="model" v-model="product.model">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="product.model && modelOption.id === product.model.id ? product.model : modelOption"
                v-for="modelOption in models"
                :key="modelOption.id"
              >
                {{ modelOption.id }}
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
            :disabled="$v.product.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./product-update.component.ts"></script>
