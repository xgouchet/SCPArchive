package fr.xgouchet.scparchive.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.Blockquote;
import fr.xgouchet.scparchive.model.Footer;
import fr.xgouchet.scparchive.model.HRule;
import fr.xgouchet.scparchive.model.Header;
import fr.xgouchet.scparchive.model.Image;
import fr.xgouchet.scparchive.model.Link;
import fr.xgouchet.scparchive.model.ListItem;
import fr.xgouchet.scparchive.model.Paragraph;
import fr.xgouchet.scparchive.model.Photo;
import fr.xgouchet.scparchive.model.Table;
import fr.xgouchet.scparchive.ui.viewholders.ArticleElementViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.BaseViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.ImageViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.LinkViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.ListItemViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.ParagraphViewHolder;
import fr.xgouchet.scparchive.ui.viewholders.PhotoViewHolder;

/**
 * @author Xavier Gouchet
 */
public class ArticleElementAdapter extends BaseSimpleAdapter<ArticleElement, ArticleElementViewHolder> {

    private static final int TYPE_PARAGRAPH = 1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_FOOTER = 3;
    private static final int TYPE_LINK = 4;
    private static final int TYPE_BLOCK_QUOTE = 5;
    private static final int TYPE_LIST_ITEM = 6;
    private static final int TYPE_IMAGE = 7;
    private static final int TYPE_PHOTO = 8;
    private static final int TYPE_H_RULE = 9;
    private static final int TYPE_TABLE = 10;

    @NonNull private final BaseViewHolder.Listener<ArticleElement> listener;

    public ArticleElementAdapter(@NonNull BaseViewHolder.Listener<ArticleElement> listener) {
        this.listener = listener;
    }

    @Override public int getItemViewType(int position) {
        ArticleElement element = getItem(position);
        if (element == null) return 0;

        if (element instanceof Photo) {
            return TYPE_PHOTO;
        } else if (element instanceof Image) {
            return TYPE_IMAGE;
        } else if (element instanceof ListItem) {
            return TYPE_LIST_ITEM;
        } else if (element instanceof Blockquote) {
            return TYPE_BLOCK_QUOTE;
        } else if (element instanceof Header) {
            return TYPE_HEADER;
        } else if (element instanceof Footer) {
            return TYPE_FOOTER;
        } else if (element instanceof Link) {
            return TYPE_LINK;
        } else if (element instanceof Paragraph) {
            return TYPE_PARAGRAPH;
        } else if (element instanceof HRule) {
            return TYPE_H_RULE;
        } else if (element instanceof Table) {
            return TYPE_TABLE;
        }

        return super.getItemViewType(position);
    }

    @Override protected ArticleElementViewHolder instantiateViewHolder(int viewType, View view) {
        switch (viewType) {
            case TYPE_PARAGRAPH:
            case TYPE_BLOCK_QUOTE:
            case TYPE_HEADER:
            case TYPE_FOOTER:
                return new ParagraphViewHolder(null, view);
            case TYPE_LINK:
                return new LinkViewHolder(listener, view);
            case TYPE_LIST_ITEM:
                return new ListItemViewHolder(null, view);
            case TYPE_PHOTO:
                return new PhotoViewHolder(null, view);
            case TYPE_IMAGE:
                return new ImageViewHolder(null, view);
            case TYPE_TABLE:
            default:
                return new ArticleElementViewHolder(null, view);
        }
    }

    @Override protected int getLayout(int viewType) {
        switch (viewType) {
            case TYPE_PARAGRAPH:
                return R.layout.item_paragraph;
            case TYPE_PHOTO:
                return R.layout.item_photo;
            case TYPE_IMAGE:
                return R.layout.item_image;
            case TYPE_LIST_ITEM:
                return R.layout.item_list_item;
            case TYPE_BLOCK_QUOTE:
                return R.layout.item_blockquote;
            case TYPE_H_RULE:
                return R.layout.item_hrule;
            case TYPE_HEADER:
                return R.layout.item_header;
            case TYPE_FOOTER:
                return R.layout.item_footer;
            case TYPE_LINK:
                return R.layout.item_link;
            default:
                return android.R.layout.simple_list_item_1;
        }
    }
}
