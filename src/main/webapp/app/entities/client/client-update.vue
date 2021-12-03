<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.client.home.createOrEditLabel"
          data-cy="ClientCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.client.home.createOrEditLabel')"
        >
          Create or edit a Client
        </h2>
        <div>
          <div class="form-group" v-if="client.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="client.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.addedDateTime')" for="client-addedDateTime"
              >Added Date Time</label
            >
            <div class="d-flex">
              <input
                id="client-addedDateTime"
                data-cy="addedDateTime"
                type="datetime-local"
                class="form-control"
                name="addedDateTime"
                :class="{ valid: !$v.client.addedDateTime.$invalid, invalid: $v.client.addedDateTime.$invalid }"
                required
                :value="convertDateTimeFromServer($v.client.addedDateTime.$model)"
                @change="updateZonedDateTimeField('addedDateTime', $event)"
              />
            </div>
            <div v-if="$v.client.addedDateTime.$anyDirty && $v.client.addedDateTime.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.addedDateTime.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.client.addedDateTime.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.lastName')" for="client-lastName">Last Name</label>
            <input
              type="text"
              class="form-control"
              name="lastName"
              id="client-lastName"
              data-cy="lastName"
              :class="{ valid: !$v.client.lastName.$invalid, invalid: $v.client.lastName.$invalid }"
              v-model="$v.client.lastName.$model"
              required
            />
            <div v-if="$v.client.lastName.$anyDirty && $v.client.lastName.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.lastName.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.firstName')" for="client-firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              name="firstName"
              id="client-firstName"
              data-cy="firstName"
              :class="{ valid: !$v.client.firstName.$invalid, invalid: $v.client.firstName.$invalid }"
              v-model="$v.client.firstName.$model"
              required
            />
            <div v-if="$v.client.firstName.$anyDirty && $v.client.firstName.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.firstName.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.email')" for="client-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="client-email"
              data-cy="email"
              :class="{ valid: !$v.client.email.$invalid, invalid: $v.client.email.$invalid }"
              v-model="$v.client.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.phone')" for="client-phone">Phone</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="client-phone"
              data-cy="phone"
              :class="{ valid: !$v.client.phone.$invalid, invalid: $v.client.phone.$invalid }"
              v-model="$v.client.phone.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.adress')" for="client-adress">Adress</label>
            <input
              type="text"
              class="form-control"
              name="adress"
              id="client-adress"
              data-cy="adress"
              :class="{ valid: !$v.client.adress.$invalid, invalid: $v.client.adress.$invalid }"
              v-model="$v.client.adress.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.country')" for="client-country">Country</label>
            <input
              type="text"
              class="form-control"
              name="country"
              id="client-country"
              data-cy="country"
              :class="{ valid: !$v.client.country.$invalid, invalid: $v.client.country.$invalid }"
              v-model="$v.client.country.$model"
              required
            />
            <div v-if="$v.client.country.$anyDirty && $v.client.country.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.country.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.postalCode')" for="client-postalCode"
              >Postal Code</label
            >
            <input
              type="text"
              class="form-control"
              name="postalCode"
              id="client-postalCode"
              data-cy="postalCode"
              :class="{ valid: !$v.client.postalCode.$invalid, invalid: $v.client.postalCode.$invalid }"
              v-model="$v.client.postalCode.$model"
              required
            />
            <div v-if="$v.client.postalCode.$anyDirty && $v.client.postalCode.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.postalCode.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.client.user')" for="client-user">User</label>
            <select class="form-control" id="client-user" data-cy="user" name="user" v-model="client.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="client.user && userOption.id === client.user.id ? client.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
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
            :disabled="$v.client.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./client-update.component.ts"></script>
