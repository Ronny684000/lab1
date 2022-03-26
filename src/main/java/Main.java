public class Main {

    private static final String INPUT_PATH = "./src/main/resources/input.html";
    private static final String OUTPUT_PATH = "./src/main/resources/output.html";

    public static void main(String[] args) {
       String html = Steps.readHtml(INPUT_PATH);
       var uncoveredLinks = Steps.getLinks(html);
       var newLinks = Steps.getNewLinks(uncoveredLinks);
       for (int i = 0; i < uncoveredLinks.size(); i++) {
            html = html.replace(uncoveredLinks.get(i), newLinks.get(i));
       }
       Steps.writeHtml(html, OUTPUT_PATH);
    }
}
