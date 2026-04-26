# 🏦 Console Bank

A simple Java console-based banking application that simulates core banking operations through a menu-driven command-line interface. Built with clean layered architecture — no frameworks, no database, just pure Java.

---

## Features

- **Open Account** — Create a new customer profile and bank account (Savings or Current) with an optional initial deposit
- **Deposit** — Add funds to any existing account
- **Withdraw** — Withdraw funds with insufficient balance protection
- **Transfer** — Move funds between two different accounts
- **Account Statement** — View a timestamped transaction history for any account
- **List Accounts** — Display all accounts sorted by account number
- **Search by Customer Name** — Find accounts associated with a customer name

---

## Project Structure

```
├── app/
│   └── main.java                        # Entry point & CLI menu handler
├── domain/
│   ├── Account.java                     # Account entity
│   ├── Customer.java                    # Customer entity
│   ├── Transaction.java                 # Transaction entity
│   └── Type.java                        # Transaction type enum (DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT)
├── repository/
│   ├── AccountRepository.java           # In-memory account storage
│   ├── CustomerRepository.java          # In-memory customer storage
│   └── TransactionRepository.java       # In-memory transaction storage
├── service/
│   ├── BankService.java                 # Service interface
│   └── impl/
│       └── BankServiceImpl.java         # Business logic implementation
└── exceptions/
    ├── AccountNotFoundException.java    # Thrown when account lookup fails
    └── InsufficientFundsException.java  # Thrown when balance is too low
```

---

## Getting Started

### Prerequisites

- Java 17 or higher (uses text blocks and switch expressions)
- No external dependencies required

### Compile

From the project root directory:

```bash
javac -d out app/main.java domain/*.java repository/*.java service/BankService.java service/impl/BankServiceImpl.java exceptions/*.java
```

### Run

```bash
java -cp out app.main
```

---

## Usage

On launch, you'll see the main menu:

```
Welcome to Console Bank

    1. Open Account
    2. Deposit
    3. Withdraw
    4. Transfer
    5. Account Statement
    6. List Accounts
    7. Search by Customer name
    0. Exit

CHOOSE:
```

Enter the number corresponding to the action you want to perform and follow the prompts.

### Example — Opening an Account

```
CHOOSE: 1
Customer name:
Jane Doe
Customer email:
jane@example.com
Account type (SAVINGS/CURRENT):
SAVINGS
Initial deposit (optional, blank for 0):
500
Account opened: AC000001
```

### Account Number Format

Accounts are automatically assigned sequential numbers in the format `AC000001`, `AC000002`, etc.

---

## Architecture

The application follows a layered architecture:

| Layer | Responsibility |
|---|---|
| `app` | CLI interaction and user input handling |
| `service` | Business logic and validation |
| `repository` | In-memory data storage (acts as a mock database) |
| `domain` | Plain Java objects (entities) |
| `exceptions` | Custom runtime exceptions |

> **Note:** All data is stored in-memory and is lost when the application exits. There is no persistent storage or database.

---

## Known Limitations

- Data is not persisted between sessions
- Account numbers are sequential and not collision-safe if accounts are deleted
- A bug exists in `transfer()`: the recipient's balance is incorrectly calculated using the sender's post-deduction balance. The `toTransaction` is also not saved to the repository.
- No input validation for email format or negative amounts

---

## License

This project is open source and available under the [MIT License](LICENSE).
