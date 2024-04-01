package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;

import log.Logger;
import state.WindowState;
import state.WindowWithState;

public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ArrayList<WindowWithState> windows = new ArrayList<>();

    public MainApplicationFrame() {
        setContentPane(desktopPane);

        restoreState();

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                closeWithConfirmation();
            }
        });
    }

    protected void closeWithConfirmation() {
        if (JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите закрыть приложение?",
                "Подтверждение закрытия",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION) {
            storeState();
            for (WindowWithState window: windows)
                window.dispose();
            dispose();
        }
    }

    private JMenu createMenu(String title, int mnemonic, String description) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private JMenuItem createMenuItem(String title, KeyStroke key, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setAccelerator(key);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu mainMenu = createMenu("Menu", KeyEvent.VK_D, "Главное меню");
        menuBar.add(mainMenu);

        mainMenu.add(createMenuItem(
                        "Quit",
                        KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK),
                        (event) -> closeWithConfirmation()
                )
        );

        JMenu lookAndFeelMenu = createMenu("Режим отображения", KeyEvent.VK_V,
                "Управление режимом отображения приложения");
        menuBar.add(lookAndFeelMenu);

        lookAndFeelMenu.add(createMenuItem(
                "Системная схема",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK),
                (event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                })
        );
        lookAndFeelMenu.add(createMenuItem(
                "Универсальная схема",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK),
                (event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                })
        );

        JMenu testMenu = createMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        menuBar.add(testMenu);

        testMenu.add(createMenuItem(
                "Сообщение в лог",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK),
                (event) -> Logger.debug("Новая строка"))
        );

        return menuBar;
    }

    protected void addWindow(JInternalFrame window) {
        desktopPane.add(window);
        window.setVisible(true);
    }

    protected void addWindow(JInternalFrame window, Point location, Dimension size, boolean isIcon) {
        window.setLocation(location);
        window.setSize(size);
        addWindow(window);
        try { window.setIcon(isIcon); } catch (PropertyVetoException ignored) {}
    }

    protected void addDefaultWindows() {
        LogWindow logWindow = initLogWindow();
        windows.add(logWindow);
        addWindow(logWindow, new Point(10, 10), new Dimension(300, 400), false);

        GameWindow gameWindow = initGameWindow();
        windows.add(gameWindow);
        addWindow(gameWindow, new Point(320, 10), new Dimension(400, 400), false);
    }

    protected LogWindow initLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());

        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow initGameWindow() {
        return new GameWindow();
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    private void storeState() {
        String stateFilePath = System.getProperty("user.home") + File.separator + "state.dat";
        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get(stateFilePath)))) {
            for (WindowWithState window: windows)
                stream.writeObject(window.getState());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    private void restoreState() {
        String stateFilePath = System.getProperty("user.home") + File.separator + "state.dat";
        try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(Paths.get(stateFilePath)))) {
            while (true) {
                WindowState state = (WindowState) stream.readObject();
                WindowWithState window;
                switch (state.getTitle()) {
                    case "Игровое поле": window = new GameWindow(state); break;
                    case "Протокол работы": window = new LogWindow(state); break;
                    default: window = null;
                }
                if (window != null) {
                    windows.add(window);
                    addWindow(window, state.getLocation(), state.getSize(), state.getIsIcon());
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            if (windows.isEmpty())
                addDefaultWindows();
        }
    }
}
