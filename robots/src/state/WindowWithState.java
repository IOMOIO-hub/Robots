package state;

import javax.swing.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class WindowWithState extends JInternalFrame implements Storable {

    private final String pathToStateFile = System.getProperty("user.home") + File.separator + this.title + ".dat";

    public WindowWithState(String title) {
        super(title, true, true, true, true);
    }

    public WindowState getState() {
        return new WindowState(this);
    }

    public void store() {
        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get(pathToStateFile)))) {
            stream.writeObject(this.getState());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
    public void restore() {
        try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(Paths.get(pathToStateFile)))) {
            WindowState state = (WindowState) stream.readObject();
            this.setLocation(state.getLocation());
            this.setSize(state.getSize());
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }
}