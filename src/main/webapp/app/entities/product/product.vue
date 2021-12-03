<template>
  <div>
    <h2 id="page-heading" data-cy="ProductHeading">
      <span v-text="$t('jHipsterExerciseApp.product.home.title')" id="product-heading">Products</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('jHipsterExerciseApp.product.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProductCreate' }" custom v-slot="{ navigate }">
          <button
            v-if="(hasAdminAuthority('ROLE_ADMIN') || hasSellerAuthority('ROLE_SELLER')) && authenticated"
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-product"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('jHipsterExerciseApp.product.home.createLabel')"> Create a new Product </span>
          </button>
        </router-link>
      </div>
      <span v-text="$t('Filter')">Filter</span> <input type="text" v-model="filtered" class="form-control" />
      
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && products && products.length === 0">
      <span v-text="$t('jHipsterExerciseApp.product.home.notFound')">No products found</span>
    </div>
    <div class="table-responsive" v-if="products && products.length > 0">
      <table class="table table-striped" aria-describedby="products">
        <thead>
          <tr>
            <th scope="col" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('description')">
              <span v-text="$t('description')">description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('photoId')">
              <span v-text="$t('photoId')">photoId</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'photoId'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('stock')">
              <span v-text="$t('stock')">stock</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stock'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('price')">
              <span v-text="$t('price')">price</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('modelName')">
              <span v-text="$t('modelName')">modelName</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'modelName'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('color')">
              <span v-text="$t('color')">color</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'color'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('subFamily')">
              <span v-text="$t('subFamily')">subFamily</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subFamily'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('brand')">
              <span v-text="$t('brand')">brand</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'brand'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('seller')">
              <span v-text="$t('seller')">seller</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'seller'"></jhi-sort-indicator>
            </th>
            <th scope="col" v-on:click="changeOrder('tags')">
              <span v-text="$t('tags')">tags</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tags'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in orderBy(filterBy(products, filtered), propOrder, reverse === true ? 1 : -1)" :key="product.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">{{ product.id }}</router-link>
            </td>
            <td>{{ product.description }}</td>
            <td>{{ product.photoId }}</td>
            <td>{{ product.stock }}</td>
            <td>{{ product.price }}</td>
            <td>{{ product.modelName }}</td>
            <td v-text="$t('jHipsterExerciseApp.Color.' + product.color)">{{ product.color }}</td>
            <td>
              <div v-if="product.subFamily">
                <router-link :to="{ name: 'SubFamilyView', params: { subFamilyId: product.subFamily.id } }">{{
                  product.subFamily.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="product.brand">
                <router-link :to="{ name: 'BrandView', params: { brandId: product.brand.id } }">{{ product.brand.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="product.seller">
                <router-link :to="{ name: 'SellerView', params: { sellerId: product.seller.id } }">{{ product.seller.email }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(tags, i) in product.tags" :key="tags.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'TagView', params: { tagId: tags.id } }">{{ tags.name }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProductView', params: { productId: product.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
                  <button
                    v-if="(hasAdminAuthority('ROLE_ADMIN') || hasSellerAuthority('ROLE_SELLER')) && authenticated"
                    @click="navigate"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="(hasAdminAuthority('ROLE_ADMIN') || hasSellerAuthority('ROLE_SELLER')) && authenticated"
                  v-on:click="prepareRemove(product)"
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
        ><span id="jHipsterExerciseApp.product.delete.question" data-cy="productDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-product-heading" v-text="$t('jHipsterExerciseApp.product.delete.question', { id: removeId })">
          Are you sure you want to delete this Product?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-product"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProduct()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./product.component.ts"></script>
