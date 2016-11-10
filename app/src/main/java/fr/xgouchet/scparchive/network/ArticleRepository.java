package fr.xgouchet.scparchive.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.deezer.android.counsel.annotations.Trace;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import fr.xgouchet.scparchive.model.Article;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
@Trace
public class ArticleRepository {

    public static final String TAG = ArticleRepository.class.getSimpleName();

    public static final String BASE_URL = "http://www.scp-wiki.net";
    public static final String BASE_INTERNAL_URL = "scp-archive://www.scp-wiki.net";
    public static final String ABOUT_ARTICLE = "__fr.xgouchet.scparchive.ABOUT__";

    public static final String SCP_WIKI_LINK = "<a href=\"http://www.scp-wiki.net/\">SCP Foundation wiki</a>";
    public static final String AUTHORS_LINK = "<a href=\"http://www.scp-wiki.net/members-pages\">Authors' page</a>";
    public static final String GITHUB_LINK = "<a href=\"https://github.com/xgouchet/SCPArchive\">GitHub</a>";
    public static final String CC_BY_SA_LINK = "<a href=\"http://creativecommons.org/licenses/by-sa/3.0/\">Creative Commons Attribution-ShareAlike 3.0 License</a>";


    private static final String DISCLAIMER = "This article comes from the " +
            SCP_WIKI_LINK + " (use authorised under the " + CC_BY_SA_LINK + ").";
    private static final String AUTHORS_CREDIT = "Credit for this article go to the authors on the wiki.";

    private final OkHttpClient client;

    public ArticleRepository(OkHttpClient client) {
        this.client = client;
    }

    @NonNull
    public Observable<Article> fetchArticle(final String articleId) {

        return Observable.create(new Observable.OnSubscribe<Article>() {
            @Override public void call(Subscriber<? super Article> subscriber) {

                if (TextUtils.equals(articleId, ABOUT_ARTICLE)) {
                    subscriber.onNext(aboutArticle());
                    subscriber.onCompleted();
                    return;
                }

                try {
                    final String url = scpUrl(articleId);
                    String fullPage = downloadPage(url);
                    Article article = extractArticle(fullPage, articleId);
                    article.setUrl(url);
                    subscriber.onNext(article);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private String downloadPage(@NonNull String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private Article extractArticle(String fullHtml, String articleId) throws IOException {
        Document document = Jsoup.parse(fullHtml);
        Article article = new Article(articleId, articleId.toUpperCase(Locale.US));
        if (TextUtils.equals(article.getId(), "scp-2521")) {
            article.setTitle("●●|●●●●●|●●|●");
        }

        parseContent(document, article);
        if (article.isEmpty()) return article;

        appendDisclaimerAndAuthors(article);

        return article;
    }

    private void appendDisclaimerAndAuthors(Article article) {
        article.appendHRule();
        article.appendFooter(DISCLAIMER);
        article.appendFooter(AUTHORS_CREDIT);
//        article.appendFooter("<a href=\"" + scpUrl(article.getId()) + "#_history\">Authors of " + article.getTitle() + "</a>");
    }

    @Nullable public static Element selectFirst(@NonNull Element parent, @NonNull String query) {
        Elements candidates = parent.select(query);

        if ((candidates == null) || candidates.isEmpty()) return null;
        return candidates.get(0);
    }

//    private Map<String, String> parseAuthors(@NonNull Document document) {
//        final Map<String, String> authors = new HashMap<>();
//        Element revisionList = selectFirst(document, "#revision-list");
//        if (revisionList == null) return authors;
//
//        Elements rows = revisionList.select("tr");
//        if (rows.isEmpty()) return authors;
//
//        for (Element row : rows) {
//            Element printUser = selectFirst(row, ".printuser");
//            if (printUser == null) continue;
//
//            Element dateSpan = selectFirst(row, ".odate");
//            final String date = dateSpan == null ? "" : dateSpan.text();
//            final String name = printUser.text();
//            if (authors.containsKey(name)) {
//                String dates = authors.get(name);
//                dates += ", " + date;
//                authors.put(name, dates);
//            } else {
//                authors.put(name, date);
//            }
//        }
//
//        return authors;
//    }

    private void parseContent(@NonNull Document document, @NonNull Article article) {

        Element pageContent = selectFirst(document, "#page-content");
        if (pageContent == null) {
            throw new IllegalArgumentException("Nothing to parse... ");
        }

        for (Node node : pageContent.childNodes()) {
            parseNode(article, node);
        }
    }


    private void parseNode(@NonNull Article article, @Nullable Node node) {
        if (node == null) return;

        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            if (textNode.isBlank()) return;

            article.appendParagraph(textNode.getWholeText().trim());
        } else if (node instanceof Element) {
            parseElement(article, (Element) node);
        }
    }

    private void parseElement(@NonNull Article article, @NonNull Element element) {
        switch (element.tagName()) {
            case "div":
                parseDiv(article, element);
                break;
            case "p":
            case "span":
                parseParagraph(article, element);
                break;
            case "ul":
                parseUList(article, element);
                break;
            case "ol":
                parseOList(article, element);
                break;
            case "img":
                parseImage(article, element);
                break;
            case "blockquote":
                parseBlockquote(article, element);
                break;
            case "hr":
                article.appendHRule();
                break;
            case "table":
                parseTable(article, element);
                break;
            case "h2":
                parseHeader(article, element);
                break;
            case "br":
                // ignore;
                break;
            default:
                article.addUnhandledTag(element.tagName());
                Log.w(TAG, "Unhandled element " + article.getTitle() + " : " + element.tagName());
                Log.d(TAG, element.outerHtml());
                break;
        }
    }

    private void parseTable(@NonNull Article article, @NonNull Element element) {
        int maxWidth = 0, width;
        List<List<String>> rows = new LinkedList<>();

        Elements trs = element.select("tr");
        for (Element tr : trs) {
            width = 0;
            List<String> row = new LinkedList<>();
            Elements tds = tr.select("td,th");
            for (Element td : tds) {
                String html = td.html();
                html = html.replaceAll("<strong>", "<u>");
                html = html.replaceAll("</strong>", "</u>");
                row.add("<strong>" + html + "</strong>");
                width++;
            }

            if (width > maxWidth) maxWidth = width;
            rows.add(row);
        }

        String[][] table = new String[rows.size()][maxWidth];
        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) {
                String cell = row.get(j);
                table[i][j] = cell;
            }
        }
        article.appendTable(table);
    }

    private void parseImage(@NonNull Article article, @NonNull Element element) {
        String imgUrl = element.attr("abs:src");
        article.appendImage(imgUrl);
    }

    private void parseBlockquote(@NonNull Article article, @NonNull Element element) {
        String html = element.html();
        html = html.replaceAll("<strong>", "<u>");
        html = html.replaceAll("</strong>", "</u>");

        article.appendBlockquote(html);
    }

    private void parseUList(@NonNull Article article, @NonNull Element element) {
        Elements listItems = element.select("li");
        for (Element listItem : listItems) {
            String html = listItem.html();
            html = html.replaceAll("<strong>", "<u>");
            html = html.replaceAll("</strong>", "</u>");

            article.appendListItem(html);
        }
    }

    private void parseOList(@NonNull Article article, @NonNull Element element) {
        int number = 1;
        Elements listItems = element.select("li");
        for (Element listItem : listItems) {
            String html = listItem.html();
            html = html.replaceAll("<strong>", "<u>");
            html = html.replaceAll("</strong>", "</u>");

            article.appendListItem(html, Integer.toString(number++));
        }
    }

    private void parseParagraph(@NonNull Article article, @NonNull Element element) {

        String html = element.html();
        html = html.replaceAll("<strong>", "<u>");
        html = html.replaceAll("</strong>", "</u>");

        article.appendParagraph(html);
    }

    private void parseHeader(@NonNull Article article, @NonNull Element element) {

        String html = element.html();
        html = html.replaceAll("<strong>", "<u>");
        html = html.replaceAll("</strong>", "</u>");

        article.appendHeader(html);
    }

    private void parseDiv(@NonNull Article article, @NonNull Element element) {
        String html = element.html();

        // Ignore rating box
        if (html.contains("page-rate-widget-box")) {
            return;
        }

        // Picture
        if (element.hasClass("scp-image-block")) {
            parsePhoto(article, element);
            return;
        }

        // Picture
        if (element.hasClass("image-container")) {
            parsePicture(article, element);
            return;
        }

        // Spoilers
        if (element.hasClass("collapsible-block")) {
            parseFoldable(article, element);
            return;
        }

        // Footers
        if (element.hasClass("footnotes-footer")) {
            parseFooters(article, element);
            return;
        }

        Log.w(TAG, "Unhandled div (" + article.getId() + ") \n" + element.outerHtml());
        article.addUnhandledTag("div." + element.className() + "#" + element.id());
    }

    private void parseFooters(@NonNull Article article, @NonNull Element element) {
        article.appendHRule();

        Elements footers = element.select("div.footnote-footer");
        for (Element footer : footers) {
            String html = footer.html();
            html = html.replaceAll("<strong>", "<u>");
            html = html.replaceAll("</strong>", "</u>");
            article.appendFooter(html);
        }
    }

    private void parseFoldable(@NonNull Article article, @NonNull Element element) {
        final Element foldedLink = selectFirst(element, "div.collapsible-block-folded>a");
        final Element unfoldedLink = selectFirst(element, "div.collapsible-block-unfolded-link>a");
        if ((foldedLink == null) || (unfoldedLink == null)) {
            Log.w(TAG, "Collapsible without folded link : " + element.outerHtml());
            return;
        }

        // start
        article.startFoldable();

        // Links
        article.appendFolded(foldedLink.html());
        article.appendLink(unfoldedLink.html());

        // content
        Elements divContents = element.select("div.collapsible-block-content");
        for (Element divContent : divContents) {
            for (Node node : divContent.childNodes()) {
                parseNode(article, node);
            }
        }

        article.endFoldable();
    }

    private void parsePicture(@NonNull Article article, @NonNull Element element) {
        final Element img = selectFirst(element, "img");
        if (img == null) {
            Log.w(TAG, "Image container without image : " + element.outerHtml());
            return;
        }
        String imgUrl = img.attr("abs:src");
        article.appendImage(imgUrl);
    }

    private void parsePhoto(@NonNull Article article, @NonNull Element element) {
        final Element img = selectFirst(element, "img");
        if (img == null) {
            Log.w(TAG, "Photo container without image : " + element.outerHtml());
            return;
        }

        String imgUrl = img.attr("abs:src");
        String caption = element.select("div.scp-image-caption").html();
        caption = caption.replaceAll("<strong>", "<u>");
        caption = caption.replaceAll("</strong>", "</u>");
        article.appendPhoto(imgUrl, caption);
    }

    private static String scpUrl(String articleId) {
        return BASE_URL + "/" + articleId;
    }

    private Article aboutArticle() {
        Article article = new Article(ABOUT_ARTICLE, "SCP-Archive");
        article.appendBlockquote("<u>Credits:</u>All the content in this app comes from the " + SCP_WIKI_LINK + ", shared under the " + CC_BY_SA_LINK + ". " +
                "An up to date list of all authors is available on the " + AUTHORS_LINK + ".");
        article.appendBlockquote("<u>License:</u> SCP-Archive itself is Open-Source, shared under the " + CC_BY_SA_LINK +
                ", and available on " + GITHUB_LINK + ".");
        article.appendPhoto("http://i.imgur.com/IDroBfX.jpg", "SCP-Archive on an Android phone");
        article.appendParagraph("<u>Item #:</u> SCP-Archive");
        article.appendParagraph("<u>Object Class:</u> Safe");
        article.appendParagraph("<u>Special Containment Procedures:</u> SCP-Archive needs to be installed on an Android cell-phone at all time, and should be used at least once a week by the owner of the phone.");
        article.appendParagraph("<u>Description:</u> SCP-Archive is an Android application written by Xavier F. Gouchet</a>, and published on the Google Play Store  on 11/07/2016. Its presence on a phone without being used doesn't have any anomalous effect.");
        article.appendParagraph("When used, it displays various redacted report from the SCP Foundation, which consists of thousands of anomalous items, locations, creatures, persons or events.");
        article.appendParagraph("<u>Addendum:</u> SCP-Archive relies on several Open-Source libraries : ");
        article.appendListItem("<a href=\"https://jsoup.org/\">JSoup</a>");
        article.appendListItem("<a href=\"http://square.github.io/picasso/\">Picasso</a>");
        article.appendListItem("<a href=\"https://github.com/wasabeef/picasso-transformations\">Picasso Transformations</a>");
        article.appendListItem("<a href=\"http://jakewharton.github.io/butterknife/\">Butterknife</a>");
        article.appendListItem("<a href=\"https://github.com/ReactiveX/RxAndroid\">RxAndroid</a>");
        article.appendListItem("<a href=\"https://google.github.io/dagger/\">Dagger</a>");

        return article;
    }
}
