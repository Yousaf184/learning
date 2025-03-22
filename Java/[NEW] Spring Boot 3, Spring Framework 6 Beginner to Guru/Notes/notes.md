## Integration testing:

- Changes made to database via `TestRestTemplate` are not rolled back because they are done in a separate transaction, outside the test transaction. This is because the `TestRestTemplate` sends an actual HTTP request tp the running server.

  This means the controller handles the request in a separate transaction from the test method. Since the transaction is committed after the request completes, the changes persist outside the test transaction.

## Transactions

A transaction is a sequence of operations performed as a single, indivisible unit of work in a database. It ensures that a series of operations (e.g., updates, inserts, deletes) are completed successfully or not at all, maintaining the integrity and consistency of the database.

In relational database systems, transactions are essential for ensuring that the ACID (Atomicity, Consistency, Isolation, Durability) properties are maintained. These properties are as follows:

- **Atomicity:** A transaction is atomic, meaning it either completes entirely or does not execute at all. If a transaction fails at any point, the database is rolled back to the state it was in before the transaction started.

- **Consistency:** The database must transition from one valid state to another. After a transaction, the database must follow all predefined rules, such as constraints, triggers, and foreign keys.
- **Isolation:** Transactions are isolated from each other. The operations within a transaction are not visible to other transactions until the transaction is committed.

- **Durability:** Once a transaction is committed, its changes are permanent and will survive system crashes or failures.

### How Transactions Work Internally

Internally, transactions are managed by the transaction manager in the database system. The transaction manager is responsible for ensuring that the ACID properties are honored. The process works as follows:

1. Begin Transaction: When a transaction starts, the database creates a new context to track the operations of that transaction. This includes locking rows, pages, or tables that the transaction will modify, ensuring that no other transaction can interfere with the current transaction.

1. Execution of Queries: During the transaction, the database executes SQL statements that modify data. However, these changes are not visible to other transactions until the transaction is committed.

1. Commit: Once all the operations within a transaction are successfully completed, the transaction is committed. At this point, the changes become permanent and visible to other transactions.

1. Rollback: If a failure occurs, or if the transaction is explicitly rolled back, all changes made during the transaction are undone, and the database is restored to its state before the transaction began.

### Locking Mechanisms

Locking mechanisms are a critical part of how transactions are managed. They prevent multiple transactions from modifying the same data concurrently, which could lead to inconsistency.

- **Pessimistic Locking:** In pessimistic locking, a transaction locks the data it is working on to prevent other transactions from modifying it until the first transaction is completed. There are several types of pessimistic locks:

  - **Shared Lock:** Allows other transactions to read the data but not modify it.

  - **Exclusive Lock:** Prevents any other transaction from reading or modifying the data.

- **Optimistic Locking:** Optimistic locking assumes that conflicts are rare. Instead of locking data, it allows transactions to proceed with the assumption that no other transaction will modify the same data. Before committing, the transaction checks if the data was changed by another transaction. If it was, the transaction is rolled back and the user is notified.

  Optimistic locking is often implemented using versioning, where each row in the database has a version number or timestamp. When a transaction reads data, it also retrieves the version number. When it tries to update the data, it checks if the version number has changed. If it has, the update is rejected.

- **Isolation Levels:** The database allows different levels of isolation between transactions. These levels determine how the changes made by one transaction are visible to other transactions and how conflicts are handled.

  - **Read Uncommitted:** Transactions can read uncommitted changes made by other transactions. This can lead to dirty reads (reading data that may later be rolled back).

  - **Read Committed:** Transactions can only read committed data. However, non-repeatable reads (reading different values in the same transaction) can still occur.

  - **Repeatable Read:** Ensures that if a transaction reads a value, it will always read the same value within the transaction, preventing non-repeatable reads. However, phantom reads (inserts or deletes by other transactions that would affect the query results) can still occur.

  - **Serializable:** The highest level of isolation, where transactions are executed in such a way that the results are as if they were run one after the other, not concurrently. This prevents dirty reads, non-repeatable reads, and phantom reads.

  [Isolation Levels - PostgreSQL Docs](https://www.postgresql.org/docs/current/transaction-iso.html)

### Understanding @Transactiona annotation

[Spring Transaction Management: @Transactional In-Depth - Marco Behler](https://www.marcobehler.com/guides/spring-transaction-management-transactional-in-depth)
