package it.smasini.utility.library.adapters;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public class SpinnerItem {

    private String label, value;

    public SpinnerItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public SpinnerItem() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
