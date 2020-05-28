package medaware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InvestigationRow
{
    List<String> words = new ArrayList<>();
    private String line;

    public InvestigationRow(String line)
    {
        this.line = line;
        breakLineToWords(line);
    }

    private void breakLineToWords(String line)
    {
        words.addAll(Arrays.stream(line.split(" "))
                .filter(str -> !str.matches("[0-9]+.*")).collect(Collectors.toList()));

    }

    public String getLine()
    {
        return line;
    }

    public String getPrintableWords()
    {
        StringBuilder builder = new StringBuilder();
        for(String str : words)
        {
            builder.append("Word: ").append(str).append(", ");
        }
        builder.append("\n");
        return builder.toString();
    }

    public List<String> getWords()
    {
        return words;
    }
}
