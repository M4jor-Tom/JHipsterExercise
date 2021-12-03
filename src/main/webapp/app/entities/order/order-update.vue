<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jHipsterExerciseApp.order.home.createOrEditLabel"
          data-cy="OrderCreateUpdateHeading"
          v-text="$t('jHipsterExerciseApp.order.home.createOrEditLabel')"
        >
          Create or edit a Order
        </h2>
        <div>
          <div class="form-group" v-if="order.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="order.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.sum')" for="order-sum">Sum</label>
            <input
              type="number"
              class="form-control"
              name="sum"
              id="order-sum"
              data-cy="sum"
              :class="{ valid: !$v.order.sum.$invalid, invalid: $v.order.sum.$invalid }"
              v-model.number="$v.order.sum.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.deliveryAdress')" for="order-deliveryAdress"
              >Delivery Adress</label
            >
            <input
              type="text"
              class="form-control"
              name="deliveryAdress"
              id="order-deliveryAdress"
              data-cy="deliveryAdress"
              :class="{ valid: !$v.order.deliveryAdress.$invalid, invalid: $v.order.deliveryAdress.$invalid }"
              v-model="$v.order.deliveryAdress.$model"
              required
            />
            <div v-if="$v.order.deliveryAdress.$anyDirty && $v.order.deliveryAdress.$invalid">
              <small class="form-text text-danger" v-if="!$v.order.deliveryAdress.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.deliveryDateTime')" for="order-deliveryDateTime"
              >Delivery Date Time</label
            >
            <div class="d-flex">
              <input
                id="order-deliveryDateTime"
                data-cy="deliveryDateTime"
                type="datetime-local"
                class="form-control"
                name="deliveryDateTime"
                :class="{ valid: !$v.order.deliveryDateTime.$invalid, invalid: $v.order.deliveryDateTime.$invalid }"
                required
                :value="convertDateTimeFromServer($v.order.deliveryDateTime.$model)"
                @change="updateZonedDateTimeField('deliveryDateTime', $event)"
              />
            </div>
            <div v-if="$v.order.deliveryDateTime.$anyDirty && $v.order.deliveryDateTime.$invalid">
              <small class="form-text text-danger" v-if="!$v.order.deliveryDateTime.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.order.deliveryDateTime.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.quantity')" for="order-quantity">Quantity</label>
            <input
              type="number"
              class="form-control"
              name="quantity"
              id="order-quantity"
              data-cy="quantity"
              :class="{ valid: !$v.order.quantity.$invalid, invalid: $v.order.quantity.$invalid }"
              v-model.number="$v.order.quantity.$model"
              required
            />
            <div v-if="$v.order.quantity.$anyDirty && $v.order.quantity.$invalid">
              <small class="form-text text-danger" v-if="!$v.order.quantity.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.order.quantity.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.billingMethod')" for="order-billingMethod"
              >Billing Method</label
            >
            <select
              class="form-control"
              name="billingMethod"
              :class="{ valid: !$v.order.billingMethod.$invalid, invalid: $v.order.billingMethod.$invalid }"
              v-model="$v.order.billingMethod.$model"
              id="order-billingMethod"
              data-cy="billingMethod"
              required
            >
              <option
                v-for="billingMethod in billingMethodValues"
                :key="billingMethod"
                v-bind:value="billingMethod"
                v-bind:label="$t('jHipsterExerciseApp.BillingMethod.' + billingMethod)"
              >
                {{ billingMethod }}
              </option>
            </select>
            <div v-if="$v.order.billingMethod.$anyDirty && $v.order.billingMethod.$invalid">
              <small class="form-text text-danger" v-if="!$v.order.billingMethod.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.orderState')" for="order-orderState">Order State</label>
            <select
              class="form-control"
              name="orderState"
              :class="{ valid: !$v.order.orderState.$invalid, invalid: $v.order.orderState.$invalid }"
              v-model="$v.order.orderState.$model"
              id="order-orderState"
              data-cy="orderState"
              required
            >
              <option
                v-for="orderState in orderStateValues"
                :key="orderState"
                v-bind:value="orderState"
                v-bind:label="$t('jHipsterExerciseApp.OrderState.' + orderState)"
              >
                {{ orderState }}
              </option>
            </select>
            <div v-if="$v.order.orderState.$anyDirty && $v.order.orderState.$invalid">
              <small class="form-text text-danger" v-if="!$v.order.orderState.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="$t('jHipsterExerciseApp.order.products')" for="order-products">Products</label>
            <select
              class="form-control"
              id="order-products"
              data-cy="products"
              multiple
              name="products"
              v-if="order.products !== undefined"
              v-model="order.products"
            >
              <option v-bind:value="getSelected(order.products, productOption)" v-for="productOption in products" :key="productOption.id">
                {{ productOption.modelName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('jHipsterExerciseApp.order.client')" for="order-client">Client</label>
            <select class="form-control" id="order-client" data-cy="client" name="client" v-model="order.client">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="order.client && clientOption.id === order.client.id ? order.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.id }}
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
            :disabled="$v.order.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./order-update.component.ts"></script>
