import {Customer} from './customer.model';

export interface Account {
  type:          string;
  id:            string;
  balance:       number;
  createdAt:     Date;
  status:        string;
  customerDTO:   Customer;
  overDraft?:    number;
  interestRate?: number;
}
