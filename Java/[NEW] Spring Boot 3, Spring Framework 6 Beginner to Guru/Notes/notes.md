### Integration testing:

---

- Changes made to database via `TestRestTemplate` are not rolled back because they are done in a separate transaction, outside the test transaction. This is because the `TestRestTemplate` sends an actual HTTP request tp the running server.

  This means the controller handles the request in a separate transaction from the test method. Since the transaction is committed after the request completes, the changes persist outside the test transaction.
