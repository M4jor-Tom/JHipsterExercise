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
              required
            />
            <div v-if="$v.product.stock.$anyDirty && $v.product.stock.$invalid">
              <small class="form-text text-danger" v-if="!$v.product.stock.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.product.stock.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.price')" for="product-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="product-price"
              data-cy="price"
              :class="{ valid: !$v.product.price.$invalid, invalid: $v.product.price.$invalid }"
              v-model.number="$v.product.price.$model"
              required
            />
            <div v-if="$v.product.price.$anyDirty && $v.product.price.$invalid">
              <small class="form-text text-danger" v-if="!$v.product.price.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.product.price.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.modelName')" for="product-modelName"
              >Model Name</label
            >
            <input
              type="text"
              class="form-control"
              name="modelName"
              id="product-modelName"
              data-cy="modelName"
              :class="{ valid: !$v.product.modelName.$invalid, invalid: $v.product.modelName.$invalid }"
              v-model="$v.product.modelName.$model"
              required
            />
            <div v-if="$v.product.modelName.$anyDirty && $v.product.modelName.$invalid">
              <small class="form-text text-danger" v-if="!$v.product.modelName.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
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
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.subFamily')" for="product-subFamily"
              >Sub Family</label
            >
            <select class="form-control" id="product-subFamily" data-cy="subFamily" name="subFamily" v-model="product.subFamily" required>
              <option v-if="!product.subFamily" v-bind:value="null" selected></option>
              <option
                v-bind:value="product.subFamily && subFamilyOption.id === product.subFamily.id ? product.subFamily : subFamilyOption"
                v-for="subFamilyOption in subFamilies"
                :key="subFamilyOption.id"
              >
                {{ subFamilyOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.product.subFamily.$anyDirty && $v.product.subFamily.$invalid">
            <small class="form-text text-danger" v-if="!$v.product.subFamily.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.brand')" for="product-brand">Brand</label>
            <select class="form-control" id="product-brand" data-cy="brand" name="brand" v-model="product.brand" required>
              <option v-if="!product.brand" v-bind:value="null" selected></option>
              <option
                v-bind:value="product.brand && brandOption.id === product.brand.id ? product.brand : brandOption"
                v-for="brandOption in brands"
                :key="brandOption.id"
              >
                {{ brandOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.product.brand.$anyDirty && $v.product.brand.$invalid">
            <small class="form-text text-danger" v-if="!$v.product.brand.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.product.seller')" for="product-seller">Seller</label>
            <select class="form-control" id="product-seller" data-cy="seller" name="seller" v-model="product.seller" required>
              <option v-if="!product.seller" v-bind:value="null" selected></option>
              <option
                v-bind:value="product.seller && sellerOption.id === product.seller.id ? product.seller : sellerOption"
                v-for="sellerOption in sellers"
                :key="sellerOption.id"
              >
                {{ sellerOption.email }}
              </option>
            </select>
          </div>
          <div v-if="$v.product.seller.$anyDirty && $v.product.seller.$invalid">
            <small class="form-text text-danger" v-if="!$v.product.seller.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label v-text="$t('jHipsterExerciseApp.product.tags')" for="product-tags">Tags</label>
            <select
              class="form-control"
              id="product-tags"
              data-cy="tags"
              multiple
              name="tags"
              v-if="product.tags !== undefined"
              v-model="product.tags"
            >
              <option v-bind:value="getSelected(product.tags, tagOption)" v-for="tagOption in tags" :key="tagOption.id">
                {{ tagOption.name }}
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
