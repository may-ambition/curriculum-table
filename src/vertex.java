public class vertex {
    private final String curname;
    vertex nextNode;//指向的下一个课程
    vertex preNode;//被指向的课程
    private int like;//0或非0；0代表不喜欢，非0代表喜欢。根据喜爱值的大小调整其在zeroStack的位置，喜爱值越大，离栈顶越近。
    private int grade;//课程所占的学分。
    public int indegree;//该节点的入度。当为负一表示其被删除。
    private int stackPosition;//表示该节点在哪个栈。0表示在zeroStack,1表示在helpStack.
//构造方法
    public vertex(String name,int grade){
        curname=name;
        this.grade=grade;
        like=0;
        nextNode=null;
        indegree=0;
        stackPosition=0;
    }
//get/set方法。
    public int getGrade() {
        return grade;
    }

    public int getFavorate() {
        return like;
    }

    public String getCurname() {
        return curname;
    }

    public void setFavorate(int favorate) {
        this.like = favorate;
    }

    public int getStackPosition() {
        return stackPosition;
    }

    public void setStackPosition(int stackPosition) {
        this.stackPosition = stackPosition;
    }
}
