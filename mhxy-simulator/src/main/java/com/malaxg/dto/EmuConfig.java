package com.malaxg.dto;

public class EmuConfig {
    private int index;
    private int port;
    private String name;
    private String description;
    private boolean running = false;

    // getters and setters
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRunning() { return running; }
    public void setRunning(boolean running) { this.running = running; }

    @Override
    public String toString() {
        return String.format("%s (端口:%d, %s)", name, port, description);
    }
}