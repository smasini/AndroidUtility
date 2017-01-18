package it.smasini.utility.library.ui.multichoice;

/**
 * Created by Simone on 15/09/16.
 */
public class MultiChoiceModel {

    private String id;
    private String label;
    private boolean isSelected, isEnabled;

    public MultiChoiceModel() {
        this.isEnabled = true;
    }

    public MultiChoiceModel(String id, String label) {
        this.id = id;
        this.label = label;
        this.isEnabled = true;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
