use std::fs::File;
use std::io::Write;
use std::io::{self, BufRead, BufReader};
use std::collections::HashSet;

/// Loads courses from a CSV file, validates prerequisites, and inserts them into the AVL tree.
///
/// # Arguments
/// * `tree` - The AVLTree to insert courses into.
/// * `filename` - The path to the CSV file containing course data.
///
/// Returns an io::Result indicating success or failure.
pub fn load_courses_from_file(tree: &mut AVLTree, filename: &str) -> io::Result<()> {
    // Open the file and read the lines into the 'lines' vector
    let file = File::open(filename)?;
    let reader = BufReader::new(file);
    let mut lines: Vec<String> = Vec::new();

    // trim whitespace from the lines and skip empty lines
    for line in reader.lines() {
        let line = line?;
        if !line.trim().is_empty() {
            lines.push(line);
        }
    }

    // Gather all valid course IDs for later prerequisite validation
    let valid_ids: HashSet<String> = lines
        .iter()
        .filter_map(|line| {
            let id = line.split(',').next().unwrap_or("").trim();
            if !id.is_empty() {
                Some(id.to_string())
            } else {
                None
            }
        })
        .collect();

    // Iterate through each line in the 'lines' vector
    for line in lines {
        let split_line: Vec<&str> = line.split(',').collect();
        // If the line has two elements, create a new Course with the first element as the Course ID and the second element as the Course name and no prerequisites
        if split_line.len() == 2 {
            let course = Course {
                course_id: split_line[0].to_string(),
                course_name: split_line[1].to_string(),
                prereqs: Vec::new(),
            };
            tree.insert(course);
        } 
        // If the line has more than two elements, create a new Course with the first element as the Course ID and the second element as the Course name and the rest of the elements as prerequisites
        else if split_line.len() > 2 {
            let mut course = Course {
                course_id: split_line[0].to_string(),
                course_name: split_line[1].to_string(),
                prereqs: Vec::new(),
            };
            for prereq in &split_line[2..] {
                // If the prerequisite is valid, add it to the Course's prerequisites
                if valid_ids.contains(*prereq) { // dereference the &&str to a &str
                    course.prereqs.push(prereq.to_string());
                }
            }
            // Insert the new Course into the AVL tree
            tree.insert(course);
        }
    }
    // Return Ok(()) to indicate success
    Ok(())
}

/// Prints detailed information about a course if it exists, or an error if not.
///
/// # Arguments
/// * `course` - An Option containing a reference to the course to print. If None, prints an error message.
fn print_course(course: Option<&Course>) {
    // Match against the Option enum to print the course information or an error message
    match course {
        // If the Course is Some, print the course information
        Some(course) => {
            println!("\nID: {}", course.course_id); // print the course ID
            println!("Name: {}", course.course_name); // print the course name
            if !course.prereqs.is_empty() { // if the course has prerequisites
                print!("Prerequisites: "); // print the prerequisites
                for (i, prereq) in course.prereqs.iter().enumerate() {
                    if i != course.prereqs.len() - 1 {
                        print!("{}, ", prereq);
                    } else {
                        println!("{}", prereq);
                    }
                }
            }
        }
        // If the Course is None, print an error message
        None => {
            println!("\nERROR: Course not found.");
        }
    }
}

/// Prints the main menu to the console.
fn print_menu() {
    println!("\nMenu:");
    println!("1. Load Courses");
    println!("2. Print Course List");
    println!("3. Print Course Information");
    println!("9. Exit\n");
}

/// Entry point for the program. Handles user interaction and menu logic.
fn main() {
    // Create a new AVL tree
    let mut tree = AVLTree::new();
    // Initialize the user_choice and input variables
    let mut user_choice = String::new();
    let mut input = String::new();
    // Get the standard input stream
    let stdin = std::io::stdin();
    // The path to the CSV file containing the course data
    const INPUT_FILE: &str = "CourseInput.csv";

    // Loop until the user chooses to exit
    loop {
        // Prompt the user
        print_menu();
        print!("Enter choice: ");
        std::io::stdout().flush().unwrap();
        // Clear the user_choice variable
        user_choice.clear();
        // Read the user's menu choice and trim any whitespace
        stdin.read_line(&mut user_choice).expect("Failed to read line");
        let user_choice = user_choice.trim();

        // Match against the user's menu choice
        match user_choice {
            // The user selected "1"
            "1" => {
                /* Load the courses from the CSV file into the AVL tree. 
                 * This method returns an io::Result enum, which is either Ok(()) or
                 * Err(e). Then match against this Result to print a success or error message.*/
                match load_courses_from_file(&mut tree, INPUT_FILE) {
                    Ok(_) => println!("Courses loaded successfully."),
                    Err(e) => println!("Failed to load courses: {}", e),
                }
            }
            // The user selected "2"
            "2" => {
                // Print the course list in alphabetical order using tree in order traversal
                tree.in_order_traversal();
            }
            // The user selected "3"
            "3" => {
                // Prompt the user for a course ID
                print!("\nEnter Course ID: ");
                std::io::stdout().flush().unwrap();
                // Clear the input variable
                input.clear();
                // Read the user's course ID and trim any whitespace
                stdin.read_line(&mut input).expect("Failed to read line");
                let search_id = input.trim();
                // Match against the course ID to print the course information or an error message
                match tree.search_node(search_id) {
                    Some(node) => print_course(Some(&node.course)),
                    None => print_course(None),
                }
            }
            // The user selected "9"
            "9" => {
                // Print a goodbye message and break the loop to exit the program
                println!("\nGoodbye!");
                break;
            }
            // The user selected an invalid choice
            _ => { // _ is a wildcard pattern that matches any value not caught by the previous cases
                // Print an error message for an invalid choice
                println!("\n{} is not a valid choice.", user_choice);
            }
        }
    }
}

/// Represents a course with an ID, name, and prerequisites.
pub struct Course {
    /// Unique identifier for the course.
    course_id: String,
    /// Name of the course.
    course_name: String,
    /// List of prerequisite course IDs.
    prereqs: Vec<String>,
}

/// Node in the AVL tree, containing a Course and child pointers.
pub struct Node {
    /// The Course stored in this node.
    course: Course,
    /// Left child node.
    left: Option<Box<Node>>,
    /// Right child node.
    right: Option<Box<Node>>,
    /// Height of the node in the tree.
    height: i32,
}

/// AVL tree for storing courses, self-balancing on insertions and deletions.
pub struct AVLTree {
    /// Root node of the AVL tree.
    root: Option<Box<Node>>,
}

/// Enum to specify which child (left or right) to operate on.
enum ChildSide {
    // This enum negates the need for passing a &str to set_child() to indicate the child side
    Left,
    Right,
}

impl AVLTree {
    /// Creates a new, empty AVL tree.
    ///
    /// # Returns
    /// * `AVLTree` - A new, empty AVL tree.
    pub fn new() -> Self {
        // Initialize the root to None
        AVLTree { root: None }
    }

    /// Inserts a course into the AVL tree.
    ///
    /// # Arguments
    /// * `course` - The Course to insert.
    pub fn insert(&mut self, course: Course) {
        // Create a new node with the Course, left and right children set to None, and height set to 0
        let new_node = Box::new(Node {
            course,
            left: None,
            right: None,
            height: 0,
        });
        // Insert the new node into the AVL tree
        self.root = Self::insert_node(self.root.take(), new_node);
    }

    /// Removes a node from the AVL tree by course ID, using the recursive approach from the root.
    ///
    /// # Arguments
    /// * `course_id` - The ID of the course to remove.
    pub fn remove(&mut self, course_id: &str) {
        // Remove the node from the AVL tree
        /* This method returns an Option<Box<Node>>, which represents the new tree root
         * after the target node is removed. */
        self.root = Self::remove_node(self.root.take(), course_id);
    }

    /// Searches for a node in the AVL tree by Course ID, starting at the root.
    /// Returns a reference to the node if found, or None otherwise.
    ///
    /// # Arguments
    /// * `course_id` - The ID of the course to search for.
    ///
    /// # Returns
    /// * `Option<&Node>` - Some reference to the node if found, or None if not found.
    pub fn search_node(&self, course_id: &str) -> Option<&Node> {
        // Convert the Course ID to lowercase for case-insensitive comparison
        let search_id = course_id.to_lowercase();
        // Start at the root of the tree
        let mut current = self.root.as_ref();
        // Traverse the tree until the Course is found or the end of the tree is reached
        while let Some(node) = current {
            // Convert the Course ID to lowercase for case-insensitive comparison
            let node_id = node.course.course_id.to_lowercase();
            // If the search ID is less than the node ID, go left
            if search_id < node_id {
                current = node.left.as_ref();
            } 
            // If the search ID is greater than the node ID, go right
            else if search_id > node_id {
                current = node.right.as_ref();
            } 
            // If the search ID is equal to the node ID, return the node and end the loop
            else {
                return Some(node);
            }
        }
        // If the Course is not found, return None
        None
    }

    /// Performs a recursive in-order traversal of the AVL tree and prints courses.
    pub fn in_order_traversal(&self) {
        // Helper function to traverse the tree recursively
        fn traverse(node: &Option<Box<Node>>) {
            // If the node is None, return
            let Some(cur) = node else { return };
            // Traverse the left child, print the course, and traverse the right child
            traverse(&cur.left); // keep going left until the left child is None
            print_course(Some(&cur.course)); // print the course
            traverse(&cur.right); // keep going right until the right child is None
        }
        // Start the traversal at the tree root
        traverse(&self.root);
    }

    /// Returns the height of a node, or -1 if the node is None.
    ///
    /// # Arguments
    /// * `node` - The node to get the height of.
    ///
    /// # Returns
    /// * `i32` - The height of the node, or -1 if the node is None.
    fn height(node: &Option<Box<Node>>) -> i32 {
        match node {
            Some(n) => n.height,
            None => -1,
        }
    }

    // --- Insertion Helper ---
    /// Recursively inserts a node into the subtree, rebalancing as needed.
    /// Returns the new subtree root after insertion and rebalancing.
    ///
    /// # Arguments
    /// * `subtree` - The subtree to insert into.
    /// * `node_to_insert` - The node to insert.
    ///
    /// # Returns
    /// * `Option<Box<Node>>` - The new subtree root after insertion and rebalancing.
    fn insert_node(subtree: Option<Box<Node>>, node_to_insert: Box<Node>) -> Option<Box<Node>> {
        // Match against the subtree Node
        match subtree {
            // If the current_node is Some, compare the Course ID of the node to insert to the Course ID of the current node
            Some(mut current_node) => {
                // If the Course ID of the node to insert is less than the Course ID of the current node, go left
                if node_to_insert.course.course_id < current_node.course.course_id {
                    current_node.left = Self::insert_node(current_node.left.take(), node_to_insert);
                } 
                // If the Course ID of the node to insert is greater than the Course ID of the current node, go right
                else if node_to_insert.course.course_id > current_node.course.course_id {
                    current_node.right = Self::insert_node(current_node.right.take(), node_to_insert);
                } 
                // If the Course ID of the node to insert is equal to the Course ID of the current node, return the current node
                else {
                    return Some(current_node); // return the current node
                }
                // Rebalance the tree and return the new subtree root
                Some(Self::rebalance(current_node))
            }
            // If the current_node is None, return the node to insert
            None => {
                Some(node_to_insert)
            }
        }
    }

    // --- Removal Helpers ---
    /// Recursively removes a node by course ID and rebalances the tree.
    /// Returns the new subtree root after removal and rebalancing.
    ///
    /// # Arguments
    /// * `subtree` - The subtree to remove from.
    /// * `course_id` - The ID of the course to remove.
    ///
    /// # Returns
    /// * `Option<Box<Node>>` - The new subtree root after removal and rebalancing.
    fn remove_node(subtree: Option<Box<Node>>, course_id: &str) -> Option<Box<Node>> {
        // Match against the subtree Node
        match subtree {
            /* The passed subtree root node is Some */
            // If the course ID is less than the node's course ID, go left
            Some(mut node) if course_id < &node.course.course_id => {
                // Recursively call remove_node on the left child
                node.left = Self::remove_node(node.left.take(), course_id);
                // Rebalance the tree and return the new subtree root
                Some(Self::rebalance(node))
            }
            // If the course ID is greater than the node's course ID, go right
            Some(mut node) if course_id > &node.course.course_id => {
                // Recursively call remove_node on the right child
                node.right = Self::remove_node(node.right.take(), course_id);
                // Rebalance the tree and return the new subtree root
                Some(Self::rebalance(node))
            }
            // If the course ID is equal to the node's course ID, remove the node
            Some(mut node) => 
            // Determine the successor node to replace the removed node based on the number of children
            match (node.left.take(), node.right.take()) {
                // If the node has no children, return None
                (None, None) => None,
                // If the node has only a left child, return the left child
                (Some(left), None) => Some(left),
                // If the node has only a right child, return the right child
                (None, Some(right)) => Some(right),
                // If the node has two children, find the in-order successor and return the new subtree root
                (Some(_), Some(right)) => {
                    // Find the in-order successor
                    let (successor, new_right) = Self::find_successor(right);
                    // Set the left child of the successor to the left child of the node
                    let mut successor = successor;
                    // Set the right child of the successor to the right child of the node
                    successor.right = new_right;
                    // Rebalance the tree and return the new subtree root
                    Some(Self::rebalance(successor))
                }
            },
            // If the subtree root node is None, return None
            None => None,
        }
    }

    /// Helper to find the in-order successor node to a node with two children
    ///
    /// # Arguments
    /// * `node` - The right child subtree to search for the successor.
    ///
    /// # Returns
    /// * `(Box<Node>, Option<Box<Node>>)` - The successor node and the new subtree root after removal.
    fn find_successor(mut node: Box<Node>) -> (Box<Node>, Option<Box<Node>>) {
        // Match against the left child of the node
        match node.left.take() {
            // If the left child is Some, recursively call find_successor on the left child
            Some(left) => {
                // Recursively call find_successor on the left child
                let (successor, new_left) = Self::find_successor(left);
                // Set the left child of the node to the new left child
                node.left = new_left;
                // Rebalance the tree and return the new subtree root
                (successor, Some(Self::rebalance(node)))
            }
            // If the left child is None, return the node and its right child
            None => {
                // Set the right child of the node to the right child of the node
                let right = node.right.take();
                // Return the node and its right child
                (node, right)
            }
        }
    }

    // --- AVL Balancing Helpers ---
    /// Rebalances a node and returns the new subtree root.
    /// This function updates the node's height, checks its balance factor, and performs
    /// the appropriate single or double rotation to maintain AVL balance.
    ///
    /// # Arguments
    /// * `node` - The node to rebalance.
    ///
    /// # Returns
    /// * `Box<Node>` - The new subtree root after rebalancing.
    fn rebalance(mut node: Box<Node>) -> Box<Node> {
        // Update the height of the node
        Self::update_height(&mut node);
        // Get the balance factor of the node
        let balance = Self::get_balance(&node);
        // If the balance factor is -2, the node is right heavy
        if balance == -2 {
            // If the right child is Some and the balance factor of the right child is 1, perform a double rotation: Right-Left
            if let Some(ref right_child) = node.right {
                if Self::get_balance(right_child) == 1 {
                    // If the right child is Some, perform a right rotation on the right child
                    if let Some(right) = node.right.take() {
                        // Set the right child of the node to the new right child
                        node.right = Some(Self::rotate_right(right));
                    }
                }
            }
            return Self::rotate_left(node);
        } else if balance == 2 {
            // If the balance factor is 2, the node is left heavy
            if let Some(ref left_child) = node.left {
                // If the balance factor of the left child is -1, perform a double rotation: Left-Right
                if Self::get_balance(left_child) == -1 {
                    // If the left child is Some, perform a left rotation on the left child
                    if let Some(left) = node.left.take() {
                        // Set the left child of the node to the new left child
                        node.left = Some(Self::rotate_left(left));
                    }
                }
            }
            // Perform a right rotation on the node
            return Self::rotate_right(node);
        }
        // Return the node
        node
    }

    /// Returns the balance factor of a node (left height - right height) using match statements.
    ///
    /// # Arguments
    /// * `node` - The node to get the balance factor of.
    ///
    /// # Returns
    /// * `i32` - The balance factor of the node.
    fn get_balance(node: &Node) -> i32 {
        // Get the height of the left child
        let left_height = Self::height(&node.left);
        // Get the height of the right child
        let right_height = Self::height(&node.right);
        // Return the difference between the left and right heights
        left_height - right_height
    }

    /// Updates the height of a node based on the heights of its children.
    ///
    /// # Arguments
    /// * `node` - The node to update the height of.
    fn update_height(node: &mut Box<Node>) {
        // Get the height of the left child
        let left_height = match node.left {
            Some(ref left_child) => left_child.height,
            None => -1,
        };
        // Get the height of the right child
        let right_height = match node.right {
            Some(ref right_child) => right_child.height,
            None => -1,
        };
        // Set the height of the node to the maximum of the left and right heights plus 1
        node.height = std::cmp::max(left_height, right_height) + 1;
    }

    // --- Rotation Helpers ---
    /// Performs a left rotation on the given node and returns the new subtree root.
    ///
    /// # Arguments
    /// * `root` - The node to perform a left rotation on.
    ///
    /// # Returns
    /// * `Box<Node>` - The new subtree root after the left rotation.
    fn rotate_left(mut root: Box<Node>) -> Box<Node> {
        // Match against the right child of the root
        match root.right.take() {
            // If the right child is Some, perform a left rotation
            Some(mut right_child) => {
                // Save right_child's left subtree
                let right_left_child = right_child.left.take(); // save the left subtree of the right child
                // Set the root as the left child of the right child
                Self::set_child(&mut right_child, ChildSide::Left, Some(root));
                // Set the right left child as the right child of the new left child (which is the old root)
                if let Some(ref mut left_child) = right_child.left { // if the left child of the right child is Some
                    Self::set_child(left_child, ChildSide::Right, right_left_child); // set the right left child as the right child of the left child
                }
                // Return the right child
                right_child
            },
            // If the right child is None, return the original root
            None => root,
        }
    }

    /// Performs a right rotation on the given node and returns the new subtree root.
    /// If the left child is None, returns the original root.
    ///
    /// # Arguments
    /// * `root` - The node to perform a right rotation on.
    ///
    /// # Returns
    /// * `Box<Node>` - The new subtree root after the right rotation.
    fn rotate_right(mut root: Box<Node>) -> Box<Node> {
        // Match against the left child of the root
        match root.left.take() {
            // If the left child is Some, perform a right rotation
            Some(mut left_child) => {
                // Save left_child's right subtree
                let left_right_child = left_child.right.take(); // save the right subtree of the left child
                // Set the root as the right child of the left child
                Self::set_child(&mut left_child, ChildSide::Right, Some(root));
                // Set the left right child as the left child of the new right child (which is the old root)
                if let Some(ref mut right_child) = left_child.right { // if the right child of the left child is Some
                    Self::set_child(right_child, ChildSide::Left, left_right_child); // set the left right child as the left child of the right child
                }
                // Return the left child
                left_child
            },
            // If the left child is None, return the original root
            None => root,
        }
    }

    // --- Child Management Helpers ---
    /// Sets the left or right child of a parent node using the ChildSide enum.
    /// Updates the parent's height after setting the child.
    /// 
    /// # Arguments
    /// * `parent` - The parent node to set the child of.
    /// * `side` - The side of the parent to set the child of.
    /// * `child` - The child node to set.
    fn set_child(parent: &mut Box<Node>, side: ChildSide, child: Option<Box<Node>>) {
        // Match against the side
        match side {
            // If the side is Left, set the left child of the parent to the child
            ChildSide::Left => parent.left = child,
            // If the side is Right, set the right child of the parent to the child
            ChildSide::Right => parent.right = child,
        }
        // Update the height of the parent
        Self::update_height(parent);
    }
}