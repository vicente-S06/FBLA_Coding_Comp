import javax.swing.*;
import javax.swing.text.*;

import java.io.*;
import java.awt.*; //for layout managers and more
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/* ============================================
 * FBLA Coding & Programming Competitive Event
 * CTE Department Partners 
 * Vicente Santos-Linares
 * Eli Marountas
 * ============================================*/
public class CTE_Business_Partner_Search extends JPanel implements ActionListener {
    // Data Fields
    private ArrayList<Partner> partnerList = new ArrayList<>();
    private ArrayList<JButton> buttonArr = new ArrayList<>();
    private ArrayList<Partner> filteredPartnerList = new ArrayList<>();
    private ArrayList<JButton> filteredButtonArr = new ArrayList<>();
    private Map<String, Integer> orgTypesMap = new HashMap<>();
    private int currentIndex = -1;
    private int sortedStatus = 0;
    final private int BUTTONSIZE = 35;
    private static int filesMade = 0;

    // Action command strings
    private final String deleteButtonString = "deleteButton";
    private final String filterDropDownString = "filterDropDown";
    private final String sortButtonString = "sortButton";
    private final String addButtonString = "addButton";
    private final String searchBarString = "searchBar";

    // Static GUI parts
    private JTextPane mainTextPane;
    private JPanel buttonsListPanel;
    private JScrollPane scroller;
    private static StyledDocument doc;
    private JComboBox<String> dropDown;

    // Partner Information text file
    private File partnerFile = new File("partnersList.txt");

    // Icons
    private Icon sort = new ImageIcon(new ImageIcon("sort.png").getImage().getScaledInstance(BUTTONSIZE, BUTTONSIZE,
            java.awt.Image.SCALE_SMOOTH));
    private Icon delete = new ImageIcon(new ImageIcon("delete.png").getImage().getScaledInstance(BUTTONSIZE, BUTTONSIZE,
            java.awt.Image.SCALE_SMOOTH));
    private Icon add = new ImageIcon(new ImageIcon("add.png").getImage().getScaledInstance(BUTTONSIZE, BUTTONSIZE,
            java.awt.Image.SCALE_SMOOTH));

    // Initiate contents of the JFrame
    public CTE_Business_Partner_Search() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMinimumSize(new Dimension(720, 520));
        setPreferredSize(new Dimension(720, 520));

        // Split panels between left and right
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        JPanel mainPanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(220, 520));
        mainPanel.setPreferredSize(new Dimension(500, 520));
        sidePanel.setMinimumSize(new Dimension(220, 300));
        mainPanel.setMinimumSize(new Dimension(400, 300));

        // Side Panel Split
        JPanel sideSearchPanel = new JPanel();
        sideSearchPanel.setMinimumSize(new Dimension(10, 10));
        sideSearchPanel.setPreferredSize(new Dimension(220, 120));
        sideSearchPanel.setBackground(new Color(185, 185, 185));

        // Main Panel Split
        JPanel mainEditPanel = new JPanel();
        mainEditPanel.setLayout(new BoxLayout(mainEditPanel, BoxLayout.X_AXIS));
        JPanel mainInfoPanel = new JPanel();
        // Set Sizes and Colors
        mainEditPanel.setMinimumSize(new Dimension(10, 10));
        mainInfoPanel.setMinimumSize(new Dimension(10, 10));
        mainEditPanel.setPreferredSize(new Dimension(500, 50));
        mainInfoPanel.setPreferredSize(new Dimension(500, 450));
        mainEditPanel.setBackground(new Color(190, 190, 190));
        mainInfoPanel.setBackground(new Color(210, 210, 210));

        // Borders
        sideSearchPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        sideSearchPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        mainEditPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        mainInfoPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        /*
         * ================================
         * Main Editor Panel Section
         * ================================
         */
        JButton addButton = new JButton(add);
        JButton deleteButton = new JButton(delete);
        deleteButton.addActionListener(this);
        deleteButton.setActionCommand(deleteButtonString);
        addButton.addActionListener(this);
        addButton.setActionCommand(addButtonString);

        // Button Colors
        deleteButton.setForeground(Color.black);
        deleteButton.setBackground(new Color(230, 230, 230));
        addButton.setForeground(Color.black);
        addButton.setBackground(new Color(230, 230, 230));

        // Button Sizes
        deleteButton.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        deleteButton.setMaximumSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        addButton.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        addButton.setMaximumSize(new Dimension(BUTTONSIZE, BUTTONSIZE));

        // Remove focus border
        deleteButton.setFocusPainted(false);
        addButton.setFocusPainted(false);

        // Button Spacing
        mainEditPanel.add(Box.createRigidArea(new Dimension(35, 0)));
        mainEditPanel.add(addButton);
        mainEditPanel.add(Box.createRigidArea(new Dimension(360, 0)));
        mainEditPanel.add(deleteButton);

        /*
         * ================================
         * Side Button Scroller
         * ================================
         */
        buttonsListPanel = new JPanel();
        buttonsListPanel.setLayout(new BoxLayout(buttonsListPanel, BoxLayout.Y_AXIS));
        buttonsListPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        scroller = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setViewportView(buttonsListPanel);
        // Change Scrolling speed
        scroller.getVerticalScrollBar().setUnitIncrement(5);

        scroller.setMinimumSize(new Dimension(10, 10));
        scroller.setPreferredSize(new Dimension(220, 400));

        generateParters();

        /*
         * ================================
         * Main Information Display Section
         * ================================
         */
        mainTextPane = createTextPane();
        mainTextPane.setPreferredSize(new Dimension(430, 400));
        mainTextPane.setMinimumSize(new Dimension(10, 10));
        mainTextPane.setMargin(new Insets(20, 40, 10, 40));

        mainInfoPanel.add(Box.createRigidArea(new Dimension(490, 20)));
        mainInfoPanel.add(mainTextPane);

        /*
         * ================================
         * Side Search Section
         * ================================
         */
        // Instantiate Search Bar
        JTextField searchBar = new JTextField(11);
        searchBar.setActionCommand(searchBarString);
        searchBar.addActionListener(this);
        // Labels for search bar, dropdown, and sort
        JLabel searchBarLabel = new JLabel("Search Bar: ");
        JLabel dropDownLabel = new JLabel("Filter: ");
        JLabel sortButtonLabel = new JLabel("Sort: ");

        JButton sortButton = new JButton(sort);
        dropDown = createDropDown();
        dropDown.setPreferredSize(new Dimension(155, 22));
        dropDown.setMinimumSize(new Dimension(10, 10));

        sortButton.addActionListener(this);
        sortButton.setActionCommand(sortButtonString);
        dropDown.addActionListener(this);
        dropDown.setActionCommand(filterDropDownString);

        // Button Colors
        sortButton.setForeground(Color.black);
        sortButton.setBackground(new Color(230, 230, 230));

        // Button Sizes
        sortButton.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        sortButton.setMaximumSize(new Dimension(BUTTONSIZE, BUTTONSIZE));

        sortButton.setFocusPainted(false);

        // Button Spacing
        sideSearchPanel.add(Box.createRigidArea(new Dimension(220, 5)));
        sideSearchPanel.add(searchBarLabel);
        sideSearchPanel.add(searchBar);
        sideSearchPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        sideSearchPanel.add(dropDownLabel);
        sideSearchPanel.add(dropDown);
        sideSearchPanel.add(sortButtonLabel);
        sideSearchPanel.add(sortButton);

        /*
         * ================================
         * Tie everything together
         * ================================
         */
        // Add Side Panel components
        sidePanel.add(sideSearchPanel);
        sidePanel.add(scroller);

        // Add Main Panel components
        mainPanel.add(mainEditPanel, BorderLayout.NORTH);
        mainPanel.add(mainInfoPanel, BorderLayout.CENTER);

        // Add main and side panels to Jframe
        add(sidePanel);
        add(mainPanel);
    }

    public void actionPerformed(ActionEvent e) {
        String accCmd = e.getActionCommand();
        if (deleteButtonString.equals(accCmd)) {// Delete Button
            if (currentIndex != -1) {
                // Delete from parent lists
                for (int i = 0; i < partnerList.size(); i++) {
                    if (partnerList.get(i).equals(filteredPartnerList.get(currentIndex))) {
                        partnerList.remove(i);
                        buttonArr.remove(i);
                        i = partnerList.size();
                    }
                }

                // delete from filtered lists
                filteredButtonArr.remove(currentIndex);
                filteredPartnerList.remove(currentIndex);

                mainTextPane.setText("");// Clear text
                buttonsListPanel.remove(currentIndex);// remove button
                updatePartnerButtons();
                currentIndex = -1;
            }
        } else if (filterDropDownString.equals(accCmd)) {// Filter Button
            filterPartners();
        } else if (sortButtonString.equals(accCmd)) {// Sort Button
            if (sortedStatus == 0) {// Has not been sorted yet
                sortPartnersAZ();
                sortedStatus = 1;
            } else if (sortedStatus == 1) {// It was already sorted A-Z
                sortPartnersZA();
                sortedStatus = 2;
            } else {// It was already sorted Z-A
                sortPartnersAZ();
                sortedStatus = 1;
            }
        } else if (addButtonString.equals(accCmd)) {// Add Button
            if (currentIndex != -1) {
                System.out.println("Attempting Output Report: ");
                try {

                    File newFile = new File("outputReport" + filesMade + ".txt");
                    if (newFile.createNewFile()) {
                        System.out.println("File created: " + newFile.getName());
                        System.out.println("Output Report Generated");
                    }
                    FileWriter myWriter = new FileWriter(newFile.getName());
                    myWriter.write(filteredPartnerList.get(currentIndex).toString());
                    myWriter.close();
                    filesMade++;
                } catch (IOException exep) {
                }
            }
        } else if (searchBarString.equals(accCmd)) {// Search Bar
            String pName = ((JTextField) e.getSource()).getText();// Get Input
            ((JTextField) e.getSource()).setText(""); // Empty Search bar
            searchPartners(pName); // Search for matches
            mainTextPane.setText(""); // Clears text
            currentIndex = -1; // Unselects index
        } else {// Scroller Buttons
            currentIndex = Integer.parseInt(accCmd);
            fillTextPane();
        }
    }

    public void sortPartnersAZ() {
        buttonsListPanel.removeAll();
        for (int i = 0; i < filteredPartnerList.size(); i++) {
            int lowestIndex = i;
            for (int j = i; j < filteredPartnerList.size(); j++) {
                String temp = filteredPartnerList.get(j).getPartnerName();
                if (temp.compareToIgnoreCase(filteredPartnerList.get(lowestIndex).getPartnerName()) < 0) {
                    lowestIndex = j;
                }
            }

            Partner switchPartnerTemp = filteredPartnerList.get(lowestIndex);
            JButton switchButtonTemp = filteredButtonArr.get(lowestIndex);
            filteredPartnerList.set(lowestIndex, filteredPartnerList.get(i));
            filteredButtonArr.set(lowestIndex, filteredButtonArr.get(i));
            filteredPartnerList.set(i, switchPartnerTemp);
            filteredButtonArr.set(i, switchButtonTemp);

            buttonsListPanel.add(filteredButtonArr.get(i));
        }
        updatePartnerButtons();
    }

    private void sortPartnersZA() {
        buttonsListPanel.removeAll();
        for (int i = 0; i < filteredPartnerList.size(); i++) {
            int highestIndex = i;
            for (int j = i; j < filteredPartnerList.size(); j++) {
                String temp = filteredPartnerList.get(j).getPartnerName();
                if (temp.compareToIgnoreCase(filteredPartnerList.get(highestIndex).getPartnerName()) > 0) {
                    highestIndex = j;
                }
            }

            Partner switchPartnerTemp = filteredPartnerList.get(highestIndex);
            JButton switchButtonTemp = filteredButtonArr.get(highestIndex);
            filteredPartnerList.set(highestIndex, filteredPartnerList.get(i));
            filteredButtonArr.set(highestIndex, filteredButtonArr.get(i));
            filteredPartnerList.set(i, switchPartnerTemp);
            filteredButtonArr.set(i, switchButtonTemp);

            buttonsListPanel.add(filteredButtonArr.get(i));
        }
        updatePartnerButtons();
    }

    private void filterPartners() {
        // Clear filtered lists and all buttons in scroller
        filteredButtonArr.clear();
        filteredPartnerList.clear();
        buttonsListPanel.removeAll();

        int j = 0;
        for (int i = 0; i < partnerList.size(); i++) {
            if (dropDown.getSelectedItem().equals("")
                    || dropDown.getSelectedItem().equals(partnerList.get(i).getOrgType())) {
                filteredPartnerList.add(partnerList.get(i));
                filteredButtonArr.add(buttonArr.get(i));
                buttonsListPanel.add(filteredButtonArr.get(j));
                j++;
            }

        }
        updatePartnerButtons();
    }

    private void generateParters() {
        try {
            Scanner sc = new Scanner(partnerFile);
            for (int i = 0; sc.hasNextLine(); i++) {
                String compName = sc.nextLine();
                buttonArr.add(new JButton(compName));
                buttonArr.get(i).setPreferredSize(new Dimension(204, 40));
                buttonArr.get(i).setMaximumSize(new Dimension(204, 40));
                buttonArr.get(i).setBorder(BorderFactory.createLineBorder(Color.gray));
                buttonArr.get(i).setBackground(new Color(240, 240, 240));
                buttonArr.get(i).setFocusPainted(false);
                buttonArr.get(i).addActionListener(this);
                // buttonArr.get(i).setActionCommand(i + "");

                partnerList.add(new Partner(compName, sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine()));
                filteredButtonArr.add(buttonArr.get(i));
                filteredPartnerList.add(partnerList.get(i));
                filteredButtonArr.get(i).setActionCommand(i + "");

                buttonsListPanel.add(filteredButtonArr.get(i));
                sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
        }
    }

    private void updatePartnerButtons() {
        for (int i = 0; i < filteredPartnerList.size(); i++) {
            filteredButtonArr.get(i).setActionCommand(i + "");
        }
        scroller.setViewportView(buttonsListPanel);
    }

    private void searchPartners(String name) {
        // Clear filtered lists and all buttons in scroller
        filteredButtonArr.clear();
        filteredPartnerList.clear();
        buttonsListPanel.removeAll();

        if (name.equals("")) {// If empty show everything
            for (int n = 0; n < partnerList.size(); n++) {
                if (dropDown.getSelectedItem().equals("")
                        || dropDown.getSelectedItem().equals(partnerList.get(n).getOrgType())) {
                    filteredPartnerList.add(partnerList.get(n));
                    filteredButtonArr.add(buttonArr.get(n));
                    buttonsListPanel.add(filteredButtonArr.get(filteredButtonArr.size() - 1));
                }
            }
        } else {
            // Search for matches in name and only display those
            name = name.toLowerCase();
            for (int i = 0; i < partnerList.size(); i++) {
                if (dropDown.getSelectedItem().equals("")
                        || dropDown.getSelectedItem().equals(partnerList.get(i).getOrgType())) {
                    if ((partnerList.get(i).getPartnerName().toLowerCase()).indexOf(name) >= 0) {
                        filteredPartnerList.add(partnerList.get(i));
                        filteredButtonArr.add(buttonArr.get(i));
                        buttonsListPanel.add(filteredButtonArr.get(filteredButtonArr.size() - 1));
                    }
                }
            }
        }
        updatePartnerButtons();
    }

    private JTextPane createTextPane() {

        JTextPane textPane = new JTextPane();
        doc = textPane.getStyledDocument();
        addStylesToDocument();
        textPane.setBackground(new Color(225, 225, 225));
        textPane.setEditable(false);

        return textPane;
    }

    private JComboBox<String> createDropDown() {
        orgTypesMap.put("", 1);
        for (int i = 0; i < partnerList.size(); i++) {
            orgTypesMap.putIfAbsent(partnerList.get(i).getOrgType(), 1);
        }

        String[] temp = orgTypesMap.keySet().toArray(new String[orgTypesMap.size()]);
        Arrays.sort(temp);
        JComboBox<String> comboBox = new JComboBox<>(temp);

        return comboBox;
    }

    private void fillTextPane() {
        mainTextPane.setText("");
        Partner currentPartner = filteredPartnerList.get(currentIndex);

        try {
            doc.insertString(doc.getLength(),
                    currentPartner.getEmail() + "\t\t\t\t\t\t\t\t\t" + currentPartner.getPhoneNumber() + "\n\n\n",
                    doc.getStyle("small"));

            doc.insertString(doc.getLength(), currentPartner.getPartnerName() + "\n", doc.getStyle("title"));

            doc.insertString(doc.getLength(), currentPartner.getOrgType() + "\n\n\n", doc.getStyle("subtitle"));

            doc.insertString(doc.getLength(), currentPartner.getResourcesAvailable() + "\n\n\n\n\n\n\n",
                    doc.getStyle("body"));

        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
    }

    private void addStylesToDocument() {
        // Initialize some styles.

        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(def, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), def, false);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("title", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setFontSize(s, 24);

        s = doc.addStyle("subtitle", regular);
        StyleConstants.setFontSize(regular, 17);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("body", regular);
        StyleConstants.setFontSize(regular, 14);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 12);
    }

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("CTE Partners");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(300, 200);
        // Add content to the window.
        frame.add(new CTE_Business_Partner_Search());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();
                try {
                    // 0-Swing, 1-Mac, 2-?, 3-Windows, 4-Old Windows
                    UIManager.setLookAndFeel(looks[0].getClassName());
                } catch (Exception e) {
                }
                createAndShowGUI();
            }
        });
    }
}