#include <iostream>
#include <fstream>
#include <vector>
#include <limits>
#include <string>

using namespace std;

// Constant for input file name
const string INPUT_FILE = "CourseInput.csv";

/*
 * Function to load file into vector of strings.
 * Throws runtime_error if file is unable to be opened for any reason.
 */
vector<string> loadFile(string filePath) {
    // Open file
    ifstream file(filePath);
    // Vector to hold lines
    vector<string> lines;
    // If file is open, read lines into vector
    if (file.is_open()) {
        string line;
        // While there is another line in the file, read it into the vector
        while (getline (file, line)) {
            // Skip empty lines
            if (line.empty()) {
                continue;
            }
            else {
                lines.push_back(line);
            }
        }
    }
        // If file is not open, throw runtime_error
    else {
        throw runtime_error("Could not open file");
    }
    // Return lines vector
    return lines;
}

/*
 * This is a helper function to determine which course IDs are valid
 * prerequisites for other courses.
 */
vector<string> loadValidCourseIds(string filePath){
    vector<string> validIds;
    vector<string> lines = loadFile(filePath);
    /*
     * For every line in the file, the courseId is the first element in the line.
     * Push the courseId into the validIds vector.
     */
    for (const string& line : lines) {
        string courseId = line.substr(0, line.find(','));
        validIds.push_back(courseId);
    }
    return validIds;
}

// Helper function to split a string by a delimiter
vector<string> split(string line, string delimiter) {
    vector<string> splitLine;
    // index variable to keep track of current position in string being split
    size_t pos;
    /*
     * While the pos variable is a valid index value in the line string, keep looping
     */
    while ((pos = line.find(delimiter)) != string::npos) {
        // Push the substring of index 0 to the pos index to the splitLine vector
        splitLine.push_back(line.substr(0,pos));
        // Erase the substring including the delimiter and keep looping until done
        line.erase(0,pos+delimiter.size());
    }
    // Get the leftovers if there's no delimiter at the line end
    if (!line.empty()) {
        splitLine.push_back(line);
    }
    return splitLine;
}

// Struct to hold course information read from file.
struct Course {
    string courseId;
    string courseName;
    vector<string> prereqCourses;

    // Constructor
    Course() {
        courseId = "-1";
    }
};

// Node struct to hold course information and point to other nodes
struct Node {
    Course course;
    Node* left;
    Node* right;

    // Default Constructor
    Node() {
        left = nullptr;
        right = nullptr;
    }

    // Initialize a Node with a Course object
    Node(Course aCourse) : Node() {
        course = aCourse;
    }
};


// Binary Search Tree class definition
class BinarySearchTree {
private:
    // Root node of the tree
    Node* root;
    void InOrder(Node* node);

public:
    BinarySearchTree();
    Course Search(string searchId);
    void Insert(Course aCourse);
    void Remove(string searchId);
    void PrintCourse(Course course);
    void InOrder();
};

BinarySearchTree::BinarySearchTree() {
    root = nullptr;
}

// Search BST for a Course object with a matching courseId
Course BinarySearchTree::Search(string searchId) {
    // Start searching for matching Node at the root
    Node* cur = root;
    // While we have not hit a dead end after a leaf node
    while (cur != nullptr) {
        // If we found a matching courseId within the cur Node's Course object
        if (searchId == cur->course.courseId) {
            // Return the Course object
            return cur->course;
        }
            // searchId is greater than the courseId in the current Node, go right
        else if (searchId > cur->course.courseId) {
            cur = cur->right;
        }
            // searchId is less than the courseId in the current Node, go left
        else {
            cur = cur->left;
        }
    }
    // If we get here, we did not find a matching courseId, so return a default Course object
    return {};
}

// Insert a Course object into the BST
void BinarySearchTree::Insert(Course aCourse) {
    Node* newNode = new Node(aCourse);
    // If the tree is empty, set the root to the new Node
    if (root == nullptr) {
        root = newNode;
    }
        // Else, follow BST rules to insert newNode. Start searching for insertion point at the root.
    else {
        Node* cur = root;
        while (cur != nullptr) {
            // newCourse's courseId is less than the current Node's courseId, go left
            if (newNode->course.courseId < cur->course.courseId) {
                // if the left node of cur is empty, insert newNode here
                if (cur->left == nullptr) {
                    cur->left = newNode;
                    // Set exit condition
                    cur = nullptr;
                }
                    // Otherwise, move on to check the left node
                else {
                    cur = cur->left;
                }
            }
                // else, go right
            else {
                if (cur->right == nullptr) {
                    cur->right = newNode;
                    cur = nullptr;
                }
                else {
                    cur = cur->right;
                }
            }
        }
    }
}

// Remove a Course from the BST given a courseId
void BinarySearchTree::Remove(string searchId) {
    /*
     * For removal purposes, we keep track of the parent Node of the currently searched Node.
     * Initialize parent Node as nullptr.
     */
    Node* parent = nullptr;
    // Start searching at the root Node
    Node* cur = root;
    while (cur != nullptr) {
        // If we find a matching courseId, there are four removal cases to handle
        if (searchId == cur->course.courseId) {
            // CASE 1: Matching Node is a leaf
            if (cur->left == nullptr && cur->right == nullptr) {
                // If the matching Node is the root
                if (parent == nullptr) {
                    root = nullptr;
                }
                    // The matching Node is the left child of the parent Node
                else if (parent->left == cur) {
                    parent->left = nullptr;
                }
                    // The matching Node is the right child of the parent Node
                else {
                    parent->right = nullptr;
                }
            }
                // CASE 2: Matching Node has only a left child
            else if (cur->right == nullptr) {
                if (parent == nullptr) {
                    root = cur->left;
                }
                else if (parent->left == cur) {
                    parent->left = cur->left;
                }
                else {
                    parent->right = cur->left;
                }
            }
                // CASE 3: Matching Node has only a right child
            else if (cur->left == nullptr) {
                if (parent == nullptr) {
                    root = cur->right;
                }
                else if (parent->left == cur) {
                    parent->left = cur->right;
                }
                else {
                    parent->right = cur->right;
                }
            }
                /*
                 * CASE 4: Matching Node has two children.
                 * In this case, we must find the "successor Node" to the one being removed.
                 * The successor Node is the leftmost child of the right subtree.
                 */
            else {
                Node* successor = cur->right;
                while (successor->left != nullptr) {
                    successor = successor->left;
                }
                // Store the Course object of the successor Node
                Course sucCourse = successor->course;
                // Recursively remove the successor Node
                Remove(successor->course.courseId);
                // Replace the current Node's Course object with sucCourse
                cur->course = sucCourse;
            }
            // Exit the function once the Node has been deleted
            return;
        }
            // Search right subtree
        else if (searchId > cur->course.courseId) {
            parent = cur;
            cur = cur->right;
        }
            // Search left subtree
        else {
            parent = cur;
            cur = cur->left;
        }
    }
    // If we reach this point, no matching courseId was found in any Node
}

// Print information from a Course object
void BinarySearchTree::PrintCourse(Course course) {
    // If the course parameter is not the default Course object, print the course information
    if (course.courseId != "-1") {
        cout << endl << "ID: " << course.courseId << endl;
        cout << "Name: " << course.courseName << endl;
        // If the prereqCourses vector is not empty, print the prerequisite courses
        if (!course.prereqCourses.empty()) {
            cout << "Prerequisites: ";
            for (int i = 0; i < course.prereqCourses.size(); i++) {
                // avoid placing unnecessary separator commas
                if (i != course.prereqCourses.size() - 1) {
                    cout << course.prereqCourses[i] << ", ";
                }
                    // This happens when the last prerequisite course is to be printed
                else {
                    cout << course.prereqCourses[i] << endl;
                }
            }
        }
    }
        // If the course parameter is the default Course object
    else {
        cout << endl << "ERROR: Course not found." << endl;
    }
}

// Traverse the BST in order based on courseId value starting from parameter node
void BinarySearchTree::InOrder(Node* node) {
    // If the node argument is null, exit the function
    if (node == nullptr) {
        return;
    }
    // Recurse down the left subtree
    InOrder(node->left);
    PrintCourse(node->course);
    InOrder(node->right);
}

// Call InOrder function above on the root Node
void BinarySearchTree::InOrder() {
    InOrder(root);
}

// Create Course object from file information and load them into a BST - return the BST object
BinarySearchTree loadCourses(BinarySearchTree& bst, string filePath) {
    // Read course information to line vector
    vector<string> lines = loadFile(filePath);
    // Gather all valid course IDs for later validation when inserting prerequisite courses
    vector<string> validIds = loadValidCourseIds(filePath);
    for (const string& line : lines) {
        // Split the course information string
        vector<string> splitLine = split(line, ",");
        // Course with no prerequisites
        if (splitLine.size() == 2) {
            Course newCourse;
            // First element in splitLine is the course ID
            newCourse.courseId = splitLine[0];
            // Second element in splitLine is the course name
            newCourse.courseName = splitLine[1];
            // Insert the Course object into the BST
            bst.Insert(newCourse);
        }
            // Course with one or more prerequisites
        else if (splitLine.size() > 2) {
            Course newCourse;
            newCourse.courseId = splitLine[0];
            newCourse.courseName = splitLine[1];
            /*
             * Every element past the first two is a prerequisite
             * Iterate through these elements, verify that the course ID is valid
             * and if valid, add the course ID to the prereqCourses vector.
             */
            for (int i = 2; i <splitLine.size(); i++) {
                bool valid = false;
                for (const string& validId : validIds) {
                    if (splitLine[i] == validId) {
                        valid = true;
                    }
                }
                if (valid) {
                    newCourse.prereqCourses.push_back(splitLine[i]);
                }
            }
            bst.Insert(newCourse);
        }
    }
    return bst;
}

void displayMenu() {
    cout << endl << "Menu: " << endl;
    cout << "1. Load Courses" << endl;
    cout << "2. Print Course List" << endl;
    cout << "3. Print Course Information" << endl;
    cout << "9. Exit" << endl << endl;
}

int main() {
    BinarySearchTree bst;
    string searchId;
    Course searchCourse;
    int userChoice = 0;

    // Loop until user provides input to exit program.
    while (userChoice != 9) {
        displayMenu();
        cout << "Enter choice: ";
        cin >> userChoice;

        /*
         * This is needed if the user inputs a string to avoid infinite looping.
         * Re-prompt user until an integer is provided as input.
         */
        while (cin.fail()) {
            cout << endl << "Invalid input. Try again" << endl << endl;
            // Clear the error
            cin.clear();
            // Ignore the entire input buffer (up to the maximum size until a newline is found)
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            // Re-prompt user for input
            cout << "Enter choice: ";
            cin >> userChoice;
        }

        // Match user input to a specific case
        switch (userChoice) {
            case 1: // Load course information into the BST
                loadCourses(bst, INPUT_FILE);
                break;
            case 2: // Print all course information in alphanumeric order
                bst.InOrder();
                break;
            case 3: // Print information about a single course given a courseId
                // Ask the user for the course ID
                cout << endl << "Enter Course ID: ";
                cin >> searchId;
                // Search for the course and store result in Course object
                searchCourse = bst.Search(searchId);
                // Call PrintCourse function on the Course object
                bst.PrintCourse(searchCourse);
                break;
            case 9: // Exit
                cout << endl << "Goodbye!" << endl;
                break;
            default: // Any other input
                cout << endl << userChoice << " is not a valid choice." << endl;
                break;
        }
    }
    return 0;
}
