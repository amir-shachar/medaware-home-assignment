package medaware;

import java.io.FileNotFoundException;
import java.util.*;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        List<SimilarSentences> similarityGroups = showInvestigatorGroups();
        printGroups(similarityGroups);
    }

    private static List<SimilarSentences>  showInvestigatorGroups() throws FileNotFoundException
    {
        return new RowGrouper().getGroupings();
    }

    private static void printGroups(List<SimilarSentences> similarityGroups)
    {
        System.out.println("=====");
        for(SimilarSentences similarity : similarityGroups)
        {
            printSimilarityGroup(similarity);
        }
        System.out.println("=====");
    }

    private static void printSimilarityGroup(SimilarSentences similarity)
    {
        StringBuilder builder = new StringBuilder();
        Set<String> usedLines = new HashSet<>();
        Set<String> usedChangingWords = new HashSet<>();

        buildTextForGroup(similarity, builder, usedLines, usedChangingWords);
        buildTextForChangedWords(builder, usedChangingWords);
        printIfGroupHasMembers(builder, usedLines);
    }

    private static void printIfGroupHasMembers(StringBuilder builder, Set<String> usedLines)
    {
        if(usedLines.size() > 1)
        {
            System.out.println(builder.toString());
        }
    }

    private static void buildTextForChangedWords(StringBuilder builder, Set<String> usedChangingWords)
    {
        StringJoiner joiner = new StringJoiner(", ");
        usedChangingWords.forEach(joiner::add);
        builder.append("The changing word was: ").append(joiner.toString()).append("\n");
    }

    private static void buildTextForGroup(SimilarSentences similarity, StringBuilder builder, Set<String> usedLines, Set<String> usedChangingWords)
    {
        for(InvestigationRow row : similarity.getRows())
        {
            if(!usedLines.contains(row.getLine()))
            {
                builder.append(row.getLine()).append("\n");
                usedChangingWords.add(row.getWords().get(similarity.getWordIndex()));
                usedLines.add(row.getLine());
            }
        }
    }
}
