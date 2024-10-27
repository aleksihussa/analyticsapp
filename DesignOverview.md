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

### Mid-term self-evaluation

Our initial design for the application architecture was to implement three modules: **UI**, **Service** and **Data**, which are also described above. The chosen architecture has supported implementation mainly because it has made simultaneous developing problem-free. This is due to the work rarely overlapping between modules. Also, the design ensures that each module has a single responsibility. For the rest of the project, we intend to continue with this design.

As one could maybe deduce from the previous paragraph, we have been able to stick to the original design very well. The "inner" designs of some of the modules have been modified slightly along the way though, for example to follow some design pattern. Implementation of the base features has been quite clear during the whole process: what needs to be done, where, when, etc. To address how the design corresponds to quality, we have planned the application to be relatively easily tested. Also, the design follows separation of concerns. 

We believe we have at least somehow implemented all the main classes needed to take the project to the finish line. The remaining work will not necessarily require any notable changes – instead, it will be related to connecting the pieces and building some small things onto what already exists. Additionally, tests need to be implemented. Finally, the guideline requirements regarding the actual code at this point should be fulfilled with ease. 
