package edu.cmu.cs.cs214.hw4.core;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TestTiles {
    private TestTileBean tb;

    TestTiles() {
        Yaml yaml = new Yaml();
        try (
                InputStream is = new FileInputStream("src/main/resources/TestTiles.yml")) {
            tb =  yaml.loadAs(is, TestTileBean.class);
        } catch (
                FileNotFoundException e) {
            throw new IllegalArgumentException("File " + "src/main/resources/TestTiles.yml" + " not found!");
        } catch (
                IOException e) {
            throw new IllegalArgumentException("Error when reading " + "src/main/resources/TestTiles.yml" + "!");
        }

    }

    public TestTileBean getTiles() { return tb; }
}
