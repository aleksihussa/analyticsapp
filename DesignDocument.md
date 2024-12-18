### Project Architecture Overview

The project is divided into three main modules: **UI**, **Service**, and **Data**. The architecture follows a modular design, which promotes clear separation of concerns and ensures scalability and maintainability.

- **UI Module**: The user interface layer is responsible for handling user interactions. It initiates service calls based on user input and displays the processed data.

- **Service Module**: The service layer acts as an intermediary between the UI and the data handling logic. Upon receiving a request from the UI, it calls the appropriate API and forwards the response to the Data module for processing. Uses the factory pattern for creating service objects `AgricultureService` and `GDPService`, both implementing the `ApiService` interface. The `Service` class is responsible for making the necessary HTTP requests.

- **Data Module**: This module handles the conversion and management of data. It processes API responses and converts them into appropriate data objects, which are formed from the data returned by the service module. This module has base classes for `GDP` and 'Agricultural Employement By Country' (`AgrEmplByCountry`), and converters for converting `JSONobject`s to these two class objects.

#### Control Flow:

1. The **UI** calls the desired service.
2. The **Service** makes an API call and passes the result to the **Data** module.
3. The **Data** module processes the API data and creates data objects.
4. The processed data objects are returned to the **UI** through the **Service**.

This architecture enables parallel development, allowing multiple team members to work concurrently on the UI, Service, and Data modules. The design adheres to the principles of **object-oriented programming (OOP)** and leverages **Java design patterns** such as Converter pattern which in the Gang of four design patterns is known as `Adapter pattern`. As the project progressses, code components will have more dependency injection, abstractions and inheritance used.

The core principle behind the design (although naming and some conventions modified) **Model-View-Controller (MVC)** pattern to ensure a clean separation between the data model, business logic, and user interface.

### API usage

Two external APIs are used:

1. **International Monetary Fund: DataMapper API, v1**

The IMF Datamapper API is used for retrieving various economical data on different countried or regions. In our project we use it to retrieve GDP by year on different countries.

**Documentation**:
[IMF DataMapper API documentation](https://www.imf.org/external/datamapper/api/help)

**Example API call**

```
https://www.imf.org/external/datamapper/api/v1/indicator/NGDPD/country/fin
```

**Return object format (example years 2000-2003, Billions of U.S Dollars):**

```
{
  2000: 126.075,
  2001: 129.534,
  2002: 140.305,
  2003: 171.609,
}
```

2. **The World Bank: Indicators API, v2**

The World Bank Indicators API v2 offers over 16 000 indicators from over 45 databases with data from up to 50 years ago. The themes are mostly in economy-relates matters, including international debt statistics, world development indicators, and
subnational poverty.

In this project the API is used to gain access to the percentage of employment being in the agricultural sector in a country.

**Documentation**:
[World Bank API Developer Information](https://datahelpdesk.worldbank.org/knowledgebase/topics/125589-developer-information)

**Example API call**:

```
https://api.worldbank.org/v2/country/fin/indicator/SL.AGR.EMPL.ZS?date=2000:2003&format=json
```

**Return array format (Relevant data being `date` and `value`):**

```
[
  {
    indicator: {id: AG.AGR.TRAC.NO, value: <id explanation>},
    country: {id: <code> , value: <country name>},
    countryiso3code: <country iso3 code>,
    date: <year (string)>,
    value: <agriculture % of total employment (float)> || null,
    unit: <empty string>,
    obs_status: <empty string>,
    decimal: 0
  }
]
```

3. **Countries**

Countries are sourced from [world_countries](https://github.com/stefangabos/world_countries) and formatted with custom code. Additionally, some updates were made to country names to maintain consistency.

### Mid-term self-evaluation

Our initial design for the application architecture was to implement three modules: **UI**, **Service** and **Data**, which are also described above. The chosen architecture has supported implementation mainly because it has made simultaneous developing problem-free. This is due to the work rarely overlapping between modules. Also, the design ensures that each module has a single responsibility. For the rest of the project, we intend to continue with this design.

As one could maybe deduce from the previous paragraph, we have been able to stick to the original design very well. The "inner" designs of some of the modules have been modified slightly along the way though, for example to follow some design pattern. Implementation of the base features has been quite clear during the whole process: what needs to be done, where, when, etc. To address how the design corresponds to quality, we have planned the application to be relatively easily tested. Also, the design follows separation of concerns.

We believe we have at least somehow implemented all the main classes needed to take the project to the finish line. The remaining work will not necessarily require any notable changes – instead, it will be related to connecting the pieces and building some small things onto what already exists. Additionally, tests need to be implemented. Finally, the guideline requirements regarding the actual code at this point should be fulfilled with ease.

### Use of AI

Our use of AI has for the most parts focused on using Github copilot to aid and speed up our programming. Also, when we were throwing ideas around in the beginning, we used ChatGPT's ideas as a source for the idea we ended up with.
