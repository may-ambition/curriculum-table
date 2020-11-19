import java.util.*;
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
    9.图形界面设计：
 */
public class graph {
    private vertex[] vertexArray;//vertex类型的顺序表
    private int Vnumber;//顶点数量
    private int position;
    private int timeLimit=280;
    private int timeNow=0;
    //构造方法
    public graph(int number){
        Vnumber=number;
        vertexArray=new vertex[number];
        position=0;
    }
    //vertexArray添加
    public void vertexArrayAdd(vertex a){
        if (position==0) {
            vertexArray[0] =a;
            position++;
        }
        int i;
        for(i=0;i<position;i++){
            if(a.getCurname().equals(vertexArray[i].getCurname()))
                break;
        }
        if(i==position){
            vertexArray[position]=a;
            position++;
        }
    }
    //初始化vertexArray,建立邻接链表和逆邻接链表
    public void addEdge(String formmer,int grade1,String latter,int grade2){
        vertex v1=new vertex(formmer,grade1);
        vertex v2=new vertex(latter,grade2);
        vertexLinknode v3= new vertexLinknode(v1);
        vertexLinknode v4= new vertexLinknode(v2);
        vertexArrayAdd(v1);
        vertexArrayAdd(v2);
        v4.nextLinknode=vertexArray[StringToVetex(formmer)].nextNode;
        vertexArray[StringToVetex(formmer)].nextNode=v4;
        v3.preLinknode=vertexArray[StringToVetex(latter)].preNode;
        vertexArray[StringToVetex(latter)].preNode=v3;
    }

    //利用逆邻接表计算每个点的入度。
    public void indegreeCalculate(){
        for(vertex x:vertexArray){
            vertexLinknode a=x.preNode;

            while(a!=null){
                x.indegree++;
                a=a.preLinknode;
            }
        }
    }
    //删除结点，将其结点入度设为0，并把其邻接表上的结点入度减一。
    public void deleteVertex(int x){
        vertexArray[x].indegree=-1;
        vertexLinknode a=vertexArray[x].nextNode;
        while(a!=null){
            vertexArray[StringToVetex(a.getName())].indegree--;
            a=a.nextLinknode;
        }
        //删除结点。
        vertexArray[x].nextNode=null;//删除节点的过程只改变了邻接表，未改变更新逆邻接表，但是更新了入度,刚好逆邻接表用于延调

    }
    //拓扑排序
    public void typo(){
        Comparator<vertex> cmp = new Comparator<vertex>() {
            public int compare(vertex a, vertex b) { //这里是大根堆
                //if(a.getFavorate()==b.getFavorate())
                    //return b.getImportance()-a.getImportance();//第二键值做参考
                return b.getFavorate()-a.getFavorate(); //第一键值做参考
            }
        };
        Queue<vertex> q = new PriorityQueue<>(cmp);
        for(vertex x:vertexArray){
            if(x.indegree==0) {
                q.add(x);
                x.setIsNew(0);
            }
        }
        int i=1;
        Scanner in = new Scanner(System.in);
        for(i=1;i<=8;i++) {
            System.out.println("第"+i+"个学期的课程为：");
            while (!q.isEmpty() && timeNow <= timeLimit) {
                String a=q.poll().getCurname();
                //System.out.println(a);
                deleteVertex(StringToVetex(a));
                vertexArray[StringToVetex(a)].setWhichsemester(i);
                timeNow+=vertexArray[StringToVetex(a)].getGrade()*16;
                System.out.print(a+" ");
            }
            timeNow=0;
            System.out.println("\n请问您是否想延调课程？延调的话请输入1，然后输入延调的门数，然后输入延调课程的名称，各个信息用空格隔开");
            int isback=in.nextInt();
            if(isback==0)
                System.out.println("未选择延调！");
            else if(isback==1){
                int backNum=in.nextInt();
                for(int j=0;j<backNum;j++){
                    String backName=in.next();
                    for(vertex c:vertexArray){
                        vertexLinknode d=c.preNode;
                        while(d!=null){
                            if(d.getName().equals(backName)){
                                c.indegree+=1;
                                vertexArray[StringToVetex(d.getName())].setIsNew(1);
                            }
                            d=d.preLinknode;

                        }
                    }
                    vertexArray[StringToVetex(backName)].indegree=0;
                    vertexArray[StringToVetex(backName)].setIsNew(1);
                }
            }
            for(vertex x:vertexArray){
                if(x.indegree==0&&x.getIsNew()==1) {
                    q.add(x);
                    x.setIsNew(0);
                }
            }
            System.out.println("");
            for(vertex x:vertexArray){
                System.out.println(x.getCurname()+": "+x.getIndegree());
            }
        }

    }




    //设置喜爱值
    public void setFavorate(String curriculum,int l){
        vertexArray[StringToVetex(curriculum)].setFavorate(l);
    }

    public int StringToVetex(String s){
        for(int i=0;i<Vnumber;i++) {
            if (vertexArray[i].getCurname().equals(s))
                return i;
        }
        return -1;

    }

    public vertex[] getVertexArray() {
        return vertexArray;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getVnumber() {
        return Vnumber;
    }



    public void setVnumber(int vnumber) {
        Vnumber = vnumber;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPosition() {
        return position;
    }

}
