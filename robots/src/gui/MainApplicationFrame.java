package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import javax.swing.*;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */

public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private WindowState windowState;
    private File file = new File(System.getProperty("user.home") + "/WindowSettings.dat");

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);

        addWindow(gameWindow);

        if (file.exists()) {
            try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                HashMap<String, WindowState> frameData = (HashMap) stream.readObject();
                for (var e: frameData.entrySet())
                {
                    System.out.println(e.getKey()+ e.getValue().getName());
                }
                for (JInternalFrame f : desktopPane.getAllFrames())
                {
                    System.out.println(f.getTitle());
                    if (frameData.containsKey(f.getTitle()))

                        f.setIcon(frameData.get(f.getTitle()).getIconified());
                    f.setMaximum(frameData.get(f.getTitle()).getMaximized());
                    f.setLocation(frameData.get(f.getTitle()).getLocation());
                    f.setSize(frameData.get(f.getTitle()).getDimension());
                }

                if(frameData.get("Main").getIconified()) setExtendedState(ICONIFIED);
                else if (frameData.get("Main").getMaximized()) setExtendedState(MAXIMIZED_BOTH);
                setSize(frameData.get("Main").getDimension());
                setLocation(frameData.get("Main").getLocation());
                windowState = frameData.get("Main");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            windowState = new WindowState(false, true, Toolkit.getDefaultToolkit().getScreenSize(), new Point(0,0), "Main");
            System.out.println(windowState.getName());
            setExtendedState(MAXIMIZED_BOTH);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                windowState.setIconified(true);
                System.out.println("Iconified");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                windowState.setIconified(false);
                if(windowState.getMaximized())
                    setExtendedState(MAXIMIZED_BOTH);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowState.setDimension(e.getComponent().getSize());
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH)
                    windowState.setMaximized(true);
                else
                    windowState.setMaximized(false);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                windowState.setLocation(e.getComponent().getLocation());
            }
        });

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());

        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        JMenu exitMenu = new JMenu("Выход");
        {
            JMenuItem subExitItem = new JMenuItem("Закрыть приложение", KeyEvent.VK_E);
            subExitItem.addActionListener((event) -> {
                confirmExit();
            });
            exitMenu.add(subExitItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    public void confirmExit()
    {
        JFrame frame = new JFrame("Exit confirmation frame");
        String[] options = { "Да", "Нет" };
        int n = JOptionPane.showOptionDialog(frame, "Вы действительно хотите закрыть приложение?",
                "Подтверждение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (n == 0) {
            try (ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                HashMap<String, WindowState> frames = new HashMap<>();
                    for (var w: desktopPane.getAllFrames()){
                    frames.put(w.getTitle(), new WindowState(w.isIcon(), w.isMaximum(), w.getSize(), w.getLocation(), w.getTitle()));
                        System.out.println("ExitInfo"+w.getTitle()+w.isIcon()+w.isMaximum()+w.getSize()+w.getLocation());
                    }
                    frames.put("Main", windowState);
                    stream.writeObject(frames);
                System.out.println("ExitInfo"+windowState.getName()+windowState.getIconified()+windowState.getLocation()+windowState.getMaximized());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}