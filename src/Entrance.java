import staticData.DataPara;
import tools.ConceptLatticeTools;
import tools.DataCalTools;
import tools.DataTools;
import tools.impl.ConceptLatticeToolsImpl;
import tools.impl.DataCalToolsImpl;
import tools.impl.DataToolsImpl;

public class Entrance {
    public static void main(String[] args) {

        /** 1.Read data */
        DataTools dTools = new DataToolsImpl();
        DataPara.adjMat = dTools.inputData();

        long stime = System.currentTimeMillis();

        /** 2.generateConceptLattice */
        ConceptLatticeTools clTools = new ConceptLatticeToolsImpl();
        clTools.generateConceptLattice(DataPara.adjMat,DataPara.nodes);

        /** 3. calculateDegree */
        DataCalTools dcTools = new DataCalToolsImpl();
        dcTools.calIdx();
        dcTools.calculateDegree();

        // 结束时间
        long etime = System.currentTimeMillis();
        System.out.printf("programe runtime is ：%d seconds.", (etime - stime) / 1000);

    }
}
