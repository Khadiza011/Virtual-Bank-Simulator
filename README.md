# Virtual Bank Simulator

A beginner-friendly **Java OOP + Java Swing** desktop application that simulates basic banking operations. This project is organized for learning, running in IntelliJ IDEA, and uploading to GitHub.

> **Important:** This is an educational demo only. It is not connected to a real bank and must not be used to store real financial information.

## Features

- Login screen
- Account dashboard and balance display
- Deposit, withdraw, and transfer actions
- Transaction history
- Validation for invalid amounts and insufficient balance
- Transaction data saved between app launches
- Java Swing graphical user interface
- Uses standard Java libraries; no third-party runtime libraries are required

## Project Files and Folders

Here is what the main items in the project do.

| File / Folder | Purpose |
|---|---|
| `.gitignore` | Tells Git which generated or personal files should not be uploaded, such as `target/`, IntelliJ settings, and compiled output. |
| `pom.xml` | Maven project configuration. It defines the project name/version, text encoding, Java source/target compatibility (Java 8), and the application's main class. IntelliJ uses it to recognize and configure the Maven project. |
| `README.md` | This guide. It explains the project, files, setup, login, running instructions, and troubleshooting. |
| `RUN_WINDOWS.bat` | Windows helper script. Double-click it to launch `VirtualBankSimulator.jar` with `java -jar`, then keep the console open so any error message can be read. |
| `VirtualBankSimulator.jar` | Packaged Java application that can be launched without opening the source code in IntelliJ, provided Java is installed. |
| `MANIFEST.MF` | JAR metadata. It identifies `com.mycompany.gui.GUI` as the main class so Java knows which class to start. |
| `src/` | Application source code—the Java files you can read and edit. |
| `src/main/java/com/mycompany/gui/GUI.java` | Main application source. It creates the Swing login screen and dashboard, handles button actions, validates transactions, refreshes the interface, and contains the `Transaction` and `BankService` helper classes. |
| `target/` *(if present)* | Maven's generated build output, such as compiled `.class` files. It can be recreated by building the project, so it normally should not be committed to GitHub. |
| `.idea/` *(may be created by IntelliJ)* | Local IDE project settings. These are generated on your computer and are not required for the app's business logic. |

### Important Java classes inside `GUI.java`

The current version keeps the supporting classes in the same Java file to make the project easy for a beginner to open and run.

- **`GUI`** — starts the program and builds the graphical interface.
- **`BankService`** — handles the balance and transaction operations, checks whether an operation is valid, and reads/writes saved transaction data.
- **`Transaction`** — represents an individual transaction record, including its transaction details.

Useful methods include:

- `main(...)` — the application entry point.
- `showLogin()` / `createLoginPage()` — show and build the login screen.
- `createDashboardPage()` — builds the account dashboard.
- `performTransaction(...)` — processes a selected banking action.
- `refreshDashboard()` — updates the balance and transaction history shown on screen.
- `transact(...)` — performs the service-level transaction checks and updates saved data.

## How the Application Works

1. Run the application and enter the demo credentials.
2. The dashboard displays the account balance and transaction history.
3. Choose a transaction type and enter an amount.
4. The application checks the amount and whether enough balance is available for withdrawals/transfers.
5. If the transaction is accepted, the balance/history display is refreshed and the transaction is saved.
6. Saved transactions are loaded again the next time the application starts.

**Transfer limitation:** This demo does not manage recipient accounts. A transfer is recorded as money leaving the demo account; it does not credit another account.

## Demo Login

- **Username:** `khadiza242`
- **Password:** `242`

These credentials are hard-coded for demonstration and are not suitable for a real authentication system.

## Requirements

- JDK 8 or newer
- IntelliJ IDEA Community Edition or Ultimate
- Windows users can use `RUN_WINDOWS.bat`; the JAR can also be run from a terminal

## Run the Project

### Option A — Run from IntelliJ IDEA (recommended for learning)

1. Extract the ZIP archive.
2. Open the extracted project folder—the folder containing `pom.xml`—in IntelliJ IDEA.
3. Allow IntelliJ to load/sync the Maven project if prompted.
4. Open **File → Project Structure → Project** and select an installed JDK (Java 8 or newer).
5. Open `src/main/java/com/mycompany/gui/GUI.java`.
6. Click the green Run icon beside `main()`, or create a run configuration with this main class:

   `com.mycompany.gui.GUI`

### Option B — Run the packaged JAR

Make sure Java is installed. Open a terminal/Command Prompt in the project folder and run:

```bash
java -jar VirtualBankSimulator.jar
```

On Windows, you can double-click `RUN_WINDOWS.bat` instead.

To check whether Java is installed, run:

```bash
java -version
```

### Option C — Build with Maven

If Maven is available, open a terminal in the folder containing `pom.xml` and run:

```bash
mvn clean package
```

The Maven compiler settings target Java 8-compatible bytecode. If you change the source code, rebuild the JAR before launching the packaged application.

## Where Transaction Data Is Saved

The app stores transaction data in the user's home directory, outside the source-code folder:

- **Windows:** `%USERPROFILE%\.virtual-bank-simulator\transactions.csv`
- **macOS/Linux:** `~/.virtual-bank-simulator/transactions.csv`

This makes the data location independent of the folder from which the application is launched. The folder/file is created when needed.

## Reset the Demo Data

1. Close the application.
2. Open the data location listed above.
3. Delete `transactions.csv`.
4. Start the application again.

Only delete this file when you want to reset the demo transaction history and balance. Do not edit it while the app is running.

## Troubleshooting

- **`java` is not recognized:** Install a JDK and configure `JAVA_HOME`/PATH, or configure the JDK inside IntelliJ IDEA.
- **The JAR does not open when double-clicked:** Run `java -jar VirtualBankSimulator.jar` from Command Prompt to see the error. Check that Java is installed.
- **Maven sync/build fails:** Confirm that IntelliJ has a valid JDK selected and that the project is opened from the folder containing `pom.xml`.
- **The app opens but the login is rejected:** Use the demo username and password listed above, including the exact spelling.
- **The balance/history is not at its initial state:** The app loads saved data from `transactions.csv`. Follow the reset steps above to start fresh.

## Learning OOP from This Project

This project is a practical introduction to:

- **Classes and objects:** `GUI`, `BankService`, and `Transaction`.
- **Encapsulation:** transaction and balance operations are grouped in a service class rather than being scattered across every button handler.
- **Methods:** reusable methods build screens, process actions, validate input, and refresh the display.
- **Object-oriented modelling:** each `Transaction` object represents one transaction.
- **Separation of responsibilities:** Swing UI code presents information while `BankService` handles transaction rules and persistence.

For a larger future version, `GUI`, `BankService`, and `Transaction` could be moved into separate `.java` files, and recipient accounts, stronger authentication, and database storage could be added.
