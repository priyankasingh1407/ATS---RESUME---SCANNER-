
import java.awt.*;
import java.io.File;
import java.util.*;
import javax.swing.*;

public class MainWindow extends JFrame {

    private JTextArea jobDescription;
    private JTextArea output;
    private File selectedResume;

    private final ResumeParser parser = new ResumeParser();
    private final KeywordExtractor extractor = new KeywordExtractor();
    private final Scorer scorer = new Scorer();
    private final RankingService rankingService =
        new RankingService();

    public MainWindow() {

        setTitle("ATS Resume Scanner");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(
            "ATS RESUME SCANNER", JLabel.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        main.add(heading, BorderLayout.NORTH);

        jobDescription = new JTextArea();
        jobDescription.setLineWrap(true);
        jobDescription.setWrapStyleWord(true);

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel jdPanel = new JPanel(new BorderLayout());
        jdPanel.add(
            new JLabel("Paste Job Description:"),
            BorderLayout.NORTH
        );
        jdPanel.add(new JScrollPane(jobDescription));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(
            new JLabel("Resume Analysis Result:"),
            BorderLayout.NORTH
        );
        resultPanel.add(new JScrollPane(output));

        center.add(jdPanel);
        center.add(resultPanel);

        main.add(center, BorderLayout.CENTER);

        JButton uploadButton = new JButton("Upload Resume");
        JButton analyzeButton = new JButton("Analyze Resume");
        JButton rankButton = new JButton("Rank Multiple Resumes");
        JButton clearButton = new JButton("Clear");

        JPanel buttons = new JPanel();

        buttons.add(uploadButton);
        buttons.add(analyzeButton);
        buttons.add(rankButton);
        buttons.add(clearButton);

        main.add(buttons, BorderLayout.SOUTH);

        uploadButton.addActionListener(e -> uploadResume());
        analyzeButton.addActionListener(e -> analyzeResume());
        rankButton.addActionListener(e -> rankResumes());

        clearButton.addActionListener(e -> {
            jobDescription.setText("");
            output.setText("");
            selectedResume = null;
        });

        add(main);
    }

    private void uploadResume() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Select TXT Resume");

        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Resume (*.txt)", "txt"
            )
        );

        int choice = chooser.showOpenDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {

            selectedResume = chooser.getSelectedFile();

            JOptionPane.showMessageDialog(
                this,
                "Resume Uploaded Successfully!"
            );
        }
    }

    private void analyzeResume() {

        try {
            if (selectedResume == null) {
                JOptionPane.showMessageDialog(
                    this, "Please upload a resume first!"
                );
                return;
            }

            String jd = jobDescription.getText();

            if (jd.isBlank()) {
                JOptionPane.showMessageDialog(
                    this, "Please enter Job Description!"
                );
                return;
            }

            String resumeText =
                parser.extractText(selectedResume);

            Map<String, Integer> resumeKeywords =
                extractor.extract(resumeText);

            Map<String, Integer> jdKeywords =
                extractor.extract(jd);

            ScoreResult result =
                scorer.score(resumeKeywords, jdKeywords);

            output.setText(
                "========== ATS RESUME REPORT ==========\n\n" +
                "Resume: " + selectedResume.getName() + "\n\n" +
                "ATS SCORE: " +
                String.format("%.2f", result.getScore()) +
                " / 100\n\n" +
                "MATCHED KEYWORDS:\n" +
                result.getMatched() + "\n\n" +
                "MISSING KEYWORDS:\n" +
                result.getMissing() + "\n\n" +
                "========== END OF REPORT =========="
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Error: " + ex.getMessage()
            );
        }
    }

    private void rankResumes() {

        String jd = jobDescription.getText();

        if (jd.isBlank()) {
            JOptionPane.showMessageDialog(
                this, "Please enter Job Description!"
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Resume (*.txt)", "txt"
            )
        );

        int choice = chooser.showOpenDialog(this);

        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            java.util.List<File> files =
                Arrays.asList(chooser.getSelectedFiles());

            java.util.List<ScoreResult> ranking =
                rankingService.rankResumes(files, jd);

            StringBuilder report = new StringBuilder();

            report.append("========== RESUME RANKING ==========\n\n");

            int rank = 1;

            for (ScoreResult item : ranking) {

                report.append("Rank: ").append(rank++).append("\n");
                report.append("Resume: ")
                    .append(item.getFileName()).append("\n");

                report.append("ATS Score: ")
                    .append(String.format("%.2f", item.getScore()))
                    .append("%\n");

                report.append("Matched Keywords: ")
                    .append(item.getMatched()).append("\n\n");
            }

            output.setText(report.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Error: " + ex.getMessage()
            );
        }
    }
}
