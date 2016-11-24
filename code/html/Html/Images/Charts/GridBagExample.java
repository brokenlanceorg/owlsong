import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.print.*;

/** An example of a printable window in Java 1.2. The key point
 *  here is that <B>any</B> component is printable in Java 1.2.
 *  However, you have to be careful to turn off double buffering
 *  globally (not just for the top-level window).
 *  See the PrintUtilities class for the printComponent method
 *  that lets you print an arbitrary component with a single
 *  function call.
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class GridBagExample extends JFrame
{
  JSlider theSlider = null;
  JCheckBox theCheckBox = null;
  JComboBox theDropDown = null;

  protected void initializeConstraint( GridBagConstraints c )
	  {
     c.gridx = 0; c.gridy = 0;
     c.weightx = 0; c.weighty = 0;
     c.gridwidth = 1; c.gridheight = 1;
     c.fill = GridBagConstraints.NONE;
     c.insets = new Insets( 0, 0, 1, 1 );
     c.anchor = GridBagConstraints.CENTER;
  } // end GridBagConstaints

  public static void main(String[] args) {
    new GridBagExample();
  }

  public GridBagExample() {
    super("Printing Swing Components");
    //WindowUtilities.setNativeLookAndFeel();

    Container theContent = getContentPane();

    GridBagLayout theLayout = new GridBagLayout();
    GridBagConstraints theConst = new GridBagConstraints();

    JPanel theScreen = new JPanel();
    theScreen.setLayout( theLayout );

    theConst.gridx = 5; theConst.gridy = 5;

    JButton theButton = new JButton( "Close" );
    theButton.addActionListener( new ActionListener() {
       public void actionPerformed( ActionEvent e ) {
          System.out.println( "The value is: " + theSlider.getValue() );
          System.out.println( "The check box value is: " + theCheckBox.isSelected() );
          System.out.println( "The drop down value is: " + theDropDown.getSelectedItem().toString() );
          System.exit( 0 ); }
    } );

    theLayout.setConstraints( theButton, theConst );
    theScreen.add( theButton );

    
    JLabel text1 = new JLabel( "Text Field 1:" );
    initializeConstraint( theConst );
    theConst.gridx = 0; theConst.gridy = 0;
    theLayout.setConstraints( text1, theConst );
    theScreen.add( text1 );

    JTextField textField1 = new JTextField();
    initializeConstraint( theConst );
    theConst.gridy = GridBagConstraints.RELATIVE;
    theConst.fill = GridBagConstraints.HORIZONTAL;
    theLayout.setConstraints( textField1, theConst );
    theScreen.add( textField1 );

    JLabel text2 = new JLabel( "Text Field 2:" );
    initializeConstraint( theConst );
    theConst.gridx = 2;
    theConst.gridwidth = GridBagConstraints.REMAINDER;
    theLayout.setConstraints( text2, theConst );
    theScreen.add( text2 );

    JTextField textField2 = new JTextField();
    initializeConstraint( theConst );
    theConst.gridx = 2;
    theConst.gridy = GridBagConstraints.RELATIVE;
    theConst.gridwidth = GridBagConstraints.REMAINDER;
    theConst.fill = GridBagConstraints.HORIZONTAL;
    theLayout.setConstraints( textField2, theConst );
    theScreen.add( textField2 );

    JLabel text3 = new JLabel( "Text Field 3:" );
    initializeConstraint( theConst );
    theConst.gridx = 0;
    theConst.gridy = GridBagConstraints.RELATIVE;
    theLayout.setConstraints( text3, theConst );
    theScreen.add( text3 );

    JTextArea textArea1 = new JTextArea();
    initializeConstraint( theConst );
//    theConst.gridx = 0;
    theConst.gridy = GridBagConstraints.RELATIVE;
    theConst.gridwidth = GridBagConstraints.REMAINDER;
    theConst.fill = GridBagConstraints.HORIZONTAL;
    theLayout.setConstraints( textArea1, theConst );
    theScreen.add( textArea1 );

    theCheckBox = new JCheckBox();
    initializeConstraint( theConst );
    theConst.gridx = 0;
    theConst.gridy = GridBagConstraints.RELATIVE;
    theLayout.setConstraints( theCheckBox, theConst );
    theScreen.add( theCheckBox );
    

    theSlider = new JSlider();
    initializeConstraint( theConst );
//    theSlider.setExtent( 50 );
    theSlider.setMaximum( 200 );
    theSlider.putClientProperty( "JSlider.isFilled", Boolean.TRUE );
    theSlider.setPaintLabels( true );
    theSlider.setPaintTicks( true );
    theSlider.setMinorTickSpacing( 5 );
    theSlider.setMajorTickSpacing( 40 );
//    theSlider.re
    theConst.gridx = 0;
    theConst.gridy = 5;
    theLayout.setConstraints( theSlider, theConst );
    theScreen.add( theSlider );

    theDropDown = new JComboBox();
    theDropDown.addItem( "One" );
    theDropDown.addItem( "Two" );
    theDropDown.addItem( "Three" );
    theDropDown.setEditable( false );
    initializeConstraint( theConst );
    theConst.gridx = 0;
    theConst.gridy = 5;
    theLayout.setConstraints( theDropDown, theConst );
    theScreen.add( theDropDown );

    theContent.add( theScreen, BorderLayout.CENTER );
    theContent.add( theSlider, BorderLayout.SOUTH );
    pack();
    setVisible( true );

  } // end constructor

} // end class GridBagExample