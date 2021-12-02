<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.seller.home.createOrEditLabel"
          data-cy="SellerCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.seller.home.createOrEditLabel')"
        >
          Create or edit a Seller
        </h2>
        <div>
          <div class="form-group" v-if="seller.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="seller.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.seller.socialReason')" for="seller-socialReason"
              >Social Reason</label
            >
            <input
              type="text"
              class="form-control"
              name="socialReason"
              id="seller-socialReason"
              data-cy="socialReason"
              :class="{ valid: !$v.seller.socialReason.$invalid, invalid: $v.seller.socialReason.$invalid }"
              v-model="$v.seller.socialReason.$model"
              required
            />
            <div v-if="$v.seller.socialReason.$anyDirty && $v.seller.socialReason.$invalid">
              <small class="form-text text-danger" v-if="!$v.seller.socialReason.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.seller.address')" for="seller-address">Address</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="seller-address"
              data-cy="address"
              :class="{ valid: !$v.seller.address.$invalid, invalid: $v.seller.address.$invalid }"
              v-model="$v.seller.address.$model"
              required
            />
            <div v-if="$v.seller.address.$anyDirty && $v.seller.address.$invalid">
              <small class="form-text text-danger" v-if="!$v.seller.address.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.seller.siretNumber')" for="seller-siretNumber"
              >Siret Number</label
            >
            <input
              type="text"
              class="form-control"
              name="siretNumber"
              id="seller-siretNumber"
              data-cy="siretNumber"
              :class="{ valid: !$v.seller.siretNumber.$invalid, invalid: $v.seller.siretNumber.$invalid }"
              v-model="$v.seller.siretNumber.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.seller.phone')" for="seller-phone">Phone</label>
            <input
              type="number"
              class="form-control"
              name="phone"
              id="seller-phone"
              data-cy="phone"
              :class="{ valid: !$v.seller.phone.$invalid, invalid: $v.seller.phone.$invalid }"
              v-model.number="$v.seller.phone.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.seller.email')" for="seller-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="seller-email"
              data-cy="email"
              :class="{ valid: !$v.seller.email.$invalid, invalid: $v.seller.email.$invalid }"
              v-model="$v.seller.email.$model"
              required
            />
            <div v-if="$v.seller.email.$anyDirty && $v.seller.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.seller.email.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.seller.connection')" for="seller-connection"
              >Connection</label
            >
            <select class="form-control" id="seller-connection" data-cy="connection" name="connection" v-model="seller.connection">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="seller.connection && connectionOption.id === seller.connection.id ? seller.connection : connectionOption"
                v-for="connectionOption in connections"
                :key="connectionOption.id"
              >
                {{ connectionOption.id }}
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
            :disabled="$v.seller.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./seller-update.component.ts"></script>
