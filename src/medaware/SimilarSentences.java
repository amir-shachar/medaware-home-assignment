package medaware;

import java.util.HashSet;
import java.util.Set;

public class SimilarSentences
{
    private final int commonWordIndex;
    private Set<InvestigationRow> rows = new HashSet<>();

    public SimilarSentences(InvestigationRow row1, int commonWordIndex)
    {
        this.commonWordIndex = commonWordIndex;
        addToGroup(row1);
    }

    public int getWordIndex()
    {
        return commonWordIndex;
    }

    public Set<InvestigationRow> getRows()
    {
        return rows;
    }

    public void addToGroup(InvestigationRow row1)
    {
        rows.add(row1);
    }
}
