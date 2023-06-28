
<br />
<div align="center">
    <img src="app/src/main/res/mipmap-xxxhdpi/ic_slice_up_logo_parts_round.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">SliceUp - meal planning app</h3>

  <p align="center">
  Plan meals for the whole week. Save time, money, and energy for someone or something you love.

</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#roadmap">Screenshots</a></li>
    <li><a href="#room-database">Room Database</a></li>
  </ol>
</details>

## About The Project

SliceUp is my new Android app for planning meals for the whole week.
The main idea is to simplify the whole process of planning your meals - from creating a base with your favorite recipes, planning what you are going to cook for the whole week of just weekends, and finally, building an entire list of groceries to save time for shopping. 

### Built With

* Kotlin
* Jetpack compose

## Roadmap
- [x] Created personas, a customer journey map  <a href="https://drive.google.com/file/d/1vrpAytPPcbJLOrA0djycPZ3BcT5bxnfE/view?usp=sharing">Link</a>
- [x] Created the UX prototype - <a href="https://www.figma.com/file/CCxIlRWLMe1mUQ1WplVj29/MealPrep_current?node-id=1-632">Link to Figma</a>
- [x] Created UI on Jetpack compose
- [x] Integrated RoomDatabase to this project as a data persistence solution - <a href="https://github.com/eliseevavika/MealPrep/blob/main/app/src/main/java/com/example/mealprep/Database.kt">Link to Kotlin file</a>

- [ ] MVVM, Repository, Clean Architecture patterns

## Screenshots

<img src="app/screenshots/1.png" alt= “” width="300" height="600">  |  <img src="app/screenshots/2.png" alt= “” width="300" height="600">  |  <img src="app/screenshots/3.png" alt= “” width="300" height="600">

<img src="app/screenshots/4.png" alt= “” width="300" height="600">  |  <img src="app/screenshots/5.png" alt= “” width="300" height="600">  


## Room Database

Integrated RoomDatabase to this project as a data persistence solution. RoomDatabase is an Android library that provides an abstraction layer over SQLite, allowing for efficient and convenient database operations. 
By integrating RoomDatabase into my project, I was able to efficiently store, retrieve, and manage structured data, providing a robust and reliable data layer for my application.

Incorporating RoomDatabase into my project involved the following steps:

* Setting up the necessary dependencies: I added the Room library to my project's dependencies in the build.gradle file.
* Defining the entity classes: I created data classes that represent the tables in my database. These classes are annotated with @Entity to define their structure and properties.
* Creating the database class: I implemented a subclass of RoomDatabase that serves as the main access point to the database. This class is annotated with @Database and includes the entity classes and database version information.
* Defining DAO (Data Access Object) interface: I created an interface that defines the database operations using annotations such as @Insert, @Update, @Delete, and @Query. This interface serves as a bridge between the application and the database, allowing for convenient data access and manipulation.
* Accessing the database in the application: I utilized the RoomDatabase instance in my application's components, such as ViewModel or Repository class, to perform database operations asynchronously using coroutines or LiveData.
