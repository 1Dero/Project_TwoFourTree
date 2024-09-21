/*
Darel Gomez
COP 3503
 */



public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;

        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the node is a non-leaf.
        TwoFourTreeItem rightChild = null;
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            return values == 1;
        }

        public boolean isThreeNode() {
            return values == 2;
        }

        public boolean isFourNode() {
            return values == 3;
        }

        public boolean isRoot() {
            return (parent == null);
        }

        public void setIsLeaf() {
            isLeaf = (leftChild == null)
                    && (rightChild == null)
                    && (centerChild == null)
                    && (centerLeftChild == null)
                    && (centerRightChild == null);
        }

        public boolean contains(int value) {
            return switch (values) {
                case 1 -> (value == value1);
                case 2 -> (value == value1) || (value == value2);
                case 3 -> (value == value1) || (value == value2) || (value == value3);
                default -> throw new ArithmeticException("Count values better");
            };
        }

        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
        }

        public TwoFourTreeItem(int value1, int value2) {
            values = 2;
            if(value1 < value2) {
                this.value1 = value1;
                this.value2 = value2;
            }
            else {
                this.value1 = value2;
                this.value2 = value1;
            }
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            values = 3;
            if(value1 < value2) {
                if(value1 < value3) {
                    this.value1 = value1;
                    if(value2 < value3) {
                        this.value2 = value2;
                        this.value3 = value3;
                    }
                    else {
                        this.value2 = value3;
                        this.value3 = value2;
                    }
                }
                else {
                    this.value1 = value3;
                    this.value2 = value1;
                    this.value3 = value2;
                }
            }
            else if(value2 < value3) {
                this.value1 = value2;
                if(value1 < value3) {
                    this.value2 = value1;
                    this.value3 = value3;
                }
                else {
                    this.value2 = value3;
                    this.value3 = value1;
                }
            }
            else {
                this.value1 = value3;
                this.value2 = value2;
                this.value1 = value1;
            }
        }

        public boolean addChild(TwoFourTreeItem child) {
            if(child == null) return true;
            if(isLeaf) isLeaf = false;

            child.parent = this;
            switch(values) {
                case 1: {
                    if(child.value1 < value1) {
                        if(leftChild == null) leftChild = child;
                        else return false;
                    }
                    else if(rightChild == null) rightChild = child;
                    else return false;
                    break;
                }
                case 2: {
                    if(child.value1 < value1) {
                        if(leftChild == null) leftChild = child;
                        else return false;
                    }
                    else if(child.value1 < value2) {
                        if(centerChild == null) centerChild = child;
                        else return false;
                    }
                    else if(rightChild == null) rightChild = child;
                    else return false;
                    break;
                }
                case 3: {
                    if(child.value1 < value1) {
                        if(leftChild == null) leftChild = child;
                        else return false;
                    }
                    else if(child.value1 < value2) {
                        if(centerLeftChild == null) centerLeftChild = child;
                        else return false;
                    }
                    else if(child.value1 < value3) {
                        if(centerRightChild == null) centerRightChild = child;
                        else return false;
                    }
                    else if(rightChild == null) rightChild = child;
                    else return false;
                    break;
                }
                default: {
                    throw new ArithmeticException("Count values better");
                }
            }
            return true;
        }

        private void printIndents(int indent) {
            for (int i = 0; i < indent; i++) System.out.print("  ");
        }

        public boolean delete() {
            if(values != 0) return false;

            if(this.isRoot()) return true;

            if(this.parent.leftChild == this) this.parent.leftChild = null;
            else if (this.parent.centerLeftChild == this) this.parent.centerLeftChild = null;
            else if (this.parent.centerChild == this) this.parent.centerChild = null;
            else if (this.parent.centerRightChild == this) this.parent.centerRightChild = null;
            else if (this.parent.rightChild == this) this.parent.rightChild = null;
            else throw new IllegalStateException("target is not a child of the parent somehow?");

            this.parent = null;
            this.setIsLeaf();


            return true;
        }

        // For debugging
        public boolean checkValidNode() {
            if(isTwoNode()) {
                if((centerLeftChild == null) && (centerChild == null) && (centerRightChild == null)) {
                    if(leftChild != null && leftChild.value1 >= value1) return false;
                    if(rightChild != null && rightChild.value1 <= value1) return false;
                }
                else return false;
            }
            else if(isThreeNode()) {
                if((centerLeftChild == null) && (centerRightChild == null)) {
                    if(leftChild != null && leftChild.value1 >= value1) return false;
                    if(centerChild != null && (centerChild.value1 <= value1 || centerChild.value1 >= value2)) return false;
                    if(rightChild != null && rightChild.value1 <= value2) return false;
                }
                else return false;
            }
            else if(isFourNode()) {
                if(centerChild == null) {
                    if(leftChild != null && leftChild.value1 >= value1) return false;
                    if(centerLeftChild != null && (centerLeftChild.value1 <= value1 || centerLeftChild.value1 >= value2)) return false;
                    if(centerRightChild != null && (centerRightChild.value1 <= value2 || centerRightChild.value1 >= value3)) return false;
                    if(rightChild != null && rightChild.value1 <= value3) return false;
                }
                else return false;
            }
            else return false;

            return true;
        }

        public void printInOrder(int indent) {
            if (!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf) rightChild.printInOrder(indent + 1);
        }

        public String toString() {
            return switch (values) {
                case 1 -> String.format("[%d]", value1);
                case 2 -> String.format("[%d, %d]", value1, value2);
                case 3 -> String.format("[%d, %d, %d]", value1, value2, value3);
                default -> "[]";
            };
        }

        // Just something I use to replace target value with successor in deletion process
        public boolean replace(int value, int newValue) {
            if(value1 == value) value1 = newValue;
            else if(value2 == value) value2 = newValue;
            else if(value3 == value) value3 = newValue;
            else return false;

            return true;
        }
    }

    TwoFourTreeItem root = null;

    // Returns true on successful operation
    public boolean addValue(int value) {
        //System.out.println("------------------\nAdding " + value);

        if(root == null) {
            root = new TwoFourTreeItem(value);
            // printInOrder();
            return true;
        }
        else {
            TwoFourTreeItem walker = root;

            while(walker != null) {
                // if(!walker.checkValidNode()) throw new IllegalStateException("Not valid node");

                if(walker.contains(value)) return true;
                //System.out.printf("walker = %s, walker.parent = %s, \n", walker, walker.parent);
                if(walker.isFourNode()) {
                    //System.out.printf("Splitting %s...\n", walker);
                    walker = split(walker);
                    //printInOrder();
                }
                if(walker.isLeaf) {
                    if(hasValue(value)) System.out.println("\n---------WTF--------\n");
                    if(!insertValueToNode(walker, value)) {
                        throw new IllegalStateException("Adding to a node that is full");
                    }
                    //printInOrder();
                    return true;
                }
                walker = traverse(walker, value);
            }
        }
        return false;
    }

    // Pre-condition: Trying to find which children contains the interval of value
    // Post-condition: Returns the child which contains the interval of value
    private TwoFourTreeItem traverse(TwoFourTreeItem node, int value) {
        // if(!node.checkValidNode()) throw new IllegalStateException("Not a valid node");

        if(node.isTwoNode()) {
            if(value < node.value1) return node.leftChild;
            else if(value > node.value1) return node.rightChild;
            else throw new IllegalStateException("Should not be checking if value is in node");
        }
        else if(node.isThreeNode()) {
            if(value < node.value1) return node.leftChild;
            else if(value < node.value2) return node.centerChild;
            else if(value > node.value3) return node.rightChild;
            else throw new IllegalStateException("Should not be checking if value is in node");
        }
        else if(node.isFourNode()) {
            if(value < node.value1) return node.leftChild;
            else if(value < node.value2) return node.centerLeftChild;
            else if(value < node.value3) return node.centerRightChild;
            else if(value > node.value3) return node.rightChild;
            else throw new IllegalStateException("Should not be checking if value is in node");
        }
        else throw new IllegalStateException("Not a 2,3, or 4 node");
    }

    // Pre-condition: node is a 4-node and was found during insertion process
    // Post-condition: node is split
    private TwoFourTreeItem split(TwoFourTreeItem node) {
        int value = node.value2;

        TwoFourTreeItem left = new TwoFourTreeItem(node.value1);
        TwoFourTreeItem right = new TwoFourTreeItem(node.value3);

        if(!left.addChild(node.leftChild)) throw new IllegalStateException("No space to add child");
        if(!left.addChild(node.centerLeftChild)) throw new IllegalStateException("No space to add child");
        if(!right.addChild(node.centerRightChild)) throw new IllegalStateException("No space to add child");
        if(!right.addChild(node.rightChild)) throw new IllegalStateException("No space to add child");

        if(node.isRoot()) { // Case: Root must be split
            root = new TwoFourTreeItem(value);
            root.addChild(left);
            root.addChild(right);
            return root;
        }
        else { // Case: Value is added to parent node
            // Remove node from being a child of node.parent
            if(node.parent.leftChild == node) node.parent.leftChild = null;
            else if(node.parent.centerLeftChild == node) node.parent.centerLeftChild = null;
            else if(node.parent.centerChild == node) node.parent.centerChild = null;
            else if(node.parent.centerRightChild == node) node.parent.centerRightChild = null;
            else if(node.parent.rightChild == node) node.parent.rightChild = null;
            else throw new IllegalStateException("Node is not a child of node.parent");

            node = node.parent; // Middle value of node was moved up to parent
            insertValueToNode(node, value);


            // Moving children to parent
            node.addChild(left);
            node.addChild(right);
        }


        return node;
    }


    // Pre-condition: node is a two/three node, and we wish to add another data value
    // Post-condition: node now is a three/four node
    // Returns true on successful operation
    private boolean insertValueToNode(TwoFourTreeItem node, int value) {
        if(node.isFourNode()) return false;
        else if(node.isThreeNode()) {
            if(value < node.value1) {
                node.value3 = node.value2;
                node.value2 = node.value1;
                node.value1 = value;
            }
            else if(value < node.value2) {
                node.value3 = node.value2;
                node.value2 = value;
            }
            else node.value3 = value;
        }
        else {
            // 2-node
            if(value < node.value1) {
                node.value2 = node.value1;
                node.value1 = value;
            }
            else node.value2 = value;
        }
        node.values++;
        if(!node.isLeaf) organizeChildren(node);
        return true;
    }

    // Pre-condition: a value is being removed from node
    // Post-condition: node now is a two/three node
    private boolean removeValueFromNode(TwoFourTreeItem node, int value) {
        if(value == node.value1) {
            node.value1 = node.value2;
            node.value2 = node.value3;
        }
        else if(value == node.value2) {
            node.value2 = node.value3;
        }
        node.value3 = 0;
        node.values--;

        return true;
    }

    // Pre-condition: children pointers are not being used correctly
    // Post-condition: children are moved around to fit into their proper ranges
    // Returns true on successful operation
    private boolean organizeChildren(TwoFourTreeItem node) {
        if(node.isLeaf) return true; // no children to organize
        // if(node.checkValidNode()) return true;

        TwoFourTreeItem[] children = new TwoFourTreeItem[] {
                node.leftChild,
                node.centerLeftChild,
                node.centerChild,
                node.centerRightChild,
                node.rightChild
        };

        node.leftChild = null;
        node.centerLeftChild = null;
        node.centerChild = null;
        node.centerRightChild = null;
        node.rightChild = null;

        for(TwoFourTreeItem child : children) {
            if(!node.addChild(child)) throw new IllegalStateException("Not enough space for child");
        }

        return true;
    }

    // Returns true if value exists in tree
    public boolean hasValue(int value) {
        if(root == null) return false;

        TwoFourTreeItem walker = root;
        while(walker != null) {
            if(walker.contains(value)) return true;
            else walker = traverse(walker, value);
        }

        return false;
    }

    // Returns true if node was found and deleted
    // REMEMBER: removal only happens at leaf nodes
    public boolean deleteValue(int value) {
        TwoFourTreeItem target = root;
        while(target != null && !target.contains(value)) {
            // if(!target.checkValidNode()) throw new IllegalStateException("Not valid node");

            target = traverse(target, value);
            if(target != null && target.isTwoNode()) target = fix(target);
        }

        if(target == null) return false; // Value to be deleted not found

        // Value to be deleted found:
        if(target.isLeaf) {
            /*
            No need to worry about children, REMOVAL ONLY HAPPENS AT LEAF NODES
             */
            removeValueFromNode(target, value);
            if(target.values == 0) target.delete();
            return true;
        }

        // Node with value to be deleted IS NOT a leaf node: Go find a successor

        TwoFourTreeItem successor = findSuccessor(target, value);
        if(successor == null) throw new IllegalStateException("Failed in finding a successor");
        else {
            int newValue;
            if(successor.value1 > value) newValue = successor.value1;
            else {
                switch(successor.values) {
                    case 1: {
                        throw new IllegalStateException("Successor should not be a 2-node");
                    }
                    case 2: {
                        newValue = successor.value2;
                        break;
                    }
                    case 3: {
                        newValue = successor.value3;
                        break;
                    }
                    default: {
                        throw new ArithmeticException("Count values better");
                    }
                }
            }
            while(!target.contains(value)) {
                // Special case where target doesn't contain value after the value is moved around during fixing 2-nodes
                target = traverse(target, value);
            }
            if(target.isLeaf) {
                // Special case where target was moved into a leaf node while fixing 2-nodes
                if(target != successor) throw new IllegalStateException("wtf is happening here");
                removeValueFromNode(successor, value);
                if(successor.values == 0) successor.delete();
                return true;
            }

            target.replace(value, newValue);

            removeValueFromNode(successor, newValue);
            if(target.values == 0) target.delete();
            return true;
        }
    }

    // Pre-condition: A 2-node was found during traversal in the deletion process
    // Post-condition: The tree was altered according to 3 special cases. Refer to comment below method.
    private TwoFourTreeItem fix(TwoFourTreeItem node) {
        TwoFourTreeItem[] siblings = findSiblings(node);

        if(siblings == null || siblings[0] == null && siblings[1] == null) {
            return node;
        }

        boolean isLeftSibling = true;

        // This loop handles Case 1
        for(TwoFourTreeItem sibling : siblings) {
            if(sibling == null) {
                isLeftSibling = false;
                continue;
            }

            if(!sibling.isTwoNode()) { // ROTATION
                int parentValue; // value to be rotated in parent
                int siblingValue; // value to be rotated in sibling

                // Establishing sibling value for rotation
                if(isLeftSibling) {
                    // use the rightmost value of sibling node
                    switch(sibling.values) {
                        case 1: throw new IllegalStateException("Sibling should not be a 2-node when rotating");
                        case 2: {
                            siblingValue = sibling.value2;
                            break;
                        }
                        case 3: {
                            siblingValue = sibling.value3;
                            break;
                        }
                        default: throw new ArithmeticException("Count better");
                    }
                }
                else {
                    // use leftmost value of the sibling node
                    siblingValue = sibling.value1;
                }

                // Establishing parent value for rotation
                if(isLeftSibling) {
                   parentValue = overlookingValue(node.parent, siblingValue, node.value1);
                }
                else {
                    parentValue = overlookingValue(node.parent, node.value1, siblingValue);
                }

                // Now that we have parentValue and siblingValue, we rotate!

                node.parent.replace(parentValue, siblingValue);
                if(!insertValueToNode(node, parentValue)) throw new IllegalStateException("Node should be 2-node");
                // Node is now a 3-node

                // Inheriting sibling's child
                if(isLeftSibling) {
                    node.addChild(sibling.rightChild);
                    sibling.rightChild = null;
                }
                else {
                    node.addChild(sibling.leftChild);
                    sibling.leftChild = null;
                }

                // Removing siblingValue
                removeValueFromNode(sibling, siblingValue);
                organizeChildren(sibling);

                // if(!node.checkValidNode()) throw new IllegalStateException("Not a valid node");
                return node;

            }

            isLeftSibling = false;
        }

        // Should only ever exit the loop if one or more siblings are 2-node
        TwoFourTreeItem sibling;

        if(siblings[0] == null) {
            sibling = siblings[1];
        }
        else if(siblings[1] == null) {
            sibling = siblings[0];
        }
        else {
            // Do I prefer left or right sibling? I'm going to choose right.
            sibling = siblings[1];
        }

        // Case 2
        if(node.parent.isTwoNode()) {
            // if(!node.parent.checkValidNode()) throw new IllegalStateException("Not a valid node");

            // Removing node and sibling from being a child of node.parent
            node.parent.leftChild = null;
            node.parent.rightChild = null;

            insertValueToNode(node.parent, node.value1);
            insertValueToNode(node.parent, sibling.value1);


            if(!node.parent.addChild(node.leftChild))
                throw new IllegalStateException("Not enough space for child.");
            if(!node.parent.addChild(node.rightChild))
                throw new IllegalStateException("Not enough space for child.");
            if(!node.parent.addChild(sibling.leftChild))
                throw new IllegalStateException("Not enough space for child.");
            if(!node.parent.addChild(sibling.rightChild))
                throw new IllegalStateException("Not enough space for child.");

            // if(!node.parent.checkValidNode()) throw new IllegalStateException("Not a valid node");
            return node.parent;
        }
        // Case 3
        else {
            // Finding parent value for merge
            isLeftSibling = sibling == siblings[0];
            int parentValue;
            if(isLeftSibling) {
                parentValue = overlookingValue(node.parent, sibling.value1, node.value1);
            }
            else {
                parentValue = overlookingValue(node.parent, node.value1, sibling.value1);
            }

            // Now that we have parentValue, merge!
            insertValueToNode(node, parentValue);
            insertValueToNode(node, sibling.value1);

            // Removing sibling from being a child of node.parent
            if(node.parent.leftChild == sibling) node.parent.leftChild = null;
            else if(node.parent.centerLeftChild == sibling) node.parent.centerLeftChild = null;
            else if(node.parent.centerChild == sibling) node.parent.centerChild = null;
            else if(node.parent.centerRightChild == sibling) node.parent.centerRightChild = null;
            else if(node.parent.rightChild == sibling) node.parent.rightChild = null;
            else throw new IllegalStateException("Sibling is not a child of node.parent");

            // Removing parentValue from parent
            removeValueFromNode(node.parent, parentValue);
            organizeChildren(node.parent);


            // Inheriting sibling's children
            if(!node.addChild(sibling.leftChild))
                throw new IllegalStateException("Not enough space for child.");
            if(!node.addChild(sibling.rightChild))
                throw new IllegalStateException("Not enough space for child.");

            // if(!node.checkValidNode()) throw new IllegalStateException("Not a valid node");
            return node;
        }
    }
    /*
    Case 1: Sibling is a 3-node or 4-node
    - Perform a rotation with that sibling (parent value moves to current node and siblings closest value to the node moves up to parent)
    - Also, this node inherits the child of the rotated value.

    Case 2: Parent is a 2-node and sibling is a 2-node
    - Merge them all to form a 4-node.
    - Make appropriate changes to children.

    Case 3: Parent is a 3-node or 4-node, and siblings are 2-nodes
    - The adjacent 2-node sibling and the parent node value overlooking the two merge together to form a 4-node.
    - Sibling's children are children of this 4-node.
    */

    // Returns an array containing the siblings of a node
    private TwoFourTreeItem[] findSiblings(TwoFourTreeItem node) {
        if(node.parent == null) return null;

        boolean isLeftChild = node == node.parent.leftChild;
        boolean isCenterLeftChild = node == node.parent.centerLeftChild;
        boolean isCenterChild = node == node.parent.centerChild;
        boolean isCenterRightChild = node == node.parent.centerRightChild;
        boolean isRightChild = node == node.parent.rightChild;

        // siblings[0] is sibling to left, and siblings[1] is siblings to right
        TwoFourTreeItem[] siblings = new TwoFourTreeItem[2];

        if(isLeftChild)  {
            siblings[0] = null;
            switch(node.parent.values) {
                case 1: {
                    siblings[1] = node.parent.rightChild;
                    break;
                }
                case 2: {
                    siblings[1] = node.parent.centerChild;
                    break;
                }
                case 3: {
                    siblings[1] = node.parent.centerLeftChild;
                    break;
                }
                default:  throw new ArithmeticException("Count values variable better");
            }
        }
        else if(isCenterLeftChild) {
            siblings[0] = node.parent.leftChild;
            siblings[1] = node.parent.centerRightChild;
        }
        else if(isCenterChild) {
            siblings[0] = node.parent.leftChild;
            siblings[1] = node.parent.rightChild;
        }
        else if(isCenterRightChild) {
            siblings[0] = node.parent.centerLeftChild;
            siblings[1] = node.parent.rightChild;
        }
        else if(isRightChild) {
            siblings[1] = null;
            switch(node.parent.values) {
                case 1: {
                    siblings[0] = node.parent.leftChild;
                    break;
                }
                case 2: {
                    siblings[0] = node.parent.centerChild;
                    break;
                }
                case 3: {
                    siblings[0] = node.parent.centerRightChild;
                    break;
                }
                default:  throw new ArithmeticException("Count values variable better");
            }
        }
        else throw new IllegalStateException("Node is not a child? WTF");

        return siblings;
    }

    // Returns the node value which goes in-between lower and upper
    private int overlookingValue(TwoFourTreeItem node, int lower, int upper) {
        if(node.value1 > lower && node.value1 < upper) {
            return node.value1;
        }
        else if(node.value2 > lower && node.value2 < upper) {
            return node.value2;
        }
        else if(node.value3 > lower && node.value3 < upper) {
            return node.value3;
        }
        else {
            throw new IllegalStateException("No parent value overseeing node and sibling");
        }
    }

    // Pre-condition: Found the node containing the deletion target, but it's not a leaf node.
    // Post-condition: Returns the node containing the value that will replace the deleted target.
    private TwoFourTreeItem findSuccessor(TwoFourTreeItem node, int target) {
        if(node.isLeaf) return node;

        TwoFourTreeItem successor = null;

        boolean isValue1 = node.value1 == target;
        boolean isValue2 = node.value2 == target;
        boolean isValue3 = node.value3 == target;

        if(isValue1) {
            switch(node.values) {
                case 1: {
                    if(node.rightChild != null) successor = node.rightChild;
                    else if(node.leftChild != null) successor = node.leftChild;
                    else throw new IllegalStateException("No where to get a successor");
                    break;
                }
                case 2: {
                    if(node.centerChild != null) successor = node.centerChild;
                    else if(node.leftChild != null) successor = node.leftChild;
                    else throw new IllegalStateException("No where to get a successor");
                    break;
                }
                case 3: {
                    if(node.centerLeftChild != null) successor = node.centerLeftChild;
                    else if(node.leftChild != null) successor = node.leftChild;
                    else throw new IllegalStateException("No where to get a successor");
                    break;
                }
                default: {
                    throw new ArithmeticException("Count values better");
                }
            }
        }
        else if(isValue2) {
            switch(node.values) {
                case 1: {
                    throw new ArithmeticException("Can't be a 2-node if it has a value2");
                }
                case 2: {
                    if(node.rightChild != null) successor = node.rightChild;
                    else if(node.centerChild != null) successor = node.centerChild;
                    else throw new IllegalStateException("No where to get a successor");
                    break;
                }
                case 3: {
                    if(node.centerRightChild != null) successor = node.centerRightChild;
                    else if(node.centerLeftChild != null) successor = node.centerLeftChild;
                    else throw new IllegalStateException("No where to get a successor");
                    break;
                }
                default: {
                    throw new ArithmeticException("Count values better");
                }
            }
        }
        else if(isValue3) {
            switch(node.values) {
                case 1: {
                    throw new ArithmeticException("Can't be a 2-node if it has a value3");
                }
                case 2: {
                    throw new ArithmeticException("Can't be a 3-node if it has a value3");
                }
                case 3: {
                    if(node.rightChild != null) successor = node.rightChild;
                    else if(node.centerRightChild != null) successor = node.centerRightChild;
                    else throw new IllegalStateException("No where to get a successor");
                    break;
                }
                default: {
                    throw new ArithmeticException("Count values better");
                }
            }
        }
        else {
            throw new IllegalStateException("Target should be a value inside node");
        }

        // Moved down to one of node's children
        int successorValue = successor.value1;
        if(successor.isTwoNode()) successor = fix(successor);
        if(successor.contains(target)) successor = findSuccessor(successor, target);
        if(successorValue > target) {
            // Find minimum
            while(!successor.isLeaf) {
                successor = successor.leftChild;
                if(successor.isTwoNode()) successor = fix(successor);
            }
        }
        else if(successorValue < target) {
            // Find maximum
            while(!successor.isLeaf) {
                successor = successor.rightChild;
                if(successor.isTwoNode()) successor = fix(successor);
            }
        }
        else if(!successor.isLeaf && successor.contains(target)) throw new IllegalStateException("hmmm");

        // if(!successor.checkValidNode()) throw new IllegalStateException("Not valid node");
        return successor;
    }

    public void printInOrder() {
        if (root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
        root = null;
    }


}

// Me venting to myself:
/*
I spent more time trying to fix edge cases I hadn't considered than writing the tree. Endless cycle of:
1. "IT WORKS! I'm a genius."
2. New error pops up. "Nevermind, I'm as dumb as a brick"

I'm so glad I wrote errors for things not working properly, using a debugger made it stop at those lines
where errors were thrown and it was so easy to check what the problem was.... now finding why that problem
happened in the first place - THAT WAS HARD.
 */
