package it.smasini.utility.library.ui.multichoice;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import it.smasini.utility.library.R;
import it.smasini.utility.library.adapters.BaseAdapter;

/**
 * Created by Simone on 15/09/16.
 */
public class MultiChoiceAdapter extends BaseAdapter<MultiChoiceModel> {

    public MultiChoiceAdapter(Context context) {
        super(context, R.layout.multichoice_item, false);
    }

    @Override
    public void onBindCustomViewHolder(final ViewHolder viewHolder, final int position, final MultiChoiceModel viewModel) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if(viewModel.isEnabled()) {
            myViewHolder.label.setText(viewModel.getLabel());
            myViewHolder.checkBox.setChecked(viewModel.isSelected());
            myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    viewModel.setSelected(b);
                    toggleSelection(position);
                }
            });
        }
        myViewHolder.checkBox.setEnabled(viewModel.isEnabled());
        myViewHolder.label.setEnabled(viewModel.isEnabled());
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    class MyViewHolder extends ViewHolder{

        public final TextView label;
        public final CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            label = (TextView) view.findViewById(R.id.label);
        }
    }

}
