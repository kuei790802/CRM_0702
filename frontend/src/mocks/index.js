import  { newsHandlers }  from './modules/news';
import { contactHandler } from './modules/contact'; 
import { productHandlers } from './modules/product';
import { loginHandlers } from './modules/login';

export const handlers = [
  ...newsHandlers,
  ...contactHandler,
  ...productHandlers,
  ...loginHandlers,

];