package it.smasini.utility.library.ui.tree;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by Simone on 27/01/17.
 */

public class TreeItemViewModel implements Cloneable {

    private String id, label;
    private int level;
    private List<TreeItemViewModel> childs;
    private boolean expanded, selected;
    private Drawable drawable;

    public TreeItemViewModel(String id, String label) {
        this(id, label, 0);
    }

    public boolean hasChilds(){
        if(childs!=null)
            return childs.size() > 0;
        return false;
    }

    public TreeItemViewModel(String id, String label, int level) {
        this.id = id;
        this.label = label;
        this.level = level;
        this.expanded = false;
        this.selected = false;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public List<TreeItemViewModel> getChilds() {
        return childs;
    }

    public void setChilds(List<TreeItemViewModel> childs) {
        this.childs = childs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public TreeItemViewModel clone(){
        TreeItemViewModel treeItemViewModel = new TreeItemViewModel(id, label, level);
        treeItemViewModel.setExpanded(expanded);
        treeItemViewModel.setSelected(selected);
        treeItemViewModel.setDrawable(drawable.mutate());
        /*List<TreeItemViewModel> clonedChilds = new ArrayList<>();
        if(childs!=null) {
            for (TreeItemViewModel t : childs) {
                clonedChilds.add(t.clone());
            }
        }
        treeItemViewModel.setChilds(clonedChilds);*/
        return treeItemViewModel;
    }
}
