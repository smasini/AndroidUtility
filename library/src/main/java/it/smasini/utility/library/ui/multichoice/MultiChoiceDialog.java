package it.smasini.utility.library.ui.multichoice;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;
import it.smasini.utility.library.R;
import it.smasini.utility.library.graphics.DrawableUtility;
import it.smasini.utility.library.ui.Style;

/**
 * Created by Simone on 15/09/16.
 */
public class MultiChoiceDialog  {

    private String mTitle;
    private Activity mContext;
    private List<MultiChoiceModel> multiChoiceItems;
    private MultiChoiceAdapter adapter;
    private OnConfirm onConfirm;
    private Dialog dialog;
    protected Style style;

    public MultiChoiceDialog(Activity context, String title, List<MultiChoiceModel> multiChoiceItems){
        this(context, title, multiChoiceItems, Style.DARK);
    }

    public MultiChoiceDialog(Activity context, String title, List<MultiChoiceModel> multiChoiceItems, Style style) {
        this.mContext = context;
        this.multiChoiceItems = multiChoiceItems;
        this.mTitle = title;
        this.style = style;
        init();
    }

    private void init(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, getStyleDialog());
        if(mTitle != null)
            builder.setTitle(mTitle);
        builder.setNegativeButton(mContext.getString(R.string.label_annulla), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(mContext.getString(R.string.label_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<MultiChoiceModel> sel = adapter.getSelectedItems();
                if(onConfirm!=null){
                    onConfirm.onSelectionComplete(sel);
                }
                dialogInterface.dismiss();
            }
        });
        LayoutInflater inflater = mContext.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.multi_choice_dialog_layout, null);
        builder.setView(dialogView);

        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.recyclerview_dialog);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);
        adapter = new MultiChoiceAdapter(mContext);
        adapter.setStyle(style);
        recyclerView.setAdapter(adapter);
        adapter.swapData(multiChoiceItems);

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int)mContext.getResources().getDimension(R.dimen.multi_choice_dialog_width);
        lp.height = (int)mContext.getResources().getDimension(R.dimen.multi_choice_dialog_height);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(getDrawable());
    }

    public void show(){
        dialog.show();
    }

    public void setOnConfirm(OnConfirm onConfirm) {
        this.onConfirm = onConfirm;
    }

    public interface OnConfirm {
        void onSelectionComplete(List<MultiChoiceModel> models);
    }

    protected int getStyleDialog(){
        switch (style){
            case LIGHT:
                return R.style.AlertDialogStyleLight;
            default:
            case DARK:
                return R.style.AlertDialogStyle;
        }
    }

    protected Drawable getDrawable(){
        switch (style){
            case LIGHT:
                return getDrawableLight();
            default:
            case DARK:
                return getDrawableDark();
        }
    }

    private Drawable getDrawableLight(){
         return DrawableUtility.getDrawable(mContext, android.R.drawable.dialog_holo_light_frame);
    }

    private Drawable getDrawableDark(){
        return DrawableUtility.getDrawable(mContext, android.R.drawable.dialog_holo_dark_frame);
    }
}
