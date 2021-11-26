<template>
  <div>
    <h2 id="page-heading" data-cy="ConnectionHeading">
      <span v-text="$t('jHipsterExerciseApp.connection.home.title')" id="connection-heading">Connections</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jHipsterExerciseApp.connection.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ConnectionCreate' }" custom v-slot="{ navigate }">
          <button
            v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-connection"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jHipsterExerciseApp.connection.home.createLabel')"> Create a new Connection </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && connections && connections.length === 0">
      <span v-text="$t('jHipsterExerciseApp.connection.home.notFound')">No connections found</span>
    </div>
    <div class="table-responsive" v-if="connections && connections.length > 0">
      <table class="table table-striped" aria-describedby="connections">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.connection.username')">Username</span></th>
            <th scope="row"><span v-text="$t('jHipsterExerciseApp.connection.password')">Password</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="connection in connections" :key="connection.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ConnectionView', params: { connectionId: connection.id } }">{{ connection.id }}</router-link>
            </td>
            <td>{{ connection.username }}</td>
            <td>{{ connection.password }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ConnectionView', params: { connectionId: connection.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ConnectionEdit', params: { connectionId: connection.id } }" custom v-slot="{ navigate }">
                  <button
                    v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
                    @click="navigate"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
                  v-on:click="prepareRemove(connection)"
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
        ><span
          id="jHipsterExerciseApp.connection.delete.question"
          data-cy="connectionDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-connection-heading" v-text="$t('jHipsterExerciseApp.connection.delete.question', { id: removeId })">
          Are you sure you want to delete this Connection?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-connection"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeConnection()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./connection.component.ts"></script>
