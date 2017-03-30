package com.aerokube.selenoid.misc;

public enum Page {
    
    FIRST("first.html"),
    SECOND("second.html"),
    HOTKEYS("hotkeys.html"),
    ALERT("alert.html"),
    FRAMES("frames.html"),
    DRAG("drag.html");
    
    private final String name;

    Page(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

