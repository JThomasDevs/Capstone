#[derive(Debug, Clone)]
struct Course {
    course_id: String,
    course_name: String,
    prereqs: Vec<String>,
}

#[derive(Debug)]
struct Node {
    course: Course,
    left: Option<Box<Node>>,
    right: Option<Box<Node>>,
    height: i32,
}

struct AVLTree {
    root: Option<Box<Node>>,
}

// Enum to specify which child
enum ChildSide {
    Left,
    Right,
}

impl AVLTree {
    pub fn new() -> Self {
        AVLTree { root: None }
    }

    pub fn insert(&mut self, course: Course) {
        // To be implemented
    }

    pub fn remove(&mut self, course_id: &str) {
        // To be implemented
    }

    pub fn search(&self, course_id: &str) -> Option<&Course> {
        // To be implemented
        None
    }

    pub fn in_order_traversal(&self) {
        // To be implemented
    }

    // --- AVL Helper Function Signatures ---
    fn height(node: &Option<Box<Node>>) -> i32 {
        // To be implemented
        0
    }

    fn balance_factor(node: &Option<Box<Node>>) -> i32 {
        // To be implemented
        0
    }

    fn rotate_left(z: Box<Node>) -> Box<Node> {
        // To be implemented
        z
    }

    fn rotate_right(mut root: Box<Node>) -> Box<Node> {
        match root.left.take() {
            Some(mut left_child) => {
                // Save left_child's right subtree
                let left_right_child = left_child.right.take();
                // Set root as right child of left_child
                Self::set_child(&mut left_child, ChildSide::Right, Some(root));
                // Set left_right_child as left child of the new right child (which is the old root)
                if let Some(ref mut right_child) = left_child.right {
                    Self::set_child(right_child, ChildSide::Left, left_right_child);
                }
                left_child
            },
            None => root, // If no left child, cannot rotate; return original root
        }
    }

    fn update_height(node: &mut Box<Node>) {
        let left_height = match node.left {
            Some(ref left_child) => left_child.height,
            None => -1,
        };
        let right_height = match node.right {
            Some(ref right_child) => right_child.height,
            None => -1,
        };
        node.height = std::cmp::max(left_height, right_height) + 1;
    }

    // Set the left or right child of a parent node using ChildSide enum
    fn set_child(parent: &mut Box<Node>, side: ChildSide, child: Option<Box<Node>>) -> bool {
        match side {
            ChildSide::Left => parent.left = child,
            ChildSide::Right => parent.right = child,
        }
        Self::update_height(parent);
        true
    }

    // Replace a parent's child (left or right) with a new child using ChildSide enum
    fn replace_child(parent_node: &mut Box<Node>, side: ChildSide, replacement_child: Option<Box<Node>>) -> bool {
        Self::set_child(parent_node, side, replacement_child)
    }

    // Get the balance factor of a node (using match statements)
    fn get_balance(node: &Node) -> i32 {
        let left_height = match node.left {
            Some(ref left) => left.height,
            None => -1,
        };
        let right_height = match node.right {
            Some(ref right) => right.height,
            None => -1,
        };
        left_height - right_height
    }

    // Rebalance a node and return the new subtree root
    fn rebalance(mut node: Box<Node>) -> Box<Node> {
        Self::update_height(&mut node);
        let balance = Self::get_balance(&node);

        if balance == -2 {
            // Right heavy
            if let Some(ref right_child) = node.right {
                if Self::get_balance(right_child) == 1 {
                    // Double rotation: Right-Left
                    if let Some(right) = node.right.take() {
                        node.right = Some(Self::rotate_right(right));
                    }
                }
            }
            return Self::rotate_left(node);
        } else if balance == 2 {
            // Left heavy
            if let Some(ref left_child) = node.left {
                if Self::get_balance(left_child) == -1 {
                    // Double rotation: Left-Right
                    if let Some(left) = node.left.take() {
                        node.left = Some(Self::rotate_left(left));
                    }
                }
            }
            return Self::rotate_right(node);
        }
        node
    }
}

fn main() {
    // Entry point for the program
}
