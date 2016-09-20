package it.smasini.utility.library.ui.multichoice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import it.smasini.utility.library.R;
import it.smasini.utility.library.adapters.BaseAdapter;
import it.smasini.utility.library.ui.Style;

/**
 * Created by Simone on 15/09/16.
 */
public class MultiChoiceAdapter extends BaseAdapter<MultiChoiceModel> {

    private Style style;

    public MultiChoiceAdapter(Context context) {
        super(context, R.layout.multichoice_item, false);
    }

    @Override
    public void onBindCustomViewHolder(final ViewHolder viewHolder, final int position, final MultiChoiceModel viewModel) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.label.setText(viewModel.getLabel());
        myViewHolder.checkBox.setChecked(!viewModel.isEnabled() || viewModel.isSelected());
        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox)view).isChecked();
                viewModel.setSelected(isChecked);
                toggleSelection(position, false);
            }
        });
        myViewHolder.checkBox.setEnabled(viewModel.isEnabled());
        myViewHolder.label.setEnabled(viewModel.isEnabled());
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public List<MultiChoiceModel> getSelectedItems() {
        List<MultiChoiceModel> selected = new ArrayList<>();
        List<MultiChoiceModel> selection = super.getSelectedItems();
        for(int i = 0; i < selection.size(); i++){
            if(selection.get(i).isEnabled()){
                selected.add(selection.get(i));
            }
        }
        return selected;
    }

    class MyViewHolder extends ViewHolder{

        public final TextView label;
        public final CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            label = (TextView) view.findViewById(R.id.label);
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled} // disabled
            };
            switch (style){
                case DARK:
                    int[] colors = new int[] {
                            Color.WHITE,
                            Color.GRAY
                    };
                    ColorStateList myList = new ColorStateList(states, colors);
                    label.setTextColor(myList);
                    checkBox.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    break;
                case LIGHT:
                    colors = new int[] {
                            Color.DKGRAY,
                            Color.GRAY
                    };
                    myList = new ColorStateList(states, colors);
                    label.setTextColor(myList);
                    checkBox.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    break;
            }
        }
    }
}
