import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Steps {

    public static String readHtml(String htmlPath) {
        try (FileReader reader = new FileReader(htmlPath);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String html = "";
            while(bufferedReader.ready()) {
                html = html.concat(bufferedReader.readLine());
            }
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeHtml(String html, String htmlPath) {
        try (FileWriter writer = new FileWriter(htmlPath);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getMatchList(String regex, String target) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(target);
        var list = new ArrayList<String>();
        while(matcher.find()) {
            list.add(matcher.group());
        }
        if(list.isEmpty()) {
            System.out.println("----------------#region------------------");
            System.out.println("Не найдено совпадений");
            System.out.println("----------------#endregion------------------");
        } else {
            System.out.println("----------------#region------------------");
            System.out.println("По данному шаблону (" + regex + ") в следующей строке : "
                    + target + " найдено " + list.size() + " совпадений(я, е):");
            list.forEach(System.out::println);
            System.out.println("----------------#endregion------------------");
        }
        return list;
    }

    public static String getSingleMatch(String regex, String target) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(target);
        var list = new ArrayList<String>();
        while(matcher.find()) {
            list.add(matcher.group());
        }
        return list.get(0);
    }

    public static List<String> getLinks(String html) {
        var coveredLinks = getMatchList("<a href=[^>]*><\\/a>", html);
        var uncoveredLinks = getMatchList("https:\\/\\/[^\\s, >, <, \"]+", html);
        BiPredicate<String, List<String>> containsPredicate = (link, list) -> {
            for (var item : list) {
                if (item.contains(link)) {
                    return false;
                }
            }
            return true;
        };
        var pureUncoveredLinks = uncoveredLinks.stream()
                .filter(link -> containsPredicate.test(link, coveredLinks))
                .collect(Collectors.toList());
        System.out.println("Найдено обрамленных ссылок: " + (uncoveredLinks.size() - pureUncoveredLinks.size()));
        System.out.println("Найдено необрамленных ссылок: " + pureUncoveredLinks.size());
        return pureUncoveredLinks;
    }

    public static List<String> getNewLinks(List<String> uncoveredLinks) {
        var uncoveredLinksDescription = uncoveredLinks.stream()
                .map(link -> getSingleMatch("https:\\/\\/[^\\/]+", link))
                .collect(Collectors.toList());
        var resultList = new ArrayList<String>();
        for (int i = 0; i < uncoveredLinks.size(); i++) {
            resultList.add("<a href=\"" + uncoveredLinks.get(i) + "\">" + uncoveredLinksDescription.get(i) + "</a>");
        }
        return resultList;
    }

}
