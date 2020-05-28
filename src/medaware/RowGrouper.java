package medaware;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class RowGrouper
{
    private List<InvestigationRow> report = new ArrayList<>();
    private List<SimilarSentences> similarSentencesList = new ArrayList<>();

    public List<SimilarSentences> getGroupings() throws FileNotFoundException
    {
        loadTextLinesIntoReport();
        splitIntoGroups();
        return similarSentencesList;
    }

    private void loadTextLinesIntoReport() throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("result_file2.txt"));
        while (scanner.hasNextLine())
        {
            report.add(new InvestigationRow(scanner.nextLine()));
        }
    }

    private void splitIntoGroups()
    {
        for (InvestigationRow row : report)
        {
            assignRowToRelevantGroups(row);
        }
    }

    private void assignRowToRelevantGroups(InvestigationRow row)
    {
        if (describesEating(row))
        {
            addRowToEatingRelatedGroups(row);
        }
        else
        {
            addToGettingGroupIfExists(row);
        }
    }

    private void addRowToEatingRelatedGroups(InvestigationRow row)
    {
        addToEatingGroupIfExists(row);
        if (eatsAtDiner(row))
        {
            addToDinerGroupIfExists(row);
        }
        else if (eatsAtRestaurant(row))
        {
            addToRestaurantGroupIfExists(row);
        }
    }

    private void addToGettingGroupIfExists(InvestigationRow row)
    {
        int groupIndex = gettingToGroupIndex();
        if (groupIndex != -1)
        {
            similarSentencesList.get(groupIndex).addToGroup(row);
        }
        else
        {
            similarSentencesList.add(new SimilarSentences(row, 0));
        }
    }

    private int gettingToGroupIndex()
    {
        for (int i = 0; i < similarSentencesList.size(); i++)
        {
            if (isGettingGroup(similarSentencesList.get(i)))
            {
                return i;
            }
        }
        return -1;
    }

    private int eatingRestaurantGroupIndex()
    {
        for (int i = 0; i < similarSentencesList.size(); i++)
        {
            if (isEatingRestaurantGroup(similarSentencesList.get(i)))
            {
                return i;
            }
        }
        return -1;
    }

    private int eatingDinerGroupIndex()
    {
        for (int i = 0; i < similarSentencesList.size(); i++)
        {
            if (isEatingDinerGroup(similarSentencesList.get(i)))
            {
                return i;
            }
        }
        return -1;
    }

    private int eatingGroupOfNameIndex(String name)
    {
        for (int i = 0; i < similarSentencesList.size(); i++)
        {
            if (isEatingGroupOfName(similarSentencesList.get(i), name))
            {
                return i;
            }
        }
        return -1;
    }

    private boolean isGettingGroup(SimilarSentences similarSentences)
    {
        return similarSentences.getRows().stream().allMatch(row -> row.getWords().get(2).equals("getting"));
    }

    private boolean isEatingRestaurantGroup(SimilarSentences similarSentences)
    {
        return similarSentences.getWordIndex() == 0 &&
                similarSentences.getRows().stream().allMatch(RowGrouper::eatsAtRestaurant);
    }

    private static boolean eatsAtRestaurant(InvestigationRow row)
    {
        return row.getWords().get(5).equals("restaurant");
    }

    private void addToRestaurantGroupIfExists(InvestigationRow row)
    {
        int groupIndex = eatingRestaurantGroupIndex();
        if (groupIndex != -1)
        {
            similarSentencesList.get(groupIndex).addToGroup(row);
        }
        else
        {
            similarSentencesList.add(new SimilarSentences(row, 0));
        }
    }

    private void addToDinerGroupIfExists(InvestigationRow row)
    {
        int groupIndex = eatingDinerGroupIndex();
        if (groupIndex != -1)
        {
            similarSentencesList.get(groupIndex).addToGroup(row);
        }
        else
        {
            similarSentencesList.add(new SimilarSentences(row, 0));
        }
    }

    private void addToEatingGroupIfExists(InvestigationRow row1)
    {
        int groupIndex = eatingGroupOfNameIndex(row1.getWords().get(0));
        if (groupIndex != -1)
        {
            similarSentencesList.get(groupIndex).addToGroup(row1);
        }
        else
        {
            similarSentencesList.add(new SimilarSentences(row1, 5));
        }
    }

    private boolean isEatingDinerGroup(SimilarSentences similarSentences)
    {
        return similarSentences.getWordIndex() == 0 && similarSentences.getRows().stream().allMatch(RowGrouper::eatsAtDiner);
    }

    private static boolean eatsAtDiner(InvestigationRow row1)
    {
        return row1.getWords().get(5).equals("diner");
    }

    private boolean isEatingGroupOfName(SimilarSentences similarSentences, String name)
    {
        return similarSentences.getRows().stream().allMatch(
                row -> row.getWords().get(0).equals(name) && describesEating(row));
    }

    private boolean describesEating(InvestigationRow row1)
    {
        return row1.getWords().get(2).toLowerCase().equals("eating");
    }
}