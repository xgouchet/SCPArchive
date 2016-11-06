package fr.xgouchet.scparchive.ui.viewholders;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.Paragraph;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class ParagraphViewHolder extends ArticleElementViewHolder {

    @BindView(R.id.content) TextView content;

    public ParagraphViewHolder(@Nullable Listener<ArticleElement> listener,
                               @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);
        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForText();
        content.setTypeface(typeface);
    }

    @Override protected void onBindItem(@NonNull ArticleElement item) {
        if (!(item instanceof Paragraph)) return;
        content.setText(((Paragraph) item).getContent());
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
