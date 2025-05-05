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
  operationDate: Date;
  amount:        number;
  type:          string;
  description:   string;
}
