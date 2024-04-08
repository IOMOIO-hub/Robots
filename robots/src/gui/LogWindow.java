package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;
import state.WindowState;
import state.WindowWithState;

public class LogWindow extends WindowWithState implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы");
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    public WindowState getState() {
        WindowState state = new WindowState(this);
        state.setAdditionalInfo("Некторая дополнительная информация");
        return state;
    }
    public LogWindow(WindowState state) {
        this(Logger.getDefaultLogSource());
//         Могу использовать здесь state.getAdditionalInfo()
    }
}
