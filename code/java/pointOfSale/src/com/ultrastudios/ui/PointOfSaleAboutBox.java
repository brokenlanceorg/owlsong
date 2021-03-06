/*
 * PointOfSaleAboutBox.java
 */

package com.ultrastudios.ui;

import org.jdesktop.application.Action;

public class PointOfSaleAboutBox extends javax.swing.JDialog {

    public PointOfSaleAboutBox(java.awt.Frame parent, String id ) {
        super(parent);
        setIdentifier( id );
        initComponents();
        getRootPane().setDefaultButton(closeButton);
    }

    @Action public void closeAboutBox() {
        dispose();
    }

    public void setIdentifier( String id )
    {
       _identifier = id;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jLayeredPane1 = new javax.swing.JLayeredPane();
      javax.swing.JLabel imageLabel = new javax.swing.JLabel();
      javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
      javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
      javax.swing.JLabel appVendorLabel = new javax.swing.JLabel();
      javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
      javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
      closeButton = new javax.swing.JButton();
      javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
      javax.swing.JLabel versionLabel = new javax.swing.JLabel();
      javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.ultrastudios.ui.PointOfSale.class).getContext().getResourceMap(PointOfSaleAboutBox.class);
      setTitle(resourceMap.getString("title")); // NOI18N
      setModal(true);
      setName("aboutBox"); // NOI18N
      setResizable(false);

      jLayeredPane1.setName("jLayeredPane1"); // NOI18N

      imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon")); // NOI18N
      imageLabel.setName("imageLabel"); // NOI18N
      imageLabel.setBounds(0, 0, 398, 190);
      jLayeredPane1.add(imageLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

      vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
      vendorLabel.setText(resourceMap.getString("vendorLabel.text")); // NOI18N
      vendorLabel.setName("vendorLabel"); // NOI18N
      vendorLabel.setBounds(50, 140, 120, 14);
      jLayeredPane1.add(vendorLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
      homepageLabel.setText(resourceMap.getString("homepageLabel.text")); // NOI18N
      homepageLabel.setName("homepageLabel"); // NOI18N
      homepageLabel.setBounds(50, 160, 120, 14);
      jLayeredPane1.add(homepageLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      appVendorLabel.setText(resourceMap.getString("Application.vendor")); // NOI18N
      appVendorLabel.setName("appVendorLabel"); // NOI18N
      appVendorLabel.setBounds(190, 140, 180, 14);
      jLayeredPane1.add(appVendorLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      appHomepageLabel.setText(_identifier);
      appHomepageLabel.setName("appHomepageLabel"); // NOI18N
      appHomepageLabel.setBounds(190, 160, 190, 14);
      jLayeredPane1.add(appHomepageLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      appDescLabel.setText(resourceMap.getString("appDescLabel.text")); // NOI18N
      appDescLabel.setName("appDescLabel"); // NOI18N
      appDescLabel.setBounds(50, 70, 292, 50);
      jLayeredPane1.add(appDescLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.ultrastudios.ui.PointOfSale.class).getContext().getActionMap(PointOfSaleAboutBox.class, this);
      closeButton.setAction(actionMap.get("closeAboutBox")); // NOI18N
      closeButton.setName("closeButton"); // NOI18N
      closeButton.setBounds(130, 50, 140, 20);
      jLayeredPane1.add(closeButton, javax.swing.JLayeredPane.PALETTE_LAYER);

      appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+6));
      appTitleLabel.setText(resourceMap.getString("Application.title")); // NOI18N
      appTitleLabel.setName("appTitleLabel"); // NOI18N
      appTitleLabel.setBounds(30, 0, 360, 50);
      jLayeredPane1.add(appTitleLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
      versionLabel.setText(resourceMap.getString("versionLabel.text")); // NOI18N
      versionLabel.setName("versionLabel"); // NOI18N
      versionLabel.setBounds(50, 120, 120, 14);
      jLayeredPane1.add(versionLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      appVersionLabel.setText(resourceMap.getString("Application.version")); // NOI18N
      appVersionLabel.setName("appVersionLabel"); // NOI18N
      appVersionLabel.setBounds(190, 120, 50, 14);
      jLayeredPane1.add(appVersionLabel, javax.swing.JLayeredPane.PALETTE_LAYER);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents
    
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton closeButton;
   private javax.swing.JLayeredPane jLayeredPane1;
   // End of variables declaration//GEN-END:variables
   private String _identifier;    
}
