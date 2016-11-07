package fr.xgouchet.scparchive.network;

import java.util.List;

import fr.xgouchet.scparchive.model.Article;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.FoldedElement;
import fr.xgouchet.scparchive.model.UnfoldedElement;
import rx.functions.Func1;

/**
 * @author Xavier Gouchet
 */
public class ResolveFoldableArticle implements Func1<Article, Article> {
    @Override public Article call(Article article) {

        Article resolved = new Article(article.getId(), article.getTitle());
        resolved.setUrl(article.getUrl());

        List<ArticleElement> elements = article.getElements();

        for (ArticleElement element : elements){
            if (element == null) continue;

            if (element instanceof FoldedElement){
                FoldedElement foldedElement = (FoldedElement) element;
                if (article.isFolded(foldedElement.getFoldableId())){
                    resolved.appendElement(foldedElement.getElement());
                }
            } else if (element instanceof UnfoldedElement){
                UnfoldedElement unfoldedElement = (UnfoldedElement) element;
                if (!article.isFolded(unfoldedElement.getFoldableId())){
                    resolved.appendElement(unfoldedElement.getElement());
                }
            } else {
                resolved.appendElement(element);
            }

        }

        return resolved;
    }
}
