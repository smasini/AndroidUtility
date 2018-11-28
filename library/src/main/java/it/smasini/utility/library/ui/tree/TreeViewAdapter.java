package it.smasini.utility.library.ui.tree;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.smasini.utility.library.R;
import it.smasini.utility.library.SizeUtility;
import it.smasini.utility.library.adapters.BaseAdapter;

/**
 * Created by Simone on 27/01/17.
 */

public class TreeViewAdapter extends BaseAdapter<TreeItemViewModel> {

    private TreeView.SelectionType selectionType = TreeView.SelectionType.MULTIPLE_SELECTION;
    private List<TreeItemViewModel> itemsSelected = new ArrayList<>();
    private int[] colors;

    public TreeViewAdapter(Context context) {
        this(context, R.layout.tree_view_item);
    }

    protected TreeViewAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    @Override
    public void onBindCustomViewHolder(ViewHolder viewHolder, int position, TreeItemViewModel viewModel) {
        TreeViewHolder treeViewHolder = (TreeViewHolder) viewHolder;

        int padding = (int) SizeUtility.convertDpToPixel(8, mContext) * viewModel.getLevel();
        treeViewHolder.addPadding(padding);

        if(!viewModel.hasChilds()){
            treeViewHolder.imageButtonExpande.setVisibility(View.INVISIBLE);
        }else{
            treeViewHolder.imageButtonExpande.setVisibility(View.VISIBLE);
        }
        if(viewModel.isExpanded()){
            treeViewHolder.imageButtonExpande.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        }else{
            treeViewHolder.imageButtonExpande.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
        treeViewHolder.imageButtonExpande.setTag(viewModel);
        treeViewHolder.textViewLabel.setText(viewModel.getLabel());
        if(viewModel.getDrawable()!=null){
            treeViewHolder.imageViewIcon.setImageDrawable(viewModel.getDrawable());
            treeViewHolder.imageViewIcon.setVisibility(View.VISIBLE);
        }else{
            treeViewHolder.imageViewIcon.setVisibility(View.GONE);
        }
        if(selectionType == TreeView.SelectionType.MULTIPLE_SELECTION || selectionType == TreeView.SelectionType.SINGLE_CHECK_SELECTION){
            treeViewHolder.checkBox.setVisibility(View.VISIBLE);
            treeViewHolder.checkBox.setChecked(viewModel.isSelected());
        }else{
            treeViewHolder.checkBox.setVisibility(View.GONE);
        }
        treeViewHolder.checkBox.setTag(viewModel);
    }

    @Override
    protected void setDeselectedStyle(ViewHolder holder, int position) {
        super.setDeselectedStyle(holder, position);
        if(colors!=null && colors.length > 0){
            TreeItemViewModel t = getItem(position);
            int index = t.getLevel();
            if(colors.length <= index){
                index = colors.length-1;
            }
            holder.changeBackground(colors[index]);
        }
    }

    protected void exdpandeCollapse(TreeItemViewModel treeItemViewModel){
        if(treeItemViewModel.isExpanded()){
            collapse(treeItemViewModel);
        }else{
            int position = getPosition(treeItemViewModel)+1;
            for(TreeItemViewModel tivm : treeItemViewModel.getChilds()){
                viewModels.add(position, tivm);
                position++;
            }
            treeItemViewModel.setExpanded(true);
        }
        notifyDataSetChanged();
    }

    protected void collapse(TreeItemViewModel treeItemViewModel){
        for(TreeItemViewModel tivm : treeItemViewModel.getChilds()){
            collapse(tivm);
            int position = getPosition(tivm);
            if(position >= 0) {
                viewModels.remove(position);
                treeItemViewModel.setExpanded(false);
            }
        }
    }

    public List<TreeItemViewModel> getItemsSelected() {
        return itemsSelected;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    @Override
    public boolean isEquals(TreeItemViewModel model, TreeItemViewModel modelToCompare) {
        return !(model == null || modelToCompare == null) && model.getId().equals(modelToCompare.getId());
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new TreeViewHolder(view);
    }

    public void setSelectionType(TreeView.SelectionType selectionType) {
        this.selectionType = selectionType;
    }

    @Override
    public void swapData(List<TreeItemViewModel> newList) {
        checkSelected(newList);
        super.swapData(newList);
    }

    private void checkSelected(List<TreeItemViewModel> list){
        for(TreeItemViewModel t : list){
            if(t.isSelected() && !isSelected(t)){
                itemsSelected.add(t);
                checkSelected(t.getChilds());
            }
        }
    }

    private boolean isSelected(TreeItemViewModel t){
        for(TreeItemViewModel t1 : itemsSelected){
            if(t1.getId().equals(t.getId())){
                return true;
            }
        }
        return false;
    }

    protected class TreeViewHolder extends ViewHolder {

        ImageButton imageButtonExpande;
        TextView textViewLabel;
        CheckBox checkBox;
        View rootView;
        ImageView imageViewIcon;
        //RelativeLayout rootCell;

        protected TreeViewHolder(View view) {
            super(view);
            rootView = view;
            rootView.setTag(getPaddingStart(rootView));
            //  rootCell = (RelativeLayout) view.findViewById(R.id.root_cell);
            imageViewIcon = (ImageView) rootView.findViewById(R.id.imageview_treeview);
            imageButtonExpande = (ImageButton) view.findViewById(R.id.btn_expande);
            imageButtonExpande.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exdpandeCollapse((TreeItemViewModel) view.getTag());
                }
            });
            textViewLabel = (TextView) view.findViewById(R.id.textview_label);
            checkBox = (CheckBox) view.findViewById(R.id.check_selected);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!isOnBind) {
                        TreeItemViewModel treeItemViewModel = (TreeItemViewModel) checkBox.getTag();
                        if (treeItemViewModel != null) {
                            if (selectionType == TreeView.SelectionType.SINGLE_CHECK_SELECTION) {
                                if (itemsSelected.size() > 0 && !itemsSelected.contains(treeItemViewModel)) {
                                    checkBox.setChecked(false);
                                    return;
                                } else if (itemsSelected.size() > 0) {
                                    itemsSelected.remove(0);
                                }
                            }
                            itemsSelected.add(treeItemViewModel);
                            treeItemViewModel.setSelected(b);
                        }
                    }
                }
            });
        }

        public void addPadding(int padding){
            int paddingStartInitial = (Integer) rootView.getTag();
            rootView.setPadding(paddingStartInitial + padding, rootView.getPaddingTop(), getPaddingEnd(rootView), rootView.getPaddingBottom());
        }
    }

    private int getPaddingStart(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getPaddingStart();
        }
        return view.getPaddingLeft();
    }

    private int getPaddingEnd(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getPaddingEnd();
        }
        return view.getPaddingRight();
    }
}