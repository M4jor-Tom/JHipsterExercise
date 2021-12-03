/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import OrderService from '@/entities/order/order.service';
import { Order } from '@/shared/model/order.model';
import { BillingMethod } from '@/shared/model/enumerations/billing-method.model';
import { OrderState } from '@/shared/model/enumerations/order-state.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Order Service', () => {
    let service: OrderService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new OrderService();
      currentDate = new Date();
      elemDefault = new Order(123, 0, 'AAAAAAA', currentDate, 0, BillingMethod.PAYPAL, OrderState.PROCESSING);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            deliveryDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Order', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            deliveryDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            deliveryDateTime: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Order', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Order', async () => {
        const returnedFromService = Object.assign(
          {
            sum: 1,
            deliveryAdress: 'BBBBBB',
            deliveryDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            quantity: 1,
            billingMethod: 'BBBBBB',
            orderState: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            deliveryDateTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Order', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Order', async () => {
        const patchObject = Object.assign(
          {
            sum: 1,
            deliveryDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            billingMethod: 'BBBBBB',
          },
          new Order()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            deliveryDateTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Order', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Order', async () => {
        const returnedFromService = Object.assign(
          {
            sum: 1,
            deliveryAdress: 'BBBBBB',
            deliveryDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            quantity: 1,
            billingMethod: 'BBBBBB',
            orderState: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            deliveryDateTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Order', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Order', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Order', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
