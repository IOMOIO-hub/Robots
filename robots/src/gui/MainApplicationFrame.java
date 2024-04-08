package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;

import javax.swing.*;

import model.Robot;
import log.Logger;
import state.WindowWithState;

public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        setContentPane(desktopPane);

        Robot robot = new Robot(100, 100);

        LogWindow logWindow = initLogWindow();
        GameWindow gameWindow = initGameWindow(robot);
        RobotPositionWindow robotPositionWindow = new RobotPositionWindow(robot);

        addWindow(logWindow, new Point(10, 10), new Dimension(300, 400), false);
        addWindow(gameWindow, new Point(320, 10), new Dimension(400, 400), false);
        addWindow(robotPositionWindow, new Point(730, 10), new Dimension(300, 100), false);
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
            saveStateConfirmation();
            for (JInternalFrame frame : desktopPane.getAllFrames())
                frame.dispose();
            dispose();
        }
    }

    protected void saveStateConfirmation() {
        if (JOptionPane.showConfirmDialog(
                this,
                "Сохранить текущее состояние окон?",
                "Сохранение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION) {
            storeState();
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
        try {
            window.setIcon(isIcon);
        } catch (PropertyVetoException ignored) {
        }
    }

    protected LogWindow initLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());

        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow initGameWindow(Robot robot) {
        return new GameWindow(robot);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ignore) {
        }
    }

    private void storeState() {
        for (JInternalFrame frame : desktopPane.getAllFrames())
            if (frame instanceof WindowWithState)
                ((WindowWithState) frame).store();
    }

    private void restoreState() {
        for (JInternalFrame frame : desktopPane.getAllFrames())
            if (frame instanceof WindowWithState)
                ((WindowWithState) frame).restore();
    }
}
