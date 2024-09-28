import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Gui extends JFrame {
    public static final String FOLDER_PATH = "files";

    public Gui() {

        super("Delete Files");

        setSize(563, 392);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        addGuiComponents();

    }

    private void addGuiComponents() {

        JButton deleteButton = new JButton("Delete Files");

        deleteButton.setFont(new Font("Dialog", Font.BOLD, 48));

        add(deleteButton, BorderLayout.CENTER);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File folder = new File(FOLDER_PATH);

                if (folder.isDirectory()) {
                    File[] files = folder.listFiles();

                    if (files.length > 0) {
                        deleteFiles(files);
                    } else {
                        showResultDialog("No Files to Delete");
                    }
                }
            }
        });

    }

    private void showResultDialog(String message) {
        JDialog resultDialog = new JDialog(this, "Result", true);

        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        resultDialog.setSize(300, 150);
        resultDialog.setLocationRelativeTo(null);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Dialog", Font.BOLD, 26));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultDialog.add(messageLabel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Dialog", Font.BOLD, 18));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resultDialog.dispose();
            }
        });

        resultDialog.add(confirmButton, BorderLayout.SOUTH);
        resultDialog.setVisible(true);
    }

    private void deleteFiles(File[] files) {
        JDialog loadingDialog = new JDialog(this, "Deleting Files", true);
        loadingDialog.setSize(300, 100);
        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loadingDialog.setLocationRelativeTo(this);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setFont(new Font("Dialog", Font.BOLD, 18));
        progressBar.setForeground(Color.decode("#2c8558"));
        progressBar.setValue(0);

        Thread deleteFilesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int totalFiles = files.length;

                int filesDeleted = 0;

                int progress;

                for (File file : files) {

                    if (file.delete()) {
                        filesDeleted++;

                        progress = (int) ((((double) filesDeleted / totalFiles) * 100));

                        try {
                            Thread.sleep(60);

                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();

                        }

                        progressBar.setValue(progress);

                    }
                }
                if (loadingDialog.isVisible()) {
                    loadingDialog.dispose();
                }

                showResultDialog("Files Deleted");

            }
        });

        deleteFilesThread.start();

        progressBar.setStringPainted(true);
        loadingDialog.add(progressBar, BorderLayout.CENTER);
        loadingDialog.setVisible(true);


    }
}
