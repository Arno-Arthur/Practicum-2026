package ru.omstu.fitprogwork;

import java.util.Objects;

public class ExtractionRequest {
    private String type;
    private String data;
    private String path;

    // Геттеры и сеттеры
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtractionRequest that)) return false;
        return Objects.equals(type, that.type) &&
                Objects.equals(data, that.data) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data, path);
    }
}