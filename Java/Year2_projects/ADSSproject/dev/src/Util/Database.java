package Util;
import java.sql.*;

public final class Database {
    private static final String DB_URL = "jdbc:sqlite:ADSS_Group_AR.db";
    private static Connection currentConnection;

    private static void initializeConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            currentConnection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = currentConnection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            try (Statement st = currentConnection.createStatement()) {
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Shipments(
                                id   INTEGER PRIMARY KEY AUTOINCREMENT,
                                origin   INTEGER NOT NULL,
                                destination   INTEGER NOT NULL,
                                day INTEGER NOT NULL,
                                month INTEGER NOT NULL,
                                year INTEGER NOT NULL,
                                hour INTEGER,
                                mins INTEGER,
                                status TEXT NOT NULL ,
                                FOREIGN KEY (origin) REFERENCES Locations(id),
                                FOREIGN KEY (destination) REFERENCES Locations(id)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Locations(
                                id   INTEGER PRIMARY KEY AUTOINCREMENT,
                                street           TEXT NOT NULL,
                                number      INTEGER  NOT NULL,
                                district       TEXT NOT NULL,
                                contact_name TEXT NOT NULL,
                                contact_num TEXT NOT NULL
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Drivers(
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT NOT NULL,
                                status Text  NOT NULL
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Licenses(
                                license_type TEXT NOT NULL,
                                driver_id INTEGER NOT NULL,
                                PRIMARY KEY (license_type, driver_id),
                                FOREIGN KEY (driver_id)  REFERENCES Drivers(id) ON DELETE CASCADE
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Vehicles(
                                license_num INTEGER PRIMARY KEY NOT NULL,
                                vehicle_weight INTEGER NOT NULL,
                                max_vehicle_weight INTEGER NOT NULL,
                                vehicle_status TEXT NOT NULL,
                                vehicle_type TEXT NOT NULL
                            );
                        """);
                st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Products (
                                productID INTEGER PRIMARY KEY,
                                productName TEXT,
                                producerName TEXT,
                                salePrice REAL,
                                minAmount INTEGER,
                                shelfAmount INTEGER,
                                storageAmount INTEGER,
                                weightPerUnit REAL,
                                primaryCategory TEXT,
                                secondaryCategory TEXT,
                                sizeCategory TEXT,
                                FOREIGN KEY(primaryCategory) REFERENCES Categories(categoryName),
                                FOREIGN KEY(secondaryCategory) REFERENCES Categories(categoryName),
                                FOREIGN KEY(sizeCategory) REFERENCES Categories(categoryName)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS ShipmentsProducts(
                                product_name TEXT NOT NULL,
                                product_weight REAL NOT NULL,
                                product_amount INTEGER NOT NULL,
                                shipment_id INTEGER NOT NULL,
                                PRIMARY KEY (product_name, shipment_id),
                                FOREIGN KEY (shipment_id)  REFERENCES Shipments(id) ON DELETE CASCADE
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS order_items (
                            orderID INTEGER,
                            productID INTEGER,
                            quantity INTEGER,
                            price REAL,
                            name TEXT,
                            PRIMARY KEY(orderID, productID),
                            FOREIGN KEY(orderID) REFERENCES orders(orderID),
                            FOREIGN KEY(productID) REFERENCES products(productID)
                        );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Products (
                                productID INTEGER PRIMARY KEY,
                                productName TEXT,
                                producerName TEXT,
                                salePrice REAL,
                                minAmount INTEGER,
                                shelfAmount INTEGER,
                                storageAmount INTEGER,
                                weightPerUnit REAL,
                                primaryCategory TEXT,
                                secondaryCategory TEXT,
                                sizeCategory TEXT,
                                FOREIGN KEY(primaryCategory) REFERENCES Categories(categoryName),
                                FOREIGN KEY(secondaryCategory) REFERENCES Categories(categoryName),
                                FOREIGN KEY(sizeCategory) REFERENCES Categories(categoryName)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Units (
                                unitID INTEGER PRIMARY KEY,
                                productID INTEGER,
                                location TEXT,
                                expiryDate TEXT,
                                isDefective BOOLEAN,
                                FOREIGN KEY(productID) REFERENCES Products(productID)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS ProductCostPrices (
                                productID INTEGER,
                                startDate TEXT,
                                costPrice REAL,
                                FOREIGN KEY(productID) REFERENCES Products(productID)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS Categories (
                                categoryName TEXT PRIMARY KEY,
                                rank INTEGER
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS SaleDiscounts (
                                discountID INTEGER PRIMARY KEY,
                                rate REAL,
                                startDate TEXT,
                                endDate TEXT
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS ProductDiscounts (
                                discountID INTEGER PRIMARY KEY,
                                productID INTEGER,
                                FOREIGN KEY(productID) REFERENCES Products(productID),
                                FOREIGN KEY(discountID) REFERENCES SaleDiscounts(discountID)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS CategoryDiscounts (
                                discountID INTEGER PRIMARY KEY,
                                categoryName TEXT,
                                FOREIGN KEY(categoryName) REFERENCES Categories(categoryName),
                                FOREIGN KEY(discountID) REFERENCES SaleDiscounts(discountID)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS suppliers (
                                supplierID INTEGER PRIMARY KEY AUTOINCREMENT,
                                supplierName TEXT NOT NULL,
                                bankAccountNumber TEXT,
                                isActive BOOLEAN
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS agreements (
                                agreementID INTEGER PRIMARY KEY AUTOINCREMENT,
                                supplierID INTEGER,
                                paymentMethod TEXT,
                                paymentDate TEXT,
                                agreementStatus BOOLEAN,
                                FOREIGN KEY(supplierID) REFERENCES suppliers(supplierID)
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS discounts (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                catalogID INTEGER,
                                quantityCond INTEGER,
                                precentage REAL,
                                agreementID INTEGER,
                                FOREIGN KEY(agreementID) REFERENCES agreements(agreementID),
                                FOREIGN KEY(catalogID) REFERENCES supplier_products(catalogID)
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS fixedDeliveryAgreement (
                                agreementID INTEGER PRIMARY KEY,
                                DayOfSupply TEXT,
                                FOREIGN KEY(agreementID) REFERENCES agreements(agreementID)
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS flexibleDeliveryAgreement (
                                agreementID INTEGER PRIMARY KEY,
                                SupplyDays INTEGER,
                                FOREIGN KEY(agreementID) REFERENCES agreements(agreementID)
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS pickupAgreement (
                                agreementID INTEGER PRIMARY KEY,
                                address TEXT,
                                FOREIGN KEY(agreementID) REFERENCES agreements(agreementID)
                            );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS supplier_products (
                                  catalogID INTEGER PRIMARY KEY,
                                  productID INTEGER,
                                  manufacturer TEXT,
                                  supplierID INTEGER,
                                  agreementID INTEGER,
                                  price REAL,
                                  productType TEXT,
                                  productName TEXT,
                                  AgreementType TEXT,
                                  isActive BOOLEAN,
                                  FOREIGN KEY(supplierID) REFERENCES suppliers(supplierID),
                                  FOREIGN KEY(agreementID) REFERENCES agreements(agreementID)
                              );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS order_items (
                            orderID INTEGER,
                            productID INTEGER,
                            quantity INTEGER,
                            price REAL,
                            name TEXT,
                            PRIMARY KEY(orderID, productID),
                            FOREIGN KEY(orderID) REFERENCES orders(orderID)
                        );
                        """);
                st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS orders (
                            orderID INTEGER PRIMARY KEY AUTOINCREMENT,
                            orderDate TEXT,
                            orderRecDate TEXT,
                            supplierID INTEGER,
                            orderStatus BOOLEAN,
                            totalPrice REAL,
                            orderType TEXT,
                            address TEXT,
                            contactID INTEGER,
                            FOREIGN KEY(supplierID) REFERENCES suppliers(supplierID),
                            FOREIGN KEY(contactID) REFERENCES contacts(contactID)
                        );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS supplier_manufacturers (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                supplierID INTEGER,
                                manufacturerName TEXT,
                                FOREIGN KEY(supplierID) REFERENCES suppliers(supplierID)
                            );
                        """);
                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS contacts (
                                contactID INTEGER PRIMARY KEY AUTOINCREMENT,
                                supplierID INTEGER,
                                name TEXT NOT NULL,
                                phone TEXT NOT NULL,
                                FOREIGN KEY(supplierID) REFERENCES suppliers(supplierID)
                                );
                        """);

                st.executeUpdate("""
                            CREATE TABLE IF NOT EXISTS supplier_product_types (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                supplierID INTEGER,
                                productType TEXT,
                                FOREIGN KEY(supplierID) REFERENCES suppliers(supplierID)
                            );
                        """);

            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (currentConnection == null || currentConnection.isClosed()) {
            initializeConnection();
        }
        return currentConnection;

    }
}

