package it.smasini.utility.library.activities;

/**
 * Created by smasini on 11/08/16.
 */
public class DrawerFragmentType {

    private int resTitle;
    private String tag;

    public DrawerFragmentType(int resTitle, String tag) {
        this.resTitle = resTitle;
        this.tag = tag;
    }

    public int getResTitle() {
        return resTitle;
    }

    public void setResTitle(int resTitle) {
        this.resTitle = resTitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
