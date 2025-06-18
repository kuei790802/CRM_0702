import  { newsHandlers }  from './modules/news';
import { contactHandler } from './modules/contact'; 
import { productHandlers } from './modules/product';
import { loginHandlers } from './modules/login';
import { crmCustomerHandlers } from './modules/crmCustomer';
import { crmOpportunitiesHandlers } from './modules/crmOpportunities';
import { crmcalendarEventsHandler } from './modules/crmcalendarEventsHandler';

export const handlers = [
  ...newsHandlers,
  ...contactHandler,
  ...productHandlers,
  ...loginHandlers,
  ...crmCustomerHandlers,
  ...crmOpportunitiesHandlers,
  ...crmcalendarEventsHandler,

];