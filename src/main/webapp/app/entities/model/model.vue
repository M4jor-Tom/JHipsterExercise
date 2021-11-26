<template>
  <div>
    <h2 id="page-heading" data-cy="ModelHeading">
      <span v-text="$t('jHipsterExerciseApp.model.home.title')" id="model-heading">Models</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jHipsterExerciseApp.model.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ModelCreate' }" custom v-slot="{ navigate }">
          <button
            v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-model"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jHipsterExerciseApp.model.home.createLabel')"> Create a new Model </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && models && models.length === 0">
      <span v-text="$t('jHipsterExerciseApp.model.home.notFound')">No models found</span>
    </div>
    <div class="table-responsive" v-if="models && models.length > 0">
      <table class="table table-striped" aria-describedby="models">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.model.name')">Name</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.model.brand')">Brand</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.model.family')">Family</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="model in models" :key="model.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ModelView', params: { modelId: model.id } }">{{ model.id }}</router-link>
            </td>
            <td>{{ model.name }}</td>
            <td>
              <div v-if="model.brand">
                <router-link :to="{ name: 'BrandView', params: { brandId: model.brand.id } }">{{ model.brand.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="model.family">
                <router-link :to="{ name: 'FamilyView', params: { familyId: model.family.id } }">{{ model.family.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ModelView', params: { modelId: model.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ModelEdit', params: { modelId: model.id } }" custom v-slot="{ navigate }">
                  <button v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
                  v-on:click="prepareRemove(model)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="jHipsterExerciseApp.model.delete.question" data-cy="modelDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-model-heading" v-text="$t('jHipsterExerciseApp.model.delete.question', { id: removeId })">
          Are you sure you want to delete this Model?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-model"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeModel()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./model.component.ts"></script>
