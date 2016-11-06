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

    public static final String BASE_URL = "http://www.scp-wiki.net/";

    private final OkHttpClient client;

    public ArticleRepository(OkHttpClient client) {
        this.client = client;
    }

    @NonNull
    public Observable<Article> fetchArticle(final String articleId) {

        return Observable.create(new Observable.OnSubscribe<Article>() {
            @Override public void call(Subscriber<? super Article> subscriber) {
                try {
                    String fullPage = downloadPage(articleId);
                    Article article = extractArticle(fullPage, articleId);
                    subscriber.onNext(article);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private String downloadPage(String articleId) throws IOException {
        Request request = new Request.Builder()
                .url(scpUrl(articleId))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    private Article extractArticle(String fullHtml, String articleId) throws IOException {
        Document document = Jsoup.parse(fullHtml);
        Elements pageContentCandidates = document.select("#page-content");

        if (pageContentCandidates.isEmpty()) {
            throw new IllegalArgumentException("Nothing to parse... ");
        }

        Article article = new Article(articleId, articleId.toUpperCase(Locale.US));
        if (TextUtils.equals(articleId, "scp-2521")) {
            article.setTitle("●●|●●●●●|●●|●");
        }
        Element pageContent = pageContentCandidates.get(0);
        for (Node node : pageContent.childNodes()) {
            parseElement(article, node);
        }

        return article;
    }

    private void parseElement(@NonNull Article article, @Nullable Node node) {
        if (node == null) return;

        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            if (textNode.isBlank()) return;

            article.appendParagraph(textNode.getWholeText().trim());
        } else if (node instanceof Element) {
            Element element = (Element) node;
            switch (element.tagName()) {
                case "div":
                    parseDiv(article, element);
                    break;
                case "p":
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
                default:
                    article.addUnhandledTag(element.tagName());
                    Log.w(TAG, "Unhandled element " + article.getTitle() + " : " + element.tagName());
                    Log.d(TAG, element.outerHtml());
                    break;
            }
        }

    }

    private void parseBlockquote(Article article, Element element) {
        String html = element.html();
        html = html.replaceAll("<strong>", "<u>");
        html = html.replaceAll("</strong>", "</u>");

        article.appendBlockquote(html);
    }

    private void parseImage(Article article, Element element) {
        String imgUrl = element.attr("abs:src");
        article.appendImage(imgUrl);
    }

    private void parseUList(Article article, Element element) {
        Elements listItems = element.select("li");
        for (Element listItem : listItems) {
            String html = listItem.html();
            html = html.replaceAll("<strong>", "<u>");
            html = html.replaceAll("</strong>", "</u>");
            article.appendListItem(html);
        }
    }

    private void parseOList(Article article, Element element) {
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

    private void parseDiv(@NonNull Article article, @NonNull Element element) {
        String html = element.html();

        // Ignore rating box
        if (html.contains("page-rate-widget-box")) {
            return;
        }

        // Picture
        if (element.hasClass("scp-image-block")) {
            Elements imgs = element.select("img");
            if (imgs.isEmpty()) {
                Log.w(TAG, "Photo block without image : " + element.outerHtml());
                return;
            }
            String imgUrl = imgs.get(0).attr("abs:src");
            String caption = element.select("div.scp-image-caption").html();
            article.appendPhoto(imgUrl, caption);
            return;
        }

        // Picture
        if (element.hasClass("image-container")) {
            Elements imgs = element.select("img");
            if (imgs.isEmpty()) {
                Log.w(TAG, "Image container without image : " + element.outerHtml());
                return;
            }
            String imgUrl = imgs.get(0).attr("abs:src");
            article.appendImage(imgUrl);
            return;
        }
        // TODO spoilers

        Log.w(TAG, "Unhandled div (" + article.getId() + ") \n" + element.outerHtml());
        article.addUnhandledTag("div." + element.className() + "#" + element.id());
    }

    private static String scpUrl(String articleId) {
        return BASE_URL + articleId;
    }
}
