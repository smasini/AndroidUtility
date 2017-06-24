package it.smasini.utility.library.ui.tree;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import it.smasini.utility.library.adapters.BaseAdapter;

/**
 * Created by Simone on 27/01/17.
 */

public class TreeView extends RecyclerView {

    private SelectionType selectionType = SelectionType.MULTIPLE_SELECTION;
    private TreeViewAdapter adapter;
    private int[] colors;

    public TreeView(Context context) {
        super(context);
        init(context, null);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new TreeViewAdapter(context);
        adapter.setSelectionType(selectionType);
        setAdapter(adapter);


    }

    public List<TreeItemViewModel> getItemsSelected(){
        return adapter.getItemsSelected();
    }

    public void setItems(List<TreeItemViewModel> items){
        adapter.swapData(items);
    }

    public void setAdapter(TreeViewAdapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
    }

    @Override
    public BaseAdapter<TreeItemViewModel> getAdapter() {
        return adapter;
    }

    public void setSelectionType(SelectionType selectionType) {
        this.selectionType = selectionType;
        adapter.setSelectionType(selectionType);
    }

    public void setOnClickSelection(BaseAdapter.OnClickHandler<TreeItemViewModel> onClickHandler){
        this.selectionType = SelectionType.SINGLE_CLICK_SELECTION;
        adapter.setSelectionType(selectionType);
        adapter.setOnClickHandler(onClickHandler);
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        adapter.setColors(colors);
    }

    public int[] getColors() {
        return colors;
    }

    public enum SelectionType {
        MULTIPLE_SELECTION,
        SINGLE_CHECK_SELECTION,
        SINGLE_CLICK_SELECTION
    }
}
