public class vertexLinknode {
    private vertex node;
    public vertexLinknode nextLinknode;
    public vertexLinknode preLinknode;
    private String name;
    public vertexLinknode(vertex node){
        this.node=node;
        nextLinknode=null;
        name= node.getCurname();
    }

    public vertex getNode() {
        return node;
    }

    public String getName() {
        return name;
    }
}
