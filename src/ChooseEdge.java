package fairCliques.src;

import staticData.DataPara;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ChooseEdge {

    public static void main(String[] args) throws IOException {
        List<String> edges = Files.readAllLines(Paths.get("data/rawData/" + DataPara.fileName));

        double probability = 0.8;
        int totalEdges = edges.size();
        int selectedEdgesCount = (int) Math.ceil(probability * totalEdges);
        Set<Integer> selectedIndices = new HashSet<>();
        Random random = new Random();

        // 随机选择相应数量的不重复的边的索引
        while (selectedIndices.size() < selectedEdgesCount) {
            selectedIndices.add(random.nextInt(totalEdges));
        }

        List<String> selectedEdges = selectedIndices.stream()
                .map(edges::get)
                .collect(Collectors.toList());

        String outputFileName = "data/pData/" + DataPara.fileName + String.valueOf(probability) + "P";
        Files.write(Paths.get(outputFileName), selectedEdges, StandardCharsets.UTF_8);
        System.out.println("Selected edges saved to " + outputFileName);

    }
}
