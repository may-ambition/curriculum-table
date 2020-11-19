import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
public class main {
    public static String getJson() {
        String jsonStr = "";
        try {
            File file = new File("/Users/robin/IdeaProjects/CurriculumManagementSystem/src/curriculum.json");
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file),"Utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String args[]) {
        System.out.println(getJson());
        graph g=new graph(15);
        JSONArray getJsonArray=JSON.parseArray(getJson());
        for(int i=0;i<getJsonArray.size();i++){
            JSONObject obj=getJsonArray.getJSONObject(i);
            g.addEdge(obj.getString("formmer"),obj.getIntValue("grade1"), obj.getString("latter"),obj.getIntValue("geade2") );

            //System.out.print(g.getVertexArray()[0].getCurname()+" ");
            //System.out.print(g.getPosition());
        }
        g.indegreeCalculate();
        for(vertex x:g.getVertexArray()){
            System.out.println(x.getCurname()+": "+x.getIndegree());
        }
        g.typo();
        //JSONObject obj=getJsonArray.getJSONObject(0);
        //g.addEdge(obj.getString("formmer"),obj.getIntValue("grade1"), obj.getString("latter"),obj.getIntValue("geade2") );
        /*JSONArray getJsonArray=JSON.parseArray(getJson());
        JSONObject obj=getJsonArray.getJSONObject(0);
        JSONObject obj2=obj.getJSONObject("news");
        //String s=obj2.getString("title");
        int a=obj2.getIntValue("title");
        System.out.println(obj2.getIntValue("title")+obj2.getString("content")+obj2.getString("date"));*/
    }
}
