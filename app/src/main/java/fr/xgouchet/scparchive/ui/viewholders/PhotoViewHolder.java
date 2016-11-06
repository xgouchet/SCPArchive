package fr.xgouchet.scparchive.ui.viewholders;

import android.graphics.PointF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import fr.xgouchet.scparchive.BaseApplication;
import fr.xgouchet.scparchive.R;
import fr.xgouchet.scparchive.model.ArticleElement;
import fr.xgouchet.scparchive.model.Photo;
import jp.wasabeef.picasso.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class PhotoViewHolder extends ArticleElementViewHolder {

    public static final PointF VIGNETTE_CENTER = new PointF(0.5f, 0.5f);
    public static final float[] VIGNETTE_COLOR = new float[]{0.0f, 0.0f, 0.0f};

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.caption) TextView caption;

    public PhotoViewHolder(@Nullable Listener<ArticleElement> listener,
                           @NonNull View itemView) {
        super(listener, itemView);
        bind(this, itemView);
        Typeface typeface = BaseApplication.from(itemView.getContext())
                .getAppComponent().getTypefaceForCaption();
        caption.setTypeface(typeface);
    }

    @Override protected void onBindItem(@NonNull ArticleElement item) {
        if (!(item instanceof Photo)) return;

        Photo photoElement = (Photo) item;
        Picasso.with(image.getContext())
                .load(photoElement.getUrl())
                .transform(new SepiaFilterTransformation(image.getContext(), 0.42f))
                .transform(new VignetteFilterTransformation(image.getContext(),
                        VIGNETTE_CENTER,
                        VIGNETTE_COLOR,
                        0.0f,
                        0.8f))
                .into(image);

        caption.setText(photoElement.getCaption());
    }
}
