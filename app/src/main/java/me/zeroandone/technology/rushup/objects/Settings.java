package me.zeroandone.technology.rushup.objects;


public class Settings {
    public static final int HEADER = 0;
    public static final int OPTION = 1;
    public static final int Notification = 2;
    String title;
    String options;
    int types;

    public Settings(String title, String options, int type) {
        this.title = title;
        this.options = options;
        this.types = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getType() {
        return types;
    }

    public void setType(int type) {
        this.types = type;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", options='" + options + '\'' +
                ", type=" + types +
                '}';
    }
}
