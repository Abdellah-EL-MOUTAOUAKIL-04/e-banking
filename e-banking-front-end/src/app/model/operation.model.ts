import {Account} from './account.model';

export interface Operation {
  accountId:         string;
  balance:           number;
  totalPages:        number;
  pageSize:          number;
  accountOperations: AccountOperation[];
  currentPage:       number;
}

export interface AccountOperation {
  id:            number;
  bankAccountDTO:Account;
  operationDate: Date;
  amount:        number;
  type:          string;
  description:   string;
}
