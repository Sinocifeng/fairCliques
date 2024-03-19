package tools;

import entity.Node;

import java.util.Map;

public interface ConceptLatticeTools {

    public void generateConceptLattice(int[][] adjMat, Map<Integer, Node> nodes);
}
