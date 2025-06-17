<!-- Home Page content for GH Pages -->

<!-- Professional Self-Assesment -->
## Self-Assesment

*In progess...*

<!-- Code Review Video -->
## **Code Review**

The first step in conceptualizing the enhancements made to my chosen artifacts was completing an in-depth code review for the artifacts in their original state.

[![Capstone Artifact Code Review link](https://img.youtube.com/vi/Ge65v-B6JPI/0.jpg)](https://www.youtube.com/watch?v=Ge65v-B6JPI)

<!-- Artifacts -->
## **Artifacts**

I chose to enhance two artifacts from previous Computer Science courses.

The first and third enhancements were implemented on an artifact from my **_Intro to Software Development_** course.</br> 
This artifact has been enhanced in the areas of _Software Design and Engineering_ and _Databases_ through the addition of a web based UI and an SQLite database for persistent storage.</br>
> Artifact 1 in pre-enhancement state can be found [here](./Artifact%201%20Original%20State/).

The second enhancement was implemented on an artifact from my **_Data Structures and Algorithms_** course.</br>
This artifact has been enhanced in the area of _Data Structures and Algorithms_ with the original BST being converted to an AVL tree, rewritten in Rust.</br>
> Artifact 2 in pre-enhancement state can be found [here](./Artifact%202%20Original%20State/).

<!-- Narratives -->
## **Enhancement Narratives**

&emsp; Alongside each enhancement, I created an accompanying narrative that covers the origins of the artifact, why I chose to enhance the artifact for my Capstone portfolio, detailed breakdowns of the enhancement(s) made for the artifact, how the enhancements support the desired course outcomes, and a reflection of the enhancement process. 

*For convenience, each narrative has been formatted for markdown and included below.*

<!-- Narrative for Enhancement 1 -->
<details>
<summary><b>Enhancement 1 Narrative: Design and Software Engineering</b></summary>
</br>
<b><u>Artifact Description</u></b></br>
&emsp; The original artifact enhanced in this milestone was a simple Java command line application that allows users to register, view, and reserve rescue animals. This artifact was originally created in Summer of 2023 and was primarily composed of basic Java classes and a Driver responsible for business logic. Due to the limited scope of the original artifact, the core functionality consists of I/O operations in the console and using in-memory storage.
</br></br>
<b><u>Justification for Inclusion</u></b></br>
&emsp; I chose this artifact for my ePortfolio because I believe that it can greatly benefit from an interactive user interface that is easy to navigate and operate. In its original form, the application feels clunky and unintuitive at times – especially when registering a new animal – due to the intentionally minimal design of the original artifact. By transforming the artifact from a basic console application into a multi-component, reactive system, I can showcase a range of software development skills.

This milestone contains two major enhancements:

- <i>Creation of the RESTful API</i></br>
&emsp; To enable interoperability between the Java backend and the Python frontend, I designed and built a RESTful API in Java. This API exposes the core functionality of the app (with some optimizations) which allows it to be accessed by external clients. Having an API also enhances the flexibility of my code.

- <i>Development of a Python GUI</i></br>
&emsp; I chose to build the GUI enhancement in Python, leveraging the Streamlit framework, my previous experience with web-based development, and my experience creating a UX focused reactive UI in Tkinter. The choice to implement the GUI in Python necessitated the creation of the Java API to ensure that the frontend can utilize the functionality of the Java backend to update and edit program data in a user-friendly interface.

&emsp; These enhancements demonstrate my ability to design and build cross-language solutions, design an API, and create a UI that is both UX and performance focused. The artifact now displays my understanding of backend and frontend development, in addition to my ability to modernize and extend legacy code.

<b><u>Course Outcomes and Updates</u></b></br>
&emsp; The enhancements made in this milestone support the course outcomes I aimed to meet in several ways:
- Adding an API and Python GUI allows for easier collaboration and updates by others.
- Aimed to keep documentation for the components of the system clear for users and anyone who might view the project later. I did this using Javadoc style comments in the Java portions and Pydoc-like Docstrings for the Python portions.
- I used standard tools and methods for building the API and GUI.
- Basic security practices like handling malformed data and input validation were followed in the API design.

&emsp; Most of these updates are in line with my original plans. The only unplanned update in this milestone is the inclusion of the Java API.

<b><u>Reflection on the Enhancement Process</u></b></br>
&emsp; Enhancing this project from a simple Java console app to a system with a UX-focused UI and an API was a strong reinforcement of concepts that I had encountered through previous courses and personal projects. Choosing Python as the language to write the GUI meant that I needed a way for it to communicate with the Java backend. Originally, I tried a CLI based approach that got to a point where it felt overengineered and too clunky, resulting in the decision to move towards a RESTful API. I had implemented an API before and made a UX-focused GUI before, but I had not previously implemented them on the same program.
</br>
&emsp; Several parts of the process presented unique challenges I had not encountered before. One such challenge was getting data to be serialized to JSON from the Java backend and then deserializing that data with Python. What seemed like a straightforward task turned into a struggle to transform specific types of data structures into JSON keys and values. This, however, I attribute to my lack of experience with Java syntax and data type names/functions.
</br>
&emsp; Overall, the work done this milestone helped me get more exposure to connecting different system components written in different languages. It also reinforced the concepts I already held close about designing an application for future expansion by creating clear interfaces, separating concerns, and making choices that ease the implementation of new features and changes in the future.
</details>
</br>

<!-- Narrative for Enhancment 2 -->
<details>
<summary><b>Enhancement 2 Narrative: Data Structures and Algorithms</b></summary>
</br>
<b><u>Artifact Description</u></b></br>
&emsp; The original artifact enhanced in this milestone was a C++ implementation of a basic binary search tree (BST). It was submitted as one of the final projects in my Data Structures and Algorithms course. The BST provided standard operations such as insertion, deletion, search, and in-order traversal. I chose this artifact to enhance in the area of Data Structures and Algorithms as part of my Capstone project. By implementing self-balancing to the BST, I have enhanced the worst-case performance of search, insert, and delete operations from O(n) time for a given operation to O(log n) for the same operations with the AVL tree. Additionally, by rewriting in the Rust language, I have guaranteed memory safety throughout the entire program due to Rust’s concept of “ownership” and its accompanying borrow checker.
</br></br>

<b><u>Justification for Inclusion</u></b></br>
&emsp; I chose this artifact for my ePortfolio because it demonstrates my ability to assess and improve upon previous work through the use of performant data structures and modern programming languages. The original C++ BST was susceptible to degradation of performance in the worst case and displayed the ease of writing memory unsafe code in a language like C++. By rewriting the artifact in Rust and implementing an AVL tree, I was able to demonstrate several key skills:
</br>

- <i>Memory Safety and Reliability</i></br>
&emsp; Rust’s concept of ownership and strict compile-time checks done by the borrow checker eliminate entire categories of bugs, like dangling pointers and improper memory management. This makes the Rust implementation inherently safer and more resistant to bugs/undefined behavior.
</br>

- <i>Explicit Error Handling</i></br>
&emsp; In Rust, the `Option` type provides a memory safe alternative to null pointers by providing an enum wrapper where a value either exists – `Some(val)` – or does not exist – `None`. Similarly, the `Result` type in Rust provides an alternative to error handling with try-catch blocks. Instead of throwing errors to be caught somewhere else, a process that returns a `Result` can either return `Ok(optional_val)` or `Err(err_msg)` that serves as a wrapper around the expected value or the returned error message. This enables Rust to return one type that can represent both success and failure as opposed to the old system of throwing exceptions to be later caught.
</br>

- <i>Algorithmic Improvement</i></br>
&emsp; Upgrading from a regular BST to an AVL tree ensures that the tree remains balanced after every operation. This guarantees O(log n) worst-case time complexity for search, insert, and delete operations. This is a large improvement over the original BST, which has a worst-case time complexity of O(n) for the same operations.
</br>

&emsp; These enhancements demonstrate my ability to apply algorithmic principles, leverage language features for safer code, and optimize data structures for real-world use cases.
</br>

<b><u>Course Outcomes and Updates</u></b></br>
&emsp; The enhancements made in this milestone support the course outcomes in the following ways:

- By using Rust’s documentation tools, I made the codebase more approachable for future collaborators, supporting diverse audiences in understanding and extending the project.

- I maintained clear, technically sound documentation throughout the code, using Rustdoc comments and providing usage examples to ensure the project is accessible to both technical and non-technical audiences.

- The move from a C++ BST to a Rust AVL tree directly demonstrates my ability to design and evaluate computing solutions using appropriate algorithmic principles, while managing trade-offs between performance and complexity.

- Adopting Rust for this project showcases my willingness to use innovative tools and techniques in software engineering, moving beyond traditional languages to deliver more reliable and maintainable solutions.

- Rust’s memory safety guarantees and explicit error handling help mitigate common vulnerabilities, such as buffer overflows and null pointer dereferences, thereby enhancing the security and reliability of the artifact.

&emsp; These updates are in line with my original plans for the capstone. The only notable change is the deeper focus on leveraging Rust’s safety features, which became more apparent as I worked through the enhancement process.

<b><u>Reflection</u></b></br>
&emsp; Enhancing this artifact from a C++ BST to a Rust AVL tree was both challenging and rewarding. The process required me to adapt recursive tree algorithms to Rust’s strict ownership and borrowing model, which fortified my understanding of the strengths and limitations of the Rust language.
</br>
&emsp; Already having a base familiarity with Rust, applying the ownership and borrowing rules to a complex, pointer-heavy data structure like an AVL tree deepened my understanding of the nuances in the ownership and borrow checker models that are not immediately obvious. One such nuance was the inability for a `struct` to have a member variable that is of its own type – a `Node` that has left and right children that are also `Node` type. To solve this, I used the `Box` smart pointer provided by Rust, which points to data allocated on the heap. Additionally, Rust’s pattern matching made it easier to handle cases that would have required careful pointer management and null checks in C++.
</br>
&emsp; By transitioning from a BST to an AVL tree, I also learned more about the importance of algorithmic efficiency. Implementing the balancing logic gave me a deeper understanding of how data structures can degrade into inefficient messes and how simple changes can mitigate the degradation.
</details>
</br>

<!-- Narrative for Enhancement 3 -->
<details>
<summary><b>Enhancement 3 Narrative: Databases</b></summary>
</br>
<b><u>Artifact Description</u></b></br>
&emsp; The artifact is a Rescue Animal Management System that originated as my final project for IT-145 (Introduction to Software Development). Originally, the project was designed as an exercise in implementing inheritance and encapsulation in Java, focusing on creating a mockup animal rescue and reservation system. After my initial enhancement that added a RESTful API and Python Streamlit frontend, this enhancement focuses on implementing persistent storage using JPA/Hibernate with SQLite and adapting the data models for database integration.
</br>

<b><u>Justification for ePortfolio Inclusion</u></b></br>
&emsp; I selected this artifact for my ePortfolio because it demonstrates my ability to implement professional grade database solutions by extending and modifying existing software. The enhancement process included:

- <i>Database Integration:</i> I implemented persistent storage using JPA/Hibernate with SQLite, which:
    - Replaced in memory storage with an SQLite database  
    - Implemented proper transaction management for database state consistency  
    - Demonstrated understanding of ORM principles  
    - Obfuscates DB operations behind object-oriented interfaces  
    - Use of prepared SQL queries to prevent injection style attacks

- <i>Data Model Adaptation:</i> I modified the existing models to work with JPA/Hibernate:
    - Added proper JPA annotations (@Entity, @Column, @MappedSuperclass)  
    - Created Data Access Objects (DAOs) for handling object related DB operations

- <i>Architecture Improvement:</i> The system now follows proper layered architecture:
    - Clear separation between models, DAOs, and controllers  
    - More robust error handling
</br>

<b><u>Course Outcomes Achievement</u></b></br>
&emsp; The enhancement successfully addressed several course outcomes:
- Implemented professional database solution using JPA/Hibernate
- Designed efficient database queries
- Implemented proper data access patterns  
- Created reusable data access components
- Added comprehensive Javadoc documentation  
- Implemented proper error handling and logging  
- Maintained clean code structure
- Implemented proper transaction management  
- Protected against SQL injection through JPA+Hibernate


<b><u>Reflection on the Enhancement Process</u></b></br>
&emsp; The process of enhancing this artifact provided valuable learning experiences in database implementation and system architecture. Through the implementation of JPA/Hibernate with SQLite, I gained a deeper understanding of proper ORM configuration, transaction management, and data access patterns. The enhancement process required careful consideration of error handling in database operations, particularly when dealing with concurrent operations and transaction management to maintain database integrity.
</br>
&emsp; The most significant challenge I faced was adapting the existing models to work with JPA/Hibernate while maintaining the original inheritance structure. This required careful consideration of proper annotation placement and inheritance mapping, as well as implementing robust transaction management and error handling. The process of configuring JPA/Hibernate with SQLite presented unique challenges, particularly in ensuring the interoperability of the various dependencies.
</br>
&emsp; Through this enhancement, I learned the importance of proper layered architecture and the value of separation of concerns. Implementing the DAO pattern effectively was crucial in maintaining a clean separation between the data access layer and the business logic. This experience taught me how to maintain backward compatibility while evolving a system from in-memory storage to a proper database backed solution. The implementation of JPA/Hibernate with SQLite has provided valuable insights into professional database design practices, significantly improving my understanding of database implementation and ORM principles.
</details>

