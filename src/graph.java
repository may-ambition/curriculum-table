import java.util.Stack;
/*
    1：实现形成不同学期的课程计划：
          在每一学期把当前图中入度为0的课程压入zeroStack，不断删除此栈的结点代表为本学期选课。将新产生入度为0的课程压入辅助栈，
        以备下学期选课。本学期选课结束后，将辅助栈的课程压入zeroStack，供下一学期的选课。循环实现，直到两栈均为空或者学分修够结束。
        （基本思想）
    2：限制每学期的学分总数：
          课程类（vertex）设有学分属性。在每学期pop栈zeroStack时，判断本学期学分是否修满，若修满，停止pop。
    3：某门课的后移：
          在zeroStack里寻找要后移的课程，当要pop此课程时，用个临时变量存储此课程信息，正常继续pop。把辅助栈里的课程pop到
        zeroStack之后把该临时变量的课程类压入zeroStack。
    4：感兴趣课程的挑选：
          vertex类里的like属性是为了实现这一功能而设的。检查zeroStack里like里有没有值非0的课程，若值非0，会根据like的
        大小选择优先pop，实现优先选择感兴趣课程。
    5：输入有向弧的顶点对构建图：grade?
    6：文件形式存储：有关保存文件的操作（学习）
    7：图形界面：（Gui的复习）每个学期选择感兴趣的课程以及输入喜爱值、想后移课程、每个学期的学分限制，显示此学期的编排计划。
         每个学期均选择完毕后显示一个完整的课表。
    8：错误提示：选课太少，修不够学分。
    图形界面设计：
 */
public class graph {
    private vertex[] vertexArray;//vertex类型的顺序表
    private int Vnumber;//顶点数量
    private int position;//vertexArray里真正存入的课程数。
    private Stack<vertex> zeroStack;//存放当前入度为0的课程，该栈内的课程即为本学期能选的课程
    private Stack<vertex> helpStack;//辅助栈
    private vertex[] helpList=new vertex[20];//为了后调课程以及挑选喜爱课程而设置的顺序表，将被调课程或者普通课程压入helpStack,该表存zeroStack中未被后调的结点。
    private vertex[] helpLikeList=new vertex[20];//将喜爱的课程放入此表，然后按喜爱值再压入zeroStack。
    private int gradeLimit;
    //构造方法
    public graph(int number){
        Vnumber=number;
        vertexArray=new vertex[number];
        position=0;
    }
    //为图增加边，创建结点对象,并搭建邻接表和逆邻接表；
    public void addEdge(String formmer,int grade1,String latter,int grade2){
        vertex v1=new vertex(formmer,grade1);
        vertex v2=new vertex(latter,grade2);
        v2.nextNode=v1.nextNode;
        v1.nextNode=v2;
        v1.preNode=v2.preNode;
        v2.preNode=v1;
        if (position==0) {
            vertexArray[0] = v1;
            position++;
        }
        int i;
        for(i=0;i<position;i++){
            if(v1.getCurname()==vertexArray[i].getCurname())
                break;
        }
        if(i==position){
            vertexArray[position]=v1;
        }
        int j;
        for(j=0;j<position;j++){
            if(v2.getCurname()==vertexArray[j].getCurname())
                break;
        }
        if(j==position){
            vertexArray[position]=v2;
        }
    }
    //利用逆邻接表计算每个点的入度。
    public void indegreeCalculate(){
        for(vertex x:vertexArray){
            vertex a;
            a=x;
            while(a.preNode!=null){
                a=a.preNode;
                x.indegree++;
            }
        }
    }
    //删除结点，将其结点入度设为0，并把其邻接表上的结点入度减一。
    public void deleteVertex(vertex x){
        x.indegree=-1;
        vertex a=x;
        while(a.nextNode!=null){
            a=a.nextNode;
            a.indegree--;
        }
        //删除结点。
        x.nextNode.preNode=null;
        x.nextNode=null;
        Vnumber--;
    }
    //后调课程
    public void shiftBackCurriculum(String curriculum){
        vertex x=StringToVetex(curriculum);
        int i=0;
        while(zeroStack.peek()!=x){
            helpList[i]=zeroStack.peek();
            i++;
            zeroStack.pop();
        }
        helpStack.push(zeroStack.pop());
        for(int a=i;a<=1;a--){
            zeroStack.push(helpList[a]);
        }
    }
    //设置喜爱值
    public void setFavorate(String curriculum,int l){
        StringToVetex(curriculum).setFavorate(l);
    }
    //拓扑排序前的准备，将刚开始入度为0的结点压入zeroStack.
    public void PreTypo() {
        for (vertex x : vertexArray) {
            if (x.indegree == 0)
                zeroStack.push(x);
            else
                x.setStackPosition(1);
        }
    }
    //拓扑排序
    public void typo(){
        int i=0;
        int j=0;
        while(!zeroStack.empty()){
            if(zeroStack.peek().getFavorate()!=0){
                helpLikeList[i]=zeroStack.peek();
                i++;
                zeroStack.pop();
            }
            else{
                helpList[j]=zeroStack.pop();
                j++;
            }
        }
        for(int a=j;a<=1;a--){
            zeroStack.push(helpList[a]);
        }

        if(i!=0){
            int c=i;
            while(c>0){
                int min=0;
                for(int d=1;d<i;d++){
                    if(helpLikeList[0].getFavorate()>helpLikeList[d].getFavorate())
                        min=d;
                }
                zeroStack.push(helpLikeList[min]);
            }
        }
        //拓扑排序真正的开始
        int sum=0;
        while(!zeroStack.empty()&&sum+zeroStack.peek().getGrade()<=gradeLimit){
            sum=sum+zeroStack.peek().getGrade();
            deleteVertex(zeroStack.pop());
            //寻找新产生的入度为0的结点
            for(vertex e:vertexArray){
                if(e.indegree ==0&&e.getStackPosition()==1){
                    helpStack.push(e);
                }
            }
        }
        //两栈均空，递归结束。
        if(zeroStack.isEmpty()&&helpStack.isEmpty())
            return;
        //将helpStack里的结点压入zeroStack
        while (!helpStack.empty()){
            helpStack.peek().setStackPosition(0);
            zeroStack.push(helpStack.pop());
        }
        //递归调用。
        typo();

    }

     //根据课程名字返回相应的vertex结点。
    public vertex StringToVetex(String s){
        for(int i=0;i<Vnumber;i++){
            if(vertexArray[i].getCurname()==s)
                return vertexArray[i];
        }
        return null;
    }


    public int getGradeLimit() {
        return gradeLimit;
    }

    public int getVnumber() {
        return Vnumber;
    }

    public Stack<vertex> getHelpStack() {
        return helpStack;
    }

    public Stack<vertex> getZeroStack() {
        return zeroStack;
    }

    public void setVnumber(int vnumber) {
        Vnumber = vnumber;
    }

    public void setGradeLimit(int gradeLimit) {
        this.gradeLimit = gradeLimit;
    }
}
