package it.smasini.utility.library.adapters;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public class SpinnerItem {

    private String label, value, extraData;
    private Object tag;

    public SpinnerItem(String label, String value, Object tag) {
        this(label, value);
        this.tag = tag;
    }

    public SpinnerItem(String label, String value, String extraData) {
        this(label, value);
        this.extraData = extraData;
    }

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

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
