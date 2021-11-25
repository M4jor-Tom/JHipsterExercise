<template>
  <div>
    <h2 id="page-heading" data-cy="SubFamilyHeading">
      <span v-text="$t('jHipsterExerciseApp.subFamily.home.title')" id="sub-family-heading">Sub Families</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jHipsterExerciseApp.subFamily.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'SubFamilyCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-sub-family"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jHipsterExerciseApp.subFamily.home.createLabel')"> Create a new Sub Family </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && subFamilies && subFamilies.length === 0">
      <span v-text="$t('jHipsterExerciseApp.subFamily.home.notFound')">No subFamilies found</span>
    </div>
    <div class="table-responsive" v-if="subFamilies && subFamilies.length > 0">
      <table class="table table-striped" aria-describedby="subFamilies">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.subFamily.name')">Name</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.subFamily.family')">Family</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subFamily in subFamilies" :key="subFamily.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubFamilyView', params: { subFamilyId: subFamily.id } }">{{ subFamily.id }}</router-link>
            </td>
            <td>{{ subFamily.name }}</td>
            <td>
              <div v-if="subFamily.family">
                <router-link :to="{ name: 'FamilyView', params: { familyId: subFamily.family.id } }">{{ subFamily.family.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'SubFamilyView', params: { subFamilyId: subFamily.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SubFamilyEdit', params: { subFamilyId: subFamily.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(subFamily)"
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
        ><span id="jHipsterExerciseApp.subFamily.delete.question" data-cy="subFamilyDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-subFamily-heading" v-text="$t('jHipsterExerciseApp.subFamily.delete.question', { id: removeId })">
          Are you sure you want to delete this Sub Family?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-subFamily"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeSubFamily()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./sub-family.component.ts"></script>
