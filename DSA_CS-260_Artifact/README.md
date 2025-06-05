# AVL Tree Implementation (CS-260 Artifact)

The improved CS-260 artifact now uses an **AVL tree** to store and manage course and prerequisite information. An AVL tree is a self-balancing binary search tree that ensures:
- **Efficient search, insertion, and deletion:** All major operations are guaranteed to run in O(log n) time due to automatic balancing.
- **Self-balancing:** After every insertion or deletion, the tree checks and restores its balance using rotations, ensuring optimal performance even as data grows.
- **Course and prerequisite management:** Each course is stored as a node in the AVL tree, with support for prerequisites and detailed course lookups.
- **Robust file loading:** Courses and their prerequisites can be loaded from a CSV file, with validation to ensure data integrity.
- **User-friendly traversal:** The tree supports in-order traversal to print all courses in sorted order, as well as detailed lookups for individual courses.

## Key Features
- **Self-balancing:** Maintains a height-balanced tree for optimal performance.
- **O(log n) operations:** Fast search, insert, and delete for course data.
- **Prerequisite validation:** Ensures all prerequisites exist in the course list.
- **Detailed output:** Prints course details, including prerequisites, or error messages if a course is not found.

## How to use

1. **Build the project:**
   - Make sure you have [Rust](https://www.rust-lang.org/tools/install) installed.
   - Open a terminal in this directory and run:
     ```sh
     cargo build --release
     ```

2. **Prepare your course data:**
   - Ensure you have a `CourseInput.csv` file in the top level directory of the executable (DSA_CS-260_Artifact in this case).

3. **Run the program:**
   - In the terminal, run:
     ```sh
     cargo run --release
     ```
   - Or run the built executable from `target/release/`.

4. **Using the menu:**
   - **1. Load Courses:** Loads courses from `CourseInput.csv` into the AVL tree.
   - **2. Print Course List:** Prints all courses in alphanumeric order, including prerequisites.
   - **3. Print Course Information:** Enter a course ID to see detailed information and prerequisites for that course.
   - **9. Exit:** Quit the program.

## Original Artifact
[Original DSA Artifact](https://github.com/JThomasDevs/SNHU-Portfolio/tree/main/CS-300%20Portfolio%20Submission)
