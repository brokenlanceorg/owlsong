/*
 * PointOfSaleView.java
 */

package com.ultrastudios.ui;

import com.toedter.calendar.*;
import com.ultrastudios.speech.commands.*;
import com.ultrastudios.ui.models.*;
import com.ultrastudios.ui.tasks.*;
import com.ultrastudios.dao.*;
import common.FileReader;
import java.awt.GridLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.TableRowSorter;
import org.joda.time.*;

/**
 * The application's main frame.
 */
public class PointOfSaleView extends FrameView {

    public PointOfSaleView(SingleFrameApplication app) {
        super(app);

      try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         System.err.println( "Error setting Nimbus look-and-feel: " + ex );
      } catch (InstantiationException ex) {
         System.err.println( "Error setting Nimbus look-and-feel: " + ex );
      } catch (IllegalAccessException ex) {
         System.err.println( "Error setting Nimbus look-and-feel: " + ex );
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         System.err.println( "Error setting Nimbus look-and-feel: " + ex );
      }

       _identifier = (String) System.getProperties().get( "ultra.id.long" );
       _departmentTableModel = new DepartmentTableModel();
       _departmentSorter = new TableRowSorter<DepartmentTableModel>(_departmentTableModel);
       _inventoryTableModel = new InventoryTableModel();
       _inventorySorter = new TableRowSorter< InventoryTableModel >( _inventoryTableModel );
       _transactionTableModel = new TransactionTableModel( _inventoryTableModel );
       _transactionSorter = new TableRowSorter< TransactionTableModel >( _transactionTableModel );
       _reportTableModel = new ReportTableModel();

       _commandDispatcher = new CommandDispatcher();

       initComponents();
       _reportTableModel.setTransactionTable( _transactionTable );

       // speech recognition setups:
       _commandDispatcher.register( new InventoryFilterSpeechCommand( _iventoryFilterButton, _inventoryFilterText) );
       _commandDispatcher.register( new InventoryAddRowSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryTradeSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryReturnSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryVendorSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryMarkUpSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryCostSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventorySizeSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryColorSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new InventoryDescriptionSpeechCommand( _inventoryTable ) );
       _commandDispatcher.register( new TransactionCashSpeechCommand( _transactionTable ) );
       _commandDispatcher.register( new TransactionCreditSpeechCommand( _transactionTable ) );
       _commandDispatcher.register( new TransactionVoidSpeechCommand( _transactionTable ) );
       _commandDispatcher.register( new TransactionMenuSpeechCommand( _transactionMenuItem ) );

       _departmentFromDateChooser = new JDateChooser();
       _departmentToDateChooser = new JDateChooser();
       _departmentFromDateChooser.getJCalendar().setTodayButtonVisible(true);
       _departmentToDateChooser.getJCalendar().setTodayButtonVisible(true);
       _departmentFromDateChooser.getJCalendar().setNullDateButtonVisible(true);
       _departmentToDateChooser.getJCalendar().setNullDateButtonVisible(true);
       _departmentDateChooserPanel.setLayout(new GridLayout(2,1));
       _departmentDateChooserPanel.add(_departmentFromDateChooser);
       _departmentDateChooserPanel.add(_departmentToDateChooser);
       JDateChooserCellEditor departmentCellEditor = new JDateChooserCellEditor();
       ((JDateChooser)departmentCellEditor.getTableCellEditorComponent( _departmentTable, null, false, 0, 3 )).getJCalendar().setTodayButtonVisible( true );
       ((JDateChooser)departmentCellEditor.getTableCellEditorComponent( _departmentTable, null, false, 0, 3 )).getJCalendar().setNullDateButtonVisible( true );
       _departmentTable.setDefaultEditor(Date.class, departmentCellEditor);

       // inventory table setup
       _inventoryFromDateChooser = new JDateChooser();
       _inventoryToDateChooser = new JDateChooser();
       _inventoryFromDateChooser.getJCalendar().setTodayButtonVisible(true);
       _inventoryToDateChooser.getJCalendar().setTodayButtonVisible(true);
       _inventoryFromDateChooser.getJCalendar().setNullDateButtonVisible(true);
       _inventoryToDateChooser.getJCalendar().setNullDateButtonVisible(true);
       _inventoryDateChooserPanel.setLayout(new GridLayout(2,1));
       _inventoryDateChooserPanel.add(_inventoryFromDateChooser);
       _inventoryDateChooserPanel.add(_inventoryToDateChooser);
       JDateChooserCellEditor inventoryCellEditor = new JDateChooserCellEditor();
       ((JDateChooser)inventoryCellEditor.getTableCellEditorComponent( _inventoryTable, null, false, 0, 10 )).getJCalendar().setTodayButtonVisible( true );
       ((JDateChooser)inventoryCellEditor.getTableCellEditorComponent( _inventoryTable, null, false, 0, 10 )).getJCalendar().setNullDateButtonVisible( true );
       _inventoryTable.setDefaultEditor(Date.class, inventoryCellEditor);

       // Transaction table setup:
       _transactionFromDateChooser = new JDateChooser();
       _transactionToDateChooser = new JDateChooser();
       _transactionFromDateChooser.getJCalendar().setTodayButtonVisible(true);
       _transactionToDateChooser.getJCalendar().setTodayButtonVisible(true);
       _transactionFromDateChooser.getJCalendar().setNullDateButtonVisible(true);
       _transactionToDateChooser.getJCalendar().setNullDateButtonVisible(true);
       _transactionDateChooserPanel.setLayout(new GridLayout(2,1));
       _transactionDateChooserPanel.add(_transactionFromDateChooser);
       _transactionDateChooserPanel.add(_transactionToDateChooser);
       JDateChooserCellEditor transactionCellEditor = new JDateChooserCellEditor();
       ((JDateChooser)inventoryCellEditor.getTableCellEditorComponent( _transactionTable, null, false, 0, 10 )).getJCalendar().setTodayButtonVisible( true );
       ((JDateChooser)inventoryCellEditor.getTableCellEditorComponent( _transactionTable, null, false, 0, 10 )).getJCalendar().setNullDateButtonVisible( true );
       _transactionTable.setDefaultEditor(Date.class, transactionCellEditor );

      BufferedImage iconimage = null;
      try
      {
         iconimage = ImageIO.read( getClass().getResource( "ultra-icon.jpg" ) );
      }
      catch( IOException e )
      {
         System.err.println( "Caught IOException: " + e );
      }
      getFrame().setIconImage( iconimage );


        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        busyIconTimer.start();
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
//                     progressBar.setVisible(true);
//                     progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
//                     progressBar.setVisible(false);
//                     progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } 
//                 else if ("progress".equals(propertyName)) {
//                     int value = (Integer)(evt.getNewValue());
//                     progressBar.setVisible(true);
//                     progressBar.setIndeterminate(false);
//                     progressBar.setValue(value);
//                 }
            }
        });
      SpeechTask task = new SpeechTask( _commandDispatcher );
      task.setCheckBoxMenuItem( _audioOnCheckBox );
      task.execute();

      printSystemProperties();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PointOfSale.getApplication().getMainFrame();
            aboutBox = new PointOfSaleAboutBox(mainFrame, _identifier);
            aboutBox.setLocationRelativeTo(mainFrame);
            ((PointOfSaleAboutBox)aboutBox).setIdentifier(_identifier);
        }
        PointOfSale.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      mainPanel = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      _departmentTab = new javax.swing.JTabbedPane();
      jPanel1 = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      _departmentTable = new javax.swing.JTable();
      jPanel4 = new javax.swing.JPanel();
      _addDepartmentRowButton = new javax.swing.JButton();
      _deleteDepartmentRowButton = new javax.swing.JButton();
      _departmentFilterButton = new javax.swing.JButton();
      jLabel3 = new javax.swing.JLabel();
      _departmentFilterText = new javax.swing.JTextField();
      _departmentDateChooserPanel = new javax.swing.JPanel();
      jLabel4 = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();
      jButton1 = new javax.swing.JButton();
      jPanel2 = new javax.swing.JPanel();
      jScrollPane2 = new javax.swing.JScrollPane();
      _inventoryTable = new javax.swing.JTable();
      jPanel5 = new javax.swing.JPanel();
      _inventoryAddRowButton = new javax.swing.JButton();
      _inventoryDeleteRowButton = new javax.swing.JButton();
      jLabel6 = new javax.swing.JLabel();
      _inventoryFilterText = new javax.swing.JTextField();
      _iventoryFilterButton = new javax.swing.JButton();
      jLabel7 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      _inventoryDateChooserPanel = new javax.swing.JPanel();
      _inventoryFilterDateButton = new javax.swing.JButton();
      jPanel3 = new javax.swing.JPanel();
      jPanel6 = new javax.swing.JPanel();
      _transactionAddRowButton = new javax.swing.JButton();
      _transactionDeleteRowButton = new javax.swing.JButton();
      jLabel9 = new javax.swing.JLabel();
      _transactionFilterText = new javax.swing.JTextField();
      _transactionFilterButton = new javax.swing.JButton();
      jLabel10 = new javax.swing.JLabel();
      jLabel11 = new javax.swing.JLabel();
      _transactionDateChooserPanel = new javax.swing.JPanel();
      _transactionFilterDateButton = new javax.swing.JButton();
      jScrollPane3 = new javax.swing.JScrollPane();
      _transactionTable = new javax.swing.JTable();
      jPanel7 = new javax.swing.JPanel();
      jScrollPane4 = new javax.swing.JScrollPane();
      _reportTable = new javax.swing.JTable();
      _reportCalcButton = new javax.swing.JButton();
      _saveReportButton = new javax.swing.JButton();
      menuBar = new javax.swing.JMenuBar();
      javax.swing.JMenu fileMenu = new javax.swing.JMenu();
      _importDatabaseMenuItem = new javax.swing.JMenuItem();
      _exportDatabaseMenuItem = new javax.swing.JMenuItem();
      _saveDatabaseMenuItem = new javax.swing.JMenuItem();
      jSeparator1 = new javax.swing.JPopupMenu.Separator();
      javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
      _audioMenu = new javax.swing.JMenu();
      _audioOnCheckBox = new javax.swing.JCheckBoxMenuItem();
      _actionMenu = new javax.swing.JMenu();
      _transactionMenuItem = new javax.swing.JMenuItem();
      _accountingReportMenuItem = new javax.swing.JMenuItem();
      _saveReportMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenu helpMenu = new javax.swing.JMenu();
      javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
      statusPanel = new javax.swing.JPanel();
      javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
      statusMessageLabel = new javax.swing.JLabel();
      statusAnimationLabel = new javax.swing.JLabel();
      progressBar = new javax.swing.JProgressBar();
      jLabel1 = new javax.swing.JLabel();

      mainPanel.setName("mainPanel"); // NOI18N

      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.ultrastudios.ui.PointOfSale.class).getContext().getResourceMap(PointOfSaleView.class);
      jLabel2.setBackground(resourceMap.getColor("jLabel2.background")); // NOI18N
      jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
      jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
      jLabel2.setName("jLabel2"); // NOI18N
      jLabel2.setOpaque(true);

      _departmentTab.setName("_departmentTab"); // NOI18N

      jPanel1.setName("jPanel1"); // NOI18N

      jScrollPane1.setName("jScrollPane1"); // NOI18N

      _departmentTable.setModel(_departmentTableModel);
      _departmentTable.setName("_departmentTable"); // NOI18N
      _departmentTable.setRowSorter(_departmentSorter);
      jScrollPane1.setViewportView(_departmentTable);

      jPanel4.setName("jPanel4"); // NOI18N

      _addDepartmentRowButton.setText(resourceMap.getString("_addDepartmentRowButton.text")); // NOI18N
      _addDepartmentRowButton.setName("_addDepartmentRowButton"); // NOI18N
      _addDepartmentRowButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _addDepartmentRowButtonActionPerformed(evt);
         }
      });

      _deleteDepartmentRowButton.setText(resourceMap.getString("_deleteDepartmentRowButton.text")); // NOI18N
      _deleteDepartmentRowButton.setName("_deleteDepartmentRowButton"); // NOI18N
      _deleteDepartmentRowButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _deleteDepartmentRowButtonActionPerformed(evt);
         }
      });

      _departmentFilterButton.setText(resourceMap.getString("_departmentFilterButton.text")); // NOI18N
      _departmentFilterButton.setName("_departmentFilterButton"); // NOI18N
      _departmentFilterButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _departmentFilterButtonActionPerformed(evt);
         }
      });
      _departmentFilterButton.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
            _departmentFilterButtonKeyTyped(evt);
         }
      });

      jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
      jLabel3.setName("jLabel3"); // NOI18N

      _departmentFilterText.setText(resourceMap.getString("_departmentFilterText.text")); // NOI18N
      _departmentFilterText.setName("_departmentFilterText"); // NOI18N
      _departmentFilterText.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
            _departmentFilterTextKeyTyped(evt);
         }
      });

      _departmentDateChooserPanel.setName("_departmentDateChooserPanel"); // NOI18N
      _departmentDateChooserPanel.setPreferredSize(new java.awt.Dimension(135, 56));

      javax.swing.GroupLayout _departmentDateChooserPanelLayout = new javax.swing.GroupLayout(_departmentDateChooserPanel);
      _departmentDateChooserPanel.setLayout(_departmentDateChooserPanelLayout);
      _departmentDateChooserPanelLayout.setHorizontalGroup(
         _departmentDateChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 135, Short.MAX_VALUE)
      );
      _departmentDateChooserPanelLayout.setVerticalGroup(
         _departmentDateChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 56, Short.MAX_VALUE)
      );

      jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
      jLabel4.setName("jLabel4"); // NOI18N

      jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
      jLabel5.setName("jLabel5"); // NOI18N

      jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
      jButton1.setName("jButton1"); // NOI18N
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addGroup(jPanel4Layout.createSequentialGroup()
                  .addComponent(_addDepartmentRowButton)
                  .addGap(18, 18, 18)
                  .addComponent(jLabel3)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(_departmentFilterText, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(jPanel4Layout.createSequentialGroup()
                  .addComponent(_deleteDepartmentRowButton)
                  .addGap(18, 18, 18)
                  .addComponent(_departmentFilterButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGap(18, 18, 18)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jLabel4)
               .addComponent(jLabel5))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(_departmentDateChooserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton1)
            .addContainerGap(214, Short.MAX_VALUE))
      );

      jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {_addDepartmentRowButton, _deleteDepartmentRowButton});

      jPanel4Layout.setVerticalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(_departmentDateChooserPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                  .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                     .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_departmentFilterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                     .addComponent(_addDepartmentRowButton))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(_departmentFilterButton)
                     .addComponent(_deleteDepartmentRowButton)
                     .addComponent(jLabel5))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {_departmentFilterButton, _departmentFilterText});

      jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jLabel4, jLabel5});

      jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {_addDepartmentRowButton, _deleteDepartmentRowButton});

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      );

      _departmentTab.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

      jPanel2.setName("jPanel2"); // NOI18N

      jScrollPane2.setName("jScrollPane2"); // NOI18N

      _inventoryTable.setModel(_inventoryTableModel);
      _inventoryTable.setName("_inventoryTable"); // NOI18N
      _inventoryTable.setRowSorter(_inventorySorter);
      jScrollPane2.setViewportView(_inventoryTable);

      jPanel5.setName("jPanel5"); // NOI18N
      jPanel5.setPreferredSize(new java.awt.Dimension(768, 78));

      _inventoryAddRowButton.setText(resourceMap.getString("_inventoryAddRowButton.text")); // NOI18N
      _inventoryAddRowButton.setName("_inventoryAddRowButton"); // NOI18N
      _inventoryAddRowButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _inventoryAddRowButtonActionPerformed(evt);
         }
      });

      _inventoryDeleteRowButton.setText(resourceMap.getString("_inventoryDeleteRowButton.text")); // NOI18N
      _inventoryDeleteRowButton.setName("_inventoryDeleteRowButton"); // NOI18N
      _inventoryDeleteRowButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _inventoryDeleteRowButtonActionPerformed(evt);
         }
      });

      jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
      jLabel6.setName("jLabel6"); // NOI18N

      _inventoryFilterText.setText(resourceMap.getString("_inventoryFilterText.text")); // NOI18N
      _inventoryFilterText.setName("_inventoryFilterText"); // NOI18N
      _inventoryFilterText.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
            _inventoryFilterTextKeyTyped(evt);
         }
      });

      _iventoryFilterButton.setText(resourceMap.getString("_iventoryFilterButton.text")); // NOI18N
      _iventoryFilterButton.setName("_iventoryFilterButton"); // NOI18N
      _iventoryFilterButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _iventoryFilterButtonActionPerformed(evt);
         }
      });

      jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
      jLabel7.setName("jLabel7"); // NOI18N

      jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
      jLabel8.setName("jLabel8"); // NOI18N

      _inventoryDateChooserPanel.setName("_inventoryDateChooserPanel"); // NOI18N
      _inventoryDateChooserPanel.setPreferredSize(new java.awt.Dimension(135, 56));

      javax.swing.GroupLayout _inventoryDateChooserPanelLayout = new javax.swing.GroupLayout(_inventoryDateChooserPanel);
      _inventoryDateChooserPanel.setLayout(_inventoryDateChooserPanelLayout);
      _inventoryDateChooserPanelLayout.setHorizontalGroup(
         _inventoryDateChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 135, Short.MAX_VALUE)
      );
      _inventoryDateChooserPanelLayout.setVerticalGroup(
         _inventoryDateChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 56, Short.MAX_VALUE)
      );

      _inventoryFilterDateButton.setText(resourceMap.getString("_inventoryFilterDateButton.text")); // NOI18N
      _inventoryFilterDateButton.setName("_inventoryFilterDateButton"); // NOI18N
      _inventoryFilterDateButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _inventoryFilterDateButtonActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(_inventoryAddRowButton)
               .addComponent(_inventoryDeleteRowButton))
            .addGap(18, 18, 18)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel5Layout.createSequentialGroup()
                  .addComponent(jLabel6)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(_inventoryFilterText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addComponent(_iventoryFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jLabel7)
               .addComponent(jLabel8))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(_inventoryDateChooserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(_inventoryFilterDateButton)
            .addContainerGap(215, Short.MAX_VALUE))
      );

      jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {_inventoryAddRowButton, _inventoryDeleteRowButton});

      jPanel5Layout.setVerticalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(_inventoryDateChooserPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addGroup(jPanel5Layout.createSequentialGroup()
                     .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_inventoryFilterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7))
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_iventoryFilterButton)
                        .addComponent(jLabel8)))
                  .addComponent(_inventoryFilterDateButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addGroup(jPanel5Layout.createSequentialGroup()
                  .addComponent(_inventoryAddRowButton)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(_inventoryDeleteRowButton)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {_inventoryAddRowButton, _inventoryDeleteRowButton, _inventoryFilterText, _iventoryFilterButton});

      jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jLabel7, jLabel8});

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      );

      _departmentTab.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

      jPanel3.setName("jPanel3"); // NOI18N

      jPanel6.setName("jPanel6"); // NOI18N
      jPanel6.setPreferredSize(new java.awt.Dimension(768, 78));

      _transactionAddRowButton.setText(resourceMap.getString("_transactionAddRowButton.text")); // NOI18N
      _transactionAddRowButton.setName("_transactionAddRowButton"); // NOI18N
      _transactionAddRowButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _transactionAddRowButtonActionPerformed(evt);
         }
      });

      _transactionDeleteRowButton.setText(resourceMap.getString("_transactionDeleteRowButton.text")); // NOI18N
      _transactionDeleteRowButton.setName("_transactionDeleteRowButton"); // NOI18N
      _transactionDeleteRowButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _transactionDeleteRowButtonActionPerformed(evt);
         }
      });

      jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
      jLabel9.setName("jLabel9"); // NOI18N

      _transactionFilterText.setName("_transactionFilterText"); // NOI18N
      _transactionFilterText.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
            _transactionFilterTextKeyTyped(evt);
         }
      });

      _transactionFilterButton.setText(resourceMap.getString("_transactionFilterButton.text")); // NOI18N
      _transactionFilterButton.setName("_transactionFilterButton"); // NOI18N
      _transactionFilterButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _transactionFilterButtonActionPerformed(evt);
         }
      });

      jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
      jLabel10.setName("jLabel10"); // NOI18N

      jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
      jLabel11.setName("jLabel11"); // NOI18N

      _transactionDateChooserPanel.setName("_transactionDateChooserPanel"); // NOI18N
      _transactionDateChooserPanel.setPreferredSize(new java.awt.Dimension(135, 56));

      javax.swing.GroupLayout _transactionDateChooserPanelLayout = new javax.swing.GroupLayout(_transactionDateChooserPanel);
      _transactionDateChooserPanel.setLayout(_transactionDateChooserPanelLayout);
      _transactionDateChooserPanelLayout.setHorizontalGroup(
         _transactionDateChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 135, Short.MAX_VALUE)
      );
      _transactionDateChooserPanelLayout.setVerticalGroup(
         _transactionDateChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 56, Short.MAX_VALUE)
      );

      _transactionFilterDateButton.setText(resourceMap.getString("_transactionFilterDateButton.text")); // NOI18N
      _transactionFilterDateButton.setName("_transactionFilterDateButton"); // NOI18N
      _transactionFilterDateButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _transactionFilterDateButtonActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
      jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(_transactionAddRowButton)
               .addComponent(_transactionDeleteRowButton))
            .addGap(18, 18, 18)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel6Layout.createSequentialGroup()
                  .addComponent(jLabel9)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(_transactionFilterText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addComponent(_transactionFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jLabel10)
               .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(_transactionDateChooserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(_transactionFilterDateButton)
            .addContainerGap(195, Short.MAX_VALUE))
      );

      jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {_transactionAddRowButton, _transactionDeleteRowButton});

      jPanel6Layout.setVerticalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(_transactionDateChooserPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addGroup(jPanel6Layout.createSequentialGroup()
                     .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_transactionFilterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10))
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_transactionFilterButton)
                        .addComponent(jLabel11)))
                  .addComponent(_transactionFilterDateButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addGroup(jPanel6Layout.createSequentialGroup()
                  .addComponent(_transactionAddRowButton)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(_transactionDeleteRowButton)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jScrollPane3.setName("jScrollPane3"); // NOI18N

      _transactionTable.setModel(_transactionTableModel);
      _transactionTable.setName("_transactionTable"); // NOI18N
      _transactionTable.setRowSorter(_transactionSorter);
      jScrollPane3.setViewportView(_transactionTable);

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
            .addGap(20, 20, 20))
         .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
      );
      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
            .addGap(11, 11, 11)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      );

      _departmentTab.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

      jPanel7.setName("jPanel7"); // NOI18N

      jScrollPane4.setName("jScrollPane4"); // NOI18N

      _reportTable.setModel(_reportTableModel);
      _reportTable.setName("_reportTable"); // NOI18N
      jScrollPane4.setViewportView(_reportTable);

      _reportCalcButton.setText(resourceMap.getString("_reportCalcButton.text")); // NOI18N
      _reportCalcButton.setName("_reportCalcButton"); // NOI18N
      _reportCalcButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _reportCalcButtonActionPerformed(evt);
         }
      });

      _saveReportButton.setText(resourceMap.getString("_saveReportButton.text")); // NOI18N
      _saveReportButton.setName("_saveReportButton"); // NOI18N
      _saveReportButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _saveReportButtonActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
      jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
         .addComponent(_reportCalcButton, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
         .addComponent(_saveReportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
      );
      jPanel7Layout.setVerticalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
            .addComponent(_reportCalcButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(_saveReportButton)
            .addGap(12, 12, 12))
      );

      jPanel7Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {_reportCalcButton, _saveReportButton});

      _departmentTab.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

      javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
      mainPanel.setLayout(mainPanelLayout);
      mainPanelLayout.setHorizontalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(mainPanelLayout.createSequentialGroup()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(_departmentTab, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE))
      );
      mainPanelLayout.setVerticalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
         .addComponent(_departmentTab, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
      );

      menuBar.setName("menuBar"); // NOI18N

      fileMenu.setMnemonic(KeyEvent.VK_F);
      fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
      fileMenu.setName("fileMenu"); // NOI18N

      _importDatabaseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
      _importDatabaseMenuItem.setMnemonic(KeyEvent.VK_I);
      _importDatabaseMenuItem.setText(resourceMap.getString("_importDatabaseMenuItem.text")); // NOI18N
      _importDatabaseMenuItem.setName("_importDatabaseMenuItem"); // NOI18N
      _importDatabaseMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _importDatabaseMenuItemActionPerformed(evt);
         }
      });
      fileMenu.add(_importDatabaseMenuItem);

      _exportDatabaseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
      _exportDatabaseMenuItem.setMnemonic(KeyEvent.VK_E);
      _exportDatabaseMenuItem.setText(resourceMap.getString("_exportDatabaseMenuItem.text")); // NOI18N
      _exportDatabaseMenuItem.setName("_exportDatabaseMenuItem"); // NOI18N
      _exportDatabaseMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _exportDatabaseMenuItemActionPerformed(evt);
         }
      });
      fileMenu.add(_exportDatabaseMenuItem);

      _saveDatabaseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
      _saveDatabaseMenuItem.setMnemonic(KeyEvent.VK_S);
      _saveDatabaseMenuItem.setText(resourceMap.getString("_saveDatabaseMenuItem.text")); // NOI18N
      _saveDatabaseMenuItem.setName("_saveDatabaseMenuItem"); // NOI18N
      _saveDatabaseMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _saveDatabaseMenuItemActionPerformed(evt);
         }
      });
      fileMenu.add(_saveDatabaseMenuItem);

      jSeparator1.setName("jSeparator1"); // NOI18N
      fileMenu.add(jSeparator1);

      javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.ultrastudios.ui.PointOfSale.class).getContext().getActionMap(PointOfSaleView.class, this);
      exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
      exitMenuItem.setName("exitMenuItem"); // NOI18N
      fileMenu.add(exitMenuItem);

      menuBar.add(fileMenu);

      _audioMenu.setMnemonic(KeyEvent.VK_U);
      _audioMenu.setText(resourceMap.getString("_audioMenu.text")); // NOI18N
      _audioMenu.setName("_audioMenu"); // NOI18N

      _audioOnCheckBox.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
      _audioOnCheckBox.setMnemonic(KeyEvent.VK_O);
      _audioOnCheckBox.setSelected(true);
      _audioOnCheckBox.setText(resourceMap.getString("_audioOnCheckBox.text")); // NOI18N
      _audioOnCheckBox.setToolTipText(resourceMap.getString("_audioOnCheckBox.toolTipText")); // NOI18N
      _audioOnCheckBox.setName("_audioOnCheckBox"); // NOI18N
      _audioOnCheckBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _audioOnCheckBoxActionPerformed(evt);
         }
      });
      _audioMenu.add(_audioOnCheckBox);

      menuBar.add(_audioMenu);

      _actionMenu.setMnemonic(KeyEvent.VK_A);
      _actionMenu.setText(resourceMap.getString("_actionMenu.text")); // NOI18N
      _actionMenu.setName("_actionMenu"); // NOI18N

      _transactionMenuItem.setMnemonic(KeyEvent.VK_T);
      _transactionMenuItem.setText(resourceMap.getString("_transactionMenuItem.text")); // NOI18N
      _transactionMenuItem.setToolTipText(resourceMap.getString("_transactionMenuItem.toolTipText")); // NOI18N
      _transactionMenuItem.setName("_transactionMenuItem"); // NOI18N
      _transactionMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _transactionMenuItemActionPerformed(evt);
         }
      });
      _actionMenu.add(_transactionMenuItem);

      _accountingReportMenuItem.setMnemonic(KeyEvent.VK_R);
      _accountingReportMenuItem.setText(resourceMap.getString("_accountingReportMenuItem.text")); // NOI18N
      _accountingReportMenuItem.setToolTipText(resourceMap.getString("_accountingReportMenuItem.toolTipText")); // NOI18N
      _accountingReportMenuItem.setName("_accountingReportMenuItem"); // NOI18N
      _accountingReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _accountingReportMenuItemActionPerformed(evt);
         }
      });
      _actionMenu.add(_accountingReportMenuItem);

      _saveReportMenuItem.setMnemonic(KeyEvent.VK_S);
      _saveReportMenuItem.setText(resourceMap.getString("_saveReportMenuItem.text")); // NOI18N
      _saveReportMenuItem.setName("_saveReportMenuItem"); // NOI18N
      _saveReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            _saveReportMenuItemActionPerformed(evt);
         }
      });
      _actionMenu.add(_saveReportMenuItem);

      menuBar.add(_actionMenu);

      helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
      helpMenu.setName("helpMenu"); // NOI18N

      aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
      aboutMenuItem.setName("aboutMenuItem"); // NOI18N
      helpMenu.add(aboutMenuItem);

      menuBar.add(helpMenu);

      statusPanel.setName("statusPanel"); // NOI18N
      statusPanel.setPreferredSize(new java.awt.Dimension(989, 35));

      statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

      statusMessageLabel.setName("statusMessageLabel"); // NOI18N

      statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
      statusAnimationLabel.setToolTipText(resourceMap.getString("statusAnimationLabel.toolTipText")); // NOI18N
      statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

      progressBar.setName("progressBar"); // NOI18N

      jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
      jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
      jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
      jLabel1.setName("jLabel1"); // NOI18N
      jLabel1.setOpaque(true);

      javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
      statusPanel.setLayout(statusPanelLayout);
      statusPanelLayout.setHorizontalGroup(
         statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1017, Short.MAX_VALUE)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(statusPanelLayout.createSequentialGroup()
                  .addContainerGap(853, Short.MAX_VALUE)
                  .addComponent(statusMessageLabel))
               .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(statusAnimationLabel)
            .addContainerGap())
      );
      statusPanelLayout.setVerticalGroup(
         statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(statusPanelLayout.createSequentialGroup()
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
               .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
               .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(statusMessageLabel)
                  .addComponent(statusAnimationLabel)))
            .addGap(11, 11, 11))
      );

      setComponent(mainPanel);
      setMenuBar(menuBar);
      setStatusBar(statusPanel);
   }// </editor-fold>//GEN-END:initComponents

private void _audioOnCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__audioOnCheckBoxActionPerformed
   if(_audioOnCheckBox.getState())
   {
      busyIconTimer.start();
   }
   else
   {
      busyIconTimer.stop();
      statusAnimationLabel.setIcon(idleIcon);
   }
}//GEN-LAST:event__audioOnCheckBoxActionPerformed

private void _exportDatabaseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__exportDatabaseMenuItemActionPerformed
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Ultra Database file", "csv", "txt");
   if(_exportDatabaseChooser == null)
   {
      _exportDatabaseChooser = new JFileChooser();
      _exportDatabaseChooser.setDialogType( JFileChooser.SAVE_DIALOG );
      _exportDatabaseChooser.setApproveButtonText( "Save" );
   }
   _exportDatabaseChooser.setFileFilter(filter);

	int returnVal = _exportDatabaseChooser.showOpenDialog(getFrame());
   if (returnVal == JFileChooser.APPROVE_OPTION) 
	{
      _saveDatabaseMenuItemActionPerformed( null );
   } 
}//GEN-LAST:event__exportDatabaseMenuItemActionPerformed

private void _importDatabaseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__importDatabaseMenuItemActionPerformed
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Ultra Database file", "csv", "txt");
   JFileChooser importDatabaseChooser = new JFileChooser();
   importDatabaseChooser.setFileFilter(filter);

   /*
   String path = "http://www.jaybenham.com/test-database.csv";
   String outpath = "http://www.jaybenham.com/test-database-2.csv";
   try
   {
      URL url = new URL( path );
      BufferedReader theReader = new BufferedReader( new InputStreamReader ( url.openStream() ) );
      URLConnection conn = url.openConnection();
      conn.setDoOutput(true);
      BufferedWriter theWriter = new BufferedWriter( new OutputStreamWriter ( conn.getOutputStream() ) );
      String theLine = null;
 
      while( (theLine = theReader.readLine()) != null )
      {
         System.out.println( theLine );
         theWriter.write(theLine);
      }
      theReader.close();
      theWriter.close();

   }
//    catch( URISyntaxException e )
//    {
//       System.err.println( "Error: " + e );
//    }
   catch( FileNotFoundException e )
   {
      System.err.println( "File Error: " + e );
   }
   catch( IOException e )
   {
      System.err.println( "IO Error: " + e );
   }
    * */

	int returnVal = importDatabaseChooser.showOpenDialog(getFrame());
   if (returnVal == JFileChooser.APPROVE_OPTION) 
	{
      File file = importDatabaseChooser.getSelectedFile();
      DepartmentTableDAO deptDAO = new DepartmentTableDAO(_departmentTableModel);
      common.FileReader reader = new common.FileReader( file.toString(), "," );
      String[] words = reader.getArrayOfWords();
      deptDAO.loadData(words);
      InventoryTableDAO inventoryDAO = new InventoryTableDAO(_inventoryTableModel);
      inventoryDAO.loadData(words);
      TransactionTableDAO transactionDAO = new TransactionTableDAO(_transactionTableModel);
      transactionDAO.loadData(words);
      inventoryDAO.loadData(words);
   } 
}//GEN-LAST:event__importDatabaseMenuItemActionPerformed

private void _saveDatabaseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__saveDatabaseMenuItemActionPerformed
   if(_exportDatabaseChooser != null)
   {
      File file = _exportDatabaseChooser.getSelectedFile();
      if(file != null)
      {
         System.out.println( "will save database to: " + file);
         BufferedWriter bufferedWriter = null;
         try
         {
            bufferedWriter = new BufferedWriter( new FileWriter( file.toString() ) );
            DepartmentTableDAO departmentDAO = new DepartmentTableDAO(_departmentTableModel);
            departmentDAO.persistData( bufferedWriter );
            bufferedWriter.newLine(); 
            InventoryTableDAO inventoryDAO = new InventoryTableDAO(_inventoryTableModel);
            inventoryDAO.persistData( bufferedWriter );
            TransactionTableDAO transactionDAO = new TransactionTableDAO(_transactionTableModel);
            transactionDAO.persistData( bufferedWriter );
            bufferedWriter.flush();
            bufferedWriter.close();
         }
         catch( IOException e )
         {
            System.err.println( "IOException writing file: " + e );
         }
      }
   }
}//GEN-LAST:event__saveDatabaseMenuItemActionPerformed

private void _addDepartmentRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__addDepartmentRowButtonActionPerformed
   _departmentTableModel.addRow();
}//GEN-LAST:event__addDepartmentRowButtonActionPerformed

private void _deleteDepartmentRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__deleteDepartmentRowButtonActionPerformed
   int row = _departmentTable.getSelectedRow();
   if(row > -1)
   {
      int response = JOptionPane.showConfirmDialog( getFrame(), "Are you sure you want to delete this row?", "Confirm Row Deletion", JOptionPane.YES_NO_OPTION );
      if( response == 0 )
      {
         _departmentTableModel.deleteRow(_departmentTable.convertRowIndexToModel(row));
      }
   }
}//GEN-LAST:event__deleteDepartmentRowButtonActionPerformed

private void _departmentFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__departmentFilterButtonActionPerformed
   RowFilter<DepartmentTableModel, Object> rf = RowFilter.regexFilter(_departmentFilterText.getText(), 0, 1, 2, 3 );
   _departmentSorter.setRowFilter( rf );
}//GEN-LAST:event__departmentFilterButtonActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   RowFilter<DepartmentTableModel, Object> rf = new RowFilter<DepartmentTableModel, Object>()
   {
      public boolean include(Entry <? extends DepartmentTableModel, ? extends Object> entry)
      {
         DateMidnight from = (new DateTime(_departmentFromDateChooser.getDate())).toDateMidnight();
         DateMidnight to = (new DateTime(_departmentToDateChooser.getDate())).toDateMidnight();
         for(int i=0; i<entry.getValueCount(); i++)
         {
            Object o = entry.getValue(i);
            if(o instanceof Date)
            {
               DateMidnight c = (new DateTime((Date)o)).toDateMidnight();
               if(c.isBefore(from) || c.isAfter(to))
               {
                  return false;
               }
            }
         }
         return true;
      }

   };
   _departmentSorter.setRowFilter( rf );
}//GEN-LAST:event_jButton1ActionPerformed

private void _inventoryAddRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__inventoryAddRowButtonActionPerformed
   _inventoryTableModel.addRow();
}//GEN-LAST:event__inventoryAddRowButtonActionPerformed

private void _inventoryDeleteRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__inventoryDeleteRowButtonActionPerformed
   int row = _inventoryTable.getSelectedRow();
   if(row > -1)
   {
      int response = JOptionPane.showConfirmDialog( getFrame(), "Are you sure you want to delete this row?", "Confirm Row Deletion", JOptionPane.YES_NO_OPTION );
      if( response == 0 )
      {
         _inventoryTableModel.deleteRow(_inventoryTable.convertRowIndexToModel(row));
      }
   }
}//GEN-LAST:event__inventoryDeleteRowButtonActionPerformed

private void _iventoryFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__iventoryFilterButtonActionPerformed
   RowFilter<InventoryTableModel, Object> rf = RowFilter.regexFilter(_inventoryFilterText.getText(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 );
   _inventorySorter.setRowFilter( rf );
}//GEN-LAST:event__iventoryFilterButtonActionPerformed

private void _inventoryFilterDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__inventoryFilterDateButtonActionPerformed
   RowFilter<InventoryTableModel, Object> rf = new RowFilter<InventoryTableModel, Object>()
   {
      public boolean include(Entry <? extends InventoryTableModel, ? extends Object> entry)
      {
         DateMidnight from = (new DateTime(_inventoryFromDateChooser.getDate())).toDateMidnight();
         DateMidnight to = (new DateTime(_inventoryToDateChooser.getDate())).toDateMidnight();
         Object o = entry.getValue(10);
         if(o == null)
         {
            return false;
         }
         else
         {
            DateMidnight c = (new DateTime((Date)o)).toDateMidnight();
            if(c.isBefore(from) || c.isAfter(to))
            {
               return false;
            }
         }

         return true;
//          for(int i=0; i<entry.getValueCount(); i++)
//          {
//             Object o = entry.getValue(i);
//             if(o instanceof Date)
//             {
//                DateMidnight c = (new DateTime((Date)o)).toDateMidnight();
//                if(c.isBefore(from) || c.isAfter(to))
//                {
//                   return false;
//                }
//             }
//          }
      }

   };
   _inventorySorter.setRowFilter( rf );
}//GEN-LAST:event__inventoryFilterDateButtonActionPerformed

private void _inventoryFilterTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__inventoryFilterTextKeyTyped
   if( '\n' == evt.getKeyChar() )
   {
      _iventoryFilterButton.doClick();
   }
}//GEN-LAST:event__inventoryFilterTextKeyTyped

private void _departmentFilterButtonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__departmentFilterButtonKeyTyped
}//GEN-LAST:event__departmentFilterButtonKeyTyped

private void _departmentFilterTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__departmentFilterTextKeyTyped
   if( '\n' == evt.getKeyChar() )
   {
      _departmentFilterButton.doClick();
   }
}//GEN-LAST:event__departmentFilterTextKeyTyped

private void _transactionAddRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__transactionAddRowButtonActionPerformed
   _transactionTableModel.addRow();
}//GEN-LAST:event__transactionAddRowButtonActionPerformed

private void _transactionDeleteRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__transactionDeleteRowButtonActionPerformed
   Boolean enableDelete = Boolean.getBoolean( "ultra.admin" );
   if( enableDelete )
   {
      int row = _transactionTable.getSelectedRow();
      if(row > -1)
      {
         int response = JOptionPane.showConfirmDialog( getFrame(), "Are you sure you want to delete this row?", "Confirm Row Deletion", JOptionPane.YES_NO_OPTION );
         if( response == 0 )
         {
            _transactionTableModel.deleteRow(_transactionTable.convertRowIndexToModel(row));
         }
      }
   }
   else
   {
      JOptionPane.showMessageDialog( getFrame(), "Delete function on monetary transactions is currently disabled" );
   }
}//GEN-LAST:event__transactionDeleteRowButtonActionPerformed

private void _transactionFilterTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__transactionFilterTextKeyTyped
   if( '\n' == evt.getKeyChar() )
   {
      _transactionFilterButton.doClick();
   }
}//GEN-LAST:event__transactionFilterTextKeyTyped

private void _transactionFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__transactionFilterButtonActionPerformed
   RowFilter<TransactionTableModel, Object> rf = RowFilter.regexFilter(_transactionFilterText.getText(), 0, 1, 2, 3, 4, 5, 6 );
   _transactionSorter.setRowFilter( rf );
}//GEN-LAST:event__transactionFilterButtonActionPerformed

private void _transactionFilterDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__transactionFilterDateButtonActionPerformed
   RowFilter<TransactionTableModel, Object> rf = new RowFilter<TransactionTableModel, Object>()
   {
      public boolean include(Entry <? extends TransactionTableModel, ? extends Object> entry)
      {
         DateMidnight from = (new DateTime(_transactionFromDateChooser.getDate())).toDateMidnight();
         DateMidnight to = (new DateTime(_transactionToDateChooser.getDate())).toDateMidnight();
         Object o = entry.getValue(6);
         if(o == null)
         {
            return false;
         }
         else
         {
            DateMidnight c = (new DateTime((Date)o)).toDateMidnight();
            if(c.isBefore(from) || c.isAfter(to))
            {
               return false;
            }
         }

         return true;
      }

   };
   _transactionSorter.setRowFilter( rf );
}//GEN-LAST:event__transactionFilterDateButtonActionPerformed

private void _transactionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__transactionMenuItemActionPerformed
   _departmentTab.setSelectedIndex( 2 );
   String s = (String) JOptionPane.showInputDialog( getFrame(), 
           "Scan or Enter the Transaction Item Code", 
           "Scan Transaction Code", 
           JOptionPane.PLAIN_MESSAGE, 
           null, 
           null, 
           "Drawer Open");
   _transactionTableModel.addRow();
   int row = _transactionTable.getRowCount() - 1;
   _transactionTable.setRowSelectionInterval( row, row );
   _transactionTableModel.setValueAt( s, row, 1 );
}//GEN-LAST:event__transactionMenuItemActionPerformed

private void _accountingReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__accountingReportMenuItemActionPerformed
   _departmentTab.setSelectedIndex( 3 );
   _reportCalcButton.doClick();
}//GEN-LAST:event__accountingReportMenuItemActionPerformed

private void _reportCalcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__reportCalcButtonActionPerformed
   _transactionTable.selectAll();
   _reportTableModel.recalculate( _transactionTable.getSelectedRows() );
   _transactionTable.clearSelection();
}//GEN-LAST:event__reportCalcButtonActionPerformed

private void _saveReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__saveReportButtonActionPerformed
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Ultra Accounting Report File", "csv");
   if(_reportFileChooser == null)
   {
      _reportFileChooser = new JFileChooser();
      _reportFileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
      _reportFileChooser.setApproveButtonText( "Save" );
   }
   _reportFileChooser.setFileFilter(filter);

	int returnVal = _reportFileChooser.showOpenDialog(getFrame());
   if (returnVal == JFileChooser.APPROVE_OPTION) 
	{
      File file = _reportFileChooser.getSelectedFile();
      if(file != null)
      {
         System.out.println( "will save report to: " + file);
         BufferedWriter bufferedWriter = null;
         try
         {
            bufferedWriter = new BufferedWriter( new FileWriter( file.toString() ) );
            ReportTableDAO reportDAO = new ReportTableDAO(_reportTableModel);
            reportDAO.persistData( bufferedWriter );
            bufferedWriter.flush();
            bufferedWriter.close();
         }
         catch( IOException e )
         {
            System.err.println( "IOException writing file: " + e );
         }
      }
   } 
}//GEN-LAST:event__saveReportButtonActionPerformed

private void _saveReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__saveReportMenuItemActionPerformed
   _saveReportButton.doClick();
}//GEN-LAST:event__saveReportMenuItemActionPerformed

private void printSystemProperties()
{
   Properties props = System.getProperties();
   props.list( System.out );
}

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JMenuItem _accountingReportMenuItem;
   private javax.swing.JMenu _actionMenu;
   private javax.swing.JButton _addDepartmentRowButton;
   private javax.swing.JMenu _audioMenu;
   private javax.swing.JCheckBoxMenuItem _audioOnCheckBox;
   private javax.swing.JButton _deleteDepartmentRowButton;
   private javax.swing.JPanel _departmentDateChooserPanel;
   private javax.swing.JButton _departmentFilterButton;
   private javax.swing.JTextField _departmentFilterText;
   private javax.swing.JTabbedPane _departmentTab;
   private javax.swing.JTable _departmentTable;
   private javax.swing.JMenuItem _exportDatabaseMenuItem;
   private javax.swing.JMenuItem _importDatabaseMenuItem;
   private javax.swing.JButton _inventoryAddRowButton;
   private javax.swing.JPanel _inventoryDateChooserPanel;
   private javax.swing.JButton _inventoryDeleteRowButton;
   private javax.swing.JButton _inventoryFilterDateButton;
   private javax.swing.JTextField _inventoryFilterText;
   private javax.swing.JTable _inventoryTable;
   private javax.swing.JButton _iventoryFilterButton;
   private javax.swing.JButton _reportCalcButton;
   private javax.swing.JTable _reportTable;
   private javax.swing.JMenuItem _saveDatabaseMenuItem;
   private javax.swing.JButton _saveReportButton;
   private javax.swing.JMenuItem _saveReportMenuItem;
   private javax.swing.JButton _transactionAddRowButton;
   private javax.swing.JPanel _transactionDateChooserPanel;
   private javax.swing.JButton _transactionDeleteRowButton;
   private javax.swing.JButton _transactionFilterButton;
   private javax.swing.JButton _transactionFilterDateButton;
   private javax.swing.JTextField _transactionFilterText;
   private javax.swing.JMenuItem _transactionMenuItem;
   private javax.swing.JTable _transactionTable;
   private javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel10;
   private javax.swing.JLabel jLabel11;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel5;
   private javax.swing.JPanel jPanel6;
   private javax.swing.JPanel jPanel7;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JScrollPane jScrollPane4;
   private javax.swing.JPopupMenu.Separator jSeparator1;
   private javax.swing.JPanel mainPanel;
   private javax.swing.JMenuBar menuBar;
   private javax.swing.JProgressBar progressBar;
   private javax.swing.JLabel statusAnimationLabel;
   private javax.swing.JLabel statusMessageLabel;
   private javax.swing.JPanel statusPanel;
   // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private JFileChooser _exportDatabaseChooser;
    private JFileChooser _reportFileChooser;
    // department tables
    private DepartmentTableModel _departmentTableModel;
    private TableRowSorter _departmentSorter;
    private JDateChooser _departmentFromDateChooser;
    private JDateChooser _departmentToDateChooser;
    // inventory tables
    private InventoryTableModel _inventoryTableModel;
    private TableRowSorter _inventorySorter;
    private JDateChooser _inventoryFromDateChooser;
    private JDateChooser _inventoryToDateChooser;
    // transaction tables
    private TransactionTableModel _transactionTableModel;
    private TableRowSorter _transactionSorter;
    private JDateChooser _transactionFromDateChooser;
    private JDateChooser _transactionToDateChooser;
    // report tables
    private ReportTableModel _reportTableModel;

    private CommandDispatcher _commandDispatcher;
    private String            _identifier;

}