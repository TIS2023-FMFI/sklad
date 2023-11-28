package GUI;

public class Position {
    private String name;
    private boolean oversizedInHeight;

    public Position(String name, boolean oversizedInHeight) {
        this.name = name;
        this.oversizedInHeight = oversizedInHeight;
    }

    // zistí, či je pozícia nadrozmerná
    public boolean isOversizedInHight(){
        return true;
    }

    public String getName() {
        return name;
    }
}
