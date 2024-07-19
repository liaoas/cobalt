import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author LiAo
 * @since 2021/11/5
 */
public class SwingWorkerWithProgressBar extends JFrame {

    // 进度条组件
    private final JProgressBar progressBar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SwingWorkerWithProgressBar frame = new SwingWorkerWithProgressBar();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SwingWorkerWithProgressBar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(JBUI.Borders.empty(5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        progressBar = new JProgressBar(0, 100);
        contentPane.add(progressBar, BorderLayout.NORTH);

        JButton btnBegin = new JButton("Begin");
        btnBegin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProgressBarRealized().execute();
            }
        });
        contentPane.add(btnBegin, BorderLayout.SOUTH);
    }

    class ProgressBarRealized extends SwingWorker<Void, Integer> {

        @Override
        //后台任务在此方法中实现
        protected Void doInBackground() throws Exception {
            // 模拟有一百项任务，每次睡1s
            for (int i = 0; i < 100; i++) {
                Thread.sleep(100);
                publish(i);//将当前进度信息加入chunks中
            }
            return null;
        }

        //每次更新进度条的信息
        @Override
        protected void process(List<Integer> chunks) {
            progressBar.setValue(chunks.get(chunks.size() - 1));
        }

        @Override
        //任务完成后返回一个信息
        protected void done() {
            JOptionPane.showMessageDialog(null, "任务完成！");
        }
    }
}
