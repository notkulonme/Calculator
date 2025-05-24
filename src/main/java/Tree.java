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

    public boolean isRoot(){
        return parent == null;
    }

    public Tree getRoot(){
        if (isRoot())
            return this;
        else
            return parent.getRoot();
    }

    public void delete(){
        leftChild = null;
        rightChild = null;
        parent = null;
        value = null;
    }

    public void removeChildren(){
        leftChild = null;
        rightChild = null;
    }

    public boolean hasChildren(){
        return leftChild != null && rightChild != null;
    }
}
