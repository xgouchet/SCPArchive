package fr.xgouchet.scparchive.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.Image;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class ImageViewHolder extends ArticleElementViewHolder {


    @BindView(R.id.image) ImageView image;

    public ImageViewHolder(@Nullable Listener<ArticleElement> listener,
                           @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);
    }

    @Override protected void onBindItem(@NonNull ArticleElement item) {
        if (!(item instanceof Image)) return;

        Image imageElement = (Image) item;
        Picasso.with(image.getContext())
                .load(imageElement.getUrl())
                .into(image);

    }
}
