import axios, { AxiosPromise } from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import { Product } from '@/shared/model/product.model';

export default class ListProduitService {

	public get(productId: number): Promise<any> {
	return axios.get(`api/products/${productId}`);
	}
	
	public update(product: Product): Promise<any> {
    return axios.put('api/products', product);
	  }
	  
	public retrieve(req?: any): Promise<any> {
    return axios.get(`api/products?${buildPaginationQueryOpts(req)}`);
    }

	

	
}