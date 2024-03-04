package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

import javax.swing.*;

import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {

        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset * 2,
            screenSize.height - inset * 2);

        setContentPane(desktopPane);

        addWindow(initLogWindow(10, 10, 300, 800));
        addWindow(initGameWindow(320, 10, 400, 400));

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeWithConfirmation();
            }
        });
    }
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected void closeWithConfirmation() {
        if (JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите закрыть приложение?",
                "Подтверждение закрытия",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            ) == JOptionPane.YES_OPTION)
        {
            dispose();
            System.exit(0);
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

    protected LogWindow initLogWindow(int x, int y, int width, int height) {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(x, y);
        logWindow.setSize(width, height);

        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow initGameWindow(int x, int y, int width, int height) {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setLocation(x, y);
        gameWindow.setSize(width, height);
        return gameWindow;
    }
    
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
