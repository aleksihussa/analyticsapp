### Project Architecture Overview

The project is divided into three main modules: **UI**, **Service**, and **Data**. The architecture follows a modular design, which promotes clear separation of concerns and ensures scalability and maintainability.

- **UI Module**: The user interface layer is responsible for handling user interactions. It initiates service calls based on user input and displays the processed data.

- **Service Module**: The service layer acts as an intermediary between the UI and the data handling logic. Upon receiving a request from the UI, it calls the appropriate API and forwards the response to the Data module for processing.

- **Data Module**: This module handles the conversion and management of data. It processes API responses and converts them into appropriate data objects, which are formed from the data returned by the service module.

#### Control Flow:
1. The **UI** calls the desired service.
2. The **Service** makes an API call and passes the result to the **Data** module.
3. The **Data** module processes the API data and creates data objects.
4. The processed data objects are returned to the **UI** through the **Service**.

This architecture enables parallel development, allowing multiple team members to work concurrently on the UI, Service, and Data modules. The design adheres to the principles of **object-oriented programming (OOP)** and leverages **Java design patterns** such as Converter pattern which in the Gang of four design patterns is known as `Adapter pattern`. As the project progressses, code components will have more dependency injection, abstractions and inheritance used.

The core principle behind the design (although naming and some conventions modified) **Model-View-Controller (MVC)** pattern to ensure a clean separation between the data model, business logic, and user interface.

