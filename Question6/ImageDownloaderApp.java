package Question6;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class ImageDownloaderApp {
    private JFrame frame;
    private JTextField textField;
    private JButton downloadButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton cancelButton;
    private JPanel statusPanel;
    private JScrollPane scrollPane;  // Added JScrollPane
    private ExecutorService threadPool;
    private CopyOnWriteArrayList<DownloadTask> downloadTasks;

    public ImageDownloaderApp() {
        frame = new JFrame("Image Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);  // Set a fixed size

        frame.setLayout(null);

        JLabel label = new JLabel("URL:");
        label.setBounds(10, 10, 80, 30);
        frame.add(label);

        textField = new JTextField();
        textField.setBounds(50, 10, 380, 30);
        frame.add(textField);

        downloadButton = new JButton("Download");
        downloadButton.setBounds(460, 10, 100, 30);
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDownloadButtonClick();
            }
        });
        frame.add(downloadButton);

        pauseButton = new JButton("Pause");
        pauseButton.setBounds(110, 50, 80, 30);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePauseButtonClick();
            }
        });
        frame.add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.setBounds(200, 50, 100, 30);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleResumeButtonClick();
            }
        });
        frame.add(resumeButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(310, 50, 80, 30);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancelButtonClick();
            }
        });
        frame.add(cancelButton);

        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

        // Create JScrollPane and add statusPanel to it
        scrollPane = new JScrollPane(statusPanel);
        scrollPane.setBounds(50, 90, 500, 250);

        // Set horizontal scrollbar policy to NEVER
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Set a preferred size for the statusPanel
        statusPanel.setPreferredSize(new Dimension(480, 1000));

        frame.add(scrollPane);

        threadPool = Executors.newFixedThreadPool(5);
        downloadTasks = new CopyOnWriteArrayList<>();

        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void handleDownloadButtonClick() {
        String url = textField.getText();
        if (!url.isEmpty()) {
            // Check if the URL is valid and represents an image
            if (isValidImageURL(url)) {
                // Generate a unique file name using a timestamp
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timestamp = dateFormat.format(new Date());
                String fileName = "image_" + timestamp + ".png";
    
                // Construct the full path to the desktop with the unique file name
                Path savePath = Paths.get(System.getProperty("user.home"), "Desktop", fileName);
    
                DownloadTask downloadTask = new DownloadTask(url);
                downloadTask.setSavePath(savePath);
                downloadTasks.add(downloadTask);
                threadPool.submit(downloadTask);
    
                // Enable pause and resume buttons after initiating download
                pauseButton.setEnabled(true);
                resumeButton.setEnabled(true);
            } else {
                // Show popup for invalid or non-image URL
                JOptionPane.showMessageDialog(frame, "Invalid or non-image URL. Please enter a valid image URL.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Please enter an image URL.", "Empty Field", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidImageURL(String url) {
        try {
            // Attempt to read the image using ImageIO
            @SuppressWarnings("deprecation")
            BufferedImage image = ImageIO.read(new URL(url));
    
            // Check if the image is null, indicating that the URL does not represent a valid image
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }

    private void handlePauseButtonClick() {
        for (DownloadTask task : downloadTasks) {
            if (task.isActive()) {
                task.pause();
            }
        }
    }

    private void handleResumeButtonClick() {
        for (DownloadTask task : downloadTasks) {
            if (task.isPaused()) {
                task.resume();
            }
        }
    }

    private void handleCancelButtonClick() {
        for (DownloadTask task : downloadTasks) {
            if (task.isActive() || task.isPaused()) {
                task.cancel(true);
                task.updateStatus("Cancelled");
            }
        }

        // Disable pause and resume buttons after canceling
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
    }

    private class DownloadTask extends SwingWorker<Void, Integer> {
        private String url;
        private boolean paused;
        private Path savePath;
        private JProgressBar progressBar;
        private JLabel statusLabel;

        public DownloadTask(String url) {
            this.url = url;
            this.paused = false;
            this.progressBar = new JProgressBar(0, 100);
            this.statusLabel = new JLabel();
            initializeUI();
        }

        public void setSavePath(Path savePath) {
            this.savePath = savePath;
        }

        private void initializeUI() {
            statusLabel.setText("Downloading - " + url);
            statusPanel.add(statusLabel);
            statusPanel.add(progressBar);
            frame.validate();
        }

        public boolean isActive() {
            return !isCancelled() && !isDone() && !isPaused();
        }

        public boolean isPaused() {
            return paused;
        }

        public void pause() {
            paused = true;
            updateStatus("Paused");
        }

        public void resume() {
            paused = false;
            updateStatus("Resumed");
        }

        @Override
        protected Void doInBackground() throws Exception {
            Path tempPath = savePath.resolveSibling(savePath.getFileName() + ".temp");
        
            try {
                @SuppressWarnings("deprecation")
                URL imageUrl = new URL(url);
                try (InputStream in = imageUrl.openStream()) {
                    // Save the image to the temporary file
                    Files.copy(in, tempPath);
                }
                for (int progress = 0; progress < 100; progress++) {
                    if (isCancelled()) {
                        // Delete the temporary file if download was canceled
                        Files.deleteIfExists(tempPath);
                        return null;
                    }
        
                    // Check if paused
                    while (paused) {
                        Thread.sleep(50); // Sleep while paused
                    }
        
                    publish(progress);
                    try {
                        Thread.sleep(50); // Simulate download delay
                    } catch (InterruptedException e) {
                        // Restore the interrupted status
                        Thread.currentThread().interrupt();
                    }
                }
        
                // Move the temporary file to the final destination upon completion
                Files.move(tempPath, savePath);
            } catch (IOException e) {
                updateStatus("Error: " + e.getMessage());
            }
            return null;
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            int progress = chunks.get(chunks.size() - 1);
            progressBar.setValue(progress);
            updateStatus("Downloading (" + progress + "%)");
        }

        @Override
        protected void done() {
            updateStatus("Completed");
        }

        private void updateStatus(String status) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText(status + " - " + url);
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImageDownloaderApp();
            }
        });
    }
}
