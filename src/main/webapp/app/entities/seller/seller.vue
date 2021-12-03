<template>
  <div>
    <h2 id="page-heading" data-cy="SellerHeading">
      <span v-text="$t('jHipsterExerciseApp.seller.home.title')" id="seller-heading">Sellers</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jHipsterExerciseApp.seller.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'SellerCreate' }" custom v-slot="{ navigate }">
          <button
            v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-seller"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jHipsterExerciseApp.seller.home.createLabel')"> Create a new Seller </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && sellers && sellers.length === 0">
      <span v-text="$t('jHipsterExerciseApp.seller.home.notFound')">No sellers found</span>
    </div>
    <div class="table-responsive" v-if="sellers && sellers.length > 0">
      <table class="table table-striped" aria-describedby="sellers">
        <thead>
          <tr>
          <th scope="col" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
          </th>
          <th scope="col" v-on:click="changeOrder('socialReason')">
              <span v-text="$t('SocialReason')">SocialReason</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'socialReason'"></jhi-sort-indicator>
          </th>
          <th scope="col" v-on:click="changeOrder('address')">
              <span v-text="$t('Address')">Address</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'address'"></jhi-sort-indicator>
          </th>
          <th scope="col" v-on:click="changeOrder('siretNumber')">
              <span v-text="$t('SiretNumber')">SiretNumber</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'siretNumber'"></jhi-sort-indicator>
          </th>
          <th scope="col" v-on:click="changeOrder('phone')">
              <span v-text="$t('Phone')">Phone</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
          </th>
          <th scope="col" v-on:click="changeOrder('email')">
              <span v-text="$t('Email')">Email</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
          </th>
          <th scope="col" v-on:click="changeOrder('connection')">
              <span v-text="$t('Connection')">Connection</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'connection'"></jhi-sort-indicator>
          </th>
          
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="seller in sellers" :key="seller.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SellerView', params: { sellerId: seller.id } }">{{ seller.id }}</router-link>
            </td>
            <td>{{ seller.socialReason }}</td>
            <td>{{ seller.address }}</td>
            <td>{{ seller.siretNumber }}</td>
            <td>{{ seller.phone }}</td>
            <td>{{ seller.email }}</td>
            <td>
              {{ seller.user ? seller.user.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'SellerView', params: { sellerId: seller.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SellerEdit', params: { sellerId: seller.id } }" custom v-slot="{ navigate }">
                  <button
                    @click="navigate"
                    v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
                  v-on:click="prepareRemove(seller)"
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
        ><span id="jHipsterExerciseApp.seller.delete.question" data-cy="sellerDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-seller-heading" v-text="$t('jHipsterExerciseApp.seller.delete.question', { id: removeId })">
          Are you sure you want to delete this Seller?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-seller"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeSeller()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./seller.component.ts"></script>
