import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple music player application that allows playback of audio files with cover art display.
 *
 * Programmer: [Your Name]
 * Date: [Date]
 */
public class MusicPlayer extends JFrame implements ActionListener, ChangeListener {
    private JButton playButton, stopButton, previousButton, nextButton;
    private Clip audioClip;
    private List<File> songFiles;
    private int currentSongIndex;
    private long pausedPosition;
    private JLabel coverLabel;

    public MusicPlayer() {
        initializeUI();
        loadSongFiles();
    }

    /**
     * Initializes the graphical user interface of the music player.
     */
    private void initializeUI() {
        setTitle("Music Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        playButton = new JButton(">");
        playButton.addActionListener(this);

        stopButton = new JButton("■");
        stopButton.addActionListener(this);

        previousButton = new JButton("<<");
        previousButton.addActionListener(this);

        nextButton = new JButton(">>");
        nextButton.addActionListener(this);

        coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(200, 200)); // Set the size of the cover art

        JPanel controlsPanel = new JPanel(new FlowLayout());
        controlsPanel.add(previousButton);
        controlsPanel.add(playButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(nextButton);

        JPanel coverPanel = new JPanel(new FlowLayout());
        coverPanel.add(coverLabel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(coverPanel, BorderLayout.CENTER);
        centerPanel.add(controlsPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            togglePlayback();
        } else if (e.getSource() == stopButton) {
            stopMusic();
        } else if (e.getSource() == previousButton) {
            playPreviousSong();
        } else if (e.getSource() == nextButton) {
            playNextSong();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // Implement the ChangeListener method here
        // This method will be called when the state of the JSlider changes
        // You can update the playback position of the audioClip here
    }

    /**
     * Toggles playback of the audio clip.
     */
    private void togglePlayback() {
        if (audioClip != null) {
            if (audioClip.isRunning()) {
                pauseMusic();
            } else {
                resumeMusic();
            }
        } else {
            playNextSong();
        }
    }

    /**
     * Loads audio files from a specified directory into the songFiles list.
     */
    private void loadSongFiles() {
        songFiles = new ArrayList<>();
        File songsFolder = new File("/Users/nikhil/Desktop/MusicPlayer/Songs");
        if (songsFolder.exists() && songsFolder.isDirectory()) {
            File[] files = songsFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".wav")) {
                        songFiles.add(file);
                    }
                }
            }
        }
    }

    /**
     * Plays the previous song in the playlist.
     */
    private void playPreviousSong() {
        if (!songFiles.isEmpty()) {
            stopMusic();
            currentSongIndex = (currentSongIndex - 1 + songFiles.size()) % songFiles.size();
            playMusic(songFiles.get(currentSongIndex));
        }
    }

    /**
     * Plays the next song in the playlist.
     */
    private void playNextSong() {
        if (!songFiles.isEmpty()) {
            stopMusic();
            currentSongIndex = (currentSongIndex + 1) % songFiles.size();
            playMusic(songFiles.get(currentSongIndex));
        }
    }

    /**
     * Plays the audio clip from the specified file and displays its cover art.
     */
    private void playMusic(File audioFile) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);

            if (pausedPosition > 0) {
                audioClip.setMicrosecondPosition(pausedPosition);
            }

            displayCoverArt(audioFile); // Display cover art for the current song
            audioClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pauses the currently playing music.
     */
    private void pauseMusic() {
        if (audioClip != null && audioClip.isRunning()) {
            pausedPosition = audioClip.getMicrosecondPosition();
            audioClip.stop();
        }
    }

    /**
     * Resumes playback of the music.
     */
    private void resumeMusic() {
        if (audioClip != null && !audioClip.isRunning()) {
            audioClip.start();
        }
    }

    private void stopMusic() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
        pausedPosition = 0;
    }

    private void displayCoverArt(File audioFile) {
        // Load and set the cover image for the current song
        String coverImagePath = getCoverImagePath(audioFile);
        if (coverImagePath != null) {
            ImageIcon icon = new ImageIcon(coverImagePath);
            coverLabel.setIcon(icon);
        } else {
            coverLabel.setIcon(null); // Clear the cover image if not available
        }
    }

    private String getCoverImagePath(File audioFile) {
        // Replace this with your logic to fetch the cover image path for the given audio file
        // For example, you can store cover images in the same directory as the audio files
        // with similar names and extensions.
        return null; // Return the actual cover image path or null if not available
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicPlayer());
    }
}

