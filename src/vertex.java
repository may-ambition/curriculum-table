public class vertex {
    private final String curname;
    vertexLinknode nextNode;//指向的下一个课程
    vertexLinknode preNode;//被指向的课程
    private int like;//0或非0；0代表不喜欢，非0代表喜欢。根据喜爱值的大小调整其在zeroStack的位置，喜爱值越大，离栈顶越近。
    private int grade;//课程所占的学分。
    public int indegree;//该节点的入度。当为负一表示其被删除。
    private int whichsemester;
    private int isNew;
    //private int importance;
//构造方法
    public vertex(String name,int grade){
        curname=name;
        this.grade=grade;
        like=0;
        nextNode=null;
        indegree=0;
        whichsemester=9;
        isNew=1;
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

    public int getWhichsemester() {
        return whichsemester;
    }

    public void setWhichsemester(int whichsemester) {
        this.whichsemester = whichsemester;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsNew() {
        return isNew;
    }

   /* public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }*/
}
