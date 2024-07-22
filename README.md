# Bookstore Management System

The Bookstore Management System is a Java-based application designed to manage book sales and inventory. This project employs several design patterns to create a flexible, scalable, and maintainable system. The key design patterns used include Factory, Builder, Adapter, Bridge, Decorator, and Strategy.

## Project Objectives
- Implement a bookstore management system that handles book inventory, sales, and user interactions.
- Utilize various design patterns to ensure code flexibility, reusability, and adherence to object-oriented principles.
- Provide a scalable architecture that can be easily extended with new features or modified requirements.

## Design Patterns Used

### 1. Factory Pattern
The Factory Pattern is used to create objects without specifying the exact class of object that will be created. This pattern is applied to create different types of book objects (e.g., physical books, e-books) and manage their instantiation through a common interface.

**Example Usage:**
- **BookFactory:** Creates instances of `Book` subclasses (e.g., `PhysicalBook`, `EBook`) based on the type requested.

### 2. Builder Pattern
The Builder Pattern is used to construct complex objects step by step. It is applied to construct detailed book objects with various attributes such as title, author, and publication year, allowing for a customizable creation process.

**Example Usage:**
- **BookBuilder:** Constructs a `Book` object with optional attributes like a special edition or author’s autograph.

### 3. Adapter Pattern
The Adapter Pattern allows incompatible interfaces to work together. It is used to integrate the bookstore system with external systems or libraries, such as payment gateways or inventory management systems.

**Example Usage:**
- **PaymentAdapter:** Adapts different payment processing services to a unified payment interface.

### 4. Bridge Pattern
The Bridge Pattern separates an abstraction from its implementation, allowing both to vary independently. It is used to decouple the book management system's core functionality from the underlying storage mechanism (e.g., database, file system).

**Example Usage:**
- **BookStorageBridge:** Provides an interface to store and retrieve book data from various storage implementations (e.g., SQL database, NoSQL database).

### 5. Decorator Pattern
The Decorator Pattern adds new functionality to an object dynamically. It is used to extend the functionality of book objects with additional features like special promotions or discount wrappers without altering their core structure.

**Example Usage:**
- **DiscountDecorator:** Adds discount functionality to a `Book` object, applying different discount strategies.

### 6. Strategy Pattern
The Strategy Pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. This pattern is used for implementing different pricing strategies and search algorithms within the bookstore system.

**Example Usage:**
- **PricingStrategy:** Provides various pricing algorithms (e.g., seasonal discounts, member discounts) that can be applied to book prices dynamically.

## Features
- **Book Creation:** Create various types of books with different attributes and formats using Factory and Builder patterns.
- **Inventory Management:** Manage book inventory and integrate with external systems using Adapter and Bridge patterns.
- **Dynamic Pricing:** Apply different pricing strategies and promotions using the Strategy and Decorator patterns.
- **Flexible Integration:** Easily integrate with third-party services for payment processing and data storage.


