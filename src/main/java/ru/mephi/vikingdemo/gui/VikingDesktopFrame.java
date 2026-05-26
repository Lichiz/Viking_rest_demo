package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.service.AnalyticsService;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final AnalyticsService analyticsService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, AnalyticsService analyticsService) {
        this.vikingService = vikingService;
        this.analyticsService = analyticsService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createRandomVikingButton = new JButton("Create random viking");
        createRandomVikingButton.addActionListener(event -> onCreateViking());

        JButton createRandomVikingsButton = new JButton("Create 30 random vikings");
        createRandomVikingsButton.addActionListener(event -> onCreateVikings());

        JButton showStatisticsButton = new JButton("Show statistics");
        showStatisticsButton.addActionListener(event -> onCallStatistics());



        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createRandomVikingButton);
        bottomPanel.add(createRandomVikingsButton);
        bottomPanel.add(showStatisticsButton);
        add(bottomPanel, BorderLayout.SOUTH);

        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onCreateVikings(){
        List<Viking> vikings = vikingService.createRandomVikings(30);
        tableModel.addVikings(vikings);
    }

    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    public void onDeleteViking(Integer vikingId){
        tableModel.deleteViking(vikingId);
    }

    public void onUpdateViking(Viking viking, boolean databaseIsChanged){
        if (databaseIsChanged){
            tableModel.updateViking(viking);
        }else{
            System.out.println("No update.");
        }
    }

    public void onCallStatistics(){
        int vikingsOver40 = analyticsService.countVikingsOverAge(40);
        int vikingsBelow40 = analyticsService.countVikingsBelowAge(40);
        int vikingsEquals40 = analyticsService.countVikingsEqualsAge(40);
        int vikingsNotEquals40 = analyticsService.countVikingsNotEqualsAge(40);
        int vikingsColoredAndBearded = analyticsService.countVikingsBeardSyleAndHairColor(BeardStyle.BRAIDED, HairColor.Brown);
        int vikingsWithAxes = analyticsService.countVikingsWithAxes();

        Viking vikingOver180 = analyticsService.getAnyVikingOver180();
        List<Viking> vikingsWithLegendaryEquipment = analyticsService.getAllVikingsWithLegendaryEquipment();
        List<Viking> vikingsWithRedBeardSortedByVikings = analyticsService.getRedBeardedVikingsSortedByAge();

        int maxId = analyticsService.getMaxId();
        List<Integer> evenIds = analyticsService.getEvenIds();


        StringBuilder report = new StringBuilder();

        report.append("--- VIKING STATISTICS ---\n");

        report.append(String.format("Age Distribution:\n"));
        report.append(String.format(" - Over 40: %d\n", vikingsOver40));
        report.append(String.format(" - Below 40: %d\n", vikingsBelow40));
        report.append(String.format(" - Exactly 40: %d\n", vikingsEquals40));
        report.append(String.format(" - Not 40: %d\n\n", vikingsNotEquals40));

        report.append("Viking Statistics:\n");
        report.append(String.format(" - Braided Beards & Brown Hair: %d\n", vikingsColoredAndBearded));
        report.append(String.format(" - Vikings wielding Axes: %d\n\n", vikingsWithAxes));

        report.append("Legendary Heroes:\n");
        if (vikingOver180 != null) {
            report.append(String.format(" - Any Viking over 180cm: %s (%d cm)\n", vikingOver180.name(), vikingOver180.heightCm()));
        }




        StringBuilder legendarySection = new StringBuilder();

        if (vikingsWithLegendaryEquipment.isEmpty()) {
            legendarySection.append("No legendary vikings found in the records.\n");
        } else {
            for (Viking v : vikingsWithLegendaryEquipment) {
                String equipmentList = v.equipment().stream()
                        .map(EquipmentItem::name)
                        .collect(Collectors.joining(", "));

                legendarySection.append(String.format("   * %s (Gear: %s)\n",
                        v.name(),
                        equipmentList.isEmpty() ? "No gear" : equipmentList));
            }
        }

        String legendaryVikingsReport = legendarySection.toString();

        report.append(" - Legendary Gear Vikings: " + legendaryVikingsReport + "\n");

        report.append(" - Red Bearded Vikings (Youngest to Oldest):\n");
        for (Viking v : vikingsWithRedBeardSortedByVikings) {
            report.append(String.format("   * %s (Age: %d)\n", v.name(), v.age()));
        }

        report.append(String.format(" - Max Record ID: %d\n", maxId));
        report.append(String.format(" - Even Indexed Records: %d\n", evenIds.size()));

        String finalReport = report.toString();

        System.out.println(finalReport);
        JOptionPane.showMessageDialog(rootPane, finalReport);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()){
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }
}