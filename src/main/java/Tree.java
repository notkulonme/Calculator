import hu.notkulonme.Token;

public class Tree {
    Tree leftChild;
    Tree rightChild;
    Tree parent;
    Token value;

    public Tree(Tree leftChild, Tree rightChild, Token value){
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = null;
        this.value = value;
    }

    public Tree(Tree leftChild, Tree rightChild, Tree parent, Token value){
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
        this.value = value;
    }
}
